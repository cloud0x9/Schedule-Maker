package ScheduleCreator.controllers;

/**
 *
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

public class AddToDatabaseController extends Admin {

    //used to test if a file actaully contains course infromation of not before proceeding
    Pattern courseData = Pattern.compile("(.+?(?= - (?:[0-9]{5}))[ ])|([ ][0-9]{5}[ ])|([ ]\\b[A-Z]{3}\\b.\\b[0-9]{3}\\b.+ [0-9]{2}\\b)|([\\t](?:(?:(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\\\b).-.(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b))|(\\b([\\t](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA.*TBA\\b)))\\b)|(\\t.*([0-9]{3}))(?=\\t[A-z]{3} [0-9]{2})|((?=(\\tLecture|\\tLab|\\tIndividual Study|\\tSeminar|\\tClinical|Colloquia|\\tDissertation or Thesis|\\tEnsemble|\\tInternship, Field Exp, Coop Ed|\\tLecture and lab|\\tPerformance|\\tPhysical Activity|\\tPracticum - Dlvrd Ind Setting|\\tPracticum - Dlvrd Org Course|\\tRecitations|\\tStudent Teaching|\\tStudio|\\tLecture and Lab|\\tDissertation or Thesis)).+?(?<=(((\\(P\\)E-mail)|(\\(P\\))|(TBA)))))");
    //used to test if the file name is inline with the naming convetions used by the db
    Pattern fileName = Pattern.compile("(fall|spring|winter|summer)([0-9]{4}).*?");

    /**
     * When a file is dropped on the subscene, the file is moved to the right
     * place in this programs file hierarchy and the database is
     * regenerated(which allows user to add new semesters to db).
     *
     * @param event File Dropped onto subscene.
     */
    @FXML
    void dragDrop(DragEvent event) throws IOException {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            String filePath = null;
            //for loop allows for mutliple files to be dragged and dropped at once
            for (File file : db.getFiles()) {
                filePath = file.getAbsolutePath();
                String filename = file.getName();
                //get rid of the file extension if present, it messes with the db if its not removed.
                if (file.getName().contains(".")) {
                    filename = filename.substring(0, filename.lastIndexOf('.'));
                }
                String filenametest = file.getName();
                Matcher filenametester = fileName.matcher(filenametest);
                //check if the file name is inline with the database uses
                if (filenametester.find()) {
                    String content = Admin.getFullText(filePath);
                    Matcher matcher = courseData.matcher(content);
                    //check if file atcaully contains course information of not before doing anything
                    if (matcher.find()) {
                        File sourceFile = new File(filePath);
                        //move files from where it originally was to the right place in this programs file hierarchy.
                        File destFile = new File("." + File.separator + "src" + File.separator + "ScheduleCreator" + File.separator + "resources" + File.separator + "raw" + File.separator + filename);
                        //if files was scuessfuly moved into the right place in the program files then its parsed and added to the db
                        if (sourceFile.renameTo(destFile)) {
                            Admin.regenDB();
                            System.out.println("Sucess: file moved and was added to the database ");
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                            alert.setTitle("Success");
                            alert.setHeaderText("Success");
                            alert.setContentText("Please go back to the schedule builder!");
                            alert.showAndWait();
                        } else {
                            showError("File Not Moved", "The file was not scessfully moved, try again with a diffrent file!");
                            System.out.println("Failed: file was not moved");
                        }
                    } else {
                        showError("Invalid File Content", "The file does not contain valid course information, try again with a diffrent file!");
                        System.out.println("the file does not contain valid course information");
                    }
                } else {
                    showError("Invalid File Name", "Rename the file and try again.\nExample of valid file name...\n\"fall2020\",\"spring3020\",\"summer2015\"...etc");
                    System.out.println("file name is not valid for the database");

                }
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }

    /**
     *
     * @param event drag on top of subscene.
     */
    @FXML
    void dragOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }

    //used to show error popupboxes, since they will be used a lot in this class
    void showError(String _popupMessage1, String _popupMessage2) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
        alert.setTitle(_popupMessage1);
        alert.setHeaderText(_popupMessage1);
        alert.setContentText(_popupMessage2);
        alert.showAndWait();
    }
}
