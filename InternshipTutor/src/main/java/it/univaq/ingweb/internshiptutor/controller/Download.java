package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.StreamResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class Download extends InternshipTutorBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
           (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_download_convenzione (HttpServletRequest request, HttpServletResponse response)
            throws IOException, NamingException, SQLException {
        String nome_doc = request.getParameter("res");
        StreamResult result = new StreamResult(getServletContext());
        File downloaded_file = new File(getServletContext().getRealPath("") + File.separatorChar + getServletContext().getInitParameter("uploads.directory") + File.separatorChar + nome_doc);
        request.setAttribute("contentType", "application/pdf");
        result.activate(downloaded_file, request, response);
    }
    
    private void action_download_candidatura (HttpServletRequest request, HttpServletResponse response)
            throws IOException, NamingException, SQLException {
        String nome_doc = request.getParameter("res");
        StreamResult result = new StreamResult(getServletContext());
        File downloaded_file = new File(getServletContext().getRealPath("") + File.separatorChar + getServletContext().getInitParameter("uploads.directory") + File.separatorChar + nome_doc);
        request.setAttribute("contentType", "application/pdf");
        result.activate(downloaded_file, request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
            }
            if(request.getParameter("tipo").equals("convenzione"))
                action_download_convenzione(request, response);
            else if(request.getParameter("tipo").equals("candidatura"))
                action_download_candidatura(request, response);
        } catch (NumberFormatException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (NamingException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (SQLException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }

    }
}