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
import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class Statistiche extends InternshipTutorBaseController {

    //logger
    final static Logger logger = Logger.getLogger(Statistiche.class);
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) {
        
        try {
            // prende le 5 aziende con più tirocinanti attivi
            List<Azienda> aziende = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAziendeByStato(1);
            aziende.forEach((a) -> {
                a.getTirocinantiAttivi();
            });
            aziende.sort(Comparator.comparing(Azienda::getTirocinantiAttivi).reversed());
            if (aziende.size() > 5) {
                aziende.subList(0, 5);
            }
            
            // prende i 5 tutori universatori più richiesti
            List<TutoreUni> tutori_uni = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().getTutori();
            tutori_uni.forEach((tu) -> {
                tu.getOccorrenze();
            });
            tutori_uni.sort(Comparator.comparing(TutoreUni::getOccorrenze).reversed());
            if (tutori_uni.size() > 5) {
                tutori_uni.subList(0, 5);
            }
            
            // prende le 5 offerte di tirocinio con più richieste totali
            List<OffertaTirocinio> ots = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getBestFiveOffertaTirocinio();
            
            // prende le 5 aziende con le valutazioni migliori
            List<Azienda> best_az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getBestFiveAziende();

            request.setAttribute("best_aziende", best_az);
            request.setAttribute("best_offerte", ots);
            request.setAttribute("tutori_uni", tutori_uni);
            request.setAttribute("aziende", aziende);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("statistiche.ftl.html", request, response);
        } catch (TemplateManagerException | DataException ex) {
            logger.error("Exception : ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        HttpSession s = SecurityLayer.checkSession(request);
        if (s!= null && "ad".equals((String)s.getAttribute("tipologia"))) {
            request.setAttribute("nome_utente", (String)s.getAttribute("username"));
            request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
            request.setAttribute("activeStat", "active");
            action_default(request, response);
        } else {
            logger.error("Utente non autorizzato");
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "Utente non autorizzato");
            request.setAttribute("errore", "401 Unauthorized");
            action_error(request, response);
        }
    }
}
