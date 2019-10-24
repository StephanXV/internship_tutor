/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.impl;

import it.univaq.web_engineering.internship_tutor.data.model.RespTirocini;

/**
 *
 * @author steph
 */
public class RespTirociniImpl implements RespTirocini {
    
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;

    public RespTirociniImpl() {
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
