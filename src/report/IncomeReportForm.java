package report;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import util.SQLUtil;
import view.tm.OrderItemListTM;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Gihan Madhusankha
 * 2022-06-05 1:24 AM
 **/

public class IncomeReportForm {

    public static String from;
    public static String to;
    public AnchorPane IncomeReportContext;
    public JFXButton btnGet;
    public JFXDatePicker txtFromDate;
    public JFXDatePicker txtToDate;
    public TableView<OrderItemListTM> tblIncome;
    public TableColumn colYear;
    public TableColumn colIncome;
    public TextField txtTotalIncome;

    public void initialize() {
        colYear.setCellValueFactory(new PropertyValueFactory<>("date"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("total"));

        txtFromDate.setEditable(false);
        txtToDate.setEditable(false);
        txtTotalIncome.setEditable(false);

        setIncomeForYear();
    }

    private void setIncomeForYear() {
        try {

            ResultSet rst = SQLUtil.executeQuery("SELECT O.orderDate, SUM((I.unitPrice*OD.orderQty) - (I.unitPrice*OD.orderQty*OD.discount/100)) AS Total\n" +
                    "FROM orders O INNER JOIN OrderDetail OD ON\n" +
                    "O.orderId = OD.orderId\n" +
                    "INNER JOIN Item I on OD.itemCode = I.itemCode\n" +
                    "GROUP BY DATE_FORMAT(O.orderDate, '%Y')");

            ObservableList<OrderItemListTM> obList = FXCollections.observableArrayList();
            while (rst.next()) {
                obList.add(new OrderItemListTM(
                        rst.getString(1).substring(0, 4),
                        rst.getDouble(2)
                ));
            }
            tblIncome.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getButtonOnAction(ActionEvent actionEvent) {
        if (txtFromDate.getValue() != null && txtToDate.getValue() != null) {
            setDetails();

        } else {
            new Alert(Alert.AlertType.WARNING, "Some values are empty..!!").show();
        }
    }

    private void setDetails() {
        int year1 = txtFromDate.getValue().getYear();
        int month1 = txtFromDate.getValue().getMonth().getValue();
        int dayOfMonth1 = txtFromDate.getValue().getDayOfMonth();

        int year2 = txtToDate.getValue().getYear();
        int month2 = txtToDate.getValue().getMonth().getValue();
        int dayOfMonth2 = txtToDate.getValue().getDayOfMonth();

        from = year1 + "-" + month1 + "-" + dayOfMonth1;
        to = year2 + "-" + month2 + "-" + dayOfMonth2;

        setValues(from, to);
    }

    private void setValues(String from, String to) {
        try {
            ResultSet resultSet = SQLUtil.executeQuery("SELECT O.orderDate, SUM((I.unitPrice * OD.orderQty) - (I.unitPrice * OD.orderQty * OD.discount / 100)) AS Total\n" +
                    "FROM orders O\n" +
                    "         INNER JOIN OrderDetail OD ON\n" +
                    "        O.orderId = OD.orderId\n" +
                    "         INNER JOIN Item I on OD.itemCode = I.itemCode\n" +
                    "WHERE orderDate BETWEEN ? AND ?", from, to);

            if (resultSet.next()) {
                txtTotalIncome.setText(resultSet.getString(2));
                txtTotalIncome.setEditable(false);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
