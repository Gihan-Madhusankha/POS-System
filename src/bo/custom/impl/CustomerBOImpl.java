package bo.custom.impl;

import bo.custom.CustomerBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import entity.Customer;
import dto.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 11:46 AM
 **/

public class CustomerBOImpl implements CustomerBO {
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);


    @Override
    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }

    @Override
    public String generateNewCustomerID() throws SQLException, ClassNotFoundException {
        return customerDAO.generateID();
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> all = customerDAO.getAll();
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        for (Customer c : all) {
            allCustomers.add(new CustomerDTO(
                    c.getCusID(), c.getCusTitle(), c.getCusName(), c.getCusAddress(), c.getCity(),
                    c.getProvince(), c.getPostalCode()
            ));
        }
        return allCustomers;
    }

    @Override
    public boolean saveCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.save(new Customer(
                dto.getId(), dto.getTitle(), dto.getName(), dto.getAddress(), dto.getCity(), dto.getProvince(),
                dto.getPostalCode()
        ));
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(
                dto.getId(), dto.getTitle(), dto.getName(), dto.getAddress(), dto.getCity(), dto.getProvince(),
                dto.getPostalCode()
        ));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }
}
