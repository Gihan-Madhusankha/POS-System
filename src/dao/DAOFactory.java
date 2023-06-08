package dao;

import dao.custom.impl.*;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 1:46 PM
 **/

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return daoFactory == null ? daoFactory = new DAOFactory() : daoFactory;
    }

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDER_DETAIL:
                return new OrderDetailsDAOImpl();
            case QUERY:
                return new QueryDAOImpl();
            default:
                return null;
        }
    }

    public enum DAOTypes {
        CUSTOMER, ITEM, ORDER, ORDER_DETAIL, QUERY
    }

}
