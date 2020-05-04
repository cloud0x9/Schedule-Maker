package ScheduleCreator;

/**
 * An adapter that passes method calls to the translator.
 *
 * @author Nathan Tolodziecki Last Updated: 4/21/2020
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Adapter implements TranslatorInterface {

    final protected TranslatorInterface translator = new Translator();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSections(String _courseNumber, String _semester) {
        return this.translator.getSections(_courseNumber, _semester);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFullText(String _resourceName) throws FileNotFoundException, IOException {
        return this.translator.getFullText(_resourceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getCourses(String _semesterName) {
        return this.translator.getCourses(_semesterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSectionInfo(Translator.choice _choice, String _semesterName, String _section) throws FileNotFoundException, IOException {
        return this.translator.getSectionInfo(_choice, _semesterName, _section);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveCourse(String _course, String _semester) {
        this.translator.saveCourse(_course, _semester);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCourse(String _course, String _semester) {
        this.translator.removeCourse(_course, _semester);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSelectedCourses(String _semester) {
        return this.translator.getSelectedCourses(_semester);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSemesters() throws FileNotFoundException, IOException {
        return this.translator.getSemesters();
    }
}
