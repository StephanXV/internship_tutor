/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.framework.security.SecurityLayerException;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class RichiesteConvenzione extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_accetta(HttpServletRequest request, HttpServletResponse response, int id_azienda)
            throws IOException, ServletException, TemplateManagerException {
        try {
            String src = SecurityLayer.issetString(request.getParameter("src"));
            Azienda az =  ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_azienda);
            az.setSrcDocConvenzione(src);
            az.setStatoConvenzione(1);
            if (1 == ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().updateAzienda(az)){
                response.sendRedirect("home");
            } else {
                action_error(request, response);
            }
        } catch (DataException | SecurityLayerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
    private void action_rifiuta(HttpServletRequest request, HttpServletResponse response, int id_azienda)
            throws IOException, ServletException, TemplateManagerException {
        try {
            Azienda az =  ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_azienda);
            az.setStatoConvenzione(2);
            if (1 == ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().updateAzienda(az)){
                response.sendRedirect("home");
            } else {
                action_error(request, response);
            }
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
            }
            int id_azienda = SecurityLayer.checkNumeric(request.getParameter("az"));
            if (request.getParameter("convalida").equals("si")) {
                action_accetta(request, response, id_azienda);
            } else if  (request.getParameter("convalida").equals("no")) {
                action_rifiuta(request, response, id_azienda);
            }
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}
