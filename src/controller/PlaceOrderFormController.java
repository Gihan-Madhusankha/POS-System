package controller;

import bo.BOFactory;
import bo.custom.PurchaseOrderBO;
import bo.custom.impl.PurchaseOrderBOImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailsDTO;
import util.Today;
import util.ValidateUtil;
import view.tm.OrderItemListTM;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author : Gihan Madhusankha
 * 2022-05-30 12:12 PM
 **/

public class PlaceOrderFormController {

    private final PurchaseOrderBO purchaseOrderBO = (PurchaseOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PURCHASE_ORDER);
    public JFXComboBox<String> cmbCusID;
    public TextField txtUnitPrice;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtPackSize;
    public TextField txtDiscount;
    public TextField txtQTY;
    public TableView<OrderItemListTM> tblOrderList;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colOperate;
    public TableColumn colDiscount;
    public JFXButton btnConfirmOrder;
    public Label lblTotalAmount;
    public Label lblTotalDiscount;
    public Label lblTotal;
    public JFXButton btnAdd;
    public Label lblOrderID;
    public Label lblCustomerName;
    public JFXComboBox<String> cmbItemCode;
    public Label lblCustomerTitle;
    public AnchorPane placeOrderContext;
    public Label lblDate;
    public Label lblTime;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    ObservableList<OrderItemListTM> toList = FXCollections.observableArrayList();
    OrderItemListTM orderItemListTM = null;

    public void initialize() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
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
            deleteCustomer(delete);
            return new ReadOnlyObjectWrapper<>(hBox);

        });

        try {

            Today.setDateAndTime(lblDate, lblTime);
            loadAllCustomerIds();
            setCustomerDetails();
            loadAllItemCodes();
            setItemDetails();
            lblOrderID.setText(generateNewOrderID());
            btnAdd.setDisable(true);
            enableOrDisableConfirmBtn();

            Pattern qtyPattern = Pattern.compile("^[0-9]{1,3}$");
            Pattern discountPattern = Pattern.compile("^[0-9]{1,2}$");
            map.put(txtQTY, qtyPattern);
            map.put(txtDiscount, discountPattern);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void enableOrDisableConfirmBtn() {
        btnConfirmOrder.setDisable(tblOrderList.getSelectionModel().isEmpty());
    }

    private void deleteCustomer(ImageView delete) {
        delete.setOnMouseClicked(event -> {
            orderItemListTM = tblOrderList.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure delete this customer ?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)) {
                toList.remove(orderItemListTM);
                calculateAmount();
                enableOrDisableConfirmBtn();
            }
        });
    }

    private void clickedEditBtn(ImageView edit) {
        edit.setOnMouseClicked(event -> {
            orderItemListTM = tblOrderList.getSelectionModel().getSelectedItem();
            cmbItemCode.setValue(orderItemListTM.getItemCode());
            cmbItemCode.setEditable(false);
            txtDescription.setText(orderItemListTM.getDescription());
            txtPackSize.setText(orderItemListTM.getPackSize());
            txtUnitPrice.setText(String.valueOf(orderItemListTM.getUnitPrice()));
            txtDiscount.setText(String.valueOf(orderItemListTM.getDiscount()));
            txtQTY.setText(String.valueOf(orderItemListTM.getQty()));
            btnAdd.setDisable(false);
            btnAdd.setText("Update");
        });
    }

    private void setItemDetails() {
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {
                if (newValue!=null) {

                    ItemDTO search = purchaseOrderBO.searchItem(newValue);

                    txtDescription.setText(search.getDescription());
                    txtPackSize.setText(search.getPackSize());
                    txtUnitPrice.setText(String.valueOf(search.getUnitPrice()));
                    txtQtyOnHand.setText(String.valueOf(search.getQtyOnHand()));

                    txtDescription.setEditable(false);
                    txtPackSize.setEditable(false);
                    txtUnitPrice.setEditable(false);
                    txtQtyOnHand.setEditable(false);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });
    }

    private void loadAllItemCodes() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> all = purchaseOrderBO.getAllItems();
        ObservableList<String> itemList = FXCollections.observableArrayList();

        for (ItemDTO dto : all) {
            itemList.add(dto.getItemCode());
        }
        cmbItemCode.setItems(itemList);
    }

    private String generateNewOrderID() throws SQLException, ClassNotFoundException {
        return purchaseOrderBO.generateNewOrderID();
    }

    private void setCustomerDetails() {
        cmbCusID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null) {
                    CustomerDTO search = purchaseOrderBO.searchCustomer(newValue);
                    lblCustomerTitle.setText(search.getTitle());
                    lblCustomerName.setText(search.getName());
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadAllCustomerIds() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> all = purchaseOrderBO.getAllCustomers();
        ObservableList<String> itemListTMS = FXCollections.observableArrayList();

        for (CustomerDTO dto : all) {
            itemListTMS.add(dto.getId());
        }
        cmbCusID.setItems(itemListTMS);
    }

    public void navigateToHome(MouseEvent event) {
    }

    public void textField_Key_Released(KeyEvent keyEvent) {
        ValidateUtil.validate(map, btnAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidateUtil.validate(map, btnAdd);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

    public void addCustomerOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/manageCustomerForm.fxml"))));
            stage.show();
            loadAllCustomerIds();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cancelBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) placeOrderContext.getScene().getWindow();
        stage.close();
    }

    public void confirmOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
