package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.Valutazione;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class ValutazioneAzienda extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        int id_az = SecurityLayer.checkNumeric(request.getParameter("az"));
        int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
        try {
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_az);
            request.setAttribute("az", az);
            Valutazione valutazione = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getValutazioneDAO().getValutazione(id_az, id_st);
            if (valutazione != null) {
                System.out.println(valutazione);
                request.setAttribute("valutazione", valutazione);
            }
            request.setAttribute("id_az", id_az);
            request.setAttribute("id_st", id_st);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("valutazione.ftl.html", request, response);
        } catch (DataException ex) {
            Logger.getLogger(ValutazioneAzienda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action_valuta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
        int id_az = SecurityLayer.checkNumeric(request.getParameter("az"));
        
        try {
            Studente st = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(id_st);
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_az);
            Valutazione valutazione = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getValutazioneDAO().createValutazione();
            valutazione.setAzienda(az);
            valutazione.setStudente(st);
            valutazione.setStelle(SecurityLayer.checkNumeric(request.getParameter("rating")));
            int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getValutazioneDAO().insertValutazione(valutazione);
            if (insert != 1) {
                request.setAttribute("message", "errore_valutazione");
                request.setAttribute("errore", "I dati della valutazione non sono validi, riprova");
                action_error(request, response);
            }
            response.sendRedirect("home");
            
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
            }
            
            if (request.getParameter("submit") != null) {
                action_valuta(request, response);
            }
            else
                action_default(request, response);
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}