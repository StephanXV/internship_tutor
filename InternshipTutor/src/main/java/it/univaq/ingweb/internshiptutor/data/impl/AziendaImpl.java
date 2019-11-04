package it.univaq.ingweb.internshiptutor.data.impl;

import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.RespTirocini;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import it.univaq.ingweb.internshiptutor.data.model.Valutazione;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public class AziendaImpl implements Azienda {
    
    private Utente utente;
    private String ragioneSociale;
    private String indirizzo;
    private String citta;
    private String cap;
    private String provincia;
    private String rappresentanteLegale;
    private String piva;
    private String foroCompetente;
    private String srcDocConvenzione;
    private String tematiche;
    private String corsoStudio;
    private int statoConvenzione; // 0=in attesa, 1=convenzionata, 2=rifiutata, 3=scaduta
    private Date inizioConvenzione;
    private int durataConvenzione;
    private RespTirocini respTirocini;
    private List<OffertaTirocinio> offerteTirocinio;
    private List<Valutazione> valutazioni;

    public AziendaImpl(){
        this.utente = null;
        this.ragioneSociale = "";
        this.indirizzo = "";
        this.citta = "";
        this.cap = "";
        this.provincia = "";
        this.rappresentanteLegale = "";
        this.piva = "";
        this.foroCompetente = "";
        this.srcDocConvenzione = "";
        this.tematiche = "";
        this.corsoStudio = "";
        this.statoConvenzione = 0;
        this.inizioConvenzione = null;
        this.durataConvenzione = 0;
        this.respTirocini = null;
        this.offerteTirocinio = null;
        this.valutazioni = null;
    }
    
    @Override
    public Utente getUtente() {
        return utente;
    }

    @Override
    public void setUtente(Utente ut) {
        this.utente = ut;
    }

    @Override
    public String getRagioneSociale() {
        return ragioneSociale;
    }

    @Override
    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    @Override
    public String getIndirizzo() {
        return indirizzo;
    }

    @Override
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @Override
    public String getCitta() {
        return citta;
    }

    @Override
    public void setCitta(String citta) {
        this.citta = citta;
    }

    @Override
    public String getCap() {
        return cap;
    }

    @Override
    public void setCap(String cap) {
        this.cap = cap;
    }

    @Override
    public String getProvincia() {
        return provincia;
    }

    @Override
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    @Override
    public String getRappresentanteLegale() {
        return rappresentanteLegale;
    }

    @Override
    public void setRappresentanteLegale(String rappresentanteLegale) {
        this.rappresentanteLegale = rappresentanteLegale;
    }

    @Override
    public String getPiva() {
        return piva;
    }

    @Override
    public void setPiva(String piva) {
        this.piva = piva;
    }

    @Override
    public String getForoCompetente() {
        return foroCompetente;
    }

    @Override
    public void setForoCompetente(String foroCompetente) {
        this.foroCompetente = foroCompetente;
    }

    @Override
    public String getSrcDocConvenzione() {
        return srcDocConvenzione;
    }

    @Override
    public void setSrcDocConvenzione(String srcDocConvenzione) {
        this.srcDocConvenzione = srcDocConvenzione;
    }

    @Override
    public String getTematiche() {
        return tematiche;
    }

    @Override
    public void setTematiche(String tematiche) {
        this.tematiche = tematiche;
    }

    @Override
    public String getCorsoStudio() {
        return corsoStudio;
    }

    @Override
    public void setCorsoStudio(String corsoStudio) {
        this.corsoStudio = corsoStudio;
    }

    @Override
    public int getStatoConvenzione() {
        return statoConvenzione;
    }

    @Override
    public void setStatoConvenzione(int statoConvenzione) {
        this.statoConvenzione = statoConvenzione;
    }

    @Override
    public Date getInizioConvenzione() {
        return inizioConvenzione;
    }

    @Override
    public void setInizioConvenzione(Date inizioConvenzione) {
        this.inizioConvenzione = inizioConvenzione;
    }

    @Override
    public int getDurataConvenzione() {
        return durataConvenzione;
    }

    @Override
    public void setDurataConvenzione(int durataConvenzione) {
        this.durataConvenzione = durataConvenzione;
    }

    @Override
    public List<OffertaTirocinio> getOfferteTirocinio() {
        return offerteTirocinio;
    }

    @Override
    public void setOfferteTirocinio(List<OffertaTirocinio> offerteTirocinio) {
        this.offerteTirocinio = offerteTirocinio;
    }

    @Override
    public List<Valutazione> getValutazioni() {
        return valutazioni;
    }

    @Override
    public void setValutazioni(List<Valutazione> valutazioni) {
        this.valutazioni = valutazioni;
    }
    
    @Override
    public void addValutazione(Valutazione val) {
        this.valutazioni.add(val);
    }


    @Override
    public RespTirocini getRespTirocini() {
        return respTirocini;
    }

    @Override
    public void setRespTirocini(RespTirocini respTirocini) {
        this.respTirocini = respTirocini;
    }
    
}