package ScheduleCreator.controllers;

import ScheduleCreator.Translator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

/**
 * This class controls interactions in the Courses View.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 3/16/2020
 */
public class CoursesController implements Initializable {

    @FXML
    protected Button semesterButton;
    @FXML
    protected ComboBox<String> semesterComboBox;
    @FXML
    protected ComboBox<String> courseComboBox;
    @FXML
    protected ListView selectedCourses;
    @FXML
    protected Button courseButton;
    @FXML
    protected Button removeCourseButton;
    protected String currentSemester;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadSemesters();
        } catch (IOException ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSelectedCourse(ActionEvent _event) throws Exception {
        String selectedCourse = this.courseComboBox.getValue();

        this.selectedCourses.getItems().add(selectedCourse);
        Translator.saveCourse(selectedCourse, formatSemester(this.currentSemester));
    }

    public void switchSemester(ActionEvent _event) throws Exception {
        this.currentSemester = semesterComboBox.getValue();
        clearCalendar();
        clearSectionList();

        loadAllCourses(this.currentSemester);
        loadSelectedCourses(formatSemester(this.currentSemester));

    }

    public void clearCalendar() {
        System.out.println("Dummy function to clear the calendar for when we switch semesters");
    }

    protected void clearSectionList() {
        System.out.println("Dummy function to clear the list of available sections for when we switch semesters");
    }

    public void removeSelectedCourse(ActionEvent _event) throws Exception {
        Object itemToRemove = this.selectedCourses.getSelectionModel().getSelectedItem();
        this.selectedCourses.getItems().remove(itemToRemove);

        String courseToDelete = (String) itemToRemove;
        Translator.removeCourse(courseToDelete);
    }

    public void loadAllCourses(String _semester) throws Exception {

        List<String> courses = Translator.getCourses(_semester);
        this.courseComboBox.setItems(FXCollections.observableList(courses));
    }

    public void loadSemesters() throws IOException {
        List<String> semesters = Translator.getSemesters();
        this.semesterComboBox.setItems(FXCollections.observableList(semesters));
    }

    public void loadSelectedCourses(String _semester) throws Exception {
        List<String> courses = Translator.getSelectedCourses(_semester);
        this.selectedCourses.setItems(FXCollections.observableList(courses));
    }

    public String formatSemester(String _semester) {
        //Format current semester to pass as argument in appropriate Translator methods
        String[] temp = this.currentSemester.split(" ");
        String formattedSemester = temp[0].toLowerCase() + temp[1];

        return formattedSemester;
    }
}
