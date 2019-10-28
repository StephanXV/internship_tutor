package it.univaq.web_engineering.internship_tutor.data.dao;


import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.model.RespTirocini;
import java.sql.ResultSet;

/**
 *
 * @author Stefano Florio
 */
public interface RespTirociniDAO {
    
    RespTirocini createRespTirocini();
    
    RespTirocini createRespTirocini(ResultSet rs) throws DataException;
    
    RespTirocini getRespTirocini(int id) throws DataException;
    
    void insertRespTirocini(RespTirocini rt) throws DataException;
}
