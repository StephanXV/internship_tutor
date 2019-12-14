/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.framework.security.SecurityLayer;
import it.univaq.ingweb.framework.result.TemplateManagerException;
import it.univaq.ingweb.framework.result.TemplateResult;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefano Florio
 */
public class Home extends InternshipTutorBaseController {
    //logger (log4J)
    final static Logger logger = Logger.getLogger(Home.class);
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_anonymous(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        request.setAttribute("page_title", "Home anonimo");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("home_anonimo.ftl.html", request, response);
    }
    
    private void action_admin(HttpServletRequest request, HttpServletResponse response)
            throws DataException, TemplateManagerException  {
            
            // lista aziende registrate, ovvero in attesa di convenzione
            List<Azienda> az_registrate = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAziendeByStato(0);
            request.setAttribute("az_registrate", az_registrate);
            if (az_registrate.size() > 0) {
                request.setAttribute("num_az_registrate", az_registrate.size());
            } else {
                request.setAttribute("num_az_registrate", 0);
            }
            
            // lista azinde convenzionate
            List<Azienda> az_convenzionate = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAziendeByStato(1);
            request.setAttribute("az_convenzionate", az_convenzionate);
            if (az_convenzionate.size() > 0) {
                request.setAttribute("num_az_convenzionate", az_convenzionate.size());
            } else {
                request.setAttribute("num_az_convenzionate", 0);
            }
            
            // lista azinde rifiutate
            List<Azienda> az_rifiutate = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAziendeByStato(2);
            request.setAttribute("az_rifiutate", az_rifiutate);
            if (az_rifiutate.size() > 0) {
                request.setAttribute("num_az_rifiutate", az_rifiutate.size());
            } else {
                request.setAttribute("num_az_rifiutate", 0);
            }
            
            
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Dashboard admin");
            res.activate("home_admin.ftl.html", request, response);
    }
    
    private void action_azienda(HttpServletRequest request, HttpServletResponse response, int id_utente) throws IOException, DataException, TemplateManagerException {

            // dati dell'azienda
            Azienda az = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().getAzienda(id_utente);
            
            if (az.getStatoConvenzione() != 0) {
                // controllo che la convenzione non sia scaduta
                LocalDate now = LocalDate.now();
                LocalDate inizio_conv = az.getInizioConvenzione();
                int durata = az.getDurataConvenzione();
                LocalDate fine_conv = inizio_conv.plusMonths(durata);
                // se è scaduta nego l'accesso all'azienda
                if (az.getStatoConvenzione() != 3 && az.getStatoConvenzione() != 2 && now.compareTo(fine_conv) > 0) {
                    az.setStatoConvenzione(3);
                    ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getAziendaDAO().updateAzienda(az);
                    response.sendRedirect("home");
                }
            }
            
            request.setAttribute("azienda", az);
            
            // lista delle offerte di tirocinio attive dell'azienda
            List<OffertaTirocinio> tirocini_attivi = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOfferteTirocinio(az, true);
            request.setAttribute("ot_attive", tirocini_attivi);
            
            // lista delle offerte di tirocinio disattive dell'azienda
            List<OffertaTirocinio> tirocini_disattivi = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getOffertaTirocinioDAO().getOfferteTirocinio(az, false);
            request.setAttribute("ot_disattive", tirocini_disattivi);
            
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Home azienda");
            
            res.activate("home_azienda.ftl.html", request, response);
    }
    
    private void action_studente(HttpServletRequest request, HttpServletResponse response, int id_utente) throws DataException, TemplateManagerException {
            // dati dello studente
            Studente st = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getStudenteDAO().getStudente(id_utente);
            
            // lista delle candidature dello studente
            List<Candidatura> candidature = ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().getCandidature(st);
            List<Candidatura> attesa = new ArrayList<>();
            List<Candidatura> attiva = new ArrayList<>();
            List<Candidatura> finita = new ArrayList<>();
            List<Candidatura> rifiutata = new ArrayList<>();
            
            LocalDate now = LocalDate.now();
            
            for (Candidatura c: candidature){
                
                switch (c.getStatoCandidatura()) {
                    case 0 :
                        attesa.add(c);
                        break;
                        
                    case 1 :
                        // check sulla data di fine tirocinio: se è stata superata il tirocinio cambia stato
                        if (now.compareTo(c.getFineTirocinio()) > 0) {
                            ((InternshipTutorDataLayer)request.getAttribute("datalayer")).getCandidaturaDAO().updateCandidaturaStato(2, c.getStudente().getUtente().getId(), c.getOffertaTirocinio().getId());
                            finita.add(c);
                        } else {
                            attiva.add(c);
                        }
                        break;
                        
                    case 2:
                        finita.add(c);
                        break;
                        
                    case 3:
                        rifiutata.add(c);
                        break;
                        
                    default: break;
                }
            }
            
            request.setAttribute("candidature_attesa", attesa);
            request.setAttribute("candidature_attive", attiva);
            request.setAttribute("candidature_finite", finita);
            request.setAttribute("candidature_rifiutate", rifiutata);
            
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Home studente");
            
            res.activate("home_studente.ftl.html", request, response);
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("activeHome", "active");
            
            HttpSession s = SecurityLayer.checkSession(request);
            if (s != null) {
                int id_utente = (int) s.getAttribute("id_utente");
                request.setAttribute("nome_utente", (String)s.getAttribute("username"));
                request.setAttribute("tipologia", (String)s.getAttribute("tipologia"));

                switch ((String) s.getAttribute("tipologia")) {
                    case "ad":
                        action_admin(request, response);
                        break;
                    case "st":
                        action_studente(request, response, id_utente);
                        break;
                    case "az":
                        action_azienda(request, response, id_utente);
                        break;
                }
            } else {
                action_anonymous(request, response);
            }
        } catch (IOException | TemplateManagerException | DataException ex) {
            logger.error("Exception : ", ex);
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    
}
