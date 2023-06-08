package report;

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
import util.Today;

import java.io.IOException;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 7:40 PM
 **/

public class SystemReportForm {
    public Label lblDate;
    public Label lblTime;
    public Label lblMenu;
    public AnchorPane reportContext;

    public void initialize() {
        Today.setDateAndTime(lblDate, lblTime);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), reportContext);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgIncome":
                    lblMenu.setText("Income Report");
                    break;
                case "imgMostItem":
                    lblMenu.setText("Movable Items");
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
            lblMenu.setText("ALL Reports");
        }
    }

    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            Stage stage = new Stage();

            switch (icon.getId()) {
                case "imgIncome":
                    stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/IncomeReportForm.fxml"))));
                    break;
                case "imgMostItem":
                    stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/movableItemForm.fxml"))));
                    break;
            }
            stage.show();
        }
    }

    public void backBtnOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) reportContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/administratorForm.fxml"))));
        stage.centerOnScreen();
    }

}
