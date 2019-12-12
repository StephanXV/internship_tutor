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
import it.univaq.ingweb.framework.security.SecurityLayerException;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.Resoconto;
import org.apache.log4j.Logger;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class Documenti extends InternshipTutorBaseController {
    //logger
    final static Logger logger = Logger.getLogger(Documenti.class);
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_doc_richiesta_convenzione(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws NumberFormatException, DataException, TemplateManagerException {

            //se non è admin non autorizzato
            if (!s.getAttribute("tipologia").equals("ad")) {
                userNotAuthorized(request, response);
                return;
            }

            int id_azienda = SecurityLayer.checkNumeric(request.getParameter("az"));
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_azienda);

            if (az == null) {
                throw new DataException("risorsa non trovata");
            }

            request.setAttribute("azienda", az);
            
            TemplateResult res = new TemplateResult(getServletContext());
            res.activateNoOutline("doc_convenzione.ftl.html", request, response);

    }


    private void action_doc_richiesta_tirocinio(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws NumberFormatException, DataException, TemplateManagerException {

        int id_studente = SecurityLayer.checkNumeric(request.getParameter("st"));
        int id_offerta_tirocinio = SecurityLayer.checkNumeric(request.getParameter("ot"));
        Azienda currentAzienda = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(id_offerta_tirocinio).getAzienda();

        //controllo se l'utente loggato è un azienda e se l'offerta di tirocinio è stata emessa dall'azienda loggata
        if (!s.getAttribute("tipologia").equals("az") || !s.getAttribute("id_utente").equals(currentAzienda.getUtente().getId())) {
            userNotAuthorized(request, response);
        }

        Candidatura rc = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getCandidaturaDAO().getCandidatura(id_studente, id_offerta_tirocinio);

        if (rc == null) {
            throw new DataException("risorsa non trovata");
        }

        request.setAttribute("rc", rc);

        TemplateResult res = new TemplateResult(getServletContext());
        res.activateNoOutline("doc_candidatura.ftl.html", request, response);
    }
    
    private void action_doc_resoconto(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws DataException, NumberFormatException, TemplateManagerException {
            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
            int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
            
            Resoconto resoconto = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getResocontoDAO().getResoconto(id_st, id_ot);
            request.setAttribute("resoconto", resoconto);
            
            Candidatura c = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidatura(id_st, id_ot);
            request.setAttribute("candidatura", c);

            if (resoconto == null || c == null) {
                throw new DataException("Risorsa non trovata");
            }
            
            TemplateResult res = new TemplateResult(getServletContext());
            res.activateNoOutline("doc_resoconto.ftl.html", request, response);
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                
                int tipo = SecurityLayer.checkNumeric(request.getParameter("tipo"));
                switch (tipo) {
                    case 0: // documento di richiesta di convezionamento non firmato
                        //if admin - else utente non autorizzato
                        action_doc_richiesta_convenzione(request, response, s);
                        break;
                        
                    case 1: // documento di richiesta di tirocinio non firmato
                        //if azienda inerente - else utente non autorizzato
                        action_doc_richiesta_tirocinio(request, response, s);
                        break;
                        
                    case 2: // documento di resoconto non firmato
                        //if azienda inerente - else utente non autorizzato
                        action_doc_resoconto(request, response, s);
                }
            } else {
                userNotAuthorized(request, response);
                return;
            }
        } catch (TemplateManagerException ex) {
            logger.error("Exception : ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataException | NumberFormatException  e) {
            logger.error("Risorsa non trovata: ", e);
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "Risorsa non disponibile");
            request.setAttribute("errore", "404 Not Found");
            action_error(request, response);
        }
    }


    private void userNotAuthorized(HttpServletRequest request, HttpServletResponse response) {
        logger.error("Utente non autorizzato");
        request.setAttribute("message", "errore gestito");
        request.setAttribute("title", "Utente non autorizzato");
        request.setAttribute("errore", "401 Unauthorized");
        action_error(request, response);
    }
    
}
