package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class GestioneTirocini extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_disattiva(HttpServletRequest request, HttpServletResponse response, int id_ot)
            throws ServletException, IOException, TemplateManagerException {
        try {
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().updateOffertaTirocinioAttiva(id_ot, false);
            response.sendRedirect("home");
        } catch (DataException ex) {
            request.setAttribute("exception", "Unable to update offerta tirocinio");
            action_error(request, response);
        }
    }
    
    private void action_attiva(HttpServletRequest request, HttpServletResponse response, int id_ot)
            throws ServletException, IOException, TemplateManagerException {
        try {
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().updateOffertaTirocinioAttiva(id_ot, true);
            response.sendRedirect("home");
        } catch (DataException ex) {
            request.setAttribute("exception", "Unable to update offerta tirocinio");
            action_error(request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
            }
            if(request.getParameter("action").equals("attiva")) {
                action_attiva(request, response, SecurityLayer.checkNumeric(request.getParameter("ot")));
            }
            else if(request.getParameter("action").equals("disattiva")) {
                action_disattiva(request, response, SecurityLayer.checkNumeric(request.getParameter("ot")));
            }
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Parametro errato");
            action_error(request, response);
        } catch (IOException | TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}