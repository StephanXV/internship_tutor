/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.ingweb.internshiptutor.data.model;

import java.time.LocalDate;
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
    
    LocalDate getDataNascita();
    
    void setDataNascita(LocalDate dn);
    
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
    
    String getDiploma();
    
    void setDiploma(String d);
    
    String getLaurea();
    
    void setLaurea(String l);
    
    String getDottoratoRicerca();
    
    void setDottoratoRicerca(String dr);
    
    String getSpecializzazione();
    
    void setSpecializzazione(String s);
    
    boolean isHandicap();
    
    void setHandicap(boolean hand);
    
    List<Candidatura> getCandidature();
    
    void setCandidature(List<Candidatura> c);
    
}
