/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.Candidatura;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public interface CandidaturaDAO {
    
    Candidatura createCandidatura();
    
    Candidatura createCandidatura(ResultSet rs) throws DataException;
    
    List<Candidatura> getCandidature(Studente st) throws DataException;
    
    List<Candidatura> getCandidature(OffertaTirocinio ot, int stato) throws DataException;
    
    List<Candidatura> getCandidature(OffertaTirocinio ot) throws DataException;
    
    Candidatura getCandidatura(int id_st, int id_ot) throws DataException;
    
    void insertCandidatura(Candidatura c) throws DataException;
    
}
