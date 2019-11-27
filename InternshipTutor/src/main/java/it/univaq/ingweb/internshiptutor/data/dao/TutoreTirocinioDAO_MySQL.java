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
import it.univaq.ingweb.internshiptutor.data.model.TutoreTirocinio;
import it.univaq.ingweb.internshiptutor.data.proxy.TutoreTirocinioProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public class TutoreTirocinioDAO_MySQL extends DAO implements TutoreTirocinioDAO {

    
    private PreparedStatement sTutoreTirocinioById;
    private PreparedStatement sTutoriTirocinioByAzienda;
    private PreparedStatement iTutoreTirocinio;
    private PreparedStatement dTutoreTirocinio;

    public TutoreTirocinioDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sTutoreTirocinioById = connection.prepareStatement("SELECT * FROM tutore_tirocinio WHERE ID=?");
            sTutoriTirocinioByAzienda = connection.prepareStatement("SELECT * FROM tutore_tirocinio WHERE id_azienda=?");
            iTutoreTirocinio = connection.prepareStatement("INSERT INTO tutore_tirocinio (nome, cognome, email, telefono, id_azienda)"
                    + "values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            dTutoreTirocinio = connection.prepareStatement("DELETE FROM tutore_tirocinio WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent è una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {
            sTutoreTirocinioById.close();
            sTutoriTirocinioByAzienda.close();
            iTutoreTirocinio.close();
            dTutoreTirocinio.close();
          
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }
    
    @Override
    public TutoreTirocinioProxy createTutoreTirocinio() {
        return new TutoreTirocinioProxy(getDataLayer());
    }

    @Override
    public TutoreTirocinioProxy createTutoreTirocinio(ResultSet rs) throws DataException {
        try {
            TutoreTirocinioProxy tt = createTutoreTirocinio();
            tt.setId(rs.getInt("id"));
            tt.setNome(rs.getString("nome"));
            tt.setCognome(rs.getString("cognome"));
            tt.setEmail(rs.getString("email"));
            tt.setTelefono(rs.getString("telefono"));
            tt.setId_azienda(rs.getInt("id_azienda"));
            return tt;
        } catch (SQLException ex) {
            throw new DataException("Unable to create TutoreTirocinio object form ResultSet", ex);
        }
    }

    @Override
    public TutoreTirocinio getTutoreTirocinio(int id) throws DataException {
        try {
            sTutoreTirocinioById.setInt(1, id);
            try (ResultSet rs = sTutoreTirocinioById.executeQuery()) {
                if (rs.next()) {
                    //notare come utilizziamo il costrutture
                    //"helper" della classe AuthorImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    //note how we use here the helper constructor
                    //of the AuthorImpl class to quickly
                    //create an instance from the current record
                    return createTutoreTirocinio(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load TutoreTirocinio by ID", ex);
        }
        return null;
    }
    
    @Override
    public int insertTutoreTirocinio(TutoreTirocinio tt) throws DataException {
        try {
            iTutoreTirocinio.setString(1, tt.getNome());
            iTutoreTirocinio.setString(2, tt.getCognome());
            iTutoreTirocinio.setString(3, tt.getEmail());
            iTutoreTirocinio.setString(4, tt.getTelefono());
            if (tt.getAzienda() != null)
                iTutoreTirocinio.setInt(5, tt.getAzienda().getUtente().getId());
            else
                iTutoreTirocinio.setNull(5, java.sql.Types.INTEGER);
            if (iTutoreTirocinio.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iTutoreTirocinio.getGeneratedKeys()) {
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
            }
            else { return 0; }
        } catch (SQLException ex) {
            throw new DataException("Unable to insert new tutore tirocinio", ex);
        }
    }

    @Override
    public int deleteTutoreTirocinio(int id) throws DataException {
       try {
           dTutoreTirocinio.setInt(1, id);
           return dTutoreTirocinio.executeUpdate();
       } catch (SQLException ex) {
            throw new DataException("Unable to delete tutore tirocinio", ex);
        } 
    }

    @Override
    public List<TutoreTirocinio> getTutoriTirocinio(Azienda az) throws DataException {
        List<TutoreTirocinio> result = new ArrayList();
        try {
            sTutoriTirocinioByAzienda.setInt(1, az.getUtente().getId());
            try (ResultSet rs = sTutoriTirocinioByAzienda.executeQuery()) {
                while (rs.next()) {
                    result.add((TutoreTirocinio) getTutoreTirocinio(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load tutori tirocinio of the azienda " + az.getRagioneSociale(), ex);
        }
        return result;
    }
}

