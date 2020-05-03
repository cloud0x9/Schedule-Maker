package ScheduleCreator;

/**
 * An interface to allow easier modularity of the adapter.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 * Last updated 04/21/2020
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface TranslatorInterface {

    /**
     *
     * @return a list of semester names (Strings) that can be used as arguments
     * in all other DBAdapter methods
     * @throws java.io.FileNotFoundException
     */
    abstract List<String> getSemesters() throws FileNotFoundException, IOException;

    abstract List<String> getSections(String _courseNumber, String _semester);

    /**
     * utility method to return the full text of the file using classpath
     * resource inside the jar.
     *
     * @param _resourceName name of the file in the resources directory (without
     * a leading /)
     * @return the fulltext as a String
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    abstract String getFullText(String _resourceName) throws FileNotFoundException, IOException;

    /**
     *
     * @param _semesterName
     * @return a List of courses (as Strings) available for the semester
     */
    abstract List<String> getCourses(String _semesterName);

    /**
     * Get specific information about a section
     *
     * @param _choice type of info wanted about section
     * @param _semesterName semester name from getSemesters()
     * @param _section section name e.g. "CSC 250 - 01"
     * @return requested info as a String
     * @throws java.io.FileNotFoundException
     */
    abstract String getSectionInfo(Translator.choice _choice, String _semesterName, String _section) throws FileNotFoundException, IOException;

    abstract void saveCourse(String _course, String _semester);

    /**
     * Removes the selected course from the database.
     *
     * @param _course, _semester
     */
    abstract void removeCourse(String _course, String _semester);

    /**
     * Returns a list of the selected courses.
     *
     * @return A List (of Strings) of the selected courses.
     */
    abstract List<String> getSelectedCourses(String _semester);
}
