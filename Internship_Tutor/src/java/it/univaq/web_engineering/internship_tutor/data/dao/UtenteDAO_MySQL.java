/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.framework.data.DAO;
import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.framework.data.DataLayer;
import it.univaq.web_engineering.internship_tutor.data.model.Utente;
import it.univaq.web_engineering.internship_tutor.data.proxy.UtenteProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Stefano Florio
 */
public class UtenteDAO_MySQL extends DAO implements UtenteDAO {
    
    private PreparedStatement sUtenteById;
    
    public UtenteDAO_MySQL(DataLayer d) {
        super(d);
    }    
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sUtenteById = connection.prepareStatement("SELECT * FROM utente WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent è una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {
            sUtenteById.close();
          
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }
    
    @Override
    public UtenteProxy createUtente() {
        return new UtenteProxy(getDataLayer());
    }

    @Override
    public UtenteProxy createUtente(ResultSet rs) throws DataException {
        try {
            UtenteProxy ut = createUtente();
            ut.setId(rs.getInt("id"));
            ut.setEmail(rs.getString("email"));
            ut.setUsername(rs.getString("username"));
            ut.setPw(rs.getString("pw"));
            ut.setTipologia(rs.getString("email"));
            return ut;
        } catch (SQLException ex) {
            throw new DataException("Unable to create Utente object form ResultSet", ex);
        }
    }

    @Override
    public Utente getUtente(int id) throws DataException {
        try {
            sUtenteById.setInt(1, id);
            try (ResultSet rs = sUtenteById.executeQuery()) {
                if (rs.next()) {
                    //notare come utilizziamo il costrutture
                    //"helper" della classe AuthorImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    //note how we use here the helper constructor
                    //of the AuthorImpl class to quickly
                    //create an instance from the current record
                    return createUtente(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Utente by ID", ex);
        }
        return null;
    }
    
}
