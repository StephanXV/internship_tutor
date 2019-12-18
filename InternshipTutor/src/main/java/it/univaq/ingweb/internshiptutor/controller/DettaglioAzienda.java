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
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Enrico Monte
 */
public class DettaglioAzienda extends InternshipTutorBaseController {
    
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
                int id_azienda =  Integer.parseInt(request.getParameter("n"));
                HttpSession s = SecurityLayer.checkSession(request);
                if (s!= null) {
                    request.setAttribute("nome_utente", s.getAttribute("username"));
                    request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                }
                action_default(request, response, id_azienda);
            } else {
                logger.error("parametro azienda sbagliato, impossibile recuperare azienda");
                request.setAttribute("message", "Impossibile trovare l'azienda");
                action_error(request, response);
                return;
            }
        } catch (TemplateManagerException | DataException ex) {
            logger.error("Exception : ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, int id_azienda) throws TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());

        Azienda azienda = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_azienda);

        if (azienda != null && azienda.getStatoConvenzione() == 1) {
            List<OffertaTirocinio> tirocini = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOfferteTirocinio(azienda, true);

            // calcolo dei dati per le statistiche dell'azienda
            if (azienda.getValutazioni().size() > 0) {
                request.setAttribute("media_valutazioni", azienda.getMediaValutazioni(azienda.getValutazioni()));
            }


            request.setAttribute("tirocini", tirocini);
            request.setAttribute("azienda", azienda);
            request.setAttribute("page_title", "Azienda:" + azienda.getRagioneSociale());
            res.activate("dettaglio_azienda.ftl.html", request, response);
        } else {
            logger.error("parametro azienda sbagliato, impossibile recuperare azienda");
            request.setAttribute("message", "Impossibile trovare l'azienda");
                action_error(request, response);
            return;
        }
    }
    
    
}


