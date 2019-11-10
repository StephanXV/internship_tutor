/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DAO;
import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;
import it.univaq.ingweb.internshiptutor.data.proxy.TutoreUniProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Stefano Florio
 */
public class TutoreUniDAO_MySQL extends DAO implements TutoreUniDAO {

    
    private PreparedStatement sTutoreUniById;
    private PreparedStatement iTutoreUni, dTutoreUni;

    public TutoreUniDAO_MySQL(DataLayer d) 
    {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sTutoreUniById = connection.prepareStatement("SELECT * FROM tutore_uni WHERE id=?");
            iTutoreUni = connection.prepareStatement("INSERT INTO tutore_uni (nome, cognome, email, telefono)"
                    + "values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            dTutoreUni = connection.prepareStatement("DELETE FROM tutore_uni WHERE id=1");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent è una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {
            sTutoreUniById.close();
            iTutoreUni.close();
            dTutoreUni.close();
          
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }
    
    @Override
    public TutoreUniProxy createTutoreUni() {
        return new TutoreUniProxy(getDataLayer());
    }

    @Override
    public TutoreUniProxy createTutoreUni(ResultSet rs) throws DataException {
        try {
            TutoreUniProxy tu = createTutoreUni();
            tu.setId(rs.getInt("id"));
            tu.setNome(rs.getString("nome"));
            tu.setCognome(rs.getString("cognome"));
            tu.setEmail(rs.getString("email"));
            tu.setTelefono(rs.getString("telefono"));
            return tu;
        } catch (SQLException ex) {
            throw new DataException("Unable to create TutoreUni object form ResultSet", ex);
        }
    }

    @Override
    public TutoreUni getTutoreUni(int id) throws DataException {
        try {
            sTutoreUniById.setInt(1, id);
            try (ResultSet rs = sTutoreUniById.executeQuery()) {
                if (rs.next()) {
                    //notare come utilizziamo il costrutture
                    //"helper" della classe AuthorImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    //note how we use here the helper constructor
                    //of the AuthorImpl class to quickly
                    //create an instance from the current record
                    return createTutoreUni(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load TutoreUni by ID", ex);
        }
        return null;
    }
    
    @Override
    public int insertTutoreUni(TutoreUni tt) throws DataException {
        try {
            iTutoreUni.setString(1, tt.getNome());
            iTutoreUni.setString(2, tt.getCognome());
            iTutoreUni.setString(3, tt.getEmail());
            iTutoreUni.setString(4, tt.getTelefono());
            if (iTutoreUni.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iTutoreUni.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        //the returned value is a ResultSet with a distinct record for
                        //each generated key (only one in our case)
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            //the record fields are the key componenets
                            //(a single integer in our case)
                            tt.setId(keys.getInt(1));
                            //aggiornaimo la chiave in caso di inserimento
                            //after an insert, uopdate the object key
                        }
                    }
                return 1;
            } else { return 0; }
        } catch (SQLException ex) {
            throw new DataException("Unable to insert new tutore università", ex);
        }
    }

    @Override
    public int deleteTutoreUni(int id) throws DataException {
        try {
            dTutoreUni.setInt(1, id);
            return dTutoreUni.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete tutore università", ex);
        }
    }
      
}
