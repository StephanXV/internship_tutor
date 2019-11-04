/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DAO;
import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.Valutazione;
import it.univaq.ingweb.internshiptutor.data.proxy.ValutazioneProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public class ValutazioneDAO_MySQL extends DAO implements ValutazioneDAO {

    private PreparedStatement sValutazione;
    private PreparedStatement sValutazioniByStudente, sValutazioniByAzienda;
    private PreparedStatement iValutazione;
    
    public ValutazioneDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
         try {
            sValutazione = connection.prepareStatement("SELECT * FROM valutazione WHERE id_studente=? AND "
                    + "id_azienda=?");
            sValutazioniByStudente = connection.prepareStatement("SELECT * FROM valutazione WHERE id_studente=?");
            sValutazioniByAzienda = connection.prepareStatement("SELECT * FROM valutazione WHERE id_azienda=?");
            iValutazione = connection.prepareStatement("INSERT INTO valutazione (id_studente, id_azienda, "
                    + "stelle) VALUES (?,?,?)");
        } catch(SQLException ex) {
            throw new DataException("Error initializing internship tutor datalayer", ex);
        }
        super.init();
    }
    
    @Override
    public void destroy() throws DataException {
         try {
            sValutazione.close();
            sValutazioniByStudente.close();
            sValutazioniByAzienda.close();
            iValutazione.close();
        } catch(SQLException ex) {
            throw new DataException("Error closing statements", ex);
        }
        super.destroy();
    }

    @Override
    public ValutazioneProxy createValutazione() {
        return new ValutazioneProxy(getDataLayer());
    }

    @Override
    public ValutazioneProxy createValutazione(ResultSet rs) throws DataException {
        ValutazioneProxy v = createValutazione();
         try {
            v.setId_studente(rs.getInt("id_studente"));
            v.setId_azienda(rs.getInt("id_azienda"));
            v.setStelle(rs.getInt("stelle"));
        } catch(SQLException ex) {
            throw new DataException("Unable to create valutazione from result set", ex);
        }
        return v;
    }
    
    @Override
    public Valutazione getValutazione(int id_azienda, int id_studente) throws DataException {
        try {
            sValutazione.setInt(1, id_azienda);
            sValutazione.setInt(2, id_studente);
            try (ResultSet rs = sValutazione.executeQuery()) {
                if (rs.next())
                    return createValutazione(rs);
            }
        } catch(SQLException ex) {
            throw new DataException("Unable to load valutazione by azienda and studente", ex);
        }
        return null; 
    }
    
    @Override
    public List<Valutazione> getValutazioni(Azienda az) throws DataException {
        List<Valutazione> result = new ArrayList();
        try {
            sValutazioniByAzienda.setInt(1, az.getUtente().getId());
            try (ResultSet rs = sValutazioniByAzienda.executeQuery()) {
                while(rs.next()) {
                    result.add(getValutazione(rs.getInt("id_studente"), rs.getInt("id_azienda")));
                }
            }
        } catch(SQLException ex) {
            throw new DataException("Unable to load valutazioni by azienda", ex);
        }
        return result;
    }

    @Override
    public List<Valutazione> getValutazioni(Studente st) throws DataException {
        List<Valutazione> result = new ArrayList();
        try {
            sValutazioniByStudente.setInt(1, st.getUtente().getId());
            try (ResultSet rs = sValutazioniByStudente.executeQuery()) {
                while(rs.next()) {
                    result.add(getValutazione(rs.getInt("id_studente"), rs.getInt("id_azienda")));
                }
            }
        } catch(SQLException ex) {
            throw new DataException("Unable to load valutazioni by studente", ex);
        }
        return result;
    }

    @Override
    public void insertValutazione(Valutazione v) throws DataException {
        try {
            if(v.getStudente() != null)
                iValutazione.setInt(1, v.getStudente().getUtente().getId());
            else
                iValutazione.setNull(1, java.sql.Types.INTEGER);
            if(v.getAzienda() != null)
                iValutazione.setInt(2, v.getAzienda().getUtente().getId());
            else
                iValutazione.setNull(2, java.sql.Types.INTEGER);
            iValutazione.setInt(3, v.getStelle());
            iValutazione.executeUpdate();
        } catch(SQLException ex) {
            throw new DataException("Unable to insert new valutazione", ex);
        } 
    }

    
   
    
    
    
}
