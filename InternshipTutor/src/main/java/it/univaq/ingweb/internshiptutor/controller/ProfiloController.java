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

/**
 *
 * @author Enrico Monte
 */
public class ProfiloController extends InternshipTutorBaseController {
        
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else if (request.getAttribute("message") != null) {
            
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        } else if (request.getAttribute("alert_msg") != null) {
            request.setAttribute("referrer", "profilo.ftl.html");
            (new FailureResult(getServletContext())).activateAlert((String) request.getAttribute("alert_msg"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws DataException, TemplateManagerException {

        request.setAttribute("nome_utente", (String) s.getAttribute("username"));
        request.setAttribute("tipologia", (String) s.getAttribute("tipologia"));


        if (s.getAttribute("tipologia").equals("st")) {
            Studente studente = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getStudenteDAO().getStudente((int) s.getAttribute("id_utente"));
            request.setAttribute("studente", studente);
            request.setAttribute("cf", studente.getCF());
            Utente utente = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) s.getAttribute("id_utente"));
            request.setAttribute("email", utente.getEmail());
            request.setAttribute("username", utente.getUsername());

            //candidature
            List<Candidatura> candidature = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getCandidaturaDAO().getCandidature(studente);
            List<Candidatura> attesa = new ArrayList<>();
            List<Candidatura> attiva = new ArrayList<>();
            List<Candidatura> finita = new ArrayList<>();

            for (Candidatura c : candidature) {
                switch (c.getStatoCandidatura()) {
                    case 0:
                        attesa.add(c);
                        break;

                    case 1:
                        attiva.add(c);
                        break;

                    case 2:
                        finita.add(c);
                        break;

                    default:
                        break;
                }
            }

            request.setAttribute("candidature_attesa", attesa.size());
            request.setAttribute("candidature_attive", attiva.size());
            request.setAttribute("candidature_finite", finita.size());


        } else if (s.getAttribute("tipologia").equals("ad")) {
            Utente admin = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) s.getAttribute("id_utente"));
            request.setAttribute("admin", admin);
        } else if (s.getAttribute("tipologia").equals("az")) {
            Azienda azienda = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getAziendaDAO().getAzienda((int) s.getAttribute("id_utente"));
            request.setAttribute("azienda", azienda);
        } else {
            throw new DataException("Tipologia utente non trovata");
        }

        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Profilo");


