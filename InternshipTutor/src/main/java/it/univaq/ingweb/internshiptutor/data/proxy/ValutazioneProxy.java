/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.proxy;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.dao.AziendaDAO;
import it.univaq.ingweb.internshiptutor.data.dao.StudenteDAO;
import it.univaq.ingweb.internshiptutor.data.impl.ValutazioneImpl;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Florio
 */
public class ValutazioneProxy extends ValutazioneImpl {
    
    protected boolean dirty;
    protected int id_studente = 0;
    protected int id_azienda = 0;
    
    protected DataLayer dataLayer;
    
    public ValutazioneProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.dirty = false;
        this.id_azienda = 0;
        this.id_studente = 0;
    }

    @Override
    public void setStelle(int stelle) {
        super.setStelle(stelle);
        this.dirty = true;
    }

    @Override
    public void setAzienda(Azienda azienda) {
        super.setAzienda(azienda);
        this.id_azienda = azienda.getUtente().getId();
        this.dirty = true;
    }

    @Override
    public Azienda getAzienda() {
        if (super.getAzienda() == null && id_azienda > 0) {
            try {
                super.setAzienda(((AziendaDAO) dataLayer.getDAO(Azienda.class)).getAzienda(id_azienda));
            } catch (DataException ex) {
                Logger.getLogger(ValutazioneProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getAzienda();
    }

    @Override
    public void setStudente(Studente studente) {
        super.setStudente(studente);
        this.id_studente = studente.getUtente().getId();
        this.dirty = true;
    }

    @Override
    public Studente getStudente() {
         //notare come l'oggetto in relazione venga caricato solo su richiesta
        if (super.getStudente() == null && id_studente > 0) {
            try {
                super.setStudente(((StudenteDAO) dataLayer.getDAO(Studente.class)).getStudente(id_studente));
            } catch (DataException ex) {
                Logger.getLogger(ValutazioneProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //attenzione: l'oggetto caricato viene legato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se l'utente viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione
        return super.getStudente();
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setId_studente(int id_studente) {
        this.id_studente = id_studente;
        super.setStudente(null);
    }

    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
        super.setAzienda(null);
    }
    
    
    
    
}
