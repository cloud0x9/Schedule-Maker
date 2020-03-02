package ScheduleCreator.controllers;

import ScheduleCreator.DBAdapter;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;


/** This class controls interactions in the Courses View.  
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 * 
 * Last Updated: 2/21/2020
 */

public class CoursesController implements Initializable {
    
  
    @FXML private ComboBox<String> comboBox;    

    @FXML private ListView selectedCourses;
    
    @FXML private Button courseButton;
    @FXML private Button removeCourseButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
        loadAllCourses();
        loadSelectedCourses();
        }
        catch (Exception e) {};
        
    }
    
    public void addSelectedCourse(ActionEvent _event) throws Exception {
        String choice = comboBox.getValue();
        List<String> courseList = new ArrayList();
        courseList.add(choice);
        selectedCourses.getItems().add(choice);
        DBAdapter.saveCourse(choice);
    }
	public void switchSemester(ActionEvent _event) {
		System.out.println("dummye");
	}

	
    public void removeSelectedCourse(ActionEvent _event) throws Exception {
        Object itemToRemove = selectedCourses.getSelectionModel().getSelectedItem();
        String courseToDelete = (String)itemToRemove;
        selectedCourses.getItems().remove(itemToRemove);
        DBAdapter.removeCourse(courseToDelete);
    }
    
    public void loadAllCourses() throws Exception {
           File file = new File("src/ScheduleCreator/resources/raw/courses.txt");
           Scanner input = new Scanner(file);
           List<String> courses = new ArrayList();
           String line = "";
           while (input.hasNext()) {
               line = input.nextLine();
               courses.add(line);
           }
           comboBox.setItems(FXCollections.observableList(courses));
    }
    
    public void loadSelectedCourses() throws Exception {
        
        List<String> courses = DBAdapter.getSelectedCourses();
        selectedCourses.setItems(FXCollections.observableList(courses));
    }
}

