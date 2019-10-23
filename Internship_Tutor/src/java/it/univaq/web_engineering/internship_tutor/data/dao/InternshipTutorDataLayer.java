/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.web_engineering.internship_tutor.data.dao;

import it.univaq.web_engineering.internship_tutor.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.DataLayer;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author steph
 */
public class InternshipTutorDataLayer extends DataLayer {

    public InternshipTutorDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }
   
    @Override
    public void init() throws DataException {
        super.init();
        
        //registriamo i nostri dao
    }
        
}
