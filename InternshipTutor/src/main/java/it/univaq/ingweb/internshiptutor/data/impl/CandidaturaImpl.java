package it.univaq.ingweb.internshiptutor.data.impl;

import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Stefano Florio
 */
public class CandidaturaImpl implements Candidatura {
    
    private Studente studente;
    private OffertaTirocinio offertaTirocinio;
    private TutoreUni tutoreUni;
    private int cfu;
    private String diploma;
    private String laurea;
    private String dottoratoRicerca;
    private String specializzazione;
    private int statoCandidatura; // 0=in attesa, 1=accettata/in corso, 2=conclusa, 3=rifiutata
    private String srcDocCandidatura;
    private LocalDate inizioTirocinio;
    private LocalDate fineTirocinio;
    private Date tms;
    
    public CandidaturaImpl() {
        this.studente = null;
        this.offertaTirocinio = null;
        this.tutoreUni = null;
        this.cfu = 0;
        this.statoCandidatura = 0;
        this.inizioTirocinio = null;
        this.fineTirocinio = null;
        this.tms = null;
        this.diploma = null;
        this.laurea = null;
        this.dottoratoRicerca = null;
        this.specializzazione = null;
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
    public String getSrcDocCandidatura() {
        return srcDocCandidatura;
    }
    
    @Override
    public void setSrcDocCandidatura(String srcDocCandidatura) {
        this.srcDocCandidatura = srcDocCandidatura;
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
    public LocalDate getInizioTirocinio() {
        return inizioTirocinio;
    }
    
    @Override
    public void setInizioTirocinio(LocalDate inizioTirocinio) {
        this.inizioTirocinio = inizioTirocinio;
    }
    
    @Override
    public LocalDate getFineTirocinio() {
        return fineTirocinio;
    }
    
    @Override
    public void setFineTirocinio(LocalDate fineTirocinio) {
        this.fineTirocinio = fineTirocinio;
    }
    
    @Override
    public String getDiploma() {
        return diploma;
    }

    @Override
    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }

    @Override
    public String getLaurea() {
        return laurea;
    }

    @Override
    public void setLaurea(String laurea) {
        this.laurea = laurea;
    }

    @Override
    public String getDottoratoRicerca() {
        return dottoratoRicerca;
    }

    @Override
    public void setDottoratoRicerca(String dottoratoRicerca) {
        this.dottoratoRicerca = dottoratoRicerca;
    }

    @Override
    public String getSpecializzazione() {
        return specializzazione;
    }

    @Override
    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }
    
    @Override
    public Date getTms() {
        return tms;
    }
    
    @Override
    public void setTms(Date tms) {
        this.tms = tms;
    }
    
    @Override
    public String toString() {
        return "CandidaturaImpl{" + "studente=" + studente + ", offertaTirocinio=" + offertaTirocinio + ", tutoreUni=" + tutoreUni + ", cfu=" + cfu + ", statoCandidatura=" + statoCandidatura + ", srcDocCandidatura=" + srcDocCandidatura + ", inizioTirocinio=" + inizioTirocinio + ", fineTirocinio=" + fineTirocinio + ", tms=" + tms + '}';
    }
    
}
