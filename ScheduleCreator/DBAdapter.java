package ScheduleCreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to retrieve and modify persistent data for the
 * application.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 * 
 * Last Updated: 3/3/2020
 */
public class DBAdapter { 

    /**
     * Saves the selected course (abbreviation and number) and saves to database.
     * @param _course
     * @throws Exception
     */
    
    public DBAdapter() {
    }

    public  void saveCourse(String _course) throws Exception {
//
//        String file = getClass().getResource("resources/raw/spring2020").getFile();
//        PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(file)));
//
//        //Adds new selected course to new line.
//        output.append(_course + "\n");
//
//        output.close();
    }
    
    /**
     * Removes the selected course from the database. 
     * @param _course
     * @throws Exception 
     */
    
//    public static void removeCourse(String _course) throws Exception {
//        
//            Scanner input = new Scanner(selectedCourseFile);
//            StringBuffer newContents = new StringBuffer();
//            String line = "";
//            
//            /**
//             * Gets all of the courses except the selected one 
//             * and appends to a new file to be saved.              * 
//             */
//            while (input.hasNext()) {
//                   line = input.nextLine();
//
//            if (!line.contains(_course))
//                newContents.append(_course + '\n');                   
//
//            }
//
//        FileWriter writer = new FileWriter(selectedCourseFile);
//        writer.append(newContents.toString());
//        writer.close();
//
//    }

    	// get a list of semesters (which can be used as an argument to DBAdapter.getCourses)
	public  List<String> getSemesters() throws Exception {

            List<String> semesters = new ArrayList();

            InputStream stream = getClass().getResourceAsStream("resources/raw/available_semesters.txt");
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(streamReader);

            String line = reader.readLine();
            while (line != null) {
                semesters.add(line);
                line = reader.readLine();
            }

            reader.close();

            System.out.println(semesters.toString());
            return semesters;
                
	}
    
    /**
     * Returns a list of the selected courses.
     * @return
     * @throws Exception 
     */

    public List<String> getSelectedCourses() throws Exception {

        InputStream stream = getClass().getResourceAsStream("resources/raw/user_selected_courses.txt");
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);
        
        ArrayList<String> selectedCourses = new ArrayList();
        
        String line = reader.readLine();
        while (line != null) {
            selectedCourses.add(line);
            line = reader.readLine();
        }

        reader.close();      

        return selectedCourses;
    }

    public List<String> getCourses(String _semester) throws Exception {

        InputStream stream = getClass().getResourceAsStream("resources/raw/" + _semester + "courses.txt");
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        ArrayList<String> courses = new ArrayList();
        
        String line = reader.readLine();
        while (line != null) {
            courses.add(line);
            line = reader.readLine();
        }
        return courses;
    }

    //given a class name (ex CSC 230) the times for all sections are resturned.
    //given a class name with section (ex. CSC 230 - 01) only the time for the class is returned
    public static void getTime(String _abbreviation) throws Exception {
        //regex expression to get time from the current format
        final String time = "(?<=([ ]\\b[A-Z]{3}\\b.\\b[0-9]{3}\\b.+ [0-9]{2}\\b)).+?(?=\\b([	](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA.*TBA\\b)))\\b)";
        List<String> lines = Files.readAllLines(Paths.get("src/ScheduleCreator/resources/raw/CoursesTimeDate.txt"));
        for (String line : lines) {
            if (line.contains(_abbreviation)) {
                String results = line;

                Matcher match = Pattern.compile(time).matcher(results);
                while (match.find()) {

                    String output = (match.group());
                    System.out.println(output);

                }

            }
        }
    }

}

