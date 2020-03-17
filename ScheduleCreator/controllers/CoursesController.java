package ScheduleCreator.controllers;

import ScheduleCreator.Translator;
import ScheduleCreator.models.Semester;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
    @FXML
    protected Accordion sectionContainer;

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
    
    public void refreshTitlePane(List<String> _courseNames) {
        
        this.sectionContainer.getPanes().clear();
        for (String _courseName : _courseNames) {
            TitledPane newPane = new TitledPane();
            ScrollPane scrollPane = new ScrollPane();
            VBox vbox = new VBox();
            HBox sectionTitleBox = new HBox();
            sectionTitleBox.getChildren().add(new Label("section #"));

            vbox.getChildren().addAll(sectionTitleBox);
            scrollPane.setContent(vbox);
            newPane.setContent(scrollPane);

            this.sectionContainer.getPanes().add(newPane);

            Pane pane = new Pane();
            pane.setMaxWidth(100);
            pane.getChildren().add(new Label(_courseName));
            Button btn = new Button("Remove");
            BorderPane borderPane = new BorderPane(btn);
            HBox graphicHBox = new HBox();
            HBox removeButtonBox = new HBox();
            removeButtonBox.getChildren().add(borderPane);
            graphicHBox.getChildren().addAll(pane);
            GridPane title = new GridPane();
           
            GridPane.setConstraints(pane, 0, 0);
            GridPane.setConstraints(borderPane, 1, 0);
 
            title.getChildren().addAll(pane, borderPane);
            newPane.setGraphic(title);
        }
    }

    public void addSelectedCourse(ActionEvent _event) throws Exception {

        String selectedCourse = this.courseComboBox.getValue();
        this.courseComboBox.setValue("-");

        if (selectedCourse != null && selectedCourse != "-") {

            //if course added successfully
            if (currentSemester.addCourse(selectedCourse)) {
                loadSelectedCourses(this.currentSemester.getName());
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

        loadAllCourses(currentSemesterString);
        loadSelectedCourses(this.currentSemester.getName());

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
        this.currentSemester.removeCourse(courseToDelete.trim());

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
        refreshTitlePane(courses);
    }

    public String formatSemester(String _semester) {
        //Format current semester to pass as argument in appropriate Translator methods

        String[] temp = _semester.split(" ");

        String formattedSemester = temp[0].toLowerCase() + temp[1];

        return formattedSemester;
    }
}
