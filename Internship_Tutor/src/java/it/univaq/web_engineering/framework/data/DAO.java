package it.univaq.web_engineering.framework.data;

import java.sql.Connection;

/**
 *
 * @author Giuseppe Della Penna
 */
public class DAO {

    protected final DataLayer dataLayer;
    protected final Connection connection;

    public DAO(DataLayer d) {
        this.dataLayer = d;
        this.connection = d.getConnection();
    }

    protected DataLayer getDataLayer() {
        return dataLayer;
    }

    protected Connection getConnection() {
        return connection;
    }

    public void init() throws DataException {

    }

    public void destroy() throws DataException {

    }
}
