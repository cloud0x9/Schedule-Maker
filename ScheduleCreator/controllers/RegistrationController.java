package ScheduleCreator.controllers;

/**
 * Controller for Registration
 *
 * @author Ilyass Sfar, Jamison Valentine
 *
 * Last Updated: 4/8/2020
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class RegistrationController {

    @FXML
    private WebView webview;

    private WebEngine engine;

    //Button to take user back to the main screen
    @FXML
    void backToPrimary(ActionEvent _event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ScheduleCreator/resources/views/select_courses.fxml"));
            Scene primaryScene = new Scene(root);

            //Get window object and refresh to show the new scene
            Stage window = (Stage) ((Node) _event.getSource()).getScene().getWindow();
            window.setScene(primaryScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //loads the uncg geine webpage as soon as the user acesses the registration screen
    public void initialize() {
        engine = webview.getEngine();
        engine.load("https://ssb.uncg.edu/");

    }

    //Forward and Backwards buttons based on https://stackoverflow.com/questions/18928333
    //Button to go back to pervious web page
    @FXML
    void goBackWeb(ActionEvent event) {
        final WebHistory history = engine.getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();
        Platform.runLater(new Runnable() {
            public void run() {
                history.go(-1);
            }
        });
        engine.load(entryList.get(currentIndex > 0 ? currentIndex - 1 : currentIndex).getUrl());
    }

    //Button to go forward to pervious web page
    @FXML
    void goForwardWeb(ActionEvent event) {
        final WebHistory history = engine.getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();
        Platform.runLater(new Runnable() {
            public void run() {
                history.go(1);
            }
        });
        engine.load(entryList.get(currentIndex < entryList.size() - 1 ? currentIndex + 1 : currentIndex).getUrl());

    }

    //Button to reload current web page
    @FXML
    void reloadWeb(ActionEvent event) {
        engine.reload();
    }

}
