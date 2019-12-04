/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;


import it.univaq.ingweb.framework.data.DAO;
import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.proxy.StudenteProxy;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author Stefano Florio
 */
public class StudenteDAO_MySQL extends DAO implements StudenteDAO {
    
    private PreparedStatement sStudenteById;
    private PreparedStatement iStudente, uStudente;

    public StudenteDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sStudenteById = connection.prepareStatement("SELECT * FROM studente WHERE id_utente=?");
            iStudente = connection.prepareStatement("INSERT INTO studente (id_utente, nome, cognome, "
                    + "codice_fiscale, data_nascita, citta_nascita, provincia_nascita, citta_residenza, "
                    + "provincia_residenza, cap_residenza, telefono, corso_laurea, handicap) values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            uStudente = connection.prepareStatement("UPDATE studente SET nome=?, cognome=?, "
                    + "codice_fiscale=?, data_nascita=?, citta_nascita=?, provincia_nascita=?, citta_residenza=?, "
                    + "provincia_residenza=?, cap_residenza=?, telefono=?, corso_laurea=?, handicap=? WHERE id_utente=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor datalayer", ex);
        }
        
    }

    @Override
    public void destroy() throws DataException {
        try {
            sStudenteById.close();
            iStudente.close();
            uStudente.close();
         } catch(SQLException ex) {
            throw new DataException("Error closing statements", ex);
        }
        super.destroy();
    }

    @Override
    public StudenteProxy createStudente() {
        return new StudenteProxy(getDataLayer());
    }

    @Override
    public StudenteProxy createStudente(ResultSet rs) throws DataException {
        StudenteProxy s = createStudente();
        try {
            s.setId_utente(rs.getInt("id_utente"));
            s.setNome(rs.getString("nome"));
            s.setCognome(rs.getString("cognome"));
            s.setCF(rs.getString("codice_fiscale"));
            s.setDataNascita(rs.getDate("data_nascita").toLocalDate());
            s.setCittaNascita(rs.getString("citta_nascita"));
            s.setProvinciaNascita(rs.getString("provincia_nascita"));
            s.setCittaResidenza(rs.getString("citta_residenza"));
            s.setProvinciaResidenza(rs.getString("provincia_residenza"));
            s.setCapResidenza(rs.getString("cap_residenza"));
            s.setTelefono(rs.getString("telefono"));
            s.setCorsoLaurea(rs.getString("corso_laurea"));
            s.setHandicap(rs.getBoolean("handicap"));
        } catch(SQLException ex) {
            throw new DataException("Unable to create studente object from resultset", ex);
        }
        return s;
    }

    @Override
    public Studente getStudente(int id) throws DataException {
        try {
            sStudenteById.setInt(1, id);
            try (ResultSet rs = sStudenteById.executeQuery()) {
                if (rs.next()) {
                    return createStudente(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load studente by Id", ex);
        }
        return null;
    }

    @Override
    public int insertStudente(Studente st) throws DataException {
        try {
            if (st.getUtente() != null)
                iStudente.setInt(1, st.getUtente().getId());
            else 
                iStudente.setNull(1, java.sql.Types.INTEGER);
            iStudente.setString(2, st.getNome());
            iStudente.setString(3, st.getCognome());
            iStudente.setString(4, st.getCF());
            iStudente.setDate(5, java.sql.Date.valueOf(st.getDataNascita()));
            iStudente.setString(6, st.getCittaNascita());
            iStudente.setString(7, st.getProvinciaNascita());
            iStudente.setString(8, st.getCittaResidenza());
            iStudente.setString(9, st.getProvinciaResidenza());
            iStudente.setString(10, st.getCapResidenza());
            iStudente.setString(11, st.getTelefono());
            iStudente.setString(12, st.getCorsoLaurea());
            iStudente.setBoolean(13, st.isHandicap());
            return iStudente.executeUpdate();
            
        } catch (SQLException ex) {
            throw new DataException("Unable to insert new studente", ex);
        }
    }
    
     @Override
    public int updateStudente(Studente st) throws DataException {
        try {
            uStudente.setString(1, st.getNome());
            uStudente.setString(2, st.getCognome());
            uStudente.setString(3, st.getCF());
            uStudente.setDate(4, java.sql.Date.valueOf(st.getDataNascita()));
            uStudente.setString(5, st.getCittaNascita());
            uStudente.setString(6, st.getProvinciaNascita());
            uStudente.setString(7, st.getCittaResidenza());
            uStudente.setString(8, st.getProvinciaResidenza());
            uStudente.setString(9, st.getCapResidenza());
            uStudente.setString(10, st.getTelefono());
            uStudente.setString(11, st.getCorsoLaurea());
            uStudente.setBoolean(12, st.isHandicap());
            uStudente.setInt(13, st.getUtente().getId());
            return uStudente.executeUpdate();
            
        } catch (SQLException ex) {
            throw new DataException("Unable to update studente", ex);
        }
    }    
    
}
