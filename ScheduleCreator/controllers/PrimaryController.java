package ScheduleCreator.controllers;

/**
 * This class serves as the controller for the primary view
 *
 * @author Jamison Valentine
 *
 * Last Updated: 3/18/2020
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class PrimaryController implements Initializable {

    public void changeToSelectClasses(ActionEvent _event) throws Exception {

        //new FXML loader and scene for new screen
        Parent root = FXMLLoader.load(getClass().getResource("/ScheduleCreator/resources/views/select_courses.fxml"));
        Scene classViewScene = new Scene(root);

        //Get window object and refresh to show the new scene
        Stage window = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        window.setScene(classViewScene);
        window.show();
    }

    public void backToPrimary(ActionEvent _event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/ScheduleCreator/resources/views/primary.fxml"));
        Scene primaryScene = new Scene(root);

        //Get window object and refresh to show the new scene
        Stage window = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        window.setScene(primaryScene);
        window.show();
    }

    public void changeToRegistrationScreen(ActionEvent _event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/ScheduleCreator/resources/views/registration_screen.fxml"));
        Scene scene = new Scene(root);

        //Get window object and refresh to show the new scene
        Stage stage = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
