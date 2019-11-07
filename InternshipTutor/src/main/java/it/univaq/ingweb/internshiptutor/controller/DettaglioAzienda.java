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
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuse
 */
public class DettaglioAzienda extends InternshipTutorBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int n = SecurityLayer.checkNumeric(request.getParameter("n"));
        try {
            try {
                action_default(request, response, n);
            } catch (DataException ex) {
                request.setAttribute("exception", ex);
                action_error(request, response);
            }
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, int n) throws TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        Azienda azienda = (Azienda) ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(n);
        List<OffertaTirocinio> tirocini = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOfferteTirocinio(azienda);
        request.setAttribute("tirocini", tirocini);
        request.setAttribute("azienda", azienda);
        request.setAttribute("page_title", "Azienda:" + azienda.getRagioneSociale());
        res.activate("dettaglio_azienda.ftl.html", request, response);
        
    }
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
           (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
}


