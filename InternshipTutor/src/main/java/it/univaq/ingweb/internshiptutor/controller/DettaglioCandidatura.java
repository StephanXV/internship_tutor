package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import java.io.IOException;
import java.time.LocalDate;
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
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
        
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, int id_ot, int id_st, HttpSession s) throws TemplateManagerException {
        try {

            Candidatura candidatura = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidatura(id_st, id_ot);
            if (candidatura == null) {
                throw new DataException("Candidatura non trovata");
            }

            Azienda currentAzienda = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(id_ot).getAzienda();
            //controllo se l'utente loggato è un azienda e se l'offerta di tirocinio è stata emessa dall'azienda loggata
            if (!s.getAttribute("tipologia").equals("az") || !s.getAttribute("id_utente").equals(currentAzienda.getUtente().getId())) {
                userNotAuthorized(request, response);
                return;
            }
            request.setAttribute("candidatura", candidatura);
            
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("dettaglio_candidatura.ftl.html", request, response);
            
        } catch (DataException ex) {
            logger.error("Candidatura non trovata: ", ex);
            request.setAttribute("exception", ex);
                action_error(request, response);
        }
    }

    private void action_salva_date(HttpServletRequest request, HttpServletResponse response, int id_ot, int id_st) throws IllegalArgumentException, IOException, DataException {
            //check con throws IllegalArgument
            LocalDate inizio_tirocinio = SecurityLayer.checkDate(request.getParameter("inizio_tirocinio"));
            LocalDate fine_tirocinio = SecurityLayer.checkDate(request.getParameter("fine_tirocinio"));
            Candidatura c = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidatura(id_st, id_ot);
            c.setInizioTirocinio(inizio_tirocinio);
            c.setFineTirocinio(fine_tirocinio);
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidatura(c);
            response.sendRedirect("dettaglio_candidatura?st="+id_st+"&ot="+id_ot);
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null && "az".equals((String)s.getAttribute("tipologia"))) {
                //check con catch IllegalArgument
                int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
                int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));

                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));

                if (request.getParameter("tipo") != null && request.getParameter("tipo").equals("salva_date")) {
                        action_salva_date(request, response, id_ot, id_st);
                }
                else {
                    action_default(request, response, id_ot, id_st, s);
                }
            } else {
                userNotAuthorized(request, response);
            }
        } catch (TemplateManagerException | IOException | DataException | IllegalArgumentException ex) {
            logger.error("Exception: ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    private void userNotAuthorized(HttpServletRequest request, HttpServletResponse response) {
        logger.error("Utente non autorizzato");
        request.setAttribute("message", "Utente non autorizzato");
        action_error(request, response);
    }
}