/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.proxy;

import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.framework.data.DataLayer;
import it.univaq.web_engineering.internship_tutor.data.dao.OffertaTirocinioDAO;
import it.univaq.web_engineering.internship_tutor.data.dao.RespTirociniDAO;
import it.univaq.web_engineering.internship_tutor.data.dao.UtenteDAO;
import it.univaq.web_engineering.internship_tutor.data.dao.ValutazioneDAO;
import it.univaq.web_engineering.internship_tutor.data.impl.AziendaImpl;
import it.univaq.web_engineering.internship_tutor.data.model.OffertaTirocinio;
import it.univaq.web_engineering.internship_tutor.data.model.RespTirocini;
import it.univaq.web_engineering.internship_tutor.data.model.Utente;
import it.univaq.web_engineering.internship_tutor.data.model.Valutazione;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author steph
 */
public class AziendaProxy extends AziendaImpl {
    
    protected boolean dirty;
    protected int id_utente = 0;
    protected int id_resp_tirocini = 0;
   
    protected DataLayer dataLayer;

    public AziendaProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.dirty = false;
        this.id_utente = 0;
        this.id_resp_tirocini = 0;
    }

    @Override
    public Utente getUtente() {
        //notare come l'autore in relazione venga caricato solo su richiesta
        //note how the related author is loaded only after it is requested
        if (super.getUtente() == null && id_utente > 0) {
            try {
                super.setUtente(((UtenteDAO) dataLayer.getDAO(Utente.class)).getUtente(id_utente));
            } catch (DataException ex) {
                Logger.getLogger(UtenteProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //attenzione: l'utente caricato viene legato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se l'utente viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione
        return super.getUtente();
    }

    @Override
    public void setUtente(Utente utente) {
        super.setUtente(utente);
        this.id_utente = utente.getId();
        this.dirty = true;
    }
    
    @Override
    public RespTirocini getRespTirocini() {
        //notare come l'oggetto in relazione venga caricato solo su richiesta
        if (super.getRespTirocini() == null && id_resp_tirocini > 0) {
            try {
                super.setRespTirocini(((RespTirociniDAO) dataLayer.getDAO(RespTirocini.class)).getRespTirocini(id_resp_tirocini));
            } catch (DataException ex) {
                Logger.getLogger(UtenteProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //attenzione: l'oggetto caricato viene legato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se l'utente viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione
        return super.getRespTirocini();
    }

    @Override
    public void setRespTirocini(RespTirocini respTirocini) {
        super.setRespTirocini(respTirocini);
        this.id_resp_tirocini = respTirocini.getId();
        this.dirty = true;
    }

   @Override
    public void setRagioneSociale(String ragioneSociale) {
        super.setRagioneSociale(ragioneSociale);
        this.dirty = true;
    }

    @Override
    public void setIndirizzo(String indirizzo) {
        super.setIndirizzo(indirizzo);
    }

    @Override
    public void setCitta(String citta) {
        super.setCitta(citta);
    }

    @Override
    public void setCap(String cap) {
        super.setCap(cap);
    }

    @Override
    public void setProvincia(String provincia) {
        super.setProvincia(provincia);
    }

    @Override
    public void setRappresentanteLegale(String rappresentanteLegale) {
        super.setRappresentanteLegale(rappresentanteLegale);
    }


    @Override
    public void setPiva(String piva) {
        super.setPiva(piva);
    }

    @Override
    public void setForoCompetente(String foroCompetente) {
        super.setForoCompetente(foroCompetente);
    }

    @Override
    public void setSrcDocConvenzionamento(String srcDocConvenzionamento) {
        super.setSrcDocConvenzionamento(srcDocConvenzionamento);
    }

    @Override
    public void setTematiche(String tematiche) {
        super.setTematiche(tematiche);
    }

    @Override
    public void setCorsoStudio(String corsoStudio) {
        super.setCorsoStudio(corsoStudio);
    }

    @Override
    public void setStatoConvenzione(int statoConvenzione) {
        super.setStatoConvenzione(statoConvenzione);
    }

    @Override
    public void setInizioConvenzione(Date inizioConvenzione) {
        super.setInizioConvenzione(inizioConvenzione);
    }

    @Override
    public void setDurataConvenzione(int durataConvenzione) {
        super.setDurataConvenzione(durataConvenzione);
    }
    
    @Override
    public List<OffertaTirocinio> getOfferteTirocinio() {
        if (super.getOfferteTirocinio() == null) {
            try {
                super.setOfferteTirocinio(((OffertaTirocinioDAO) dataLayer.getDAO(OffertaTirocinio.class)).getOfferteTirocinio(this));
            } catch (DataException ex) {
                Logger.getLogger(AziendaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getOfferteTirocinio();
    }

    @Override
    public void setOfferteTirocinio(List<OffertaTirocinio> offerteTirocinio) {
        super.setOfferteTirocinio(offerteTirocinio);
        this.dirty = true;
    }
    
    @Override
    public List<Valutazione> getValutazioni() {
        if (super.getValutazioni() == null) {
            try {
                super.setValutazioni(((ValutazioneDAO) dataLayer.getDAO(Valutazione.class)).getValutazioni(this));
            } catch (DataException ex) {
                Logger.getLogger(AziendaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getValutazioni();
    }

    @Override
    public void setValutazioni(List<Valutazione> valutazioni) {
        super.setValutazioni(valutazioni);
        this.dirty = true;
    }

    //METODI DEL PROXY
    //PROXY-ONLY METHODS
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setIdUtente(int id) {
        this.id_utente = id;
        //resettiamo la cache dell'autore
        //reset author cache
        super.setUtente(null);
    }
    
}
