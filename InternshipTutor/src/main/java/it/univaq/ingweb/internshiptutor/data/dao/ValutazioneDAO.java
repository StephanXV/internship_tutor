/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.Valutazione;
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
    
    Valutazione getValutazione(int id_az, int id_st) throws DataException;
    
    void insertValutazione(Valutazione v) throws DataException;
}
