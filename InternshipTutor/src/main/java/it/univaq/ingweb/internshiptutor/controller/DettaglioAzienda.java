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
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Giuseppe Gasbarro
 */
public class DettaglioAzienda extends InternshipTutorBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (SecurityLayer.checkNumericBool(request.getParameter("n"))) {
                int id_azienda = SecurityLayer.checkNumeric(request.getParameter("n"));
                HttpSession s = SecurityLayer.checkSession(request);
                if (s!= null) {
                    request.setAttribute("nome_utente", s.getAttribute("username"));
                    request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                }
                action_default(request, response, id_azienda);
            } else {
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Impossibile trovare l'azienda");
                request.setAttribute("errore", "404 NOT FOUND");
                action_error(request, response);
            }
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Parametro errato");
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, int id_azienda) {
        TemplateResult res = new TemplateResult(getServletContext());
        Azienda azienda;

        try {
            azienda = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_azienda);
            
            // calcolo dei dati per le statistiche dell'azienda
            if (azienda.getValutazioni().size() > 0) {
                System.out.println(azienda.getValutazioni());
                request.setAttribute("media_valutazioni", azienda.getMediaValutazioni(azienda.getValutazioni()));
            }
            List<OffertaTirocinio> tirocini = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOfferteTirocinio(azienda, true);
            request.setAttribute("tirocini", tirocini);
            request.setAttribute("azienda", azienda);
            request.setAttribute("page_title", "Azienda:" + azienda.getRagioneSociale());
            res.activate("dettaglio_azienda.ftl.html", request, response);
        } catch (NullPointerException | DataException | TemplateManagerException ex){
            Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "Impossibile trovare l'azienda");
            request.setAttribute("errore", "404 NOT FOUND");
            action_error(request, response);
        }  
    }
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            request.setAttribute("referrer", "dettaglio_azienda.ftl.html");
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
}


