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
    
    List<OffertaTirocinio> getOfferteTirocinio(Azienda az) throws DataException;
    
    OffertaTirocinio getOffertaTirocinio(int id) throws DataException;
    
    void insertOffertaTirocinio(OffertaTirocinio ot) throws DataException;
}
