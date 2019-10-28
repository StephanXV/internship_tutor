/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.model.TutoreTirocinio;
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

