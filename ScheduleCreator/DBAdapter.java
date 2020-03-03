package ScheduleCreator;

import java.io.File;
import java.io.FileWriter;
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

    protected static File selectedCourseFile = new File("src/ScheduleCreator/resources/raw/user_selected_courses.txt");

    ;

    /**
     * Saves the selected course (abbreviation and number) and saves to database.
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
    
    /**
     * Removes the selected course from the database. 
     * @param _course
     * @throws Exception 
     */
    
    public static void removeCourse(String _course) throws Exception {
        
            Scanner input = new Scanner(selectedCourseFile);
            StringBuffer newContents = new StringBuffer();
            String line = "";
            
            /**
             * Gets all of the courses except the selected one 
             * and appends to a new file to be saved.              * 
             */
            while (input.hasNext()) {
                   line = input.nextLine();

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
    
    /**
     * Returns a list of the selected courses.
     * @return
     * @throws Exception 
     */

    public static List<String> getSelectedCourses() throws Exception {

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

    public static List<String> getCourses(String _semester) throws Exception {

        File file = new File("src/ScheduleCreator/resources/raw/" + _semester + "courses.txt");
        Scanner input = new Scanner(file);
        input.useDelimiter("\\Z");
        String contents = input.next();
        
        List<String> courses = Arrays.asList(contents.split("\n"));
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

