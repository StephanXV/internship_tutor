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

    private String MSG = null;
    private String TITLE = null;
    private String ICON = null;
    private String alertType = null;

    protected ServletContext context;
    private final TemplateResult template;

    public FailureResult(ServletContext context) {
        this.context = context;
        template = new TemplateResult(context);
    }

    /* se c'è un eccezione */
    public void activate(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("err", "Errore Sconosciuto");
            request.setAttribute("type_err", "Impossibile completare la richiesta.");
            template.activate("error.ftl.html", request, response);
        } catch (TemplateManagerException e) { //se non trovo il template
            Logger.getLogger(FailureResult.class.getName()).error("Exception: ", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(FailureResult.class.getName()).error("Exception: ", e);
            }

        }
    }


    public void activate(String message, HttpServletRequest request, HttpServletResponse response) {
        /* se sono errori di convalidazione */
        if (request.getAttribute("message").equals("errore_convalida") && request.getAttribute("referrer") != null) {
            try {
                messageError((String) request.getAttribute("errore"));
                request.setAttribute("TITLE", TITLE);
                request.setAttribute("MSG", MSG);
                request.setAttribute("alert", alertType);
                request.setAttribute("ICON", ICON);

                template.activate((String) request.getAttribute("referrer"), request, response);
            } catch (Exception ex) {
                try {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (IOException e) {
                    Logger.getLogger(FailureResult.class.getName()).error("EXCEPTION: ", e);
                }
            }
        } else { /* se sono errori gestiti */
            try {
                request.setAttribute("err", (String) request.getAttribute("errore"));
                request.setAttribute("type_err", (String) request.getAttribute("title"));

                template.activate("error.ftl.html", request, response);
            } catch (Exception ex) {
                try {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (IOException e) {
                    Logger.getLogger(FailureResult.class.getName()).error("EXCEPTION: ", e);
                }
            }
        }

    }


    protected void messageError(String msg){
            TITLE = "ERRORE";
            MSG = msg;
            alertType = "danger";
            ICON = "fas fa-exclamation-triangle";
    }
}
