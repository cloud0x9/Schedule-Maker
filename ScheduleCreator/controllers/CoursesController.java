package ScheduleCreator.controllers;

/** This class controls interactions in the Courses/Scheduling View
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 * Last Updated: 2/28/2020
 */

import ScheduleCreator.DBAdapter;
import ScheduleCreator.Tests;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CoursesController implements Initializable {

	// name of the currently selected semester
	protected String semesterName;
	
	// list of available semesters to choose from
	protected ArrayList<String> semesters;

	// list of semesters the user has saved
	protected ArrayList<String> userSemesters;

	// current list of all sections in the selected semester
	protected ArrayList<ScheduleCreator.models.Section> currentSections;

	// list of sections the user has selected
	protected ArrayList<ScheduleCreator.models.Section> selectedSections;

	// some variables for the display of course boxes
	private int rowIndex = 0;
	private int colIndex = 0;
	private int numberOfCourses;

	// UI elements
	@FXML private GridPane courses; 
	@FXML private TextField courseText;

        /**
         * Allows user to go back to primary view. 
         * @param _event
         * @throws Exception 
         */
        public void backToPrimary(ActionEvent _event) throws Exception {
            
		Parent root = FXMLLoader.load(getClass().getResource("/ScheduleCreator/resources/views/primary.fxml"));
		Scene primaryScene = new Scene(root);
            
		//Get information from primary stage
		Stage window = (Stage)((Node)_event.getSource()).getScene().getWindow();
		window.setScene(primaryScene);
		window.show();
        }

        /**
         * Gets courses using DBAdapter and loads them into the courses GridPane
         * @throws Exception 
         */
        public  void loadSections() throws Exception {

		for (int i = 0; i < selectedSections.size(); i++) {
			rowIndex = i / 3;
			colIndex = i % 3;
			Label label = new Label(selectedSections.get(i).getClassName());
                        System.out.println(selectedSections.get(i).getClassName());
			HBox newBox = new HBox(label);
			newBox.setMinHeight(100);
			newBox.setMinWidth(100);
			newBox.setStyle("-fx-border-color: black");
			courses.add(newBox, colIndex, rowIndex);
		}
        }
        
        public void addCourse(ActionEvent _event) throws Exception {

		// get user input
		String course = courseText.getText();

		// Don't do this, instead use a section from currentSections
		ScheduleCreator.models.Section exampleSection = new ScheduleCreator.models.Section(course, "CAS", 441, 12412412);
		selectedSections.add(exampleSection);

		// new way to save a list of sections
		DBAdapter.saveUserSections(semesterName, currentSections);

		// reset search box
		courseText.setText("");

		// create new box with the section
		Label label = new Label(course);
		HBox newBox = new HBox(label);
		
		//Go to next row after every 3rd course.
		rowIndex = numberOfCourses / 3;
            
		//Go back to first column after every 3rd course.
		colIndex = numberOfCourses % 3;
            
		// add box to the grid.
		newBox.setMinHeight(100);
		newBox.setMinWidth(100);
		newBox.setStyle("-fx-border-color: black");
		courses.getChildren().add(newBox);
		GridPane.setRowIndex(newBox, rowIndex);
		GridPane.setColumnIndex(newBox, colIndex);
		numberOfCourses++;            
        }

	// only runs once
	// TODO: this replaces the constructor for this class, right?
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// load the available semesters
		semesters = ScheduleCreator.DBAdapter.getSemesters();
                
		// choose a semester to start off from (Spring)
		this.semesterName = semesters.get(0);                

                // load the sections for the current semester
		this.currentSections = 	ScheduleCreator.DBAdapter.getSemesterSections(semesterName);                

		// load the available semesters for user data, if any
		userSemesters = ScheduleCreator.DBAdapter.getUserSemesters();

                // load the user's saved sections for this semester		
		this.selectedSections = ScheduleCreator.DBAdapter.getUserSections(semesterName);                

		// DON'T do this for the actual implementation, only
		// request user sections using strings from
		// getUserSemesters(), and make sure currentSections
		// and selectedSections are from the same semester:

		try {
			loadSections();
		}
		catch (Exception ex) {Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);};
	}
}
