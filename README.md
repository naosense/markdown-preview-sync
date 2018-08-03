[TOC]

作为Vim粉丝一枚，一直在寻找一款好用的Markdown预览插件，功能要简单，能在书写之余偶尔撇一下效果即可，毕竟Markdown语法都烂熟于心了。先是偶然看到了Chrome一款插件[Markdown Viewer](https://chrome.google.com/webstore/detail/markdown-viewer/ckkdlimhmcjmikdlpkmbgfkaikojcbjk)，风格非常喜欢，遗憾的是不能与Vim同步，后来又发现一款Vim插件[markdown-preview.vim](https://github.com/iamcco/markdown-preview.vim)，但是我装了好几次，在我的机器上老是报错。故事本来到此就结束了，但是程序猿乞肯如此轻易屈服，程序猿要让这天，再遮不住眼，要这地，再埋不了心，程序猿要用自己的双手开天辟地！经过程序猿在键盘上一顿猛干，[markdown-preview-sync](https://github.com/pingao777/markdown-preview-sync)横空出世了！好了，就吹到这吧。

运行效果如下：

![snapshot-en](http://ozgrgjwvp.bkt.clouddn.com/markdown-preview-sync/en.png)

![snapshot-ch](http://ozgrgjwvp.bkt.clouddn.com/markdown-preview-sync/ch.png)

下面简单介绍下这款插件：

### 特性

- 代码高亮
- MathJax
- 自定义CSS
- GFM-Table
- 目录TOC

### 安装准备

- Jre8及以上
- Vim支持python2或python3

### 安装方式

如果你使用[pathogen](https://github.com/tpope/vim-pathogen)，将release中的`markdown-preview-sync-v1.0.zip`解压到bundle文件夹即可。

如果你没有使用任何插件管理工具，解压release中的`markdown-preview-sync-v1.0.zip`，然后将/autoload和/plugin目录中的文件分别放在Vim的/autoload和/plugin目录即可。

注：/src目录是项目源代码，如果不关注直接删掉就可以了。

### 设置

```vim
" Chrome和Firefox都可以，推荐使用Chrome
" 可以这样设置Chrome路径
let g:markdown_preview_sync_chrome_path = ""

" 设置Firefox浏览器路径
let g:markdown_preview_sync_firefox_path = ""

" (Optional)设置自定义CSS主题，将你的CSS文件放在autoload/java/webapp/css文件夹下，
" 以“主题名-theme.css”方式命名，然后设置如下变量
let g:markdown_preview_sync_theme = "主题名"

" 配置快捷键
autocmd filetype markdown nnoremap <F9> :MarkSyncPreview<cr>
autocmd filetype markdown nnoremap <S-F9> :MarkSyncClose<cr>
```

最后，欢迎大家多多提意见和建议。

---

### Feature

- Code Highlight
- MathJax
- Custom CSS
- GFM-Table
- TOC

### Prerequisite

- Jre8 or above
- Vim with python2 or python3 support

### Installation

If you use [pathogen](https://github.com/tpope/vim-pathogen), do this: unzip release file `markdown-preview-sync-v1.0.zip` to bundle directory.

If you don't use any plugin manager, just unzip release file `markdown-preview-sync-v1.0.zip`, then copy files in /autoload and /plugin to Vim's /autoload and /plugin directory.

Note: /src directory is source code, you can simply delete it if you don't need it.

### Setting

```vim
" Both Chrome and Firefox are good, but Chrome is prefer
" Set Chrome path
let g:markdown_preview_sync_chrome_path = ""

" Set Firefox path
let g:markdown_preview_sync_firefox_path = ""

" (Optional)Set your own css theme, put your css file in
" autoload/java/webapp/css directory with a name: name-theme.css,
" then set
let g:markdown_preview_sync_theme = "name"

" Set key
autocmd filetype markdown nnoremap <F9> :MarkSyncPreview<cr>
autocmd filetype markdown nnoremap <S-F9> :MarkSyncClose<cr>
```

Last but not least, comments and issues are welcome.
