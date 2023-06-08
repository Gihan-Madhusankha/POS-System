package dao.custom.impl;

import dao.custom.OrderDAO;
import entity.Orders;
import util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 9:48 AM
 **/

public class OrderDAOImpl implements OrderDAO {

    @Override
    public ArrayList<Orders> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Orders entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO Orders VALUES(?,?,?)", entity.getOrderID(), entity.getOrderDate(), entity.getCusID());
    }

    @Override
    public boolean update(Orders dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exist(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateID() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT orderID FROM Orders ORDER BY orderID DESC LIMIT 1");

        if (rst.next()) {
            String id = rst.getString(1);
            int newOrderID = Integer.parseInt(id.replace("ORD-", "")) + 1;
            return String.format("ORD-%03d", newOrderID);

        } else {
            return "ORD-001";
        }
    }

    @Override
    public Orders search(String s) throws SQLException, ClassNotFoundException {
        return null;
    }
}
