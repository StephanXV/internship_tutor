package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.framework.security.SecurityLayerException;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class GestioneCandidature extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
        try {
            OffertaTirocinio ot = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(id_ot);
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
            
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
    
    private void action_accetta_cand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        try {
            int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
            String src = SecurityLayer.issetString(request.getParameter("src"));
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaStato(1, id_st, id_ot);
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaDocumento(id_st, id_ot, src);
            response.sendRedirect("dettaglio_candidatura?st="+id_st+"&ot="+id_ot);
        } catch (DataException ex) {
            request.setAttribute("message", "Unable to update candidatura");
            action_error(request, response);
        } catch (SecurityLayerException ex) {
            Logger.getLogger(GestioneCandidature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Parametro errato");
        }
    }
    
    private void action_rifiuta_cand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        try {
            int id_st = SecurityLayer.checkNumeric(request.getParameter("st"));
            int id_ot = SecurityLayer.checkNumeric(request.getParameter("ot"));
            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaStato(3, id_st, id_ot);
            response.sendRedirect("gestione_candidati?ot=" + id_ot);
        } catch (DataException ex) {
            request.setAttribute("message", "Unable to update candidatura");
            action_error(request, response);
        } catch (NumberFormatException ex) {
            request.setAttribute("message", "Parametro errato");
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null && "az".equals((String)s.getAttribute("tipologia"))) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                if (null == request.getParameter("convalida")) {
                    action_default(request, response);
                }
                else switch (request.getParameter("convalida")) {
                    case "si":
                        action_accetta_cand(request, response);
                        break;
                    case "no":
                        action_rifiuta_cand(request, response);
                        break;
                    default:
                        break;
                }
            } else {
                request.setAttribute("message", "Access denied");
            }
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}