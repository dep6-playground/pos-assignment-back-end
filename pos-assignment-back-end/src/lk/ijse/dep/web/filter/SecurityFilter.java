/*
 * Copyright (c) 2020
 * @ uvin6667
 *
 */

package lk.ijse.dep.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName="SecurityFilter")
public class SecurityFilter implements Filter {


    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Security filter incoming");
        chain.doFilter(req,resp);
        System.out.println("Security filter outgoing");
    }


}
