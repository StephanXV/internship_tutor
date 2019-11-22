/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.ingweb.internshiptutor.data.impl;

import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public class StudenteImpl implements Studente {
    private Utente utente;
    private String nome;
    private String cognome;
    private String CF;
    private LocalDate dataNascita;
    private String cittaNascita;
    private String provinciaNascita;
    private String cittaResidenza;
    private String capResidenza;
    private String provinciaResidenza;
    private String telefono;
    private String corsoLaurea;
    private String diploma;
    private String laurea;
    private String dottoratoRicerca;
    private String specializzazione;
    private boolean handicap;
    private List<Candidatura> candidature;

    public StudenteImpl() {
        this.utente = null;
        this.nome = "";
        this.cognome = "";
        this.CF = "";
        this.dataNascita = null;
        this.cittaNascita = "";
        this.provinciaNascita = "";
        this.cittaResidenza = "";
        this.capResidenza = "";
        this.provinciaResidenza = "";
        this.telefono = "";
        this.corsoLaurea = "";
        this.diploma = null;
        this.laurea = null;
        this.dottoratoRicerca = null;
        this.specializzazione = null;
        this.handicap = false;
        this.candidature = null;
    }

    @Override
    public Utente getUtente() {
        return utente;
    }

    @Override
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getCognome() {
        return cognome;
    }

    @Override
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String getCF() {
        return CF;
    }

    @Override
    public void setCF(String CF) {
        this.CF = CF;
    }

    @Override
    public LocalDate getDataNascita() {
        return dataNascita;
    }

    @Override
    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    @Override
    public String getCittaNascita() {
        return cittaNascita;
    }

    @Override
    public void setCittaNascita(String cittaNascita) {
        this.cittaNascita = cittaNascita;
    }
    
    @Override
    public String getProvinciaNascita() {
        return provinciaNascita;
    }

    @Override
    public void setProvinciaNascita(String provinciaNascita) {
        this.provinciaNascita = provinciaNascita;
    }

    @Override
    public String getCittaResidenza() {
        return cittaResidenza;
    }

    @Override
    public void setCittaResidenza(String cittaResidenza) {
        this.cittaResidenza = cittaResidenza;
    }

    @Override
    public String getCapResidenza() {
        return capResidenza;
    }
    
    @Override
    public void setCapResidenza(String capResidenza) {
        this.capResidenza = capResidenza;
    }

    @Override
    public String getProvinciaResidenza() {
        return provinciaResidenza;
    }

    @Override
    public void setProvinciaResidenza(String provinciaResidenza) {
        this.provinciaResidenza = provinciaResidenza;
    }

    @Override
    public String getTelefono() {
        return telefono;
    }

    @Override
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String getCorsoLaurea() {
        return corsoLaurea;
    }

    @Override
    public void setCorsoLaurea(String corsoLaurea) {
        this.corsoLaurea = corsoLaurea;
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
    public boolean isHandicap() {
        return handicap;
    }

    @Override
    public void setHandicap(boolean handicap) {
        this.handicap = handicap;
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
    public String toString() {
        return "StudenteImpl{" + "utente=" + utente + ", nome=" + nome + ", cognome=" + cognome + ", CF=" + CF + ", dataNascita=" + dataNascita + ", cittaNascita=" + cittaNascita + ", provinciaNascita=" + provinciaNascita + ", cittaResidenza=" + cittaResidenza + ", capResidenza=" + capResidenza + ", provinciaResidenza=" + provinciaResidenza + ", telefono=" + telefono + ", corsoLaurea=" + corsoLaurea + ", diploma=" + diploma + ", laurea=" + laurea + ", dottoratoRicerca=" + dottoratoRicerca + ", specializzazione=" + specializzazione + ", handicap=" + handicap + ", candidature=" + candidature + '}';
    }
    
    
       
}