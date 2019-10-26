/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.framework.data.DAO;
import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.framework.data.DataLayer;
import it.univaq.web_engineering.internship_tutor.data.model.RespTirocini;
import it.univaq.web_engineering.internship_tutor.data.proxy.RespTirociniProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Stefano Florio
 */
public class RespTirociniDAO_MySQL extends DAO implements RespTirociniDAO {

    
    private PreparedStatement sRespTirociniById;

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
    
}
