package ScheduleCreator.models;

import java.util.ArrayList;
import java.util.List;

/**
 * This class keeps track of added course sections and produces CRNs.
 *
 * @author Jamison Valentine
 *
 * Last Updated: 3/31/2020
 */
public class Schedule {

    protected List<Section> addedSections;
    protected String totalCredits;

    public Schedule() {
        this.addedSections = new ArrayList();
    }

    /**
     * Checks to see if this _newSection can be added to the schedule
     * without incident
     * @param _newSection
     * @return
     */
    public Boolean addSection(Section _newSection) {
        if (this.addedSections.size() > 0 && !_newSection.isOnline) {

            List<Section> campusSections = new ArrayList();
            for (Section section : addedSections) if (!section.isOnline) campusSections.add(section);

            for (Section existingSection : campusSections) {

                Boolean sameDay = false;
                for (char day : _newSection.getDays().toCharArray()) {
                    if (existingSection.getDays().contains("" + day)) sameDay = true;
                }

                if (sameDay) {
                    if (existingSection.endTime >= _newSection.startTime && existingSection.startTime <= _newSection.startTime) {
                        return false;
                    }
                    if (existingSection.startTime <= _newSection.endTime && existingSection.endTime >= _newSection.endTime) {
                        return false;
                    }
                }
            }
        }

        this.addedSections.add(_newSection);
        return true;
    }

    public List<Section> getAddedSections() {
        return this.addedSections;
    }

    @Override
        public String toString() {
        StringBuilder string = new StringBuilder();
        for (Section section : this.addedSections) {
            string.append("(" + section.getCourseID() + " - " + section.getSectionNumber() + ")\n");
        }
        return string.toString();
    }
}
