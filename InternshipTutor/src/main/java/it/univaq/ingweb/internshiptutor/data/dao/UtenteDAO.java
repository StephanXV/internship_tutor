/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.Utente;
import java.sql.ResultSet;

/**
 *
 * @author Stefano Florio
 */
public interface UtenteDAO {
    
    Utente createUtente();
    
    Utente createUtente(ResultSet rs)  throws DataException;
 
    Utente getUtente(int id) throws DataException;
    
    Utente getUtente(String username, String password) throws DataException;

    Utente getUtenteByUser(String username) throws DataException;

    boolean checkUtenteExist(String user, String email) throws DataException;
    
    int insertUtente(Utente ut) throws DataException;
    
    int deleteUtente(int id) throws DataException;
    
    int updateUtente(Utente ut) throws DataException;
}
