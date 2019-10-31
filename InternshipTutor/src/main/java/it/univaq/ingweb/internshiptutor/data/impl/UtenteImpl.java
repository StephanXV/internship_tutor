/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.ingweb.internshiptutor.data.impl;

import it.univaq.ingweb.internshiptutor.data.model.Utente;



/**
 *
 * @author steph
 */
public class UtenteImpl implements Utente {
    
    private int id;
    private String email;
    private String username;
    private String pw;
    private String tipologia;

    public UtenteImpl() {
        this.id = 0;
        this.email = "";
        this.pw = "";
        this.tipologia = "";
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
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public void setUsername(String un){
        this.username = un;
    }

    @Override
    public String getPw() {
        return pw;
    }

    @Override
    public void setPw(String pw) {
        this.pw = pw;
    }

    @Override
    public String getTipologia() {
        return tipologia;
    }

    @Override
    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
    
    
}
