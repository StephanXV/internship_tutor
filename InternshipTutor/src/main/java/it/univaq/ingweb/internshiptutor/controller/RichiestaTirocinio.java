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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author giuse
 */
public class RichiestaTirocinio extends InternshipTutorBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
            }
            try {
                if (request.getParameter("submit") != null) {
                    action_request(request, response);
                } else {
                   action_default(request, response);
                }
            } catch (DataException ex) {
                request.setAttribute("exception", ex);
                action_error(request, response);
            }
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
    private void action_request (HttpServletRequest request, HttpServletResponse response) throws DataException, IOException {
        HttpSession s = SecurityLayer.checkSession(request);
        Candidatura c = new CandidaturaImpl();
        int u = (int) s.getAttribute("id_utente");
        TutoreUni tu = new TutoreUniImpl();
        
        c.setStudente(((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(u));
        c.setOffertaTirocinio(((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(SecurityLayer.checkNumeric(request.getParameter("n"))));
        
        if (!(request.getParameter("id_tutore").equals("add"))) {
            tu = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().getTutoreUni(SecurityLayer.checkNumeric(request.getParameter("id_tutore")));
        } else {
            tu.setNome(request.getParameter("nome_tutore"));
            tu.setCognome(request.getParameter("cognome_tutore"));
            tu.setEmail(request.getParameter("email_tutore"));
            tu.setTelefono(request.getParameter("telefono_tutore"));
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().insertTutoreUni(tu);
        }
        
        c.setTutoreUni(tu);
        c.setCfu(Integer.valueOf(request.getParameter("cfu")));
                
        ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().insertCandidatura(c);
        
        response.sendRedirect("home");
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        int n = SecurityLayer.checkNumeric(request.getParameter("n"));
        //OffertaTirocinio tirocinio = (OffertaTirocinio) ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(n);
        List<TutoreUni> tutori = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().getTutori();
        request.setAttribute("tutori", tutori);
        res.activate("richiesta_tirocinio.ftl.html", request, response);
        
    }
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
           (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
}
