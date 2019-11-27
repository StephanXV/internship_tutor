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
import java.io.IOException;
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
public class CreaOffertaTirocinio extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            request.setAttribute("referrer", "crea_offerta_tirocinio.ftl.html");
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda((String)s.getAttribute("username"));
                request.setAttribute("azienda", az);
                List<TutoreTirocinio> tutori_tirocinio = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreTirocinioDAO().getTutoriTirocinio(az);
                request.setAttribute("tutori_tirocinio", tutori_tirocinio);
            }
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("crea_offerta_tirocinio.ftl.html", request, response);
        } catch (DataException ex) {
            Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
    
    private void action_crea_offerta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException, DataException {
        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda((String)s.getAttribute("username"));
            OffertaTirocinio ot = new OffertaTirocinioImpl();
            
            //controlli lato server
            if (SecurityLayer.checkNumericBool(request.getParameter("tutore")) && SecurityLayer.checkString(request.getParameter("titolo")) &&
                    SecurityLayer.checkString(request.getParameter("luogo")) && SecurityLayer.checkString(request.getParameter("settore")) &&
                    SecurityLayer.checkNumericBool(request.getParameter("durata")) && SecurityLayer.checkString(request.getParameter("obiettivi")) &&
                    SecurityLayer.checkString(request.getParameter("modalita"))) {
                
                try {
                    ot.setTutoreTirocinio(((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreTirocinioDAO().getTutoreTirocinio(SecurityLayer.checkNumeric(request.getParameter("tutore"))));
                    ot.setAzienda(az);
                    ot.setTitolo(request.getParameter("titolo"));
                    ot.setLuogo(request.getParameter("luogo"));
                    ot.setSettore(request.getParameter("settore"));
                    ot.setDurata(SecurityLayer.checkNumeric(request.getParameter("durata")));
                    ot.setObiettivi(request.getParameter("obiettivi"));
                    ot.setModalita(request.getParameter("modalita"));
                    
                    if (request.getParameter("orari") != null && request.getParameter("orari").length() > 0) {
                        ot.setOrari(request.getParameter("orari"));
                    }
                    
                    if (request.getParameter("facilitazioni") != null && request.getParameter("facilitazioni").length() > 0) {
                        ot.setFacilitazioni(request.getParameter("facilitazioni"));
                    }
                    
                    //insert offerta tirocinio
                    ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().insertOffertaTirocinio(ot);
                    try {
                        response.sendRedirect("home");
                    } catch (IOException e) {
                        request.setAttribute("exception", e);
                        action_error(request, response);
                    }
                    
                } catch (DataException e) {
                    Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, e);
                    request.setAttribute("message", "errore gestito");
                    request.setAttribute("title", "Impossibile creare la nuova offerta di tirocinio");
                    request.setAttribute("errore", "ERRORE");
                    action_error(request, response);
                    return;
                }
                
            } else {
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "I campi inseriti non sono corretti. Riprova!");
                request.setAttribute("errore", "Errore di_convalidazione");
                action_error(request, response);
            }
        }
    }
    
    private void action_tutore_tirocinio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException, DataException {
        HttpSession s = SecurityLayer.checkSession(request);
        if (s!= null) {
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda((String)s.getAttribute("username"));
            if (SecurityLayer.checkString(request.getParameter("name")) && SecurityLayer.checkEmail(request.getParameter("email")) &&
                    SecurityLayer.checkTelefono(request.getParameter("telefono")) && SecurityLayer.checkString(request.getParameter("surname"))) {
                TutoreTirocinio tutore = new TutoreTirocinioImpl();
                tutore.setNome(request.getParameter("name"));
                tutore.setCognome(request.getParameter("surname"));
                tutore.setEmail(request.getParameter("email"));
                tutore.setTelefono(request.getParameter("telefono"));
                tutore.setAzienda(az);
                try {
                    ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreTirocinioDAO().insertTutoreTirocinio(tutore);
                    //result ok
                    request.setAttribute("MSG", "Tutore Aggiunto! Ora potrai inserirlo nella tua richiesta");
                    request.setAttribute("ICON", "fas fa-check");
                    request.setAttribute("alert", "success");
                    action_default(request, response);
                } catch (DataException e) {
                    Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, e);
                    request.setAttribute("message", "errore_convalida");
                    request.setAttribute("errore", "Tutore non aggiunto! Verifica che il tutore non sia già presente");
                    action_error(request, response);
                }
            }
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
            }
            if (request.getParameter("submitOfferta") != null) {
                action_crea_offerta(request, response);
            } else if (request.getParameter("submitTutore") != null) {
                action_tutore_tirocinio(request, response);
            } else {
                action_default(request, response);
            }
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataException ex) {
            Logger.getLogger(CreaOffertaTirocinio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}