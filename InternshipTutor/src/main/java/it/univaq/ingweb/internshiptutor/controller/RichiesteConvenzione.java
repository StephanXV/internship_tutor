/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.framework.security.SecurityLayerException;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import org.apache.log4j.Logger;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class RichiesteConvenzione extends InternshipTutorBaseController {
    //logger
    final static Logger logger = Logger.getLogger(RichiesteConvenzione.class);
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_accetta(HttpServletRequest request, HttpServletResponse response, int id_azienda) throws IOException, SecurityLayerException, DataException {
            String src = SecurityLayer.issetString(request.getParameter("src"));
            Azienda az =  ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_azienda);
            az.setSrcDocConvenzione(src);
            az.setStatoConvenzione(1);
            if (1 == ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().updateAzienda(az)){
                response.sendRedirect("home");
            } else {
                throw new DataException("Impossibile convalidare la richiesta");
            }
    }
    
    private void action_rifiuta(HttpServletRequest request, HttpServletResponse response, int id_azienda) throws IOException, DataException {
            Azienda az =  ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_azienda);
            az.setStatoConvenzione(2);
            if (1 == ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().updateAzienda(az)){
                response.sendRedirect("home");
            } else {
                throw new DataException("Impossibile rifiutare la richiesta");
            }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null && "ad".equals((String)s.getAttribute("tipologia"))) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                int id_azienda = SecurityLayer.checkNumeric(request.getParameter("az"));
                if (request.getParameter("convalida").equals("si")) {
                    action_accetta(request, response, id_azienda);
                } else if  (request.getParameter("convalida").equals("no")) {
                    action_rifiuta(request, response, id_azienda);
                }
            } else {
                logger.error("UTENTE NON AUTORIZZATO");
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Utente non autorizzato");
                request.setAttribute("errore", "401 Unauthorized");
                action_error(request, response);
            }
        } catch (NumberFormatException | IOException | SecurityLayerException | DataException ex) {
            logger.error("Exception : ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}
