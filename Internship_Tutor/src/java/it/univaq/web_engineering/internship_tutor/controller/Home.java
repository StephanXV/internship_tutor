/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.controller;

import it.univaq.web_engineering.internship_tutor.data.DataException;
import it.univaq.web_engineering.internship_tutor.result.TemplateManagerException;
import it.univaq.web_engineering.internship_tutor.result.TemplateResult;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author steph
 */
public class Home extends HttpServlet {
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("home.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
  

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }   
}
