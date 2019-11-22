/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.proxy;


import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.dao.AziendaDAO;
import it.univaq.ingweb.internshiptutor.data.dao.CandidaturaDAO;
import it.univaq.ingweb.internshiptutor.data.dao.TutoreTirocinioDAO;
import it.univaq.ingweb.internshiptutor.data.impl.OffertaTirocinioImpl;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.TutoreTirocinio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Florio
 */
public class OffertaTirocinioProxy extends OffertaTirocinioImpl {
    
    protected boolean dirty;
    protected int id_azienda, id_tutore_tirocinio;
    
    protected DataLayer dataLayer;

    public OffertaTirocinioProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.dirty = false;
        this.id_azienda = 0;
        this.id_tutore_tirocinio = 0;
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
    public void setTutoreTirocinio(TutoreTirocinio tt) {
        super.setTutoreTirocinio(tt);
        this.id_tutore_tirocinio = tt.getId();
        this.dirty = true;
    }

    @Override
    public TutoreTirocinio getTutoreTirocinio() {
        if (super.getTutoreTirocinio() == null && id_tutore_tirocinio > 0) {
            try {
                super.setTutoreTirocinio(((TutoreTirocinioDAO) dataLayer.getDAO(TutoreTirocinio.class)).getTutoreTirocinio(id_tutore_tirocinio));
            } catch (DataException ex) {
                Logger.getLogger(OffertaTirocinioProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getTutoreTirocinio();
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
    public void setAttiva(boolean attiva) {
        super.setAttiva(attiva);
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
    
    public void setId_tutore_tirocinio(int id_tutore_tirocinio) {
        this.id_tutore_tirocinio = id_tutore_tirocinio;
        super.setTutoreTirocinio(null);
    }

}
