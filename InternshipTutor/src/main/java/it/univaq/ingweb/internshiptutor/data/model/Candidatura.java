/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.ingweb.internshiptutor.data.model;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Stefano Florio
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
    
    String getSrcDocCandidatura();
    
    void setSrcDocCandidatura(String src);
    
    int getStatoCandidatura();
    
    void setStatoCandidatura(int sc);
    
    LocalDate getInizioTirocinio();
    
    void setInizioTirocinio(LocalDate it);
    
    LocalDate getFineTirocinio();
    
    void setFineTirocinio(LocalDate ft);
    
    Date getTms();
    
    void setTms(Date tms); 
    
    String getDiploma();
    
    void setDiploma(String d);
    
    String getLaurea();
    
    void setLaurea(String l);
    
    String getDottoratoRicerca();
    
    void setDottoratoRicerca(String dr);
    
    String getSpecializzazione();
    
    void setSpecializzazione(String s);
    
}
