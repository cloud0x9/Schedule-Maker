package ScheduleCreator;

/**
 * This class is used to start and initialize the application
 *
 * @author Jamison Valentine
 *
 * Last Updated: 3/18/2020
 */

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        primaryStage.setMinHeight(560);
        primaryStage.setMinWidth(1090);

        // shows window on screen
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);

    }

}
