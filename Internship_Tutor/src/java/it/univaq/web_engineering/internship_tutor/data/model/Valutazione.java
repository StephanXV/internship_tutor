/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.model;

/**
 *
 * @author steph
 */
public interface Valutazione {
    
    Studente getStudente();
    
    void setStudente(Studente st);
    
    Azienda getAzienda();
    
    void setAzienda(Azienda az);
    
    int getStelle();
    
    void setStelle(int s);
}
