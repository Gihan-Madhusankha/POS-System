package bo.custom;

import bo.SuperBO;
import dto.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 12:44 PM
 **/

public interface ItemBO extends SuperBO {
    String generateNewItemCode() throws SQLException, ClassNotFoundException;

    boolean deleteItem(String itemCode) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;

    boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException;

    boolean existItem(String id) throws SQLException, ClassNotFoundException;
}
