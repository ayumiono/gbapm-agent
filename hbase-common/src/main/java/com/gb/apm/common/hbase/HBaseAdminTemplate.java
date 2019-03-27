package com.gb.apm.common.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

/**
 * @author emeroad
 */
public class HBaseAdminTemplate {

    private final Admin admin;
    private final Connection connection;

    public HBaseAdminTemplate(Configuration configuration) {
        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        } catch (Exception e) {
            throw new HbaseSystemException(e);
        }
    }

    public boolean createTableIfNotExist(HTableDescriptor htd) {
        try {
            if (!admin.tableExists(htd.getTableName())) {
                this.admin.createTable(htd);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new HbaseSystemException(e);
        }
    }

    public boolean tableExists(TableName tableName) {
        try {
            return admin.tableExists(tableName);
        } catch (IOException e) {
            throw new HbaseSystemException(e);
        }
    }

    public boolean dropTableIfExist(TableName tableName) {
        try {
            if (admin.tableExists(tableName)) {
                this.admin.disableTable(tableName);
                this.admin.deleteTable(tableName);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new HbaseSystemException(e);
        }
    }

    public void dropTable(TableName tableName) {
        try {
            this.admin.disableTable(tableName);
            this.admin.deleteTable(tableName);
        } catch (IOException e) {
            throw new HbaseSystemException(e);
        }
    }

    public void close() {
        try {
            this.admin.close();
            this.connection.close();
        } catch (IOException e) {
            throw new HbaseSystemException(e);
        }
    }
}
