package bo.custom;

import bo.SuperBO;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailsDTO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 12:16 PM
 **/

public interface PurchaseOrderBO extends SuperBO {
    void purchaseOrder(OrderDTO orders, ArrayList<OrderDetailsDTO> orDetails) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String newValue) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;

    String generateNewOrderID() throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String newValue) throws SQLException, ClassNotFoundException;

    ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException;
}