//        boolean b = saveOrder(lblOrderID.getText(), LocalDate.now(), cmbCusID.getValue(),
//                tblOrderList.getItems().stream().map(tm -> new OrderDetailsDTO(tm.getItemCode(), tm.getQty(), tm.getDiscount())).collect(Collectors.toList()));

    /*    ArrayList<OrderDetailsDTO> itemList = new ArrayList<>();
        for (OrderItemListTM detail : toList) {
            itemList.add(new OrderDetailsDTO(
                    lblOrderID.getText(),
                    detail.getItemCode(),
                    detail.getQty(),
                    detail.getDiscount()
            ));
        }

        boolean b = saveOrder(lblOrderID.getText(), LocalDate.now(), cmbCusID.getValue(), itemList);

//                tblOrderList.getItems().stream().map(tm -> new OrderDetailsDTO(tm.getItemCode(), tm.getQty(), tm.getDiscount())).collect(Collectors.toList()));

        if (b) {
            new Alert(Alert.AlertType.CONFIRMATION, "Order has been placed successfully").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order has not been placed").show();
        }

        lblOrderID.setText(generateNewOrderID());
//        lblId.setText("Order Id: " + orderId);
        cmbCusID.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        tblOrderList.getItems().clear();
        txtQTY.clear();
        txtDiscount.clear();
        lblCustomerTitle.setText("Mr");
        lblCustomerName.setText("Name");
        calculateAmount();*/

        /*Transaction*/
        String orId = generateNewOrderID();
        OrderDTO orders = new OrderDTO(
                orId,
                LocalDate.now(),
                cmbCusID.getValue()
        );

        ArrayList<OrderDetailsDTO> orDetails = new ArrayList<>();
        for (OrderItemListTM tm : toList
        ) {
            orDetails.add(
                    new OrderDetailsDTO(
                            orId,
                            tm.getItemCode(),
                            tm.getQty(),
                            tm.getDiscount()
                    )
            );
        }

        PurchaseOrderBO purchaseOrderBO = new PurchaseOrderBOImpl();
        purchaseOrderBO.purchaseOrder(orders, orDetails);

        lblOrderID.setText(generateNewOrderID());
        cmbCusID.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        tblOrderList.getItems().clear();
        txtQTY.clear();
        txtDiscount.clear();
        txtDescription.clear();
        txtPackSize.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        lblCustomerTitle.setText("Mr");
        lblCustomerName.setText("Name");
        calculateAmount();

    }

    private ItemDTO findItem(String itemCode) throws SQLException, ClassNotFoundException {
        return purchaseOrderBO.searchItem(itemCode);
    }

    public void addBtnOnAction(ActionEvent actionEvent) {
        if (cmbItemCode.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Select Item code").show();
            return;
        }

        if (Integer.parseInt(txtQTY.getText()) >= Integer.parseInt(txtQtyOnHand.getText())) {
            new Alert(Alert.AlertType.WARNING, "Invalid Qty..!").show();
            txtQTY.clear();
            return;
        }

        int qty = Integer.parseInt(txtQTY.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double discount = Double.parseDouble(txtDiscount.getText());
        double tot = qty * unitPrice;
        double totalOfDis = (tot - (tot * discount / 100));

        OrderItemListTM exists = isExists(cmbItemCode.getValue());
        if (exists != null) {
            orderItemListTM = tblOrderList.getSelectionModel().getSelectedItem();

            if (btnAdd.getText().equalsIgnoreCase("Update")) {
                exists.setQty(qty);
                exists.setDiscount(discount);
                exists.setTotal(totalOfDis);

            } else {
                exists.setQty(exists.getQty() + qty);
                exists.setDiscount(exists.getDiscount() + discount);
                exists.setTotal(exists.getTotal() + totalOfDis);
            }
            btnAdd.setText("Add to List");

        } else {
            toList.add(new OrderItemListTM(
                    cmbItemCode.getValue(), txtDescription.getText(), txtPackSize.getText(), unitPrice,
                    discount, qty, totalOfDis
            ));
            btnConfirmOrder.setDisable(false);
            tblOrderList.setItems(toList);
        }
        txtQTY.clear();
        txtDiscount.clear();
        calculateAmount();
        tblOrderList.refresh();
    }

    private void calculateAmount() {
        double totalAmount = 0;
        double discount = 0;

        for (OrderItemListTM itemListTM : toList) {
            totalAmount += (itemListTM.getQty() * itemListTM.getUnitPrice());
            if (itemListTM.getDiscount() != 0.0) {
                discount += totalAmount * itemListTM.getDiscount() / 100;
            }
        }
        lblTotalAmount.setText(String.valueOf(totalAmount));
        lblTotalDiscount.setText(String.valueOf(discount));
        lblTotal.setText(String.valueOf(totalAmount - discount));
    }

    private OrderItemListTM isExists(String itemCode) {
        for (OrderItemListTM itemListTM : toList) {
            if (itemListTM.getItemCode().equals(itemCode)) {
                return itemListTM;
            }
        }
        return null;
    }
}
