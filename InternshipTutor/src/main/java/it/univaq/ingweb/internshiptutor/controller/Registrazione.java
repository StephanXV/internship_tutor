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
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            // controlli sul responsabile tirocini
            if (SecurityLayer.checkString(request.getParameter("nome_rt")) && SecurityLayer.checkString(request.getParameter("cognome_rt")) &&
                    SecurityLayer.checkEmail(request.getParameter("email_rt")) && SecurityLayer.checkTelefono(request.getParameter("telefono_rt"))){
                
                rt.setNome(request.getParameter("nome_rt"));
                rt.setCognome(request.getParameter("cognome_rt"));
                rt.setEmail(request.getParameter("email_rt"));
                rt.setTelefono(request.getParameter("telefono_rt"));
                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getRespTirociniDAO().insertRespTirocini(rt);
            }
            
            try {  
                Utente ut = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().createUtente();
                // controlli sull'utente
                if (SecurityLayer.checkString(request.getParameter("username")) && SecurityLayer.checkString(request.getParameter("pw")) &&
                        SecurityLayer.checkEmail(request.getParameter("email"))) {
                    
                    ut.setUsername(request.getParameter("username"));
                    ut.setPw(request.getParameter("pw"));
                    ut.setEmail(request.getParameter("email"));
                    ut.setTipologia("az");
                    ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().insertUtente(ut);
                }
                
                try {
                    Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().createAzienda();
                    // controlli sull'azienda
                    if (SecurityLayer.checkString(request.getParameter("ragione_sociale")) && SecurityLayer.checkString(request.getParameter("indirizzo")) &&
                            SecurityLayer.checkString(request.getParameter("citta")) && SecurityLayer.checkCap(request.getParameter("cap")) &&
                            SecurityLayer.checkString(request.getParameter("provincia")) && SecurityLayer.checkString(request.getParameter("rappresentante_legale")) &&
                            SecurityLayer.checkString(request.getParameter("piva")) && SecurityLayer.checkString(request.getParameter("foro_competente")) &&
                            SecurityLayer.checkString(request.getParameter("tematiche")) && SecurityLayer.checkString(request.getParameter("corso_studio")) &&
                            SecurityLayer.checkDurata(request.getParameter("durata"))) {
                        
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
                        az.setRespTirocini(rt);
                        az.setUtente(ut);
                        az.setDurataConvenzione(SecurityLayer.checkNumeric(request.getParameter("durata")));
                        ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().insertAzienda(az);
                    }  
                } catch (DataException ex) {
                    
                    // se fallisce l'inserimento dell'azienda, cancella l'utente e il responsabile inseriti prima
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getRespTirociniDAO().deleteRespTirocini(rt.getId());
                    ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().deleteUtente(ut.getId());
                }
            } catch (DataException ex) {
                
                // se fallisce l'inserimento dell'utente, cancella il responsabile inserito prima
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getRespTirociniDAO().deleteRespTirocini(rt.getId());
            }
            response.sendRedirect("home");    
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }    
    }
    
    private void action_registrazione_studente(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException, TemplateManagerException {
        try {
            Utente ut = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().createUtente();
                // controlli sull'utente
            if (SecurityLayer.checkString(request.getParameter("username")) && SecurityLayer.checkString(request.getParameter("pw")) &&
                    SecurityLayer.checkEmail(request.getParameter("email"))) {
                    
                ut.setUsername(request.getParameter("username"));
                ut.setPw(request.getParameter("pw"));
                ut.setEmail(request.getParameter("email"));
                ut.setTipologia("st");
                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().insertUtente(ut);
            }
            
            try {
                Studente st = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().createStudente();
                 // controlli sullo studente
                if (SecurityLayer.checkString(request.getParameter("nome")) && SecurityLayer.checkString(request.getParameter("cognome")) &&
                        SecurityLayer.checkString(request.getParameter("codice_fiscale")) && SecurityLayer.checkBoolean(request.getParameter("handicap")) &&
                        SecurityLayer.checkDateString(request.getParameter("data_nascita")) && SecurityLayer.checkString(request.getParameter("citta_nascita")) &&
                        SecurityLayer.checkString(request.getParameter("provincia_nascita")) && SecurityLayer.checkString(request.getParameter("citta_residenza")) &&
                        SecurityLayer.checkString(request.getParameter("cap_residenza")) && SecurityLayer.checkString(request.getParameter("provincia_residenza")) &&
                        SecurityLayer.checkString(request.getParameter("telefono")) && SecurityLayer.checkString(request.getParameter("corso_laurea")))  {
                    
                    st.setNome(request.getParameter("nome"));
                    st.setCognome(request.getParameter("cognome"));
                    st.setCF(request.getParameter("codice_fiscale"));
                    st.setDataNascita(SecurityLayer.checkDate(request.getParameter("data_nascita")));
                    st.setCittaNascita(request.getParameter("citta_nascita"));
                    st.setProvinciaNascita(request.getParameter("provincia_nascita"));
                    st.setCittaResidenza(request.getParameter("citta_residenza"));
                    st.setCapResidenza(request.getParameter("cap_residenza"));
                    st.setProvinciaResidenza(request.getParameter("provincia_residenza"));
                    st.setTelefono(request.getParameter("telefono"));
                    st.setCorsoLaurea(request.getParameter("corso_laurea"));
                    st.setHandicap(Boolean.valueOf(request.getParameter("handicap")));
                    st.setUtente(ut);
                    ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().insertStudente(st);
                }
            } catch (DataException ex) {
                // se fallisce l'inserimento dello studente, cancella l'utente inserito prima
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().deleteUtente(ut.getId());
            }
            
            response.sendRedirect("home");
            
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
        
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
