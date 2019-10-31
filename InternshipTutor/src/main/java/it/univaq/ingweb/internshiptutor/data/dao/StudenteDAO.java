/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import java.sql.ResultSet;

/**
 *
 * @author Stefano Florio
 */
public interface StudenteDAO {
    
    Studente createStudente();
    
    Studente createStudente(ResultSet rs) throws DataException;
    
    Studente getStudente(int id) throws DataException;
    
    void insertStudente(Studente st) throws DataException;
       
}
