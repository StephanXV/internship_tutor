/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.proxy;

import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.impl.TutoreUniImpl;


/**
 *
 * @author Stefano Florio
 */
public class TutoreUniProxy extends TutoreUniImpl {
       
    protected boolean dirty;
    protected DataLayer dataLayer;

    public TutoreUniProxy(DataLayer d) {
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

}
