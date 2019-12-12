/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.OffertaTirocinio;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public interface OffertaTirocinioDAO {
    
    OffertaTirocinio createOffertaTirocinio();
    
    OffertaTirocinio createOffertaTirocinio(ResultSet rs) throws DataException;
    
    List<OffertaTirocinio> getOfferteTirocinio(Azienda az, boolean attiva) throws DataException;
    
    List<OffertaTirocinio> getOfferteTirocinio(Azienda az) throws DataException;
    
    OffertaTirocinio getOffertaTirocinio(int id) throws DataException;
    
    int insertOffertaTirocinio(OffertaTirocinio ot) throws DataException;
    
    int updateOffertaTirocinioAttiva(int id_ot, boolean attiva) throws DataException;

    List<OffertaTirocinio> searchOffertaTirocinio(String durata, String titolo, boolean facilitazioni, String luogo, String settore, String obiettivi, String corsoStudio) throws DataException;
    
    List<OffertaTirocinio> getBestFiveOffertaTirocinio() throws DataException;

    List<OffertaTirocinio> getAllOfferteTirocinio() throws DataException;
}
