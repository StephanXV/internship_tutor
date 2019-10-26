/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.model.Utente;
import java.sql.ResultSet;

/**
 *
 * @author Stefano Florio
 */
public interface UtenteDAO {
    
    Utente createUtente();
    
    Utente createUtente(ResultSet rs)  throws DataException;
 
    Utente getUtente(int id) throws DataException;
}
