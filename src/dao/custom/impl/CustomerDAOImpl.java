package dao.custom.impl;

import dao.custom.CustomerDAO;
import entity.Customer;
import util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 1:22 AM
 **/

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<Customer> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Customer");
        ArrayList<Customer> allCustomers = new ArrayList<>();
        while (rst.next()) {
            allCustomers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return allCustomers;
    }

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO Customer VALUES(?,?,?,?,?,?,?)",
                entity.getCusID(), entity.getCusTitle(), entity.getCusName(), entity.getCusAddress(), entity.getCity(), entity.getProvince(), entity.getPostalCode());
    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE Customer SET cusTitle = ?, cusName = ?, cusAddress = ?, city = ?, province = ?, postalCode = ? WHERE cusID = ?",
                entity.getCusTitle(), entity.getCusName(), entity.getCusAddress(), entity.getCity(), entity.getProvince(), entity.getPostalCode(), entity.getCusID());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("DELETE FROM Customer WHERE cusID = ?", id);
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeQuery("SELECT CusID FROM Customer WHERE CusID = ?", id).next();
    }

    @Override
    public String generateID() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT cusID FROM Customer ORDER BY cusID DESC LIMIT 1");

        if (rst.next()) {
            String custId = rst.getString("CusID");
            int newCustomerID = Integer.parseInt(custId.replace("C00-", "")) + 1;
            return String.format("C00-%03d", newCustomerID);

        } else {
            return "C00-001";
        }
    }

    @Override
    public Customer search(String s) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Customer WHERE cusID = ?", s);
        if (rst.next()) {
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
        }
        return null;
    }


}
