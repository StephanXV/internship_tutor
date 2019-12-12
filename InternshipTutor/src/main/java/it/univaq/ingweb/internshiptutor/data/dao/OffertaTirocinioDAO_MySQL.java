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
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.proxy.OffertaTirocinioProxy;
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
public class OffertaTirocinioDAO_MySQL extends DAO implements OffertaTirocinioDAO {
    
    private PreparedStatement sOffertaTirocinio, sAllOfferteTirocinio;
    private PreparedStatement sOfferteTirocinioByAzienda, sOfferteTirocinioByAttiva, sBestFive;
    private PreparedStatement iOffertaTirocinio, uOffertaTirocinioAttiva;
    private PreparedStatement ricercaTirocinio;
    private PreparedStatement ricercaTirocinioFac;

    public OffertaTirocinioDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            sOffertaTirocinio = connection.prepareStatement("SELECT * FROM offerta_tirocinio WHERE id=?");
            sOfferteTirocinioByAttiva = connection.prepareStatement("SELECT * FROM offerta_tirocinio WHERE id_azienda=? AND attiva=?");
            sOfferteTirocinioByAzienda = connection.prepareStatement("SELECT * FROM offerta_tirocinio WHERE id_azienda=?");
            iOffertaTirocinio = connection.prepareStatement("INSERT INTO offerta_tirocinio (luogo, settore, orari, "
                    + "durata, titolo, obiettivi, modalita, facilitazioni, id_azienda, id_tutore_tirocinio) values (?,?,?,?,?,?,?,?,?,?)"
                    , Statement.RETURN_GENERATED_KEYS);
            uOffertaTirocinioAttiva = connection.prepareStatement("UPDATE offerta_tirocinio SET attiva=? WHERE id=?");
            ricercaTirocinio = connection.prepareStatement(" SELECT t.*, a.* FROM offerta_tirocinio as t, azienda as a  where (t.id_azienda=a.id_utente) and (t.luogo like ? and t.settore like ? and t.titolo like ? and t.obiettivi like ? and t.durata like ? and a.corso_studio like ? and attiva=1))");
            ricercaTirocinioFac = connection.prepareStatement(" SELECT t.* FROM offerta_tirocinio as t, azienda as a  where (t.id_azienda=a.id_utente) and (t.luogo like ? and t.settore like ? and t.titolo like ? and t.obiettivi like ? and t.durata like ? and a.corso_studio like ? and t.facilitazioni is not null and attiva=1)");
            sBestFive = connection.prepareStatement("select ot.*, count(*) as richieste from offerta_tirocinio ot join candidatura c where ot.id = c.id_offerta_tirocinio group by ot.id having richieste > 0 order by richieste desc limit 5");
            sAllOfferteTirocinio = connection.prepareStatement("select * from offerta_tirocinio where attiva=1");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor datalayer", ex);
        }
        super.init();
    }
    
    @Override
    public void destroy() throws DataException {
        try {
            sOffertaTirocinio.close();
            sOfferteTirocinioByAzienda.close();
            sOfferteTirocinioByAttiva.close();
            iOffertaTirocinio.close();
            uOffertaTirocinioAttiva.close();
            sBestFive.close();
            ricercaTirocinioFac.close();
            ricercaTirocinio.close();
            sAllOfferteTirocinio.close();
        } catch (SQLException ex) {
            throw new DataException("Error closing statements", ex);
        }
        super.destroy();
    }

    @Override
    public OffertaTirocinioProxy createOffertaTirocinio() {
        return new OffertaTirocinioProxy(getDataLayer());
    }

    @Override
    public OffertaTirocinioProxy createOffertaTirocinio(ResultSet rs) throws DataException {
        OffertaTirocinioProxy ot = createOffertaTirocinio();
        try {
            ot.setId(rs.getInt("id"));
            ot.setLuogo(rs.getString("luogo"));
            ot.setSettore(rs.getString("settore"));
            ot.setOrari(rs.getString("orari"));
            ot.setDurata(rs.getInt("durata"));
            ot.setTitolo(rs.getString("titolo"));
            ot.setObiettivi(rs.getString("obiettivi"));
            ot.setModalita(rs.getString("modalita"));
            ot.setFacilitazioni(rs.getString("facilitazioni"));
            ot.setAttiva(rs.getBoolean("attiva"));
            ot.setId_azienda(rs.getInt("id_azienda"));
            ot.setId_tutore_tirocinio(rs.getInt("id_tutore_tirocinio"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create offerta tirocinio from result set", ex);
        }
        return ot;
    }
    
    @Override
    public OffertaTirocinio getOffertaTirocinio(int id) throws DataException {
        try {
            sOffertaTirocinio.setInt(1, id);
            try (ResultSet rs = sOffertaTirocinio.executeQuery()) {
                if (rs.next())
                    return createOffertaTirocinio(rs);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load offerta tirocinio by id", ex);
        }
        return null;
    }

    @Override
    public List<OffertaTirocinio> getAllOfferteTirocinio() throws DataException {
        List<OffertaTirocinio> result = new ArrayList();
        try {
            ResultSet rs = sAllOfferteTirocinio.executeQuery();
            while (rs.next()) {
                result.add(getOffertaTirocinio(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load offerte tirocinio by azienda", ex);
        }
        return result;
    }

    @Override
    public List<OffertaTirocinio> getOfferteTirocinio(Azienda az, boolean attiva) throws DataException {
        List<OffertaTirocinio> result = new ArrayList();
        try {
            sOfferteTirocinioByAttiva.setInt(1, az.getUtente().getId());
            sOfferteTirocinioByAttiva.setBoolean(2, attiva);
            try (ResultSet rs = sOfferteTirocinioByAttiva.executeQuery()) {
                while (rs.next()) {
                    result.add(getOffertaTirocinio(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load offerte tirocinio by azienda", ex);
        }
        return result;
    }
    
    @Override
    public List<OffertaTirocinio> getOfferteTirocinio(Azienda az) throws DataException {
        List<OffertaTirocinio> result = new ArrayList();
        try {
            sOfferteTirocinioByAzienda.setInt(1, az.getUtente().getId());
            try (ResultSet rs = sOfferteTirocinioByAzienda.executeQuery()) {
                while (rs.next()) {
                    result.add(getOffertaTirocinio(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load offerte tirocinio by azienda", ex);
        }
        return result;
    }

    @Override
    public int insertOffertaTirocinio(OffertaTirocinio ot) throws DataException {
        int id = 0;
        try {
            iOffertaTirocinio.setString(1, ot.getLuogo());
            iOffertaTirocinio.setString(2, ot.getSettore());
            iOffertaTirocinio.setString(3, ot.getOrari());
            iOffertaTirocinio.setInt(4, ot.getDurata());
            iOffertaTirocinio.setString(5, ot.getTitolo());
            iOffertaTirocinio.setString(6, ot.getObiettivi());
            iOffertaTirocinio.setString(7, ot.getModalita());
            iOffertaTirocinio.setString(8, ot.getFacilitazioni());
            if (ot.getAzienda() != null)
                iOffertaTirocinio.setInt(9, ot.getAzienda().getUtente().getId());
            else
                iOffertaTirocinio.setNull(9, java.sql.Types.INTEGER);
            if (ot.getTutoreTirocinio() != null)
                iOffertaTirocinio.setInt(10, ot.getTutoreTirocinio().getId());
            else
                iOffertaTirocinio.setNull(10, java.sql.Types.INTEGER);
            if (iOffertaTirocinio.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iOffertaTirocinio.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        //the returned value is a ResultSet with a distinct record for
                        //each generated key (only one in our case)
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            //the record fields are the key componenets
                            //(a single integer in our case)
                            id = keys.getInt(1);
                            //aggiornaimo la chiave in caso di inserimento
                            //after an insert, uopdate the object key
                        }
                    }
                    ot.setId(id);
                    return 1;
                }
            else { return 0; }
            
        } catch(SQLException ex) {
            throw new DataException("Unable to insert new offerta tirocinio", ex);
        }
    }

    @Override
    public int updateOffertaTirocinioAttiva(int id_ot, boolean attiva) throws DataException {
        try {
            uOffertaTirocinioAttiva.setBoolean(1, attiva);
            uOffertaTirocinioAttiva.setInt(2, id_ot);
            return uOffertaTirocinioAttiva.executeUpdate();            
        } catch (SQLException ex) {
            throw new DataException("Unable to update offerta tirocinio attiva", ex);
        }
    }

    @Override
    public List<OffertaTirocinio> searchOffertaTirocinio(String durata, String titolo, boolean facilitazioni, String luogo, String settore, String obiettivi, String corsoStudio) throws DataException {
        List<OffertaTirocinio> result = new ArrayList();

        try {
            if (!facilitazioni){
                ricercaTirocinio.setString(1, luogo);
                ricercaTirocinio.setString(2, settore);
                ricercaTirocinio.setString(3, titolo);
                ricercaTirocinio.setString(4 ,obiettivi);
                ricercaTirocinio.setString(5, durata);
                ricercaTirocinio.setString(6, corsoStudio);

                try (ResultSet rs = ricercaTirocinio.executeQuery()) {
                    while (rs.next()) {
                        result.add(createOffertaTirocinio(rs));
                    }
                    return result;
                }
            } else {
                ricercaTirocinioFac.setString(1, luogo);
                ricercaTirocinioFac.setString(2, settore);
                ricercaTirocinioFac.setString(3, titolo);
                ricercaTirocinioFac.setString(4 ,obiettivi);
                ricercaTirocinioFac.setString(5, durata);
                ricercaTirocinioFac.setString(6, corsoStudio);

                try (ResultSet rs = ricercaTirocinioFac.executeQuery()) {
                    while (rs.next()) {
                        result.add(createOffertaTirocinio(rs));
                    }
                    return result;
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to search offerte tirocinio", ex);
        }
    }

    @Override
    public List<OffertaTirocinio> getBestFiveOffertaTirocinio() throws DataException {
        List<OffertaTirocinio> result = new ArrayList();
        try {
            try (ResultSet rs = sBestFive.executeQuery()) {
                while(rs.next()) {
                    result.add(createOffertaTirocinio(rs));
                }
            }
            return result;
         } catch (SQLException ex) {
            throw new DataException("Unable to search best five offerte tirocinio", ex);
        }
    }
    
    
}
