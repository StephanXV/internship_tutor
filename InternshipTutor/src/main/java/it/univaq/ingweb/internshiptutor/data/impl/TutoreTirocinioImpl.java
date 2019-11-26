/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.ingweb.internshiptutor.data.impl;

import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.TutoreTirocinio;



/**
 *
 * @author Stefano Florio
 */
public class TutoreTirocinioImpl implements TutoreTirocinio {
    
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private Azienda azienda;

    public TutoreTirocinioImpl() {
        this.id = 0;
        this.nome = "";
        this.cognome = "";
        this.email = "";
        this.telefono = "";
        this.azienda = null;
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

    @Override
    public Azienda getAzienda() {
        return azienda;
    }

    @Override
    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }
    
    
    
}
