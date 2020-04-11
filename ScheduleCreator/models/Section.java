package ScheduleCreator.models;

import java.util.Scanner;

/**
 * This class models information for course sections.
 *
 * @author Jamison Valentine, Ilyass sfar
 *
 * Last Updated: 4/11/2020
 */
public class Section {

    protected final String courseID;
    protected final String id;
    protected final String location;
    protected final String instructor;
    protected final String daysAndTimes;
    protected final String secondDaysAndTimes;
    protected String days;
    protected double startTime;
    protected double endTime;
    protected final String CRN;
    protected final String sectionNumber;
    protected final Boolean isOnline;
    protected final Boolean hasTwoTimes;

    public Section(String _courseID, String _sectionNumber, String _daysAndTimes, String _secondDaysAndTimes, String _location, String _instructor, String _CRN, Boolean _isOnline, Boolean _twoTimes) {
        this.courseID = _courseID;
        this.location = _location;
        this.instructor = _instructor;
        this.daysAndTimes = _daysAndTimes;
        this.secondDaysAndTimes = _secondDaysAndTimes;
        this.CRN = _CRN;
        this.sectionNumber = _sectionNumber;
        this.id = _courseID + "-" + _sectionNumber;
        this.isOnline = _isOnline;
        this.hasTwoTimes = _twoTimes;
        this.setTimes();
    }

//=================  GETTERS ===============
    public String getID() {
        return this.id;
    }

    public double getDurationHours() {
        double difference = this.endTime - this.startTime;
        double hours = (int) (difference / 100);
        double minutes = difference % 100;
        hours += (minutes / 60);
        return hours;
    }

    public Boolean isOnline() {
        return this.isOnline;
    }

    public String getDaysAndTimes() {
        return this.daysAndTimes;
    }

    public String getDays() {
        return this.days;
    }

    public double getStartTime() {
        return this.startTime;
    }

    public double getEndTime() {
        return this.endTime;
    }

    public String getCRN() {
        return this.CRN;
    }

    public String getInstructor() {
        return this.instructor;
    }

    public String getLocation() {
        return this.location;
    }

    public String getSectionNumber() {
        return this.sectionNumber;
    }

    public String getCourseID() {
        return this.courseID;
    }

    @Override
    public String toString() {
        String string = "";

        if (!this.isOnline && !this.hasTwoTimes) {
            string = this.sectionNumber + " | " + this.daysAndTimes + " | " + this.location + " | " + this.instructor + " | " + this.CRN;
        } else if (this.isOnline) {
            string = this.sectionNumber + " | Online | " + this.instructor + " | " + this.CRN;
        } else if (this.hasTwoTimes) {
            string = this.sectionNumber + " | " + this.daysAndTimes + " | " + this.secondDaysAndTimes + " | " + this.location + " | " + this.instructor + " | " + this.CRN;
        }
        return string;
    }

//========================= SETTERS =============================
    private void setTimes() {

        if (this.isOnline) {
            this.days = "";
            this.startTime = 0;
            this.endTime = 0;
            return;
        }
        Scanner input = new Scanner(this.daysAndTimes);
        this.days = input.next();
        int start = Integer.parseInt(input.next().replace(":", ""));
        if (input.next().equals("pm")) {
            if (start < 1200) {
                start += 1200;
            }
        }
        input.next();
        int end = Integer.parseInt(input.next().replace(":", ""));
        if (input.next().equals("pm")) {
            if (end < 1200) {
                end += 1200;
            }
        }
        this.startTime = start;
        this.endTime = end;
    }

}
