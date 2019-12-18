package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Stefano Florio
 */
public class ValutazioneAzienda extends InternshipTutorBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, int id_az, int id_st) throws NumberFormatException, TemplateManagerException, DataException {
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_az);
            request.setAttribute("az", az);
            Valutazione valutazione = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getValutazioneDAO().getValutazione(id_az, id_st);
            if (valutazione != null) {
                System.out.println(""+valutazione.getStelle());
                request.setAttribute("valutazione", valutazione);
            }
            request.setAttribute("id_az", id_az);
            request.setAttribute("id_st", id_st);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("valutazione.ftl.html", request, response);
    }
    
    private void action_valuta(HttpServletRequest request, HttpServletResponse response, int id_az, int id_st) throws IOException, NumberFormatException, DataException {
            Studente st = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(id_st);
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_az);
            Valutazione valutazione = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getValutazioneDAO().createValutazione();
            valutazione.setAzienda(az);
            valutazione.setStudente(st);
            valutazione.setStelle(SecurityLayer.checkNumeric(request.getParameter("rating")));
            int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getValutazioneDAO().insertValutazione(valutazione);
            if (insert != 1) {
                throw new DataException("Inserimento non effettuato");
            }
            response.sendRedirect("home");
    }
    
    private void action_cancella_valutazione(HttpServletRequest request, HttpServletResponse response, int id_az, int id_st) throws NumberFormatException, IOException, DataException {

            int delete = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getValutazioneDAO().deleteValutazione(id_az, id_st);
            
            if (delete != 1) {
                throw new DataException("Cancellazione non effettuata");
            }
            response.sendRedirect("valutazione?st="+id_st+"&az="+id_az);

    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null && s.getAttribute("tipologia").equals("st")) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));

                int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
                int id_az = SecurityLayer.checkNumeric(request.getParameter("az"));

                //check autorizzazione a vedere la risorsa
                int idUtente_check = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(id_st).getUtente().getId();
                if (idUtente_check != (int) s.getAttribute("id_utente")) {
                    userNotAuthorized(request, response);
                    return;
                }

                if (request.getParameter("submit") != null) {
                    action_valuta(request, response, id_az, id_st);
                }
                else if (request.getParameter("delete") != null) {
                    action_cancella_valutazione(request, response, id_az, id_st);
                } else {
                    action_default(request, response, id_az, id_st);
                }
            } else {
                userNotAuthorized(request, response);
                return;
            }
        } catch (TemplateManagerException | IOException | DataException | NumberFormatException ex) {
            logger.error("Exception : ", ex);
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