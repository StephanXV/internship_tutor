/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.model.OffertaTirocinio;
import it.univaq.web_engineering.internship_tutor.data.model.Resoconto;
import it.univaq.web_engineering.internship_tutor.data.model.Studente;
import java.sql.ResultSet;

import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public interface ResocontoDAO {
    
    Resoconto createResoconto();
    
    Resoconto createResoconto(ResultSet rs) throws DataException;
    
    List<Resoconto> getResoconti(Studente st) throws DataException;
    
    List<Resoconto> getResoconti(OffertaTirocinio ot) throws DataException;
    
    Resoconto getResoconto(Studente st, OffertaTirocinio ot) throws DataException;
}
