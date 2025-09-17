package filtri;

import la_teca_del_giardiniere.classes.RigaCarrello;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebFilter(urlPatterns = {"*.jsp", "*.html", "/catalogo-piante", "/catalogo-accessori"}) // Apply to relevant pages/servlets
public class FiltroCarrello implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false); // Don't create a new session if one doesn't exist
        int numberOfItems = 0;

        if (session != null) {
            Object cartObject = session.getAttribute("carrello");
            if (cartObject instanceof Map) {
                Map<String, RigaCarrello> cart = (Map<String, RigaCarrello>) cartObject;
                for (RigaCarrello item : cart.values()) {
                    numberOfItems += item.getQuantita();
                }
            }
        }

        // Set the calculated number as a request attribute
        httpRequest.setAttribute("numeroArticoliCarrello", numberOfItems);

        // Continue to the next filter or servlet/JSP
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}