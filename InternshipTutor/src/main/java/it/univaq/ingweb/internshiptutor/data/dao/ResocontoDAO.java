/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Resoconto;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
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
    
    Resoconto getResoconto(int id_st, int id_ot) throws DataException;
    
    void insertResoconto(Resoconto r) throws DataException;
}
