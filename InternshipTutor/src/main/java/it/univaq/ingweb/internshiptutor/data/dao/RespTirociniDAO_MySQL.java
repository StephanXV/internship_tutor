/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DAO;
import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.model.RespTirocini;
import it.univaq.ingweb.internshiptutor.data.proxy.RespTirociniProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Stefano Florio
 */
public class RespTirociniDAO_MySQL extends DAO implements RespTirociniDAO {

    
    private PreparedStatement sRespTirociniById;
    private PreparedStatement iRespTirocini, dRespTirocini;

    public RespTirociniDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sRespTirociniById = connection.prepareStatement("SELECT * FROM responsabile_tirocini WHERE ID=?");
            iRespTirocini = connection.prepareStatement("INSERT INTO responsabile_tirocini (nome, cognome, email, telefono)"
                    + "values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            dRespTirocini = connection.prepareStatement("DELETE FROM responsabile_tirocini WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent è una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {
            sRespTirociniById.close();
            iRespTirocini.close();
          
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }
    
    @Override
    public RespTirociniProxy createRespTirocini() {
        return new RespTirociniProxy(getDataLayer());
    }

    @Override
    public RespTirociniProxy createRespTirocini(ResultSet rs) throws DataException {
        try {
            RespTirociniProxy rt = createRespTirocini();
            rt.setId(rs.getInt("id"));
            rt.setNome(rs.getString("nome"));
            rt.setCognome(rs.getString("cognome"));
            rt.setEmail(rs.getString("email"));
            rt.setTelefono(rs.getString("telefono"));
            return rt;
        } catch (SQLException ex) {
            throw new DataException("Unable to create RespTirocini object form ResultSet", ex);
        }
    }

    @Override
    public RespTirocini getRespTirocini(int id) throws DataException {
        try {
            sRespTirociniById.setInt(1, id);
            try (ResultSet rs = sRespTirociniById.executeQuery()) {
                if (rs.next()) {
                    //notare come utilizziamo il costrutture
                    //"helper" della classe AuthorImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    //note how we use here the helper constructor
                    //of the AuthorImpl class to quickly
                    //create an instance from the current record
                    return createRespTirocini(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load RespTirocini by ID", ex);
        }
        return null;
    }
    
    @Override
    public int insertRespTirocini(RespTirocini rt) throws DataException {
        try {
            iRespTirocini.setString(1, rt.getNome());
            iRespTirocini.setString(2, rt.getCognome());
            iRespTirocini.setString(3, rt.getEmail());
            iRespTirocini.setString(4, rt.getTelefono());
            if (iRespTirocini.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iRespTirocini.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        //the returned value is a ResultSet with a distinct record for
                        //each generated key (only one in our case)
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            //the record fields are the key componenets
                            //(a single integer in our case)
                            rt.setId(keys.getInt(1));
                            //aggiornaimo la chiave in caso di inserimento
                            //after an insert, uopdate the object key   
                        }
                    }
                return 1;
            } else { return 0; }
        } catch (SQLException ex) {
            throw new DataException("Unable to insert new responsabile tirocini", ex);
        }
    }
    
    @Override
    public int deleteRespTirocini(int id) throws DataException {
        try {
            dRespTirocini.setInt(1, id);
            return dRespTirocini.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete responsabile tirocini", ex);
        }
    }
    
}
