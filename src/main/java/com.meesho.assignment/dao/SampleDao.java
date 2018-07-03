package com.meesho.assignment.dao;

import com.meesho.assignment.config.SQLConfig;
import lombok.Builder;
import lombok.Data;

import java.sql.*;

/**
 * Created by hitesh.jain1 on 02/07/18.
 */

@Builder
@Data
public class SampleDao {
    Connection connection = null;
    SQLConfig sqlConfig;

    public boolean createConnection() {
        try {
            connection = DriverManager.getConnection(sqlConfig.getUrl() + sqlConfig.getDatabase(), sqlConfig.getUser(), sqlConfig.getPassword());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean closeConnection() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void createOrder(Order order) throws SQLException {
        String insertSql = "INSERT INTO orders (orderid, name, userid) VALUES (?,?,?);";

        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setLong(1, order.getOrderId());
        preparedStatement.setString(2, order.getName());
        preparedStatement.setLong(3, order.getUserId());
        preparedStatement.execute();
        preparedStatement.close();
    }

    public ResultSet getOrder(Long orderId) throws SQLException {
        String selectSQL = "SELECT * from orders where orderid = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setLong(1, orderId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
