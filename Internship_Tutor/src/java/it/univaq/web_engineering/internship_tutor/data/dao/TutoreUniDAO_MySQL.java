/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;


import it.univaq.web_engineering.framework.data.DAO;
import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.framework.data.DataLayer;
import it.univaq.web_engineering.internship_tutor.data.model.TutoreUni;
import it.univaq.web_engineering.internship_tutor.data.proxy.TutoreUniProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Stefano Florio
 */
public class TutoreUniDAO_MySQL extends DAO implements TutoreUniDAO {

    
    private PreparedStatement sTutoreUniById;

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
    
}
