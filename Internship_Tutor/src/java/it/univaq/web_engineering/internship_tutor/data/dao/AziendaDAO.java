/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.model.Azienda;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public interface AziendaDAO {
    
    Azienda createAzienda();
    
    Azienda createAzienda(ResultSet rs) throws DataException;
    
    Azienda getAzienda(int id) throws DataException;
    
    List<Azienda> getAziendeConvenzionate() throws DataException;

    List<Azienda> getAziendeInAttesaConvenzione() throws DataException;
    
    List<Azienda> getAziendeRifiutate() throws DataException;
        
    Azienda updateAziendaStato(Azienda az, int stato) throws DataException;
    
    void insertAzienda(Azienda az) throws DataException;
}
