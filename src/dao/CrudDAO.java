package dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 1:20 AM
 **/

public interface CrudDAO<T, ID> extends SuperDAO{

    ArrayList<T> getAll() throws SQLException, ClassNotFoundException;

    boolean save(T dto) throws SQLException, ClassNotFoundException;

    boolean update(T dto) throws SQLException, ClassNotFoundException;

    boolean delete(ID id) throws SQLException, ClassNotFoundException;

    boolean exist(ID id) throws SQLException, ClassNotFoundException;

    String generateID() throws SQLException, ClassNotFoundException;

    T search(ID id) throws SQLException, ClassNotFoundException;
}
