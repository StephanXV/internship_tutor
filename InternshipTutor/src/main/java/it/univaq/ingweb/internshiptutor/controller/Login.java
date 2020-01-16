package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import org.apache.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Enrico Monte
 */
public class Login extends InternshipTutorBaseController {

    //logger
    final static Logger logger = Logger.getLogger(Login.class);
    
    BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else if (request.getAttribute("message") != null) {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        } else if (request.getAttribute("alert_msg") != null) {
            request.setAttribute("referrer", "login.ftl.html");
            (new FailureResult(getServletContext())).activateAlert((String) request.getAttribute("alert_msg"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Login");
        
        //passamano del referrer richiesta tirocinio (prima a login html poi alla servlet che valida il login)
        if (request.getParameter("referrer") != null && !request.getParameter("referrer").equals("login")) { //il != login.html perchè quando c'è un errore passo il referrer per farlo tornare a lui
            request.setAttribute("referrer", request.getParameter("referrer") + "?" + request.getParameter("referrer_res"));
        }
        
        res.activate("login.ftl.html", request, response);
    }
    
    private void action_login(HttpServletRequest request, HttpServletResponse response) throws IOException, DataException, TemplateManagerException {
        
        /*Per vedere la pass criptata di un utente con pass non criptata
        String password = request.getParameter("pw");
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        System.out.println("pass admin " + encryptedPassword);*/
        if (SecurityLayer.checkString(request.getParameter("username")) && SecurityLayer.checkString("pw")) {
            
            Utente ut = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtenteByUser(request.getParameter("username"));
            
            if (ut != null && passwordEncryptor.checkPassword(request.getParameter("pw"), ut.getPw())) {
                SecurityLayer.createSession(request, ut.getUsername(), ut.getId(), ut.getTipologia());
                
                //per richiesta tirocinio vedo il referrer
                if (request.getParameter("referrer") != null && !request.getParameter("referrer").equals("login.ftl.html")) {
                    if (ut.getTipologia().equals("st")) {
                        response.sendRedirect(request.getParameter("referrer"));
                    } else { //se non è studente --> non autorizzato
                        logger.error("Utente non autorizzato a richiedere il tirocinio");
                        request.setAttribute("message", "Utente non autorizzato");
                        action_error(request, response);
                        return;
                    }
                } else { //se non c'è un reffer vai a home (accesso normale)
                    response.sendRedirect("home");
                }
            } else {
                //notifica errore credenziali
                logger.error("Credenziali Errate");
                request.setAttribute("alert_msg", "Username e/o Password non validi");
                action_error(request, response);
            }
        } else {
            //errore inserimento campi
            logger.error("Errore inserimento campi");
            request.setAttribute("alert_msg", "I campi inseriti non sono corretti. Riprova!");
            action_error(request, response);
        
        }
    }
   
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s == null) {
                if (request.getParameter("login") != null)
                    action_login(request, response);
                else
                    action_default(request, response);
            } else {
                request.setAttribute("message", "Utente già autenticato; per cambiare account effettuare prima il logout");
                action_error(request, response);
            }
        } catch (IOException | TemplateManagerException | DataException ex) {
            logger.error("Exception : ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
            
        }
    }
    
}