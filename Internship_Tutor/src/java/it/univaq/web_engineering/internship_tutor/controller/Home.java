/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.controller;

import it.univaq.web_engineering.internship_tutor.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.dao.InternshipTutorDataLayer;
import it.univaq.web_engineering.internship_tutor.result.FailureResult;
import it.univaq.web_engineering.internship_tutor.result.TemplateManagerException;
import it.univaq.web_engineering.internship_tutor.result.TemplateResult;
import it.univaq.web_engineering.internship_tutor.security.SecurityLayer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author steph
 */
public class Home extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_anonymous(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Home anonimo");
        try {
            res.activate("home_anonimo.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action_admin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Dashboard admin");
        try {
            res.activate("home_admin.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    private void action_azienda(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Dashboard azienda");
        try {
            res.activate("home_azienda.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action_studente(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Home studente");
        try {
            res.activate("home_studente.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s == null) {
                action_anonymous(request, response);
            } else {
                switch ((String) s.getAttribute("tipo")) {
                    case "ad":
                        action_admin(request, response);
                        break;
                    case "st":
                        action_studente(request, response);
                        break;
                    case "az":
                        action_azienda(request, response);
                }
            }
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
  

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
}
