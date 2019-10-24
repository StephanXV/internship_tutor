/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.model;

import java.util.Date;

/**
 *
 * @author steph
 */
public interface Candidatura {
    
    Studente getStudente();
    
    void setStudente(Studente studente);
    
    OffertaTirocinio getOffertaTirocinio();
    
    void setOffertaTirocinio(OffertaTirocinio ot);
    
    TutoreUni getTutoreUni();
    
    void setTutoreUni(TutoreUni tu);
    
    int getCfu();
    
    void setCfu(int cfu);
    
    int getOreTirocinio();
    
    void setOreTirocinio(int ot);
    
    int getStatoCandidatura();
    
    void setStatoCandidatura(int sc);
    
    Date getInizioTirocinio();
    
    void setInizioTirocinio(Date it);
    
    Date getFineTirocinio();
    
    void setFineTirocinio(Date ft);
    
    Date getTms();
    
    void setTms(Date tms); 
    
}
