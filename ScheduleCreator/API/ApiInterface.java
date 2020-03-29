package ScheduleCreator.API;

/**
 * Interface for the "YesNoApi" that sets a standard for any calls made to the API.
 *
 * @author Ilyass Sfar
 *
 * Last Updated: 3/27/2020
 */

public interface ApiInterface {
    /**
     * The API has only one field that is also optional, the field is left blank.
     *
     * @param _decision The decision the API returns can be forced to a specific choice
     * this is left blank here, so the API makes its own choice.
     * @return
     */
    public String decisionForce(String _decision);

}
