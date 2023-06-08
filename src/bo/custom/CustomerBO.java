package bo.custom;

import bo.SuperBO;
import dto.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 12:44 PM
 **/

public interface CustomerBO extends SuperBO {
    boolean existCustomer(String id) throws SQLException, ClassNotFoundException;

    String generateNewCustomerID() throws SQLException, ClassNotFoundException;

    ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException;

    boolean saveCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
}
