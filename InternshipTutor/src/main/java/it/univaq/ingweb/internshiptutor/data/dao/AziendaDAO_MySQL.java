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
import it.univaq.ingweb.internshiptutor.data.proxy.AziendaProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public class AziendaDAO_MySQL extends DAO implements AziendaDAO {
    
    private PreparedStatement sAziendaById;
    private PreparedStatement sAziendeByStato;
    private PreparedStatement iAzienda, uAziendaStato;

    public AziendaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sAziendaById = connection.prepareStatement("SELECT * FROM azienda WHERE id_utente=?");
            sAziendeByStato = connection.prepareStatement("SELECT id_utente FROM azienda WHERE stato_convenzione=?");
            iAzienda = connection.prepareStatement ("INSERT INTO azienda (id_utente, ragione_sociale, indirizzo, citta, cap,"
                    + " provincia, rappresentante_legale, piva, foro_competente, tematiche, corso_studio, durata_convenzione,"
                    + " id_responsabile) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            uAziendaStato = connection.prepareStatement("UPDATE azienda SET stato_convenzione=? WHERE id_utente=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor datalayer", ex);
        }
    }
        
    @Override
    public void destroy() throws DataException {
        try {
            sAziendaById.close();
            sAziendeByStato.close();
            iAzienda.close();
            uAziendaStato.close();
        } catch(SQLException ex) {
            throw new DataException("Error closing statements", ex);
        }
        super.destroy();
    }

    @Override
    public AziendaProxy createAzienda() {
        return new AziendaProxy(getDataLayer());
    }

    @Override
    public AziendaProxy createAzienda(ResultSet rs) throws DataException {
        AziendaProxy a = createAzienda();
        try {
            a.setId_utente(rs.getInt("id_utente"));
            a.setRagioneSociale(rs.getString("ragione_sociale"));
            a.setIndirizzo(rs.getString("indirizzo"));
            a.setCitta(rs.getString("citta"));
            a.setCap(rs.getString("cap"));
            a.setProvincia(rs.getString("provincia"));
            a.setRappresentanteLegale(rs.getString("rappresentante_legale"));
            a.setPiva(rs.getString("piva"));
            a.setForoCompetente(rs.getString("foro_competente"));
            a.setSrcDocConvenzione(rs.getString("src_documento_convenzione"));
            a.setTematiche(rs.getString("tematiche"));
            a.setStatoConvenzione(rs.getInt("stato_convenzione"));
            a.setCorsoStudio(rs.getString("corso_studio"));
            a.setInizioConvenzione(rs.getDate("inizio_convenzione"));
            a.setDurataConvenzione(rs.getInt("durata_convenzione"));
            a.setId_respTirocini(rs.getInt("id_responsabile"));
            
        } catch(SQLException ex) {
            throw new DataException("Unable to create Azienda object from resultset", ex);
        }
        return a;
    }

    @Override
    public Azienda getAzienda(int id) throws DataException {
        try {
            sAziendaById.setInt(1, id);
            try (ResultSet rs = sAziendaById.executeQuery()) {
                if (rs.next()) {
                    return createAzienda(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load azienda by Id", ex);
        }
        return null;
    }

    @Override
    public List<Azienda> getAziendeByStato(int stato) throws DataException {
        List<Azienda> result = new ArrayList();
        try {
            sAziendeByStato.setInt(1, stato);
            try (ResultSet rs = sAziendeByStato.executeQuery()) {
                while (rs.next()) {
                    result.add((Azienda) getAzienda(rs.getInt("id_utente")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load aziende with stato " + stato, ex);
        }
        return result;
    }

    @Override
    public Azienda updateAziendaStato(Azienda az, int stato) throws DataException {
        int id_utente = az.getUtente().getId();
        try {
            uAziendaStato.setInt(1, stato);
            uAziendaStato.setInt(2, id_utente);
            if (uAziendaStato.executeUpdate() == 1) {
                az.setStatoConvenzione(stato);
                return az;
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to update azienda stato", ex);
        }
        return null;
    }

    @Override
    public void insertAzienda(Azienda az) throws DataException {
        try {
            if (az.getUtente() != null)
                iAzienda.setInt(1, az.getUtente().getId());
            else 
                iAzienda.setNull(1, java.sql.Types.INTEGER);
            iAzienda.setString(2, az.getRagioneSociale());
            iAzienda.setString(3, az.getIndirizzo());
            iAzienda.setString(4, az.getCitta());
            iAzienda.setString(5, az.getCap());
            iAzienda.setString(6, az.getProvincia());
            iAzienda.setString(7, az.getRappresentanteLegale());
            iAzienda.setString(8, az.getPiva());
            iAzienda.setString(9, az.getForoCompetente());
            iAzienda.setString(10, az.getTematiche());
            iAzienda.setString(11, az.getCorsoStudio());
            iAzienda.setInt(12, az.getDurataConvenzione());
            if (az.getRespTirocini() != null)
                iAzienda.setInt(13, az.getRespTirocini().getId());
            else 
                iAzienda.setNull(13, java.sql.Types.INTEGER);
            iAzienda.executeUpdate();
            
        } catch (SQLException ex) {
            throw new DataException("Unable to insert new azienda", ex);
        }
    }
    
}
