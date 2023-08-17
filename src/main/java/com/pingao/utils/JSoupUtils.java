package com.pingao.utils;

import com.pingao.enums.Operate;
import com.pingao.model.MarkDownUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by pingao on 2018/7/21.
 */
public class JSoupUtils {
    private static final Map<String, List<String>> HTML_CACHE = new HashMap<>();
    private static final Map<String, String> TOC_CACHE = new HashMap<>();

    private static final List<String> HEADING_TAGS = Arrays.asList("h1", "h2", "h3", "h4", "h5", "h6");

    // Prevent instance
    public JSoupUtils() {
    }

    public static List<MarkDownUnit> diff(String path, String html) {
        List<String> oldHtml = HTML_CACHE.get(path);

        Document document = Jsoup.parse(html);
        Elements elements = document.body().children();

        List<MarkDownUnit> units = new ArrayList<>(elements.size());

        if (oldHtml == null) {
            oldHtml = new ArrayList<>(elements.size());
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                transformLocalImgSrc(element, path);
                String id = "element-" + i;
                element.attr("id", id);
                String content = element.outerHtml();
                oldHtml.add(content);
                units.add(new MarkDownUnit(id, Operate.APPEND, content, isMathJax(content)));
            }
            HTML_CACHE.put(path, oldHtml);
        } else {
            int min = Math.min(oldHtml.size(), elements.size());
            int max = Math.max(oldHtml.size(), elements.size());

            for (int i = 0; i < min; i++) {
                Element element = elements.get(i);
                transformLocalImgSrc(element, path);
                String id = "element-" + i;
                element.attr("id", id);
                String content = element.outerHtml();
                if (!oldHtml.get(i).equals(content)) {
                    oldHtml.set(i, content);
                    units.add(new MarkDownUnit(id, Operate.REPLACE, content, isMathJax(content)));
                }
            }

            if (oldHtml.size() > elements.size()) {
                for (int i = max - 1; i >= min; i--) {
                    oldHtml.remove(i);
                    String id = "element-" + i;
                    units.add(new MarkDownUnit(id, Operate.REMOVE, "", 0));
                }
            } else {
                for (int i = min; i < max; i++) {
                    Element element = elements.get(i);
                    transformLocalImgSrc(element, path);
                    String id = "element-" + i;
                    element.attr("id", id);
                    String content = element.outerHtml();
                    oldHtml.add(content);
                    units.add(new MarkDownUnit(id, Operate.APPEND, content, isMathJax(content)));
                }
            }
        }

        if (isTocEnable(elements)) {
            String oldToc = TOC_CACHE.get(path);
            String newToc = buildToc(elements);
            if (oldToc == null) {
                units.add(new MarkDownUnit("toc_container", Operate.REPLACE, newToc, 0));
                units.get(0).setContent("<p id='element-0' style='display: none'>[TOC]</p>");
            } else {
                if (!oldToc.equals(newToc)) {
                    units.add(new MarkDownUnit("toc_container", Operate.REPLACE, newToc, 0));
                }
            }
            TOC_CACHE.put(path, newToc);
            //
        } else {
            String oldToc = TOC_CACHE.get(path);
            if (oldToc != null) {
                units.add(new MarkDownUnit("toc_container", Operate.REMOVE, "", 0));
                TOC_CACHE.remove(path);
            }
        }

        return units;
    }

    public static void removeCache(String path) {
        HTML_CACHE.remove(path);
        TOC_CACHE.remove(path);
    }

    private static String buildToc(Elements elements) {
        StringBuilder toc = new StringBuilder("<ul id='toc'>\n");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isHeading(element)) {
                String id = "element-" + i;
                int level = Integer.parseInt(element.tagName().substring(1));
                StringBuilder text = new StringBuilder();
                for (int j = 1; j < level; j++) {
                    text.append("&emsp;");
                }
                text.append(element.text());
                toc.append("<li><a href='#").append(id).append("'>").append(text).append("</a></li>\n");
            }
        }
        toc.append("</ul>");
        return toc.toString();
    }

    private static boolean isTocEnable(Elements elements) {
        return !elements.isEmpty() && elements.get(0).text().equals("[TOC]");
    }

    private static boolean isHeading(Element element) {
        return HEADING_TAGS.contains(element.tagName().toLowerCase());
    }

    private static void transformLocalImgSrc(Element element, String path) {
        Elements images = element.select("img");
        for (Element img : images) {
            String src = img.attr("src");
            try {
                if (!src.startsWith("http")) {
                    if (isWindows()) {
                        img.attr(
                            "src",
                            "/image?src=" + src.replace("\\", "\\\\") + "&path=" + URLEncoder.encode(
                                path,
                                "UTF-8"
                            )
                        );
                    } else {
                        img.attr(
                            "src",
                            "/image?src=" + src + "&path=" + URLEncoder.encode(
                                path,
                                "UTF-8"
                            )
                        );
                    }
                }
            } catch (UnsupportedEncodingException e) {
                Logger.error("Error occurs cause", e);
            }
        }
    }

    private static int isMathJax(String content) {
        int first = content.indexOf('$');
        return first > -1 && first != content.lastIndexOf('$') ? 1 : 0;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
