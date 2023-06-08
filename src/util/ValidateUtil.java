package util;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * @author : Gihan Madhusankha
 * 2022-06-01 1:27 PM
 **/

public class ValidateUtil {

    public static Object validate(LinkedHashMap<TextField, Pattern> map, JFXButton btnSaveCustomer) {
        for (TextField textField : map.keySet()) {
            Pattern pattern = map.get(textField);

            if (!pattern.matcher(textField.getText()).matches()) {
                addError(textField, btnSaveCustomer);
                return textField;
            } else {
                removeError(textField, btnSaveCustomer);
            }
        }
        btnSaveCustomer.setDisable(false);
        return true;
    }

    private static void removeError(TextField textField, JFXButton btnSaveCustomer) {
        textField.getParent().setStyle("-fx-border-color: GREEN");
    }

    private static void addError(TextField textField, JFXButton btnSaveCustomer) {
        if (textField.getText().length() > 0) {
            textField.getParent().setStyle("-fx-border-color: RED");
        }
        btnSaveCustomer.setDisable(true);
    }

    public static void setBorders(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.getParent().setStyle("-fx-border-color: rgba(76, 73, 73, 0.83)");
        }
    }
}
