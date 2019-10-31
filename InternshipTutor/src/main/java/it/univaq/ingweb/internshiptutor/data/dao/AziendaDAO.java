/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
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
