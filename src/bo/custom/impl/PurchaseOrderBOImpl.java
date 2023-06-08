package bo.custom.impl;

import bo.custom.PurchaseOrderBO;
import dao.DAOFactory;
import dao.custom.*;
import db.DBConnection;
import entity.Customer;
import entity.Item;
import entity.OrderDetails;
import entity.Orders;
import javafx.scene.control.Alert;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailsDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 11:47 AM
 **/

public class PurchaseOrderBOImpl implements PurchaseOrderBO {
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    @Override
    public void purchaseOrder(OrderDTO orders, ArrayList<OrderDetailsDTO> orDetails) throws SQLException, ClassNotFoundException {

        /*Transaction*/
        Connection connection = null;
        try {

            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isSavedOrder = saveOrder(orders);

            if (isSavedOrder) {
                boolean isDetailsSaved = saveOrderDetails(orDetails);
                if (isDetailsSaved) {
                    connection.commit();
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved").show();

                } else {
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Error").show();
                }

            } else {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Error").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());

        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public ItemDTO searchItem(String newValue) throws SQLException, ClassNotFoundException {
        if (newValue!=null) {
            Item search = itemDAO.search(newValue);
            return new ItemDTO(search.getItemCode(), search.getDescription(), search.getPackSize(), search.getUnitPrice(), search.getQtyOnHand());
        }
        return null;
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
    public String generateNewOrderID() throws SQLException, ClassNotFoundException {
        return orderDAO.generateID();
    }

    @Override
    public CustomerDTO searchCustomer(String newValue) throws SQLException, ClassNotFoundException {
        Customer cus = customerDAO.search(newValue);
        return new CustomerDTO(cus.getCusID(), cus.getCusTitle(), cus.getCusName(), cus.getCusAddress(), cus.getCity(), cus.getProvince(), cus.getPostalCode());
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> all = customerDAO.getAll();
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        for (Customer cus : all) {
            allCustomers.add(new CustomerDTO(
                    cus.getCusID(), cus.getCusTitle(), cus.getCusName(), cus.getCusAddress(), cus.getCity(), cus.getProvince(), cus.getPostalCode()
            ));
        }
        return allCustomers;
    }

    private boolean saveOrderDetails(ArrayList<OrderDetailsDTO> orDetails) throws SQLException, ClassNotFoundException {
        for (OrderDetailsDTO det : orDetails
        ) {
            boolean save = orderDetailsDAO.save(new OrderDetails(
                    det.getOrderID(), det.getItemCode(), det.getQty(), det.getDiscount()
            ));

            if (save) {
                if (!updateQty(det.getItemCode(), det.getQty())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean saveOrder(OrderDTO orders) throws SQLException, ClassNotFoundException {
        return orderDAO.save(new Orders(orders.getOrderID(), orders.getOrderDate(), orders.getId()));
    }

    private boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        return itemDAO.updateQty(itemCode, qty);
    }

}
