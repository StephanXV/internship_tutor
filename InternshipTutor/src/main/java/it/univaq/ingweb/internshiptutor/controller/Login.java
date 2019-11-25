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
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            request.setAttribute("referrer", "login.ftl.html");
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Login");

        //passamano del referrer (prima a login html poi alla servlet che valida il login)
        if (request.getParameter("referrer") != null) {
            request.setAttribute("referrer", request.getParameter("referrer") + "?" + request.getParameter("referrer_res"));
        }

        res.activate("login.ftl.html", request, response);
    }
    
    private void action_login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, TemplateManagerException {

        /*Per vedere la pass criptata di un utente con pass non criptata
        String password = request.getParameter("pw");
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        System.out.println("pass admin " + encryptedPassword);*/

        try {
            if (SecurityLayer.checkString(request.getParameter("username")) && SecurityLayer.checkString("pw")) {

                Utente ut = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtenteByUser(request.getParameter("username"));

                if (ut != null && passwordEncryptor.checkPassword(request.getParameter("pw"), ut.getPw())) {
                    SecurityLayer.createSession(request, ut.getUsername(), ut.getId(), ut.getTipologia());

                    if (request.getParameter("referrer") != null) {
                        if (ut.getTipologia().equals("st")) {
                            response.sendRedirect(request.getParameter("referrer"));
                        } else { //se non è studente --> non autorizzato
                            request.setAttribute("message", "errore gestito");
                            request.setAttribute("title", "Devi essere uno studente per richiedere un tirocinio");
                            request.setAttribute("errore", "401 Unauthorized");
                            action_error(request, response);
                        }
                    } else {
                        response.sendRedirect("home");
                    }
                } else {
                    //notifica errore credenziali
                    request.setAttribute("message", "errore_convalida");
                    request.setAttribute("errore", "Username e/o Password non validi");
                    action_error(request, response);
                }
            } else {
                //errore inserimento campi
                request.setAttribute("message", "errore_convalida");
                request.setAttribute("errore", "I campi inseriti non sono corretti. Riprova!");
                action_error(request, response);
            }
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
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