/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.TutoreTirocinio;
import java.sql.ResultSet;

/**
 *
 * @author Stefano Florio
 */
public interface TutoreTirocinioDAO {
    
    TutoreTirocinio createTutoreTirocinio();
    
    TutoreTirocinio createTutoreTirocinio(ResultSet rs) throws DataException;
    
    TutoreTirocinio getTutoreTirocinio(int id) throws DataException;
    
    void insertTutoreTirocinio(TutoreTirocinio tt) throws DataException;
}

