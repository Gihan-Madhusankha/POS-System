package dao.custom.impl;

import dao.custom.OrderDetailsDAO;
import entity.OrderDetails;
import util.SQLUtil;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 9:48 AM
 **/

public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    @Override
    public ArrayList<OrderDetails> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(OrderDetails entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO OrderDetail VALUES(?,?,?,?)",
                entity.getOrderID(), entity.getItemCode(), entity.getOrderQty(), entity.getDiscount());
    }

    @Override
    public boolean update(OrderDetails entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateID() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public OrderDetails search(String s) throws SQLException, ClassNotFoundException {
        return null;
    }
}
