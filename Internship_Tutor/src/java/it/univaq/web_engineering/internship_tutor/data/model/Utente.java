/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.model;

/**
 *
 * @author steph
 */
public interface Utente {
    
    int getId();
    
    void setId(int id);
    
    String getEmail();
    
    void setEmail(String em);
    
    String getPw();
    
    void setPw(String pw);
    
    String getTipologia();
    
    void setTipologia(String t);
    
}
