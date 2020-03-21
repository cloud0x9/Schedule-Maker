package ScheduleCreator.controllers;

import ScheduleCreator.Translator;
import ScheduleCreator.models.Course;
import ScheduleCreator.models.Schedule;
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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * This class controls interactions in the Courses View.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 3/21/2020
 */
public class CoursesController implements Initializable {

    @FXML
    protected ComboBox<String> semesterComboBox;
    @FXML
    protected ListView availableCourses;
    @FXML
    protected ListView selectedCourses;
    @FXML
    protected ListView sectionListView;
    @FXML
    protected Button courseButton;
    @FXML
    protected Button removeCourseButton;
    @FXML
    protected TextField searchField;
    @FXML
    protected GridPane scheduleGrid;
    @FXML
    protected Label scheduleLabel;

    // list of courses for current semester
    FilteredList<String> courseList;

    //ObservableList<String> courseList = FXCollections.observableArrayList();
    protected Semester currentSemester;
    protected Semester spring2020 = new Semester("spring2020");
    protected Semester summer2020 = new Semester("summer2020");
    protected Semester fall2020 = new Semester("fall2020");

    protected Course focusedCourse;

    protected int NUM_ROWS;
    protected int NUM_COLS;
    protected double ROW_HEIGHT;
    protected double COL_WIDTH;
    protected int currentScheduleIndex;

