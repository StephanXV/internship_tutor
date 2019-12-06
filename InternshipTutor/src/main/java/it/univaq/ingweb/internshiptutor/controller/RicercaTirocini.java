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
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Enrico Monte
 */
public class RicercaTirocini extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Tirocini");
            res.activate("ricerca_tirocini.ftl.html", request, response);
        } catch (TemplateManagerException e) {
            request.setAttribute("exception", e);
            action_error(request, response);
        }
    }
    
    private void action_ricerca(HttpServletRequest request, HttpServletResponse response) {
        try {
            String obiettivi = "%";
            String corso = "%";
            String durata = "%";
            String luogo = "%";
            String settore = "%";
            String titolo = "%";
            boolean facilitazioni = false;
            
            if ((request.getParameter("facilitazioni") == null || request.getParameter("facilitazioni").length() < 1)
                    && (request.getParameter("corso") == null || request.getParameter("corso").length() < 1)
                    && (request.getParameter("obiettivi") == null || request.getParameter("obiettivi").length() < 1)
                    && (request.getParameter("luogo") == null || request.getParameter("luogo").length() < 1)
                    && (request.getParameter("settore") == null || request.getParameter("settore").length() < 1)
                    && (request.getParameter("titolo") == null || request.getParameter("titolo").length() < 1)
                    && (request.getParameter("durata") == null || request.getParameter("durata").length() < 1)) {
                
                request.setAttribute("noSearch", "Inserire almeno un parametro per avviare la ricerca!");
                action_default(request,response);
                
                return;
            }
            
            if (request.getParameter("facilitazioni") != null && request.getParameter("facilitazioni").length() > 0 && request.getParameter("facilitazioni").equals("1")) {
                facilitazioni = true;
                request.setAttribute("p_facilitazioni", request.getParameter("facilitazioni"));
            }
            
            if (request.getParameter("obiettivi") != null && request.getParameter("obiettivi").length() > 0) {
                obiettivi = "%" + request.getParameter("obiettivi") + "%";
                request.setAttribute("p_obiettivi", request.getParameter("obiettivi"));
            }
            
            if (request.getParameter("luogo") != null && request.getParameter("luogo").length() > 0) {
                luogo = "%" + request.getParameter("luogo") + "%";
                request.setAttribute("p_luogo", request.getParameter("luogo"));
            }
            
            if (request.getParameter("settore") != null && request.getParameter("settore").length() > 0) {
                settore = "%" + request.getParameter("settore") + "%";
                request.setAttribute("p_settore", request.getParameter("settore"));
            }
            
            if (request.getParameter("durata") != null && request.getParameter("durata").length() > 0) {
                durata = request.getParameter("durata");
                request.setAttribute("p_durata", request.getParameter("durata"));
            }
            
            if (request.getParameter("corso") != null && request.getParameter("corso").length() > 0) {
                corso = request.getParameter("corso");
                request.setAttribute("p_corso", request.getParameter("corso"));
            }
            
            if (request.getParameter("titolo") != null && request.getParameter("titolo").length() > 0) {
                titolo = "%" + request.getParameter("titolo") + "%";
                request.setAttribute("p_titolo", request.getParameter("titolo"));
            }
            
            
            List<OffertaTirocinio> tirocini = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getOffertaTirocinioDAO().searchOffertaTirocinio(durata, titolo, facilitazioni, luogo, settore, obiettivi, corso);
            request.setAttribute("tirocini", tirocini);
            action_default(request,response);
        } catch (DataException ex) {
            request.setAttribute("exception", "Unable to load offerte di tirocinio");
            action_error(request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        request.setAttribute("activeTirocini", "active");
        
        HttpSession s = SecurityLayer.checkSession(request);
        if (s!= null) {
            request.setAttribute("nome_utente", (String)s.getAttribute("username"));
            request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
        }
        if (request.getParameter("submit") != null && request.getParameter("submit").equals("Cerca")) {
            action_ricerca(request,response);
        } else {
            action_default(request, response);
        }  
    } 
}
