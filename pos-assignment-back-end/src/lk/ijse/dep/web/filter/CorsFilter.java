/*
 * Copyright (c) 2020
 * @ uvin6667
 *
 */

package lk.ijse.dep.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName="CorsFilter", urlPatterns = "/*")

public class CorsFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        response.addHeader("Access-Control-Allow-Headers","Content-Type");
        response.addHeader("Access-Control-Allow-Methods","POST,PUT,GET,DELETE");
        chain.doFilter(request, response);
    }
}
