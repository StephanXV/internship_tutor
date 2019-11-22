/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.proxy;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.dao.CandidaturaDAO;
import it.univaq.ingweb.internshiptutor.data.dao.UtenteDAO;
import it.univaq.ingweb.internshiptutor.data.impl.StudenteImpl;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Florio
 */
public class StudenteProxy  extends StudenteImpl {
    
    protected boolean dirty;
    protected int id_utente = 0;
    
    protected DataLayer dataLayer;

    public StudenteProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.dirty = false;
        this.id_utente = 0;
    }

    @Override
    public void setCandidature(List<Candidatura> candidature) {
        super.setCandidature(candidature);
        this.dirty = true;
    }

    @Override
    public List<Candidatura> getCandidature() {
        if (super.getCandidature() == null) {
            try {
                super.setCandidature(((CandidaturaDAO) dataLayer.getDAO(Candidatura.class)).getCandidature(this));
            } catch (DataException ex) {
                Logger.getLogger(StudenteProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCandidature();
    }

    @Override
    public void setHandicap(boolean handicap) {
        super.setHandicap(handicap);
        this.dirty = true;
    }

    @Override
    public void setCorsoLaurea(String corsoLaurea) {
        super.setCorsoLaurea(corsoLaurea);
        this.dirty = true;
    }

    @Override
    public void setSpecializzazione(String specializzazione) {
        super.setSpecializzazione(specializzazione);
        this.dirty = true;
    }

    @Override
    public void setDottoratoRicerca(String dottoratoRicerca) {
        super.setDottoratoRicerca(dottoratoRicerca);
        this.dirty = true;
    }

    @Override
    public void setLaurea(String laurea) {
        super.setLaurea(laurea);
        this.dirty = true;
    }

    @Override
    public void setDiploma(String diploma) {
        super.setDiploma(diploma);
        this.dirty = true;
    }
    
    

    @Override
    public void setTelefono(String telefono) {
        super.setTelefono(telefono);
        this.dirty = true;
    }

    @Override
    public void setProvinciaResidenza(String provinciaResidenza) {
        super.setProvinciaResidenza(provinciaResidenza);
        this.dirty = true;
    }

    @Override
    public void setCapResidenza(String capResidenza) {
        super.setCapResidenza(capResidenza);
        this.dirty = true;
    }

    @Override
    public void setCittaResidenza(String cittaResidenza) {
        super.setCittaResidenza(cittaResidenza);
        this.dirty = true;
    }

    @Override
    public void setProvinciaNascita(String provinciaNascita) {
        super.setProvinciaNascita(provinciaNascita);
        this.dirty = true;
    }

    @Override
    public void setCittaNascita(String cittaNascita) {
        super.setCittaNascita(cittaNascita);
        this.dirty = true;
    }

    @Override
    public void setDataNascita(LocalDate dataNascita) {
        super.setDataNascita(dataNascita);
        this.dirty = true;
    }

    @Override
    public void setCF(String CF) {
        super.setCF(CF);
        this.dirty = true;
    }

    @Override
    public void setCognome(String cognome) {
        super.setCognome(cognome);
        this.dirty = true;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.dirty = true;
    }

    @Override
    public void setUtente(Utente utente) {
        super.setUtente(utente);
        this.id_utente = utente.getId();
        this.dirty = true;
    }

    @Override
    public Utente getUtente() {
        //notare come l'oggetto in relazione venga caricato solo su richiesta
        if (super.getUtente() == null && id_utente > 0) {
            try {
                super.setUtente(((UtenteDAO) dataLayer.getDAO(Utente.class)).getUtente(id_utente));
            } catch (DataException ex) {
                Logger.getLogger(StudenteProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //attenzione: l'utente caricato viene legato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se l'utente viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione
        return super.getUtente();
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
        super.setUtente(null);
    }
    
    
    
    
    
    
    
    
}
