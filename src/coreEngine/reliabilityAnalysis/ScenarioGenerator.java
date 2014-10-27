package coreEngine.reliabilityAnalysis;

import GUI.major.MainWindow;
import coreEngine.Helper.CEConst;
import static coreEngine.Helper.CEConst.IDS_MAIN_NUM_LANES_IN;
import static coreEngine.Helper.CEConst.IDS_NUM_PERIOD;
import static coreEngine.Helper.CEConst.IDS_NUM_SEGMENT;
import coreEngine.Helper.CEDate;
import coreEngine.Seed;
import coreEngine.reliabilityAnalysis.DataStruct.DemandData;
import coreEngine.reliabilityAnalysis.DataStruct.IncidentData;
import coreEngine.reliabilityAnalysis.DataStruct.Scenario;
import coreEngine.reliabilityAnalysis.DataStruct.ScenarioInfo;
import coreEngine.reliabilityAnalysis.DataStruct.WeatherData;
import coreEngine.reliabilityAnalysis.DataStruct.WorkZoneData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Class that generates the reliability scenarios for FREEVAL reliability
 * analysis.
 *
 * @author Lake Trask
 *
 */
public class ScenarioGenerator {

    //<editor-fold defaultstate="collapsed" desc="Input Variables">
    /**
     * Seed instance from which the scenarios will be generated.
     */
    private Seed seed = null;

    /**
     * Data Structure holding the scenario generator GP segment demand
     * information for the Reliability Reporting Period.
     */
    private DemandData demandData_GP = null;

    /**
     * Data Structure holding the scenario generator weather information for the
     * Reliability Reporting Period.
     */
    private WeatherData weatherData = null;

    /**
     * Data structure holding the scenario generator GP segment incident
     * information for the Reliability Reporting Period.
     */
    private IncidentData incidentData_GP = null;

    /**
     * List of work zones (GP Only) for the Reliability Reporting Period.
     */
    private ArrayList<WorkZoneData> workZoneData_GP = null;

    /**
     * Boolean indicating if Work Zones should be included in the Scenario
     * Generation process.
     */
    private boolean wzBool;

    /**
     * Boolean indicating if weather events should be included in the Scenario
     * Generation process.
     */
    private boolean weatherBool;

    /**
     * Boolean indicating if GP incident events should be included in the
     * Scenario Generation process.
     */
    private boolean incidentBool_GP;

    /**
     * Boolean indicating if the Scenario Generation process has managed lane
     * analysis.
     */
    private boolean mlUsed = false;

    /**
     * Data structure holding the scenario generator ML segment incident
     * information for the Reliability Reporting Period.
     */
    private IncidentData incidentData_ML = null;

    /**
     * Data structure holding the scenario generator ML segment demand
     * information for the Reliability Reporting Period.
     */
    private DemandData demandData_ML = null;

    /**
     * Boolean indicating if GP incident events should be included in the
     * Scenario Generation process.
     */
    private boolean incidentBool_ML;
//</editor-fold>

    // Outputs
    //GP Lanes
    /**
     * Scenario instance containing the Adjustment Factors for all generated GP
     * reliability scenarios.
     */
    private Scenario scenario;

    /**
     * List of ScenarioInfos for all generated reliability scenarios.
     */
    private ArrayList<ScenarioInfo> scenarioInfos = null;

    // Managed Lanes
    /**
     * Scenario instance containing the Adjustment Factors for all generated ML
     * reliability scenarios.
     */
    private Scenario mlScenario;

    // Constants and calculated values
    /**
     * Length (in hours) of the daily study period.
     */
    private final float dailyStudyPeriodDuration;

    /**
     * Number of replications for each Demand Combination (Default: 4).
     */
    private int numReplications = 4;

    /**
     * Number of reliability scenarios generated.
     */
    private int numScenarios = 0;

    /**
     * Array containing the number of each day type (Mon, Tue, etc) excluded in
     * each month.
     */
    private final int[][] excludedDays = new int[12][7];

    // Random number generator
    /**
     * Instance of Java Random class used as the random number generator.
     */
    private Random RNG;

    /**
     * Random number generator seed used by the RNG variable.
     */
    private long rngSeed;

    /**
     * List of names of each month (used in MainWindow Log outputs).
     */
    public final static String[] monthString = new String[]{
        "January", "February", "March", "April", "May", "June", "July", "August", "September",
        "October", "November", "December"
    };

    /**
     * Constructor for the Scenario Generator Class
     *
     * @param seed The Seed instance that will be used to generate scenarios.
     */
    public ScenarioGenerator(Seed seed) {
        this.seed = seed;

        this.dailyStudyPeriodDuration = this.seed.getValueInt(IDS_NUM_PERIOD) * 0.25f;

        this.rngSeed = (System.currentTimeMillis() % 1000000);
        this.RNG = new Random(rngSeed);

    }

    // <editor-fold defaultstate="collapsed" desc="Setters">
    /**
     * Setter for the GP demand data used to generate scenarios
     *
     * @param data GP demand data information.
     */
    public void setDemandDataGP(DemandData data) {
        this.demandData_GP = data;
    }

    /**
     * Setter the work zone data (GP Only) used in scenario generation.
     *
     * @param data ArrayList of GP Work Zones in the RRP
     */
    public void setWorkZoneDataGP(ArrayList<WorkZoneData> data) {
        this.workZoneData_GP = data;
    }

    /**
     * Setter for the weather data used to generate scenarios.
     *
     * @param data Weather event generation information.
     */
    public void setWeatherData(WeatherData data) {
        this.weatherData = data;
    }

    /**
     * Setter for the GP segment incident data used to generate scenarios
     *
     * @param data GP Incident data information.
     */
    public void setGPIncidentData(IncidentData data) {
        this.incidentData_GP = data;
    }

    /**
     * Setter for the number of replications of each Demand Combination to be
     * generated. Controls the total number of reliability scenarios that will
     * be generated: Total Number of Scenarios = Number of Replications * Number
     * of unique Demand Combinations (Overwrites the default value of 4 if
     * used).
     *
     * @param numReplications New number of replications for each Demand
     * Combination (must be greater than 0).
     */
    public void setNumberOfReplications(int numReplications) {
        if (numReplications > 0) {
            this.numReplications = numReplications;
        } else {
            throw new NumberFormatException();
        }
    }

    /**
     * Setter for the seed value for the Random Number Generator used in the
     * Scenario Generation process.
     *
     * @param newSeed
     */
    public void setRNGSeed(long newSeed) {
        rngSeed = newSeed;
        resetRNG();
    }

    /**
     * Setter for the boolean used to indicated if Managed Lanes should be
     * included in Scenario Generation. If set to true, then reliability
     * scenarios will include demand factors and incident events (if specified)
     * for the managed lanes of the facility.
     *
     * @param mlUsed Boolean indicator of Managed Lane inclusion.
     */
    public void setMLUsed(boolean mlUsed) {
        this.mlUsed = mlUsed;
    }

    /**
     * Setter for the Managed Lane(ML) segment incident data used to generate
     * scenarios.
     *
     * @param incidentMLData Managed Lane segment incident information.
     */
    public void setMLIncidentData(IncidentData incidentMLData) {
        this.incidentData_ML = incidentMLData;
    }

    /**
     * Setter for the Managed Lane (ML) segment demand data information.
     *
     * @param demandMLData Managed Lane segment demand information.
     */
    public void setMLDemandData(DemandData demandMLData) {
        this.demandData_ML = demandMLData;
    }

    /**
     * Method to indicate whether work zones are included in reliability
     * analysis. If set to true then any specified work zones will be assigned
     * to the scenarios generated.
     *
     * @param val Boolean indicating the inclusion of work zone events in
     * reliability analysis.
     */
    public void includeGPWorkZones(boolean val) {
        wzBool = val;
    }

    /**
     * Method to indicate if GP incidents are included in reliability analysis.
     * If set to true then incidents are generated and assigned to reliability
     * scenarios via the specified GP IncidentData.
     *
     * @param val Boolean indicating the inclusion of incident events in
     * reliability analysis.
     */
    public void includeIncidentsGP(boolean val) {
        incidentBool_GP = val;
    }

    /**
     * Method to indicate if weather events are included in reliability
     * analysis. If set to true then weather events are generated and assigned
     * to reliability scenarios via the specified WeatherData.
     *
     * @param val Boolean indicating the inclusion of weather events in
     * reliability analysis.
     */
    public void includeWeather(boolean val) {
        weatherBool = val;
    }

