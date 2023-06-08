package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import dto.OrderDTO;
import util.SQLUtil;
import util.Today;
import util.ValidateUtil;
import view.tm.OrderItemListTM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author : Gihan Madhusankha
 * 2022-05-30 12:16 PM
 **/

public class ManageOrderFormController {

    public TextField txtSearchField;
    public TableView<OrderDTO> tblOrderNO;
    public JFXComboBox<String> cmbCusID;
    public Label lblCusName;
    public JFXButton btnConfirm;
    public JFXTextField txtItemCode;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtDescription;
    public JFXTextField txtDiscount;
    public JFXTextField txtOrderDate;
    public JFXTextField txtQty;
    public TableView<OrderItemListTM> tblOrder;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colOperate;
    public TableColumn colOrderNo;
    public TableColumn colDelete;
    public TableColumn colDiscount;
    public JFXTextField txtTotalAmount;
    public JFXTextField txtTotalDiscount;
    public JFXTextField txtTotal;
    public JFXButton btnUpdate;
    public Label lblQtyOnHand;
    public AnchorPane manageOrderContext;
    public Label lblDate;
    public Label lblTime;
    OrderItemListTM selectedItem = null;
    ObservableList<OrderItemListTM> itemListTMS = null;
    ObservableList<OrderDTO> orList = null;
    OrderDTO selectedOrder = null;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        try {
            colOrderNo.setCellValueFactory(new PropertyValueFactory<>("orderID"));
            colDelete.setCellValueFactory(param -> {
                ImageView delete = new ImageView("view/asserts/images/delete.png");

                HBox hBox = new HBox(delete);
                delete.setFitHeight(20);
                delete.setFitWidth(20);
                delete.setStyle("-fx-cursor: Hand");

                hBox.setStyle("-fx-alignment: center");
                HBox.setMargin(delete, new Insets(2, 2, 2, 3));

                deleteOrder(delete);
                return new ReadOnlyObjectWrapper<>(hBox);
            });

            colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
            colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            colOperate.setCellValueFactory(param -> {
                ImageView edit = new ImageView("view/asserts/images/edit.png");
                ImageView delete = new ImageView("view/asserts/images/delete.png");

                HBox hBox = new HBox(edit, delete);
                edit.setFitHeight(20);
                edit.setFitWidth(20);
                delete.setFitHeight(20);
                delete.setFitWidth(20);
                delete.setStyle("-fx-cursor: Hand");
                edit.setStyle("-fx-cursor: Hand");

                hBox.setStyle("-fx-alignment: center");
                HBox.setMargin(edit, new Insets(2, 3, 2, 2));
                HBox.setMargin(delete, new Insets(2, 2, 2, 3));

                clickedEditBtn(edit);
                deleteOrderItem(delete);
                return new ReadOnlyObjectWrapper<>(hBox);
            });

            Today.setDateAndTime(lblDate, lblTime);
            btnUpdate.setDisable(true);
            btnConfirm.setDisable(true);
            getAllCustomerIDs();
            loadCustomerName();
            loadAllOrderNoOfSpecificCustomer();
            loadAllItemsOfSpecificOrderID();

            Pattern discountPattern = Pattern.compile("^[0-9]{1,2}(.[0-9]{2})?$");
            Pattern qtyPattern = Pattern.compile("^[1-9]([0-9])?$");
            map.put(txtDiscount, discountPattern);
            map.put(txtQty, qtyPattern);


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private boolean deleteOrder(ImageView delete) {
        /*delete order item*/
        delete.setOnMouseClicked(event -> {
            selectedOrder = tblOrderNO.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure delete this order item ?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)) {
                try {
                    for (OrderItemListTM tm : itemListTMS) {
                        SQLUtil.executeUpdate("UPDATE Item SET qtyOnHand = qtyOnHand + ? WHERE itemCode = ?",
                                tm.getQty(), tm.getItemCode());
                    }

                    boolean b = SQLUtil.executeUpdate("DELETE FROM Orders WHERE orderID = ? ", selectedOrder.getOrderID());
                    if (b) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
                        orList.remove(selectedOrder);
                        tblOrderNO.refresh();
                        tblOrder.refresh();
                        itemListTMS.clear();

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        return false;
    }

    private boolean deleteOrderItem(ImageView delete) {
        /*delete order item*/
        delete.setOnMouseClicked(event -> {
            selectedItem = tblOrder.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure delete this order item ?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)) {
                try {
                    SQLUtil.executeUpdate("UPDATE Item SET qtyOnHand = qtyOnHand + ? WHERE itemCode = ?",
                            selectedItem.getQty(), selectedItem.getItemCode());


                    boolean b = SQLUtil.executeUpdate("DELETE FROM OrderDetail WHERE orderID = ? && itemCode = ?", selectedItem.getOrderID(), selectedItem.getItemCode());
                    if (b) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
//                        itemListTMS.removeIf(tm -> txtItemCode.getText().equals(tm.getItemCode()));
                        itemListTMS.remove(selectedItem);
                        tblOrder.refresh();

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        return false;
    }

    private void clickedEditBtn(ImageView edit) {
        edit.setOnMouseClicked(event -> {
            selectedItem = tblOrder.getSelectionModel().getSelectedItem();
            txtItemCode.setText(selectedItem.getItemCode());
            txtDescription.setText(selectedItem.getDescription());
            txtPackSize.setText(selectedItem.getPackSize());
            txtUnitPrice.setText(String.valueOf(selectedItem.getUnitPrice()));
            txtQty.setText(String.valueOf(selectedItem.getQty()));

            fieldsToText();
        });

        txtItemCode.setEditable(false);
        txtDescription.setEditable(false);
        txtPackSize.setEditable(false);
        txtUnitPrice.setEditable(false);
        txtOrderDate.setEditable(false);
        txtTotalAmount.setEditable(false);
        txtTotalDiscount.setEditable(false);
        txtTotal.setEditable(false);
        btnUpdate.setDisable(false);
    }

    private void fieldsToText() {
        for (OrderItemListTM tm : itemListTMS) {
            if (selectedItem.getItemCode().equals(tm.getItemCode())) {
                txtOrderDate.setText(tm.getDate());
                txtDiscount.setText(String.valueOf(tm.getDiscount()));
                double totalAmount = (tm.getUnitPrice() * tm.getQty());
                double totalDiscount = totalAmount * tm.getDiscount() / 100;
                double total = (totalAmount - totalDiscount);
                txtTotalAmount.setText(String.valueOf(totalAmount));
                txtTotalDiscount.setText(String.valueOf(totalDiscount));
                txtTotal.setText(String.valueOf(total));
                lblQtyOnHand.setText(String.valueOf(tm.getQtyOnHand()));

            }
        }
    }

    private void loadAllItemsOfSpecificOrderID() throws SQLException, ClassNotFoundException {
        tblOrderNO.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {
                if (newValue != null) {

                    ResultSet rst = SQLUtil.executeQuery("SELECT *, (((I.unitPrice * OD.orderQty) - (I.unitPrice * OD.orderQty * OD.discount / 100))) AS Total   From Orders O\n" +
                            "INNER JOIN OrderDetail OD\n" +
                            "    ON O.orderID = OD.orderID\n" +
                            "INNER JOIN Item I on OD.itemCode = I.itemCode WHERE O.orderID = ?", newValue.getOrderID());


                    itemListTMS = FXCollections.observableArrayList();

                    while (rst.next()) {
                        itemListTMS.add(new OrderItemListTM(
                                rst.getString(1),
                                rst.getString(5),
                                rst.getString(9),
                                rst.getString(10),
                                rst.getDouble(11),
                                rst.getDouble(7),
                                rst.getInt(6),
                                rst.getDouble(13),
                                rst.getString(2),
                                rst.getInt(12)
                        ));
                    }
                    clearForm();
                    tblOrder.setItems(itemListTMS);
                }


            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

    }

    private void loadAllOrderNoOfSpecificCustomer() {
        cmbCusID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
//                if (newValue != null) {

                ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Orders WHERE cusID = ?", newValue);
                orList = FXCollections.observableArrayList();
                while (rst.next()) {
                    orList.add(new OrderDTO(
                            rst.getString(1),
                            LocalDate.parse(rst.getString(2)),
                            rst.getString(3)
                    ));
                }
                tblOrderNO.setItems(orList);

                tblOrder.getItems().clear();
                clearForm();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void clearForm() {
        txtItemCode.clear();
        txtDescription.clear();
        txtPackSize.clear();
        txtUnitPrice.clear();
        txtDiscount.clear();
        txtOrderDate.clear();
        txtQty.clear();
        txtTotalAmount.clear();
        txtTotalDiscount.clear();
        txtTotal.clear();
        lblQtyOnHand.setText("00");
        btnUpdate.setDisable(true);
    }

    private void loadCustomerName() {
        cmbCusID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Customer WHERE cusID = ?", newValue);
                if (rst.next()) {
                    lblCusName.setText(rst.getString(3));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });
    }

    private void getAllCustomerIDs() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.executeQuery("SELECT cusID FROM Customer");
        ObservableList<String> idList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        cmbCusID.setItems(idList);
    }

    public void navigateToHome(MouseEvent event) {
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {

        if (Integer.parseInt(txtQty.getText()) > Integer.parseInt(lblQtyOnHand.getText())) {
            new Alert(Alert.AlertType.WARNING, "Invalid Qty..!").show();
            return;
        }

        int qty = 0;
        for (OrderItemListTM tm : itemListTMS) {
            if (txtItemCode.getText().equals(tm.getItemCode())) {
                qty = tm.getQty();
                tm.setQty(Integer.parseInt(txtQty.getText()));
                tm.setDiscount(Double.parseDouble(txtDiscount.getText()));
                double total = tm.getUnitPrice() * tm.getQty();
                double dis = (total * tm.getDiscount() / 100);

                tm.setTotal(total - dis);
                txtTotalAmount.setText(String.valueOf(total));
                txtTotalDiscount.setText(String.valueOf(dis));
                txtTotal.setText(String.valueOf(total - dis));
            }
        }
        tblOrder.refresh();
        btnConfirm.setDisable(false);
        int newQty = Integer.parseInt(txtQty.getText());

        if (qty < newQty) {
            lblQtyOnHand.setText(String.valueOf(Integer.parseInt(lblQtyOnHand.getText()) - (newQty - qty)));
        } else if (qty > newQty) {
            lblQtyOnHand.setText(String.valueOf(Integer.parseInt(lblQtyOnHand.getText()) + (qty - newQty)));
        } else {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated the previous qty.");
        }


        try {
            SQLUtil.executeUpdate("UPDATE Item SET qtyOnHand = ? WHERE itemCode = ?",
                    lblQtyOnHand.getText(), txtItemCode.getText());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cancelBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) manageOrderContext.getScene().getWindow();
        stage.close();
    }

    public void confirmBtnOnAction(ActionEvent actionEvent) {
        try {
            for (OrderItemListTM tm : itemListTMS) {
                SQLUtil.executeUpdate("UPDATE orderDetail SET orderQty = ?, discount = ? WHERE orderID = ? && itemCode = ?",
                        tm.getQty(), tm.getDiscount(), tm.getOrderID(), tm.getItemCode());
            }
            clearForm();
            orList.clear();
            cmbCusID.getSelectionModel().clearSelection();
            btnConfirm.setDisable(true);
            new Alert(Alert.AlertType.CONFIRMATION, "Saved..!").show();


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void textField_Key_Released(KeyEvent keyEvent) {
        ValidateUtil.validate(map, btnUpdate);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidateUtil.validate(map, btnUpdate);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

}
