package coreEngine.reliabilityAnalysis;

import GUI.major.MainWindow;
import coreEngine.Helper.CEConst;
import static coreEngine.Helper.CEConst.IDS_MAIN_NUM_LANES_IN;
import static coreEngine.Helper.CEConst.IDS_NUM_PERIOD;
import static coreEngine.Helper.CEConst.IDS_NUM_SEGMENT;
import coreEngine.Helper.CEDate;
import static coreEngine.Helper.CEDate.dayOfWeek;
import static coreEngine.Helper.CEDate.daysInMonth;
import coreEngine.Seed;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JOptionPane;
import coreEngine.reliabilityAnalysis.DataStruct.DemandData;
import coreEngine.reliabilityAnalysis.DataStruct.IncidentData;
import coreEngine.reliabilityAnalysis.DataStruct.Scenario;
import coreEngine.reliabilityAnalysis.DataStruct.ScenarioInfo;
import coreEngine.reliabilityAnalysis.DataStruct.WeatherData;
import coreEngine.reliabilityAnalysis.DataStruct.WorkZoneData;

/**
 *
 * @author lake and tristan
 *
 * Class that generates the scenarios for FREEVAL reliability analysis.
 *
 */
public class ScenarioGenerator {

    // Inputs

    /**
     *
     */
        private Seed seed = null;

    /**
     *
     */
    private DemandData demandData = null;

    /**
     *
     */
    private WeatherData weatherData = null;

    /**
     *
     */
    private IncidentData incidentData = null;

    /**
     *
     */
    private ArrayList<WorkZoneData> workZoneData = null;

    /**
     *
     */
    private boolean wzBool;

    /**
     *
     */
    private boolean weatherBool;

    /**
     *
     */
    private boolean incidentBool;

    /**
     *
     */
    private boolean mlUsed = false;

    /**
     *
     */
    private IncidentData incidentMLData = null;

    /**
     *
     */
    private DemandData demandMLData = null;

    /**
     *
     */
    private boolean mlIncidentBool;

    // Outputs
    //GP Lanes

    /**
     *
     */
        private Scenario scenario;

    /**
     *
     */
    private ArrayList<ScenarioInfo> scenarioInfos = null;

    // Managed Lanes

    /**
     *
     */
        private Scenario mlScenario;

    // Constants and calculated values

    /**
     *
     */
        private final float studyPeriodDuration;

    /**
     *
     */
    private int numReplications = 4;

    /**
     *
     */
    private int numScenarios = 0;

    /**
     *
     */
    private final int[][] excludedDays = new int[12][7];

    // Random number generator

    /**
     *
     */
        private Random rng;

    /**
     *
     */
    private long rngSeed;

    /**
     *
     */
    public final static String[] monthString = new String[]{
        "January", "February", "March", "April", "May", "June", "July", "August", "September",
        "October", "November", "December"
    };

    /**
     * Constructor
     *
     * @param seed The seed that will be used to generate scenarios
     */
    public ScenarioGenerator(Seed seed) {
        this.seed = seed;

        this.studyPeriodDuration = this.seed.getValueInt(IDS_NUM_PERIOD) * 0.25f;

        //this.rngSeed = System.currentTimeMillis();
        this.rngSeed = (System.currentTimeMillis() % 1000000);
        this.rng = new Random(rngSeed);

    }

    // <editor-fold defaultstate="collapsed" desc="Setters">
    /**
     * Sets the demand data used to generate scenarios
     *
     * @param data
     */
    public void setGPDemandData(DemandData data) {
        this.demandData = data;
    }

    /**
     * Sets the work zone data used in scenario generation.
     *
     * @param data
     */
    public void setGPWorkZoneData(ArrayList<WorkZoneData> data) {
        this.workZoneData = data;
    }

    /**
     * Sets the weather data used to generate scenarios
     *
     * @param data
     */
    public void setWeatherData(WeatherData data) {
        this.weatherData = data;
    }

    /**
     * Sets the incident data used to generate scenarios
     *
     * @param data
     */
    public void setGPIncidentData(IncidentData data) {
        this.incidentData = data;
    }

    /**
     *
     * @param newVal
     */
    public void setNumberOfReplications(int newVal) {
        if (newVal > 0) {
            this.numReplications = newVal;
        } else {
            throw new NumberFormatException();
        }
    }

    /**
     *
     * @param newSeed
     */
    public void setRNGSeed(long newSeed) {
        rngSeed = newSeed;
        resetRNG();
    }

    /**
     *
     * @param mlUsed
     */
    public void setMLUsed(boolean mlUsed) {
        this.mlUsed = mlUsed;
    }

    /**
     *
     * @param incidentMLData
     */
    public void setMLIncidentData(IncidentData incidentMLData) {
        this.incidentMLData = incidentMLData;
    }

    /**
     *
     * @param demandMLData
     */
    public void setMLDemandData(DemandData demandMLData) {
        this.demandMLData = demandMLData;
    }

    /**
     *
     * @param val
     */
    public void includeGPWorkZones(boolean val) {
        wzBool = val;
    }

    /**
     *
     * @param val
     */
    public void includeGPIncidents(boolean val) {
        incidentBool = val;
    }

    /**
     *
     * @param val
     */
    public void includeWeather(boolean val) {
        weatherBool = val;
    }

