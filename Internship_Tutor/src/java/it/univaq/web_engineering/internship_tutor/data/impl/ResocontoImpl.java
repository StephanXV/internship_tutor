/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.impl;

import it.univaq.web_engineering.internship_tutor.data.model.OffertaTirocinio;
import it.univaq.web_engineering.internship_tutor.data.model.Resoconto;
import it.univaq.web_engineering.internship_tutor.data.model.Studente;

/**
 *
 * @author steph
 */
public class ResocontoImpl implements Resoconto {
    
    private Studente studente;
    private OffertaTirocinio offertaTirocinio;
    private int oreEffettive;
    private String descAttivita;
    private String giudizio;
    private String srcDocResoconto;

    public ResocontoImpl() {
        this.studente = null;
        this.offertaTirocinio = null;
        this.oreEffettive = 0;
        this.descAttivita = "";
        this.giudizio = "";
    }
    
    @Override
    public Studente getStudente() {
        return studente;
    }

    @Override
    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    @Override
    public OffertaTirocinio getOffertaTirocinio() {
        return offertaTirocinio;
    }

    @Override
    public void setOffertaTirocinio(OffertaTirocinio offertaTirocinio) {
        this.offertaTirocinio = offertaTirocinio;
    }

    @Override
    public int getOreEffettive() {
        return oreEffettive;
    }

    @Override
    public void setOreEffettive(int oreEffettive) {
        this.oreEffettive = oreEffettive;
    }

    @Override
    public String getDescAttivita() {
        return descAttivita;
    }

    @Override
    public void setDescAttivita(String descAttivita) {
        this.descAttivita = descAttivita;
    }

    @Override
    public String getGiudizio() {
        return giudizio;
    }

    @Override
    public void setGiudizio(String giudizio) {
        this.giudizio = giudizio;
    }
    
    @Override
    public String getSrcDocResoconto() {
        return srcDocResoconto;
    }

    @Override
    public void setSrcDocResoconto(String srcDocResoconto) {
        this.srcDocResoconto = srcDocResoconto;
    }
}
