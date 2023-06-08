package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import dto.CustomerDTO;
import util.Today;
import util.ValidateUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author : Gihan Madhusankha
 * 2022-05-30 2:39 PM
 **/

public class ManageCustomerFormController {

    private final ObservableList<CustomerDTO> obList = FXCollections.observableArrayList();
    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    public JFXButton btnSaveCustomer;
    public TextField txtCusId;
    public JFXComboBox<String> cmbCusTitle;
    public JFXComboBox<String> cmbCusProvince;
    public TextField txtSearchField;
    public TextField txtCusAddress;
    public TextField txtCusPostalCode;
    public TextField txtCusName;
    public TextField txtCusCity;
    public ImageView resetBtn;
    public TableView<CustomerDTO> tblCustomer;
    public TableColumn colCusID;
    public TableColumn colCusTitle;
    public TableColumn colCusName;
    public TableColumn colCusAddress;
    public TableColumn colCusCity;
    public TableColumn colCusProvince;
    public TableColumn colPostalCode;
    public TableColumn colOperate;
    public Label lblDate;
    public Label lblTime;
    CustomerDTO customerDTO = null;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        colCusID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCusAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCusCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colCusProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
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
            btnSaveCustomer.setDisable(true);
            txtCusId.setText(generateNewCustomerID());
            txtCusId.setEditable(false);
            loadCustomerTitles();
            loadAllProvinces();
            loadAllCustomers();

            Pattern namePattern = Pattern.compile("^[A-z ]{3,30}$");
            Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/.]{5,30}$");
            Pattern cityPattern = Pattern.compile("^[A-z]{3,20}$");
            Pattern postalCodePattern = Pattern.compile("^[0-9]{2,9}$");

            map.put(txtCusName, namePattern);
            map.put(txtCusAddress, addressPattern);
            map.put(txtCusCity, cityPattern);
            map.put(txtCusPostalCode, postalCodePattern);

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

    private void searchFilter() {
        FilteredList<CustomerDTO> filteredList = new FilteredList<>(obList, b -> true);
        txtSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(searchModel -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                if (searchModel.getId().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getTitle().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getAddress().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getCity().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getProvince().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else return searchModel.getPostalCode().toLowerCase().indexOf(searchKeyword) > -1;
            });
        });

        SortedList<CustomerDTO> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tblCustomer.comparatorProperty());
        tblCustomer.setItems(sortedList);

    }

    private boolean deleteCustomer(ImageView delete) {
        /*delete customer*/
        delete.setOnMouseClicked(event -> {
            customerDTO = tblCustomer.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure delete this customer ?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)) {
                try {
                    boolean exist = customerBO.existCustomer(customerDTO.getId());

                    if (exist) {
                        boolean b = customerBO.deleteCustomer(customerDTO.getId());
                        if (b) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
                            loadAllCustomers();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Something else").show();
                        }
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
            customerDTO = tblCustomer.getSelectionModel().getSelectedItem();
            txtCusId.setText(customerDTO.getId());
            txtCusId.setEditable(false);
            cmbCusTitle.setValue(customerDTO.getTitle());
            txtCusName.setText(customerDTO.getName());
            txtCusAddress.setText(customerDTO.getAddress());
            txtCusCity.setText(customerDTO.getCity());
            cmbCusProvince.setValue(customerDTO.getProvince());
            txtCusPostalCode.setText(customerDTO.getPostalCode());
            btnSaveCustomer.setDisable(false);
            btnSaveCustomer.setText("Update");
        });
    }

    private String generateNewCustomerID() throws SQLException, ClassNotFoundException {
        txtCusId.setEditable(false);
        return customerBO.generateNewCustomerID();

    }

    private void loadAllProvinces() {
        ObservableList<String> provinceList = FXCollections.observableArrayList();
        provinceList.add("Western");
        provinceList.add("Eastern");
        provinceList.add("Central");
        provinceList.add("Northern");
        provinceList.add("Southern");
        provinceList.add("Sabaragamuwa");
        provinceList.add("North Western");
        provinceList.add("Uva");
        provinceList.add("North Central");
        cmbCusProvince.setItems(provinceList);
    }

    private void loadCustomerTitles() {
        ObservableList<String> cusTList = FXCollections.observableArrayList();
        cusTList.add("Mr");
        cusTList.add("Mrs");
        cusTList.add("Miss");
        cusTList.add("Dr");
        cmbCusTitle.setItems(cusTList);
    }

    private void loadAllCustomers() throws SQLException, ClassNotFoundException {
        /*get all*/

        obList.clear();
        txtCusId.setText(generateNewCustomerID());
        ArrayList<CustomerDTO> all = customerBO.getAllCustomers();
        for (CustomerDTO dto : all) {
            obList.add(new CustomerDTO(
                    dto.getId(), dto.getTitle(), dto.getName(), dto.getAddress(), dto.getCity(), dto.getProvince(), dto.getPostalCode()
            ));
        }
        tblCustomer.setItems(obList);


    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        /*Save customer*/

        String id = txtCusId.getText();
        if (btnSaveCustomer.getText().equalsIgnoreCase("Save")) {

            try {
                if (existCustomer(id)) {
                    new Alert(Alert.AlertType.ERROR, txtCusId.getText() + " already exists..!").show();
                }

                boolean save = customerBO.saveCustomer(new CustomerDTO(
                        txtCusId.getText(), cmbCusTitle.getValue(), txtCusName.getText(), txtCusAddress.getText(), txtCusCity.getText(), cmbCusProvince.getValue(), txtCusPostalCode.getText()
                ));

                loadAllCustomers();
                clearForm();

                if (save) {
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
                if (!existCustomer(id)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
                }


                boolean update = customerBO.updateCustomer(new CustomerDTO(
                        id, cmbCusTitle.getValue(), txtCusName.getText(), txtCusAddress.getText(), txtCusCity.getText(), cmbCusProvince.getValue(), txtCusPostalCode.getText()
                ));

                btnSaveCustomer.setText("Save");
                loadAllCustomers();
                clearForm();

                if (update) {
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

    private boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerBO.existCustomer(id);
    }

    private void clearForm() throws SQLException, ClassNotFoundException {
        txtCusId.setText(generateNewCustomerID());
        cmbCusTitle.getSelectionModel().clearSelection();
        txtCusName.clear();
        txtCusAddress.clear();
        txtCusCity.clear();
        cmbCusProvince.getSelectionModel().clearSelection();
        txtCusPostalCode.clear();
        btnSaveCustomer.setDisable(true);
        btnSaveCustomer.setText("Save");
        ValidateUtil.setBorders(txtCusName, txtCusAddress, txtCusCity, txtCusPostalCode);
    }

    public void textField_Key_Released(KeyEvent keyEvent) {
        ValidateUtil.validate(map, btnSaveCustomer);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidateUtil.validate(map, btnSaveCustomer);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

}
