/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.ingweb.internshiptutor.data.impl;

import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;


/**
 *
 * @author steph
 */
public class TutoreUniImpl implements TutoreUni {
    
     private int id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;

    public TutoreUniImpl() {
        this.id = 0;
        this.nome = "";
        this.cognome = "";
        this.email = "";
        this.telefono = "";
    }
    
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
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
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getTelefono() {
        return telefono;
    }

    @Override
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
}
