/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.ingweb.internshiptutor.data.model;

/**
 *
 * @author steph
 */
public interface TutoreTirocinio {
    
    int getId();
    
    void setId(int id);
    
    String getNome();
    
    void setNome(String n);
    
    String getCognome();
    
    void setCognome(String c);
    
    String getEmail();
    
    void setEmail(String em);
    
    String getTelefono();
    
    void setTelefono(String tel);
    
    Azienda getAzienda();
    
    void setAzienda(Azienda az);
}
