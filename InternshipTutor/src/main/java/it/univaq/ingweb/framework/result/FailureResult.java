/*
* FailureResult.java
*
* Si tratta di una semplice classe che incapsula un TemplateResult per offrire
* un comodo sistema di visualizzazione degli errori. Si basa su un template
* il cui nome deve essere presente nella configurazione dell'applicazione
* (web.xml, parametro view.error_template). In mancanza di questo, degrada
* a un errore http.
*
* This simple class wraps TemplateResult to provide an easy error displaying
* system. It uses a template whose name must be configured as a context
* parameter (web.xml, view.error_template parameter). If no template is found,
* the class uses simple http errors.
*
*/
package it.univaq.ingweb.framework.result;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe Della Penna
 */
public class FailureResult {
    
    //logger
    final static Logger logger = Logger.getLogger(FailureResult.class);
    
    protected ServletContext context;
    private final TemplateResult template;
    
    public FailureResult(ServletContext context) {
        this.context = context;
        template = new TemplateResult(context);
    }
    
    // passo 1: se è stato settato l'attributo 'exception'
    public void activate(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        String message = "Errore sconosciuto, impossibile completare la richiesta";
        /*String eccezione;
        if (exception != null && exception.getMessage() != null) {
            eccezione = exception.getMessage();
        } else if (exception != null) {
            eccezione = exception.getClass().getName();
        } else {
            eccezione = "Unknown Error";
        }
        logger.error("Failure Exception: " + eccezione);  */

        // chiama il passo 2 settando come message l'errore di eccezione
        activate(message, request, response);
    }
    
    // passo 2: se è stato settato l'attributo message
    public void activate(String message, HttpServletRequest request, HttpServletResponse response) {
        try {
            //se abbiamo registrato un template per i messaggi di errore, proviamo a usare quello
            //if an error template has been configured, try it
            if (context.getInitParameter("view.error_template") != null) {
                request.setAttribute("error_message", message);
                template.activateNoOutline(context.getInitParameter("view.error_template"), request, response);
            } else {
                //altrimenti, inviamo un errore HTTP
                //otherwise, use HTTP errors
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            }
        } catch (Exception ex) {
            //se qualcosa va male inviamo un errore HTTP
            //if anything goue wrong, sent an HTTP error
            message += ". In addition, the following exception was generated while trying to display the error page: " + ex.getMessage();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            } catch (IOException ex1) {
                logger.error("Errore: ", ex1);
            }
        }
    }
    
    // creazione degli alert
    public void activateAlert(String alert_msg, HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.error("Activate Alert");
            request.setAttribute("TITLE", "Errore");
            request.setAttribute("MSG", alert_msg);
            request.setAttribute("alert", "danger");
            request.setAttribute("ICON", "fas fa-exclamation-triangle");
            template.activate((String) request.getAttribute("referrer"), request, response);
        } catch (TemplateManagerException ex) {
            //se qualcosa va male inviamo un errore HTTP
            //if anything goue wrong, sent an HTTP error
            String message = "In addition, the following exception was generated while trying to display the error page: " + ex.getMessage();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            } catch (IOException ex1) {
                logger.error("Errore: ", ex1);
            }
        }
    }
}
