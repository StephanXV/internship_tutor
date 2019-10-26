/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.model.Azienda;
import it.univaq.web_engineering.internship_tutor.data.model.Studente;
import it.univaq.web_engineering.internship_tutor.data.model.Valutazione;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public interface ValutazioneDAO {
    
    Valutazione createValutazione();
    
    Valutazione createValutazione(ResultSet rs) throws DataException;
    
    List<Valutazione> getValutazioni(Azienda az) throws DataException;
    
    List<Valutazione> getValutazioni(Studente st) throws DataException;
    
    Valutazione getValutazione(Azienda az, Studente st) throws DataException;
}
