package ScheduleCreator.models;

/**
 * This class models the information for one course section.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 2/27/2020
 */

public class Section {
	protected String className; // e.g. "Software Engineering"
	protected String abbreviation; // e.g. "CSC"
	protected int courseNumber; // e.g. 340
	protected int sectionNumber; // e.g. 02
	protected String location; // e.g. "Petty Building 007"
	protected String instructor; // e.g. "Richard I Quigley"
//	protected HashMap daysAndTimes; // TODO: waiting on calendar api info
	protected int CRN; // e.g. 13198

	// constructor; we need at least this much info to use the section.
	public Section(String _className, String _abbreviation, int _courseNumber, int _CRN) {
		this.className = _className;
		this.abbreviation = _abbreviation;
		this.courseNumber = _courseNumber;
		this.CRN = _CRN;
	}
	
// ================= GETTERS ==========================================

	public String getClassName() {
		return this.className;
	}
	public int getSectionNumber() {
		return sectionNumber;
	}

	public String getLocation() {
		return location;
	}

	public String getInstructor() {
		return instructor;
	}
    
	// public HashMap getDaysAndTimes() {
	// 	return daysAndTimes;
	// }

// ================= SETTERS ============================================

	public void setSectionNumber(int _sectionNumber) {
		this.sectionNumber = _sectionNumber;
	}

	public void setLocation(String _location) {
		this.location = _location;
	}

	public void setInstructor(String _instructor) {
		this.instructor = _instructor;
	}

	// public void setDaysAndTimes(HashMap _daysAndTimes) {
	// 	this.daysAndTimes = _daysAndTimes;
	// }
}
