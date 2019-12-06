package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

/**
 *
 * @author Stefano Florio
 */
public class Upload extends InternshipTutorBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_upload_convenzione(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        // current timestamp
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String sDate = formatter.format(date);
        
        int id_azienda = SecurityLayer.checkNumeric(request.getParameter("az"));
        Part file_to_upload = request.getPart("convenzionetoupload");
        if (file_to_upload.getContentType().equals("application/pdf")) {
            
            //create a file (with a unique name) and copy the uploaded file to it
            //creiamo un nuovo file (con nome univoco) e copiamoci il file scaricato
            File uploaded_file = File.createTempFile("convenzione_",  sDate +".pdf", new File(getServletContext().getRealPath("") + File.separatorChar + getServletContext().getInitParameter("uploads.directory")));
            
            try (InputStream is = file_to_upload.getInputStream();
                    OutputStream os = new FileOutputStream(uploaded_file)) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = is.read(buffer)) > 0) {
                    os.write(buffer, 0, read);
                }
            }
            response.sendRedirect("richieste_convenzione?convalida=si&az="+id_azienda+"&src="+uploaded_file.getName());
        }
    }
    
    private void action_upload_candidatura(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        // current timestamp
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String sDate = formatter.format(date);
        
        int id_studente = SecurityLayer.checkNumeric(request.getParameter("st"));
        int id_offerta_tirocinio = SecurityLayer.checkNumeric(request.getParameter("ot"));
        Part file_to_upload = request.getPart("candidaturatoupload");
        if (file_to_upload.getContentType().equals("application/pdf")) {
            
            //create a file (with a unique name) and copy the uploaded file to it
            //creiamo un nuovo file (con nome univoco) e copiamoci il file scaricato
            File uploaded_file = File.createTempFile("candidatura_",  sDate +".pdf", new File(getServletContext().getRealPath("") + File.separatorChar + getServletContext().getInitParameter("uploads.directory")));
            
            try (InputStream is = file_to_upload.getInputStream();
                    OutputStream os = new FileOutputStream(uploaded_file)) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = is.read(buffer)) > 0) {
                    os.write(buffer, 0, read);
                }
            }
            response.sendRedirect("gestione_candidati?convalida=si&st="+id_studente+"&ot="+id_offerta_tirocinio+"&src="+uploaded_file.getName());
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!= null) {
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
                if (request.getParameter("tipo").equals("convenzione")) {
                    if (request.getPart("convenzionetoupload") != null) {
                        action_upload_convenzione(request, response);
                    }
                } else if (request.getParameter("tipo").equals("candidatura")) {
                    if (request.getPart("candidaturatoupload") != null) {
                        action_upload_candidatura(request, response);
                    }
                    request.setAttribute("exception", new Exception("Nothing to upload!"));
                    action_error(request, response);
                }
            } else {
                request.setAttribute("exception", "Access denied");
            }
        } catch (NamingException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (SQLException | FileUploadException | NoSuchAlgorithmException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (Exception ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}
