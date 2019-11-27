package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class DettaglioCandidatura extends InternshipTutorBaseController {
        
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, TemplateManagerException {
        int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
        int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
        try {
            Candidatura candidatura = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidatura(id_st, id_ot);
            request.setAttribute("candidatura", candidatura);
            
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("dettaglio_candidatura.ftl.html", request, response); 
            
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }  
    }
    
    private void action_salva_date(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, TemplateManagerException {
        int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
        int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
        LocalDate inizio_tirocinio = LocalDate.parse(request.getParameter("inizio_tirocinio"));
        LocalDate fine_tirocinio = LocalDate.parse(request.getParameter("fine_tirocinio"));
        try {
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaDate(inizio_tirocinio,
                    fine_tirocinio, id_st, id_ot);
            response.sendRedirect("dettaglio_candidatura?st="+id_st+"&ot="+id_ot);
            
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
            
            if (request.getParameter("tipo") != null) {
                if (request.getParameter("tipo").equals("salva_date"))
                    action_salva_date(request, response);
            }
            else
                action_default(request, response); 
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}