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
import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Giuseppe Gasbarro
 */
public class RichiestaTirocinio extends InternshipTutorBaseController {
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession s = SecurityLayer.checkSession(request);
        if (s!= null) {
            request.setAttribute("nome_utente", (String)s.getAttribute("username"));
            request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));
            
            if (!s.getAttribute("tipologia").equals("st")) {
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Devi essere uno studente per richiedere un tirocinio");
                request.setAttribute("errore", "401 Unauthorized");
                action_error(request, response);
                return;
            }
        }
        if (request.getParameter("submit") != null) {
            action_request(request, response);
        } else if (request.getParameter("submitTutore") != null) {
            action_addAutore(request, response);
        } else {
            action_default(request, response);
        }
    }
    
    private void action_addAutore(HttpServletRequest request, HttpServletResponse response) {
        if (SecurityLayer.checkString(request.getParameter("name")) && SecurityLayer.checkEmail(request.getParameter("email")) && SecurityLayer.checkTelefono(request.getParameter("telefono")) && SecurityLayer.checkString(request.getParameter("surname"))) {
            TutoreUni tutore = new TutoreUniImpl();
            tutore.setNome(request.getParameter("name"));
            tutore.setCognome(request.getParameter("surname"));
            tutore.setEmail(request.getParameter("email"));
            tutore.setTelefono(request.getParameter("telefono"));
            try {
                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().insertTutoreUni(tutore);
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
    
    private void action_request (HttpServletRequest request, HttpServletResponse response) {
        HttpSession s = SecurityLayer.checkSession(request);
        int u = (int) s.getAttribute("id_utente");
        
        Candidatura c = new CandidaturaImpl();
        
        //controlli lato server
        if (SecurityLayer.checkNumericBool(request.getParameter("n")) && SecurityLayer.checkNumericBool(request.getParameter("tutore")) && SecurityLayer.checkNumericBool(request.getParameter("cfu"))) {
            
            try {
                c.setStudente(((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(u));
                c.setOffertaTirocinio(((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOffertaTirocinio(SecurityLayer.checkNumeric(request.getParameter("n"))));
                
                c.setTutoreUni(((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().getTutoreUni(SecurityLayer.checkNumeric(request.getParameter("tutore"))));
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
                ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().insertCandidatura(c);
                this.sendMail(c);
                try {
                    response.sendRedirect("home");
                } catch (IOException e) {
                    request.setAttribute("exception", e);
                    action_error(request, response);
                }
                
            } catch (DataException ex) {
                request.setAttribute("message", "errore gestito");
                request.setAttribute("title", "Hai già una candidatura attiva per questa offerta di tirocinio");
                request.setAttribute("errore", "ERRORE");
                action_error(request, response);
            }
            
        } else {
            request.setAttribute("message", "errore gestito");
            request.setAttribute("title", "I campi inseriti non sono corretti. Riprova!");
            request.setAttribute("errore", "Errore di_convalidazione");
            action_error(request, response);
        }
    }
    
    private void sendMail(Candidatura c) {
        Date date = new Date();
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
        }
    }
    
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) {
        TemplateResult res = new TemplateResult(getServletContext());
        int n = SecurityLayer.checkNumeric(request.getParameter("n"));
        
        List<TutoreUni> tutori;
        try {
            tutori = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getTutoreUniDAO().getTutori();
            request.setAttribute("tutori", tutori);
            request.setAttribute("tirocinio", n);
            try {
                res.activate("richiesta_tirocinio.ftl.html", request, response);
            } catch (TemplateManagerException e) {
                request.setAttribute("exception", e);
                action_error(request, response);
            }
        } catch (DataException e) {
            request.setAttribute("exception", e);
            action_error(request, response);
        }
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
