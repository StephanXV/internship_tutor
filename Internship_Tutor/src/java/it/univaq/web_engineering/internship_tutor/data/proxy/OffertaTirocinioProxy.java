/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.proxy;

import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.framework.data.DataLayer;
import it.univaq.web_engineering.internship_tutor.data.dao.AziendaDAO;
import it.univaq.web_engineering.internship_tutor.data.dao.CandidaturaDAO;
import it.univaq.web_engineering.internship_tutor.data.impl.OffertaTirocinioImpl;
import it.univaq.web_engineering.internship_tutor.data.model.Azienda;
import it.univaq.web_engineering.internship_tutor.data.model.Candidatura;
import it.univaq.web_engineering.internship_tutor.data.model.OffertaTirocinio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Florio
 */
public class OffertaTirocinioProxy extends OffertaTirocinioImpl {
    
    protected boolean dirty;
    protected int id_azienda;
    
    protected DataLayer dataLayer;

    public OffertaTirocinioProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.dirty = false;
        this.id_azienda = 0;
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
                Logger.getLogger(OffertaTirocinioProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCandidature();
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
                Logger.getLogger(OffertaTirocinioProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getAzienda();
    }

    @Override
    public void setModalita(String modalita) {
        super.setModalita(modalita);
         this.dirty = true;
    }

    @Override
    public void setFacilitazioni(String facilitazioni) {
        super.setFacilitazioni(facilitazioni);
         this.dirty = true;
    }

    @Override
    public void setObiettivi(String obiettivi) {
        super.setObiettivi(obiettivi);
         this.dirty = true;
    }

    @Override
    public void setTitolo(String titolo) {
        super.setTitolo(titolo);
         this.dirty = true;
    }

    @Override
    public void setDurata(int durata) {
        super.setDurata(durata);
         this.dirty = true;
    }

    @Override
    public void setOrari(String orari) {
        super.setOrari(orari);
         this.dirty = true;
    }

    @Override
    public void setSettore(String settore) {
        super.setSettore(settore);
         this.dirty = true;
    }

    @Override
    public void setLuogo(String luogo) {
        super.setLuogo(luogo);
         this.dirty = true;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
         this.dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
        super.setAzienda(null);
    }  

}
