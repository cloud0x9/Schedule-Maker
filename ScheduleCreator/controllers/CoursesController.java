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
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;

/**
 * This class controls interactions in the Courses View.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 3/18/2020
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
    
    protected Course focusedCourse;

    protected int NUM_ROWS;
    protected int NUM_COLS;
    protected double ROW_HEIGHT;
    protected double COL_WIDTH;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadSemesters();
            NUM_ROWS = scheduleGrid.getRowConstraints().size();
            NUM_COLS = scheduleGrid.getColumnConstraints().size();
            ROW_HEIGHT = scheduleGrid.getRowConstraints().get(0).getPrefHeight() - .5;
            System.out.println("Row Height: " + ROW_HEIGHT);
            COL_WIDTH = scheduleGrid.getColumnConstraints().get(0).getPrefWidth() - .75;
            System.out.println("Column Width: " + COL_WIDTH);
            drawGrid();
        } catch (IOException ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSelectedCourse(ActionEvent _event) throws Exception {

        String selectedCourse = this.courseComboBox.getValue();
        this.courseComboBox.setValue("-");

        if (selectedCourse != null && !selectedCourse.equals("-")) {

            if (currentSemester.addCourse(selectedCourse)) {
                this.selectedCourses.getItems().add(selectedCourse);
            }
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

        if (this.currentSemester != null) {

            for (String course : this.currentSemester.getAllCourses()) {
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
                this.focusedCourse = course;
                courseSections = course.getSections();
                break;
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
        for (String semester : semesters) {
            m = p.matcher(semester);

            if (m.matches()) {
                formattedSemester = m.group(1).substring(0, 1).toUpperCase() + m.group(1).substring(1) + " " + m.group(2);
            }
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

    public void drawGrid() {

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 14; j++) {
                Pane region = new Pane();

                region.setStyle(("-fx-border-color: black; -fx-border-width: .5;"));
                scheduleGrid.add(region, i, j);
            }
        }

    }

    public void addEntry(ActionEvent _event) {
        System.out.println("Adding entry");
        int secIndex = this.sectionListView.getFocusModel().getFocusedIndex();
        Section section = this.focusedCourse.getSections().get(secIndex);
        double duration = (section.getEndTime() - section.getStartTime()) / 100;
        double entryHeight = duration  * this.ROW_HEIGHT;
        Rectangle rect = new Rectangle();
        System.out.println("Time difference: " + duration);
        System.out.println("row height: " + ROW_HEIGHT);
        System.out.println("entry height: " + entryHeight);
        rect.setHeight(entryHeight);
        rect.setWidth(this.COL_WIDTH);
        GridPane.setHalignment(rect, HPos.LEFT);
        GridPane.setValignment(rect, VPos.TOP);
        rect.setStyle("-fx-fill: lightblue");
//        rect.heightProperty().bind(scheduleGrid.heightProperty().divide(15));
        scheduleGrid.add(rect, 3, 3);
    }
}
