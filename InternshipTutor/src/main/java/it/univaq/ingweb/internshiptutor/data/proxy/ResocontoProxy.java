/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.proxy;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.dao.OffertaTirocinioDAO;
import it.univaq.ingweb.internshiptutor.data.dao.StudenteDAO;
import it.univaq.ingweb.internshiptutor.data.impl.ResocontoImpl;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Florio
 */
public class ResocontoProxy extends ResocontoImpl {
    
    protected boolean dirty;
    protected int id_studente = 0;
    protected int id_offerta_tirocinio = 0;
    
    protected DataLayer dataLayer;

    public ResocontoProxy(DataLayer d) {
        super();
        this.dirty = false;
        this.dataLayer = d;
        this.id_studente = 0;
        this.id_offerta_tirocinio = 0;
    }

    @Override
    public void setSrcDocResoconto(String srcDocResoconto) {
        super.setSrcDocResoconto(srcDocResoconto);
        this.dirty = true;
    }

    @Override
    public void setGiudizio(String giudizio) {
        super.setGiudizio(giudizio);
        this.dirty = true;
    }

    @Override
    public void setDescAttivita(String descAttivita) {
        super.setDescAttivita(descAttivita);
        this.dirty = true;
    }

    @Override
    public void setOreEffettive(int oreEffettive) {
        super.setOreEffettive(oreEffettive);
        this.dirty = true;
    }

    @Override
    public void setOffertaTirocinio(OffertaTirocinio offertaTirocinio) {
        super.setOffertaTirocinio(offertaTirocinio);
        this.id_offerta_tirocinio = offertaTirocinio.getId();
        this.dirty = true;
    }

    @Override
    public OffertaTirocinio getOffertaTirocinio() {
        if(super.getOffertaTirocinio() == null && id_offerta_tirocinio > 0) {
            try {
                super.setOffertaTirocinio(((OffertaTirocinioDAO) dataLayer.getDAO(OffertaTirocinio.class)).getOffertaTirocinio(id_offerta_tirocinio));
            } catch (DataException ex) {
                Logger.getLogger(ResocontoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getOffertaTirocinio();
    }

    @Override
    public Studente getStudente() {
        //notare come l'oggetto in relazione venga caricato solo su richiesta
        if (super.getStudente() == null && id_studente > 0) {
            try {
                super.setStudente(((StudenteDAO) dataLayer.getDAO(Studente.class)).getStudente(id_studente));
            } catch (DataException ex) {
                Logger.getLogger(ResocontoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //attenzione: l'oggetto caricato viene legato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se l'utente viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione
        return super.getStudente();
    }

    @Override
    public void setStudente(Studente studente) {
        super.setStudente(studente);
        this.id_studente = studente.getUtente().getId();
        this.dirty = true;
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

    public void setId_offerta_tirocinio(int id_offerta_tirocinio) {
        this.id_offerta_tirocinio = id_offerta_tirocinio;
        super.setOffertaTirocinio(null);
    }
    
    
    
    
    
    
}
