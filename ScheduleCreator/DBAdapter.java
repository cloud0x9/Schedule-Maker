package ScheduleCreator;

import java.util.TreeSet;
import ScheduleCreator.models.Course;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/** This class is used to retrieve and modify persistent data for the application.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 * 
 * Last Updated: 2/21/2020
 */

public class DBAdapter {
    
    protected static File selectedCourseFile = new File("src/ScheduleCreator/resources/raw/user_selected_courses.txt");;
    
    /**
     * Saves a selected course (abbreviation and number) and saves to database.
     * @param _course
     * @throws Exception 
     */   
        
    public static void saveCourse(String _course) throws Exception {
            
            //Open file to add new classes.
            FileWriter output = new FileWriter(selectedCourseFile, true);
            
            //Adds new selected course to new line. 
            output.append(_course + "\n");
                        
            output.close();
    }
	
    
    public static void removeCourse(String _course) throws Exception {
        
            Scanner input = new Scanner(selectedCourseFile);
            StringBuffer newContents = new StringBuffer();
            String line = "";
            
            while (input.hasNext()) {
                   line = input.nextLine();
            if (line.contains(""));
            if (!line.contains(_course))
                newContents.append(_course + '\n');                   
            }
            
            FileWriter writer = new FileWriter(selectedCourseFile);
            writer.append(newContents.toString());
            writer.close();
            
    }
	// get a list of semesters (which can be used as an argument to DBAdapter.getCourses)
	public static List<String> getSemesters() {
		List<String> semesters = new ArrayList();
		
		File dir = ScheduleCreator.Boilerplate.GetResourceFile("raw/semesters");
		String[] pathnames;
		pathnames = dir.list();
		
		for (String pathname : pathnames) {
			System.out.println(pathname);
			File f = new File(pathname);
			semesters.add(f.getName());
		}
                return semesters;
	}
    
    public static ArrayList<String> getSelectedCourses() throws Exception {

        Scanner input = new Scanner(selectedCourseFile);
        
        //Load courses from text file to be returned as a list. 
        ArrayList<String> selectedCourses = new ArrayList();
        String line = "";
        while (input.hasNext()) {
            line = input.nextLine();
            selectedCourses.add(line.trim());
        }
        
        input.close();
        return selectedCourses;
    }
    
    public static void getCourses(String _abbreviation) throws Exception {
        
        File file = new File("src/ScheduleCreator/resources/raw/spring2020.txt");
        Scanner input = new Scanner(file);
        String line;
        
        TreeSet<String> availableCourses = new TreeSet();
        FileWriter writer = new FileWriter(new File("src/ScheduleCreator/resources/raw/courses.txt"));
        int start = 0;
        int end = 0;
        while (input.hasNext()) {
                
                //add whitespace to start and end of abbreviation
                _abbreviation = " " + _abbreviation + " ";
                _abbreviation.toUpperCase();
                
                while (input.hasNext()) {
                    line = input.nextLine();
                    if (line.contains(_abbreviation)) {
                        
                        start = line.indexOf(_abbreviation);
                        end = start + 8;
                        String courseTitle = line.subSequence(start, end).toString();
                        if (!availableCourses.contains(courseTitle)) availableCourses.add(courseTitle);
                        
                    }
                }
        }
        
        writer.append(availableCourses.toString());
        input.close();
        writer.close();
    }
    

    
}
