package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.framework.security.SecurityLayerException;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;

/**
 *
 * @author Stefano Florio
 */
public class GestioneCandidature extends InternshipTutorBaseController {
    //logger
    final static Logger logger = Logger.getLogger(GestioneCandidature.class);
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws NumberFormatException, DataException, TemplateManagerException {
            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
            OffertaTirocinio ot = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(id_ot);
            if (ot == null) {
                 throw new DataException("Tirocinio non trovato");
            }

            Azienda currentAzienda = ot.getAzienda();
            //controllo se l'utente loggato è un azienda e se l'offerta di tirocinio è stata emessa dall'azienda loggata
            if (!s.getAttribute("tipologia").equals("az") || !s.getAttribute("id_utente").equals(currentAzienda.getUtente().getId())) {
                throw new DataException("L'offerta di tirocinio non appartiene all'azienda loggata");
            }

            request.setAttribute("nome_tirocinio", ot.getTitolo());
            
            List<Candidatura> candidature = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidature(ot);
            List<Candidatura> richieste_cand = new ArrayList<>();
            List<Candidatura> cand_accettate = new ArrayList<>();
            List<Candidatura> tiro_terminati = new ArrayList<>();
            List<Candidatura> richieste_rifiutate = new ArrayList<>();
            
            LocalDate now = LocalDate.now();
            
            for (Candidatura c: candidature){
                
                switch (c.getStatoCandidatura()) {
                    case 0 :
                        richieste_cand.add(c);
                        break;
                        
                    case 1 :
                        if (now.compareTo(c.getFineTirocinio()) > 0) {
                            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaStato(2, c.getStudente().getUtente().getId(), c.getOffertaTirocinio().getId());
                            tiro_terminati.add(c);
                        } else {
                            cand_accettate.add(c);
                        }
                        break;
                        
                    case 2:
                        tiro_terminati.add(c);
                        break;
                        
                    case 3:
                        richieste_rifiutate.add(c);
                        break;
                        
                    default: break;
                }
            }
            
            request.setAttribute("richieste_cand", richieste_cand);
            request.setAttribute("cand_accettate", cand_accettate);
            request.setAttribute("tiro_terminati", tiro_terminati);
            request.setAttribute("richieste_rifiutate", richieste_rifiutate);
            
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("gestione_candidati.ftl.html", request, response);
    }
    
    
    private void action_accetta_cand(HttpServletRequest request, HttpServletResponse response) throws IOException, DataException, SecurityLayerException {
            int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
            String src = SecurityLayer.issetString(request.getParameter("src"));
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaStato(1, id_st, id_ot);
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaDocumento(id_st, id_ot, src);
            response.sendRedirect("dettaglio_candidatura?st="+id_st+"&ot="+id_ot);

    }
    
    private void action_rifiuta_cand(HttpServletRequest request, HttpServletResponse response) throws IOException, DataException {
            int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaStato(3, id_st, id_ot);
            response.sendRedirect("gestione_candidati?ot=" + id_ot);
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null && "az".equals((String)s.getAttribute("tipologia"))) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                if (request.getParameter("convalida") == null) {
                    action_default(request, response, s);
                }
                else switch (request.getParameter("convalida")) {
                    case "si":
                        action_accetta_cand(request, response);
                        break;
                    case "no":
                        action_rifiuta_cand(request, response);
                        break;
                    default:
                        //throw
                        break;
                }
            } else {
                logger.error("utente non autorizzato");
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Utente non autorizzato");
                request.setAttribute("errore", "401 Unauthorized");
                action_error(request, response);
                return;
            }
        } catch (TemplateManagerException | DataException | IOException | SecurityLayerException | NumberFormatException ex) {
            logger.error("Exception: ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}