/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DAO;
import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import it.univaq.ingweb.internshiptutor.data.proxy.UtenteProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Stefano Florio
 */
public class UtenteDAO_MySQL extends DAO implements UtenteDAO {
    
    private PreparedStatement sUtenteById;
    private PreparedStatement iUtente;
    
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
            iUtente = connection.prepareStatement("INSERT INTO utente (email, username, pw, tipologia)"
                    + "values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
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
            iUtente.close();
          
        } catch (SQLException ex) {
            throw new DataException("Error closing statements", ex);
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
                    //"helper" della classe UtenteImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    return createUtente(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Utente by ID", ex);
        }
        return null;
    }

    @Override
    public void insertUtente(Utente ut) throws DataException {
        int id = 0;
        try {
            iUtente.setString(1, ut.getEmail());
            iUtente.setString(2, ut.getUsername());
            iUtente.setString(3, ut.getPw());
            iUtente.setString(4, ut.getTipologia());
            if (iUtente.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iUtente.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        //the returned value is a ResultSet with a distinct record for
                        //each generated key (only one in our case)
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            //the record fields are the key componenets
                            //(a single integer in our case)
                            id = keys.getInt(1);
                            //aggiornaimo la chiave in caso di inserimento
                            //after an insert, uopdate the object key
                        }
                    }
                    ut.setId(id);
                }
        } catch (SQLException ex) {
            throw new DataException("Unable to insert new utente", ex);
        }
    }
    
    
    
}
