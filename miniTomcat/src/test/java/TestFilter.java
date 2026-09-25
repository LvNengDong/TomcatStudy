import javax.servlet.*;
import java.io.IOException;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 17:36
 */
public class TestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("The very first Filter");
        chain.doFilter(request, response);
    }
}