    /**
     * Method to indicate if ML incidents are included in reliability analysis.
     * If set to true then incidents are generated and assigned to reliability
     * scenarios via the specified ML IncidentData.
     *
     * @param val Boolean indicating the inclusion of ML incident events in
     * reliability analysis.
     */
    public void includeIncidentsML(boolean val) {
        incidentBool_ML = val;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * Getter for GP Scenario object.
     *
     * @return Scenario Object for General Purpose (GP) segments.
     */
    public Scenario getGPScenario() {
        return scenario;
    }

    /**
     * Getter for ML Scenario Object.
     *
     * @return Scenario Object for Managed Lane (ML) segments.
     */
    public Scenario getMLScenario() {
        return mlScenario;
    }

    /**
     * Getter for the ArrayList of ScenarioInfo objects.
     *
     * @return ArrayLIst containing the ScenarioInfo objects for all generated
     * reliability scenarios.
     */
    public ArrayList<ScenarioInfo> getScenarioInfoList() {
        return scenarioInfos;
    }

    /**
     * Getter for the number of replications of each Demand Combination that
     * will be generated.
     *
     * @return Number of replications that will be generated of each unique
     * Demand Combination.
     */
    public int getNumberOfReplications() {
        return this.numReplications;
    }

    /**
     * Getter for the seed value used by the Random Number Generator during the
     * scenario generation process.
     *
     * @return Seed value for the Random Number Generator.
     */
    public long getRNGSeed() {
        return this.rngSeed;
    }
        // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Day Exclusion">
    /**
     * Method that excludes any days specified. Used to exclude days which have
     * abnormal demands or other parameters (i.e. Fourth of July). This affects
     * the probabilities of the respective Demand Combinations. Months are
     * indexed 1 (January) to 12 (December), and day types are indexed 0 - Mon,
     * 1 - Tue, 2 - Wed, 3- Thu, 4 - Fri, 5 - Sat, 6 - Sun. Note this process is
     * not additive and should be done once for each month in the Reliability
     * Reporting Period. Example: to exclude just Friday July 4th, 2014, numDays
     * = new int{0,0,0,0,1,0,0}, and month = 7.
     *
     * @param numDays Array of length 7 containing the number of each day type
     * excluded (0 - Mon, 1 - Tue, 2 - Wed, 3- Thu, 4 - Fri, 5 - Sat, 6 - Sun).
     * @param month Month in which the days excluded occur (1- Jan, 2 - Feb,
     * etc.)
     */
    public void excludeDaysInMonth(int[] numDays, int month) {
        if (arraySum(numDays) > 0) {
            System.out.println("Excluding " + arraySum(numDays)
                    + " days in " + monthString[month - 1]);
            System.arraycopy(numDays, 0, excludedDays[month - 1], 0, 7);

        }
    }

    /**
     * Method to exclude days from the array of the (12x7=84) array of active
     * days.
     *
     * @param daysArr 84 length array of active days
     */
    private void excludeDays(int[] daysArr) {
        int currIdx;
        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < 7; day++) {
                currIdx = (7 * month) + day;
                if (excludedDays[month][day] > 0) {
                    if (daysArr[currIdx] > excludedDays[month][day]) {
                        daysArr[currIdx] -= excludedDays[month][day];
                    } else {
                        daysArr[currIdx] = 0;
                    }
                }
            }
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Scenario Generation">
    // <editor-fold defaultstate="collapsed" desc="Scenario Generation with checkboxes active (currently used)">
    /**
     * Method to generate reliability scenarios for the RRP. User should set all
     * necessary demand, weather, incident, and work zone data, as well as
     * whether or each event type is included and whether Managed Lanes are
     * considered. Once the process has been run, the the Scenario(GP and ML if
     * specified) object(s) and ScenarioInfo list will contain the reliability
     * scenarios and relevant information. These should be obtained for any
     * further analsis through their respective getter methods (getGPScenario,
     * getMLScenario, getScenarioInfoList)
     *
     * @return Boolean indicating whether the Scenario Generation process was
     * successful.
     */
    public boolean generateScenarios() {

        if (allInputValid()) {

            int[] monthChangeIdx = new int[12]; // To keep track of where each month begins and ends

            int[] numDaysInMonthAP = CEDate.numDayOfWeekInMonthAP(demandData_GP);
            excludeDays(numDaysInMonthAP);
            numScenarios = calculateNumberOfScenarios(numDaysInMonthAP);

            //Creating scenario probabilities
            float[] probArray = createProbabilities(demandData_GP);
            scenario = new Scenario(numScenarios, seed.getValueInt(IDS_NUM_SEGMENT), seed.getValueInt(IDS_NUM_PERIOD));
            scenarioInfos = new ArrayList<>(numScenarios);

            // Preparing for loop
            int scenarioIndex = 0;

            // Creating scenario infos and assiging demand CAFs
            for (int month = 0; month < 12; ++month) {  // Month Loop
                for (int day = 0; day < 7; ++day) {  // Day loop, for active days only
                    //Only create scenario if demand pattern is active
                    if (numDaysInMonthAP[month * 7 + day] > 0) {
                        CEDate seedDate = seed.getSeedFileDate();
                        float seedDateAdjFactor = demandData_GP.getValue(seedDate.month - 1, seedDate.dayOfWeek()); // Subtract 1 from month to correct indexing, dayOfWeekMethod returns correct day index
                        float curr_sOAF = demandData_GP.getValue(month, day) / seedDateAdjFactor; //Calculationg the sOAF
                        float curr_sDAF = demandData_GP.getValue(month, day) / seedDateAdjFactor; //Calculationg the sDAF
                        for (int replicate = 0; replicate < numReplications; ++replicate) { //replication loop
                            scenario.OAF().set(curr_sOAF, scenarioIndex, 0, 0, scenarioIndex, seed.getValueInt(IDS_NUM_SEGMENT) - 1, seed.getValueInt(IDS_NUM_PERIOD) - 1);
                            scenario.DAF().set(curr_sDAF, scenarioIndex, 0, 0, scenarioIndex, seed.getValueInt(IDS_NUM_SEGMENT) - 1, seed.getValueInt(IDS_NUM_PERIOD) - 1);

                            scenarioInfos.add(new ScenarioInfo(probArray[month * 7 + day], scenarioIndex, CEDate.getMonthDayString(month + 1, day)));
                            scenarioInfos.get(scenarioInfos.size() - 1).setDemandMultiplier(curr_sDAF);
                            scenarioInfos.get(scenarioInfos.size() - 1).setSeed(seed);
                            scenarioInfos.get(scenarioInfos.size() - 1).month = month;
                            scenarioInfos.get(scenarioInfos.size() - 1).day = day;

                            scenarioIndex++;
                        }

                    }
                }

                monthChangeIdx[month] = scenarioIndex;  // Assigning index of last group occuring in month

            }

            // End of scenario creating and demand adjustment factor assignment
            MainWindow.printLog("Number of scenario created: " + scenarioInfos.size());

            // Assigning work zone events
            if (wzBool && workZoneData_GP != null) {
                assignWorkZones();
            }

            // Assiging weather events
            if (weatherBool) {
                createAndAssignWeatherEvents(monthChangeIdx);
            }

            // Proceed to incident modeling
            if (incidentBool_GP) {
                resetRNG(15);
                int[][] tempArr2 = createAndAssignListIncidentEventsGP(monthChangeIdx);
            }

            // Managed Lane
            if (mlUsed) {

                mlScenario = new Scenario(numScenarios, seed.getValueInt(IDS_NUM_SEGMENT), seed.getValueInt(IDS_NUM_PERIOD));
                scenarioIndex = 0;

                // Assiging DAFs and OAFs
                for (int month = 0; month < 12; ++month) {  // Month Loop
                    for (int day = 0; day < 7; ++day) {  // Day loop, for active days only
                        //Only create scenario if demand pattern is active
                        if (numDaysInMonthAP[month * 7 + day] > 0) {
                            CEDate seedDate = seed.getSeedFileDate();
                            float seedDateAdjFactor = demandData_ML.getValue(seedDate.month - 1, seedDate.dayOfWeek()); // Subtract 1 from month to correct indexing, dayOfWeekMethod returns correct day index
                            float curr_sOAF = demandData_ML.getValue(month, day) / seedDateAdjFactor; //Calculationg the sOAF
                            float curr_sDAF = demandData_ML.getValue(month, day) / seedDateAdjFactor; //Calculationg the sDAF
                            for (int replicate = 0; replicate < numReplications; ++replicate) { //replication loop
                                mlScenario.OAF().set(curr_sOAF, scenarioIndex, 0, 0, scenarioIndex, seed.getValueInt(IDS_NUM_SEGMENT) - 1, seed.getValueInt(IDS_NUM_PERIOD) - 1);
                                mlScenario.DAF().set(curr_sDAF, scenarioIndex, 0, 0, scenarioIndex, seed.getValueInt(IDS_NUM_SEGMENT) - 1, seed.getValueInt(IDS_NUM_PERIOD) - 1);

                                scenarioIndex++;
                            }
                        }
                    }

                }

                // Check if ML incidents
                if (incidentBool_ML) {
                    resetRNG(30);
                    createAndAssignListIncidentEventsML(monthChangeIdx);
                } // End managed lane incident assignment
            } // End managed lane scenario generation
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Error creating scenarios, please check inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
        // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Work Zones">
    /**
     * Method that assigns all work zones (GP only) for the RRP. Requires that
     * work zones are turned on (includeWorkZones is set to true), and a valid
     * list of work zones (via setWorkZoneDataGP) is provided by the user.
     */
    private void assignWorkZones() {
        int numWorkZones = workZoneData_GP.size();

        for (int wzIdx = 0; wzIdx < numWorkZones; wzIdx++) {
            WorkZoneData currWZ = workZoneData_GP.get(wzIdx);
            int startMonth = currWZ.getStartDate().month;  // Month indexing starts at 1 (i.e. 1 implies january)
            int startDay = currWZ.getStartDate().day;
            int endMonth = currWZ.getEndDate().month;
            int endDay = currWZ.getEndDate().day;

            int[] wzDaysArray = CEDate.numDayOfWeekInMonthAP(demandData_GP.getYear(), startMonth, startDay, endMonth, endDay, demandData_GP.getActiveDays());
            int[] fullDayOfWeekInMonth = CEDate.numDayOfWeekInMonthAP(demandData_GP.getYear(), demandData_GP.getActiveDays());

            // Post-processing of array
            for (int i = 0; i < wzDaysArray.length; i++) {
                if (fullDayOfWeekInMonth[i] != 0) {
                    wzDaysArray[i] = Math.round((wzDaysArray[i] / fullDayOfWeekInMonth[i]) * numReplications);
                } else {
                    wzDaysArray[i] = 0;
                }
            }

            int currDayType;
            int currMonth;
            for (int scenIdx = 0; scenIdx < scenarioInfos.size(); scenIdx++) {
                ScenarioInfo scen = scenarioInfos.get(scenIdx);
                currDayType = scen.getDayType();
                currMonth = scen.getMonth(); // Month indexing starts at 0 so no need to decrement

                if (wzDaysArray[(currMonth * 7) + currDayType] > 0) {
                    // Add to currScenarioInfo and Scenario
                    scen.addWorkZone(currWZ);

                    // Updating scenario
                    for (int seg = currWZ.getStartSegment() - 1; seg < currWZ.getEndSegment(); seg++) {
                        scenario.CAF().set(WorkZoneData.getCAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
                        scenario.OAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
                        scenario.DAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
                        scenario.SAF().set(WorkZoneData.getFFSAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
                        scenario.LAFWZ().set(WorkZoneData.getLAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
                    }

                    // Decrementing as work zone is assigned
                    wzDaysArray[(currMonth * 7) + currDayType] -= 1;
                }
            }
        }
    }
        // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Weather Modeling">
    /**
     * Creates the List of Weather Events (LWE) and assigns the weather events
     * to the reliability scenarios. Follows the Hybrid Scenario Generation
     * methodology. Should only be called during the scenario generation
     * process.
     *
     * @param monthChangeIdx List of indexes where each month ends (created
     * automatically in the generateScenarios method)
     */
    private void createAndAssignWeatherEvents(int[] monthChangeIdx) {
        //  Begin modeling of weather events

        // Step 6 - Group scenarios by month
        // Information stored in monthChangeIdx
        // Step 7 - Compute expected frequency of weather events by month
        int[][] expFreqWT = computeExpFreqOfWeatherEvents(monthChangeIdx);

        // Begin weather event assignment loop
        // Creating counters and marker variables for loops
        int numScenInPrevMonth = 0;
        int numScenInMonth;

        // Step 8 - Select a month to which whether events are not yet assigned
        for (int month = 0; month < 12; month++) {
                //Step 9 - Update (create) list of weather events
            // this is known implicitly from expFreqWT, weatherData.getAvgDurRoundedTo15MinIncrHour, table CAFs and FFSAFs

            // Creating array to hold scenario probabilities of the current month
            numScenInMonth = (monthChangeIdx[month] - numScenInPrevMonth);
            if (numScenInMonth > 0) {
                float[] currMonthScenProb = new float[numScenInMonth];

                // Loop to extract probabilities for all scenarios in the current month
                for (int i = 0; i < numScenInMonth; i++) {
                    currMonthScenProb[i] = scenarioInfos.get(numScenInPrevMonth + i).prob;
                }

                //getting the cumulative sum of probablities and normalizing
                float[] currMonthScenCumProb = cumSumNormalize(currMonthScenProb);

                // Steps 10 - From the LWE of the current month, select a weather event and assign a scenario and start time
                assignCurrMonthWeatherEvents(expFreqWT[month], currMonthScenCumProb, numScenInPrevMonth);
                numScenInPrevMonth = monthChangeIdx[month];
                //prevScenarioInfoMarker = scenarioInfoMarker;
            }
        } // Step 14 - Weather events for all months are assigned
        MainWindow.printLog(arraySum(expFreqWT) + " weather events assigned");
    }

    /**
     * Method to calculate the expected frequency of events of each weather type
     * occur in each month. Uses data calculated from the local weather
     * database.
     *
     * @param monthChangeIdx Array containing the final group number of
     * scenarios in each month. monthChangeIdx[0] should be the group number of
     * the final set of scenarios in January.
     * @return expFreqWT - float[12][numWT] array containing the expected
     * frequencies of each type of weather event for each month. First index
     * denotes month, second denotes weather type.
     */
    private int[][] computeExpFreqOfWeatherEvents(int[] monthChangeIdx) {

        // Creating necessary variables
        int numWT = weatherData.getNumWeatherTypes();
        int prevNumGroups = 1;
        int numScenInMonth;

        // Creating array to hold probabilities
        int[][] expFreqWT = new int[12][numWT];

        // Generating expected frequencies
        for (int month = 0; month < 12; month++) {                           // For each month
            numScenInMonth = (monthChangeIdx[month] - prevNumGroups);          // Get number of scenarios in month [N(j,scen)]
            for (int weatherType = 0; weatherType < numWT; weatherType++) {      // For each weatherType
                if (Math.pow((weatherData.getAvgDurRoundedTo15MinIncrHour(weatherType) - 0.0f), 2) < 10e-12) {  // If weatherType has nonzero duration
                    expFreqWT[month][weatherType] = 0;                         // If 0 duration, set expected frequency to 0
                } else {                                                       // Else, if nonzero duration
                    expFreqWT[month][weatherType] = (int) Math.round(// Compute expected frequency: Round[(Pt(w,j)*Dsp*N(j,scen))/E15[Dw]]
                            (weatherData.getProbabilityDecimal(month, weatherType) * dailyStudyPeriodDuration
                            * numScenInMonth) / weatherData.getAvgDurRoundedTo15MinIncrHour(weatherType));
                }
            }
            prevNumGroups = monthChangeIdx[month];
        }
        return expFreqWT;
    }

    /**
     * Method to assign the list of weather events (LWE) for the current month
     * to scenarios associated with the current month. Assigns each weather
     * event to a random scenario, and gives each event a random start time.
     * Encompasses steps 10-13 of the scenario generation workflow.
     *
     * @param expMonthFreqWT expexted frequency of each weather type in each
     * month
     * @param currMonthScenCumProb float[] holding the cumulative sum array of
     * scenario probabilities for the current month.
     * @param prevScenarioInfoMarker Marker for iterating through the
     * scenarioInfos
     */
    private void assignCurrMonthWeatherEvents(int[] expMonthFreqWT, float[] currMonthScenCumProb, int prevScenarioInfoMarker) {

        //Intializing holders for random numbers, start times, and scenarios assignments
        double sTimeRand;
        double scenRand;
        int sTime = 0;
        int scenarioIdx = 0;
        boolean scenarioFound;
        ScenarioInfo currScenarioInfo = new ScenarioInfo();

        for (int weatherType = 0; weatherType < expMonthFreqWT.length; weatherType++) {
            int weatherTypeDuration = Math.min(weatherData.getAvgDurRoundedTo15MinIncrNumIncr(weatherType), seed.getValueInt(IDS_NUM_PERIOD));
            for (int event = 0; event < expMonthFreqWT[weatherType]; event++) {

                scenarioFound = false;
                while (!scenarioFound) {

                    // Calculating random start time
                    sTimeRand = RNG.nextDouble();                             //RNG Changed
                    sTime = (int) Math.round(sTimeRand * dailyStudyPeriodDuration * 4.0f);           // Number of 15 minute increments past beginning of SP

                    //Determing scenario to which event is assigned
                    scenRand = RNG.nextDouble();                              //RNG Changed
                    scenarioIdx = 0;
                    while (currMonthScenCumProb[scenarioIdx] < scenRand) {
                        scenarioIdx++;
                    }

                    // Retrieving ScenarioInfo
                    currScenarioInfo = scenarioInfos.get(prevScenarioInfoMarker + scenarioIdx);

                    //Check to see if event will overlap with a previoulsy assigned event
                    if (currScenarioInfo.hasWeatherEvent()) {
                        scenarioFound = !(currScenarioInfo.checkWeatherOverlap(sTime, weatherTypeDuration));
                    } else {
                        scenarioFound = true;
                    }
                }
                currScenarioInfo.addWeatherEvent(weatherType, sTime, weatherTypeDuration);

                if (sTime + weatherTypeDuration <= seed.getValueInt(IDS_NUM_PERIOD)) {
                    scenario.CAF().multiply(weatherData.getAdjustmentFactor(0, weatherType),
                            (prevScenarioInfoMarker + scenarioIdx), 0, sTime,
                            (prevScenarioInfoMarker + scenarioIdx), seed.getValueInt(IDS_NUM_SEGMENT) - 1,
                            sTime + weatherTypeDuration - 1);

                    scenario.SAF().multiply(weatherData.getAdjustmentFactor(1, weatherType),
                            (prevScenarioInfoMarker + scenarioIdx), 0, sTime,
                            (prevScenarioInfoMarker + scenarioIdx), seed.getValueInt(IDS_NUM_SEGMENT) - 1,
                            sTime + weatherTypeDuration - 1);
                } else {
                    // CAF
                    // Assigning up to end of RRP
                    scenario.CAF().multiply(weatherData.getAdjustmentFactor(0, weatherType),
                            (prevScenarioInfoMarker + scenarioIdx), 0, sTime,
                            (prevScenarioInfoMarker + scenarioIdx), seed.getValueInt(IDS_NUM_SEGMENT) - 1,
                            seed.getValueInt(IDS_NUM_PERIOD) - 1);
                    // Wrapping back to beginning of RRP
                    scenario.CAF().multiply(weatherData.getAdjustmentFactor(0, weatherType),
                            (prevScenarioInfoMarker + scenarioIdx), 0, 0,
                            (prevScenarioInfoMarker + scenarioIdx), seed.getValueInt(IDS_NUM_SEGMENT) - 1,
                            ((sTime + weatherTypeDuration) % seed.getValueInt(IDS_NUM_PERIOD)) - 1);

                    // SAF
                    // Assigning up to end of RRP
                    scenario.SAF().multiply(weatherData.getAdjustmentFactor(1, weatherType),
                            (prevScenarioInfoMarker + scenarioIdx), 0, sTime,
                            (prevScenarioInfoMarker + scenarioIdx), seed.getValueInt(IDS_NUM_SEGMENT) - 1,
                            seed.getValueInt(IDS_NUM_PERIOD) - 1);
                    // Wrapping back to beginning of RRP
                    scenario.SAF().multiply(weatherData.getAdjustmentFactor(1, weatherType),
                            (prevScenarioInfoMarker + scenarioIdx), 0, 0,
                            (prevScenarioInfoMarker + scenarioIdx), seed.getValueInt(IDS_NUM_SEGMENT) - 1,
                            ((sTime + weatherTypeDuration) % seed.getValueInt(IDS_NUM_PERIOD)) - 1);
                }
            }
        }
    }
        //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Incident Modeling">
    /**
     * Method to create and assign the List of Incident Events (LIE) for GP
     * segments. Follows the methodology of the Hybrid Scenario Generation
     * method. Should only be called in the scenario generation process.
     *
     * @param monthChangeIdx Indices at which the scenarios change month, i.e.
     * the value of monthChangeIdx[0] is the index of the first scenario of
     * February.
     * @return Array (of arrays) representing the LIE from the Hybrid Scenario
     * Generation Procedure.
     */
    private int[][] createAndAssignListIncidentEventsGP(int[] monthChangeIdx) {

        // Creating counters and marker variables for loops
        int numScenInMonth;
        int totalIncidentsInMonth;
        int totalNumOfIncForRRP = 0;
        float ub; // upperbound for binary search
        float lb; // lower bound for binary search
        float[] adjParamArr1 = {1.01f,
            1.00f,
            1.00f,
            1.00f,
            1.00f,
            1.10f,
            1.04f,
            1.10f,
            1.064f,
            1.04f,
            1.04f,
            1.0f};

        //Random randomGenerator = new Random();                            // RNG Changed
        // Finding total number of incident events that will be assigned
        for (int month = 0; month < 12; month++) {
            totalIncidentsInMonth = 0;
            // Computing the number of scenarios in the current month
            numScenInMonth = month == 0 ? monthChangeIdx[month] : (monthChangeIdx[month] - monthChangeIdx[month - 1]);

            ArrayList<Integer> incidents;

            // setting up binary search for adjustment parameter
            ub = 1.50f;
            lb = 0.50f;
            int incSum = 0;

            while (numScenInMonth != incSum) {
                // Generate poisson dist values with mean (lambda) of n_j
                totalIncidentsInMonth = 0;
                incidents = new ArrayList<>();
                boolean notZero = true;
                int k = 0;
                while (notZero || k < 7) {
                    float adjParam = adjParamArr1[month];
                    float lambda = incidentData_GP.getIncidentFrequencyMonth(month);
                    float poissonValue = ((float) (Math.pow(lambda, k) * Math.exp(-1.0 * lambda))) / factorial(k);
                    int numScenWithKEvents = Math.round(adjParam * numScenInMonth * poissonValue); // Number of scenarios with k events
                    incidents.add(numScenWithKEvents);

                    if (numScenWithKEvents == 0 && k > lambda) {
                        notZero = false;
                    }

                    totalIncidentsInMonth += k * numScenWithKEvents;

                    k++;
                }

                incSum = arraySum(incidents);

                if (numScenInMonth > incSum) { // Adjustment parameter must be bigger
                    lb = adjParamArr1[month];
                    adjParamArr1[month] = (ub + adjParamArr1[month]) / 2.0f;
                } else if (numScenInMonth < incSum) {
                    ub = adjParamArr1[month];
                    adjParamArr1[month] = (lb + adjParamArr1[month]) / 2.0f;
                }
            }

            totalNumOfIncForRRP += totalIncidentsInMonth;

        }

        int[][] listIncidentEvents = new int[totalNumOfIncForRRP][5];

        // Resetting important varaibles
        int prevNumScenarioAssignedMarker = 0;
        int currIdxLIE = 0;

        // Step 15 - Select a month that incident events are not yet assigned
        for (int month = 0; month < 12; month++) {
            totalIncidentsInMonth = 0;
            //Step 16 - Compute n_j = expected frequency of all incidents per study period in month j
            // this is done previously, stored in IncidentData.incidentFreqMonth

            // Computing the number of scenarios in the current month
            numScenInMonth = month == 0 ? monthChangeIdx[month] : (monthChangeIdx[month] - monthChangeIdx[month - 1]);  // KEEP

            // Step 17 - generate a set of incident frequencies for all scenarios in the current month
            // Generate poisson dist values with mean (lambda) of n_j
            ArrayList<Integer> incidents = new ArrayList<>();
            boolean notZero = true;
            int k = 0;
            while (notZero || k < 7) {
                float adjParam = adjParamArr1[month];
                float lambda = incidentData_GP.getIncidentFrequencyMonth(month);
                float poissonValue = ((float) (Math.pow(lambda, k) * Math.exp(-1.0 * lambda))) / factorial(k);
                int numScenWithKEvents = Math.round(adjParam * numScenInMonth * poissonValue); // Number of scenarios with k events
                incidents.add(numScenWithKEvents);

                if (numScenWithKEvents == 0 && k > lambda) {
                    notZero = false;
                }

                totalIncidentsInMonth += k * numScenWithKEvents;

                k++;
            } // End step 17

            // Step 18 - Randomly assign each genearted incident to a scenario in current month
            // This table (table 12) is implicitly generated in the following step
            // Step 19 - Update the list of incident events (LIE)
            ArrayList<Integer> tempArr = createIntegerArrAscending(numScenInMonth);
            int[] numIncidentEventsToScenarioMapper = new int[numScenInMonth];
            int currIdx = 0;

            for (int numOfIncidents = 0; numOfIncidents < incidents.size(); numOfIncidents++) {
                for (int numOfScenarios = 0; numOfScenarios < incidents.get(numOfIncidents); numOfScenarios++) {
                    int randIdx = RNG.nextInt(tempArr.size());                // RNG Changed
                    numIncidentEventsToScenarioMapper[currIdx] = tempArr.remove(randIdx) + prevNumScenarioAssignedMarker;
                    for (int incident = 0; incident < numOfIncidents; incident++) {
                        listIncidentEvents[currIdxLIE][0] = numIncidentEventsToScenarioMapper[currIdx];
                        currIdxLIE++;
                    }
                    currIdx++;
                }
            }

            prevNumScenarioAssignedMarker = monthChangeIdx[month];
        }

        // Step 21 - Generate Incident Severities for each incident event
        // The distribution is determined by the user and is accessed via incidentData.getIncidentDistribution(incidentType)
        int[] totalNumIncOfEachType = new int[5];
        float adjParam2 = 1.0f;
        ub = 1.5f;
        lb = .05f;
        int incSum = 0;
        while (totalNumOfIncForRRP != incSum) {
            for (int incType = 0; incType < 5; incType++) {
                totalNumIncOfEachType[incType] = Math.round(adjParam2 * incidentData_GP.getIncidentDistributionDecimal(incType) * totalNumOfIncForRRP);
            }
            incSum = arraySum(totalNumIncOfEachType);
            if (totalNumOfIncForRRP > incSum) { // Adjustment parameter must be higher
                lb = adjParam2;
                adjParam2 = (ub + adjParam2) / 2.0f;
            } else if (totalNumOfIncForRRP < incSum) { //Adjustment parameter must be lower
                ub = adjParam2;
                adjParam2 = (lb + adjParam2) / 2.0f;
            }
        }

        // Step 22 - Randomly assign an incident severity to each incident
        int randIdx;
        ArrayList<Integer> tempArr = createIntegerArrAscending(totalNumOfIncForRRP);
        for (int incType = 0; incType < 5; incType++) {
            for (int incidentNum = 0; incidentNum < totalNumIncOfEachType[incType]; incidentNum++) {
                randIdx = RNG.nextInt(tempArr.size());                    // RNG Changed
                listIncidentEvents[tempArr.remove(randIdx)][1] = incType;
            }
        }

        // Step 23 - Generate incident durations by incident severity
        // Step 24 - Randomly assign incident durations by severity
        int[][] tempDurs = getNumScenAssignedDurationLength(totalNumIncOfEachType);  // Binary search of adjParam3 (adjParamArr3) here
        ArrayList<Integer> distArr1 = createIntegerArrDist(tempDurs[0]);
        ArrayList<Integer> distArr2 = createIntegerArrDist(tempDurs[1]);
        ArrayList<Integer> distArr3 = createIntegerArrDist(tempDurs[2]);
        ArrayList<Integer> distArr4 = createIntegerArrDist(tempDurs[3]);
        ArrayList<Integer> distArr5 = createIntegerArrDist(tempDurs[4]);
        for (int incidentNum = 0; incidentNum < totalNumOfIncForRRP; incidentNum++) {
            int currIncType = listIncidentEvents[incidentNum][1];
            //float currLNRand = generateLogNormalValue(randomGenerator, incidentData.getIncidentDuration(currIncType),incidentData.getIncidentDurationStdDev(currIncType));
            //int currLNDur = generateLogNormalDuration(currLNRand, incidentData.getIncidentDurMin(currIncType), incidentData.getIncidentDurMax(currIncType));
            //listIncidentEvents[incidentNum][3]=currLNDur;
            switch (currIncType) {
                case 0:
                    listIncidentEvents[incidentNum][3] = 15 * distArr1.remove(RNG.nextInt(distArr1.size()));   // RNG Changed
                    break;
                case 1:
                    listIncidentEvents[incidentNum][3] = 15 * distArr2.remove(RNG.nextInt(distArr2.size()));   // RNG Changed
                    break;
                case 2:
                    listIncidentEvents[incidentNum][3] = 15 * distArr3.remove(RNG.nextInt(distArr3.size()));   // RNG Changed
                    break;
                case 3:
                    listIncidentEvents[incidentNum][3] = 15 * distArr4.remove(RNG.nextInt(distArr4.size()));   // RNG Changed
                    break;
                case 4:
                    listIncidentEvents[incidentNum][3] = 15 * distArr5.remove(RNG.nextInt(distArr5.size()));   // RNG Changed
                    break;

            }

        }

        // Step 25 - Generate distribution of incident start times and location
        float[] probSegLocation = new float[seed.getValueInt(IDS_NUM_SEGMENT)];
        float totalVMTSum = seed.getValueFloat(CEConst.IDS_SP_VMTV); // Denotes sum of VMT over all segments, all periods

        // Generate probability of segment in which the incident occurs
        for (int seg = 0; seg < probSegLocation.length; seg++) {
            probSegLocation[seg] = seed.getValueFloat(CEConst.IDS_S_VMTV, seg, 0) / totalVMTSum;
        }

        // Generate probability of period in which the incident occurs
        float[] probPeriodOccur = new float[seed.getValueInt(IDS_NUM_PERIOD)];

        for (int period = 0; period < seed.getValueInt(IDS_NUM_PERIOD); period++) {
            probPeriodOccur[period] = seed.getValueFloat(CEConst.IDS_P_VMTV, 0, period, 0, -1) / totalVMTSum;
        }

        // Step 26 - Generate incident start times and locations for all incidents
        int[] numIncAssignedToSegment = new int[probSegLocation.length];
        float adjParam4 = 1.0014f;

        // Setting up parameters for binary search
        ub = 1.5f;
        lb = 0.5f;
        incSum = 0;
        int numIter = 0;

        while (listIncidentEvents.length != incSum && numIter < 200) {
            for (int seg = 0; seg < numIncAssignedToSegment.length; seg++) {
                numIncAssignedToSegment[seg] = Math.round(adjParam4 * listIncidentEvents.length * probSegLocation[seg]);
            }

            incSum = arraySum(numIncAssignedToSegment);
            numIter++;

            // Checking sums and updating binary search if necessary
            if (incSum > listIncidentEvents.length) {                     // adjustment parameter is too high
                ub = adjParam4;
                adjParam4 = (adjParam4 + lb) / 2.0f;

            } else if (incSum < listIncidentEvents.length) {           // adjustment parameter is too low
                lb = adjParam4;
                adjParam4 = (adjParam4 + ub) / 2.0f;
            }
        }

        if (listIncidentEvents.length != incSum && numIter >= 200) {
            int diff = listIncidentEvents.length - arraySum(numIncAssignedToSegment);
            int idx = argMax(numIncAssignedToSegment);
            numIncAssignedToSegment[idx] += diff;
            System.out.println("Incident inflation/reduction of " + diff + " invoked for incident segment locations.");
        }

        int[] numIncAssignedToStartInAP = new int[seed.getValueInt(IDS_NUM_PERIOD)];
        float adjParam5 = 1.0014f;

        // Setting up parameters for binary search
        ub = 1.5f;
        lb = 0.5f;
        incSum = 0;
        numIter = 0;

        while (listIncidentEvents.length != incSum && numIter < 200) {
            for (int period = 0; period < numIncAssignedToStartInAP.length; period++) {
                numIncAssignedToStartInAP[period] = Math.round(adjParam5 * listIncidentEvents.length * probPeriodOccur[period]);
            }

            incSum = arraySum(numIncAssignedToStartInAP);
            numIter++;

            // Checking sums and updating binary search if necessary
            if (incSum > listIncidentEvents.length) {                     // adjustment parameter is too high
                ub = adjParam5;
                adjParam5 = (adjParam5 + lb) / 2.0f;

            } else if (incSum < listIncidentEvents.length) {           // adjustment parameter is too low
                lb = adjParam5;
                adjParam5 = (adjParam5 + ub) / 2.0f;
            }
        }

        if (listIncidentEvents.length != incSum && numIter >= 200) {
            int diff = listIncidentEvents.length - arraySum(numIncAssignedToStartInAP);
            int idx = argMax(numIncAssignedToStartInAP);
            numIncAssignedToStartInAP[idx] += diff;
            System.out.println("Incident inflation/reduction of " + diff + " invoked for incident segment locations.");
        }

        // Step 27 - From LIE, select an incident who start-time and location have not been assigned, and
        //           randomly assign a start time and location from the previous step
        //ArrayList<Integer> selectionArrEvents = createIntegerArrAscending(listIncidentEvents.length);
        ArrayList<Integer> selectionArrSTimes = createIntegerArrDist(numIncAssignedToStartInAP);
        ArrayList<Integer> selectionArrLoc = createIntegerArrDist(numIncAssignedToSegment);

        // Shuffling the arrays into a random order for assignment
        //Collections.shuffle(selectionArrEvents, rng);
        Collections.shuffle(selectionArrSTimes, RNG);
        Collections.shuffle(selectionArrLoc, RNG);

        // Preprocessing array so that incidents of higher severity are assigned first
        ArrayList<IncidentInfo> incInfo = new ArrayList<>();
        for (int idx = 0; idx < listIncidentEvents.length; idx++) {
            incInfo.add(new IncidentInfo(idx, listIncidentEvents[idx][1]));
        }
        Collections.sort(incInfo);

        ArrayList<Integer> selectionArrEvents = new ArrayList<>();
        for (int idx = incInfo.size() - 1; idx >= 0; idx--) {
            selectionArrEvents.add(incInfo.get(idx).incIdx);
            //System.out.println(incInfo.get(idx).severity);
        }

        // Setting up variables for loops
        //int randSTimeIdx;         // Variable to hold randomly generated index for selection of starting AP
        int startAP;              // Variable to hold starting AP for incident
        //int randLocIdx;           // Variable to hold randomly generated index for selection of location
        int segmentNum = 0;           // Variable to hold location of incident
        int numPeriods = seed.getValueInt(IDS_NUM_PERIOD);

        int incidentIdx;
        ScenarioInfo currScenarioInfo;
        //Scenario currScenario;
        boolean overlap;
        boolean isValidLocation;
        int counter = 0;
        int locCounter;
        int numRandomizations = 0;
        int maxRandomizations = 5;
        int numUnassignedIncidents = 0;
        while (selectionArrEvents.size() > 0) {
            // Randomly select incident
            //randIdx = rng.nextInt(selectionArrEvents.size());
            incidentIdx = selectionArrEvents.get(0); // index of incident event that has not been assigned a start time and duration

            // Generate random start time and location
            //randSTimeIdx = rng.nextInt(selectionArrSTimes.size());
            startAP = selectionArrSTimes.get(counter);

            // Search for a valid location
            isValidLocation = false;
            locCounter = 0;
            while (!isValidLocation && (locCounter < selectionArrLoc.size())) {
                //randLocIdx = rng.nextInt(selectionArrLoc.size());
                segmentNum = selectionArrLoc.get(locCounter);
                // Checking to see if location is valid (i.e. severity does not exceed segment lanes)
                if (seed.getValueInt(IDS_MAIN_NUM_LANES_IN, segmentNum) <= listIncidentEvents[incidentIdx][1]) {
                    locCounter++;
                    //System.out.println("Looking for new incident location.");
                } else {
                    isValidLocation = true;
                }
            }
            currScenarioInfo = scenarioInfos.get(listIncidentEvents[incidentIdx][0]);
            overlap = currScenarioInfo.checkGPIncidentOverlap(startAP, listIncidentEvents[incidentIdx][3] / 15, segmentNum);
            if (!isValidLocation) { // No valid location can be found for the incident
                System.out.println("Type 1 - Incident: " + selectionArrEvents.get(0) + ", Severity" + listIncidentEvents[selectionArrEvents.get(0)][1]);
                numUnassignedIncidents++;
                selectionArrEvents.remove(0);
            } else if (overlap) { // Current location/time period overlaps with another incident
                counter++; // Effectively move to new random start time
                if (counter == selectionArrEvents.size() && selectionArrEvents.size() > 0) {
                    // If all potential time periods checked, re-randomize events, locations, and start times
                    if (numRandomizations < maxRandomizations) {
                        numRandomizations++;
                        Collections.shuffle(selectionArrEvents, RNG);
                        Collections.shuffle(selectionArrLoc, RNG);
                        Collections.shuffle(selectionArrSTimes, RNG);
                        counter = 0;
                    } else { // If re-randomizaiton fails 5 times, all remaining events will be discarded
                        numUnassignedIncidents += selectionArrEvents.size();
                        for (int loi = 0; loi < selectionArrEvents.size(); loi++) {
                            int tempInc = selectionArrEvents.get(loi);
                            System.out.println("Type 2 - Incident: " + tempInc + ", Severity:" + listIncidentEvents[tempInc][1]
                                    + ", Scen: " + currScenarioInfo.group + ", Seg: " + selectionArrLoc.get(loi) + ", STime: " + selectionArrSTimes.get(loi)
                                    + ", Dur: " + listIncidentEvents[tempInc][3]);
                        }
                        break;
                    }
                }
            } else { // Found valid location with no overlap
                incidentIdx = selectionArrEvents.remove(0); //selects and removes incident from list of unassigned incidents
                selectionArrSTimes.remove(counter);
                selectionArrLoc.remove(locCounter);
                counter = 0;
                listIncidentEvents[incidentIdx][2] = startAP;
                listIncidentEvents[incidentIdx][4] = segmentNum;

                // Add to currScenarioInfo and Scenario
                currScenarioInfo.addIncidentGP(listIncidentEvents[incidentIdx][1], // Incident type (severity)
                        listIncidentEvents[incidentIdx][2], // Incdient start period
                        (listIncidentEvents[incidentIdx][3] / 15), // Incident duration (in Ap's)
                        listIncidentEvents[incidentIdx][4]);   // Incident location

                // Updating scenario
                int numLanes = seed.getValueInt(IDS_MAIN_NUM_LANES_IN, listIncidentEvents[incidentIdx][4]);
                if (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15) <= numPeriods) {   // Changes < to <=
                    scenario.CAF().multiply(incidentData_GP.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    scenario.OAF().multiply(incidentData_GP.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    scenario.DAF().multiply(incidentData_GP.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    scenario.SAF().multiply(incidentData_GP.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                } else {
                    int endPeriod = (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15) - 1) % numPeriods;
                    scenario.CAF().multiply(incidentData_GP.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    scenario.CAF().multiply(incidentData_GP.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    scenario.OAF().multiply(incidentData_GP.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    scenario.OAF().multiply(incidentData_GP.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    scenario.DAF().multiply(incidentData_GP.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    scenario.DAF().multiply(incidentData_GP.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    scenario.SAF().multiply(incidentData_GP.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    scenario.SAF().multiply(incidentData_GP.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                }

                // update sCAF, sSAF, sOAF,SDaf and sLAF for all affected periods
                int adjPeriod;
                for (int period = 0; period < listIncidentEvents[incidentIdx][3] / 15; period++) {
                    adjPeriod = (listIncidentEvents[incidentIdx][2] + period) % seed.getValueInt(IDS_NUM_PERIOD);
                    int oldLAF = scenario.LAFI().get(listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                    int currWZLAF = scenario.LAFWZ().get(listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                    int newLAF = Math.max(oldLAF + incidentData_GP.getIncidentLAF(listIncidentEvents[incidentIdx][1], numLanes - 2), -1 * (numLanes + currWZLAF - 1));
                    scenario.LAFI().add(newLAF, listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                }
            }

        }
        if (numUnassignedIncidents > 0) {
            JOptionPane.showMessageDialog(null, "<HTML><CENTER>Failed to assign " + numUnassignedIncidents + " incident(s).<br>&nbsp<br>"
                    + "If the number of unassigned incidents is high, please ensure the inputs<br>"
                    + "(incidents frequencies, distributions, etc.) are valid<br>"
                    + "or retry Scenario Generation with a new random number generator seed.");
        }
        MainWindow.printLog((listIncidentEvents.length - numUnassignedIncidents) + " incident events assigned to scenarios.");
        return listIncidentEvents;
    }

    /**
     * Method to create and assign the List of Incident Events (LIE) for ML
     * segments. Follows the methodology of the Hybrid Scenario Generation
     * method. Should only be called in the scenario generation process.
     *
     * @param monthChangeIdx Indices at which the scenarios change month, i.e.
     * the value of monthChangeIdx[0] is the index of the first scenario of
     * February.
     * @return Array (of arrays) representing the LIE from the Hybrid Scenario
     * Generation Procedure.
     */
    private int[][] createAndAssignListIncidentEventsML(int[] monthChangeIdx) {

        // Creating counters and marker variables for loops
        int numScenInMonth;
        int totalIncidentsInMonth;
        int totalNumOfIncForRRP = 0;
        float ub; // upperbound for binary search
        float lb; // lower bound for binary search
        float[] adjParamArr1 = {1.01f,
            1.00f,
            1.00f,
            1.00f,
            1.00f,
            1.10f,
            1.04f,
            1.10f,
            1.064f,
            1.04f,
            1.04f,
            1.0f};

        // Finding total number of incident events that will be assigned
        for (int month = 0; month < 12; month++) {
            totalIncidentsInMonth = 0;
            // Computing the number of scenarios in the current month
            numScenInMonth = month == 0 ? monthChangeIdx[month] : (monthChangeIdx[month] - monthChangeIdx[month - 1]);

            ArrayList<Integer> incidents;

            // setting up binary search for adjustment parameter
            ub = 1.50f;
            lb = 0.50f;
            int incSum = 0;

            while (numScenInMonth != incSum) {
                // Generate poisson dist values with mean (lambda) of n_j
                totalIncidentsInMonth = 0;
                incidents = new ArrayList<>();
                boolean notZero = true;
                int k = 0;
                while (notZero || k < 7) {
                    float adjParam = adjParamArr1[month];
                    float lambda = incidentData_ML.getIncidentFrequencyMonth(month);
                    float poissonValue = ((float) (Math.pow(lambda, k) * Math.exp(-1.0 * lambda))) / factorial(k);
                    int numScenWithKEvents = Math.round(adjParam * numScenInMonth * poissonValue); // Number of scenarios with k events
                    incidents.add(numScenWithKEvents);

                    if (numScenWithKEvents == 0 && k > lambda) {
                        notZero = false;
                    }

                    totalIncidentsInMonth += k * numScenWithKEvents;

                    k++;
                }

                incSum = arraySum(incidents);

                if (numScenInMonth > incSum) { // Adjustment parameter must be bigger
                    lb = adjParamArr1[month];
                    adjParamArr1[month] = (ub + adjParamArr1[month]) / 2.0f;
                } else if (numScenInMonth < incSum) {
                    ub = adjParamArr1[month];
                    adjParamArr1[month] = (lb + adjParamArr1[month]) / 2.0f;
                }
            }

            totalNumOfIncForRRP += totalIncidentsInMonth;

        }

        int[][] listIncidentEvents = new int[totalNumOfIncForRRP][5];

        // Resetting important varaibles
        int prevNumScenarioAssignedMarker = 0;
        int currIdxLIE = 0;

        // Step 15 - Select a month that incident events are not yet assigned
        for (int month = 0; month < 12; month++) {
            totalIncidentsInMonth = 0;
            //Step 16 - Compute n_j = expected frequency of all incidents per study period in month j
            // this is done previously, stored in IncidentData.incidentFreqMonth

            // Computing the number of scenarios in the current month
            numScenInMonth = month == 0 ? monthChangeIdx[month] : (monthChangeIdx[month] - monthChangeIdx[month - 1]);  // KEEP

            // Step 17 - generate a set of incident frequencies for all scenarios in the current month
            // Generate poisson dist values with mean (lambda) of n_j
            ArrayList<Integer> incidents = new ArrayList<>();
            boolean notZero = true;
            int k = 0;
            while (notZero || k < 7) {
                float adjParam = adjParamArr1[month];
                float lambda = incidentData_ML.getIncidentFrequencyMonth(month);
                float poissonValue = ((float) (Math.pow(lambda, k) * Math.exp(-1.0 * lambda))) / factorial(k);
                int numScenWithKEvents = Math.round(adjParam * numScenInMonth * poissonValue); // Number of scenarios with k events
                incidents.add(numScenWithKEvents);

                if (numScenWithKEvents == 0 && k > lambda) {
                    notZero = false;
                }

                totalIncidentsInMonth += k * numScenWithKEvents;

                k++;
            } // End step 17

            // Step 18 - Randomly assign each genearted incident to a scenario in current month
            // This table (table 12) is implicitly generated in the following step
            // Step 19 - Update the list of incident events (LIE)
            ArrayList<Integer> tempArr = createIntegerArrAscending(numScenInMonth);
            int[] numIncidentEventsToScenarioMapper = new int[numScenInMonth];
            int currIdx = 0;

            for (int numOfIncidents = 0; numOfIncidents < incidents.size(); numOfIncidents++) {
                for (int numOfScenarios = 0; numOfScenarios < incidents.get(numOfIncidents); numOfScenarios++) {
                    int randIdx = RNG.nextInt(tempArr.size());                // RNG Changed
                    numIncidentEventsToScenarioMapper[currIdx] = tempArr.remove(randIdx) + prevNumScenarioAssignedMarker;
                    for (int incident = 0; incident < numOfIncidents; incident++) {
                        listIncidentEvents[currIdxLIE][0] = numIncidentEventsToScenarioMapper[currIdx];
                        currIdxLIE++;
                    }
                    currIdx++;
                }
            }

            prevNumScenarioAssignedMarker = monthChangeIdx[month];
        }

        // Step 21 - Generate Incident Severities for each incident event
        // The distribution is determined by the user and is accessed via incidentMLData.getIncidentDistribution(incidentType)
        // Pre-process incident distribution array and adjust for managed lanes
        // if values exist for infeasible incidents.
        int maxMLLanes = 0;
        for (int segIdx = 0; segIdx < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); segIdx++) {
            if (seed.getValueInt(CEConst.IDS_ML_NUM_LANES) > maxMLLanes) {
                maxMLLanes = seed.getValueInt(CEConst.IDS_ML_NUM_LANES);
            }
        }

        float[] adjustedIncidentSeverityDistribution = new float[5];
        float distSum = 0;
        for (int incidentSeverityType = 0; incidentSeverityType < maxMLLanes; incidentSeverityType++) {
            adjustedIncidentSeverityDistribution[incidentSeverityType] = incidentData_ML.getIncidentDistributionDecimal(incidentSeverityType);
            distSum += incidentData_ML.getIncidentDistributionDecimal(incidentSeverityType);
        }
        for (int incType = 0; incType < adjustedIncidentSeverityDistribution.length; incType++) {
            adjustedIncidentSeverityDistribution[incType] /= distSum;
        }

        int[] totalNumIncOfEachType = new int[5];
        float adjParam2 = 1.0f;
        ub = 1.5f;
        lb = .05f;
        int incSum = 0;
        while (totalNumOfIncForRRP != incSum) {
            for (int incType = 0; incType < 5; incType++) {
                totalNumIncOfEachType[incType] = Math.round(adjParam2 * adjustedIncidentSeverityDistribution[incType] * totalNumOfIncForRRP);
            }
            incSum = arraySum(totalNumIncOfEachType);
            if (totalNumOfIncForRRP > incSum) { // Adjustment parameter must be higher
                lb = adjParam2;
                adjParam2 = (ub + adjParam2) / 2.0f;
            } else if (totalNumOfIncForRRP < incSum) { //Adjustment parameter must be lower
                ub = adjParam2;
                adjParam2 = (lb + adjParam2) / 2.0f;
            }
        }

        // Step 22 - Randomly assign an incident severity to each incident
        int randIdx;
        ArrayList<Integer> tempArr = createIntegerArrAscending(totalNumOfIncForRRP);
        for (int incType = 0; incType < 5; incType++) {
            for (int incidentNum = 0; incidentNum < totalNumIncOfEachType[incType]; incidentNum++) {
                randIdx = RNG.nextInt(tempArr.size());                    // RNG Changed
                listIncidentEvents[tempArr.remove(randIdx)][1] = incType;
                // TODO: Check on incident severity
            }
        }

        // Step 23 - Generate incident durations by incident severity
        // Step 24 - Randomly assign incident durations by severity
        int[][] tempDurs = getNumScenAssignedDurationLength(totalNumIncOfEachType);  // Binary search of adjParam3 (adjParamArr3) here
        ArrayList<Integer> distArr1 = createIntegerArrDist(tempDurs[0]);
        ArrayList<Integer> distArr2 = createIntegerArrDist(tempDurs[1]);
        ArrayList<Integer> distArr3 = createIntegerArrDist(tempDurs[2]);
        ArrayList<Integer> distArr4 = createIntegerArrDist(tempDurs[3]);
        ArrayList<Integer> distArr5 = createIntegerArrDist(tempDurs[4]);
        for (int incidentNum = 0; incidentNum < totalNumOfIncForRRP; incidentNum++) {
            int currIncType = listIncidentEvents[incidentNum][1];
            //float currLNRand = generateLogNormalValue(randomGenerator, incidentMLData.getIncidentDuration(currIncType),incidentMLData.getIncidentDurationStdDev(currIncType));
            //int currLNDur = generateLogNormalDuration(currLNRand, incidentMLData.getIncidentDurMin(currIncType), incidentMLData.getIncidentDurMax(currIncType));
            //listIncidentEvents[incidentNum][3]=currLNDur;
            switch (currIncType) {
                case 0:
                    listIncidentEvents[incidentNum][3] = 15 * distArr1.remove(RNG.nextInt(distArr1.size()));
                    break;
                case 1:
                    listIncidentEvents[incidentNum][3] = 15 * distArr2.remove(RNG.nextInt(distArr2.size()));
                    break;
                case 2:
                    listIncidentEvents[incidentNum][3] = 15 * distArr3.remove(RNG.nextInt(distArr3.size()));
                    break;
                case 3:
                    listIncidentEvents[incidentNum][3] = 15 * distArr4.remove(RNG.nextInt(distArr4.size()));
                    break;
                case 4:
                    listIncidentEvents[incidentNum][3] = 15 * distArr5.remove(RNG.nextInt(distArr5.size()));
                    break;

            }

        }

        // Step 25 - Generate distribution of incident start times and location
        float[] probSegLocation = new float[seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT)];
        float totalVMTSumML = seed.getValueFloat(CEConst.IDS_ML_SP_VMTV); // Denotes sum of VMT over all segments, all periods

        // Generate probability of segment in which the incident occurs
        for (int seg = 0; seg < probSegLocation.length; seg++) {
            probSegLocation[seg] = seed.getValueFloat(CEConst.IDS_ML_S_VMTV, seg, 0) / totalVMTSumML;
        }

        for (int i = 0; i < probSegLocation.length; i++) {
            //System.out.println(probSegLocation[i]);
        }

        // Generate probability of period in which the incident occurs
        float[] probPeriodOccur = new float[seed.getValueInt(IDS_NUM_PERIOD)];

        for (int period = 0; period < seed.getValueInt(IDS_NUM_PERIOD); period++) {
            probPeriodOccur[period] = seed.getValueFloat(CEConst.IDS_ML_P_VMTV, 0, period, 0, -1) / totalVMTSumML;
        }

        // Step 26 - Generate incident start times and locations for all incidents
        int[] numIncAssignedToSegment = new int[probSegLocation.length];
        float adjParam4 = 1.0014f;

        // Setting up parameters for binary search
        ub = 1.5f;
        lb = 0.5f;
        incSum = 0;
        int numIter = 0;

        while (listIncidentEvents.length != incSum && numIter < 200) {
            for (int seg = 0; seg < numIncAssignedToSegment.length; seg++) {
                numIncAssignedToSegment[seg] = Math.round(adjParam4 * listIncidentEvents.length * probSegLocation[seg]);
            }

            incSum = arraySum(numIncAssignedToSegment);
            numIter++;

            // Checking sums and updating binary search if necessary
            if (incSum > listIncidentEvents.length) {                     // adjustment parameter is too high
                ub = adjParam4;
                adjParam4 = (adjParam4 + lb) / 2.0f;

            } else if (incSum < listIncidentEvents.length) {           // adjustment parameter is too low
                lb = adjParam4;
                adjParam4 = (adjParam4 + ub) / 2.0f;
            }
        }

        if (listIncidentEvents.length != incSum && numIter >= 200) {
            int diff = listIncidentEvents.length - arraySum(numIncAssignedToSegment);
            int idx = argMax(numIncAssignedToSegment);
            numIncAssignedToSegment[idx] += diff;
            System.out.println("Incident inflation/reduction of " + diff + " invoked for incident segment locations.");
        }

        int[] numIncAssignedToStartInAP = new int[seed.getValueInt(IDS_NUM_PERIOD)];
        float adjParam5 = 1.0014f;

        // Setting up parameters for binary search
        ub = 1.5f;
        lb = 0.5f;
        incSum = 0;
        numIter = 0;

        while (listIncidentEvents.length != incSum && numIter < 200) {
            for (int period = 0; period < numIncAssignedToStartInAP.length; period++) {
                numIncAssignedToStartInAP[period] = Math.round(adjParam5 * listIncidentEvents.length * probPeriodOccur[period]);
            }

            incSum = arraySum(numIncAssignedToStartInAP);
            numIter++;

            // Checking sums and updating binary search if necessary
            if (incSum > listIncidentEvents.length) {                     // adjustment parameter is too high
                ub = adjParam5;
                adjParam5 = (adjParam5 + lb) / 2.0f;

            } else if (incSum < listIncidentEvents.length) {           // adjustment parameter is too low
                lb = adjParam5;
                adjParam5 = (adjParam5 + ub) / 2.0f;
            }
        }

        if (listIncidentEvents.length != incSum && numIter >= 200) {
            int diff = listIncidentEvents.length - arraySum(numIncAssignedToStartInAP);
            int idx = argMax(numIncAssignedToStartInAP);
            numIncAssignedToStartInAP[idx] += diff;
            System.out.println("Incident inflation/reduction of " + diff + " invoked for incident starting periods.");
        }

        // Step 27 - From LIE, select an incident who start-time and location have not been assigned, and
        //           randomly assign a start time and location from the previous step
        //ArrayList<Integer> selectionArrEvents = createIntegerArrAscending(listIncidentEvents.length);
        ArrayList<Integer> selectionArrSTimes = createIntegerArrDist(numIncAssignedToStartInAP);
        ArrayList<Integer> selectionArrLoc = createIntegerArrDist(numIncAssignedToSegment);

        // Shuffling the arrays into a random order for assignment
        //Collections.shuffle(selectionArrEvents, rng);
        Collections.shuffle(selectionArrSTimes, RNG);
        Collections.shuffle(selectionArrLoc, RNG);

        // Preprocessing array so that incidents of higher severity are assigned first
        ArrayList<IncidentInfo> incInfo = new ArrayList<>();
        for (int idx = 0; idx < listIncidentEvents.length; idx++) {
            incInfo.add(new IncidentInfo(idx, listIncidentEvents[idx][1]));
        }
        Collections.sort(incInfo);

        ArrayList<Integer> selectionArrEvents = new ArrayList<>();
        for (int idx = incInfo.size() - 1; idx >= 0; idx--) {
            selectionArrEvents.add(incInfo.get(idx).incIdx);
            //System.out.println(incInfo.get(idx).severity);
        }

        // Setting up variables for loops
        //int randSTimeIdx;         // Variable to hold randomly generated index for selection of starting AP
        int startAP;              // Variable to hold starting AP for incident
        //int randLocIdx;           // Variable to hold randomly generated index for selection of location
        int segmentNum = 0;           // Variable to hold location of incident
        int numPeriods = seed.getValueInt(CEConst.IDS_NUM_PERIOD);

        int incidentIdx;
        ScenarioInfo currScenarioInfo;
        //Scenario currScenario;
        boolean overlap;
        boolean isValidLocation;
        int counter = 0;
        int locCounter;
        int numRandomizations = 0;
        int maxRandomizations = 5;
        int numUnassignedIncidents = 0;
        while (selectionArrEvents.size() > 0) {
            // Select incident
            incidentIdx = selectionArrEvents.get(0); // index of incident event that has not been assigned a start time and duration

            // Select start time from randomized array
            startAP = selectionArrSTimes.get(counter);

            // Search for a valid location
            isValidLocation = false;
            locCounter = 0;
            while (!isValidLocation && (locCounter < selectionArrLoc.size())) {
                //randLocIdx = rng.nextInt(selectionArrLoc.size());
                segmentNum = selectionArrLoc.get(locCounter);
                // Checking to see if location is valid (i.e. severity does not exceed segment lanes)
                if (seed.getValueInt(CEConst.IDS_ML_NUM_LANES, segmentNum) <= listIncidentEvents[incidentIdx][1]) {
                    locCounter++;
                    //System.out.println("Looking for new incident location.");
                } else {
                    isValidLocation = true;
                }
            }
            currScenarioInfo = scenarioInfos.get(listIncidentEvents[incidentIdx][0]);
            overlap = currScenarioInfo.checkGPIncidentOverlap(startAP, listIncidentEvents[incidentIdx][3] / 15, segmentNum);
            if (!isValidLocation) { // No valid location can be found for the incident
                System.out.println("Type 1 - Incident: " + selectionArrEvents.get(0) + ", Severity" + listIncidentEvents[selectionArrEvents.get(0)][1]);
                numUnassignedIncidents++;
                selectionArrEvents.remove(0);
            } else if (overlap) { // Current location/time period overlaps with another incident
                counter++; // Effectively move to new random start time
                if (counter == selectionArrSTimes.size() && selectionArrEvents.size() > 0) {
                    // If all potential time periods checked, re-randomize events, locations, and start times
                    if (numRandomizations < maxRandomizations) {
                        numRandomizations++;
                        Collections.shuffle(selectionArrEvents, RNG);
                        Collections.shuffle(selectionArrLoc, RNG);
                        Collections.shuffle(selectionArrSTimes, RNG);
                        counter = 0;
                    } else { // If re-randomizaiton fails 5 times, all remaining events will be discarded
                        numUnassignedIncidents += selectionArrEvents.size();
                        for (int loi = 0; loi < selectionArrEvents.size(); loi++) {
                            int tempInc = selectionArrEvents.get(loi);
                            System.out.println("Type 2 - Incident: " + tempInc + ", Severity:" + listIncidentEvents[tempInc][1]
                                    + ", Scen: " + currScenarioInfo.group + ", Seg: " + selectionArrLoc.get(loi) + ", STime: " + selectionArrSTimes.get(loi)
                                    + ", Dur: " + listIncidentEvents[tempInc][3]);
                        }
                        break;
                    }
                }
            } else {
                incidentIdx = selectionArrEvents.remove(0); // Selects and removes incident from list of unassigned incidents
                selectionArrSTimes.remove(counter);          // Removes selected start time from selection array
                selectionArrLoc.remove(locCounter);               // Removes selected location from location selection array
                counter = 0;
                listIncidentEvents[incidentIdx][2] = startAP;
                listIncidentEvents[incidentIdx][4] = segmentNum;

                // Add to currScenarioInfo and Scenario
                currScenarioInfo.addIncidentML(listIncidentEvents[incidentIdx][1], // Incident type (severity)
                        listIncidentEvents[incidentIdx][2], // Incdient start period
                        (listIncidentEvents[incidentIdx][3] / 15), // Incident duration (in Ap's)
                        listIncidentEvents[incidentIdx][4]);   // Incident location

                // Updating scenario
                int numLanes = seed.getValueInt(IDS_MAIN_NUM_LANES_IN, listIncidentEvents[incidentIdx][4]);
                if (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15) <= numPeriods) {   // Changes < to <=
                    mlScenario.CAF().multiply(incidentData_ML.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    mlScenario.OAF().multiply(incidentData_ML.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    mlScenario.DAF().multiply(incidentData_ML.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    mlScenario.SAF().multiply(incidentData_ML.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                } else {
                    int endPeriod = (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15) - 1) % numPeriods;
                    mlScenario.CAF().multiply(incidentData_ML.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    mlScenario.CAF().multiply(incidentData_ML.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    mlScenario.OAF().multiply(incidentData_ML.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    mlScenario.OAF().multiply(incidentData_ML.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    mlScenario.DAF().multiply(incidentData_ML.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    mlScenario.DAF().multiply(incidentData_ML.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    mlScenario.SAF().multiply(incidentData_ML.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    mlScenario.SAF().multiply(incidentData_ML.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                }

                // update sCAF, sSAF, sOAF,SDaf and sLAF for all affected periods
                int adjPeriod;
                for (int period = 0; period < listIncidentEvents[incidentIdx][3] / 15; period++) {
                    adjPeriod = (listIncidentEvents[incidentIdx][2] + period) % seed.getValueInt(IDS_NUM_PERIOD);
                    int oldLAF = mlScenario.LAFI().get(listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                    int currWZLAF = mlScenario.LAFWZ().get(listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                    int newLAF = Math.max(oldLAF + incidentData_ML.getIncidentLAF(listIncidentEvents[incidentIdx][1], numLanes - 2), -1 * (numLanes + currWZLAF - 1));
                    mlScenario.LAFI().add(newLAF, listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                }

            }

        }
        if (numUnassignedIncidents > 0) {
            JOptionPane.showMessageDialog(null, "<HTML><CENTER>Failed to assign " + numUnassignedIncidents + " incident(s).<br>&nbsp<br>"
                    + "If the number of unassigned incidents is high, please ensure the inputs<br>"
                    + "(incidents frequencies, distributions, etc.) are valid<br>"
                    + "or retry Scenario Generation with a new random number generator seed.");
        }
        MainWindow.printLog(listIncidentEvents.length + " incident events assigned to scenarios.");
        return listIncidentEvents;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Scenario Generation Specific Utility Functions">
    /**
     * Method to check if input is valid before the scenario generation
     * procedure can be run.
     *
     * @return True if the inputs are valid, false otherwise.
     */
    private boolean allInputValid() {
        // Check manditory inputs (Seed and DemandData_GP

        if (seed == null || demandData_GP == null) {
            return false;
        } else {
            if (incidentBool_GP && incidentData_GP == null) {
                return false;
            }
            if (weatherBool && weatherData == null) {
                return false;
            }
            if (wzBool && workZoneData_GP == null) {
                return false;
            }
            if (mlUsed) {
                if (demandData_ML == null) {
                    return false;
                }
                if (incidentBool_ML && incidentData_ML == null) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Method to calculate the number of scenarios that will be generated by the
     * Hybrid Scenario Generation procedure with the current inputs. Used in the
     * scenario generation method.
     *
     * @param numDayOfWeekInMonthAP Array with the number of each day type of
     * each month active in the Reliability Reporting Period (automatically
     * generated during the scenario generation process.
     *
     * @return Number of scenarios that will be generated by the scenario
     * generation process.
     */
    private int calculateNumberOfScenarios(int[] numDayOfWeekInMonthAP) {
        int numDemandPatterns = 0;
        for (int i = 0; i < numDayOfWeekInMonthAP.length; i++) {
            if (numDayOfWeekInMonthAP[i] > 0) {
                numDemandPatterns++;
            }
        }
        return numDemandPatterns * numReplications;
    }

    /**
     * Method to create the array of probabilities of each reliability scenario.
     *
     * @param demandData Instance of DemandData for the Reliability Reporting
     * Period.
     * @return Array containing the probability of each reliability scenario.
     */
    private float[] createProbabilities(DemandData demandData) {
        int[] daysArr = CEDate.numDayOfWeekInMonthAP(demandData);
        excludeDays(daysArr);
        float[] probArr = new float[84];
        int totalNumDaysAP = arraySum(daysArr);
        for (int i = 0; i < daysArr.length; i++) {
            probArr[i] = ((float) daysArr[i]) / totalNumDaysAP;
            //probArr[i] = probArr[i]/4.0f;
            probArr[i] = probArr[i] / ((float) numReplications);
        }

        return probArr;
    }
        // </editor-fold>
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Utility Functions">
    /**
     * Method to calculate the sum of an array.
     *
     * @param arr Array to sum.
     * @return Sum of the array.
     */
    private int arraySum(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    /**
     * Method to calculate the sum of an array of arrays.
     *
     * @param arr Array of arrays to sum.
     * @return Sum of the array of arrays.
     */
    private int arraySum(int[][] arr) {
        int sum = 0;
        for (int[] arr1 : arr) {
            for (int j = 0; j < arr[0].length; j++) {
                sum += arr1[j];
            }
        }
        return sum;
    }

    /**
     * Method to sum an ArrayList.
     *
     * @param arr ArrayList to sum.
     * @return Sum of the ArrayList
     */
    private int arraySum(ArrayList<Integer> arr) {
        int sum = 0;
        for (Integer val : arr) {
            sum += val;
        }
        return sum;
    }

    /**
     * Method to calculate the sum of an array.
     *
     * @param arr Array to sum.
     * @return Sum of the array.
     */
    private float arraySum(float[] arr) {
        float sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    /**
     * Method to calculate the sum of an array.
     *
     * @param arr Array to sum.
     * @return Sum of the array.
     */
    private int arraySum(boolean[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == true) {
                sum++;
            }
        }
        return sum;
    }

    /**
     * Array to get the max of a column of a matrix (2D array).
     *
     * @param arr Matrix to extract the column sum from.
     * @param col Column to sum.
     * @return Sum of the column of the matrix (2D array).
     */
    private int getColMax(int[][] arr, int col) {
        int max = -999999;
        int maxRowIdx = 0;
        for (int row = 0; row < arr.length; row++) {
            if (arr[row][col] > max) {
                max = arr[row][col];
                maxRowIdx = row;
            }
        }
        return maxRowIdx;

    }

    /**
     * Method to find the index of the maximum element of an array.
     *
     * @param arr Array of integers
     * @return Index of the maximum element in the array.
     */
    private int argMax(int[] arr) {
        int idx = 0;
        int max = -9999999;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                idx = i;
            }
        }
        return idx;
    }

    /**
     * Method create an array of the normalized cumulative sums of the elements
     * of the input array.
     *
     * @param arr Array of floats
     * @return New array with the normalized cumulative sums.
     */
    private float[] cumSumNormalize(float[] arr) {
        float sum = arr[0];
        float[] newArr = new float[arr.length];
        newArr[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            newArr[i] = arr[i] + newArr[i - 1];
            sum += arr[i];
        }
        // Normalizing new cumulative sum array
        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = newArr[i] / sum;
        }
        return newArr;
    }

    /**
     * Method to calculate the factorial of a non-negative integer.
     *
     * @param value Non-negative integer
     * @return Factorial of the input integer
     */
    private int factorial(int value) {
        if (value < 0) {
            throw new RuntimeException("Invalid Factorial Input");
        }
        int prod = 1;
        for (int i = 1; i <= value; i++) {
            prod *= i;
        }
        return prod;
    }

    /**
     * Method to create an ArrayList of integers ascending from 0 to one less
     * than the input length (0,1,..,length - 1). Returned ArrayList will be of
     * the specified length.
     *
     * @param length Desired length of the ascending value array.
     * @return Ascending value array with values from 0 to (length-1)
     */
    private ArrayList<Integer> createIntegerArrAscending(int length) {
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            arr.add(i);
        }
        return arr;
    }

    /**
     * Method to create an ArrayList of integer values matching a given
     * distribution. If input distribution is {10,15}, the output ArrayList will
     * have 10 instances of 0 and 15 instances of 1.
     *
     * @param distArr Distribution the values of the returned ArrayList will
     * match.
     * @return ArrayList of integer values matching the input distribution.
     */
    private ArrayList<Integer> createIntegerArrDist(int[] distArr) {
        // Creating array to hold list of events
        ArrayList<Integer> listEvents = new ArrayList<>();

        for (int eventType = 0; eventType < distArr.length; eventType++) {
            for (int event = 0; event < distArr[eventType]; event++) {
                listEvents.add(eventType);
            }
        }

        return listEvents;
    }

    /**
     * Method to generate the probabilities of incident durations matching a log
     * normal distribution. Method returns a 2D array indicating the number of
     * incidents of each type with each duration. For example, if the return
     * array has value duration[i][j] = 2, this indicates there are 2 i-lane
     * closures (i = 0 is a shoulder closure) closures with j*15 minute
     * durations (j analysis periods).
     *
     * @param numIncOfEachType Number of each type of incident.
     * @return 2D array containing the number of incidents of each type with
     * each duration.
     *
     */
    private float[][] generateLogNormalDurationProbabilites(int[] numIncOfEachType) {
        float[][] durationInfo = incidentData_GP.getIncidentDurationInfo();
        ArrayList<ArrayList<Float>> probArrayList = new ArrayList<>();
        boolean descending;
        float currProb;
        float lastProb;
        int numInc;
        int counter;
        float mu;
        float sigma;
        for (int incType = 0; incType < durationInfo.length; incType++) {
            //mu = durationInfo[incType][0];
            //sigma = durationInfo[incType][1];
            mu = (float) Math.log(Math.pow(durationInfo[incType][0], 2) / Math.sqrt(Math.pow(durationInfo[incType][1], 2) + Math.pow(durationInfo[incType][0], 2)));
            sigma = (float) Math.sqrt(Math.log(1 + (Math.pow(durationInfo[incType][1], 2) / Math.pow(durationInfo[incType][0], 2))));
            //System.out.println(mu+", "+sigma);
            numInc = numIncOfEachType[incType];
            probArrayList.add(new ArrayList<Float>());
            probArrayList.get(incType).add(0.0f);
            descending = false;
            //currProb = 1.0f;
            lastProb = 0.0f;
            counter = 1;
            while (!(Math.round(lastProb * numInc) == 0 && descending)) {
                //currProb = generateLogNormalProbability(counter*15,mu,sigma);
                if (counter == 1) {
                    currProb = logNormCDF(0.001f, (counter * 15 + 7.5f), 0.001f, mu, sigma);
                } else {
                    currProb = logNormCDF((counter * 15 - 7.5f), (counter * 15 + 7.5f), 0.001f, mu, sigma);
                }
                //System.out.println(incType+" "+(counter*15)+" "+currProb);
                probArrayList.get(incType).add(currProb);
                if (currProb < lastProb) {
                    descending = true;
                }
                lastProb = currProb;
                counter++;
            }
        }

        // Getting max ArrayList length
        int max = -1;
        for (ArrayList<Float> arrList : probArrayList) {
            if (arrList.size() > max) {
                max = arrList.size();
            }
        }

        float[][] probArray = new float[numIncOfEachType.length][max];
        for (int incType = 0; incType < probArray.length; incType++) {
            for (int i = 0; i < probArray[incType].length; i++) {
                probArray[incType][i] = (i < probArrayList.get(incType).size()) ? probArrayList.get(incType).get(i) : 0;
            }
        }

        return probArray;
    }

    /**
     * Calculates the value of a Log-Normal Probability Density Function (PDF)
     * with the input mean and standard deviation at the input value.
     *
     * @param value Value at which to find the value of the Log-Normal PDF
     * @param mean Mean of the Log-Normal distribution.
     * @param stdev Standard Deviation of the Log-Normal.
     * @return Value of the Log-Normal distribution.
     */
    private float generateLogNormalProbability(float value, float mean, float stdev) {
        // exp(-0.5 * ((ln(x) - m) / s)^2) / (s * sqrt(2 * pi) * x)
        return (float) Math.exp(-0.5 * Math.pow(((Math.log(value) - mean) / stdev), 2)) / ((float) (stdev * Math.sqrt(2 * Math.PI) * value));
    }

    /**
     * Calculating the Log-Normal CDF between two values. For reference:
     * http://en.wikipedia.org/wiki/Log-normal_distribution
     *
     *
     * @param value1 Lower bound value
     * @param value2 Upper bound value
     * @param step Step-size for calculating the integral of the underlying PDF
     * (should be small)
     * @param mu Log-scale/Location parameter
     * @param sigma Shape parameter.
     * @return Value of the Log-Normal CDF between the two specified input
     * values.
     */
    private float logNormCDF(float value1, float value2, float step, float mu, float sigma) {
        if (value1 < 0 || value2 < 0) {
            throw new RuntimeException("Invalid CDF Values: Must be larger than 0.");
        }
        float prob = 0;
        float currValue = value1;
        while (currValue < value2) {
            currValue += step;
            prob += step * generateLogNormalProbability(currValue, mu, sigma);
        }
        return prob;
    }

    /**
     * Returns hard-coded Log-Normal Probabilities for up to 3 Lane incidents.
     *
     * @return 2D Array with probability of each duration for each incident
     * severity type.
     * @deprecated
     */
    private float[][] getHCDurProbs() {
        float[][] hcdurprobs = new float[8][5];
        int[][] tempArr = {{0, 0, 0, 0, 0},
        {344, 47, 0, 0, 0},
        {190, 72, 13, 1, 0},
        {124, 47, 9, 6, 0},
        {36, 14, 7, 4, 0},
        {0, 0, 0, 4, 0},
        {0, 0, 0, 2, 0},
        {0, 0, 0, 0, 0}};
        int[] currSum = new int[5];
        for (int i = 0; i < 5; i++) {  // Getting the column sums of tempArr
            for (int j = 0; j < 8; j++) {
                currSum[i] += tempArr[j][i];
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                if (currSum[i] != 0) {
                    hcdurprobs[j][i] = ((float) tempArr[j][i]) / currSum[i];
                } else {
                    hcdurprobs[j][i] = 0.0f;
                }
            }

        }
        return hcdurprobs;
    }

    /**
     * Method to calculate the number of scenarios assigned each duration
     * length. Follows the Hybrid Scenario Generation methodology. Accounts for
     * rounding issues via the 3rd set of adjustment parameters.
     *
     * @param numIncOfEachType
     * @return 2D array of number of incidents of each duration length.
     */
    private int[][] getNumScenAssignedDurationLength(int[] numIncOfEachType) {
        //float[][] hardCodedDurationProbabilites = getHCDurProbs();
        float[][] durationProbabilities = generateLogNormalDurationProbabilites(numIncOfEachType);
        int[][] numScenAssignedDurLen = new int[durationProbabilities.length][durationProbabilities[0].length];

        // Adjustment parameters and bounds for binary search
        float[] adjParamArr3 = {1.0f,
            1.0f,
            1.0f,
            1.0f,
            1.0f};
        float ub;
        float lb;
        int incSum = 0;
        int numIter;

        //Begin loop over incident types
        for (int incType = 0; incType < 5; incType++) {
            ub = 1.5f;
            lb = 0.5f;
            numIter = 0;
            while (numIncOfEachType[incType] != incSum && numIter < 200) {
                for (int dur = 0; dur < durationProbabilities[incType].length; dur++) {
                    numScenAssignedDurLen[incType][dur] = Math.round(adjParamArr3[incType] * numIncOfEachType[incType] * durationProbabilities[incType][dur]);
                }

                // Updating values for binary search of correct adjustment parameters
                incSum = arraySum(numScenAssignedDurLen[incType]);
                numIter++;
                // System.out.println(incSum);
                if (incSum > numIncOfEachType[incType]) {                     // Adjustment paramter is too high
                    ub = adjParamArr3[incType];
                    adjParamArr3[incType] = (adjParamArr3[incType] + lb) / 2.0f;
                } else if (incSum < numIncOfEachType[incType]) {              // Adjustment parameter is too low
                    lb = adjParamArr3[incType];
                    adjParamArr3[incType] = (adjParamArr3[incType] + ub) / 2.0f;
                }
            }

            if (numIter >= 200 && incSum != numIncOfEachType[incType]) {
                int diff = numIncOfEachType[incType] - incSum;
                System.out.println(diff);
                int durIdx = getColMax(numScenAssignedDurLen, incType);
                numScenAssignedDurLen[incType][durIdx] += diff;
                System.out.println("Invoked for incType " + incType);
            }
        }

        return numScenAssignedDurLen;
    }

    /**
     * Resets the Random number generator to a new one based on the RNG Seed.
     */
    private void resetRNG() {
        this.RNG = new Random(this.rngSeed);
    }

    /**
     * Resets the Random number generator to a new one based on the RNG Seed
     * times the input factor.
     *
     * @param factor Integer by which the RNG seed is multiplied.
     */
    private void resetRNG(long factor) {
        this.RNG = new Random(this.rngSeed * factor);
    }
    //</editor-fold>

    /**
     * Class for comparing Incidents by severity. Used during scenario
     * generation to assign incidents of higher severity (i.e. higher lane
     * closures) first so as to give them the best chance of being assigned to a
     * valid segment and time period.
     */
    private class IncidentInfo implements Comparable {

        /**
         * Index of the incident.
         */
        public int incIdx;

        /**
         * Severity type of the incident. (0 - Shoulder closure, 1 - One lane
         * closure, 2 - Two lane closure, 3 - Three lane closure, 4 - Four or
         * more lane closure
         */
        public int severity;

        /**
         * Constructor for IncidentInfo Object
         *
         * @param incIdx Index of the incident.
         * @param severity Severity type of the incident.
         */
        public IncidentInfo(int incIdx, int severity) {
            this.incIdx = incIdx;
            this.severity = severity;
        }

        /**
         * Compares method based on the severity of the incident.
         *
         * @param o
         * @return
         */
        @Override
        public int compareTo(Object o) {
            return Integer.compare(severity, ((IncidentInfo) o).severity);
        }

    }
}
