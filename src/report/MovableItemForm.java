package report;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import util.SQLUtil;
import view.tm.OrderItemListTM;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Gihan Madhusankha
 * 2022-06-05 4:24 PM
 **/

public class MovableItemForm {

    private static String from;
    private static String to;
    public AnchorPane IncomeReportContext;
    public JFXButton btnGet;
    public JFXDatePicker txtFromDate;
    public JFXDatePicker txtToDate;
    public TableView<OrderItemListTM> tblMost;
    public TableView<OrderItemListTM> tblLeast;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colItemCode1;
    public TableColumn colDescription1;
    public TableColumn colQty1;

    public void initialize() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemCode1.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription1.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colQty1.setCellValueFactory(new PropertyValueFactory<>("qty"));

        txtFromDate.setEditable(false);
        txtToDate.setEditable(false);
    }


    public void getButtonOnAction(ActionEvent actionEvent) {
        if (txtFromDate.getValue() != null && txtToDate.getValue() != null) {
            try {
                setDetails();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            new Alert(Alert.AlertType.WARNING, "Some values are empty..!!").show();
        }
    }

    private void setDetails() throws SQLException, ClassNotFoundException {
        int year1 = txtFromDate.getValue().getYear();
        int month1 = txtFromDate.getValue().getMonth().getValue();
        int dayOfMonth1 = txtFromDate.getValue().getDayOfMonth();

        int year2 = txtToDate.getValue().getYear();
        int month2 = txtToDate.getValue().getMonth().getValue();
        int dayOfMonth2 = txtToDate.getValue().getDayOfMonth();

        from = year1 + "-" + month1 + "-" + dayOfMonth1;
        to = year2 + "-" + month2 + "-" + dayOfMonth2;

        setValues("DESC", from, to);
        setValues("ASC", from, to);
    }

    private void setValues(String i, String from, String to) throws SQLException, ClassNotFoundException {
        ObservableList<OrderItemListTM> mostMovableList = FXCollections.observableArrayList();
        ObservableList<OrderItemListTM> leastMovableList = FXCollections.observableArrayList();

        ResultSet resultSet = SQLUtil.executeQuery("SELECT *, SUM((OD.orderQty * I.unitPrice) - (OD.orderQty * I.unitPrice * OD.discount / 100)) AS total FROM Orders O\n" +
                "INNER JOIN OrderDetail OD ON\n" +
                "O.orderID = OD.orderId\n" +
                "INNER JOIN Item I ON\n" +
                "OD.itemCode = I.itemCode WHERE orderDate BETWEEN ? AND ? \n" +
                "GROUP BY I.itemCode ORDER BY OD.orderQty " + i + " LIMIT 3", from, to);

        if (i.equals("DESC")) {
            while (resultSet.next()) {
                mostMovableList.add(new OrderItemListTM(
                        resultSet.getString(5),
                        resultSet.getString(9),
                        resultSet.getInt(6)
                ));

            }
            tblMost.setItems(mostMovableList);
        } else {
            while (resultSet.next()) {
                leastMovableList.add(new OrderItemListTM(
                        resultSet.getString(5),
                        resultSet.getString(9),
                        resultSet.getInt(6)
                ));
            }
            tblLeast.setItems(leastMovableList);
        }
    }

}
