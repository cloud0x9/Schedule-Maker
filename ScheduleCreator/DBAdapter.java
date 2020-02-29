package ScheduleCreator;

/** This class is used to retrieve course lists
 * NOTE: DUMMY methods until we finish the translator
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 * Last Updated: 2/28/2020
 */

import java.util.ArrayList;


public class DBAdapter {

	// return available semesters 
	public static ArrayList<String> getSemesters() {
		ArrayList<String> semesters = new ArrayList<>();
		semesters.add("Spring"); // DUMMY!!!!
		semesters.add("Fall");
		return semesters;
	};

	// return list of semesters that the user has saved
	public static ArrayList<String> getUserSemesters() {
		ArrayList<String> semesters= new ArrayList<>();
		semesters.add("Spring"); // user only has one saved semester
		return semesters;
	}

	// get the sections for a semester
	public static ArrayList <ScheduleCreator.models.Section> getSemesterSections(String _semesterName) {

		// create dummy list of sections
		ArrayList<ScheduleCreator.models.Section> semester = new ArrayList<>();

		// add some dummy data (classes taught both semesters)
		semester.add(new ScheduleCreator.models.Section("Software Engineering", "CSC", 340, 124124));
		semester.add(new ScheduleCreator.models.Section("Introduction to Robotics", "EGR", 112, 12415));
                semester.add(new ScheduleCreator.models.Section("Metaphysical Contraband", "SOC", 301, 203));

		// add some semester-specific courses
		switch (_semesterName) {
		case "Spring":
			semester.add(new ScheduleCreator.models.Section("Spring Break CupCake", "BLRA", 420, 124111));
                        break;
		case "Fall":
			semester.add(new ScheduleCreator.models.Section("Falling For Poo", "FUC", 69, 420420));
                        break;
		}
                return semester;
	}

	public static ArrayList <ScheduleCreator.models.Section> getUserSections(String _semesterName) {

		// create dummy list of sections
		ArrayList<ScheduleCreator.models.Section> semester = new ArrayList<>();

		// add some semester-specific courses
		switch (_semesterName) {
		case "Spring":
			semester.add(new ScheduleCreator.models.Section("Spring Break CupCake", "BLRA", 420, 124100011));
                        break;
		case "Fall":
			semester.add(new ScheduleCreator.models.Section("Introduction to Robotics", "EGR", 112, 124123125));
                        break;
		}
                return semester;
	}

	public static void saveUserSections(String _semesterName, ArrayList <ScheduleCreator.models.Section> _sections) {
            // DUMMY, does nothing
	}

}
