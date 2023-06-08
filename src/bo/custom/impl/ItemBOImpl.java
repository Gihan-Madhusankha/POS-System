package bo.custom.impl;

import bo.custom.ItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import entity.Item;
import dto.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 12:43 PM
 **/

public class ItemBOImpl implements ItemBO {
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public String generateNewItemCode() throws SQLException, ClassNotFoundException {
        return itemDAO.generateID();
    }

    @Override
    public boolean deleteItem(String itemCode) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(itemCode);
    }

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> all = itemDAO.getAll();
        ArrayList<ItemDTO> allItems = new ArrayList<>();
        for (Item item : all) {
            allItems.add(new ItemDTO(
                    item.getItemCode(), item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand()
            ));
        }
        return allItems;
    }

    @Override
    public boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        return itemDAO.save(new Item(dto.getItemCode(), dto.getDescription(), dto.getPackSize(), dto.getUnitPrice(), dto.getQtyOnHand()));
    }

    @Override
    public boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        return itemDAO.update(new Item(dto.getItemCode(), dto.getDescription(), dto.getPackSize(), dto.getUnitPrice(), dto.getQtyOnHand()));
    }

    @Override
    public boolean existItem(String id) throws SQLException, ClassNotFoundException {
        return itemDAO.exist(id);
    }
}
