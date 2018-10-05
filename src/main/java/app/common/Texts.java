package app.common;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <br/>
 *
 * @author: landy
 * @date: 2016/08/25 16:35
 * note:
 */
public class Texts {
    public static boolean hasLength(String text) {
        return text != null && text.length() > 0;
    }

    public static boolean hasText(String text) {
        return text != null && text.trim().length() > 0;
    }
    public static String extraFileId(String url) {
        String tmpFile = null;
        int idx = url.lastIndexOf("fileId=");
        if (idx > -1) {
            tmpFile = url.substring(idx + 7);
        }
        return tmpFile;
    }
    public static String toString(Object o) {
        if (o == null) {
            return null;
        } else {
            return o.toString();
        }
    }

    /**
     * 修复url,增加http://前缀
     *
     * @param url
     * @return
     */
    public static URL fixUrl(String url) throws MalformedURLException {
        URL rst = null;
        try {
            rst = new URL(url);
        } catch (MalformedURLException e) {
            String lowerCase = url.toLowerCase();
            if (!lowerCase.startsWith("http:") && !lowerCase.startsWith("https:")) {
                rst = new URL("http://" + url);
            }
        }
        return rst;
    }

    public static String uuidLong() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String fixContextPath(String localContext) {
        if (!hasText(localContext)) {
            return "";
        }
        if (!localContext.startsWith("/") && !localContext.startsWith("\\")) {
            localContext = "/" + localContext;
        }
        return localContext;
    }

    public static String extractHost(String host) {
        if (host.toLowerCase().startsWith("http://")) {
            return host.substring("http://".length());
        }
        return host;
    }

    public static String dataSizeToHuman(long size) {
        StringBuilder sb = new StringBuilder();
        long kbUnit = 1024;
        long mbUnit = kbUnit * 1024;
        long gbUnit = mbUnit * 1024;
        long tbUnit = gbUnit * 1024;

        long remain = size;

        long tb = size / tbUnit;
        if (tb > 0) {
            sb.append(tb);
            sb.append("T ");
            remain -= tb * tbUnit;
        }
        long gb = remain / gbUnit;
        if (gb > 0) {
            sb.append(gb);
            sb.append("G ");
            remain -= gb * gbUnit;
        }
        long mb = remain / mbUnit;
        if (mb > 0) {
            sb.append(mb);
            sb.append("M ");
            remain -= mb * mbUnit;
        }
        long kb = remain / kbUnit;
        if (kb > 0) {
            sb.append(kb);
            sb.append("K ");
        }

        String result = sb.toString();
        if (!Texts.hasText(result)) {
            return "0";
        }
        return result.trim();
    }

    public static Long tryParse(String string) {
        if (string == null) {
            return null;
        } else {
            return Longs.tryParse(string);
        }
    }

    public static String getMessage(Throwable cause) {
        return cause == null ? "" : cause.getMessage();
    }

    public static String brief(String content, int len) {
        if (content.length() > len) {
            return content.substring(0, len - 3) + "...";
        } else {
            return content;
        }
    }

    public static <T> String joinCollection(Iterable<T> pics) {
        return Joiner.on(",").skipNulls().join(pics);
    }

    public static List<String> splitToList(String pics) {
        if (Texts.hasText(pics)) {
            return Splitter.on(",").omitEmptyStrings().splitToList(pics);
        } else {
            return Collections.emptyList();
        }
    }

    public static String cutLen(String longitude, int i) {
        if (longitude.length() <= i) {
            return longitude;
        } else {
            return longitude.substring(0, i);
        }
    }

    public static List<Long> splitToLongList(String text) {
        List<String> list = splitToList(text);
        List<Long> rst = Lists.newLinkedList();
        for (String one : list) {
            rst.add(Long.parseLong(one));
        }
        return rst;
    }

    public static String decodeUTF8(String fsTableData) throws UnsupportedEncodingException {
        return URLDecoder.decode(fsTableData, "UTF-8");
    }

    public static String convertARGBIntToCssRGBA(Integer strokeColor) {
        String argb = Strings.padStart(Integer.toHexString(strokeColor), 8, '0');
        String rgba = argb.substring(2) + argb.substring(0, 2);
        return '#' + rgba;
    }

    public static Integer convertCssRGBAToARGBInt(String strokeColor) {
        Assert.isTrue(strokeColor.startsWith("#"));
        String rgba = Strings.padEnd(strokeColor.substring(1), 8, 'F');
        String argb = rgba.substring(6) + rgba.substring(0, 6);
        return ((int) Long.parseLong(argb, 16));
    }

    public static String toLikeText(String text) {
        if (Texts.hasText(text)) {
            return '%' + text + '%';
        }
        return null;
    }

    public static String getUrlParam(String url, String key) {
        Pattern r = Pattern.compile("(\\?|&)?"+key+"=([^&]*)");
        Matcher m = r.matcher(url);
        String value = null;
        if (m.find()) {
            value =  m.group().replaceAll("&|" + key + "=|" + " ", "");
        }
        return value;
    }
}
