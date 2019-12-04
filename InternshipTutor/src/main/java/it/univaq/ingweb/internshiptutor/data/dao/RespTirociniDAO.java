package it.univaq.ingweb.internshiptutor.data.dao;

import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.RespTirocini;
import java.sql.ResultSet;

/**
 *
 * @author Stefano Florio
 */
public interface RespTirociniDAO {
    
    RespTirocini createRespTirocini();
    
    RespTirocini createRespTirocini(ResultSet rs) throws DataException;
    
    RespTirocini getRespTirocini(int id) throws DataException;
    
    int insertRespTirocini(RespTirocini rt) throws DataException;
    
    int deleteRespTirocini(int id) throws DataException;
    
    int updateRespTirocini(RespTirocini rt) throws DataException;
}
