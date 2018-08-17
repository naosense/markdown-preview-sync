package com.pingao.utils;

import com.pingao.enums.MiMeType;
import com.pingao.model.YAMLSpan;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.util.*;


/**
 * Created by pingao on 2018/7/13.
 */
public class HtmlUtils {
    private static final List<Extension> EXTENSIONS =
        Collections.singletonList(TablesExtension.create());
    private static final Parser PARSER = Parser.builder().extensions(EXTENSIONS).build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTENSIONS).build();

    private static final String MARKER = "_markdown_preview_sync_bottom_marker";

    private static final String MARKER_HTML = "<a href='#' id='" + MARKER + "'></a>";

    private HtmlUtils() {
    }

    public static Map<String, String> getParameters(FullHttpRequest request) {
        Map<String, String> paramMap = new HashMap<>();

        HttpMethod method = request.method();

        if (HttpMethod.GET == method) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().forEach((key, value) -> paramMap.put(key, value.get(0)));
        } else if (HttpMethod.POST == method) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            decoder.offer(request);

            List<InterfaceHttpData> params = decoder.getBodyHttpDatas();

            for (InterfaceHttpData param : params) {
                Attribute data = (Attribute) param;
                try {
                    paramMap.put(data.getName(), data.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            decoder.destroy();
            //
        } else {
            throw new IllegalStateException();
        }

        return paramMap;
    }

    public static String getRequestPath(String uri) {
        return uri.contains("?") ? uri.substring(0, uri.indexOf('?')) : uri;
    }

    public static MiMeType getMiMeTypeByPath(String path) {
        if (path.endsWith(".js")) {
            return MiMeType.JAVA_SCRIPT;
        } else if (path.endsWith(".css")) {
            return MiMeType.CSS;
        } else if (path.endsWith(".jpg")) {
            return MiMeType.JPEG;
        } else if (path.endsWith(".png")) {
            return MiMeType.PNG;
        } else {
            return MiMeType.PLAIN;
        }
    }

    public static String markdown2Html(String markdown) {
        Node document = PARSER.parse(markdown);
        String html = RENDERER.render(document);
        int index = html.indexOf(MARKER);
        if (index >= 0) {
            return html.substring(0, index) + MARKER_HTML + html.substring(index + MARKER.length());
        } else {
            return html;
        }
    }

    public static String plugBottomMarker(String markdown, int bottom) {
        YAMLSpan span = scanYAML(markdown);
        if (bottom <= span.getEndRow()) {
            bottom = span.getEndRow() + 1;
        }

        int nu = 0;
        String line;
        for (int i = 0, start = 0; i < markdown.length(); i++) {
            if (i == markdown.length() - 1) {
                if (markdown.charAt(i) == '\n') {
                    line = markdown.substring(start, i);
                } else {
                    line = markdown.substring(start, i + 1);
                }

                nu++;

                return markdown.substring(span.getEndInd() + 1) + (isCommentOrBlock(line) ? '\n' + MARKER : MARKER);
                //
            } else {
                if (markdown.charAt(i) == '\n') {
                    if (start == i) {
                        line = "";
                    } else {
                        line = markdown.substring(start, i);
                    }

                    start = i + 1;

                    nu++;

                    if (nu == bottom) {
                        if ("".equals(line.trim()) || isCommentOrBlock(line)) {
                            bottom++;
                        } else {
                            return markdown.substring(span.getEndInd() + 1, i) + MARKER + markdown.substring(i);
                        }
                    }
                }
            }
        }

        return markdown;
    }

    private static YAMLSpan scanYAML(String markdown) {
        YAMLSpan range = new YAMLSpan();
        int count = 0;
        int row = 0;
        if (markdown.startsWith("---\n")) {
            row++;
            for (int i = 4; i < markdown.length(); i++) {
                if (markdown.charAt(i) == '-') {
                    count++;
                } else if (markdown.charAt(i) == '\n') {
                    row++;

                    if (count == 3) {
                        range.setEndInd(i);
                        range.setEndRow(row);
                        return range;
                    }
                } else {
                    if (count > 0) {
                        count = 0;
                    }
                }
            }
        }

        return range;
    }

    public static List<String> split2Lines(String content) {
        List<String> lines = new ArrayList<>();
        for (int i = 0, start = 0; i < content.length(); i++) {
            if (i == content.length() - 1) {
                if (content.charAt(i) == '\n') {
                    lines.add(content.substring(start, i));
                } else {
                    lines.add(content.substring(start, i + 1));
                }
                //
            } else {
                if (content.charAt(i) == '\n') {
                    if (start == i) {
                        lines.add("");
                    } else {
                        lines.add(content.substring(start, i));
                    }

                    start = i + 1;
                }
            }
        }
        return lines;
    }

    private static boolean isCommentOrBlock(String line) {
        String l = line.trim();
        return l.startsWith("<!--")
               || l.startsWith("```")
               || l.startsWith("$$")
               || l.startsWith("---");
    }
}
