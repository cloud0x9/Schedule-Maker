package ScheduleCreator;

/**
 * This class is used to start and initialize the application
 *
 * @author Jamison Valentine
 *
 * Last Updated: 2/17/2020
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    /**
     * Starts application and opens to first window/stage.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        // loads GUI components from primary.xml
        Parent root = FXMLLoader.load(getClass().getResource("resources/views/primary.fxml"));

        // adds components to Scene
        Scene primaryScene = new Scene(root);

        // set title of application window
        primaryStage.setTitle("Schedule Creator");

        // sets primary scene
        primaryStage.setScene(primaryScene);

        // shows window on screen
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);

    }

}