    BorderPane[][] grid;
    List<BorderPane> entries = new ArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadSemesters();
            NUM_ROWS = scheduleGrid.getRowConstraints().size();
            NUM_COLS = scheduleGrid.getColumnConstraints().size();
            ROW_HEIGHT = scheduleGrid.getRowConstraints().get(0).getPrefHeight();
            COL_WIDTH = scheduleGrid.getColumnConstraints().get(0).getPrefWidth();
            grid = new BorderPane[NUM_ROWS][NUM_COLS];
            drawGrid();
        } catch (IOException ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addSelectedCourse(ActionEvent _event) throws Exception {

        if (this.availableCourses.getFocusModel().getFocusedItem() != null) {
            String selectedCourse = this.availableCourses.getFocusModel().getFocusedItem().toString();
            if (this.currentSemester.addCourse(selectedCourse)) {
                this.selectedCourses.getItems().add(selectedCourse);
                this.currentSemester.generateSchedules();
            }
            regenerateSchedules();
        }
    }

    public void switchSemester(ActionEvent _event) throws Exception {
        String currentSemesterString = semesterComboBox.getValue();

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

        if (this.currentSemester.getSelectedCourses().size() > 0) {
            loadSchedule(this.currentSemester.getSchedules().get(0));
        }

    }

    public void clearCalendar() {
        for (BorderPane entry : entries) {
            scheduleGrid.getChildren().remove(entry);
        }
    }

    protected void clearSectionList() {
        System.out.println("Dummy function to clear the list of available sections for when we switch semesters");
    }

    // TODO: connect "delete" while in the selectedCourses ListView to this method and
    // allow for selecting and deleting multiple courses
    public void removeSelectedCourse(ActionEvent _event) throws Exception {
        if (this.focusedCourse != null) {
            Object itemToRemove = this.selectedCourses.getSelectionModel().getSelectedItem();
            this.selectedCourses.getItems().remove(itemToRemove);

            String courseToDelete = ((String) itemToRemove).trim();
            this.currentSemester.removeCourse(courseToDelete);

            this.currentSemester.generateSchedules();
            regenerateSchedules();
        }

    }

    public void regenerateSchedules() {
        this.currentSemester.generateSchedules();
        clearCalendar();

        if (this.currentSemester.getNumberOfSchedules() == 0) {
            scheduleLabel.setText("0/0");
        } else if (this.currentSemester.getNumberOfSchedules() > 0) {
            loadSchedule(this.currentSemester.getSchedules().get(0));
            scheduleLabel.setText("1/" + this.currentSemester.getNumberOfSchedules());
        }

    }

    public void loadCourseSections(ActionEvent _event) {

        List<Section> courseSections = new ArrayList();

        if (this.selectedCourses.getFocusModel().getFocusedItem() != null) {
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

    }

    public void loadAllCourses(String _semester) throws Exception {

        // intermediary ObservableList of the courses
        ObservableList<String> OList = FXCollections.observableList(this.currentSemester.getAllCourses());

        // create FilteredList that we'll actually use
        this.courseList = new FilteredList<>(OList, s -> true);

        // connect availableCourses ListView to the courseList
        this.availableCourses.setItems(this.courseList);

        // TODO: make up and down arrow on the keyboard scroll the search results
        /*      searchField.setOnKeyPressed(new javafx.event.EventHandler<KeyEvent>() {
                public void handle(KeyEvent event) {
                    int i = 0;
                    switch (event.getCode()) {
                        case UP:
                            i = 1;
                            break;
                        case DOWN:
                            i = -1;
                            break;
                    }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    availableCourses.getFocusModel().focus(availableCourses.getSelectionModel().getSelectedIndex() + 1);
                }
                });
                }
                });
         */
        // Connect search bar filtering to the courseList FilteredList (this uses lambdas, it's adapted from
        // https://stackoverflow.com/questions/28448851/how-to-use-javafx-filteredlist-in-a-listview
        // and https://stackoverflow.com/questions/45045631/filter-items-within-listview-in-javafx )
        searchField.textProperty().addListener(obs -> {

            // select the top entry whenever the search term changes, but use Platform.runLater()
            // so that JavaFX doesn't try to update the selection while it's still building the ListView.
            // See https://stackoverflow.com/questions/11088612/javafx-select-item-in-listview for some context
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // Note: we can't use "this" keyword here
                    availableCourses.getSelectionModel().select(0);
                    availableCourses.getFocusModel().focus(0);
                }
            });

            String filter = searchField.getText().toLowerCase();
            // when there's nothing entered yet
            if (filter == null || filter.length() == 0) {
                // show all courses
                this.courseList.setPredicate(s -> true);
                // otherwise
            } else {
                // filter based on the contents of the search bar
                this.courseList.setPredicate(s -> s.toLowerCase().contains(filter));
            }
        });

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
        regenerateSchedules();

    }

    public String formatSemester(String _semester) {
        //Format current semester to pass as argument in appropriate Translator methods

        String[] temp = _semester.split(" ");

        String formattedSemester = temp[0].toLowerCase() + temp[1];

        return formattedSemester;
    }

    public void drawGrid() {

        for (int i = 1; i <= NUM_ROWS - 1; i++) {
            for (int j = 1; j <= NUM_COLS - 1; j++) {
                BorderPane region = new BorderPane();
                region.setStyle(("-fx-border-color: black; -fx-border-width: .5;"));
                grid[i][j] = region;
                scheduleGrid.add(region, j, i);

            }
        }

    }

    public void addSection(ActionEvent _event) {
        if (this.focusedCourse != null) {
            int secIndex = this.sectionListView.getFocusModel().getFocusedIndex();
            Section focusedSection = this.focusedCourse.getSections().get(secIndex);
            this.currentSemester.addSelectedSection(focusedCourse, focusedSection);
            this.currentSemester.generateSchedules();
            loadSchedule(this.currentSemester.getSchedules().get(0));
        }
    }

    public void addEntry(Section _section) {

        char[] daysString = _section.getDays().toCharArray();
        ArrayList<Integer> days = new ArrayList();
        for (char day : daysString) {
            switch (day) {
                case 'T':
                    days.add(2);
                    break;
                case 'M':
                    days.add(1);
                    break;
                case 'W':
                    days.add(3);
                    break;
                case 'R':
                    days.add(4);
                    break;
                case 'F':
                    days.add(5);
                    break;

            }

            int row = (int) _section.getStartTime() / 100 - 7;
            double topMargin = (_section.getStartTime() % 100 ) / 60;
            for (Integer col : days) {
                Label label = new Label(_section.getCourseID() + " - " + _section.getSectionNumber());
                BorderPane entryContainer = new BorderPane();
                entryContainer.paddingProperty().set(new Insets(grid[row][col].heightProperty().multiply(topMargin).doubleValue(), 0, 0, 0));
                StackPane pane = new StackPane();

                Rectangle rect = new Rectangle();
                rect.setStyle("-fx-fill:lightblue;");
                label.setAlignment(Pos.CENTER);

                pane.setStyle("-fx-border-color:blue;");
                pane.getChildren().addAll(rect, label);
                entryContainer.setTop(pane);

                scheduleGrid.getChildren().add(entryContainer);
                GridPane.setConstraints(entryContainer, col, row, 1, GridPane.REMAINING, HPos.CENTER, VPos.TOP);
                BorderPane region = grid[row][col];
                rect.heightProperty().bind(region.heightProperty().subtract(2).multiply(_section.getDurationHours()));
                rect.widthProperty().bind(region.widthProperty().subtract(2));
                entries.add(entryContainer);

            }

        }
    }

    public void loadSchedule(Schedule _schedule) {
        clearCalendar();
        for (Section section : _schedule.getAddedSections()) {
            addEntry(section);
        }
        scheduleLabel.setText(this.currentScheduleIndex + 1 + "/" + this.currentSemester.getNumberOfSchedules());
    }

    public void loadNextSchedule(ActionEvent _event) {

        if (this.currentSemester != null) {
            if (this.currentScheduleIndex < this.currentSemester.getSchedules().size() - 1) {
                this.currentScheduleIndex++;
                loadSchedule(this.currentSemester.getSchedules().get(this.currentScheduleIndex));
            }
        }
    }

    public void loadPrevSchedule(ActionEvent _event) {

        if (this.currentScheduleIndex > 0) {
            this.currentScheduleIndex--;
            loadSchedule(this.currentSemester.getSchedules().get(this.currentScheduleIndex));
        }
    }
}
