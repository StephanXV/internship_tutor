/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DAO;
import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Resoconto;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.proxy.ResocontoProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public class ResocontoDAO_MySQL extends DAO implements ResocontoDAO {
    
    private PreparedStatement sResoconto;
    private PreparedStatement sResocontiByStudente, sResocontiByTirocinio;
    private PreparedStatement iResoconto;

    public ResocontoDAO_MySQL(DataLayer d) {
        super(d);
    } 
    
    @Override
    public void init() throws DataException {
        try {
            sResoconto = connection.prepareStatement("SELECT * FROM resoconto WHERE id_studente=? AND "
                    + "id_offerta_tirocinio=?");
            sResocontiByStudente = connection.prepareStatement("SELECT * FROM resoconto WHERE id_studente=?");
            sResocontiByTirocinio = connection.prepareStatement("SELECT * FROM resoconto WHERE id_offerta_tirocinio=?");
            iResoconto = connection.prepareStatement("INSERT INTO resoconto (id_studente, id_offerta_tirocinio, "
                    + "ore_effettive, descrizione_attivita, giudizio) "
                    + "VALUES (?,?,?,?,?)");
        } catch(SQLException ex) {
            throw new DataException("Error initializing internship tutor datalayer", ex);
        }
        super.init();
    }
   
    @Override
    public void destroy() throws DataException {
        try {
            sResoconto.close();
            sResocontiByStudente.close();
            sResocontiByTirocinio.close();
            iResoconto.close();
        } catch(SQLException ex) {
            throw new DataException("Error closing statements", ex);
        }
        super.destroy();
    }

    @Override
    public ResocontoProxy createResoconto() {
        return new ResocontoProxy(getDataLayer());
    }

    @Override
    public ResocontoProxy createResoconto(ResultSet rs) throws DataException {
        ResocontoProxy r = createResoconto();
        try {
            r.setId_studente(rs.getInt("id_studente"));
            r.setId_offerta_tirocinio(rs.getInt("id_offerta_tirocinio"));
            r.setOreEffettive(rs.getInt("ore_effettive"));
            r.setDescAttivita(rs.getString("descrizione_attivita"));
            r.setGiudizio(rs.getString("giudizio"));
            r.setSrcDocResoconto(rs.getString("src_documento_resoconto"));
        } catch(SQLException ex) {
            throw new DataException("Unable to create resoconto from result set", ex);
        }
        return r;
    }
    
    @Override
    public Resoconto getResoconto(int id_studente, int id_offerta_tirocinio) throws DataException {
        try {
            sResoconto.setInt(1, id_studente);
            sResoconto.setInt(2, id_offerta_tirocinio);
            try (ResultSet rs = sResoconto.executeQuery()) {
                if(rs.next())
                    return createResoconto(rs);
            }
        } catch(SQLException ex) {
            throw new DataException("Unable to load resoconto by studente and tirocinio", ex);
        }
        return null;
    }
    
    @Override
    public List<Resoconto> getResoconti(Studente st) throws DataException {
        List<Resoconto> result = new ArrayList();
        try {
            sResocontiByStudente.setInt(1, st.getUtente().getId());
            try (ResultSet rs =sResocontiByStudente.executeQuery()) {
                while(rs.next()) {
                    result.add(getResoconto(rs.getInt("id_studente"), rs.getInt("id_offerta_tirocinio")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load resoconti by studente", ex);
        }
        return result;  
    }

    @Override
    public List<Resoconto> getResoconti(OffertaTirocinio ot) throws DataException {
        List<Resoconto> result = new ArrayList();
        try {
            sResocontiByTirocinio.setInt(1, ot.getId());
            try (ResultSet rs =sResocontiByTirocinio.executeQuery()) {
                while(rs.next()) {
                    result.add(getResoconto(rs.getInt("id_studente"), rs.getInt("id_offerta_tirocinio")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load resoconti by tirocinio", ex);
        }
        return result;  
    }

    @Override
    public int insertResoconto(Resoconto r) throws DataException {
        try {
            if(r.getStudente() != null)
                iResoconto.setInt(1, r.getStudente().getUtente().getId());
            else
                iResoconto.setNull(1, java.sql.Types.INTEGER);
            if(r.getOffertaTirocinio() != null)
                iResoconto.setInt(2, r.getOffertaTirocinio().getId());
            else
                iResoconto.setNull(2, java.sql.Types.INTEGER);
            iResoconto.setInt(3, r.getOreEffettive());
            iResoconto.setString(4, r.getDescAttivita());
            iResoconto.setString(5, r.getGiudizio());
            return iResoconto.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to insert new resoconto", ex);
        }
    }  
    
}
