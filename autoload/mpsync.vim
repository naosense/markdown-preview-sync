if !has("python") && !has("python3")
    echoerr "Vim has to be compiled with +python or +python3 to run this"
    finish
endif

if exists("g:markdown_preview_sync_loaded")
    finish
endif

if !exists("g:markdown_preview_sync_chrome_path") && !exists("g:markdown_preview_sync_firefox_path")
    echoerr "Not set browser path"
    finish
endif

let g:markdown_preview_sync_loaded = 1

if !exists("g:markdown_preview_sync_port")
    let g:markdown_preview_sync_port = 7788
endif

if !exists("g:markdown_preview_sync_log_level")
    let g:markdown_preview_sync_log_level = "warning"
endif

if !exists("g:markdown_preview_sync_theme")
    let g:markdown_preview_sync_theme = "github"
endif

function! s:using_py3()
  if has('python3')
    return 1
  endif
    return 0
endfunction

let s:using_python3 = s:using_py3()
let s:python_until_eof = s:using_python3 ? "python3 << EOF" : "python << EOF"
let s:python_command = s:using_python3 ? "py3 " : "py "

let s:plugin_root_dir = fnamemodify(resolve(expand("<sfile>:p")), ":h")

exec s:python_until_eof
import sys
from os.path import normpath, join
import vim
plugin_root_dir = vim.eval('s:plugin_root_dir')
python_root_dir = normpath(join(plugin_root_dir, 'python'))
sys.path.insert(0, python_root_dir)
import java_vim_bridge
EOF

function! s:start()
    if has("win32")
        execute 'silent !start /b java -jar -Dtinylog.level="' . g:markdown_preview_sync_log_level . '" "' . s:plugin_root_dir . '"/java/markdown-preview-sync.jar'
    else
        execute 'silent !java -jar -Dtinylog.level="' . g:markdown_preview_sync_log_level . '" "' . s:plugin_root_dir . '"/java/markdown-preview-sync.jar >/dev/null 2>&1 &'
    endif

exec s:python_until_eof
port = int(vim.eval('g:markdown_preview_sync_port'))
theme = vim.eval('g:markdown_preview_sync_theme')
java_vim_bridge.start(port, theme)
EOF
endfunction

function! s:is_start()
    return exists("g:markdown_preview_sync_start")
endfunction

function! s:open()
    if exists("g:markdown_preview_sync_chrome_path")
        if has("win32")
            execute "silent !start " . g:markdown_preview_sync_chrome_path . " --app=http://127.0.0.1:" . g:markdown_preview_sync_port . "/index"
        else
            execute "silent !\"" . g:markdown_preview_sync_chrome_path . "\" --app=http://127.0.0.1:" . g:markdown_preview_sync_port . "/index"
        endif
    elseif exists("g:markdown_preview_sync_firefox_path")
        if has("win32")
            execute "silent !start " . g:markdown_preview_sync_firefox_path . " http://127.0.0.1:" . g:markdown_preview_sync_port . "/index"
        else
            execute "silent !\"" . g:markdown_preview_sync_firefox_path . "\" http://127.0.0.1:" . g:markdown_preview_sync_port . "/index"
        endif
    endif

exec s:python_until_eof
path = vim.eval('expand("%:p")')
java_vim_bridge.open(path)
EOF
endfunction

function! s:sync()
exec s:python_until_eof
path = vim.eval('expand("%:p")')
content = vim.eval('join(getline(1, line("$")), "\n")')
current = int(vim.eval('line(".")'))
if current == 1:
    java_vim_bridge.sync(path, content, 1)
else:
    bottom = int(vim.eval('line("w$")'))
    java_vim_bridge.sync(path, content, bottom)
EOF
endfunction

function! s:close()
exec s:python_until_eof
path = vim.eval('expand("%:p")')
java_vim_bridge.close(path)
EOF
endfunction

function! s:stop()
exec s:python_until_eof
path = vim.eval('expand("%:p")')
java_vim_bridge.stop()
EOF
endfunction

function! s:autocmd()
    augroup markdown_preview_sync
        autocmd!
        autocmd cursormoved,cursormovedi <buffer> call s:trigger_sync()
        autocmd bufwrite <buffer> call s:sync()
        autocmd vimleave * call s:stop()
    augroup end
endfunction

function! s:trigger_sync()
    if !exists("b:old_current")
        let b:old_current = 0
    endif
    if !exists("b:old_bottom ")
        let b:old_bottom = 0
    endif
    if !exists("b:old_last ")
        let b:old_last = 0
    endif

    let l:new_current = line(".")
    let l:new_bottom = line("w$")
    let l:new_last = line("$")

    if b:old_current != l:new_current || b:old_bottom != l:new_bottom || b:old_last != l:new_last
        call s:sync()
        let b:old_current = l:new_current
        let b:old_bottom = l:new_bottom
        let b:old_last = l:new_last
    endif
endfunction

function! mpsync#preview()
    if !s:is_start()
        call s:start()
        let g:markdown_preview_sync_start = 1
    endif
    call s:open()
    call s:autocmd()
endfunction

function! mpsync#close()
    if s:is_start()
        call s:close()
    endif
endfunction
