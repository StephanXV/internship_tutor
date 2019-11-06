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
import it.univaq.ingweb.internshiptutor.data.model.RespTirocini;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stefano Florio
 */
public class Registrazione extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_azienda(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Tirocini");
        res.activate("registrazione_azienda.ftl.html", request, response);
    }
    
    private void action_studente(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Tirocini");
        res.activate("registrazione_studente.ftl.html", request, response);
    }
    
    private void action_registrazione_azienda(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException, TemplateManagerException {
        try {
            RespTirocini rt = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getRespTirociniDAO().createRespTirocini();
            rt.setNome(request.getParameter("nome_rt"));
            rt.setCognome(request.getParameter("cognome_rt"));
            rt.setEmail(request.getParameter("email_rt"));
            rt.setTelefono(request.getParameter("telefono_rt"));
            
            // controlli sul responsabile tirocini
            
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getRespTirociniDAO().insertRespTirocini(rt);

            Utente ut = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().createUtente();
            ut.setUsername(request.getParameter("username"));
            ut.setPw(request.getParameter("pw"));
            ut.setEmail(request.getParameter("email"));
            ut.setTipologia("az");
            
            // controlli sull'utente
            
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().insertUtente(ut);

            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().createAzienda();
            az.setRagioneSociale(request.getParameter("ragione_sociale"));
            az.setIndirizzo(request.getParameter("indirizzo"));
            az.setCitta(request.getParameter("citta"));
            az.setCap(request.getParameter("cap"));
            az.setProvincia(request.getParameter("provincia"));
            az.setRappresentanteLegale(request.getParameter("rappresentante_legale"));
            az.setPiva(request.getParameter("piva"));
            az.setForoCompetente(request.getParameter("foro_competente"));
            az.setTematiche(request.getParameter("tematiche"));
            az.setCorsoStudio(request.getParameter("corso_studio"));
            az.setDurataConvenzione(SecurityLayer.checkNumeric(request.getParameter("durata")));
            az.setRespTirocini(rt);
            az.setUtente(ut);
            
            // controlli sull'azienda
            
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().insertAzienda(az);
            
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("home_anonimo.ftl.html", request, response);    
            
            
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
        
        
        
    }
    
    private void action_registrazione_studente(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
    }
        
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
            if (request.getParameter("submit") != null) {
                if(request.getParameter("submit").equals("Registrati come azienda"))
                    action_registrazione_azienda(request, response);
                else if(request.getParameter("submit").equals("Registrati come studente"))
                        action_registrazione_studente(request, response);
            } else {
                    if (request.getParameter("tipo").equals("azienda"))
                        action_azienda(request, response);
                    else if (request.getParameter("tipo").equals("studente"))
                            action_studente(request, response);
                    }
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
    }

}
