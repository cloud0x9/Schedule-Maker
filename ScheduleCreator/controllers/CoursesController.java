package ScheduleCreator.controllers;

import ScheduleCreator.Translator;
import ScheduleCreator.models.Course;
import ScheduleCreator.models.Section;
import ScheduleCreator.models.Semester;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

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
    protected ListView sectionListView;
    @FXML
    protected Button courseButton;
    @FXML
    protected Button removeCourseButton;
    @FXML
    protected Button searchButton;
    @FXML
    protected TextField searchField;
    @FXML
    protected RowConstraints topRow;
    @FXML
    protected GridPane scheduleGrid;

    protected Semester currentSemester;
    protected Semester spring2020 = new Semester("spring2020");
    protected Semester summer2020 = new Semester("summer2020");
    protected Semester fall2020 = new Semester("fall2020");


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadSemesters();
        } catch (IOException ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void displaySchedule(ActionEvent _event) {
        
        double blockHeight = scheduleGrid.getHeight() / 13;
        HBox block = new HBox();
        block.setPrefHeight(blockHeight);
        GridPane.setConstraints(block, 0, 0, 0, 2);
    }

    public void addSelectedCourse(ActionEvent _event) throws Exception {

        String selectedCourse = this.courseComboBox.getValue();
        this.courseComboBox.setValue("-");

        if (selectedCourse != null && selectedCourse != "-") {

            if (currentSemester.addCourse(selectedCourse)) this.selectedCourses.getItems().add(selectedCourse);
        }
    }

    public void switchSemester(ActionEvent _event) throws Exception {
        String currentSemesterString = semesterComboBox.getValue();
        this.courseComboBox.setValue("-");

        switch (formatSemester(currentSemesterString)) {

            case "spring2020":
                this.currentSemester = spring2020;
                break;
            case "summer2020":
                this.currentSemester = summer2020;
                break;
            case "fall2020":
                this.currentSemester = fall2020;

                break;
        }

        loadAllCourses(this.currentSemester.getName());
        loadSelectedCourses(this.currentSemester.getName());

    }

    public void clearCalendar() {
        System.out.println("Dummy function to clear the calendar for when we switch semesters");
    }

    protected void clearSectionList() {
        System.out.println("Dummy function to clear the list of available sections for when we switch semesters");
    }

    public void search(ActionEvent _event) {
        String searchString = this.searchField.getText();
        List<String> filteredList = new ArrayList();

        if (this.currentSemester.getAllCourses() != null) {

            for (String course: this.currentSemester.getAllCourses()) {
                if (course.toLowerCase().contains(searchString.toLowerCase())) {
                    filteredList.add(course);
                }
            }

        }
        this.courseComboBox.setItems(FXCollections.observableList(filteredList));
    }

    public void removeSelectedCourse(ActionEvent _event) throws Exception {
        
        Object itemToRemove = this.selectedCourses.getSelectionModel().getSelectedItem();
        this.selectedCourses.getItems().remove(itemToRemove);

        String courseToDelete = (String) itemToRemove;
        this.currentSemester.removeCourse(courseToDelete.trim());

    }

    public void loadCourseSections(ActionEvent _event) {

        List<Section> courseSections = new ArrayList();
        String currentSelection = this.selectedCourses.getFocusModel().getFocusedItem().toString();

        for (Course course : this.currentSemester.getSelectedCourses()) {
            if (course.getFullText().equals(currentSelection)) {
                courseSections = course.getSections();
            }
        }

        List<String> listCellLabels = new ArrayList();

        for (Section section : courseSections) {
            listCellLabels.add(section.toString());
        }

        this.sectionListView.setItems(FXCollections.observableList(listCellLabels));

    }

    public void loadAllCourses(String _semester) throws Exception {
        this.courseComboBox.setItems(FXCollections.observableList(this.currentSemester.getAllCourses()));
    }

    public void loadSemesters() throws IOException {
        List<String> semesters = Translator.getSemesters();

        List<String> newList = new ArrayList();
        Pattern p = Pattern.compile("([a-z]*)([0-9]{4})");
        Matcher m;

        String formattedSemester = "";
        for (String semester: semesters) {
            m = p.matcher(semester);

            if (m.matches()) formattedSemester = m.group(1).substring(0, 1).toUpperCase() + m.group(1).substring(1) + " " + m.group(2);
            newList.add(formattedSemester);
        }

        this.semesterComboBox.setItems(FXCollections.observableList(newList));
    }

    public void loadSelectedCourses(String _semester) throws Exception {
        List<String> courses = Translator.getSelectedCourses(_semester);
        this.selectedCourses.setItems(FXCollections.observableList(courses));
    }

    public String formatSemester(String _semester) {
        //Format current semester to pass as argument in appropriate Translator methods

        String[] temp = _semester.split(" ");

        String formattedSemester = temp[0].toLowerCase() + temp[1];

        return formattedSemester;
    }
}
