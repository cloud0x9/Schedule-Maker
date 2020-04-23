package ScheduleCreator.controllers;

/**
 * Allows user to add new semesters via drag and drop in the UI.
 *
 * @author Ilyass Sfar
 *
 * Last Updated: 4/23/2020
 */
import ScheduleCreator.Admin;
import java.io.File;
import java.io.IOException;
import java.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class UserUpdatedDBController extends Admin {

    @FXML
    void dragDrop(DragEvent event) throws IOException {
    }

    @FXML
    void dragOver(DragEvent event) {
    }
    void showError(String _popupMessage1, String _popupMessage2) {}

}