        res.activate("profilo.ftl.html", request, response);

    }
    
    private void action_modifica_profilo(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws TemplateManagerException, IOException, DataException {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor(); //per cambiare la pass

        if (s.getAttribute("tipologia").equals("az")) {

            Azienda azienda = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getAziendaDAO().getAzienda((int) s.getAttribute("id_utente"));
            RespTirocini rt = azienda.getRespTirocini();
            // controlli sul responsabile tirocini
            if (SecurityLayer.checkString(request.getParameter("nome_rt")) && SecurityLayer.checkString(request.getParameter("cognome_rt")) &&
                    SecurityLayer.checkEmail(request.getParameter("email_rt")) && SecurityLayer.checkTelefono(request.getParameter("telefono_rt"))) {

                rt.setNome(request.getParameter("nome_rt"));
                rt.setCognome(request.getParameter("cognome_rt"));
                rt.setEmail(request.getParameter("email_rt"));
                rt.setTelefono(request.getParameter("telefono_rt"));
                int insert = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getRespTirociniDAO().updateRespTirocini(rt);
                if (insert != 1) {
                    logger.error("errore di inserimento resp tirocini");
                    throw new DataException("Impossibile inserire il resp tirocini");
                }
            } else {
                logger.error("errore di compilazione campi resp tirocini");
                request.setAttribute("alert_msg", "Dati responsabile tirocini non validi");
                action_error(request, response);
            }


            Utente ut = azienda.getUtente();
            // controlli sull'utente
            if (SecurityLayer.checkString(request.getParameter("username")) &&
                    SecurityLayer.checkEmail(request.getParameter("email"))) {

                if ("".equals(request.getParameter("pw")) || request.getParameter("pw") == null) { //se pass non cambiata
                    ut.setPw(ut.getPw());
                } else { //pass modificata
                    /* encrypt pass */
                    String password = request.getParameter("pw");
                    String encryptedPassword = passwordEncryptor.encryptPassword(password);
                    ut.setPw(encryptedPassword);
                }
                ut.setUsername(request.getParameter("username"));
                ut.setEmail(request.getParameter("email"));
                ut.setTipologia("az");
                int insert = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().updateUtente(ut);
                if (insert != 1) {
                    logger.error("errore di inserimento utente");
                    throw new DataException("Impossibile inserire utente");
                } else {
                    s.setAttribute("username", ut.getUsername());
                }
            } else {
                logger.error("errore di compilazione campi utente");
                request.setAttribute("alert_msg", "Dati utente non validi");
                action_error(request, response);
            }


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
                azienda.setDurataConvenzione(Integer.parseInt(request.getParameter("durata")));
                int insert = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getAziendaDAO().updateAzienda(azienda);
                if (insert != 1) {
                    logger.error("errore di inserimento azienda");
                    throw new DataException("Impossibile inserire l'azienda");
                }

                //result ok
                request.setAttribute("MSG", "Dati aggiornati");
                request.setAttribute("ICON", "fas fa-check");
                request.setAttribute("alert", "success");
                request.setAttribute("TITLE", "OK");

                action_default(request, response, s); //riapri profilo modificato
            } else {
                logger.error("errore di compilazione azienda");
                request.setAttribute("alert_msg", "Dati aziendali non validi");
                action_error(request, response);
            }

        } else if (s.getAttribute("tipologia").equals("st")) {

            Studente st = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getStudenteDAO().getStudente((int) s.getAttribute("id_utente"));
            Utente ut = st.getUtente();
            // controlli sull'utente
            if (SecurityLayer.checkString(request.getParameter("username")) && SecurityLayer.checkEmail(request.getParameter("email"))) {

                if ("".equals(request.getParameter("pw_st")) || request.getParameter("pw_st") == null) { //pw non cambiata
                    ut.setPw(ut.getPw());
                } else { //pw modificata
                    /* encrypt pass */
                    String password = request.getParameter("pw_st");
                    String encryptedPassword = passwordEncryptor.encryptPassword(password);
                    ut.setPw(encryptedPassword);
                }

                ut.setUsername(request.getParameter("username"));
                ut.setEmail(request.getParameter("email"));
                ut.setTipologia("st");
                int insert = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().updateUtente(ut);
                if (insert != 1) {
                    logger.error("errore di inserimento utente");
                    throw new DataException("Impossibile inserire utente");
                } else {
                    s.setAttribute("username", ut.getUsername());
                }
            } else {
                logger.error("errore di inserimento utente");
                request.setAttribute("alert_msg", "Dati utente non validi");
                action_error(request, response);
            }

            // controlli sullo studente
            if (SecurityLayer.checkString(request.getParameter("nome")) && SecurityLayer.checkString(request.getParameter("cognome")) &&
                    SecurityLayer.checkString(request.getParameter("cf")) && SecurityLayer.checkBoolean(request.getParameter("handicap")) &&
                    SecurityLayer.checkDateString(request.getParameter("data_nasc")) && SecurityLayer.checkString(request.getParameter("luogo_nasc")) &&
                    SecurityLayer.checkString(request.getParameter("prov_nasc")) && SecurityLayer.checkString(request.getParameter("residenza_citta")) &&
                    SecurityLayer.checkString(request.getParameter("cap")) && SecurityLayer.checkString(request.getParameter("residenza_prov")) &&
                    SecurityLayer.checkString(request.getParameter("telefono")) && SecurityLayer.checkString(request.getParameter("corso"))) {

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

                int insert = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getStudenteDAO().updateStudente(st);
                if (insert != 1) {
                    logger.error("errore di inserimento studente");
                    throw new DataException("Impossibile inserire studente");
                }

                //result ok
                request.setAttribute("MSG", "Dati aggiornati");
                request.setAttribute("ICON", "fas fa-check");
                request.setAttribute("alert", "success");
                request.setAttribute("TITLE", "OK");

                action_default(request, response, s); //riapri profilo modificato
            } else {
                logger.error("errore di compilazione studente");
                request.setAttribute("alert_msg", "Dati inseriti non validi");
                action_error(request, response);
            }

        } else if (s.getAttribute("tipologia").equals("ad")) {

            Utente ad = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) s.getAttribute("id_utente"));
            // controlli sull'utente
            if (SecurityLayer.checkString(request.getParameter("username"))  &&
                    SecurityLayer.checkEmail(request.getParameter("email"))) {

                if ("".equals(request.getParameter("pw")) || request.getParameter("pw") == null) { //se pass non cambiata
                    ad.setPw(ad.getPw());
                } else { //pass modificata
                    /* encrypt pass */
                    String password = request.getParameter("pw");
                    String encryptedPassword = passwordEncryptor.encryptPassword(password);
                    ad.setPw(encryptedPassword);
                }

                ad.setUsername(request.getParameter("username"));
                ad.setEmail(request.getParameter("email"));
                ad.setTipologia("ad");
                int insert = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().updateUtente(ad);
                if (insert != 1) {
                    logger.error("errore di inserimento utente");
                    throw new DataException("Impossibile inserire utente");
                } else {
                    s.setAttribute("username", ad.getUsername());
                }

                //result ok
                request.setAttribute("MSG", "Dati aggiornati");
                request.setAttribute("ICON", "fas fa-check");
                request.setAttribute("alert", "success");
                request.setAttribute("TITLE", "OK");

                action_default(request, response, s); //riapri profilo modificato
            } else {
                logger.error("errore di compilazione admin");
                request.setAttribute("alert_msg", "Dati utente non validi");
                action_error(request, response);
            }

        }
    }
    
    private void action_visualize_profile(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws DataException, TemplateManagerException {
        request.setAttribute("nome_utente", (String) s.getAttribute("username"));
        request.setAttribute("tipologia", (String) s.getAttribute("tipologia"));

        if (s.getAttribute("tipologia").equals("az") && request.getParameter("prof") != null && SecurityLayer.checkNumericBool(request.getParameter("prof"))) {
            int id = Integer.parseInt(request.getParameter("prof"));
            Studente studente = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getStudenteDAO().getStudente(id);
            request.setAttribute("studente", studente);
            request.setAttribute("cf", studente.getCF());

            if (studente.isHandicap()) {
                request.setAttribute("handicap", "si");
            } else {
                request.setAttribute("handicap", "no");
            }

            Utente utente = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) s.getAttribute("id_utente"));
            request.setAttribute("email", utente.getEmail());
            request.setAttribute("username", utente.getUsername());
            request.setAttribute("visualize", "visualize"); //per far capire all'html che i campi sono read only

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Profilo Studente");
            res.activate("profilo.ftl.html", request, response);

        } else {
            logger.error("Security check sui parametri non superato");
            request.setAttribute("message", "Impossibile caricare la pagina");
                action_error(request, response);
        }

    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                if (request.getParameter("submit") != null) {
                    action_modifica_profilo(request,response, s);
                } else if (request.getParameter("visualize") != null) {
                    action_visualize_profile(request,response, s);
                } else {
                    action_default(request, response, s);
                }
            } else {
                logger.error("Utente non autorizzato");
                request.setAttribute("message", "Utente non autorizzato");
                action_error(request, response);
            }
        } catch (TemplateManagerException | DataException | IOException ex) {
            logger.error("Exception: ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);      
        }   
    }
}
