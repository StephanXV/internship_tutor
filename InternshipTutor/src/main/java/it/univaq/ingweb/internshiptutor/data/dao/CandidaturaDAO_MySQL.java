/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DAO;
import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.proxy.CandidaturaProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public class CandidaturaDAO_MySQL extends DAO implements CandidaturaDAO {
    
    private PreparedStatement sCandidatura;
    private PreparedStatement sCandidatureByStudente, sCandidatureByTirocinio;
    private PreparedStatement iCandidatura;

    public CandidaturaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            sCandidatura = connection.prepareStatement("SELECT * FROM candidatura WHERE id_studente=? AND id_offerta_tirocinio=?");
            sCandidatureByStudente = connection.prepareStatement("SELECT * FROM candidatura WHERE id_studente=?");
            sCandidatureByTirocinio = connection.prepareStatement("SELECT * FROM candidatura WHERE id_offerta_tirocinio=?");
            iCandidatura = connection.prepareStatement("INSERT INTO candidatura (id_studente, id_offerta_tirocinio, id_tutore_uni, "
                    + "cfu, ore_tirocinio) VALUES (?,?,?,?,?)");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor datalayer", ex);
        }
        super.init();
    }

    @Override
    public void destroy() throws DataException {
         try {
            sCandidatura.close();
            sCandidatureByStudente.close();
            sCandidatureByTirocinio.close();
            iCandidatura.close();
        } catch (SQLException ex) {
            throw new DataException("Error closing statements", ex);
        }
        super.destroy();
    }
    
    @Override
    public CandidaturaProxy createCandidatura() {
        return new CandidaturaProxy(getDataLayer());
    }

    @Override
    public CandidaturaProxy createCandidatura(ResultSet rs) throws DataException {
        CandidaturaProxy c = createCandidatura();
        try {
            c.setId_studente(rs.getInt("id_studente"));
            c.setId_offerta_tirocinio(rs.getInt("id_offerta_tirocinio"));
            c.setId_tutore_uni(rs.getInt("id_tutore_uni"));
            c.setCfu(rs.getInt("cfu"));
            c.setOreTirocinio(rs.getInt("ore_tirocinio"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create candidatura from resulset", ex);
        }
        return c;
    }
    
    @Override
    public Candidatura getCandidatura(int id_studente, int id_offerta_tirocinio) throws DataException {
        try {
            sCandidatura.setInt(1, id_studente);
            sCandidatura.setInt(2, id_offerta_tirocinio);
            try (ResultSet rs = sCandidatura.executeQuery()) {
                if (rs.next()) {
                    return createCandidatura(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load candidatura by studente and tirocinio", ex);
        }
        return null;
    }

    @Override
    public List<Candidatura> getCandidature(Studente st) throws DataException {
        List<Candidatura> result = new ArrayList();
        try {
            sCandidatureByStudente.setInt(1, st.getUtente().getId());
            try (ResultSet rs = sCandidatureByStudente.executeQuery()) {
                while (rs.next()) {
                    result.add(getCandidatura(rs.getInt("id_studente"), rs.getInt("id_offerta_tirocinio")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load candidature by studente", ex);
        }
        return result;
    }

    @Override
    public List<Candidatura> getCandidature(OffertaTirocinio ot) throws DataException {
        List<Candidatura> result = new ArrayList();
        try {
            sCandidatureByTirocinio.setInt(1, ot.getId());
            try (ResultSet rs = sCandidatureByTirocinio.executeQuery()) {
                while (rs.next()) {
                    result.add(getCandidatura(rs.getInt("id_studente"), rs.getInt("id_offerta_tirocinio")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load candidature by tirocinio", ex);
        }
        return result;
    }

    @Override
    public void insertCandidatura(Candidatura c) throws DataException {
        try {
            if (c.getStudente() != null)
                iCandidatura.setInt(1, c.getStudente().getUtente().getId());
            else
                iCandidatura.setNull(1, java.sql.Types.INTEGER);
            if(c.getOffertaTirocinio() != null)
                iCandidatura.setInt(2, c.getOffertaTirocinio().getId());
            else
                iCandidatura.setNull(2, java.sql.Types.INTEGER);
            if(c.getTutoreUni() != null)
                iCandidatura.setInt(3, c.getTutoreUni().getId());
            else
                iCandidatura.setNull(3, java.sql.Types.INTEGER);
            iCandidatura.setInt(4, c.getCfu());
            iCandidatura.setInt(5, c.getOreTirocinio());
            iCandidatura.executeUpdate();
        } catch(SQLException ex) {
            throw new DataException("Unable to insert new candidatura", ex);
        }
    }
    
    

    
    
}
