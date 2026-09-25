package geek.tomcat.core;

import geek.tomcat.util.URLDecoder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 16:39
 */
@Setter
@Getter
public final class FilterMap {
    private String filterName = null;
    private String servletName = null;
    private String urlPattern = null;

    public void setURLPattern(String urlPattern) {
        this.urlPattern = URLDecoder.URLDecode(urlPattern);
    }
}
