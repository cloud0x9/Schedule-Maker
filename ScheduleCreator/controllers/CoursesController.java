package ScheduleCreator.controllers;

import ScheduleCreator.DBAdapter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;


/** This class controls interactions in the Courses View.  
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 * 
 * Last Updated: 3/4/2020
 */

public class CoursesController implements Initializable {
	@FXML private Button semesterButton;
	@FXML private ComboBox<String> semesterComboBox;    
  
	@FXML private ComboBox<String> courseComboBox;    

	@FXML private ListView selectedCourses;
    
	@FXML private Button courseButton;
	@FXML private Button removeCourseButton;
        DBAdapter adapter = new DBAdapter();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
                        loadSemesters();
			loadSelectedCourses();
		}
		catch (Exception e) {};
        
	}
    
	public void addSelectedCourse(ActionEvent _event) throws Exception {
		String choice = courseComboBox.getValue();
		List<String> courseList = new ArrayList();
		courseList.add(choice);
		selectedCourses.getItems().add(choice);
		adapter.saveCourse(choice);
	}
	public void switchSemester(ActionEvent _event) throws Exception {
		clearCalendar();
		clearSectionList();
                String[] temp = semesterComboBox.getValue().split(" ");
                String semester = temp[0].toLowerCase() + "_" + temp[1] + "_";
                loadAllCourses(semester);
                
	}

	public void clearCalendar() {
		System.out.println("Dummy function to clear the calendar for when we switch semesters");
	}

	protected void clearSectionList() {
		System.out.println("Dummy function to clear the list of available sections for when we switch semesters");
	}

	
	public void removeSelectedCourse(ActionEvent _event) throws Exception {
//		Object itemToRemove = selectedCourses.getSelectionModel().getSelectedItem();
//		String courseToDelete = (String)itemToRemove;
//		selectedCourses.getItems().remove(itemToRemove);
//		DBAdapter.removeCourse(courseToDelete);
	}
    
	public void loadAllCourses(String _semester) throws Exception {
                List<String> courses = adapter.getCourses(_semester);
		courseComboBox.setItems(FXCollections.observableList(courses));
	}

	public void loadSemesters() throws Exception {
		List<String> semesters = adapter.getSemesters();
		semesterComboBox.setItems(FXCollections.observableList(semesters));
	}

    
	public void loadSelectedCourses() throws Exception {
		List<String> courses = adapter.getSelectedCourses();
		selectedCourses.setItems(FXCollections.observableList(courses));
	}
}