    /**
     *
     * @param val
     */
    public void includeMLIncidents(boolean val) {
        mlIncidentBool = val;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * Getter for GP Scenario object
     *
     * @return
     */
    public Scenario getGPScenario() {
        return scenario;
    }

    /**
     * Getter for ML Scenario Object
     *
     * @return
     */
    public Scenario getMLScenario() {
        return mlScenario;
    }

    /**
     * Getter for the ArrayList of ScenarioInfo objects
     *
     * @return
     */
    public ArrayList<ScenarioInfo> getScenarioInfoList() {
        return scenarioInfos;
    }

    /**
     *
     * @return
     */
    public int getNumberOfReplications() {
        return this.numReplications;
    }

    /**
     *
     * @return
     */
    public long getRNGSeed() {
        return this.rngSeed;
    }
        // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Day Exclusion">
    /**
     *
     * @param numDays
     * @param month
     */
    public void excludeDaysInMonth(int[] numDays, int month) {
        if (arraySum(numDays) > 0) {
            System.out.println("Excluding " + arraySum(numDays)
                    + " days in " + monthString[month]);
            for (int i = 0; i < 7; i++) {
                excludedDays[month][i] = numDays[i];
            }
        }
    }

    /**
     * Method to exclude days from the array of the (12x7=84) array of active
     * days.
     *
     * @param daysArr 84 length array of active days
     */
    private void excludeDays(int[] daysArr) {
        int currIdx = 0;
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
     *
     * @return
     */
        public boolean generateScenarios() {

        if (allInputValid()) {

            int[] monthChangeIdx = new int[12]; // To keep track of where each month begins and ends

            int[] numDaysInMonthAP = CEDate.numDayOfWeekInMonthAP(demandData);
            excludeDays(numDaysInMonthAP);
            numScenarios = calculateNumberOfScenarios(numDaysInMonthAP);

            //Creating scenario probabilities
            float[] probArray = createProbabilities(demandData);
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
                        float seedDateAdjFactor = demandData.getValue(seedDate.month - 1, seedDate.dayOfWeek()); // Subtract 1 from month to correct indexing, dayOfWeekMethod returns correct day index
                        float curr_sOAF = demandData.getValue(month, day) / seedDateAdjFactor; //Calculationg the sOAF
                        float curr_sDAF = demandData.getValue(month, day) / seedDateAdjFactor; //Calculationg the sDAF
                        for (int replicate = 0; replicate < numReplications; ++replicate) { //replication loop
                            scenario.OAF().set(curr_sOAF, scenarioIndex, 0, 0, scenarioIndex, seed.getValueInt(IDS_NUM_SEGMENT) - 1, seed.getValueInt(IDS_NUM_PERIOD) - 1);
                            scenario.DAF().set(curr_sDAF, scenarioIndex, 0, 0, scenarioIndex, seed.getValueInt(IDS_NUM_SEGMENT) - 1, seed.getValueInt(IDS_NUM_PERIOD) - 1);

                            scenarioInfos.add(new ScenarioInfo(probArray[month * 7 + day], scenarioIndex, CEDate.getMonthDayString(month + 1, day)));
                            scenarioInfos.get(scenarioInfos.size() - 1).setDemandMultiplier(curr_sDAF);
                            scenarioInfos.get(scenarioInfos.size() - 1).setSeed(seed);
                            scenarioInfos.get(scenarioInfos.size() - 1).month = month;
                            scenarioInfos.get(scenarioInfos.size() - 1).day = day;

                            scenarioIndex++;
                            //}
                        }

                    }
                }

                monthChangeIdx[month] = scenarioIndex;  // Assigning index of last group occuring in month

            }

            //} // End of initial scenario generation and DAF assignment
            MainWindow.printLog("Number of scenario created: " + scenarioInfos.size());

            if (wzBool && workZoneData != null) {
                assignWorkZones();
            }

            if (weatherBool) {
                createAndAssignWeatherEvents(monthChangeIdx);
            }

            // Proceed to incident modeling
            if (incidentBool) {
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
                            float seedDateAdjFactor = demandMLData.getValue(seedDate.month - 1, seedDate.dayOfWeek()); // Subtract 1 from month to correct indexing, dayOfWeekMethod returns correct day index
                            float curr_sOAF = demandMLData.getValue(month, day) / seedDateAdjFactor; //Calculationg the sOAF
                            float curr_sDAF = demandMLData.getValue(month, day) / seedDateAdjFactor; //Calculationg the sDAF
                            for (int replicate = 0; replicate < numReplications; ++replicate) { //replication loop
                                mlScenario.OAF().set(curr_sOAF, scenarioIndex, 0, 0, scenarioIndex, seed.getValueInt(IDS_NUM_SEGMENT) - 1, seed.getValueInt(IDS_NUM_PERIOD) - 1);
                                mlScenario.DAF().set(curr_sDAF, scenarioIndex, 0, 0, scenarioIndex, seed.getValueInt(IDS_NUM_SEGMENT) - 1, seed.getValueInt(IDS_NUM_PERIOD) - 1);

                                scenarioIndex++;
                            }
                        }
                    }

                }

                // Check if ML incidents
                if (mlIncidentBool) {
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
     *
     */
        private void assignWorkZones() {
        int numWorkZones = workZoneData.size();

        for (int wzIdx = 0; wzIdx < numWorkZones; wzIdx++) {
            WorkZoneData currWZ = workZoneData.get(wzIdx);
            int startMonth = currWZ.getStartDate().month;  // Month indexing starts at 1 (i.e. 1 implies january)
            int startDay = currWZ.getStartDate().day;
            int endMonth = currWZ.getEndDate().month;
            int endDay = currWZ.getEndDate().day;
            ScenarioInfo currScenarioInfo;
            int startIdx;
            int endIdx;

            int[] wzDaysArray = CEDate.numDayOfWeekInMonthAP(demandData.getYear(), startMonth, startDay, endMonth, endDay, demandData.getActiveDays());
            int[] fullDayOfWeekInMonth = CEDate.numDayOfWeekInMonthAP(demandData.getYear(), demandData.getActiveDays());
            // Post-processing of array
            for (int i = 0; i < wzDaysArray.length; i++) {
                if (fullDayOfWeekInMonth[i] != 0) {
                    wzDaysArray[i] = Math.round((wzDaysArray[i] / fullDayOfWeekInMonth[i]) * numReplications);
                } else {
                    wzDaysArray[i] = 0;
                }
            }

            int currDayType;
            int currMonth = 0;
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
        // <editor-fold defaultstate="collapsed" desc="Deprecated bugged code">
        //
        //            // First, fill any partial month at beginning
        //            if (startDay!=1) {
        //                startIdx = (endMonth > 1) ? monthChangeIdx[endMonth - 2] : 0;     // Setting scenIdx to first scenario of first month of the work zone
        //                endIdx = monthChangeIdx[endMonth-1];
        //
        //                int[] numDayOfWeek = CEDate.numEachDayOfWeekInPeriod(demandData.getYear(), endMonth,  1, endDay);
        //
        //                for (int scenIdx = startIdx; scenIdx < endIdx; scenIdx++) {
        //
        //                    currScenarioInfo = scenarioInfos.get(scenIdx);
        //                    // Check to see if work zone should be assigned;
        //                    if (numDayOfWeek[currScenarioInfo.getDayType()]>0) {
        //
        //                        // Decreause number remaining to be assigned to day type DC
        //                        numDayOfWeek[currScenarioInfo.getDayType()]-=1;
        //
        //                        // Add to currScenarioInfo and Scenario
        //                        scenarioInfos.get(scenIdx).addWorkZone(currWZ);
        //
        //                        // Updating scenario
        //                        for (int seg = currWZ.getStartSegment() - 1; seg < currWZ.getEndSegment(); seg++) {
        //                            scenario.CAF().set(WorkZoneData.getCAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                            scenario.OAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                            scenario.DAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                            scenario.SAF().set(WorkZoneData.getFFSAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                            scenario.LAFWZ().set(WorkZoneData.getLAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                        }
        //                    }
        //
        //                }
        //            }
        //
        //
        //            startIdx = (startMonth > 1) ? monthChangeIdx[startMonth-2] : 0;
        //            endIdx = (endMonth > 1) ? monthChangeIdx[endMonth-2] : 0;
        //            // Loop will not run if work zone does not cover any complete month
        //            for (int scenIdx = startIdx; scenIdx < endIdx; scenIdx++) {
        //
        //                // Add to currScenarioInfo
        //                scenarioInfos.get(scenIdx).addWorkZone(currWZ);
        //
        //                // Updating scenario
        //                for (int seg = currWZ.getStartSegment() - 1; seg < currWZ.getEndSegment(); seg++) {
        //                    scenario.CAF().set(WorkZoneData.getCAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                            scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                    scenario.OAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                            scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                    scenario.DAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                            scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                    scenario.SAF().set(WorkZoneData.getFFSAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                            scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                    scenario.LAFWZ().set(WorkZoneData.getLAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                            scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                }
        //            }
        //
        //            if (startDay!=1) {
        //                // Next, fill any partial month at beginning
        //                startIdx = (endMonth > 1) ? monthChangeIdx[endMonth - 2] : 0;     // Setting scenIdx to first scenario of first month of the work zone
        //                endIdx = monthChangeIdx[endMonth-1];
        //
        //                int[] numDayOfWeek = CEDate.numEachDayOfWeekInPeriod(demandData.getYear(), endMonth,  1, endDay);
        //
        //                for (int scenIdx = startIdx; scenIdx < endIdx; scenIdx++) {
        //
        //                    currScenarioInfo = scenarioInfos.get(scenIdx);
        //                    // Check to see if work zone should be assigned;
        //                    if (numDayOfWeek[currScenarioInfo.getDayType()]>0) {
        //
        //                        // Decreause number remaining to be assigned to day type DC
        //                        numDayOfWeek[currScenarioInfo.getDayType()]-=1;
        //
        //                        // Add to currScenarioInfo and Scenario
        //                        scenarioInfos.get(scenIdx).addWorkZone(currWZ);
        //
        //                        // Updating scenario
        //                        for (int seg = currWZ.getStartSegment() - 1; seg < currWZ.getEndSegment(); seg++) {
        //                            scenario.CAF().set(WorkZoneData.getCAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                            scenario.OAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                            scenario.DAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                            scenario.SAF().set(WorkZoneData.getFFSAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                            scenario.LAFWZ().set(WorkZoneData.getLAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                    scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                        }
        //                    }
        //
        //                }
        //            }
        //
        //            // Fill any partial month at end
        //            startIdx = (endMonth > 1) ? monthChangeIdx[endMonth - 2] : 0;     // Setting scenIdx to first scenario of final month of the work zone
        //            endIdx = monthChangeIdx[endMonth-1];
        //
        //            int[] numDayOfWeek = CEDate.numEachDayOfWeekInPeriod(demandData.getYear(), endMonth,  1, endDay);
        //
        //            for (int scenIdx = startIdx; scenIdx < endIdx; scenIdx++) {
        //
        //                currScenarioInfo = scenarioInfos.get(scenIdx);
        //                // Check to see if work zone should be assigned;
        //                if (numDayOfWeek[currScenarioInfo.getDayType()]>0) {
        //
        //                    // Decreause number remaining to be assigned to day type DC
        //                    numDayOfWeek[currScenarioInfo.getDayType()]-=1;
        //
        //                    // Add to currScenarioInfo and Scenario
        //                    scenarioInfos.get(scenIdx).addWorkZone(currWZ);
        //
        //                    // Updating scenario
        //                    for (int seg = currWZ.getStartSegment() - 1; seg < currWZ.getEndSegment(); seg++) {
        //                        scenario.CAF().set(WorkZoneData.getCAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                        scenario.OAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                        scenario.DAF().set(WorkZoneData.getDAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                        scenario.SAF().set(WorkZoneData.getFFSAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                        scenario.LAFWZ().set(WorkZoneData.getLAF(currWZ.getSeverity(), seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg) - 2),
        //                                scenIdx, seg, currWZ.getStartPeriod() - 1, scenIdx, seg, currWZ.getEndPeriod() - 1);
        //                    }
        //                }
        //
        //            }
        //        }
        //        MainWindow.printLog("Work Zones assigned to Scenarios.");

        // </editor-fold>
    }
        // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Weather Modeling">

    /**
     *
     * @param monthChangeIdx
     */
        private void createAndAssignWeatherEvents(int[] monthChangeIdx) {
                //  Begin modeling of weather events

        // Step 6 - Group scenarios by month
        // Information stored in monthChangeIdx
        // Step 7 - Compute expected frequency of weather events by month
        int[][] expFreqWT = computeExpFreqOfWeatherEvents(monthChangeIdx);

        // Begin weather event assignment loop
        // Creating counters and marker variables for loops
        int numScenInPrevMonth = 0; //Marker for
        int numScenInMonth;
        //int scenarioInfoMarker = 0;
        //int prevScenarioInfoMarker = 0;

        // Step 8 - Select a month to which whether events are not yet assigned
        for (int month = 0; month < 12; month++) {
                //Step 9 - Update (create) list of weather events
            // this is known implicitly from expFreqWT, weatherData.getAvgDurRoundedTo15MinIncrHour, table CAFs and FFSAFs

            // Creating array to hold scenario probabilities of the current month
            numScenInMonth = (monthChangeIdx[month] - numScenInPrevMonth);
            if (numScenInMonth > 0) {
                float[] currMonthScenProb = new float[numScenInMonth];

                // Loop to extract probabilities for all scenarios in the current month
                //int i = 0;
                //ScenarioInfo currScenarioInfo = scenarioInfos.get(numScenInPrevMonth);
                for (int i = 0; i < numScenInMonth; i++) {
                    currMonthScenProb[i] = scenarioInfos.get(numScenInPrevMonth + i).prob;
                }

                //getting the cumulative sum of probablities and normalizing
                float[] currMonthScenCumProb = cumSumNormalize(currMonthScenProb);

                // Steps 10 - From the LWE of the current month, select a weather event and assign a scenario and start time
                assignCurrMonthWeatherEvents(expFreqWT[month], currMonthScenCumProb, numScenInPrevMonth);  // Broken?
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
        int numScenInMonth = 0;

        // Creating array to hold probabilities
        int[][] expFreqWT = new int[12][numWT];

        // Generating expected frequencies
        for (int month = 0; month < 12; month++) {                           // For each month
            numScenInMonth = (monthChangeIdx[month] - prevNumGroups);          // Get number of scenarios in month [N(j,scen)]
            for (int weatherType = 0; weatherType < numWT; weatherType++) {      // For each weatherType
                if (Math.pow((weatherData.getAvgDurRoundedTo15MinIncrHour(weatherType) - 0.0f), 2) < 10e-12) {  // If weatherType has nonzero duration
                    expFreqWT[month][weatherType] = 0;                         // If 0 duration, set expected frequency to 0
                } else {                                                       // Else, if nonzero duration
                    expFreqWT[month][weatherType] = (int) Math.round( // Compute expected frequency: Round[(Pt(w,j)*Dsp*N(j,scen))/E15[Dw]]
                            (weatherData.getProbabilityDecimal(month, weatherType) * studyPeriodDuration
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
     * @param expMonthFreqWT
     * @param expFreqWT int[] array holding the expected frequency of weather
     * events for the current month
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
                    sTimeRand = rng.nextDouble();                             //RNG Changed
                    sTime = (int) Math.round(sTimeRand * studyPeriodDuration * 4.0f);           // Number of 15 minute increments past beginning of SP

                    //Determing scenario to which event is assigned
                    scenRand = rng.nextDouble();                              //RNG Changed
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
     *
     * @param monthChangeIdx names, for debugging
     * @return
     */
    private int[][] createAndAssignListIncidentEventsGP(int[] monthChangeIdx) {

        // Creating counters and marker variables for loops
        int numScenInMonth;
        int totalIncidentsInMonth = 0;
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

            ArrayList<Integer> incidents = new ArrayList<>();

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
                    float lambda = incidentData.getIncidentFrequencyMonth(month);
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
                float lambda = incidentData.getIncidentFrequencyMonth(month);
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
                    int randIdx = rng.nextInt(tempArr.size());                // RNG Changed
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
                totalNumIncOfEachType[incType] = Math.round(adjParam2 * incidentData.getIncidentDistributionDecimal(incType) * totalNumOfIncForRRP);
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
        int randIdx = 0;
        ArrayList<Integer> tempArr = createIntegerArrAscending(totalNumOfIncForRRP);
        for (int incType = 0; incType < 5; incType++) {
            for (int incidentNum = 0; incidentNum < totalNumIncOfEachType[incType]; incidentNum++) {
                randIdx = rng.nextInt(tempArr.size());                    // RNG Changed
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
                    listIncidentEvents[incidentNum][3] = 15 * distArr1.remove(rng.nextInt(distArr1.size()));   // RNG Changed
                    break;
                case 1:
                    listIncidentEvents[incidentNum][3] = 15 * distArr2.remove(rng.nextInt(distArr2.size()));   // RNG Changed
                    break;
                case 2:
                    listIncidentEvents[incidentNum][3] = 15 * distArr3.remove(rng.nextInt(distArr3.size()));   // RNG Changed
                    break;
                case 3:
                    listIncidentEvents[incidentNum][3] = 15 * distArr4.remove(rng.nextInt(distArr4.size()));   // RNG Changed
                    break;
                case 4:
                    listIncidentEvents[incidentNum][3] = 15 * distArr5.remove(rng.nextInt(distArr5.size()));   // RNG Changed
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
        Collections.shuffle(selectionArrSTimes, rng);
        Collections.shuffle(selectionArrLoc, rng);

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

        int incidentIdx = 0;
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
                        Collections.shuffle(selectionArrEvents, rng);
                        Collections.shuffle(selectionArrLoc, rng);
                        Collections.shuffle(selectionArrSTimes, rng);
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
                    scenario.CAF().multiply(incidentData.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    scenario.OAF().multiply(incidentData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    scenario.DAF().multiply(incidentData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    scenario.SAF().multiply(incidentData.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                } else {
                    int endPeriod = (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15) - 1) % numPeriods;
                    scenario.CAF().multiply(incidentData.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    scenario.CAF().multiply(incidentData.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    scenario.OAF().multiply(incidentData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    scenario.OAF().multiply(incidentData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    scenario.DAF().multiply(incidentData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    scenario.DAF().multiply(incidentData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    scenario.SAF().multiply(incidentData.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    scenario.SAF().multiply(incidentData.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                }

                // update sCAF, sSAF, sOAF,SDaf and sLAF for all affected periods
                int adjPeriod = 0;
                for (int period = 0; period < listIncidentEvents[incidentIdx][3] / 15; period++) {
                    adjPeriod = (listIncidentEvents[incidentIdx][2] + period) % seed.getValueInt(IDS_NUM_PERIOD);
                    int oldLAF = scenario.LAFI().get(listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                    int currWZLAF = scenario.LAFWZ().get(listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                    int newLAF = Math.max(oldLAF + incidentData.getIncidentLAF(listIncidentEvents[incidentIdx][1], numLanes - 2), -1 * (numLanes + currWZLAF - 1));
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
     *
     * @param monthChangeIdx names, for debugging
     * @return
     */
    private int[][] createAndAssignListIncidentEventsML(int[] monthChangeIdx) {

        // Creating counters and marker variables for loops
        int numScenInMonth;
        int totalIncidentsInMonth = 0;
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
                    float lambda = incidentMLData.getIncidentFrequencyMonth(month);
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
                float lambda = incidentMLData.getIncidentFrequencyMonth(month);
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
                    int randIdx = rng.nextInt(tempArr.size());                // RNG Changed
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
            adjustedIncidentSeverityDistribution[incidentSeverityType] = incidentMLData.getIncidentDistributionDecimal(incidentSeverityType);
            distSum += incidentMLData.getIncidentDistributionDecimal(incidentSeverityType);
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
        int randIdx = 0;
        ArrayList<Integer> tempArr = createIntegerArrAscending(totalNumOfIncForRRP);
        for (int incType = 0; incType < 5; incType++) {
            for (int incidentNum = 0; incidentNum < totalNumIncOfEachType[incType]; incidentNum++) {
                randIdx = rng.nextInt(tempArr.size());                    // RNG Changed
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
                    listIncidentEvents[incidentNum][3] = 15 * distArr1.remove(rng.nextInt(distArr1.size()));
                    break;
                case 1:
                    listIncidentEvents[incidentNum][3] = 15 * distArr2.remove(rng.nextInt(distArr2.size()));
                    break;
                case 2:
                    listIncidentEvents[incidentNum][3] = 15 * distArr3.remove(rng.nextInt(distArr3.size()));
                    break;
                case 3:
                    listIncidentEvents[incidentNum][3] = 15 * distArr4.remove(rng.nextInt(distArr4.size()));
                    break;
                case 4:
                    listIncidentEvents[incidentNum][3] = 15 * distArr5.remove(rng.nextInt(distArr5.size()));
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
        Collections.shuffle(selectionArrSTimes, rng);
        Collections.shuffle(selectionArrLoc, rng);

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

        int incidentIdx = 0;
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
                        Collections.shuffle(selectionArrEvents, rng);
                        Collections.shuffle(selectionArrLoc, rng);
                        Collections.shuffle(selectionArrSTimes, rng);
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
                    mlScenario.CAF().multiply(incidentMLData.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    mlScenario.OAF().multiply(incidentMLData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    mlScenario.DAF().multiply(incidentMLData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                    mlScenario.SAF().multiply(incidentMLData.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15)) - 1);
                } else {
                    int endPeriod = (listIncidentEvents[incidentIdx][2] + (listIncidentEvents[incidentIdx][3] / 15) - 1) % numPeriods;
                    mlScenario.CAF().multiply(incidentMLData.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    mlScenario.CAF().multiply(incidentMLData.getIncidentCAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    mlScenario.OAF().multiply(incidentMLData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    mlScenario.OAF().multiply(incidentMLData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    mlScenario.DAF().multiply(incidentMLData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    mlScenario.DAF().multiply(incidentMLData.getIncidentDAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                    mlScenario.SAF().multiply(incidentMLData.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            listIncidentEvents[incidentIdx][2],
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            numPeriods - 1);
                    mlScenario.SAF().multiply(incidentMLData.getIncidentFFSAF(listIncidentEvents[incidentIdx][1], numLanes - 2),
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            0,
                            listIncidentEvents[incidentIdx][0],
                            listIncidentEvents[incidentIdx][4],
                            endPeriod);
                }

                // update sCAF, sSAF, sOAF,SDaf and sLAF for all affected periods
                int adjPeriod = 0;
                for (int period = 0; period < listIncidentEvents[incidentIdx][3] / 15; period++) {
                    adjPeriod = (listIncidentEvents[incidentIdx][2] + period) % seed.getValueInt(IDS_NUM_PERIOD);
                    int oldLAF = mlScenario.LAFI().get(listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                    int currWZLAF = mlScenario.LAFWZ().get(listIncidentEvents[incidentIdx][0], listIncidentEvents[incidentIdx][4], adjPeriod);
                    int newLAF = Math.max(oldLAF + incidentMLData.getIncidentLAF(listIncidentEvents[incidentIdx][1], numLanes - 2), -1 * (numLanes + currWZLAF - 1));
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
     *
     * @return
     */
        private boolean allInputValid() {
        return seed != null && demandData != null && weatherData != null && incidentData != null;
    }

    /**
     *
     * @return
     */
    private int calculateNumberOfScenarios() {
        int analysisYear = demandData.getYear();
        int startMonth = demandData.getStartMonth();
        int startDay = demandData.getStartDay();
        int endMonth = demandData.getEndMonth();
        int endDay = demandData.getEndDay();
        boolean[] activeDays = demandData.getActiveDays();
        int numActiveDaysPerWeek = arraySum(activeDays);

        int numDemandPatterns = 0;
        boolean[] tempBoolArr = new boolean[7];
        int numDaysInMonth = 0;
        int currDayType = 0;

        // Getting number days in startMonth (partial month)
        numDaysInMonth = daysInMonth(startMonth, analysisYear);
        for (int day = startDay; day <= numDaysInMonth; day++) {
            currDayType = dayOfWeek(day, startMonth, analysisYear);
            if (activeDays[currDayType] && tempBoolArr[currDayType] == false) {
                tempBoolArr[currDayType] = true;
            }
        }
        numDemandPatterns += arraySum(tempBoolArr);

        // Getting number of days in full months
        for (int monthIdx = startMonth + 1; monthIdx < endMonth; monthIdx++) {
            numDemandPatterns += numActiveDaysPerWeek;
        }

        // Getting number days in endMonth (partial month)
        //numDaysInMonth=daysInMonth(endMonth,analysisYear);
        tempBoolArr = new boolean[7];
        for (int day = 1; day <= endDay; day++) {
            currDayType = dayOfWeek(day, endMonth, analysisYear);
            if (activeDays[currDayType] && tempBoolArr[currDayType] == false) {
                tempBoolArr[currDayType] = true;
            }
        }
        numDemandPatterns += arraySum(tempBoolArr);
        return numDemandPatterns * numReplications;
    }

    /**
     *
     * @param numDayOfWeekInMonthAP
     * @return
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
     *
     * @param demandData
     * @return
     */
    public float[] createProbabilities(DemandData demandData) {
        int[] daysArr = CEDate.numDayOfWeekInMonthAP(demandData);
        excludeDays(daysArr);
        //System.out.println(daysArr[0]+" "+daysArr[1]+" "+daysArr[2]+" "+daysArr[3]+" "+daysArr[4]+" "+daysArr[5]+" "+daysArr[6]+" ");
        float[] probArr = new float[84];
        int totalNumDaysAP = arraySum(daysArr);
        for (int i = 0; i < daysArr.length; i++) {
            probArr[i] = ((float) daysArr[i]) / totalNumDaysAP;
            //probArr[i] = probArr[i]/4.0f;
            probArr[i] = probArr[i] / ((float) numReplications);
        }

        return probArr;
    }

    /**
     *
     * @param daysArr
     * @return
     */
    public int getTotalNumActiveDaysInAP(int[] daysArr) {
        /**
         * Input: integer array containing the number of times each day occurs
         * in each month of a year
         */

        // Count the number of active months in the analysis period
        int totalActiveDays = 0;
        for (int month = 0; month < 12; month++) {
            if (demandData.getMonthActive(month)) {
                for (int day = 0; day < 7; day++) {
                    if (demandData.getDayActive(day)) {
                        totalActiveDays += daysArr[month * 7 + day];
                    }
                }
            }
        }
        return totalActiveDays;
    }
        // </editor-fold>
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Utility Functions">

    /**
     *
     * @param arr
     * @return
     */
        private int arraySum(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    /**
     *
     * @param arr
     * @return
     */
    private int arraySum(int[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                sum += arr[i][j];
            }
        }
        return sum;
    }

    /**
     *
     * @param arr
     * @return
     */
    private int arraySum(ArrayList<Integer> arr) {
        int sum = 0;
        for (int i = 0; i < arr.size(); i++) {
            sum += arr.get(i);
        }
        return sum;
    }

    /**
     *
     * @param arr
     * @return
     */
    private float arraySum(float[] arr) {
        float sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    /**
     *
     * @param arr
     * @return
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
     *
     * @param arr
     * @param col
     * @return
     */
    private int arrayColSum(int[][] arr, int col) {
        int colSum = 0;
        for (int row = 0; row < arr.length; row++) {
            colSum += arr[row][col];
        }
        return colSum;

    }

    /**
     *
     * @param arr
     * @param col
     * @return
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
     *
     * @param arr
     * @return
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
     *
     * @param arr
     * @return
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
     *
     * @param arr
     * @param startTime
     * @param weatherType
     * @param adjFactorType
     * @return
     */
    private float[] applyEventToSegmentArray(float[] arr, int startTime, int weatherType, int adjFactorType) {
        //ArrayList<Float> newArr = new ArrayList<Float>(arr.size());
        //int numIncrements=weatherData.getAvgDurRoundedTo15MinIncrNumIncr(weatherType);
        int numIncrements = Math.min(weatherData.getAvgDurRoundedTo15MinIncrNumIncr(weatherType), seed.getValueInt(CEConst.IDS_NUM_PERIOD));
        int numAssigned = 0;
        while (numAssigned < numIncrements) {
            arr[(startTime + numAssigned) % arr.length] = weatherData.getAdjustmentFactor(adjFactorType, weatherType);
            numAssigned++;
        }

        return arr;
    }

    /**
     *
     * @param value
     * @return
     */
    private int factorial(int value) {
        int prod = 1;
        for (int i = 1; i <= value; i++) {
            prod *= i;
        }
        return prod;
    }

    /**
     *
     * @param length
     * @return
     */
    private ArrayList<Integer> createIntegerArrAscending(int length) {
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            arr.add(i);
        }
        return arr;
    }

    /**
     *
     * @param distArr
     * @return
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
     *
     * @param numIncOfEachType
     * @return
     */
    private float[][] generateLogNormalDurationProbabilites(int[] numIncOfEachType) {
        float[][] durationInfo = incidentData.getIncidentDurationInfo();
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
            mu = (float) Math.log(Math.pow(durationInfo[incType][0],2)/Math.sqrt(Math.pow(durationInfo[incType][1],2)+Math.pow(durationInfo[incType][0],2)));
            sigma = (float) Math.sqrt(Math.log(1+(Math.pow(durationInfo[incType][1],2)/Math.pow(durationInfo[incType][0],2))));
            //System.out.println(mu+", "+sigma);
            numInc = numIncOfEachType[incType];
            probArrayList.add(new ArrayList<Float>());
            probArrayList.get(incType).add(0.0f);
            descending = false;
            //currProb = 1.0f;
            lastProb = 0.0f;
            counter = 1;
            while(!(Math.round(lastProb*numInc) == 0 && descending)) {
                //currProb = generateLogNormalProbability(counter*15,mu,sigma);
                if (counter==1) {
                    currProb = logNormCDF(0.001f,(counter*15+7.5f), 0.001f,mu,sigma);
                } else {
                    currProb = logNormCDF((counter*15-7.5f),(counter*15+7.5f), 0.001f,mu,sigma);
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
     *
     * @param value
     * @param mean
     * @param stdev
     * @return
     */
    private float generateLogNormalProbability(float value, float mean, float stdev) {
        // exp(-0.5 * ((ln(x) - m) / s)^2) / (s * sqrt(2 * pi) * x)
        return (float) Math.exp(-0.5 * Math.pow(((Math.log(value) - mean) / stdev),2)) / ((float) (stdev * Math.sqrt(2 * Math.PI) * value));
    }
    
    /**
     *
     * @param value1
     * @param value2
     * @param step
     * @param mu
     * @param sigma
     * @return
     */
    private float logNormCDF(float value1, float value2, float step, float mu, float sigma) {
        float prob = 0;
        float currValue = value1; 
        while (currValue < value2) {
            currValue+=step;
            prob+=step*generateLogNormalProbability(currValue,mu,sigma);
        }
        return prob;
    }

    /**
     *
     * @return
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
     *
     * @param numIncOfEachType
     * @return
     */
    private int[][] getNumScenAssignedDurationLength(int[] numIncOfEachType) {
        //float[][] hardCodedDurationProbabilites = getHCDurProbs();
        float[][] durationProbabilities = generateLogNormalDurationProbabilites(numIncOfEachType);
        int[][] numScenAssignedDurLen = new int[durationProbabilities.length][durationProbabilities[0].length];
        
        //for (int i = 0; i < hardCodedDurationProbabilites.length; i++) {
        //    for (int j = 0; j < hardCodedDurationProbabilites[i].length; j++) {
        //        //System.out.println("HC: "+hardCodedDurationProbabilites[j][i]+", "+durationProbabilities[i][j]);
        //    }
        //}

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
                    //System.out.println(numScenAssignedDurLen[dur][incType]);
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
     *
     * @param fileName
     * @param tableArray
     */
    private void printLIETable(String fileName, int[][] tableArray) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "utf-8"));
            for (int i = 0; i < tableArray.length; i++) {
                String tempString = (i + 1) + ",";
                for (int j = 0; j < tableArray[i].length; j++) {
                    if (j == 1 || j == 4) {
                        tempString = tempString + (tableArray[i][j] + 1) + ",";
                    } else {
                        tempString = tempString + tableArray[i][j] + ",";
                    }
                }
                writer.write(tempString + "\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }

    /**
     *
     * @param fileName
     * @param tableArray
     * @param addFirstCol
     */
    private void printTable(String fileName, int[][] tableArray, boolean addFirstCol) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "utf-8"));
            for (int i = 0; i < tableArray.length; i++) {
                String tempString = "";
                if (addFirstCol) {
                    tempString = tempString + (i + 1) + ",";
                }
                for (int j = 0; j < tableArray[i].length; j++) {
                    tempString = tempString + tableArray[i][j] + ",";
                }
                writer.write(tempString + "\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }

    /**
     *
     * @param fileName
     * @param floatArr
     * @param tableArray
     * @param addFirstCol
     */
    private void printTable(String fileName, float[] floatArr, int[][] tableArray, boolean addFirstCol) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "utf-8"));
            for (int i = 0; i < tableArray.length; i++) {
                String tempString = "";
                if (addFirstCol) {
                    tempString = tempString + (i + 1) + ",";
                }
                tempString = tempString + floatArr[i] + ",";
                for (int j = 0; j < tableArray[i].length; j++) {
                    tempString = tempString + tableArray[i][j] + ",";
                }
                writer.write(tempString + "\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }

    /**
     *
     * @param fileName
     * @param floatArr1
     * @param floatArr2
     * @param tableArray
     * @param addFirstCol
     */
    private void printTable(String fileName, float[] floatArr1, float[] floatArr2, int[][] tableArray, boolean addFirstCol) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "utf-8"));
            for (int i = 0; i < tableArray.length; i++) {
                String tempString = "";
                if (addFirstCol) {
                    tempString = tempString + (i + 1) + ",";
                }
                tempString = tempString + floatArr1[i] + "," + floatArr2[i] + ",";
                for (int j = 0; j < tableArray[i].length; j++) {
                    tempString = tempString + tableArray[i][j] + ",";
                }
                writer.write(tempString + "\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }

    /**
     *
     * @param fileName
     * @param floatArr1
     * @param floatArr2
     * @param tableArrayList
     * @param addFirstCol
     */
    private void printTable(String fileName, float[] floatArr1, float[] floatArr2, ArrayList<ArrayList<Integer>> tableArrayList, boolean addFirstCol) {

        //Getting number of columns
        int columns = 0;
        //System.out.println(tableArrayList.size());
        for (int col = 0; col < tableArrayList.size(); col++) {
            if (tableArrayList.get(col).size() > columns) {
                columns = tableArrayList.get(col).size();
            }
        }

        ArrayList<Integer> tempArrayList;
        int[][] tableArray = new int[tableArrayList.size()][columns];
        for (int i = 0; i < tableArray.length; i++) {
            tempArrayList = tableArrayList.get(i);
            for (int j = 0; j < tempArrayList.size(); j++) {
                tableArray[i][j] = tempArrayList.get(j);
            }
        }

        //System.out.println(tableArray.length);
        //System.out.println(tableArray[0].length);
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "utf-8"));
            for (int i = 0; i < tableArray.length; i++) {
                String tempString = "";
                if (addFirstCol) {
                    tempString = tempString + (i + 1) + ",";
                }
                tempString = tempString + floatArr1[i] + "," + floatArr2[i] + ",";
                for (int j = 0; j < tableArray[i].length; j++) {
                    tempString = tempString + tableArray[i][j] + ",";
                }
                writer.write(tempString + "\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }

    /**
     *
     * @param fileName
     * @param floatArr1
     * @param floatArr2
     * @param floatArr3
     * @param tableArray
     * @param addFirstCol
     */
    private void printTable(String fileName, float[] floatArr1, float[] floatArr2, float[] floatArr3, int[][] tableArray, boolean addFirstCol) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "utf-8"));
            for (int i = 0; i < tableArray.length; i++) {
                String tempString = "";
                if (addFirstCol) {
                    tempString = tempString + (i + 1) + ",";
                }
                tempString = tempString + floatArr1[i] + "," + floatArr2[i] + "," + floatArr3[i] + ",";
                for (int j = 0; j < tableArray[i].length; j++) {
                    tempString = tempString + tableArray[i][j] + ",";
                }
                writer.write(tempString + "\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }

    /**
     *
     * @param fileName
     * @param floatArr1
     * @param floatArr2
     * @param floatArr3
     * @param tableArray
     * @param addFirstCol
     */
    private void printTable(String fileName, float[] floatArr1, float[] floatArr2, float[] floatArr3, int[] tableArray, boolean addFirstCol) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "utf-8"));
            for (int i = 0; i < tableArray.length; i++) {
                String tempString = "";
                if (addFirstCol) {
                    tempString = tempString + (i + 1) + ",";
                }
                tempString = tempString + floatArr1[i] + "," + floatArr2[i] + "," + floatArr3[i] + "," + tableArray[i] + ",";
                writer.write(tempString + "\n");
            }
        } catch (IOException ex) {
            System.err.println("error");
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }

    /**
     *
     * @param fileName
     * @param tableArray
     */
    private void printTable(String fileName, float[][] tableArray) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "utf-8"));
            for (int i = 0; i < tableArray.length; i++) {
                String tempString = (i + 1) + ",";
                for (int j = 0; j < tableArray[i].length; j++) {
                    if (j == 1) {
                        tempString = tempString + (tableArray[i][j] + 1) + ",";
                    } else {
                        tempString = tempString + tableArray[i][j] + ",";
                    }
                }
                writer.write(tempString + "\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }

    /**
     *
     */
    private void resetRNG() {
        this.rng = new Random(this.rngSeed);
    }

    /**
     *
     * @param factor
     */
    private void resetRNG(long factor) {
        this.rng = new Random(this.rngSeed * factor);
    }
    //</editor-fold>

    /**
     *
     */
    private class IncidentInfo implements Comparable {

            /**
             *
             */
            public int incIdx;

            /**
             *
             */
            public int severity;

            /**
             *
             * @param incIdx
             * @param severity
             */
            public IncidentInfo(int incIdx, int severity) {
            this.incIdx = incIdx;
            this.severity = severity;
        }

        @Override
        public int compareTo(Object o) {
            return Integer.compare(severity, ((IncidentInfo) o).severity);
        }

    }
}
