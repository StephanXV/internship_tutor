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
import it.univaq.ingweb.internshiptutor.data.dao.TutoreUniDAO;
import it.univaq.ingweb.internshiptutor.data.impl.CandidaturaImpl;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Florio
 */
public class CandidaturaProxy extends CandidaturaImpl {
       
    protected boolean dirty;
    protected int id_studente = 0;
    protected int id_offerta_tirocinio = 0; 
    protected int id_tutore_uni = 0;
   
    protected DataLayer dataLayer;

    public CandidaturaProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.dirty = false;
        this.id_studente = 0;
        this.id_offerta_tirocinio = 0;
        this.id_tutore_uni = 0;
    }

    @Override
    public void setTms(Date tms) {
        super.setTms(tms);
        this.dirty = true;
    }

    @Override
    public void setFineTirocinio(Date fineTirocinio) {
        super.setFineTirocinio(fineTirocinio);
        this.dirty = true;
    }

    @Override
    public void setInizioTirocinio(Date inizioTirocinio) {
        super.setInizioTirocinio(inizioTirocinio);
        this.dirty = true;
    }

    @Override
    public void setStatoCandidatura(int statoCandidatura) {
        super.setStatoCandidatura(statoCandidatura);
        this.dirty = true;
    }

    @Override
    public void setSrcDocCandidatura(String srcDocCandidatura) {
        super.setSrcDocCandidatura(srcDocCandidatura);
        this.dirty = true;
    }

    @Override
    public void setOreTirocinio(int oreTirocinio) {
        super.setOreTirocinio(oreTirocinio);
        this.dirty = true;
    }

    @Override
    public void setCfu(int cfu) {
        super.setCfu(cfu);
        this.dirty = true;
    }
    
    @Override
    public TutoreUni getTutoreUni() {
        if(super.getTutoreUni() == null && id_tutore_uni > 0) {
            try {
                super.setTutoreUni(((TutoreUniDAO) dataLayer.getDAO(TutoreUni.class)).getTutoreUni(id_tutore_uni));
            } catch (DataException ex) {
                Logger.getLogger(CandidaturaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getTutoreUni();
    }

    @Override
    public void setTutoreUni(TutoreUni tutoreUni) {
        super.setTutoreUni(tutoreUni);
        this.id_tutore_uni = tutoreUni.getId();
        this.dirty = true;
    }
    
    @Override
    public OffertaTirocinio getOffertaTirocinio() {
        if(super.getOffertaTirocinio() == null && id_offerta_tirocinio > 0) {
            try {
                super.setOffertaTirocinio(((OffertaTirocinioDAO) dataLayer.getDAO(OffertaTirocinio.class)).getOffertaTirocinio(id_offerta_tirocinio));
            } catch (DataException ex) {
                Logger.getLogger(CandidaturaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getOffertaTirocinio();
    }

    @Override
    public void setOffertaTirocinio(OffertaTirocinio offertaTirocinio) {
        super.setOffertaTirocinio(offertaTirocinio);
        this.id_offerta_tirocinio = offertaTirocinio.getId();
        this.dirty = true;
    }
    
    @Override
    public Studente getStudente() {
        //notare come l'oggetto in relazione venga caricato solo su richiesta
        if (super.getStudente() == null && id_studente > 0) {
            try {
                super.setStudente(((StudenteDAO) dataLayer.getDAO(Studente.class)).getStudente(id_studente));
            } catch (DataException ex) {
                Logger.getLogger(CandidaturaProxy.class.getName()).log(Level.SEVERE, null, ex);
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

    public void setId_tutore_uni(int id_tutore_uni) {
        this.id_tutore_uni = id_tutore_uni;
        super.setTutoreUni(null);
    }
    
    
    
}
