package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import javafx.scene.control.Alert;

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
 * @author Stefano Florio
 */
public class GestioneCandidature extends InternshipTutorBaseController {
        
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, TemplateManagerException {
        int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
        try {
            OffertaTirocinio ot = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(id_ot);
            request.setAttribute("nome_tirocinio", ot.getTitolo());
            
            List<Candidatura> richieste_cand = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidature(id_ot, 0);
            request.setAttribute("richieste_cand", richieste_cand);
            
            List<Candidatura> cand_accettate = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidature(id_ot, 1);
            request.setAttribute("cand_accettate", cand_accettate);
            
            List<Candidatura> tiro_terminati = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidature(id_ot, 2);
            request.setAttribute("tiro_terminati", tiro_terminati);
            
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("gestione_candidati.ftl.html", request, response); 
            
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }  
    }
    
    
    private void action_accetta_cand(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, TemplateManagerException {
        int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
        int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
        try {
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaStato(1, id_st, id_ot);
            response.sendRedirect("gestione_candidati?ot=" + id_ot);
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }  
    }
    
    private void action_rifiuta_cand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
        int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
        try {
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaStato(3, id_st, id_ot);
            response.sendRedirect("gestione_candidati?ot=" + id_ot);
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
            }
            if (null == request.getParameter("convalida")) {
                action_default(request, response);
            } 
            else switch (request.getParameter("convalida")) {
                case "si":
                    action_accetta_cand(request, response);
                    break;
                case "no":
                    action_rifiuta_cand(request, response);
                    break;
                default:
                    break;
            }
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}