package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import org.jasypt.util.text.BasicTextEncryptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class ProfiloController extends InternshipTutorBaseController {
    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
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
                System.out.println(studente.getCF());

                if (studente.isHandicap()) {
                    request.setAttribute("_selected_handicap_si", "selected");
                } else {
                    request.setAttribute("_selected_handicap_no", "selected");
                }

                Utente utente = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) s.getAttribute("id_utente"));
                request.setAttribute("email", utente.getEmail());
                request.setAttribute("username", utente.getUsername());


                //candidature
                List<Candidatura> candidature = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidature(studente);
                List<Candidatura> attesa = new ArrayList<Candidatura>();
                List<Candidatura> attiva = new ArrayList<Candidatura>();
                List<Candidatura> finita = new ArrayList<Candidatura>();

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
            return;
        }


    }

    private void action_modifica_profilo(HttpServletRequest request, HttpServletResponse response) {

        HttpSession s = SecurityLayer.checkSession(request);
        if (s!= null) {
            request.setAttribute("nome_utente", (String) s.getAttribute("username"));
            request.setAttribute("tipologia", (String) s.getAttribute("tipologia"));

            if (s.getAttribute("tipologia").equals("st")) {
                //if (controlli lato server)

                //update profilo
                action_default(request, response); //riapri pagina html

                //else errore convalida

                return;
            } else if (s.getAttribute("tipologia").equals("ad")) {
                return;
            } else if (s.getAttribute("tipologia").equals("az")) {
                return;
            }

        } else { //sessione nulla
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "Errore Sconosciuto");
            request.setAttribute("errore", "Non è stato possibile caricare la pagina. Riprova.");
            action_error(request, response);
            return;
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
                    return;
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
            return;
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {

        if (request.getParameter("submit") != null) {
            action_modifica_profilo(request,response);
        }

        else if (request.getParameter("visualize") != null) {
            action_visualize_profile(request,response);
        }

        else {
            action_default(request, response);
        }

    }


}
