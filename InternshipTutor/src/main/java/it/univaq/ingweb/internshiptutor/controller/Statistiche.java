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

import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author steph
 */
public class Statistiche extends InternshipTutorBaseController {
    
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
            for (Azienda a: aziende) {
                a.getTirocinantiAttivi();
            }
            aziende.sort(Comparator.comparing(Azienda::getTirocinantiAttivi).reversed());
            if (aziende.size() > 5)
                aziende.subList(0, 5);
            
            // prende i 5 tutori universatori più richiesti
            List<TutoreUni> tutori_uni = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().getTutori();
            for (TutoreUni tu: tutori_uni) {
                tu.getOccorrenze();
            }
            tutori_uni.sort(Comparator.comparing(TutoreUni::getOccorrenze).reversed());
            if (tutori_uni.size() > 5)
                tutori_uni.subList(0, 5);
            
            // prende le 5 offerte di tirocinio con più richieste totali
            List<OffertaTirocinio> ots = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getBestFiveOffertaTirocinio();
            
            // prende le 5 aziende con le valutazioni migliori
            List<Azienda> best_az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getBestFiveAziende();
            System.out.println(best_az);
            
            request.setAttribute("best_aziende", best_az);
            request.setAttribute("best_offerte", ots);
            request.setAttribute("tutori_uni", tutori_uni);
            request.setAttribute("aziende", aziende);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("statistiche.ftl.html", request, response);
        } catch (TemplateManagerException e) {
            request.setAttribute("exception", e);
            action_error(request, response);
        } catch (DataException ex) {
            Logger.getLogger(Statistiche.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        HttpSession s = SecurityLayer.checkSession(request);
        if (s!= null) {
            request.setAttribute("nome_utente", (String)s.getAttribute("username"));
            request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
        }
        
        request.setAttribute("activeStat", "active");
        action_default(request, response);
        
    }
    
}
