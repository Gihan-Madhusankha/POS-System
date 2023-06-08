package util;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 10:43 PM
 **/

public class Today {
    public static void setDateAndTime(Label date, Label time) {
        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        time.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
