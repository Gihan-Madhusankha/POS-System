package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 7:48 PM
 **/

public class UserLoginFormController {

    public TextField txtUserID;
    public AnchorPane loginContext;
    public RadioButton rBtnCashier;
    public RadioButton rBtnAdministrator;
    public PasswordField pwdPassword;

    public void initialize() {
    }

    public void loginFormOnAction(ActionEvent actionEvent) throws IOException {
        String pwd = "1234";
        String us1 = "User1";
        String us2 = "User2";
        boolean b1 = txtUserID.getText().equals(us1);
        boolean b2 = txtUserID.getText().equals(us2);

        if (rBtnCashier.isSelected() && pwdPassword.getText().equals(pwd) && b1) {
            Stage stage = (Stage) loginContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/cashierMainForm.fxml"))));
            stage.centerOnScreen();

        } else if (rBtnAdministrator.isSelected() && pwdPassword.getText().equals(pwd) && b2) {
            Stage stage = (Stage) loginContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/administratorForm.fxml"))));
            stage.centerOnScreen();


        } else {
            new Alert(Alert.AlertType.ERROR, "UserName or Password is wrong..!").show();
        }
    }

}
