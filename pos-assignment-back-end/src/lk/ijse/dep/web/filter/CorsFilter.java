package lk.ijse.dep.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/*
@WebFilter(filterName="CorsFilter", urlPatterns = "/*")
*/

public class CorsFilter implements Filter {
    public void destroy(){

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("CORS filter incoming");
        chain.doFilter(req,resp);
        System.out.println("CORS filter outgoing");
    }

    public void init(FilterConfig config) throws ServletException{

    }
}
