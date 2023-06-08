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
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 5:53 PM
 **/

public class AdministratorFormController {

    public ImageView imgItem;
    public ImageView imgReport;
    public Label lblDescription;
    public Label lblMenu;
    public AnchorPane administratorContext;

    public void initialize() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), administratorContext);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgItem":
                    lblMenu.setText("Manage Item");
                    lblDescription.setText("Click to add, edit, delete, search or view Items");
                    break;
                case "imgReport":
                    lblMenu.setText("Reports");
                    lblDescription.setText("Click if you want to check system reports");
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
            Stage stage = (Stage) administratorContext.getScene().getWindow();

            switch (icon.getId()) {
                case "imgItem":
                    stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/manageItemForm.fxml"))));
                    break;
                case "imgReport":
                    stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/systemReportForm.fxml"))));
                    break;
            }
            stage.centerOnScreen();

        }
    }

    public void backBtnOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) administratorContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/userLoginForm.fxml"))));
        stage.centerOnScreen();
    }
}
