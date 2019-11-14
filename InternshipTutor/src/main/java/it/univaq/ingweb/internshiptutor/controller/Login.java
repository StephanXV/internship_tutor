package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import javafx.scene.control.Alert;
import org.jasypt.util.password.BasicPasswordEncryptor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stefano Florio
 */
public class Login extends InternshipTutorBaseController {
    BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
    private String MSG = null;
    private String TITLE = null;
    private String ICON = null;
    private String alertType = null;
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            //(new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
            try {
                request.setAttribute("TITLE", TITLE);
                request.setAttribute("MSG", MSG);
                request.setAttribute("alert", alertType);
                request.setAttribute("ICON", ICON);

                action_default(request,response);
            } catch (TemplateManagerException | IOException | ServletException e) {
                e.printStackTrace();
            }
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Login");
        res.activate("login.ftl.html", request, response);
    }
    
    private void action_login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, TemplateManagerException {

        String password = request.getParameter("pw");
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        System.out.println("pass admin " + encryptedPassword);

        try {
            if (SecurityLayer.checkString(request.getParameter("username")) && SecurityLayer.checkString("pw")) {

                //Utente ut = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(request.getParameter("username"), request.getParameter("pw"));
                Utente ut = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtenteByUser(request.getParameter("username"));

                if (ut != null && passwordEncryptor.checkPassword(request.getParameter("pw"), ut.getPw())) {
                    SecurityLayer.createSession(request, ut.getUsername(), ut.getId(), ut.getTipologia());

                    if (request.getParameter("referrer") != null) {
                        response.sendRedirect(request.getParameter("referrer"));
                    } else {
                        response.sendRedirect("home");
                    }
                } else {
                    //notifica errore credenziali
                    TITLE = "ERRORE";
                    MSG = "Username e/o Password non validi";
                    alertType = "danger";
                    ICON = "fas fa-exclamation-triangle";
                    action_error(request, response);
                }
            } else {
                TITLE = "ERRORE";
                MSG = "I campi inseriti non sono corretti. Riprova!";
                alertType = "danger";
                ICON = "fas fa-exclamation-triangle";
                action_error(request, response);
            }
        } catch (DataException ex) {
            TITLE = "ERRORE";
            MSG = "I campi inseriti non sono corretti. Riprova!";
            alertType = "danger";
            ICON = "fas fa-exclamation-triangle";
            //request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
            if(request.getParameter("login") != null)
                action_login(request, response);
            else 
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