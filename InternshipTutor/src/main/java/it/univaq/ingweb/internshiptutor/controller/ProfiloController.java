package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.RespTirocini;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.jasypt.util.password.BasicPasswordEncryptor;

public class ProfiloController extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            request.setAttribute("referrer", "profilo.ftl.html");
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) {
        
        HttpSession s = SecurityLayer.checkSession(request);
        if (s!= null) {
            request.setAttribute("nome_utente", (String)s.getAttribute("username"));
            request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
            
            try {
                
                if (s.getAttribute("tipologia").equals("st")) {
                    Studente studente = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente((int) s.getAttribute("id_utente"));
                    request.setAttribute("studente", studente);
                    request.setAttribute("cf", studente.getCF());
                    Utente utente = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) s.getAttribute("id_utente"));
                    request.setAttribute("email", utente.getEmail());
                    request.setAttribute("username", utente.getUsername());
                    
                    //candidature
                    List<Candidatura> candidature = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidature(studente);
                    List<Candidatura> attesa = new ArrayList<>();
                    List<Candidatura> attiva = new ArrayList<>();
                    List<Candidatura> finita = new ArrayList<>();
                    
                    for (Candidatura c: candidature){
                        switch (c.getStatoCandidatura()) {
                            case 0 :
                                attesa.add(c);
                                break;
                                
                            case 1 :
                                attiva.add(c);
                                break;
                                
                            case 2:
                                finita.add(c);
                                break;
                                
                            default: break;
                        }
                    }
                    
                    request.setAttribute("candidature_attesa", attesa.size());
                    request.setAttribute("candidature_attive", attiva.size());
                    request.setAttribute("candidature_finite", finita.size());
                    
                    
                }
                
                else if (s.getAttribute("tipologia").equals("ad")) {
                    Utente admin = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) s.getAttribute("id_utente"));
                    request.setAttribute("admin", admin);
                }
                
                else if (s.getAttribute("tipologia").equals("az")) {
                    Azienda azienda = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda((int) s.getAttribute("id_utente"));
                    request.setAttribute("azienda", azienda);
                }
                
                TemplateResult res = new TemplateResult(getServletContext());
                request.setAttribute("page_title", "Profilo");
                
                
                res.activate("profilo.ftl.html", request, response);
                
            } catch (TemplateManagerException | DataException e) {
                request.setAttribute("exception", e);
                action_error(request, response);
            }
            
        } else {
            //sessione nulla
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "Errore Sconosciuto");
            request.setAttribute("errore", "Non è stato possibile caricare la pagina. Riprova.");
            action_error(request, response);
        }
        
        
    }
    
    private void action_modifica_profilo(HttpServletRequest request, HttpServletResponse response, HttpSession s)
            throws TemplateManagerException, IOException {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        
        if (s.getAttribute("tipologia").equals("az")) {
            try {
                Azienda azienda = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda((int) s.getAttribute("id_utente"));
                RespTirocini rt = azienda.getRespTirocini();
                // controlli sul responsabile tirocini
                if (SecurityLayer.checkString(request.getParameter("nome_rt")) && SecurityLayer.checkString(request.getParameter("cognome_rt")) &&
                        SecurityLayer.checkEmail(request.getParameter("email_rt")) && SecurityLayer.checkTelefono(request.getParameter("telefono_rt"))){
                    
                    rt.setNome(request.getParameter("nome_rt"));
                    rt.setCognome(request.getParameter("cognome_rt"));
                    rt.setEmail(request.getParameter("email_rt"));
                    rt.setTelefono(request.getParameter("telefono_rt"));
                    int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getRespTirociniDAO().updateRespTirocini(rt);
                    if (insert != 1) {
                        request.setAttribute("message", "errore_convalida");
                        request.setAttribute("errore", "I campi del responsabile tirocini non sono corretti. Riprova!");
                        action_error(request, response);
                    }
                } else {
                    request.setAttribute("message", "errore_convalida");
                    request.setAttribute("errore", "I campi del responsabile tirocini non sono corretti. Riprova!");
                    action_error(request, response);
                }
                
                try {
                    Utente ut = azienda.getUtente();
                    // controlli sull'utente
                    if (SecurityLayer.checkString(request.getParameter("username")) &&
                            SecurityLayer.checkEmail(request.getParameter("email"))) {
                        
                        if("".equals(request.getParameter("pw"))) {
                            ut.setPw(ut.getPw());
                        } else {
                            /* encrypt pass */
                            String password = request.getParameter("pw");
                            String encryptedPassword = passwordEncryptor.encryptPassword(password);
                            ut.setPw(encryptedPassword);
                        }
                        ut.setUsername(request.getParameter("username"));
                        ut.setEmail(request.getParameter("email"));
                        ut.setTipologia("az");
                        int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().updateUtente(ut);
                        if (insert != 1) {
                            request.setAttribute("message", "errore_convalida");
                            request.setAttribute("errore", "I campi utente inseriti non sono validi. Riprova!");
                            action_error(request, response);
                        }
                    } else {
                        request.setAttribute("message", "errore_convalida");
                        request.setAttribute("errore", "I campi utente inseriti non sono validi. Riprova!");
                        action_error(request, response);
                    }
                    
                    try {
                        // controlli sull'azienda
                        if (SecurityLayer.checkString(request.getParameter("ragione_sociale")) && SecurityLayer.checkString(request.getParameter("indirizzo")) &&
                                SecurityLayer.checkString(request.getParameter("citta")) && SecurityLayer.checkCap(request.getParameter("cap")) &&
                                SecurityLayer.checkString(request.getParameter("provincia")) && SecurityLayer.checkString(request.getParameter("rappresentante_legale")) &&
                                SecurityLayer.checkString(request.getParameter("piva")) && SecurityLayer.checkString(request.getParameter("foro_competente")) &&
                                SecurityLayer.checkString(request.getParameter("tematiche")) && SecurityLayer.checkString(request.getParameter("corso_studio")) &&
                                SecurityLayer.checkDurata(request.getParameter("durata"))) {
                            
                            azienda.setRagioneSociale(request.getParameter("ragione_sociale"));
                            azienda.setIndirizzo(request.getParameter("indirizzo"));
                            azienda.setCitta(request.getParameter("citta"));
                            azienda.setCap(request.getParameter("cap"));
                            azienda.setProvincia(request.getParameter("provincia"));
                            azienda.setRappresentanteLegale(request.getParameter("rappresentante_legale"));
                            azienda.setPiva(request.getParameter("piva"));
                            azienda.setForoCompetente(request.getParameter("foro_competente"));
                            azienda.setTematiche(request.getParameter("tematiche"));
                            azienda.setCorsoStudio(request.getParameter("corso_studio"));
                            azienda.setDurataConvenzione(SecurityLayer.checkNumeric(request.getParameter("durata")));
                            int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().updateAzienda(azienda);
                            if (insert != 1) {
                                request.setAttribute("message", "errore_convalida");
                                request.setAttribute("errore", "I dati aziendali inseriti non sono validi. Riprova!");
                                action_error(request, response);
                            }
                            
                            request.setAttribute("MSG", "Dati aggiornati");
                            request.setAttribute("ICON", "fas fa-check");
                            request.setAttribute("alert", "success");
                            
                            action_default(request, response);
                        }  else {
                            request.setAttribute("message", "errore_convalida");
                            request.setAttribute("errore", "I dati aziendali inseriti non sono validi. Riprova!");
                            action_error(request, response);
                        }
                    } catch (DataException ex) {
                        request.setAttribute("exception", ex);
                        action_error(request, response);
                    }
                } catch (DataException ex) {
                    request.setAttribute("exception", ex);
                    action_error(request, response);
                }
            } catch (DataException ex) {
                request.setAttribute("exception", ex);
                action_error(request, response);
            }
        } else if (s.getAttribute("tipologia").equals("st")) {
            try {
                Studente st = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente((int) s.getAttribute("id_utente"));
                Utente ut = st.getUtente();
                // controlli sull'utente
                if (SecurityLayer.checkString(request.getParameter("username")) &&
                        SecurityLayer.checkEmail(request.getParameter("email"))) {
                    
                    if("".equals(request.getParameter("pw_st"))) {
                        ut.setPw(ut.getPw());
                    } else {
                        /* encrypt pass */
                        String password = request.getParameter("pw_st");
                        String encryptedPassword = passwordEncryptor.encryptPassword(password);
                        ut.setPw(encryptedPassword);
                    }
                    
                    ut.setUsername(request.getParameter("username"));
                    ut.setEmail(request.getParameter("email"));
                    ut.setTipologia("az");
                    int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().updateUtente(ut);
                    if (insert != 1) {
                        request.setAttribute("message", "errore_convalida");
                        request.setAttribute("errore", "I campi utente inseriti non sono validi. Riprova!");
                        action_error(request, response);
                    }
                } else {
                    request.setAttribute("message", "errore_convalida");
                    request.setAttribute("errore", "I campi utente inseriti non sono validi. Riprova!");
                    action_error(request, response);
                }
                try {
                    // controlli sullo studente
                    if (SecurityLayer.checkString(request.getParameter("nome")) && SecurityLayer.checkString(request.getParameter("cognome")) &&
                            SecurityLayer.checkString(request.getParameter("cf")) && SecurityLayer.checkBoolean(request.getParameter("handicap")) &&
                            SecurityLayer.checkDateString(request.getParameter("data_nasc")) && SecurityLayer.checkString(request.getParameter("luogo_nasc")) &&
                            SecurityLayer.checkString(request.getParameter("prov_nasc")) && SecurityLayer.checkString(request.getParameter("residenza_citta")) &&
                            SecurityLayer.checkString(request.getParameter("cap")) && SecurityLayer.checkString(request.getParameter("residenza_prov")) &&
                            SecurityLayer.checkString(request.getParameter("telefono")) && SecurityLayer.checkString(request.getParameter("corso")))  {
                        
                        st.setNome(request.getParameter("nome"));
                        st.setCognome(request.getParameter("cognome"));
                        st.setCF(request.getParameter("cf"));
                        st.setDataNascita(SecurityLayer.checkDate(request.getParameter("data_nasc")));
                        st.setCittaNascita(request.getParameter("luogo_nasc"));
                        st.setProvinciaNascita(request.getParameter("prov_nasc"));
                        st.setCittaResidenza(request.getParameter("residenza_citta"));
                        st.setCapResidenza(request.getParameter("cap"));
                        st.setProvinciaResidenza(request.getParameter("residenza_prov"));
                        st.setTelefono(request.getParameter("telefono"));
                        st.setCorsoLaurea(request.getParameter("corso"));
                        st.setHandicap(Boolean.valueOf(request.getParameter("handicap")));
                        
                        int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().updateStudente(st);
                        if (insert != 1){
                            request.setAttribute("message", "errore_convalida");
                            request.setAttribute("errore", "I campi inseriti non sono corretti. Riprova!");
                            action_error(request, response);
                        }
                        
                        request.setAttribute("MSG", "Dati aggiornati");
                        request.setAttribute("ICON", "fas fa-check");
                        request.setAttribute("alert", "success");
                        request.setAttribute("TITLE", "OK");
                        
                        action_default(request, response);
                    } else {
                        request.setAttribute("message", "errore_convalida");
                        request.setAttribute("errore", "I campi inseriti non sono corretti. Riprova!");
                        action_error(request, response);
                    }
                } catch (DataException ex) {
                    request.setAttribute("exception", ex);
                    action_error(request, response);
                }
                
            } catch (DataException ex) {
                //request.setAttribute("exception", ex);
                request.setAttribute("exception", ex);
                action_error(request, response);
            }
        } else if (s.getAttribute("tipologia").equals("ad")) {
            try {
                Utente ad = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) s.getAttribute("id_utente"));
                // controlli sull'utente
                if (SecurityLayer.checkString(request.getParameter("username")) && SecurityLayer.checkString(request.getParameter("pw")) &&
                        SecurityLayer.checkEmail(request.getParameter("email"))) {
                    
                    if("".equals(request.getParameter("pw"))) {
                        ad.setPw(ad.getPw());
                    } else {
                        /* encrypt pass */
                        String password = request.getParameter("pw");
                        String encryptedPassword = passwordEncryptor.encryptPassword(password);
                        ad.setPw(encryptedPassword);
                    }
                    
                    ad.setUsername(request.getParameter("username"));
                    ad.setEmail(request.getParameter("email"));
                    ad.setTipologia("az");
                    int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().updateUtente(ad);
                    if (insert != 1) {
                        request.setAttribute("message", "errore_convalida");
                        request.setAttribute("errore", "I campi utente inseriti non sono validi. Riprova!");
                        action_error(request, response);
                    }
                    
                    request.setAttribute("MSG", "Dati aggiornati");
                    request.setAttribute("ICON", "fas fa-check");
                    request.setAttribute("alert", "success");
                    request.setAttribute("TITLE", "OK");
                    TemplateResult res = new TemplateResult(getServletContext());
                    res.activate("profilo.ftl.html", request, response);
                } else {
                    request.setAttribute("message", "errore_convalida");
                    request.setAttribute("errore", "I campi utente inseriti non sono validi. Riprova!");
                    action_error(request, response);
                }
                
            } catch (DataException ex) {
                //request.setAttribute("exception", ex);
                request.setAttribute("exception", ex);
                action_error(request, response);
            }
        }
    }
    
    private void action_visualize_profile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession s = SecurityLayer.checkSession(request);
        if (s!= null) {
            request.setAttribute("nome_utente", (String) s.getAttribute("username"));
            request.setAttribute("tipologia", (String) s.getAttribute("tipologia"));
            
            try {
                
                if (s.getAttribute("tipologia").equals("az") && request.getParameter("prof")!=null && SecurityLayer.checkNumericBool(request.getParameter("prof"))) {
                    int id = Integer.parseInt(request.getParameter("prof"));
                    Studente studente = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getStudenteDAO().getStudente(id);
                    request.setAttribute("studente", studente);
                    request.setAttribute("cf", studente.getCF());
                    System.out.println(studente.getCF());
                    
                    if (studente.isHandicap()) {
                        request.setAttribute("handicap", "si");
                    } else {
                        request.setAttribute("handicap", "no");
                    }
                    
                    Utente utente = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) s.getAttribute("id_utente"));
                    request.setAttribute("email", utente.getEmail());
                    request.setAttribute("username", utente.getUsername());
                    request.setAttribute("visualize", "visualize");
                    
                    TemplateResult res = new TemplateResult(getServletContext());
                    request.setAttribute("page_title", "Profilo Studente");
                    
                    
                    res.activate("profilo.ftl.html", request, response);
                    
                } else {
                    request.setAttribute("message", "errore gestito");
                    request.setAttribute("title", "Errore Sconosciuto");
                    request.setAttribute("errore", "Non è stato possibile caricare la pagina. Riprova.");
                    action_error(request, response);
                }
            } catch (DataException | TemplateManagerException e) {
                request.setAttribute("exception", e);
                action_error(request, response);
            }
        } else {
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "Errore Sconosciuto");
            request.setAttribute("errore", "Non è stato possibile caricare la pagina. Riprova.");
            action_error(request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                if (request.getParameter("submit") != null) {
                    action_modifica_profilo(request,response, s);
                } else if (request.getParameter("visualize") != null) {
                    action_visualize_profile(request,response);
                } else {
                    action_default(request, response);
                }
            } else {
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Utente non autorizzato");
                request.setAttribute("errore", "401 Unauthorized");
                action_error(request, response);
            }
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);      
        }   
    }
}
