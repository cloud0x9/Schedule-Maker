package ScheduleCreator.controllers;

/**
 * This class serves as the controller for the primary view
 *
 * @author Jamison Valentine
 *
 * Last Updated: 3/18/2020
 */
import com.sun.javafx.css.StyleManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;

public class PrimaryController implements Initializable {

    @FXML
    private ToggleButton darkmode;

    public void changeToSelectClasses(ActionEvent _event) throws Exception {

        //new FXML loader and scene for new screen
        Parent root = FXMLLoader.load(getClass().getResource("/ScheduleCreator/resources/views/select_courses.fxml"));
        Scene classViewScene = new Scene(root);

        //Get window object and refresh to show the new scene
        Stage window = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        window.setScene(classViewScene);
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

    //toggle and untoggle darkmode css to defult theme from button
    @FXML
    void toggleDarkMode(ActionEvent event) {
        if (darkmode.isSelected()) {
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            StyleManager.getInstance().addUserAgentStylesheet("ScheduleCreator/resources/Darkmode.css");
        } else {
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            StyleManager.getInstance().addUserAgentStylesheet(null);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
