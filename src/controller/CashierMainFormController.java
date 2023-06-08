package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author : Gihan Madhusankha
 * 2022-05-30 11:52 AM
 **/

public class CashierMainFormController {

    public AnchorPane cashierContext;
    public Label lblMenu;
    public Label lblDescription;
    public ImageView imgCustomer;
    public ImageView imgOrder;
    public ImageView imgPlaceOrder;

    public void initialize() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), cashierContext);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgCustomer":
                    lblMenu.setText("Manage Customer");
                    lblDescription.setText("Click to add, edit, delete, search or view customers");
                    break;
                case "imgOrder":
                    lblMenu.setText("Manage Order");
                    lblDescription.setText("Click if you want to manage orders");
                    break;
                case "imgPlaceOrder":
                    lblMenu.setText("Place Order");
                    lblDescription.setText("Click here if you want to place a new order");
                    break;
            }

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.3);
            scaleT.setToY(1.3);
            scaleT.play();

//            DropShadow glow = new DropShadow();
//            glow.setColor(Color.CORNFLOWERBLUE);
//            glow.setWidth(10);
//            glow.setHeight(10);
//            glow.setRadius(10);
//            icon.setEffect(glow);

        }
    }

    public void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), icon);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();

            icon.setEffect(null);
            lblMenu.setText("WELCOME");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }

    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            Stage stage = new Stage();

            switch (icon.getId()) {
                case "imgCustomer":
                    stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/manageCustomerForm.fxml"))));
                    break;
                case "imgOrder":
                    stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/manageOrderForm.fxml"))));
                    break;
                case "imgPlaceOrder":
                    stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/placeOrderForm.fxml"))));
                    break;
            }
            stage.show();

        }
    }

    public void backBtnOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cashierContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/userLoginForm.fxml"))));
        stage.centerOnScreen();
    }
}
