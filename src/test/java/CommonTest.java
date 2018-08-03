import com.google.gson.Gson;
import com.pingao.enums.Operate;
import com.pingao.model.MarkDownUnit;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


/**
 * Created by pingao on 2018/7/19.
 */
public class CommonTest {
    @Test
    public void testCommon() {
        List<String> list = Arrays.asList("a", "b");
        Assert.assertEquals(list.subList(0, 0).size(), 0);
    }

    @Test
    public void testSplitLines() {
    }

    @Test
    public void testHtml() {
        //System.out.println("aa".substring(0, 0).equals(""));
        //System.out.println(HtmlUtils.markdown2Html(Arrays.asList("", "", "  ", ""), 3));
        //System.out.println(HtmlUtils.markdown2Html(Arrays.asList("aaa", "", "  ", "bbb"), 3));
        //System.out.println(HtmlUtils.markdown2Html(Arrays.asList("aaa", "", "  ", ""), 5));
        //System.out.println(HtmlUtils.split2Lines(""));
        //System.out.println(HtmlUtils.split2Lines("\n"));
        //System.out.println(HtmlUtils.split2Lines("    \n"));
        //System.out.println(HtmlUtils.split2Lines("    \n\n"));
        //System.out.println(HtmlUtils.split2Lines("    \naaa"));
        //System.out.println(HtmlUtils.split2Lines("    \naaa\nbb"));
        //System.out.println(HtmlUtils.split2Lines("    \naaa\n\nbb"));
        //System.out.println(HtmlUtils.readIndexHtml());
        String md = "[TOC]\n"
                    + "\n"
                    + "作为Vim粉丝一枚，一直在寻找一款好用的Markdown预览插件，功能要简单，能在书写之余偶尔撇一下效果即可，毕竟Markdown语法都烂熟于心了。先是偶然看到了Chrome一款插件[Markdown Viewer](https://chrome.google.com/webstore/detail/markdown-viewer/ckkdlimhmcjmikdlpkmbgfkaikojcbjk)，风格非常喜欢，遗憾的是不能与Vim同步，后来又发现一款Vim插件[markdown-preview.vim](https://github.com/iamcco/markdown-preview.vim)，但是我装了好几次，在我的机器上老是报错。故事本来到此就结束了，但是程序猿乞肯如此轻易屈服，程序猿要让这天，再遮不住眼，要这地，再埋不了心，程序猿要用自己的双手开天辟地！经过程序猿在键盘上一顿猛干，[markdown-preview-sync](https://github.com/pingao777/markdown-preview-sync)横空出世了！好了，就吹到这吧。\n"
                    + "\n"
                    + "下面简单介绍下这款插件：\n"
                    + "\n"
                    + "### 特性\n"
                    + "\n"
                    + "- 代码高亮\n"
                    + "- MathJax\n"
                    + "- 自定义CSS\n"
                    + "\n"
                    + "### 安装准备\n"
                    + "\n"
                    + "- Jre8及以上\n"
                    + "- Python的[py4j](https://www.py4j.org/)库\n"
                    + "\n"
                    + "### 安装方式\n"
                    + "\n"
                    + "如果你使用[pathogen](https://github.com/tpope/vim-pathogen)，可以运行如下命令：\n"
                    + "\n"
                    + "```git\n"
                    + "cd your-path-to-bundle\n"
                    + "git clone git@github.com:pingao777/markdown-preview-sync.git\n"
                    + "```\n"
                    + "\n"
                    + "如果你没有使用任何插件管理工具，直接将/autoload和/plugin目录中的文件分别放在Vim的/autoload和/plugin目录即可。\n"
                    + "\n"
                    + "注：/src目录是项目源代码，如果不关注直接删掉就可以了。\n"
                    + "\n"
                    + "### 设置\n"
                    + "\n"
                    + "```vim\n"
                    + "\" Chrome和Firefox都可以，推荐使用Chrome\n"
                    + "\" 可以这样设置Chrome路径\n"
                    + "let g:markdown_preview_sync_chrome_path = \"\"\n"
                    + "\n"
                    + "\" 设置Firefox浏览器路径\n"
                    + "let g:markdown_preview_sync_firefox_path = \"\"\n"
                    + "\n"
                    + "\" (Optional)设置自定义CSS主题，将你的CSS文件放在autoload/java/webapp/css文件夹下，\n"
                    + "\" 以“主题名-theme.css”方式命名，然后设置如下变量\n"
                    + "let g:markdown_preview_sync_theme = \"主题名\"\n"
                    + "\n"
                    + "\" 配置快捷键\n"
                    + "autocmd filetype markdown nnoremap <F9> :MarkSyncPreview<cr>\n"
                    + "autocmd filetype markdown nnoremap <S-F9> :MarkSyncClose<cr>\n"
                    + "```\n"
                    + "\n"
                    + "最后，欢迎大家多多提意见和建议。\n"
                    + "\n"
                    + "---\n"
                    + "\n"
                    + "### Feature\n"
                    + "\n"
                    + "- Code Highlight\n"
                    + "- MathJax\n"
                    + "- Custom CSS\n"
                    + "\n"
                    + "### Prerequisite\n"
                    + "\n"
                    + "- Jre8 or above\n"
                    + "- Python [py4j](https://www.py4j.org/) lib\n"
                    + "\n"
                    + "### Installation\n"
                    + "\n"
                    + "If you use [pathogen](https://github.com/tpope/vim-pathogen), do this:\n"
                    + "\n"
                    + "```git\n"
                    + "cd your-path-to-bundle\n"
                    + "git clone git@github.com:pingao777/markdown-preview-sync.git\n"
                    + "```\n"
                    + "\n"
                    + "If you don't use any plugin manager, just copy files in /autoload and /plugin to Vim's /autoload and /plugin directory.\n"
                    + "\n"
                    + "Note: /src directory is source code, you can simply delete it if you don't need it.\n"
                    + "\n"
                    + "### Setting\n"
                    + "\n"
                    + "```vim\n"
                    + "\" Both Chrome and Firefox is good, but Chrome is prefer\n"
                    + "\" Set Chrome path\n"
                    + "let g:markdown_preview_sync_chrome_path = \"\"\n"
                    + "\n"
                    + "\" Set Firefox path\n"
                    + "let g:markdown_preview_sync_firefox_path = \"\"\n"
                    + "\n"
                    + "\" (Optional)Set your own css theme, put your css file in\n"
                    + "\" autoload/java/webapp/css directory with a name: name-theme.css,\n"
                    + "\" then set\n"
                    + "let g:markdown_preview_sync_theme = \"name\"\n"
                    + "\n"
                    + "\" Set key\n"
                    + "autocmd filetype markdown nnoremap <F9> :MarkSyncPreview<cr>\n"
                    + "autocmd filetype markdown nnoremap <S-F9> :MarkSyncClose<cr>\n"
                    + "```\n"
                    + "\n"
                    + "Last but not least, comments and issues are welcome.\n";
        //Document document = Jsoup.parse(html);
        //JsoupUtils.diff("ddd", html);
        //JsoupUtils.diff("ddd", html);
        //JsoupUtils.diff("ddd", html);
        //System.out.println(JsoupUtils.buildToc(document.body().children()));
        //System.out.println(Paths.get(ClassLoader.getSystemResource("").getFile()).getParent().toString());
        //System.out.println(ClassLoader.getSystemResource("").getPath().substring(1));
        //System.out.println(HtmlUtils.class.getResource("."));
        //System.out.println(Main.ROOT_PATH);
        //System.out.println(new Gson().toJson(new WebSocketMsg("command", "d:/path", "<a href='aaa'></a>")));
        //System.out.println(new GsonBuilder().disableHtmlEscaping().create().toJson(new WebSocketMsg("command", "d:/path", "<a href='aaa'></a>")));
        //System.out.println(Charset.defaultCharset());
        //System.out.println(HtmlUtils.readAsString(Main.ROOT_PATH + "/webapp/index.html"));
    }

    @Test
    public void testGson() {
        Assert.assertEquals("{\"id\":\"id\",\"operate\":\"APPEND\",\"content\":\"content\",\"isMathJax\":1}",
                            new Gson().toJson(new MarkDownUnit("id", Operate.APPEND, "content", 1)));
    }

    @Test
    public void testStringReplace() {
        String s = "addda";
        s.replace("a", "m");
        Assert.assertEquals("addda", s);
    }

    @Test
    public void testStringFormat() {
        String s = "addda";
        Assert.assertEquals("  addda", String.format("%7s", s));
    }
}
