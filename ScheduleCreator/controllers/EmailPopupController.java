package ScheduleCreator.controllers;

/**
 * Controller for email api popup
 *
 * @author Ilyass Sfar
 *
 * Last Updated: 4/12/2020
 */
import ScheduleCreator.API.EmailAdapter;
import ScheduleCreator.models.Section;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EmailPopupController {

    @FXML
    private TextField emailTF;

    @FXML
    private Button sendBtn;

    //Calls email api
    @FXML
    public void sendEmail(ActionEvent event) throws MailjetException, MailjetSocketTimeoutException {
        if (EmailAdapter.validate(emailTF.getText())) {
            EmailAdapter testAPI = new EmailAdapter();
            testAPI.sendEmail(emailTF.getText(), EmailPopupController.getCRNS());
            Stage stage = (Stage) sendBtn.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
            alert.setTitle("Invalid Email");
            alert.setHeaderText("Invalid Email");
            alert.setContentText("The given email " + "\"" + emailTF.getText() + "\"" + " is NOT valid.");
            alert.showAndWait();
        }
    }

    public static String getCRNS() {
        //Check if the user has choosen any courses when pressing the email button, if not a error popup is shown notifying the user
        if (CoursesController.currentSemester == null || CoursesController.currentSemester.getSelectedCourses().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You have not selected a semseter or any courses", ButtonType.OK);
            alert.showAndWait();
        }
        //If the user has selected some course then the crns for the coruses are rietrived.
        StringBuilder content = new StringBuilder();
        for (Section section : CoursesController.currentSemester.getSchedules().get(CoursesController.currentScheduleIndex).getAddedSections()) {
            content.append(section.getID() + "-" + section.getSectionNumber() + " | " + section.getCRN() + "\n");
        }
        return content.toString();
    }

}
