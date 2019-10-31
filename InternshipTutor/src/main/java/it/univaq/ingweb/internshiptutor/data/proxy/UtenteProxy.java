/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.proxy;

import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.impl.UtenteImpl;


/**
 *
 * @author Stefano Florio
 */
public class UtenteProxy extends UtenteImpl {
   
    protected boolean dirty;
    protected DataLayer dataLayer;

    public UtenteProxy(DataLayer d) {
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
    public void setEmail(String em) {
        super.setEmail(em);
        this.dirty = true;
    }
    
    @Override
    public void setUsername(String un) {
        super.setUsername(un);
        this.dirty = true;
    }

    @Override
    public void setPw(String pw) {
        super.setPw(pw);
        this.dirty = true;
    }
    
    @Override
    public void setTipologia(String tipo) {
        super.setTipologia(tipo);
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

}
