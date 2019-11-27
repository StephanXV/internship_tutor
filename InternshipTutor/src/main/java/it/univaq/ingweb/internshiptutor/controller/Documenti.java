/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.Resoconto;
import java.io.IOException;
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
public class Documenti extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_doc_richiesta_convenzione(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException, TemplateManagerException {
        int id_azienda = SecurityLayer.checkNumeric(request.getParameter("az"));
        
        try {
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_azienda);
            request.setAttribute("azienda", az);
           
            TemplateResult res = new TemplateResult(getServletContext());
            res.activateNoOutline("doc_convenzione.ftl.html", request, response);  
        } catch (DataException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action_doc_richiesta_tirocinio(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException, TemplateManagerException {
        int id_studente = SecurityLayer.checkNumeric(request.getParameter("st"));
        int id_offerta_tirocinio = SecurityLayer.checkNumeric(request.getParameter("ot"));
        
        try {
            Candidatura rc = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidatura(id_studente, id_offerta_tirocinio);
            request.setAttribute("rc", rc);
            System.out.println(rc);
           
            TemplateResult res = new TemplateResult(getServletContext());
            res.activateNoOutline("doc_candidatura.ftl.html", request, response);  
        } catch (DataException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action_doc_resoconto(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException, TemplateManagerException {
        int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
        int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
        try {
            Resoconto resoconto = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getResocontoDAO().getResoconto(id_st, id_ot);
            request.setAttribute("resoconto", resoconto);
            Candidatura c = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidatura(id_st, id_ot);
            request.setAttribute("candidatura", c);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activateNoOutline("doc_resoconto.ftl.html", request, response);  
        } catch (DataException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
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
           
            int tipo = SecurityLayer.checkNumeric(request.getParameter("tipo"));
            switch (tipo) {
                    // documento di richiesta di convezionamento non firmato
                    case 0:
                        action_doc_richiesta_convenzione(request, response);
                        break;
                        
                    // documento di richiesta di tirocinio non firmato    
                    case 1:
                        action_doc_richiesta_tirocinio(request, response);
                        break;
                        
                    // documento di resoconto non firmato    
                    case 2:
                        action_doc_resoconto(request, response);
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
