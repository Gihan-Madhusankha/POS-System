package dao.custom;

import dao.CrudDAO;
import entity.Item;

import java.sql.SQLException;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 9:41 AM
 **/

public interface ItemDAO extends CrudDAO<Item, String> {
    public boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException;
}
