package ScheduleCreator.models;

/**
 * The class stores user information and class selections.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 2/17/2020
 */

import ScheduleCreator.DBAdapter;
import java.util.ArrayList;

public class User {

    protected String advisor;
    protected Schedule schedule; 
    protected int advisingCode;
    protected static ArrayList<String> selectedCourses;

    public User() {
        selectedCourses = new ArrayList();
    }

    public static void selectCourse(String _course) throws Exception {
        selectedCourses.add(_course);
//        DBAdapter.saveCourse(_course);
    }
    
    public static ArrayList<String> getCourses() {
        return selectedCourses;
    }

}
