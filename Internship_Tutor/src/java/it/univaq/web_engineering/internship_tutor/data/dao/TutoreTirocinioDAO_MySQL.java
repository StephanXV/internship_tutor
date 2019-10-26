/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;


import it.univaq.web_engineering.framework.data.DAO;
import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.framework.data.DataLayer;
import it.univaq.web_engineering.internship_tutor.data.model.TutoreTirocinio;
import it.univaq.web_engineering.internship_tutor.data.proxy.TutoreTirocinioProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Stefano Florio
 */
public class TutoreTirocinioDAO_MySQL extends DAO implements TutoreTirocinioDAO {

    
    private PreparedStatement sTutoreTirocinioById;

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
    
}

