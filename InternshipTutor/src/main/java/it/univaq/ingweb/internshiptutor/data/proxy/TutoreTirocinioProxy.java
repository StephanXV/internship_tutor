/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.proxy;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.dao.AziendaDAO;
import it.univaq.ingweb.internshiptutor.data.impl.TutoreTirocinioImpl;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Stefano Florio
 */
public class TutoreTirocinioProxy extends TutoreTirocinioImpl {
       
    protected boolean dirty;
    protected DataLayer dataLayer;
    protected int id_azienda = 0;

    public TutoreTirocinioProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.dirty = false;
    }

  
    @Override
    public void setId(int id) {
        super.setId(id);
        this.dirty = true;
    }


    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.dirty = true;
    }

    @Override
    public void setCognome(String cognome) {
        super.setCognome(cognome);
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
                Logger.getLogger(OffertaTirocinioProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getAzienda();
    }
    
    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        this.dirty = true;
    }

    @Override
    public void setTelefono(String telefono) {
        super.setTelefono(telefono);
        this.dirty = true;
    }


    //METODI DEL PROXY
    //PROXY-ONLY METHODS

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }
    
     public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
        super.setAzienda(null);
    }  

}
