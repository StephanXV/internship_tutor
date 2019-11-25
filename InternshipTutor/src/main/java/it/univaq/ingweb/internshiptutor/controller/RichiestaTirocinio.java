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
import it.univaq.ingweb.internshiptutor.data.impl.CandidaturaImpl;
import it.univaq.ingweb.internshiptutor.data.impl.TutoreUniImpl;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;
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
 * @author pavan
 */
public class RichiestaTirocinio extends InternshipTutorBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
            }
                if (request.getParameter("submit") != null) {
                    action_request(request, response);
                } else if (request.getParameter("submitTutore") != null) {
                    action_addAutore(request, response);
                } else {
                   action_default(request, response);
                }
    }

    private void action_addAutore(HttpServletRequest request, HttpServletResponse response) {
        if (SecurityLayer.checkString(request.getParameter("name")) && SecurityLayer.checkEmail(request.getParameter("email")) && SecurityLayer.checkTelefono(request.getParameter("telefono")) && SecurityLayer.checkString(request.getParameter("surname"))) {
            TutoreUni tutore = new TutoreUniImpl();
            tutore.setNome(request.getParameter("name"));
            tutore.setCognome(request.getParameter("surname"));
            tutore.setEmail(request.getParameter("email"));
            tutore.setTelefono(request.getParameter("telefono"));
            try {
                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().insertTutoreUni(tutore);
                //result ok
                request.setAttribute("MSG", "Tutore Aggiunto! Ora potrai inserirlo nella tua richiesta");
                request.setAttribute("ICON", "fas fa-check");
                request.setAttribute("alert", "success");
                action_default(request, response);
            } catch (DataException e) {
                Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, e);
                request.setAttribute("message", "errore_convalida");
                request.setAttribute("errore", "Tutore non aggiunto! Verifica che il tutore non sia già presente");
                action_error(request, response);
            }
        }
    }

    private void action_request (HttpServletRequest request, HttpServletResponse response) {
        HttpSession s = SecurityLayer.checkSession(request);
        int u = (int) s.getAttribute("id_utente");

        Candidatura c = new CandidaturaImpl();

        //if all parameters are checked fai tutto else errore

        try {
            c.setStudente(((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(u));
            c.setOffertaTirocinio(((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(SecurityLayer.checkNumeric(request.getParameter("n"))));

            c.setTutoreUni(((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().getTutoreUni(SecurityLayer.checkNumeric(request.getParameter("id_tutore"))));
            c.setCfu(Integer.parseInt(request.getParameter("cfu")));
            //set corsoLaure obbligatorio

            if (request.getParameter("laurea") != null && request.getParameter("laurea").length() > 0) {
                c.setLaurea(request.getParameter("laurea"));
            }

            if (request.getParameter("dottorato") != null && request.getParameter("dottorato").length() > 0) {
                c.setDottoratoRicerca(request.getParameter("dottorato"));
            }

            if (request.getParameter("specializzazione") != null && request.getParameter("specializzazione").length() > 0) {
                c.setSpecializzazione(request.getParameter("specializzazione"));
            }

            if (request.getParameter("diploma") != null && request.getParameter("diploma").length() > 0) {
                c.setDiploma(request.getParameter("diploma"));
            }

            //insert candidatura
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().insertCandidatura(c);

        } catch (DataException e) {
            Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, e);
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "Impossibile richiedere la candidatura");
            request.setAttribute("errore", "ERRORE");
            action_error(request, response);
            return;
        }

        //mettere il template activate
        try {
            response.sendRedirect("home");
        } catch (IOException e) {
            request.setAttribute("exception", e);
            action_error(request, response);
        }

    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) {
        TemplateResult res = new TemplateResult(getServletContext());
        int n = SecurityLayer.checkNumeric(request.getParameter("n"));
        //OffertaTirocinio tirocinio = (OffertaTirocinio) ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(n);
        List<TutoreUni> tutori = null;
        try {
            tutori = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().getTutori();
            request.setAttribute("tutori", tutori);
            request.setAttribute("tirocinio", n);
            try {
                res.activate("richiesta_tirocinio.ftl.html", request, response);
            } catch (TemplateManagerException e) {
                Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, e);
                request.setAttribute("exception", e);
                action_error(request, response);
            }
        } catch (DataException e) {
            Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, e);
            request.setAttribute("exception", e);
            action_error(request, response);
        }
    }
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            request.setAttribute("referrer", "richiesta_tirocinio.ftl.html");
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
}
