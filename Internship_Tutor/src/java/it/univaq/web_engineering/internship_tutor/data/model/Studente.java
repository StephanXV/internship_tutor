/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author steph
 */
public interface Studente {
    
    Utente getUtente();
    
    void setUtente(Utente ut);
    
    String getNome();
    
    void setNome(String n);
    
    String getCognome();
    
    void setCognome(String c);
    
    String getCF();
    
    void setCF(String cf);
    
    Date getDataNascita();
    
    void setDataNascita(Date dn);
    
    String getCittaNascita();
    
    void setCittaNascita(String cn);
    
    String getProvinciaNascita();
    
    void setProvinciaNascita(String pn);
    
    String getCittaResidenza();
    
    void setCittaResidenza(String cr);
    
    String getProvinciaResidenza();
    
    void setProvinciaResidenza(String pr);
    
    String getCapResidenza();
    
    void setCapResidenza(String cr);
    
    String getTelefono();
    
    void setTelefono(String tel);
    
    String getCorsoLaurea();
    
    void setCorsoLaurea(String cl);
    
    boolean isHandicap();
    
    void setHandicap(boolean hand);
    
    List<Candidatura> getCandidature();
    
    void setCandidature(List<Candidatura> c);
    
}
