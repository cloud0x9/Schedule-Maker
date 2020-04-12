package ScheduleCreator.controllers;

/**
 * This class controls interactions in the Courses View.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 4/12/2020
 */
import ScheduleCreator.Adapter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ScheduleCreator.Translator;
import ScheduleCreator.models.Course;
import ScheduleCreator.models.Schedule;
import ScheduleCreator.models.Section;
import ScheduleCreator.models.Semester;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CoursesController implements Initializable {

    @FXML
    protected ComboBox<String> semesterComboBox;
    @FXML
    protected ListView availableCourses;
    @FXML
    protected ListView selectedCoursesListView;
    @FXML
    protected ListView sectionListView;
    @FXML
    protected Button courseButton;
    @FXML
    protected Button removeCourseButton;
    @FXML
    protected TextField searchField;
    @FXML
    protected GridPane scheduleGridPane;
    @FXML
    protected Label scheduleLabel, onlineClassesLabel;
    @FXML
    protected TabPane sectionTabPane;
    @FXML
    protected VBox CRNContainer, CRNPane;

    // List of courses for current semester.
    FilteredList<String> courseList;

    protected static Semester currentSemester;
    protected Course focusedCourse;
    protected Course currentCourse;
    protected Adapter adapter = new Adapter();

    protected int NUM_ROWS;
    protected int NUM_COLS;
    protected static int currentScheduleIndex;

    BorderPane[][] grid;
    List<BorderPane> entries = new ArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.loadSemesters();
            NUM_ROWS = scheduleGridPane.getRowConstraints().size();
            NUM_COLS = scheduleGridPane.getColumnConstraints().size();
            grid = new BorderPane[NUM_ROWS][NUM_COLS];
            this.drawGrid();
            this.CRNPane.toFront();
        } catch (Exception ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Called when "Add Course" button is clicked; Add selected course to the
     * listview and saves in database.
     *
     * @param _event
     */
    public void addSelectedCourse(ActionEvent _event) {

        if (this.availableCourses.getFocusModel().getFocusedItem() != null) {
            String selectedCourse = this.availableCourses.getFocusModel().getFocusedItem().toString();
            Course newCourse = new Course(selectedCourse, this.currentSemester.getName());
            if (this.currentSemester.addCourse(newCourse)) {
                this.selectedCoursesListView.getItems().add(selectedCourse);
                this.currentSemester.generateSchedules();
                this.createNewTab(newCourse);
            }
            this.regenerateSchedules();
        }
    }

    public void setSections(ActionEvent _event) {

        Course course;
        Tab tab;
        for (int i = 0; i < this.sectionTabPane.getTabs().size(); i++) {
            tab = this.sectionTabPane.getTabs().get(i);
            course = this.currentSemester.getSelectedCourses().get(i);
            List<Section> selected = new ArrayList();
            VBox container = (VBox) ((ScrollPane) tab.getContent()).getContent();
            for (int j = 0; j < container.getChildren().size(); j++) {
                VBox entry = (VBox) container.getChildren().get(j);
                CheckBox checkBox = (CheckBox) entry.getChildren().get(0);
                if (checkBox.isSelected()) {
                    selected.add(course.getSections().get(j));
                }
            }
            this.currentSemester.getSelectedSections().put(course, selected);
        }
        this.regenerateSchedules();
    }

    /**
     * Called when a different semester is chosen and loads appropriate info.
     *
     * @param _event
     */
    public void switchSemester(ActionEvent _event) {

        String currentSemesterString = semesterComboBox.getValue();
        this.currentSemester = new Semester(semesterDirName(currentSemesterString));

        this.loadAllCourses();
        this.loadSelectedCourses();
        // loadSelectedSections();

        // Renders the first generated schedule if there is at least 1 selected course.
        if (this.currentSemester.getSelectedCourses().size() > 0) {
            this.loadSchedule(this.currentSemester.getSchedules().get(0));
        }
    }

    /**
     * Formats given string to appropriate semester directory name
     *
     * @param _semester
     * @return
     */
    public String semesterDirName(String _semester) {

        String[] temp = _semester.split(" ");
        String formattedSemester = temp[0].toLowerCase() + temp[1];
        return formattedSemester;
    }

    /**
     * Clears the current displayed schedule.
     */
    public void clearScheduleGrid() {
        for (BorderPane entry : this.entries) {
            scheduleGridPane.getChildren().remove(entry);
        }
        this.entries.clear();
    }

    /**
     * Removes the selected course from the listview and the database.
     *
     * @param _event
     */
    // TODO: connect "delete" while in the selectedCourses ListView to this method
    public void removeSelectedCourse(ActionEvent _event) {

        if (this.selectedCoursesListView.getSelectionModel().getSelectedItem() != null) {
            int index = this.selectedCoursesListView.getSelectionModel().getSelectedIndex();
            this.sectionTabPane.getTabs().remove(index);
            String courseToRemove = ((String) this.selectedCoursesListView.getSelectionModel().getSelectedItem()).trim();
            this.selectedCoursesListView.getItems().remove(courseToRemove);
            this.currentSemester.removeCourse(courseToRemove);

            // Clears the section listview if the focus is on the course to be removed.
            if (this.focusedCourse != null && this.focusedCourse.getFullText().equalsIgnoreCase(courseToRemove)) {
                this.sectionListView.getItems().clear();
            }

            this.currentSemester.generateSchedules();
            this.regenerateSchedules();
        }
    }

    /*
     * Remove all selected courses from the ListView (and reload the UI)
     */
    public void removeAllCourses(ActionEvent _event) {
        List<String> courses = this.currentSemester.getSelectedCourseStrings();

        for (String course : courses) {
            this.currentSemester.removeCourse(course);
        }

        this.currentSemester.generateSchedules();
        this.regenerateSchedules();
        this.loadSelectedCourses();
    }

    /**
     * Generates all possible schedules consisting of selected sections.
     */
    public void regenerateSchedules() {
        this.currentSemester.generateSchedules();
        this.clearScheduleGrid();

        if (this.currentSemester.getNumberOfSchedules() == 0) {
            scheduleLabel.setText("0/0");
        } else if (this.currentSemester.getNumberOfSchedules() > 0) {
            this.loadSchedule(this.currentSemester.getSchedules().get(0));
            scheduleLabel.setText("1/" + this.currentSemester.getNumberOfSchedules());
        }
    }

    /**
     * Gets sections for a selected course and adds them to the sections
     * listview.
     *
     * @param _event
     */
    public void loadCourseSections(ActionEvent _event) {

        // Do nothing if no course has been selected.
        if (this.selectedCoursesListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        String currentSelection = this.selectedCoursesListView.getSelectionModel().getSelectedItem().toString();

        // Get the corresponding course to reference for sections
        for (Course course : this.currentSemester.getSelectedCourses()) {
            if (course.getFullText().equals(currentSelection)) {
                this.focusedCourse = course;
                break;
            }
        }

        List<String> listCellLabels = new ArrayList();

        for (Section section : this.focusedCourse.getSections()) {
            listCellLabels.add(section.toString());
        }

        this.sectionListView.setItems(FXCollections.observableList(listCellLabels));

    }

    /*
     * Load list of courses into the availableCourses ListBox and connect a few
     * things
     */
    public void loadAllCourses() {

        // intermediary ObservableList of the courses
        ObservableList<String> OList = FXCollections.observableList(this.currentSemester.getAllCourses());

        // create FilteredList that we'll actually use
        this.courseList = new FilteredList<>(OList, s -> true);

        // connect availableCourses ListView to the courseList
        this.availableCourses.setItems(this.courseList);

        // make up or down arrow on the keyboard begin to scroll the search results
        searchField.setOnKeyReleased(new javafx.event.EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                    case DOWN:
                        availableCourses.requestFocus();
                    default:
                        break;
                }
            }
        });

        // Connect search bar filtering to the courseList FilteredList (this uses
        // lambdas, it's adapted from
        // https://stackoverflow.com/questions/28448851/how-to-use-javafx-filteredlist-in-a-listview
        // and
        // https://stackoverflow.com/questions/45045631/filter-items-within-listview-in-javafx
        // )
        searchField.textProperty().addListener(obs -> {

            // select the top entry whenever the search term changes, but use
            // Platform.runLater()
            // so that JavaFX doesn't try to update the selection while it's still building
            // the ListView.
            // See
            // https://stackoverflow.com/questions/11088612/javafx-select-item-in-listview
            // for some context
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
        List<String> semesters = adapter.getSemesters();

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

    public void loadSelectedCourses() {
        this.sectionTabPane.getTabs().clear();
        this.selectedCoursesListView.setItems(FXCollections.observableList(this.currentSemester.getSelectedCourseStrings()));

        for (Course course : this.currentSemester.getSelectedCourses()) {
            this.createNewTab(course);
        }

        this.regenerateSchedules();
    }

    /**
     * Creates new tab that display associated course sections.
     *
     * @param _course
     */
    public void createNewTab(Course _course) {
        Tab tab = new Tab();
        tab.setText(_course.getID());
        VBox content = new VBox();
        ScrollPane pane = new ScrollPane();
        for (Section section : _course.getSections()) {
            VBox sectionEntry = new VBox();
            sectionEntry.setPrefHeight(30);
            sectionEntry.setMinHeight(30);
            VBox.setVgrow(content, Priority.NEVER);
            sectionEntry.setAlignment(Pos.CENTER_LEFT);
            sectionEntry.setStyle("-fx-border-color: grey; -fx-border-width: 0 0 .5 0;");
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);
            VBox.setMargin(checkBox, new Insets(0, 0, 0, 10));
            checkBox.setText(section.toString());
            sectionEntry.getChildren().add(checkBox);
            content.getChildren().add(sectionEntry);
        }
        pane.setContent(content);
        tab.setContent(pane);
        this.sectionTabPane.getTabs().add(tab);
    }

    /**
     * If there are no selections, force select all; if there are any
     * selections, unselect all of them.
     *
     * @param _event
     */
    public void selectAll(ActionEvent _event) {
        if (this.sectionTabPane.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        int index = this.sectionTabPane.getSelectionModel().getSelectedIndex();
        Tab currentTab = this.sectionTabPane.getTabs().get(index);

        if (this.allUnselected(currentTab)) {
            this.setSelectAll(true, currentTab);
        } else {
            this.setSelectAll(false, currentTab);
        }
    }

    /**
     *
     * @param _tab
     */
    public boolean allUnselected(Tab _tab) {

        VBox container = (VBox) ((ScrollPane) _tab.getContent()).getContent();
        for (int j = 0; j < container.getChildren().size(); j++) {
            VBox entry = (VBox) container.getChildren().get(j);
            CheckBox checkBox = (CheckBox) entry.getChildren().get(0);
            if (checkBox.isSelected()) {
                return false;
            }
        }
        return true;
    }

    public void setSelectAll(boolean _option, Tab _tab) {

        VBox container = (VBox) ((ScrollPane) _tab.getContent()).getContent();
        for (int j = 0; j < container.getChildren().size(); j++) {
            VBox entry = (VBox) container.getChildren().get(j);
            CheckBox checkBox = (CheckBox) entry.getChildren().get(0);
            checkBox.setSelected(_option);
        }
    }

    public void drawGrid() {

        for (int i = 1; i < NUM_ROWS; i++) {
            for (int j = 1; j < NUM_COLS; j++) {
                BorderPane region = new BorderPane();
                region.setStyle(("-fx-border-color: black; -fx-border-width: .5;"));
                this.grid[i][j] = region;
                this.scheduleGridPane.add(region, j, i);
            }
        }
    }

    public void showCRNs(ActionEvent _event) {
        if (this.currentSemester == null) {
            return;
        }
        if (this.currentSemester.getSelectedCourses().size() == 0) {
            return;
        }
        this.CRNContainer.getChildren().clear();
        StringBuilder content = new StringBuilder();
        for (Section section : this.currentSemester.getSchedules().get(this.currentScheduleIndex).getAddedSections()) {
            content.append(section.getID() + " - " + section.getCRN() + "\n");
        }
        TextArea textArea = new TextArea(content.toString());
        textArea.setEditable(false);
        this.CRNContainer.getChildren().add(textArea);
        this.CRNPane.setVisible(true);
        this.CRNPane.toFront();
    }

    public void hideCRNs() {
        this.CRNPane.setVisible(false);
    }

    public void addEntry(Section _section, int _numberOfCampusCourses) {

        String color = this.assignColor(_numberOfCampusCourses);

        int row = (int) _section.getStartTime() / 100 - 7;
        double topMargin = (_section.getStartTime() % 100) / 60;
        for (Integer col : getDays(_section)) {
            Label label = new Label(_section.getCourseID() + " - " + _section.getSectionNumber());
            BorderPane entryContainer = new BorderPane();
            entryContainer.paddingProperty().set(new Insets(grid[row][col].heightProperty().multiply(topMargin).doubleValue(), 0, 0, 0));
            StackPane pane = new StackPane();

            Rectangle rect = new Rectangle();
            rect.setStyle("-fx-fill:" + color + "; -fx-stroke: black; -fx-stroke-line-cap: round; -fx-arc-height: 10; -fx-arc-width: 10;");
            label.setAlignment(Pos.CENTER);

            pane.setStyle("");
            pane.getChildren().addAll(rect, label);
            entryContainer.setTop(pane);

            scheduleGridPane.getChildren().add(entryContainer);
            GridPane.setConstraints(entryContainer, col, row, 1, GridPane.REMAINING, HPos.CENTER, VPos.TOP);
            BorderPane region = grid[row][col];
            rect.heightProperty().bind(region.heightProperty().subtract(2).multiply(_section.getDurationHours()));
            rect.widthProperty().bind(region.widthProperty().subtract(2));
            entries.add(entryContainer);
        }
    }

    /**
     * Returns a list of meeting days for particular section.
     *
     * @param _section
     * @return
     */
    public List<Integer> getDays(Section _section) {
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
        }
        return days;
    }

    /**
     * Returns a different color for the next course on the grid.
     *
     * @param _numberOfCampusCourses
     * @return
     */
    public String assignColor(int _numberOfCampusCourses) {
        String color = "";
        switch (_numberOfCampusCourses) {
            case 1:
                // green
                color = "#ccffcc";
                break;
            case 2:
                // blue
                color = "#b3e1ff";
                break;
            case 3:
                // red
                color = "#ffb3b3 ";
                break;
            case 4:
                // yellow
                color = "#e6e600";
                break;
            case 5:
                // orange
                color = "#ffda75";
                break;
            case 6:
                color = "#ff6666";
                break;

            default:
                color = "lightblue";
        }

        return color;
    }

    public void loadSchedule(Schedule _schedule) {
        this.hideCRNs();
        this.clearScheduleGrid();
        int numberOfCampusCourses = 0;
        int onlineCourses = 0;
        StringBuilder label = new StringBuilder("Online Classes: ");
        for (Section section : _schedule.getAddedSections()) {
            if (!section.isOnline()) {
                this.addEntry(section, ++numberOfCampusCourses);
            } else {
                if (onlineCourses >= 1) {
                    label.append(" | ");
                }
                label.append(section.getID());
                onlineCourses++;
            }
        }
        this.scheduleLabel.setText(this.currentScheduleIndex + 1 + "/" + this.currentSemester.getNumberOfSchedules());
        this.onlineClassesLabel.setText(label.toString());
    }

    public void loadNextSchedule(ActionEvent _event) {
        if (this.currentSemester != null) {
            if (this.currentScheduleIndex < this.currentSemester.getSchedules().size() - 1) {
                this.currentScheduleIndex++;
                this.loadSchedule(this.currentSemester.getSchedules().get(this.currentScheduleIndex));
            }
        }
    }

    public void loadPrevSchedule(ActionEvent _event) {
        if (this.currentScheduleIndex > 0) {
            this.currentScheduleIndex--;
            this.loadSchedule(this.currentSemester.getSchedules().get(this.currentScheduleIndex));
        }
    }

    //Calls popup fxml for the email api
    public void popupAction(ActionEvent event) {
        //if no courses are selected, and the email button is pressed then thier is nothing to email, an error box is thrown
        if (this.currentSemester == null || this.currentSemester.getSelectedCourses().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
            alert.setTitle("Warning");
            alert.setHeaderText("You have not selected any courses yet");
            alert.setContentText("Select a semseter and courses and try again!");
            alert.showAndWait();
            System.out.println("No semster or courses choosen.");

            //if the user does have a course selected and the email button is pressed, it shows as normal
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ScheduleCreator/resources/views/email_popup.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 450, 150);
                Stage stage = new Stage();
                stage.setTitle("Email Course Information");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        }
    }

}
