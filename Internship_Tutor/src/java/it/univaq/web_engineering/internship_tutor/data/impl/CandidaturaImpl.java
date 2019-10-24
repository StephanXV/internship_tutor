/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.impl;

import it.univaq.web_engineering.internship_tutor.data.model.Candidatura;
import it.univaq.web_engineering.internship_tutor.data.model.OffertaTirocinio;
import it.univaq.web_engineering.internship_tutor.data.model.Studente;
import it.univaq.web_engineering.internship_tutor.data.model.TutoreUni;
import java.util.Date;

/**
 *
 * @author steph
 */
public class CandidaturaImpl implements Candidatura {
    
    private Studente studente;
    private OffertaTirocinio offertaTirocinio;
    private TutoreUni tutoreUni;
    private int cfu;
    private int oreTirocinio;
    private int statoCandidatura;
    private Date inizioTirocinio;
    private Date fineTirocinio;
    private Date tms;

    public CandidaturaImpl() {
        this.studente = null;
        this.offertaTirocinio = null;
        this.tutoreUni = null;
        this.cfu = 0;
        this.oreTirocinio = 0;
        this.statoCandidatura = 0;
        this.inizioTirocinio = null;
        this.fineTirocinio = null;
        this.tms = null;
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
    public TutoreUni getTutoreUni() {
        return tutoreUni;
    }

    @Override
    public void setTutoreUni(TutoreUni tutoreUni) {
        this.tutoreUni = tutoreUni;
    }

    @Override
    public int getCfu() {
        return cfu;
    }

    @Override
    public void setCfu(int cfu) {
        this.cfu = cfu;
    }

    @Override
    public int getOreTirocinio() {
        return oreTirocinio;
    }

    @Override
    public void setOreTirocinio(int oreTirocinio) {
        this.oreTirocinio = oreTirocinio;
    }

    @Override
    public int getStatoCandidatura() {
        return statoCandidatura;
    }

    @Override
    public void setStatoCandidatura(int statoCandidatura) {
        this.statoCandidatura = statoCandidatura;
    }

    @Override
    public Date getInizioTirocinio() {
        return inizioTirocinio;
    }

    @Override
    public void setInizioTirocinio(Date inizioTirocinio) {
        this.inizioTirocinio = inizioTirocinio;
    }

    @Override
    public Date getFineTirocinio() {
        return fineTirocinio;
    }

    @Override
    public void setFineTirocinio(Date fineTirocinio) {
        this.fineTirocinio = fineTirocinio;
    }

    @Override
    public Date getTms() {
        return tms;
    }

    @Override
    public void setTms(Date tms) {
        this.tms = tms;
    }
    
}
