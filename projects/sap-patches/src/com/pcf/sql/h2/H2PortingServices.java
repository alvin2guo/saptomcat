/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pcf.sql.h2;

import com.sap.sql.jdbc.basic.BasicDbPortingServices;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Implementation to allow SAP to use H2
 * @author thibautc
 */
class H2PortingServices extends BasicDbPortingServices
{

    public H2PortingServices(Connection connection)
    {
        super(connection);
    }

    public String getConnectionID() throws SQLException
    {
        return getUserName() + "@" + getDatabaseHost() + ":" + getDatabaseName();
    }

    public Timestamp getUTCTimestamp() throws SQLException
    {
        // Should get a DB timestamp ... not very importnat for me here
        return new Timestamp(new Date().getTime());
    }

    public String getDatabaseProductName()
            throws SQLException
    {
        return this.connection.getMetaData().getDatabaseProductName();
    }

    public String getDatabaseProductVersion()
            throws SQLException
    {
        return this.connection.getMetaData().getDatabaseProductVersion();
    }

    public String getUserName()
            throws SQLException
    {
        return this.connection.getMetaData().getUserName();
    }

    public String getDatabaseName()
            throws SQLException
    {
        return fetch("SELECT DATABASE()");
    }

    public String getDatabaseHost()
            throws SQLException
    {
        return "localhost";
    }

    public int getVendorID()
    {
        return VENDOR_MYSQL;
    }

    public String getSchemaName()
            throws SQLException
    {
        return this.connection.getMetaData().getUserName();
    }

    public String fetch(String sql) throws SQLException
    {
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = null;
        String result = null;
        try
        {
            resultSet = statement.executeQuery(sql);

            resultSet.next();

            result = resultSet.getString(1);
        } catch (SQLException sqlException)
        {
            SQLException auxSqlException = new SQLException("Couldn't retrieve "+sql, sqlException.getSQLState(), sqlException.getErrorCode());
            auxSqlException.setNextException(sqlException);
            throw auxSqlException;
        } finally
        {
            if (resultSet != null)
            {
                resultSet.close();
            }
            statement.close();
        }
        return result;

    }
}
