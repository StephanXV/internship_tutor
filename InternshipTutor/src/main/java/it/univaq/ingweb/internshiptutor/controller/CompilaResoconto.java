package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.framework.security.SecurityLayerException;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Resoconto;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class CompilaResoconto extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        
        try {
            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
            int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
            Resoconto resoconto = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getResocontoDAO().getResoconto(id_st, id_ot);
            if (resoconto != null) {
                request.setAttribute("resoconto", resoconto);
                request.setAttribute("id_ot", id_ot);
                request.setAttribute("id_st", id_st);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("compila_resoconto.ftl.html", request, response);
            } else {
                request.setAttribute("message", "Unable to load resoconto");
                action_error(request, response);
            }
        } catch (DataException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Parameters exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_invia_resoconto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        try {
            int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
            Studente st = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(id_st);
            OffertaTirocinio ot = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(id_ot);
            Resoconto resoconto = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getResocontoDAO().createResoconto();
            if (st != null && ot != null && resoconto != null) {
                resoconto.setOreEffettive(SecurityLayer.checkNumeric(request.getParameter("ore_effettive")));
                resoconto.setDescAttivita(SecurityLayer.issetString(request.getParameter("desc_attivita")));
                resoconto.setGiudizio(SecurityLayer.issetString(request.getParameter("giudizio")));
                resoconto.setStudente(st);
                resoconto.setOffertaTirocinio(ot);
                int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getResocontoDAO().insertResoconto(resoconto);
                if (insert != 1) {
                    request.setAttribute("message", "errore_resoconto");
                    request.setAttribute("errore", "I dati del resoconto non sono validi, riprova");
                    action_error(request, response);
                }
                response.sendRedirect("gestione_candidati?ot="+id_ot);
            } else {
                request.setAttribute("message", "Unable to load studente, tirocinio or resoconto");
                action_error(request, response);
            }
        } catch (DataException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        } catch (SecurityLayerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Parameters exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null && "az".equals(s.getAttribute("tipologia"))) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                if (request.getParameter("submit") != null) {
                    action_invia_resoconto(request, response);
                }
                else
                    action_default(request, response);
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