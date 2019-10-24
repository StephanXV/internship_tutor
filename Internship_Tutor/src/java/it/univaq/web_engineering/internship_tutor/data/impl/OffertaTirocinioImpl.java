/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.impl;

import it.univaq.web_engineering.internship_tutor.data.model.Azienda;
import it.univaq.web_engineering.internship_tutor.data.model.Candidatura;
import it.univaq.web_engineering.internship_tutor.data.model.OffertaTirocinio;
import java.util.List;

/**
 *
 * @author steph
 */
public class OffertaTirocinioImpl implements OffertaTirocinio {
    
    private int id;
    private String luogo;
    private String settore;
    private String orari;
    private int durata;
    private String titolo;
    private String obiettivi;
    private String facilitazioni;
    private String modalita;
    private Azienda azienda;
    private List<Candidatura> candidature;

    public OffertaTirocinioImpl() {
        this.id = 0;
        this.luogo = "";
        this.settore = "";
        this.orari = "";
        this.durata = 0;
        this.titolo = "";
        this.obiettivi = "";
        this.facilitazioni = "";
        this.modalita = "";
        this.azienda = null;
        this.candidature = null;
    }
    
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getLuogo() {
        return luogo;
    }

    @Override
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    @Override
    public String getSettore() {
        return settore;
    }

    @Override
    public void setSettore(String settore) {
        this.settore = settore;
    }

    @Override
    public String getOrari() {
        return orari;
    }

    @Override
    public void setOrari(String orari) {
        this.orari = orari;
    }
    
    @Override
    public int getDurata() {
        return durata;
    }

    @Override
    public void setDurata(int durata) {
        this.durata = durata;
    }

    @Override
    public String getTitolo() {
        return titolo;
    }

    @Override
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    @Override
    public String getObiettivi() {
        return obiettivi;
    }

    @Override
    public void setObiettivi(String obiettivi) {
        this.obiettivi = obiettivi;
    }

    @Override
    public String getFacilitazioni() {
        return facilitazioni;
    }

    @Override
    public void setFacilitazioni(String facilitazioni) {
        this.facilitazioni = facilitazioni;
    }

    @Override
    public String getModalita() {
        return modalita;
    }

    @Override
    public void setModalita(String modalita) {
        this.modalita = modalita;
    }

    @Override
    public Azienda getAzienda() {
        return azienda;
    }

    @Override
    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    @Override
    public List<Candidatura> getCandidature() {
        return candidature;
    }

    @Override
    public void setCandidature(List<Candidatura> candidature) {
        this.candidature = candidature;
    }

    @Override
    public void addCandidatura(Candidatura c) {
        this.candidature.add(c);
    }
    
}
