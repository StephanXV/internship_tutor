package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.framework.security.SecurityLayerException;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Resoconto;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
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
public class CompilaResoconto extends InternshipTutorBaseController {

    //logger
    final static Logger logger = Logger.getLogger(CompilaResoconto.class);
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws TemplateManagerException, DataException, SecurityLayerException {

            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
            int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));

            OffertaTirocinio ot = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(id_ot);
            Azienda az = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getAziendaDAO().getAzienda((s.getAttribute("id_utente").toString()));

            if (!az.getOfferteTirocinio().contains(ot)) {
                logger.error("Utente non autorizzato");
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Utente non autorizzato");
                request.setAttribute("errore", "401 Unauthorized");
                action_error(request, response);
                return;
            }

            Resoconto resoconto = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getResocontoDAO().getResoconto(id_st, id_ot);

            if (ot != null && resoconto != null) {
                request.setAttribute("resoconto", resoconto);
                request.setAttribute("id_ot", id_ot);
                request.setAttribute("id_st", id_st);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("compila_resoconto.ftl.html", request, response);
            } else {
                logger.error("Risorse non trovate");
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Risorsa non trovata");
                request.setAttribute("errore", "404 Not Found");
                action_error(request, response);
            }


    }
    
    private void action_invia_resoconto(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws IOException, DataException, SecurityLayerException {

            int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));

            Studente st = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(id_st);
            OffertaTirocinio ot = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(id_ot);
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda((String)s.getAttribute("id_utente"));

            if (!az.getOfferteTirocinio().contains(ot)) {
                logger.error("Utente non autorizzato");
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Utente non autorizzato");
                request.setAttribute("errore", "401 Unauthorized");
                action_error(request, response);
                return;
            }
            
            Resoconto resoconto = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getResocontoDAO().createResoconto();
            if (st != null && ot != null && resoconto != null) {
                resoconto.setOreEffettive(SecurityLayer.checkNumeric(request.getParameter("ore_effettive")));
                resoconto.setDescAttivita(SecurityLayer.issetString(request.getParameter("desc_attivita")));
                resoconto.setGiudizio(SecurityLayer.issetString(request.getParameter("giudizio")));
                resoconto.setStudente(st);
                resoconto.setOffertaTirocinio(ot);
                int insert = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getResocontoDAO().insertResoconto(resoconto);
                if (insert != 1) {
                    logger.error("Impossibile inserire resoconto tirocinio");
                    request.setAttribute("message", "errore gestito");
                    request.setAttribute("title", "Errore di compilazione campi");
                    request.setAttribute("errore", "I dati del resoconto non sono validi, riprova");
                    action_error(request, response);
                    return;
                }
                response.sendRedirect("gestione_candidati?ot="+id_ot);
            } else {
                logger.error("Risorsa non trovata");
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Risorsa non trovata");
                request.setAttribute("errore", "404 NOT FOUND");
                action_error(request, response);
                return;
            }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null && "az".equals(s.getAttribute("tipologia"))) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                if (request.getParameter("submit") != null) {
                    action_invia_resoconto(request, response, s);
                }
                else
                    action_default(request, response, s);
            } else {
                logger.error("Utente non autorizzato");
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Utente non autorizzato");
                request.setAttribute("errore", "401 Unauthorized");
                action_error(request, response);
                return;
            }
        } catch (TemplateManagerException | DataException | IOException | SecurityLayerException ex) {
            logger.error("Exception : ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}