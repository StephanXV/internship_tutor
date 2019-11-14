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
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stefano Florio
 */
public class Registrazione extends InternshipTutorBaseController {

    BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
    private String TYPE = null;
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {

        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            request.setAttribute("referrer", "registrazione.ftl.html");
            //per vedere se tornare alla corrispondente pagina studente o azienda
            if (TYPE != null && TYPE.equals("STUDENT")) {
                request.setAttribute("activeStudente", "active");
                request.setAttribute("ariaStudente", "true");
                request.setAttribute("ariaAzienda", "false");
            } else if (TYPE != null && TYPE.equals("AZIENDA")) {
                request.setAttribute("activeAzienda", "active");
                request.setAttribute("ariaStudente", "false");
                request.setAttribute("ariaAzienda", "true");
            }

            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_open_reg(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Registrazione");
        res.activate("registrazione.ftl.html", request, response);
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
                int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getRespTirociniDAO().insertRespTirocini(rt);
                if (insert != 1) {
                    TYPE="AZIENDA";
                    request.setAttribute("message", "errore_convalida");
                    request.setAttribute("errore", "I campi del responsabile tirocini non sono corretti. Riprova!");
                    action_error(request, response);
                }
            } else {
                TYPE="AZIENDA";
                request.setAttribute("message", "errore_convalida");
                request.setAttribute("errore", "I campi del responsabile tirocini non sono corretti. Riprova!");
                action_error(request, response);
            }
            
            try {  
                Utente ut = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().createUtente();
                // controlli sull'utente
                if (SecurityLayer.checkString(request.getParameter("username")) && SecurityLayer.checkString(request.getParameter("pw")) &&
                        SecurityLayer.checkEmail(request.getParameter("email"))) {

                    /* encrypt pass */
                    String password = request.getParameter("pw");
                    String encryptedPassword = passwordEncryptor.encryptPassword(password);

                    ut.setUsername(request.getParameter("username"));
                    ut.setPw(encryptedPassword);
                    ut.setEmail(request.getParameter("email"));
                    ut.setTipologia("az");
                    int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().insertUtente(ut);
                    if (insert != 1) {
                        TYPE="AZIENDA";
                        request.setAttribute("message", "errore_convalida");
                        request.setAttribute("errore", "I campi utente inseriti non sono validi. Riprova!");
                        action_error(request, response);
                    }
                } else {
                    TYPE="AZIENDA";
                    request.setAttribute("message", "errore_convalida");
                    request.setAttribute("errore", "I campi utente inseriti non sono validi. Riprova!");
                    action_error(request, response);
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
                        int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().insertAzienda(az);
                        if (insert != 1) {
                            TYPE="AZIENDA";
                            request.setAttribute("message", "errore_convalida");
                            request.setAttribute("errore", "I dati aziendali inseriti non sono validi. Riprova!");
                            action_error(request, response);
                        }

                        request.setAttribute("MSG", "Grazie per la registrazione. \nPotrai eseguire l'accesso non appena l'admin confermerà la vostra richiesta di convenzionamento");
                        request.setAttribute("ICON", "fas fa-check");
                        request.setAttribute("alert", "success");
                        TemplateResult res = new TemplateResult(getServletContext());
                        res.activate("home_anonimo.ftl.html", request, response);
                    }  else {
                        TYPE="AZIENDA";
                        request.setAttribute("message", "errore_convalida");
                        request.setAttribute("errore", "I dati aziendali inseriti non sono validi. Riprova!");
                        action_error(request, response);
                    }
                } catch (DataException ex) {
                    // se fallisce l'inserimento dell'azienda, cancella l'utente e il responsabile inseriti prima
                    ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getRespTirociniDAO().deleteRespTirocini(rt.getId());
                    ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().deleteUtente(ut.getId());
                    request.setAttribute("exception", ex);
                    action_error(request, response);
                }
            } catch (DataException ex) {
                // se fallisce l'inserimento dell'utente, cancella il responsabile inserito prima
                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getRespTirociniDAO().deleteRespTirocini(rt.getId());
                request.setAttribute("exception", ex);
                action_error(request, response);
            }
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


                /* encrypt pass */
                String password = request.getParameter("pw");
                String encryptedPassword = passwordEncryptor.encryptPassword(password);

                ut.setUsername(request.getParameter("username"));
                ut.setPw(encryptedPassword);
                ut.setEmail(request.getParameter("email"));
                ut.setTipologia("st");
                if (((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().checkUtenteExist(request.getParameter("username"), request.getParameter("email"))){
                    TYPE="STUDENT";
                    request.setAttribute("message", "errore_convalida");
                    request.setAttribute("errore", "Email o Password gi&agrave; esistenti. Riprova!");
                    action_error(request, response);
                    return;
                }
                    int insert = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().insertUtente(ut);
                    if (insert != 1) {
                        TYPE = "STUDENT";
                        request.setAttribute("message", "errore_convalida");
                        request.setAttribute("errore", "I campi inseriti non sono corretti. Riprova!");
                        action_error(request, response);
                        return;
                    }
            } else {
                TYPE="STUDENT";
                request.setAttribute("message", "errore_convalida");
                request.setAttribute("errore", "I campi inseriti non sono corretti. Riprova!");
                action_error(request, response);
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

                    int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().insertStudente(st);
                    if (insert != 1){
                        TYPE="STUDENT";
                        request.setAttribute("message", "errore_convalida");
                        request.setAttribute("errore", "I campi inseriti non sono corretti. Riprova!");
                        action_error(request, response);
                    }

                    request.setAttribute("MSG", "Registrazione effettuata con successo!\nOra puoi accedere ed iniziare ad usare i nostri servizi");
                    request.setAttribute("ICON", "fas fa-check");
                    request.setAttribute("alert", "success");
                    request.setAttribute("TITLE", "OK");
                    TemplateResult res = new TemplateResult(getServletContext());
                    res.activate("login.ftl.html", request, response);

                } else {
                    TYPE="STUDENT";
                    request.setAttribute("message", "errore_convalida");
                    request.setAttribute("errore", "I campi inseriti non sono corretti. Riprova!");
                    action_error(request, response);
                }
            } catch (DataException ex) {
                // se fallisce l'inserimento dello studente, cancella l'utente inserito prima
                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().deleteUtente(ut.getId());
                request.setAttribute("exception", ex);
                action_error(request, response);
            }

        } catch (DataException ex) {
            //request.setAttribute("exception", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
        
    }
        
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
            if (request.getParameter("submitStudent") != null) {
                action_registrazione_studente(request, response);
            } else if(request.getParameter("submitAzienda") != null) {
                action_registrazione_azienda(request, response);
            } else {
                    if (request.getParameter("tipo").equals("azienda")) {
                        request.setAttribute("activeAzienda", "active");
                        request.setAttribute("ariaStudente", "false");
                        request.setAttribute("ariaAzienda", "true");
                        action_open_reg(request, response);
                    }
                    else if (request.getParameter("tipo").equals("studente")) {
                        request.setAttribute("activeStudente", "active");
                        request.setAttribute("ariaStudente", "true");
                        request.setAttribute("ariaAzienda", "false");
                        action_open_reg(request, response);
                    }
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



