package controller;

import bo.BOFactory;
import bo.custom.ItemBO;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import dto.ItemDTO;
import javafx.stage.Stage;
import util.Today;
import util.ValidateUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author : Gihan Madhusankha
 * 2022-05-31 5:08 PM
 **/

public class ManageItemFormController {
    private final ObservableList<ItemDTO> obList = FXCollections.observableArrayList();
    public TextField txtItemCode;
    public TextField txtUnitPrice;
    public TextField txtSearchField;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtPackSize;
    public JFXButton btnSave;
    public TableView<ItemDTO> tblItem;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colOperate;
    public ImageView resetBtn;
    public Label lblDate;
    public Label lblTime;
    public AnchorPane mangeItemContext;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    ItemDTO itemDTO = null;
    private final ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    public void initialize() {
        try {
            colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
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
                deleteItem(delete);
                return new ReadOnlyObjectWrapper<>(hBox);
            });

            Today.setDateAndTime(lblDate, lblTime);
            btnSave.setDisable(true);
            txtItemCode.setText(generateNewItemCode());
            txtItemCode.setEditable(false);
            loadAllItems();

            Pattern descriptionPattern = Pattern.compile("^[A-z ]{3,50}$");
            Pattern packSizePattern = Pattern.compile("^[0-9]{1,3}x[0-9]{1,3}$");
            Pattern unitPricePattern = Pattern.compile("^[0-9]{2,6}(.[0-9]{2})?$");
            Pattern qtyOnHandPattern = Pattern.compile("^[0-9]{1,5}$");

            map.put(txtDescription, descriptionPattern);
            map.put(txtPackSize, packSizePattern);
            map.put(txtUnitPrice, unitPricePattern);
            map.put(txtQtyOnHand, qtyOnHandPattern);

            searchFilter();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        resetBtn.setOnMouseClicked(event -> {
            try {
                clearForm();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void clearForm() throws SQLException, ClassNotFoundException {
        txtItemCode.setText(generateNewItemCode());
        txtDescription.clear();
        txtPackSize.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        btnSave.setDisable(true);
        btnSave.setText("Save");
        ValidateUtil.setBorders(txtDescription, txtPackSize, txtUnitPrice, txtQtyOnHand);
    }

    private void searchFilter() {
        FilteredList<ItemDTO> filteredList = new FilteredList<>(obList, b -> true);
        txtSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(searchModel -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                if (searchModel.getItemCode().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getDescription().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else return searchModel.getPackSize().toLowerCase().indexOf(searchKeyword) > -1;

            });
        });

        SortedList<ItemDTO> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tblItem.comparatorProperty());
        tblItem.setItems(sortedList);

    }

    private String generateNewItemCode() throws SQLException, ClassNotFoundException {
        txtItemCode.setEditable(false);
        return itemBO.generateNewItemCode();
    }

    private boolean deleteItem(ImageView delete) {
        /*delete item*/
        delete.setOnMouseClicked(event -> {
            itemDTO = tblItem.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure delete this item ?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)) {
                try {
                    boolean d = itemBO.deleteItem(itemDTO.getItemCode());

                    if (d) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
                        loadAllItems();
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

    private void loadAllItems() throws SQLException, ClassNotFoundException {
        /*get all*/
        obList.clear();
        ArrayList<ItemDTO> all = itemBO.getAllItems();

        for (ItemDTO dto : all) {
            obList.add(new ItemDTO(
                    dto.getItemCode(), dto.getDescription(), dto.getPackSize(), dto.getUnitPrice(), dto.getQtyOnHand()
            ));
        }
        txtItemCode.setText(generateNewItemCode());
        tblItem.setItems(obList);
    }

    private void clickedEditBtn(ImageView edit) {
        edit.setOnMouseClicked(event -> {
            itemDTO = tblItem.getSelectionModel().getSelectedItem();
            txtItemCode.setText(itemDTO.getItemCode());
            txtItemCode.setEditable(false);
            txtDescription.setText(itemDTO.getDescription());
            txtPackSize.setText(itemDTO.getPackSize());
            txtUnitPrice.setText(String.valueOf(itemDTO.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(itemDTO.getQtyOnHand()));
            btnSave.setDisable(false);
            btnSave.setText("Update");
        });
    }


    public void textField_Key_Released(KeyEvent keyEvent) {
        ValidateUtil.validate(map, btnSave);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidateUtil.validate(map, btnSave);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

    public void saveItemOnAction(ActionEvent actionEvent) {
        /*Save item*/

        String id = txtItemCode.getText();
        if (btnSave.getText().equalsIgnoreCase("Save")) {

            try {
                if (existItem(id)) {
                    new Alert(Alert.AlertType.ERROR, txtItemCode.getText() + " already exists..!").show();
                }

                boolean save = itemBO.saveItem(new ItemDTO(
                        id, txtDescription.getText(), txtPackSize.getText(), Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQtyOnHand.getText())
                ));

                if (save) {
                    loadAllItems();
                    clearForm();
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved").show();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong..!").show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            /*Update customer*/

            try {
                if (!existItem(id)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such item associated with the id " + id).show();
                }

                boolean update = itemBO.updateItem(new ItemDTO(
                        id, txtDescription.getText(), txtPackSize.getText(), Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQtyOnHand.getText())
                ));

                if (update) {
                    btnSave.setText("Save");
                    loadAllItems();
                    clearForm();
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated").show();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong...!").show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean existItem(String id) throws SQLException, ClassNotFoundException {
        return itemBO.existItem(id);
    }

    public void homeOnMouseClicked(MouseEvent event) throws IOException {
        Stage stage = (Stage) mangeItemContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/administratorForm.fxml"))));
        stage.centerOnScreen();
    }
}

