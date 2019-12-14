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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public class AziendaDAO_MySQL extends DAO implements AziendaDAO {
    
    private PreparedStatement sAziendaById, sAziendaByUtenteUsername;
    private PreparedStatement sAziendeByStato, sTirocinantiAttivi, sBestFive;
    private PreparedStatement iAzienda, dAzienda, uAzienda;

    public AziendaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sAziendaById = connection.prepareStatement("SELECT * FROM azienda WHERE id_utente=?");
            sAziendaByUtenteUsername = connection.prepareStatement("SELECT az.* FROM azienda as az JOIN utente as ut WHERE az.id_utente = ut.id AND ut.username=?");
            sAziendeByStato = connection.prepareStatement("SELECT id_utente FROM azienda WHERE stato_convenzione=?");
            sTirocinantiAttivi = connection.prepareStatement("SELECT count(*) as tirocinanti_attivi FROM azienda as a JOIN offerta_tirocinio as ot JOIN candidatura as c\n" +
"	WHERE a.id_utente=? AND a.id_utente = ot.id_azienda AND c.id_offerta_tirocinio = ot.id AND c.stato_candidatura = 1");
            iAzienda = connection.prepareStatement ("INSERT INTO azienda (id_utente, ragione_sociale, indirizzo, citta, cap,"
                    + " provincia, rappresentante_legale, piva, foro_competente, tematiche, corso_studio, durata_convenzione,"
                    + " id_responsabile) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            dAzienda = connection.prepareStatement("DELETE FROM azienda WHERE id_utente=?");
            sBestFive = connection.prepareStatement("select a.*,avg(v.stelle) as media from azienda a join valutazione v where a.id_utente = v.id_azienda group by a.id_utente having media > 0 order by media");
            uAzienda = connection.prepareStatement("UPDATE azienda SET ragione_sociale=?, indirizzo=?, citta=?, cap=?,"
                    + " provincia=?, rappresentante_legale=?, piva=?, foro_competente=?, tematiche=?, corso_studio=?, durata_convenzione=?, stato_convenzione=?, inizio_convenzione=?, src_documento_convenzione=? WHERE id_utente=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor datalayer", ex);
        }
    }
        
    @Override
    public void destroy() throws DataException {
        try {
            sAziendaById.close();
            sAziendaByUtenteUsername.close();
            sAziendeByStato.close();
            sTirocinantiAttivi.close();
            iAzienda.close();
            dAzienda.close();
            sBestFive.close();
            uAzienda.close();
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
            if (rs.getDate("inizio_convenzione") != null) 
                a.setInizioConvenzione(rs.getDate("inizio_convenzione").toLocalDate());
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
    public Azienda getAzienda(String ut_username) throws DataException {
        try {
            sAziendaByUtenteUsername.setString(1, ut_username);
            try (ResultSet rs = sAziendaByUtenteUsername.executeQuery()) {
                if (rs.next()) {
                    return createAzienda(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load azienda by utente username", ex);
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
    public int insertAzienda(Azienda az) throws DataException {
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
            return iAzienda.executeUpdate();
            
        } catch (SQLException ex) {
            throw new DataException("Unable to insert new azienda", ex);
        }
    }

    @Override
    public int deleteAzienda(int id_azienda) throws DataException {
        try {
            dAzienda.setInt(1, id_azienda);
            return dAzienda.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete azienda", ex);
        }
    }

    @Override
    public int getTirocinantiAttivi(Azienda az) throws DataException {
        try {
            sTirocinantiAttivi.setInt(1, az.getUtente().getId());
             try (ResultSet rs = sTirocinantiAttivi.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tirocinanti_attivi");
                }
            }
            
        } catch (SQLException ex) {
            throw new DataException("Unable to get tirocinanti attivi by azienda", ex);
        }
        return 0;
    } 

    @Override
    public List<Azienda> getBestFiveAziende() throws DataException {
        List<Azienda> result = new ArrayList();
        try {
            try (ResultSet rs = sBestFive.executeQuery()) {
                while (rs.next()) {
                    AziendaProxy a = createAzienda(rs);
                    a.setMediaValutazioni(rs.getDouble("media")/2);
                    result.add(a);
                }
            }
            return result;
         } catch (SQLException ex) {
            throw new DataException("Unable to get best five aziende", ex);
        }
    }
    
    
    @Override
    public int updateAzienda(Azienda az) throws DataException {
        try {
            uAzienda.setString(1, az.getRagioneSociale());
            uAzienda.setString(2, az.getIndirizzo());
            uAzienda.setString(3, az.getCitta());
            uAzienda.setString(4, az.getCap());
            uAzienda.setString(5, az.getProvincia());
            uAzienda.setString(6, az.getRappresentanteLegale());
            uAzienda.setString(7, az.getPiva());
            uAzienda.setString(8, az.getForoCompetente());
            uAzienda.setString(9, az.getTematiche());
            uAzienda.setString(10, az.getCorsoStudio());
            uAzienda.setInt(11, az.getDurataConvenzione());
            uAzienda.setInt(12, az.getStatoConvenzione());
            uAzienda.setDate(13, java.sql.Date.valueOf(az.getInizioConvenzione().plusDays(1)));
            uAzienda.setString(14, az.getSrcDocConvenzione());
            uAzienda.setInt(15, az.getUtente().getId());
            
            return uAzienda.executeUpdate();
            
        } catch (SQLException ex) {
            throw new DataException("Unable to update azienda", ex);
        }
    }
    
    
}
