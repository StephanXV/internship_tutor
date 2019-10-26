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
public interface Azienda {
    
    Utente getUtente();
    
    void setUtente(Utente ut);
    
    String getRagioneSociale();
    
    void setRagioneSociale(String rs);
    
    String getIndirizzo();
    
    void setIndirizzo(String ind);
    
    String getCitta();
    
    void setCitta(String c);
    
    String getCap();
    
    void setCap(String c);
    
    String getProvincia();
    
    void setProvincia(String p);
    
    String getRappresentanteLegale();
    
    void setRappresentanteLegale(String rl);
    
    String getPiva();
    
    void setPiva(String piva);
    
    String getForoCompetente();
    
    void setForoCompetente(String fc);
    
    String getSrcDocConvenzionamento();
    
    void setSrcDocConvenzionamento(String src);
    
    String getTematiche();
    
    void setTematiche(String t);
    
    String getCorsoStudio();
    
    void setCorsoStudio(String cs);
    
    int getStatoConvenzione();
    
    void setStatoConvenzione(int sc);
    
    Date getInizioConvenzione();
    
    void setInizioConvenzione(Date ic);
    
    int getDurataConvenzione();
    
    void setDurataConvenzione(int dc);
    
    RespTirocini getRespTirocini();
    
    void setRespTirocini(RespTirocini resp);
        
    List<OffertaTirocinio> getOfferteTirocinio();
    
    void setOfferteTirocinio(List<OffertaTirocinio> ot);
    
    List<Valutazione> getValutazioni();
    
    void setValutazioni(List<Valutazione> val);
    
    void addValutazione(Valutazione val);
    
}
