/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import org.apache.log4j.Logger;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Giuseppe Gasbarro
 */
public class DettagliTirocinio extends InternshipTutorBaseController{
    //logger
    final static Logger logger = Logger.getLogger(DettagliTirocinio.class);


    
    private void action_default(HttpServletRequest request, HttpServletResponse response, int id_tirocinio) throws DataException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        OffertaTirocinio tirocinio = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(id_tirocinio);
        if (tirocinio == null) {
            throw new DataException("Tirocinio non trovato");
        }
        request.setAttribute("tirocinio", tirocinio);
        res.activate("dettagli_tirocinio.ftl.html", request, response);
    }
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (SecurityLayer.checkNumericBool(request.getParameter("n"))) {
                int id_tirocinio = Integer.parseInt(request.getParameter("n"));
                HttpSession s = SecurityLayer.checkSession(request);
                if (s!= null) {
                    request.setAttribute("nome_utente", s.getAttribute("username"));
                    request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                }
                action_default(request, response, id_tirocinio);
            } else {
                logger.error("Impossibile trovare il tirocinio");
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Impossibile trovare il tirocinio");
                request.setAttribute("errore", "404 NOT FOUND");
                action_error(request, response);
                return;
            }
        } catch (DataException | TemplateManagerException ex) {
            logger.error("Exception: ", ex);
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "Impossibile trovare il tirocinio");
            request.setAttribute("errore", "404 NOT FOUND");
            action_error(request, response);
        }
    }
}
