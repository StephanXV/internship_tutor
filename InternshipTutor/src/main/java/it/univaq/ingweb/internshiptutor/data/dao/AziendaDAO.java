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
    
    Azienda getAzienda(String ut_username) throws DataException;
    
    List<Azienda> getAziendeByStato(int stato) throws DataException;
        
    int updateAziendaStato(int id_az, int stato) throws DataException;
    
    int insertAzienda(Azienda az) throws DataException;
    
    int deleteAzienda(int id_az) throws DataException;
    
    int updateAziendaDocumento(int id_az, String src) throws DataException;
    
    int getTirocinantiAttivi(Azienda az) throws DataException;
    
    List<Azienda> getBestFiveAziende() throws DataException;
    
    int updateAzienda(Azienda az) throws DataException;
}
