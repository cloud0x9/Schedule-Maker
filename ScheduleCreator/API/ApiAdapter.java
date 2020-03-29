package ScheduleCreator.API;

/**
 * Adapter class for "YesNoApi", so it doesn't have to be called directly.
 *
 * @author Ilyass Sfar
 *
 * Last Updated: 3/27/2020
 */


public class ApiAdapter implements ApiInterface {

    protected static final ApiInterface thisApi = new YesNoApi();

    /**
     *
     * @param _decision The decision the API returns can be forced to a specific choice
     * this is left blank here, so the API makes its own choice.
     * @return
     */
    @Override
    public String decisionForce(String _decision) {
        return ApiAdapter.thisApi.decisionForce(_decision);
    }

}