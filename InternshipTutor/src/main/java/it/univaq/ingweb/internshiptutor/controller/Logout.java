package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.security.SecurityLayer;
import org.apache.log4j.Logger;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class Logout extends InternshipTutorBaseController {
    //logger
    final static Logger logger = Logger.getLogger(Logout.class);
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityLayer.disposeSession(request);
        //se è stato trasmesso un URL di origine, torniamo a quell'indirizzo
        if (request.getParameter("referrer") != null) {
            response.sendRedirect(request.getParameter("referrer"));
        } else { //altrimenti andiamo ad home
            response.sendRedirect("home");
        }
    }
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try{
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                action_logout(request, response);
            } else {
                logger.error("Utente non autorizzato, sessione vuota");
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Utente non autorizzato");
                request.setAttribute("errore", "401 Unauthorized");
                action_error(request, response);
            }
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
    }
}