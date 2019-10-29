/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.framework.data.DAO;
import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.framework.data.DataLayer;
import it.univaq.web_engineering.internship_tutor.data.model.Studente;
import it.univaq.web_engineering.internship_tutor.data.proxy.StudenteProxy;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Stefano Florio
 */
public class StudenteDAO_MySQL extends DAO implements StudenteDAO {
    
    private PreparedStatement sStudenteById;
    private PreparedStatement iStudente;

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
                    + "(?,?,?,?,?,?,?,?,?,?,?,?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing internship tutor datalayer", ex);
        }
        
    }

    @Override
    public void destroy() throws DataException {
        try {
            sStudenteById.close();
            iStudente.close();
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
            s.setDataNascita(rs.getDate("data_nascita"));
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
    public void insertStudente(Studente st) throws DataException {
        try {
            if (st.getUtente() != null)
                iStudente.setInt(1, st.getUtente().getId());
            else 
                iStudente.setNull(1, java.sql.Types.INTEGER);
            iStudente.setString(2, st.getCognome());
            iStudente.setString(3, st.getCF());
            iStudente.setDate(4, (Date)st.getDataNascita());
            iStudente.setString(5, st.getCittaNascita());
            iStudente.setString(6, st.getProvinciaNascita());
            iStudente.setString(7, st.getCittaResidenza());
            iStudente.setString(8, st.getProvinciaResidenza());
            iStudente.setString(9, st.getCapResidenza());
            iStudente.setString(10, st.getTelefono());
            iStudente.setString(11, st.getCorsoLaurea());
            iStudente.setBoolean(12, st.isHandicap());
            iStudente.executeUpdate();
            
        } catch (SQLException ex) {
            throw new DataException("Unable to insert new studente", ex);
        }
    }
    
    
    
    
    
    
    
    
    
}
