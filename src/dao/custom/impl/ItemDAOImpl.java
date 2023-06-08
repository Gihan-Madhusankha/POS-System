package dao.custom.impl;

import dao.custom.ItemDAO;
import entity.Item;
import util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 9:48 AM
 **/

public class ItemDAOImpl implements ItemDAO {

    @Override
    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Item");
        ArrayList<Item> allItems = new ArrayList<>();

        while (rst.next()) {
            allItems.add(new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5)
            ));
        }
        return allItems;
    }

    @Override
    public boolean save(Item dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO Item VALUES(?,?,?,?,?)",
                dto.getItemCode(), dto.getDescription(), dto.getPackSize(), dto.getUnitPrice(), dto.getQtyOnHand());
    }

    @Override
    public boolean update(Item dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE Item SET description = ?, packSize = ?, unitPrice = ?, qtyOnHand = ? WHERE itemCode = ?",
                dto.getDescription(), dto.getPackSize(), dto.getUnitPrice(), dto.getQtyOnHand(), dto.getItemCode());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("DELETE FROM Item WHERE itemCode = ?", id);
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeQuery("SELECT itemCode FROM Item WHERE itemCode = ?", id).next();
    }

    @Override
    public String generateID() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1");

        if (rst.next()) {
            String id = rst.getString("itemCode");
            int newCustomerID = Integer.parseInt(id.replace("I00-", "")) + 1;
            return String.format("I00-%03d", newCustomerID);

        } else {
            return "I00-001";
        }
    }

    @Override
    public Item search(String s) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Item WHERE itemCode = ?", s);
        if (rst.next()) {
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5)
            );
        }
        return null;
    }

    @Override
    public boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE Item SET qtyOnHand = qtyOnHand - ? WHERE itemCode = ?", qty, itemCode);
    }
}
