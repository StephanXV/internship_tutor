/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.ingweb.internshiptutor.data.model;

import java.util.List;

/**
 *
 * @author steph
 */
public interface OffertaTirocinio {
    
    int getId();
    
    void setId(int id);
    
    String getLuogo();
    
    void setLuogo(String l);
    
    String getSettore();
    
    void setSettore(String s);
    
    String getOrari();
    
    void setOrari(String o);
    
    int getDurata();
    
    void setDurata(int d);
    
    String getTitolo();
    
    void setTitolo(String t);
    
    String getObiettivi();
    
    void setObiettivi(String ob);
    
    String getModalita();
    
    void setModalita(String mod);
    
    String getFacilitazioni();
    
    void setFacilitazioni(String f);
    
    Azienda getAzienda();
    
    void setAzienda(Azienda az);
    
    List<Candidatura> getCandidature();
    
    void setCandidature(List<Candidatura> c);
    
    void addCandidatura(Candidatura c);
    
}
