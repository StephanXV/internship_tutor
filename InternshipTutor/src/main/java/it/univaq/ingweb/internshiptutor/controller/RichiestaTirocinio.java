/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.impl.CandidaturaImpl;
import it.univaq.ingweb.internshiptutor.data.impl.TutoreUniImpl;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.mail.smtp.SMTPTransport;
import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 *
 * @author Enry
 */

//soggetto a filtro
public class RichiestaTirocinio extends InternshipTutorBaseController {

    //logger
    final static Logger logger = Logger.getLogger(RichiestaTirocinio.class);

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s != null) {
                request.setAttribute("nome_utente", (String) s.getAttribute("username"));
                request.setAttribute("tipologia", (String) s.getAttribute("tipologia"));

                if (!s.getAttribute("tipologia").equals("st")) {
                    request.setAttribute("message", "errore gestito");
                    request.setAttribute("title", "Devi essere uno studente per richiedere un tirocinio");
                    request.setAttribute("errore", "401 Unauthorized");
                    action_error(request, response);
                    return;
                }
            } else {
                throw new IOException("Sessione Nulla, non autorizzato");
            }


            if (request.getParameter("submit") != null) {
                action_request(request, response, s);
            } else if (request.getParameter("submitTutore") != null) {
                action_addAutore(request, response);
            } else {
                action_default(request, response);
            }
        } catch (IOException | TemplateManagerException | DataException e) {
            logger.error("Exception : ", e);
            request.setAttribute("exception", e);
            action_error(request, response);
        }
    }
    
    private void action_addAutore(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        List<TutoreUni> tutori = new ArrayList<>();

        if (SecurityLayer.checkString(request.getParameter("name")) && SecurityLayer.checkEmail(request.getParameter("email")) && SecurityLayer.checkTelefono(request.getParameter("telefono")) && SecurityLayer.checkString(request.getParameter("surname"))) {
            TutoreUni tutore = new TutoreUniImpl();
            tutore.setNome(request.getParameter("name"));
            tutore.setCognome(request.getParameter("surname"));
            tutore.setEmail(request.getParameter("email"));
            tutore.setTelefono(request.getParameter("telefono"));
            try {
                //prendo i tutori per passarli in caso di errore
                tutori = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getTutoreUniDAO().getTutori();

                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().insertTutoreUni(tutore);
                //result ok
                request.setAttribute("MSG", "Tutore Aggiunto! Ora potrai inserirlo nella tua richiesta");
                request.setAttribute("ICON", "fas fa-check");
                request.setAttribute("alert", "success");
                action_default(request, response);
            } catch (DataException e) {
                logger.error("Exception : ", e);
                request.setAttribute("message", "errore_convalida");
                request.setAttribute("errore", "Tutore non aggiunto! Verifica che il tutore non sia già presente");
                request.setAttribute("tutori", tutori);
                action_error(request, response);
            }
        } else {
            logger.error("Campi errati, potenzialmente dannosi");
            request.setAttribute("message", "errore_convalida");
            request.setAttribute("errore", "Verifica i campi inseriti");
            request.setAttribute("tutori", tutori);
            action_error(request, response);
        }
    }
    
    private void action_request (HttpServletRequest request, HttpServletResponse response, HttpSession s) throws IOException {
        int utente_id_session = (int) s.getAttribute("id_utente");

        Candidatura c = new CandidaturaImpl();

        //controlli lato server
        if (SecurityLayer.checkNumericBool(request.getParameter("n")) && SecurityLayer.checkNumericBool(request.getParameter("tutore")) && SecurityLayer.checkNumericBool(request.getParameter("cfu"))) {

            try {
                c.setStudente(((InternshipTutorDataLayer) request.getAttribute("datalayer")).getStudenteDAO().getStudente(utente_id_session));
                c.setOffertaTirocinio(((InternshipTutorDataLayer) request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(Integer.parseInt(request.getParameter("n"))));

                c.setTutoreUni(((InternshipTutorDataLayer) request.getAttribute("datalayer")).getTutoreUniDAO().getTutoreUni(Integer.parseInt(request.getParameter("tutore"))));
                c.setCfu(Integer.parseInt(request.getParameter("cfu")));

                if (request.getParameter("laurea") != null && request.getParameter("laurea").length() > 0) {
                    c.setLaurea(request.getParameter("laurea"));
                }

                if (request.getParameter("dottorato") != null && request.getParameter("dottorato").length() > 0) {
                    c.setDottoratoRicerca(request.getParameter("dottorato"));
                }

                if (request.getParameter("specializzazione") != null && request.getParameter("specializzazione").length() > 0) {
                    c.setSpecializzazione(request.getParameter("specializzazione"));
                }

                if (request.getParameter("diploma") != null && request.getParameter("diploma").length() > 0) {
                    c.setDiploma(request.getParameter("diploma"));
                }

                //insert candidatura
                ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getCandidaturaDAO().insertCandidatura(c);
                //invio email
                this.sendMail(c, request, response);
                response.sendRedirect("home");

            } catch (DataException ex) {
                logger.error("Exception : " + ex);
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Hai già una candidatura attiva per questa offerta di tirocinio");
                request.setAttribute("errore", "ERRORE");
                action_error(request, response);
            }

        } else {
            logger.error("Campi errati, potenzialmente dannosi");
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "I campi inseriti non sono corretti. Riprova!");
            request.setAttribute("errore", "Errore di_convalidazione");
            action_error(request, response);
        }
    }
    
    private void sendMail(Candidatura c, HttpServletRequest request, HttpServletResponse response) {

        String SMTP_SERVER = "smtp.gmail.com";
        String USERNAME = "it.internshiptutor@gmail.com";
        String PASSWORD = "DisimIT15";

        String EMAIL_FROM = "it.internshiptutor@gmail.com";
        String EMAIL_TO_TUTORE_AZIENDA = c.getOffertaTirocinio().getTutoreTirocinio().getEmail();
        String EMAIL_TO_TUTORE_UNIVERSITA = c.getTutoreUni().getEmail();
        String EMAIL_TO_CC = "";

        String EMAIL_SUBJECT = "Richiesta Candidatura di Tirocinio";

        String EMAIL_TEXT_TUTORE_AZIENDA =
                "Lo studente " + c.getStudente().getCognome() + " " + c.getStudente().getNome() +
                ", frequentante il seguente corso di laurea: " + c.getStudente().getCorsoLaurea()
                + ", ha richiesto l'effettuazione del tirocinio: " + c.getOffertaTirocinio().getTitolo()
                + ", per un totale di: " + c.getCfu() + " CFU";

        String EMAIL_TEXT_TUTORE_UNIVERSITA =
                "Lo studente " + c.getStudente().getCognome() + " " + c.getStudente().getNome()
                 + ", frequentante il seguente corso di laurea: " + c.getStudente().getCorsoLaurea()
                 + ", ha richiesto l'effettuazione del tirocinio: " + c.getOffertaTirocinio().getTitolo()
                 + ", presso l'azienda: " + c.getOffertaTirocinio().getAzienda().getRagioneSociale() + ", per un totale di: " + c.getCfu() + " CFU";



        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.put("mail.smtp.auth", "true"); //autenticazione
        prop.put("mail.smtp.port", "587"); // default port gmail
        prop.put("mail.smtp.starttls.enable", "true"); //TLS


        Session session = Session.getInstance(prop, null);
        Message msg_azienda = new MimeMessage(session);
        Message msg_univerista = new MimeMessage(session);

        try {

            /* email al tutore tirocini azienda */
            // from
            msg_azienda.setFrom(new InternetAddress(EMAIL_FROM));

            // to
            msg_azienda.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO_TUTORE_AZIENDA + "," + EMAIL_FROM, false));

            // cc
            msg_azienda.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(EMAIL_TO_CC, false));

            // subject
            msg_azienda.setSubject(EMAIL_SUBJECT);

            // content
            msg_azienda.setText(EMAIL_TEXT_TUTORE_AZIENDA);

            msg_azienda.setSentDate(new Date());



            /* email al tutore tirocini università */
            // from
            msg_univerista.setFrom(new InternetAddress(EMAIL_FROM));

            // to --> la manda in copia anche al mittente per verifica
            msg_univerista.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO_TUTORE_UNIVERSITA + "," + EMAIL_FROM, false));

            // cc
            msg_univerista.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(EMAIL_TO_CC, false));

            // subject
            msg_univerista.setSubject(EMAIL_SUBJECT);

            // content
            msg_univerista.setText(EMAIL_TEXT_TUTORE_UNIVERSITA);

            msg_univerista.setSentDate(new Date());


            ////////////////////    SEND   /////////////////////////

            // Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

            // connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);

            // send both MAIL
            t.sendMessage(msg_azienda, msg_azienda.getAllRecipients()); //msg azienda
            t.sendMessage(msg_univerista, msg_univerista.getAllRecipients()); //msg università

            System.out.println("Response: " + t.getLastServerResponse()); //status

            t.close();



        } catch (MessagingException e) {
            logger.error("Exception : ", e);
            request.setAttribute("exception", e);
            action_error(request, response);
        }



        /*Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String sDate = formatter.format(date);
        try {
            File fileAz = new File(getServletContext().getRealPath("") + File.separatorChar + "mails" + File.separatorChar + sDate + "mail_tutore_az.txt");
            FileWriter fw = new FileWriter(fileAz);
            BufferedWriter bw = new BufferedWriter(fw);
            String message = "From: internship.tutor@univaq.it \n" + "To: " + c.getOffertaTirocinio().getTutoreTirocinio().getEmail() + "\n"
                    + "Message: Lo studente " + c.getStudente().getCognome() + " " + c.getStudente().getNome() +
                    ", frequentante il seguente corso di laurea: " + c.getStudente().getCorsoLaurea()
                    + ", ha richiesto l'effetuazione del tirocinio: " + c.getOffertaTirocinio().getTitolo()
                    + ", per un totale di: " + c.getCfu() + " CFU";
            bw.write(message);
            bw.flush();
            bw.close();
            File fileUni = new File(getServletContext().getRealPath("") + File.separatorChar + "mails" + File.separatorChar + sDate + "mail_tutore_uni.txt");
            FileWriter fw2 = new FileWriter(fileUni);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            String message2 = "From: internship.tutor@univaq.it \n" + "To: " + c.getTutoreUni().getEmail() + "\n"
                    + "Message: Lo studente " + c.getStudente().getCognome() + " " + c.getStudente().getNome() +
                    ", frequentante il seguente corso di laurea: " + c.getStudente().getCorsoLaurea()
                    + ", ha richiesto l'effetuazione del tirocinio: " + c.getOffertaTirocinio().getTitolo()
                    + ", presso l'azienda: " + c.getOffertaTirocinio().getAzienda().getRagioneSociale() + ", per un totale di: " + c.getCfu() + " CFU";
            bw2.write(message2);
            bw2.flush();
            bw2.close();
        }
        catch(IOException e) {
        } */
    }
    
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        int n = SecurityLayer.checkNumeric(request.getParameter("n"));

        //check if offerta tirocinio exists
        OffertaTirocinio offerta = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(n);
        if (offerta == null) {
            throw new DataException("Offerta di tirocinio non trovata");
        }

        List<TutoreUni> tutori = ((InternshipTutorDataLayer) request.getAttribute("datalayer")).getTutoreUniDAO().getTutori();
        request.setAttribute("tutori", tutori);
        request.setAttribute("tirocinio", n);
        res.activate("richiesta_tirocinio.ftl.html", request, response);

    }
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            request.setAttribute("referrer", "richiesta_tirocinio.ftl.html");
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
}
