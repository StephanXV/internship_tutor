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
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stefano Florio
 */
public class Test extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, TemplateManagerException {
        try {
            List<Azienda> azConvenzionate = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAziendeByStato(1);
            request.setAttribute("aziende_convenzionate", azConvenzionate);
            List<OffertaTirocinio> tirocini = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOfferteTirocinio(azConvenzionate.get(0));
            request.setAttribute("tirocini", tirocini);
            List<Candidatura> candidature = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidature(tirocini.get(0));
            request.setAttribute("candidature", candidature);
            List<Resoconto> resoconti = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getResocontoDAO().getResoconti(candidature.get(1).getStudente());
            request.setAttribute("resoconti", resoconti);
            List<Valutazione> valutazioni = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getValutazioneDAO().getValutazioni(azConvenzionate.get(0));
            request.setAttribute("valutazioni", valutazioni);
            Studente stefano = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(4);
            request.setAttribute("stefano", stefano);
        } catch (DataException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Aziende");
        res.activate("test.ftl.html", request, response);
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
            action_default(request, response);

        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
    }

}
