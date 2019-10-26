/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.model.Azienda;
import it.univaq.web_engineering.internship_tutor.data.model.OffertaTirocinio;
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
}
