package reliabilityAnalysis.DataStruct;

import atdm.DataStruct.ATDMDatabase;
import atdm.DataStruct.ATDMPlan;
import atdm.DataStruct.ATDMScenario;
import coreEngine.CEConst;
import static coreEngine.CEConst.IDS_MAIN_NUM_LANES_IN;
import static coreEngine.CEConst.IDS_NUM_PERIOD;
import static coreEngine.CEConst.IDS_NUM_SEGMENT;
import coreEngine.CM2DInt;
import coreEngine.Seed;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class contains information for each Reliability Analysis scenario.
 * Should be 1-D array in Seed class only.
 *
 * @author Shu Liu
 * @author Lake Trask
 */
public class ScenarioInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5839875102332L;

    /**
     * Seed
     */
    private Seed seed;

    /**
     * Probability of a scenario
     */
    public float prob;

    /**
     * Group number of a scenario
     */
    public int group;

    /**
     * Name of a scenario
     */
    public String name;

    /**
     * Detail events of a scenario
     */
    public String detail;

    /**
     * Input/Output status of a scenario
     */
    public int statusRL = CEConst.SCENARIO_INPUT_ONLY;

    /**
     * Number of weather events
     */
    private int numWeatherEvents;

    /**
     * Number of incidents
     */
    private int numIncidents;

    /**
     * Number of work zones
     */
    private int numWorkZones;

    /**
     * Name of demand pattern
     */
    private String demandPatternName;

    /**
     *
     */
    private float demandMultiplier;

    /**
     * Whether this scenario has weather event
     */
    private boolean hasWeatherEvent = false;

    /**
     * Whether this scenario has incident
     */
    private boolean hasIncident = false;

    /**
     * Whether this scenario has a work zone
     */
    private boolean hasWorkZone = false;

    /**
     * Start time (period index, start with 0) of weather event
     */
    private ArrayList<Integer> weatherEventStartTimes;

    /**
     * Duration (number of periods) of weather event
     */
    private ArrayList<Integer> weatherEventDurations;

    /**
     * Start time (period index, start with 0) of incident
     */
    private ArrayList<Integer> incidentEventStartTimes;

    /**
     * Duration (number of periods) of incident
     */
    private ArrayList<Integer> incidentEventDurations;

    /**
     * Location (segment index, start with 0) of incident
     */
    private ArrayList<Integer> incidentEventLocations;

    /**
     * Incident Types (0 - Shoulder closure, 1 - 1 lane closure, etc.)
     */
    private ArrayList<Integer> incidentEventTypes;

    /**
     * List of work zones
     */
    private ArrayList<WorkZoneData> workZones;


    /**
     * Constructor of ScenarioInfo class
     */
    public ScenarioInfo() {
        this(0, 0, "");
    }

    /**
     * Constructor of ScenarioInfo class
     *
     * @param prob Probability of a scenario
     */
    public ScenarioInfo(float prob) {
        this(prob, 0, "");
    }

    /**
     * Constructor of ScenarioInfo class
     *
     * @param prob Probability of a scenario
     * @param group Group number of a scenario
     */
    public ScenarioInfo(float prob, int group) {
        this(prob, group, "");
    }

    /**
     * Constructor of ScenarioInfo class
     *
     * @param prob Probability of a scenario
     * @param group Group number of a scenario
     * @param name Name of a scenario
     */
    public ScenarioInfo(float prob, int group, String name) {
        this.prob = prob;
        this.group = group;
        this.demandPatternName = name;

        this.numWeatherEvents = 0;
        this.numIncidents = 0;
        this.numWorkZones = 0;

        weatherEventStartTimes = new ArrayList<>();  // Stored as AP # in which event begins
        weatherEventDurations = new ArrayList<>();   // Stored as # of APs event lasts

        incidentEventStartTimes = new ArrayList<>(); // Stored as AP # in which event begins
        incidentEventDurations = new ArrayList<>();  // Stored as # of APs event lasts
        incidentEventLocations = new ArrayList<>();  // Stored as index of segment at which it occurs
        incidentEventTypes = new ArrayList<>();

        workZones = new ArrayList<>();

        updateName();
    }

    // <editor-fold defaultstate="collapsed" desc="Adders">
    /**
     *
     * @param workZone
     */
    public void addWorkZone(WorkZoneData workZone) {
        workZones.add(workZone);
        numWorkZones++;
        if (hasWorkZone == false) {
            hasWorkZone = true;
        }

        // Making Spacing look nice
        if (numWorkZones == 1) {
            detail = detail + "\n\n Work Zones:\n  ";
        } else {
            detail = detail + "\n  ";
        }

        detail = detail + " " + workZone.getSeverityString() + " at segment(s) "
                + (workZone.getStartSegment()) + " - " + (workZone.getEndSegment())
                + " daily for periods " + (workZone.getStartPeriod())
                + " - " + workZone.getEndPeriod();

        updateName();
    }

    /**
     * Add a weather event to a scenario
     *
     * @param weatherType type of the weather
     * @param startPeriod start period (0 is the first period) of the weather
     * event
     * @param eventDuration duration (in number of periods) of the weather event
     */
    public void addWeatherEvent(int weatherType, int startPeriod, int eventDuration) {

        if (!hasWeatherEvent) {
            hasWeatherEvent = true;
        }

        // Updating weather event info
        numWeatherEvents++;
        weatherEventStartTimes.add(startPeriod);
        weatherEventDurations.add(eventDuration);

        if (numWeatherEvents == 1) {
            detail = detail + "\n\n Weather Events:\n  ";
        } else {
            detail = detail + "\n  ";
        }

        if (eventDuration > 1) {
            detail = detail + WeatherData.getWeatherTypeFull(weatherType) + ": Starting in period " + (startPeriod + 1) + " for " + Math.min(eventDuration, seed.getValueInt(IDS_NUM_PERIOD)) + " periods. ";
        } else {
            detail = detail + WeatherData.getWeatherTypeFull(weatherType) + ": Starting in period " + (startPeriod + 1) + " for " + eventDuration + " period. ";
        }
        updateName();
    }

    /**
     * Add an incident event to a scenario
     *
     * @param incType incident type
     * @param startPeriod start period (0 is the first period) of the incident
     * event
     * @param eventDuration duration (in number of periods) of the incident
     * event
     * @param segment location of the incident event (0 is the first segment)
     */
    public void addIncident(int incType, int startPeriod, int eventDuration, int segment) {
        if (!hasIncident) {
            hasIncident = true;
        }

        // Updating incident event info
        numIncidents++;
        incidentEventStartTimes.add(startPeriod);
        incidentEventDurations.add(eventDuration);
        incidentEventLocations.add(segment);
        incidentEventTypes.add(incType);

        if (numIncidents == 1) {
            detail = detail + "\n\n Incidents:\n  ";
        } else {
            detail = detail + "\n  ";
        }

        if (eventDuration > 1) {
            detail = detail + IncidentData.getIncidentTypeFull(incType) + ": At segment " + (segment + 1) + " starting in period " + (startPeriod + 1) + " for " + Math.min(eventDuration, seed.getValueInt(IDS_NUM_PERIOD)) + " periods. ";
        } else {
            detail = detail + IncidentData.getIncidentTypeFull(incType) + ": At segment " + (segment + 1) + " starting in period " + (startPeriod + 1) + " for " + eventDuration + " period. ";
        }
        updateName();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Has event functions">
    /**
     * Whether this scenario has weather event
     *
     * @return Whether this scenario has weather event
     */
    public boolean hasWeatherEvent() {
        return hasWeatherEvent;
    }

    /**
     * Whether this scenario has incident event
     *
     * @return Whether this scenario has incident event
     */
    public boolean hasIncident() {
        return hasIncident;
    }

    /**
     *
     * @return
     */
    public boolean hasWorkZone() {
        return hasWorkZone;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Overlap Checks">
    /**
     * Check whether weather event overlap previously assigned weather event
     *
     * @param eventStartTime start period (0 is the first period) of the weather
     * event
     * @param eventDuration duration (in number of periods) of the weather event
     * @return boolean overlap: false if event does not overlap any previously
     * assigned event, true otherwise.
     */
    public boolean checkWeatherOverlap(int eventStartTime, int eventDuration) {
        boolean overlap = false;
        for (int event = 0; event < numWeatherEvents; event++) {
            int assignedEventStartTime = weatherEventStartTimes.get(event);
            int assignedEventDuration = weatherEventDurations.get(event);

            if (eventStartTime <= assignedEventStartTime && eventStartTime + eventDuration > assignedEventStartTime) {
                overlap = true;
                break;
            } else if (eventStartTime > assignedEventStartTime && eventStartTime < assignedEventStartTime + assignedEventDuration) {
                overlap = true;
                break;
            } else if (eventStartTime < assignedEventStartTime && eventStartTime < ((assignedEventStartTime + assignedEventDuration) % seed.getValueInt(IDS_NUM_PERIOD))) {
                overlap = true;
                break;
            } else if (assignedEventDuration >= seed.getValueInt(IDS_NUM_PERIOD)) {
                overlap = true;
                break;
            }
        }

        return overlap;
    }

    /**
     * Check whether incident event overlap previously assigned incident event
     *
     * @param eventStartTime start period (0 is the first period) of the
     * incident event
     * @param eventDuration duration (in number of periods) of the incident
     * event
     * @param location location of the incident event (0 is the first segment)
     * @return boolean overlap: false if event does not overlap any previously
     * assigned event, true otherwise.
     */
    public boolean checkIncidentOverlap(int eventStartTime, int eventDuration, int location) {
        boolean overlap = false;
        for (int event = 0; event < numIncidents; event++) {
            if (incidentEventLocations.get(event) == location) {
                int assignedEventStartTime = incidentEventStartTimes.get(event);
                int assignedEventDuration = incidentEventDurations.get(event);

                if (eventStartTime <= assignedEventStartTime && eventStartTime + eventDuration > assignedEventStartTime) {
                    overlap = true;
                    break;
                } else if (eventStartTime > assignedEventStartTime && eventStartTime < assignedEventStartTime + assignedEventDuration) {
                    overlap = true;
                    break;
                } else if (eventStartTime < assignedEventStartTime && eventStartTime < ((assignedEventStartTime + assignedEventDuration) % seed.getValueInt(IDS_NUM_PERIOD))) {
                    overlap = true;
                    break;
                } else if (assignedEventDuration >= seed.getValueInt(IDS_NUM_PERIOD)) {
                    overlap = true;
                    break;
                }
            } else {
                // If the incident does not occur in the same location, then temporal overlap is unimportant.
            }
        }
        return overlap;
    }

    /**
     * Check whether incident event overlap previously assigned incident event
     *
     * @param eventStartTime start period (0 is the first period) of the
     * incident event
     * @param eventDuration duration (in number of periods) of the incident
     * event
     * @param location location of the incident event (0 is the first segment)
     * @return boolean overlap: false if event does not overlap any previously
     * assigned event, true otherwise.
     */
    public boolean checkWorkZoneOverlap(int eventStartTime, int eventDuration, int location) {
        boolean overlap = false;
        for (int event = 0; event < workZones.size(); event++) {
            if (workZones.get(event).getStartSegment() <= location && workZones.get(event).getEndSegment() >= location) {
                int assignedEventStartTime = workZones.get(event).getStartPeriod();
                int assignedEventDuration = workZones.get(event).getDuration();

                if (eventStartTime <= assignedEventStartTime && eventStartTime + eventDuration > assignedEventStartTime) {
                    overlap = true;
                } else if (eventStartTime > assignedEventStartTime && eventStartTime < assignedEventStartTime + assignedEventDuration) {
                    overlap = true;
                } else if (eventStartTime < assignedEventStartTime && eventStartTime < ((assignedEventStartTime + assignedEventDuration) % seed.getValueInt(IDS_NUM_PERIOD))) {
                    overlap = true;
                } else if (assignedEventDuration >= seed.getValueInt(IDS_NUM_PERIOD)) {
                    overlap = true;
                }
            } else // If the incident does not occur in the same location, then temporal overlap is unimportant.
            {
                overlap = false; // not needed, already false?
            }
        }

        return overlap;
    }
    // </editor-fold>

    /**
     * Update scenario name
     */
    private void updateName() {
        name = demandPatternName;

        if (demandMultiplier != 0.0f) {
            name = name + " (" + String.format("%.3f", demandMultiplier) + ")";
        }

        if (numWorkZones > 0) {
            name = name + "  " + numWorkZones + "WZ";
        }

        if (numWeatherEvents > 0) {
            name = name + "   " + numWeatherEvents + "W";
        }
        if (numIncidents > 0) {
            name = name + "   " + numIncidents + "I";
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Setters">
    /**
     * Setter for seed
     *
     * @param seed
     */
    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    /**
     * Setter for demand pattern name
     *
     * @param demandPatternName name of the demand pattern
     */
    public void setDemandPatternName(String demandPatternName) {
        this.demandPatternName = demandPatternName;
        updateName();
    }

    /**
     *
     * @param demandMultiplier
     */
    public void setDemandMultiplier(float demandMultiplier) {
        this.demandMultiplier = demandMultiplier;
        detail = " Seed Demand Multiplier: " + this.demandMultiplier + " ";
        updateName();
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * Getter for number of weather events
     *
     * @return number of weather events
     */
    public int getNumberOfWeatherEvents() {
        return numWeatherEvents;
    }

    /**
     * Getter for number of incident events
     *
     * @return number of incident events
     */
    public int getNumberOfIncidentEvents() {
        return numIncidents;
    }

    /**
     * Getter for number of work zones
     *
     * @return number of incident events
     */
    public int getNumberOfWorkZones() {
        return numWorkZones;
    }

    /**
     *
     * @return
     */
    public int getMonth() {
        String[] tempString = name.split(" ");
        String monthString = tempString[0].split("-")[0];

        int monthIdx = -1;

        switch (monthString) {
            case "Jan":
                monthIdx = 0;
                break;
            case "Feb":
                monthIdx = 1;
                break;
            case "Mar":
                monthIdx = 2;
                break;
            case "Apr":
                monthIdx = 3;
                break;
            case "May":
                monthIdx = 4;
                break;
            case "Jun":
                monthIdx = 5;
                break;
            case "Jul":
                monthIdx = 6;
                break;
            case "Aug":
                monthIdx = 7;
                break;
            case "Sep":
                monthIdx = 8;
                break;
            case "Oct":
                monthIdx = 9;
                break;
            case "Nov":
                monthIdx = 10;
                break;
            case "Dec":
                monthIdx = 11;
                break;
        }
        return monthIdx;
    }

    public int getDayType() {
        String[] tempString = name.split(" ");
        String dayName = tempString[0].split("-")[1];
        switch (dayName) {
            case "Mon":
            case "mon":
            case "Monday":
            case "monday":
                return 0;
            case "Tue":
            case "tue":
            case "Tuesday":
            case "tuesday":
                return 1;
            case "Wed":
            case "wed":
            case "Wednesday":
            case "wednesday":
                return 2;
            case "Thu":
            case "thu":
            case "Thursday":
            case "thursday":
                return 3;
            case "Fri":
            case "fri":
            case "Friday":
            case "friday":
                return 4;
            case "Sat":
            case "sat":
            case "Saturday":
            case "saturday":
                return 5;
            case "Sun":
            case "sun":
            case "Sunday":
            case "sunday":
                return 6;
            default:
                return -1;
        }
    }

    /**
     * Retrieves the specified demand for the correct month - pattern from the
     * float[][] array containing the specified demand for all demand patterns.
     *
     * @param specifiedDemand
     * @return
     */
    public float getScenarioSpecifiedDemand(float[][] specifiedDemand) {

        if (name.equalsIgnoreCase("")) {
            return 1.0f;
        } else {
            String[] tempString = name.split(" ");
            String monthString = tempString[0].split("-")[0];
            String dayString = tempString[0].split("-")[1];

            int monthIdx = 0;
            int dayIdx = 0;

            switch (monthString) {
                case "Jan":
                    monthIdx = 0;
                    break;
                case "Feb":
                    monthIdx = 1;
                    break;
                case "Mar":
                    monthIdx = 2;
                    break;
                case "Apr":
                    monthIdx = 3;
                    break;
                case "May":
                    monthIdx = 4;
                    break;
                case "Jun":
                    monthIdx = 5;
                    break;
                case "Jul":
                    monthIdx = 6;
                    break;
                case "Aug":
                    monthIdx = 7;
                    break;
                case "Sep":
                    monthIdx = 8;
                    break;
                case "Oct":
                    monthIdx = 9;
                    break;
                case "Nov":
                    monthIdx = 10;
                    break;
                case "Dec":
                    monthIdx = 11;
                    break;
            }

            switch (dayString) {
                case "Mon":
                    dayIdx = 0;
                    break;
                case "Tue":
                    dayIdx = 1;
                    break;
                case "Wed":
                    dayIdx = 2;
                    break;
                case "Thur":
                    dayIdx = 3;
                    break;
                case "Fri":
                    dayIdx = 4;
                    break;
                case "Sat":
                    dayIdx = 5;
                    break;
                case "Sun":
                    dayIdx = 6;
                    break;
            }

            return specifiedDemand[monthIdx][dayIdx];
        }
    }

    /**
     *
     * @return
     */
    public float getDemandMultiplier() {
        return this.demandMultiplier;
    }

    /**
     *
     * @return
     */
    public String getWorkZoneDetail() {
        if (numWorkZones > 0) {
            return detail.split("\n\n")[1];
        } else {
            return "";
        }
    }

    /**
     *
     * @return
     */
    public String getWeatherDetail() {
        if (numWeatherEvents > 0) {
            if (numWorkZones > 0) {
                return detail.split("\n\n")[2];
            } else {
                return detail.split("\n\n")[1];
            }
        } else {
            return "";
        }
    }

    /**
     *
     * @return
     */
    public String getIncidentDetail() {
        if (numIncidents > 0) {
            if (numWorkZones > 0) {
                if (numWeatherEvents > 0) {
                    return detail.split("\n\n")[3];
                } else {
                    return detail.split("\n\n")[2];
                }
            } else {
                if (numWeatherEvents > 0) {
                    return detail.split("\n\n")[2];
                } else {
                    return detail.split("\n\n")[1];
                }
            }
        } else {
            return "";
        }
    }

    /**
     * Returns an ATDMScenario object that reflects the application of the
     * strategies in the specified ATDM plan.
     *
     * @param atdmPlan
     * @return
     */
    public ATDMScenario generateATDMScenario(ATDMPlan atdmPlan) {
        int numAnalysisPeriods = seed.getValueInt(IDS_NUM_PERIOD);
        int numSegments = seed.getValueInt(IDS_NUM_SEGMENT);

        ATDMScenario tempATDMScenario = new ATDMScenario(numSegments, numAnalysisPeriods);
        tempATDMScenario.setName(atdmPlan.getName());
        tempATDMScenario.setDiscription(atdmPlan.getInfo());

        float[][] afArray = atdmPlan.getATDMadjFactors();

        // Demand management strategies (applied for all segments, all periods)
        tempATDMScenario.DAF().set(afArray[0][0],
                0, 0,
                numSegments - 1, numAnalysisPeriods - 1);
        tempATDMScenario.OAF().set(afArray[0][0],
                0, 0,
                numSegments - 1, numAnalysisPeriods - 1);

        // Work Zone ATDM applied only if scenario has a work zone
        // (does not wrap)
        if (hasWorkZone) {
            for (int wzIdx = 0; wzIdx < numWorkZones; wzIdx++) {
                tempATDMScenario.OAF().multiply(afArray[3][0],
                        workZones.get(wzIdx).getStartSegment(),
                        workZones.get(wzIdx).getStartPeriod(),
                        workZones.get(wzIdx).getEndSegment() - 1,
                        workZones.get(wzIdx).getEndPeriod() - 1);
                tempATDMScenario.DAF().multiply(afArray[3][0],
                        workZones.get(wzIdx).getStartSegment(),
                        workZones.get(wzIdx).getStartPeriod(),
                        workZones.get(wzIdx).getEndSegment() - 1,
                        workZones.get(wzIdx).getEndPeriod() - 1);
                tempATDMScenario.SAF().multiply(afArray[3][1],
                        workZones.get(wzIdx).getStartSegment(),
                        workZones.get(wzIdx).getStartPeriod(),
                        workZones.get(wzIdx).getEndSegment() - 1,
                        workZones.get(wzIdx).getEndPeriod() - 1);
                tempATDMScenario.CAF().multiply(afArray[3][2],
                        workZones.get(wzIdx).getStartSegment(),
                        workZones.get(wzIdx).getStartPeriod(),
                        workZones.get(wzIdx).getEndSegment() - 1,
                        workZones.get(wzIdx).getEndPeriod() - 1);
            }

        }

        // Weather applied only if weather event in period
        // (Does wrap time periods)
        int startTime;
        int dur;
        if (hasWeatherEvent) {;
            for (int wIdx = 0; wIdx < numWeatherEvents; wIdx++) {
                startTime = weatherEventStartTimes.get(wIdx);
                dur = weatherEventDurations.get(wIdx);
                if (startTime + dur <= numAnalysisPeriods) {
                    tempATDMScenario.OAF().multiply(afArray[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                    tempATDMScenario.DAF().multiply(afArray[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                    tempATDMScenario.SAF().multiply(afArray[1][1],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                    tempATDMScenario.CAF().multiply(afArray[1][2],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                } else {
                    int endTime = (startTime + dur) % numAnalysisPeriods;
                    tempATDMScenario.OAF().multiply(afArray[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.OAF().multiply(afArray[1][0],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);
                    tempATDMScenario.DAF().multiply(afArray[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.DAF().multiply(afArray[1][0],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);
                    tempATDMScenario.SAF().multiply(afArray[1][1],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.SAF().multiply(afArray[1][1],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);
                    tempATDMScenario.CAF().multiply(afArray[1][2],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.CAF().multiply(afArray[1][2],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);

                }
            }
        }

        if (hasIncident) {
            int seg;
            int incType;
            int numLanes;
            int endTime;
            int durReduction = atdmPlan.getIncidentDurationReduction();
            for (int incIdx = 0; incIdx < numIncidents; incIdx++) {
                seg = incidentEventLocations.get(incIdx);
                incType = incidentEventTypes.get(incIdx);
                numLanes = seed.getValueInt(IDS_MAIN_NUM_LANES_IN, seg);
                startTime = incidentEventStartTimes.get(incIdx);
                dur = incidentEventDurations.get(incIdx);
                if (dur <= durReduction) {                                        // Case 1: Duration is shorter than duration reduction
                    // Reverse adjustmentFactors applied to scenario
                    endTime = startTime + dur;
                    if (endTime <= numAnalysisPeriods) {                        // Case 1a: incident does not wrap
                        tempATDMScenario.OAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                endTime - 1);
                        tempATDMScenario.DAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                endTime - 1);
                        tempATDMScenario.SAF().multiply((1.0f / seed.getIncidentSAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                endTime - 1);
                        tempATDMScenario.CAF().multiply((1.0f / seed.getIncidentCAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                endTime - 1);
                        // Setting the new LAF to be that of just the workzone (equivalant to removing incident LAF)
                        int newLAF = Math.min((-1 * seed.getIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
                        tempATDMScenario.LAF().add(newLAF,
                                seg,
                                startTime,
                                seg,
                                endTime - 1);

                    } else {                                                    // Case 1b: Incident wraps
                        endTime = endTime % numAnalysisPeriods;
                        tempATDMScenario.OAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                numAnalysisPeriods - 1);
                        tempATDMScenario.OAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                0,
                                seg,
                                endTime - 1);
                        tempATDMScenario.DAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                numAnalysisPeriods - 1);
                        tempATDMScenario.DAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                0,
                                seg,
                                endTime - 1);
                        tempATDMScenario.SAF().multiply((1.0f / seed.getIncidentSAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                numAnalysisPeriods - 1);
                        tempATDMScenario.SAF().multiply((1.0f / seed.getIncidentSAF()[incType][numLanes - 2]),
                                seg,
                                0,
                                seg,
                                endTime - 1);
                        tempATDMScenario.CAF().multiply((1.0f / seed.getIncidentCAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                numAnalysisPeriods - 1);
                        tempATDMScenario.CAF().multiply((1.0f / seed.getIncidentCAF()[incType][numLanes - 2]),
                                seg,
                                0,
                                seg,
                                endTime - 1);

                        int newLAF = Math.min((-1 * seed.getIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
                        tempATDMScenario.LAF().add(newLAF,
                                seg,
                                startTime,
                                seg,
                                numAnalysisPeriods - 1);
                        tempATDMScenario.LAF().add(newLAF,
                                seg,
                                0,
                                seg,
                                endTime - 1);

                    }
                } else {                                                        // Case 2: Duration is greater than duration reduction
                    endTime = startTime + dur;
                    if (endTime <= numAnalysisPeriods) {                        // Case 2a: incident does not wrap
                        tempATDMScenario.OAF().multiply(afArray[2][0],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.OAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);
                        tempATDMScenario.DAF().multiply(afArray[2][0],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.DAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);
                        tempATDMScenario.SAF().multiply(afArray[2][1],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.SAF().multiply((1.0f / seed.getIncidentSAF()[incType][numLanes - 2]),
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);
                        tempATDMScenario.CAF().multiply(afArray[2][2],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.CAF().multiply((1.0f / seed.getIncidentCAF()[incType][numLanes - 2]),
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);

                        int newLAF = Math.min((-1 * seed.getIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
                        tempATDMScenario.LAF().add(0,
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.LAF().add(newLAF,
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);

                    } else {                                                    // Case 2b: incident wraps
                        if ((endTime - durReduction) <= numAnalysisPeriods) {     // Case 2b1: wrap occurs during duration reduction
                            int wrappedEndTime = endTime % numAnalysisPeriods;
                            tempATDMScenario.OAF().multiply(afArray[2][0],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    0,
                                    seg,
                                    wrappedEndTime - 1);
                            tempATDMScenario.DAF().multiply(afArray[2][0],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    0,
                                    seg,
                                    wrappedEndTime - 1);
                            tempATDMScenario.SAF().multiply(afArray[2][1],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getIncidentSAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getIncidentSAF()[incType][numLanes - 2]),
                                    seg,
                                    0,
                                    seg,
                                    wrappedEndTime - 1);
                            tempATDMScenario.CAF().multiply(afArray[2][2],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getIncidentCAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getIncidentCAF()[incType][numLanes - 2]),
                                    seg,
                                    0,
                                    seg,
                                    wrappedEndTime - 1);

                            int newLAF = Math.min((-1 * seed.getIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
                            tempATDMScenario.LAF().add(0,
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.LAF().add(newLAF,
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.LAF().add(newLAF,
                                    seg,
                                    0,
                                    seg,
                                    wrappedEndTime - 1);

                        } else {                                                // Case 2b2: wrap occurs before duration reduction
                            endTime = endTime % numAnalysisPeriods;
                            tempATDMScenario.OAF().multiply(afArray[2][0],
                                    seg,
                                    startTime,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.OAF().multiply(afArray[2][0],
                                    seg,
                                    0,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    endTime - 1);
                            tempATDMScenario.DAF().multiply(afArray[2][0],
                                    seg,
                                    startTime,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.DAF().multiply(afArray[2][0],
                                    seg,
                                    0,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    endTime - 1);
                            tempATDMScenario.SAF().multiply(afArray[2][1],
                                    seg,
                                    startTime,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.SAF().multiply(afArray[2][1],
                                    seg,
                                    0,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getIncidentSAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    endTime - 1);
                            tempATDMScenario.CAF().multiply(afArray[2][2],
                                    seg,
                                    startTime,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.CAF().multiply(afArray[2][2],
                                    seg,
                                    0,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getIncidentCAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    endTime - 1);

                            int newLAF = Math.min((-1 * seed.getIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
                            tempATDMScenario.LAF().add(0,
                                    seg,
                                    startTime,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.LAF().add(0,
                                    seg,
                                    0,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.LAF().add(newLAF,
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    endTime - 1);

                        }
                    }
                }
            }
        }
        
        if (atdmPlan.hasShoulderOpening()) {
            CM2DInt hsrMat = atdmPlan.getHSRMatrix();
            for (int segment = 0; segment < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); segment++) {
                int numLanesSegment = seed.getValueInt(IDS_MAIN_NUM_LANES_IN, segment);
                //System.out.println(numLanesSegment);
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    if (hsrMat.get(segment, period) == 1) {
                        tempATDMScenario.LAF().add(1, segment, period); // Adding lane
                        
                        // Calculating new segment CAF using shoulder CAF
                        float rlCAF = seed.getRLCAF(group+1, segment, period);
                        //System.out.println(rlCAF);
                        float newCAF = ((numLanesSegment * rlCAF * tempATDMScenario.CAF().get(segment, period))+atdmPlan.getHSRCAF()) / (numLanesSegment+1);
                        //System.out.println(newCAF);
                        tempATDMScenario.CAF().set((newCAF/rlCAF),segment,period);
                    }
                }
            }
        }

        if (atdmPlan.hasRampMetering()) {
            tempATDMScenario.RM().deepCopyFrom(atdmPlan.getRMRate());
        }
        return tempATDMScenario;
    }

    public ATDMScenario applyAndGetATDMScenario(ATDMDatabase atdmDatabase, int planIdx) {

        return generateATDMScenario(atdmDatabase.getPlan(planIdx));

    }
    // </editor-fold>
}
