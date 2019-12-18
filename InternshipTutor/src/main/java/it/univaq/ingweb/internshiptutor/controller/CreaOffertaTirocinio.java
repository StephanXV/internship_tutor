package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.impl.OffertaTirocinioImpl;
import it.univaq.ingweb.internshiptutor.data.impl.TutoreTirocinioImpl;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.TutoreTirocinio;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class CreaOffertaTirocinio extends InternshipTutorBaseController {

    //logger
    final static Logger logger = Logger.getLogger(CreaOffertaTirocinio.class);
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else if (request.getAttribute("message") != null) {
            
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        } else if (request.getAttribute("alert_msg") != null) {
            request.setAttribute("referrer", "crea_offerta_tirocinio.ftl.html");
            (new FailureResult(getServletContext())).activateAlert((String) request.getAttribute("alert_msg"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws TemplateManagerException, DataException {
        
        Azienda az = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getAziendaDAO().getAzienda((String) s.getAttribute("username"));
        request.setAttribute("azienda", az);
        List<TutoreTirocinio> tutori_tirocinio = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getTutoreTirocinioDAO().getTutoriTirocinio(az);
        request.setAttribute("tutori_tirocinio", tutori_tirocinio);
        
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("crea_offerta_tirocinio.ftl.html", request, response);
    }
    
    private void action_crea_offerta(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws IOException{
        try {
            
            Azienda az = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getAziendaDAO().getAzienda((String) s.getAttribute("username"));
            OffertaTirocinio ot = new OffertaTirocinioImpl();
            
            //controlli lato server
            if (SecurityLayer.checkNumericBool(request.getParameter("tutore")) && SecurityLayer.checkString(request.getParameter("titolo")) &&
                    SecurityLayer.checkString(request.getParameter("luogo")) && SecurityLayer.checkString(request.getParameter("settore")) &&
                    SecurityLayer.checkNumericBool(request.getParameter("durata")) && SecurityLayer.checkString(request.getParameter("obiettivi")) &&
                    SecurityLayer.checkString(request.getParameter("modalita"))) {
                
                
                ot.setTutoreTirocinio(((InternshipTutorDataLayer) request.getAttribute("datalayer")).getTutoreTirocinioDAO().getTutoreTirocinio(Integer.parseInt(request.getParameter("tutore"))));
                ot.setAzienda(az);
                ot.setTitolo(request.getParameter("titolo"));
                ot.setLuogo(request.getParameter("luogo"));
                ot.setSettore(request.getParameter("settore"));
                ot.setDurata(Integer.parseInt(request.getParameter("durata")));
                ot.setObiettivi(request.getParameter("obiettivi"));
                ot.setModalita(request.getParameter("modalita"));
                
                if (request.getParameter("orari") != null && request.getParameter("orari").length() > 0) {
                    ot.setOrari(request.getParameter("orari"));
                }
                
                if (request.getParameter("facilitazioni") != null && request.getParameter("facilitazioni").length() > 0) {
                    ot.setFacilitazioni(request.getParameter("facilitazioni"));
                }
                
                //insert offerta tirocinio
                ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getOffertaTirocinioDAO().insertOffertaTirocinio(ot);
                response.sendRedirect("home");
                
            }
            
        } catch (DataException e) {
            logger.error("impossibile creare offerta di tirocinio: " + e);
            request.setAttribute("exception", e);
            action_error(request, response);
        }
    }
    
    private void action_tutore_tirocinio(HttpServletRequest request, HttpServletResponse response,HttpSession s) throws TemplateManagerException {
        try {
            Azienda az = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getAziendaDAO().getAzienda((String) s.getAttribute("username"));
            
            if (SecurityLayer.checkString(request.getParameter("name")) && SecurityLayer.checkEmail(request.getParameter("email")) &&
                    SecurityLayer.checkTelefono(request.getParameter("telefono")) && SecurityLayer.checkString(request.getParameter("surname"))) {
                TutoreTirocinio tutore = new TutoreTirocinioImpl();
                tutore.setNome(request.getParameter("name"));
                tutore.setCognome(request.getParameter("surname"));
                tutore.setEmail(request.getParameter("email"));
                tutore.setTelefono(request.getParameter("telefono"));
                tutore.setAzienda(az);
                
                ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getTutoreTirocinioDAO().insertTutoreTirocinio(tutore);
                //result ok
                request.setAttribute("MSG", "Tutore Aggiunto! Ora potrai inserirlo nella tua richiesta");
                request.setAttribute("ICON", "fas fa-check");
                request.setAttribute("alert", "success");
                action_default(request, response, s);
            }
        } catch (DataException ex) {
            logger.error("Tutore non aggiunto: ", ex);
            request.setAttribute("alert_msg", "Impossibile inserire il tutore, verifica che non sia già presente");
            action_error(request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null && "az".equals(s.getAttribute("tipologia"))) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                if (request.getParameter("submitOfferta") != null) {
                    action_crea_offerta(request, response, s);
                } else if (request.getParameter("submitTutore") != null) {
                    action_tutore_tirocinio(request, response, s);
                } else {
                    action_default(request, response, s);
                }
            } else {
                logger.error("Utente non autorizzato");
                request.setAttribute("message", "Utente non autorizzato");
                action_error(request, response);
                return;
            }
        } catch (TemplateManagerException | IOException | DataException ex) {
            logger.error("Exception : ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}