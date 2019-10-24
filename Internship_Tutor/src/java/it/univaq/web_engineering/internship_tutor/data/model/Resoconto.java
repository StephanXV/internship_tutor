/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.model;

/**
 *
 * @author steph
 */
public interface Resoconto {
    
    Studente getStudente();
    
    void setStudente(Studente st);
    
    OffertaTirocinio getOffertaTirocinio();
    
    void setOffertaTirocinio(OffertaTirocinio ot);
    
    int getOreEffettive();
    
    void setOreEffettive(int oe);
    
    String getDescAttivita();
    
    void setDescAttivita(String da);
    
    String getGiudizio();
    
    void setGiudizio(String g);
    
}
