package la_teca_del_giardiniere.classes; 

import la_teca_del_giardiniere.classes.Utente; // Assicurati l'import corretto

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/admin/*") // Intercetta TUTTE le richieste che iniziano con /admin/
public class AdminSecurityFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AdminSecurityFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro, se necessaria
        LOGGER.info("AdminSecurityFilter inizializzato.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String contextPath = httpRequest.getContextPath();

        // Check di Autenticazione
        if (session == null || session.getAttribute("currentUser") == null) {
            LOGGER.warning("Accesso non autenticato a risorsa admin: " + httpRequest.getRequestURI());
            httpResponse.sendRedirect(contextPath + "/Login.jsp?error=unauthenticated");
            return; // Ferma la catena di filtri e non prosegue
        }

        Utente currentUser = (Utente) session.getAttribute("currentUser");

        // Check di Autorizzazione (Utente deve essere Admin)
        if (currentUser == null || !currentUser.isAdmin()) {
            LOGGER.warning("Accesso non autorizzato a risorsa admin. Utente: " + (currentUser != null ? currentUser.getEmail() : "Nessuno") + ", URI: " + httpRequest.getRequestURI());
            // In questo caso, puoi reindirizzare al login con un messaggio specifico
            // o a una pagina di "accesso negato"
            session.setAttribute("messaggioErroreLogin", "Accesso negato: non hai i permessi di amministratore.");
            session.setAttribute("tipoMessaggio", "error");
            httpResponse.sendRedirect(contextPath + "/Login.jsp");
            return; // Ferma la catena
        }

        // Se tutto è OK, prosegue con la richiesta (verso la Servlet o la JSP)
        LOGGER.info("Accesso consentito a risorsa admin per " + currentUser.getEmail() + ": " + httpRequest.getRequestURI());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Pulizia del filtro, se necessaria
        LOGGER.info("AdminSecurityFilter distrutto.");
    }
}