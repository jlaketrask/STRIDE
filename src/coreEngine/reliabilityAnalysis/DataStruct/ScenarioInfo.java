package coreEngine.reliabilityAnalysis.DataStruct;

import CompressArray.CA2DInt;
import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import coreEngine.atdm.DataStruct.ATDMDatabase;
import coreEngine.atdm.DataStruct.ATDMPlan;
import coreEngine.atdm.DataStruct.ATDMScenario;
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

    // <editor-fold defaultstate="collapsed" desc="Class and Instance Variables">
    private static final long serialVersionUID = 5839875102332L;

    /**
     * Seed instance from which the scenarios have been created.
     */
    private Seed seed;

    /**
     * Probability of a scenario
     */
    public float prob;

    /**
     * Group number of a scenario. Also the scenario index
     */
    public int group;

    /**
     * Name of the scenario.
     */
    public String name;

    /**
     * Detail containing event summary of a scenario.
     */
    public String detail;

    /**
     * Month of the scenario. (1 - Jan, 2 - Feb, etc.)
     */
    public int month;

    /**
     * Day of week represented by the scenario. (0 - Mon, 1 - Tue, 2 - Wed, 3 -
     * Thu, 4 - Fri, 5 - Sat, 6 - Sun)
     */
    public int day;

    /**
     * Input/Output status of a scenario.
     */
    public int statusRL = CEConst.SCENARIO_INPUT_ONLY;

    /**
     * Number of weather events for the scenario.
     */
    private int numWeatherEvents;

    /**
     * Number of incidents on general purpose segments.
     */
    private int numIncidentsGP;

    /**
     * Number of incidents on managed lane segments.
     */
    private int numIncidentsML;

    /**
     * Number of work zones occurring in the scenario.
     */
    private int numWorkZones;

    /**
     * Name of demand pattern. (i.e. Jan - Mon)
     */
    private String demandPatternName;

    /**
     * Demand Multiplier corresponding to the Scenario demand pattern.
     */
    private float demandMultiplier;

    /**
     * Whether this scenario has weather event.
     */
    private boolean hasWeatherEvent = false;

    /**
     * Whether this scenario has any GP incidents.
     */
    private boolean hasIncidentGP = false;

    /**
     * Whether this scenario has any ML incidents.
     */
    private boolean hasIncidentML = false;

    /**
     * Whether this scenario has a work zone.
     */
    private boolean hasWorkZone = false;

    /**
     * Start time (period index, start with 0) of weather event.
     */
    private ArrayList<Integer> weatherEventStartTimes;

    /**
     * Duration (number of periods) of weather event.
     */
    private ArrayList<Integer> weatherEventDurations;

    /**
     * Start time (period index, start with 0) of GP incidents.
     */
    private ArrayList<Integer> incidentGPEventStartTimes;

    /**
     * Duration (number of periods) of GP incidents.
     */
    private ArrayList<Integer> incidentGPEventDurations;

    /**
     * Location (segment index, start with 0) of GP incidents.
     */
    private ArrayList<Integer> incidentGPEventLocations;

    /**
     * GP Incident Types (0 - Shoulder closure, 1 - 1 lane closure, etc.).
     */
    private ArrayList<Integer> incidentGPEventTypes;

    /**
     * Start time (period index, start with 0) of ML incidents.
     */
    private ArrayList<Integer> incidentMLEventStartTimes;

    /**
     * Duration (number of periods) of ML incidents.
     */
    private ArrayList<Integer> incidentMLEventDurations;

    /**
     * Location (segment index, start with 0) of ML incidents.
     */
    private ArrayList<Integer> incidentMLEventLocations;

    /**
     * ML Incident Types (0 - Shoulder closure, 1 - 1 lane closure, etc.).
     */
    private ArrayList<Integer> incidentMLEventTypes;

    /**
     * List of work zones.
     */
    private ArrayList<WorkZoneData> workZones;

    // </editor-fold>
    /**
     * Constructor of ScenarioInfo class.
     */
    public ScenarioInfo() {
        this(0, 0, "");
    }

    /**
     * Constructor of ScenarioInfo class.
     *
     * @param prob Probability of a scenario
     */
    public ScenarioInfo(float prob) {
        this(prob, 0, "");
    }

    /**
     * Constructor of ScenarioInfo class.
     *
     * @param prob Probability of a scenario
     * @param group Group number of a scenario
     */
    public ScenarioInfo(float prob, int group) {
        this(prob, group, "");
    }

    /**
     * Constructor of ScenarioInfo class.
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
        this.numIncidentsGP = 0;
        this.numIncidentsML = 0;
        this.numWorkZones = 0;

        weatherEventStartTimes = new ArrayList<>();  // Stored as AP # in which event begins
        weatherEventDurations = new ArrayList<>();   // Stored as # of APs event lasts

        incidentGPEventStartTimes = new ArrayList<>(); // Stored as AP # in which event begins
        incidentGPEventDurations = new ArrayList<>();  // Stored as # of APs event lasts
        incidentGPEventLocations = new ArrayList<>();  // Stored as index of segment at which it occurs
        incidentGPEventTypes = new ArrayList<>();

        incidentMLEventStartTimes = new ArrayList<>(); // Stored as AP # in which event begins
        incidentMLEventDurations = new ArrayList<>();  // Stored as # of APs event lasts
        incidentMLEventLocations = new ArrayList<>();  // Stored as index of segment at which it occurs
        incidentMLEventTypes = new ArrayList<>();

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
            detail = detail + WeatherData.getWeatherTypeFull(weatherType) + ": Starting in period " + (startPeriod + 1) + " for " + Math.min(eventDuration, seed.getValueInt(CEConst.IDS_NUM_PERIOD)) + " periods. ";
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
    public void addIncidentGP(int incType, int startPeriod, int eventDuration, int segment) {
        if (!hasIncidentGP) {
            hasIncidentGP = true;
        }

        // Updating incident event info
        numIncidentsGP++;
        incidentGPEventStartTimes.add(startPeriod);
        incidentGPEventDurations.add(eventDuration);
        incidentGPEventLocations.add(segment);
        incidentGPEventTypes.add(incType);

        if (numIncidentsGP == 1) {
            detail = detail + "\n\n Incidents (GP):\n  ";
        } else {
            detail = detail + "\n  ";
        }

        if (eventDuration > 1) {
            detail = detail + IncidentData.getIncidentTypeFull(incType) + ": At segment " + (segment + 1) + " starting in period " + (startPeriod + 1) + " for " + Math.min(eventDuration, seed.getValueInt(CEConst.IDS_NUM_PERIOD)) + " periods. ";
        } else {
            detail = detail + IncidentData.getIncidentTypeFull(incType) + ": At segment " + (segment + 1) + " starting in period " + (startPeriod + 1) + " for " + eventDuration + " period. ";
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
    public void addIncidentML(int incType, int startPeriod, int eventDuration, int segment) {
        if (!hasIncidentML) {
            hasIncidentML = true;
        }

        // Updating incident event info
        numIncidentsML++;
        incidentMLEventStartTimes.add(startPeriod);
        incidentMLEventDurations.add(eventDuration);
        incidentMLEventLocations.add(segment);
        incidentMLEventTypes.add(incType);

        if (numIncidentsML == 1) {
            detail = detail + "\n\n Incidents (ML):\n  ";
        } else {
            detail = detail + "\n  ";
        }

        if (eventDuration > 1) {
            detail = detail + IncidentData.getIncidentTypeFull(incType) + ": At segment " + (segment + 1) + " starting in period " + (startPeriod + 1) + " for " + Math.min(eventDuration, seed.getValueInt(CEConst.IDS_NUM_PERIOD)) + " periods. ";
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
     * Whether this scenario has any GP incident events
     *
     * @return Whether this scenario has incident event
     */
    public boolean hasIncidentGP() {
        return hasIncidentGP;
    }

    /**
     * Whether this scenario has any ML incident events
     *
     * @return Whether this scenario has incident event
     */
    public boolean hasIncidentML() {
        return hasIncidentML;
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
            } else if (eventStartTime < assignedEventStartTime && eventStartTime < ((assignedEventStartTime + assignedEventDuration) % seed.getValueInt(CEConst.IDS_NUM_PERIOD))) {
                overlap = true;
                break;
            } else if (assignedEventDuration >= seed.getValueInt(CEConst.IDS_NUM_PERIOD)) {
                overlap = true;
                break;
            }
        }

        return overlap;
    }

    /**
     * Check whether a GP incident event overlaps any previously assigned
     * incident event
     *
     * @param eventStartTime start period (0 is the first period) of the
     * incident event
     * @param eventDuration duration (in number of periods) of the incident
     * event
     * @param location location of the incident event (0 is the first segment)
     * @return boolean overlap: false if event does not overlap any previously
     * assigned event, true otherwise.
     */
    public boolean checkGPIncidentOverlap(int eventStartTime, int eventDuration, int location) {
        boolean overlap = false;
        for (int event = 0; event < numIncidentsGP; event++) {
            if (incidentGPEventLocations.get(event) == location) {
                int assignedEventStartTime = incidentGPEventStartTimes.get(event);
                int assignedEventDuration = incidentGPEventDurations.get(event);

                if (eventStartTime <= assignedEventStartTime && eventStartTime + eventDuration > assignedEventStartTime) {
                    overlap = true;
                    break;
                } else if (eventStartTime > assignedEventStartTime && eventStartTime < assignedEventStartTime + assignedEventDuration) {
                    overlap = true;
                    break;
                } else if (eventStartTime < assignedEventStartTime && eventStartTime < ((assignedEventStartTime + assignedEventDuration) % seed.getValueInt(CEConst.IDS_NUM_PERIOD))) {
                    overlap = true;
                    break;
                } else if (assignedEventDuration >= seed.getValueInt(CEConst.IDS_NUM_PERIOD)) {
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
     * Check whether an ML incident event overlaps any previously assigned
     * incident event
     *
     * @param eventStartTime start period (0 is the first period) of the
     * incident event
     * @param eventDuration duration (in number of periods) of the incident
     * event
     * @param location location of the incident event (0 is the first segment)
     * @return boolean overlap: false if event does not overlap any previously
     * assigned event, true otherwise.
     */
    public boolean checkMLIncidentOverlap(int eventStartTime, int eventDuration, int location) {
        boolean overlap = false;
        for (int event = 0; event < numIncidentsML; event++) {
            if (incidentMLEventLocations.get(event) == location) {
                int assignedEventStartTime = incidentMLEventStartTimes.get(event);
                int assignedEventDuration = incidentMLEventDurations.get(event);

                if (eventStartTime <= assignedEventStartTime && eventStartTime + eventDuration > assignedEventStartTime) {
                    overlap = true;
                    break;
                } else if (eventStartTime > assignedEventStartTime && eventStartTime < assignedEventStartTime + assignedEventDuration) {
                    overlap = true;
                    break;
                } else if (eventStartTime < assignedEventStartTime && eventStartTime < ((assignedEventStartTime + assignedEventDuration) % seed.getValueInt(CEConst.IDS_NUM_PERIOD))) {
                    overlap = true;
                    break;
                } else if (assignedEventDuration >= seed.getValueInt(CEConst.IDS_NUM_PERIOD)) {
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
     * Check whether a workzone event overlaps any previously assigned workzone
     * event
     *
     * @param workZone
     * @return
     */
    public boolean checkWorkZoneOverlap(WorkZoneData workZone) {
        boolean overlap = false;
        for (WorkZoneData wz : workZones) {
            if (wz.hasOverlap(workZone)) {
                overlap = true;
                break;
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
        if (numIncidentsGP > 0) {
            name = name + "   " + numIncidentsGP + "IGP";
        }
        if (numIncidentsML > 0) {
            name = name + "   " + numIncidentsML + "IML";
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
     * Getter for number of GP incident events
     *
     * @return number of incident events
     */
    public int getNumberOfGPIncidentEvents() {
        return numIncidentsGP;
    }

    /**
     * Getter for number of ML incident events
     *
     * @return number of incident events
     */
    public int getNumberOfMLIncidentEvents() {
        return numIncidentsML;
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
     * Returns the month of the demand combination of the scenario. Indexing
     * starts at 0;
     *
     * @return
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the day type (day of week) of the demand combination of the
     * scenario. 0 - Monday, 1 - Tuesday, 2 - Wednesday, 3 - Thursday, 4 -
     * Friday, 5 - Saturday, 6 - Sunday.
     *
     * @return
     */
    public int getDayType() {
        return day;
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
    public String getGPIncidentDetail() {
        if (numIncidentsGP > 0) {
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

    //<editor-fold defaultstate="collapsed" desc="ATDM with diversion for incidents">
    //<editor-fold defaultstate="collapsed" desc="ATDMScenario Generation for GP">
    /**
     * Returns an ATDMScenario object that reflects the application of the
     * strategies in the specified ATDM plan for GP segments.
     *
     * @param atdmPlan ATDM plan to be applied to the reliability scenario.
     * @return ATDMScenario instance with the ATDM plan strategies applied.
     */
    public ATDMScenario generateATDMScenario(ATDMPlan atdmPlan) {
        int numAnalysisPeriods = seed.getValueInt(CEConst.IDS_NUM_PERIOD);
        int numSegments = seed.getValueInt(CEConst.IDS_NUM_SEGMENT);

        ATDMScenario tempATDMScenario = new ATDMScenario(numSegments, numAnalysisPeriods);
        tempATDMScenario.setName(atdmPlan.getName());
        tempATDMScenario.setDiscription(atdmPlan.getInfo());

        float[][] atdmAdjFactors = atdmPlan.getATDMadjFactors();

        // Demand management strategies (applied for all segments, all periods)
        tempATDMScenario.DAF().set(atdmAdjFactors[0][0],
                0, 0,
                numSegments - 1, numAnalysisPeriods - 1);
        tempATDMScenario.OAF().set(atdmAdjFactors[0][0],
                0, 0,
                numSegments - 1, numAnalysisPeriods - 1);

        // Work Zone ATDM applied only if scenario has a work zone
        // Applied as a diversion strategy.  Upstream mainline demand reduced as
        // well as fo any on ramp segments
        // Work Zone periods will never wrap
        if (hasWorkZone) {
            for (int wzIdx = 0; wzIdx < numWorkZones; wzIdx++) {
                // Applying demand to upstream mainline demand
                tempATDMScenario.OAF().multiply(atdmAdjFactors[3][0],
                        0,
                        workZones.get(wzIdx).getStartPeriod() - 1,
                        0,
                        workZones.get(wzIdx).getEndPeriod() - 1);
                tempATDMScenario.DAF().multiply(atdmAdjFactors[3][0],
                        0,
                        workZones.get(wzIdx).getStartPeriod() - 1,
                        0,
                        workZones.get(wzIdx).getEndPeriod() - 1);
                // Applying to all upstream segments
                for (int seg = 1; seg < workZones.get(wzIdx).getStartSegment(); seg++) {
                    if (seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, seg) == CEConst.SEG_TYPE_ONR || seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, seg) == CEConst.SEG_TYPE_W) {
                        tempATDMScenario.OAF().multiply(atdmAdjFactors[3][0],
                                seg,
                                workZones.get(wzIdx).getStartPeriod() - 1,
                                seg,
                                workZones.get(wzIdx).getEndPeriod() - 1);
                    }
                }

                // Applying non-diversion ATDM AFs to work zone segments
                tempATDMScenario.SAF().multiply(atdmAdjFactors[3][1],
                        workZones.get(wzIdx).getStartSegment(),
                        workZones.get(wzIdx).getStartPeriod() - 1,
                        workZones.get(wzIdx).getEndSegment() - 1,
                        workZones.get(wzIdx).getEndPeriod() - 1);
                tempATDMScenario.CAF().multiply(atdmAdjFactors[3][2],
                        workZones.get(wzIdx).getStartSegment(),
                        workZones.get(wzIdx).getStartPeriod() - 1,
                        workZones.get(wzIdx).getEndSegment() - 1,
                        workZones.get(wzIdx).getEndPeriod() - 1);
            }

        }

        // Weather applied only if weather event in period
        // (Does wrap time periods)
        int startTime;
        int dur;
        if (hasWeatherEvent) {
            for (int wIdx = 0; wIdx < numWeatherEvents; wIdx++) {
                startTime = weatherEventStartTimes.get(wIdx);
                dur = weatherEventDurations.get(wIdx);
                if (startTime + dur <= numAnalysisPeriods) {
                    tempATDMScenario.OAF().multiply(atdmAdjFactors[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                    tempATDMScenario.DAF().multiply(atdmAdjFactors[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                    tempATDMScenario.SAF().multiply(atdmAdjFactors[1][1],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                    tempATDMScenario.CAF().multiply(atdmAdjFactors[1][2],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                } else {
                    int endTime = (startTime + dur) % numAnalysisPeriods;
                    tempATDMScenario.OAF().multiply(atdmAdjFactors[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.OAF().multiply(atdmAdjFactors[1][0],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);
                    tempATDMScenario.DAF().multiply(atdmAdjFactors[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.DAF().multiply(atdmAdjFactors[1][0],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);
                    tempATDMScenario.SAF().multiply(atdmAdjFactors[1][1],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.SAF().multiply(atdmAdjFactors[1][1],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);
                    tempATDMScenario.CAF().multiply(atdmAdjFactors[1][2],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.CAF().multiply(atdmAdjFactors[1][2],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);

                }
            }
        }

        if (hasIncidentGP) {
            int seg;
            int incType;
            int adjNumLanes;
            int endTime;
            int durReduction = atdmPlan.getIncidentDurationReduction();
            for (int incIdx = 0; incIdx < numIncidentsGP; incIdx++) {
                seg = incidentGPEventLocations.get(incIdx);
                incType = incidentGPEventTypes.get(incIdx);
                //numLanes = seed.getValueInt(CEConst.IDS_MAIN_NUM_LANES_IN, seg);
                startTime = incidentGPEventStartTimes.get(incIdx);
                dur = incidentGPEventDurations.get(incIdx);
                // Get list of upstream mainline and onramp segments
                ArrayList<Integer> divSegments = new ArrayList<>();
                divSegments.add(0);
                for (int segIdx = 1; segIdx < seg; segIdx++) {
                    if (seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, segIdx) == CEConst.SEG_TYPE_ONR || seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, segIdx) == CEConst.SEG_TYPE_W) {
                        divSegments.add(segIdx);
                    }
                }

                if (dur <= durReduction) {                                        // Case 1: Duration is shorter than duration reduction
                    // Reverse adjustmentFactors applied to scenario
                    endTime = startTime + dur;
                    if (endTime <= numAnalysisPeriods) {                        // Case 1a: incident does not wrap
                        for (int per = startTime; per <= endTime - 1; per++) {
                            adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 6) - 2, 0);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                        }
                        int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_GP);
                        tempATDMScenario.LAF().add(newLAF,
                                seg,
                                startTime,
                                seg,
                                endTime - 1);

                    } else {                                                    // Case 1b: Incident wraps
                        endTime = endTime % numAnalysisPeriods;
                        for (int per = startTime; per <= numAnalysisPeriods - 1; per++) {
                            adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 6) - 2, 0);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                        }

                        for (int per = 0; per <= endTime - 1; per++) {
                            adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 6) - 2, 0);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                        }

                        int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_GP);
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
                        // Reversal of AFs for incident duration reduction
                        for (int per = endTime - durReduction; per <= endTime - 1; per++) {
                            adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 6) - 2, 0);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                        }
                        int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_GP);
                        tempATDMScenario.LAF().add(newLAF,
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);

                        // Application of non-diversion AFs to incident segments
                        tempATDMScenario.SAF().multiply(atdmAdjFactors[2][1],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.CAF().multiply(atdmAdjFactors[2][2],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.LAF().add(0,
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);

                        // Application of AFs for incident diversion
                        for (int divSeg : divSegments) {
                            tempATDMScenario.OAF().multiply(atdmAdjFactors[2][0],
                                    divSeg,
                                    startTime,
                                    divSeg,
                                    endTime - durReduction - 1);
//                            tempATDMScenario.DAF().multiply(afArray[2][0],
//                                    divSeg,
//                                    startTime,
//                                    divSeg,
//                                    endTime - durReduction - 1);
                        }
                    } else {                                                    // Case 2b: incident wraps
                        if ((endTime - durReduction) <= numAnalysisPeriods) {     // Case 2b1: wrap occurs during duration reduction
                            int wrappedEndTime = endTime % numAnalysisPeriods;
                            // Reversal of AFs for incident duration reduction
                            for (int per = endTime - durReduction; per <= numAnalysisPeriods - 1; per++) {
                                adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 6) - 2, 0);
                                tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                            }
                            for (int per = 0; per <= wrappedEndTime - 1; per++) {
                                adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 6) - 2, 0);
                                tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        0,
                                        seg,
                                        wrappedEndTime - 1);
                                tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                            }

                            int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_GP);
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

                            // Applying ATDM AFs for incident segments
                            tempATDMScenario.SAF().multiply(atdmAdjFactors[2][1],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.CAF().multiply(atdmAdjFactors[2][2],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.LAF().add(0,
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);

                            // Applying Incident Diversion
                            for (int divSeg : divSegments) {
                                tempATDMScenario.OAF().multiply(atdmAdjFactors[2][0],
                                        divSeg,
                                        startTime,
                                        divSeg,
                                        endTime - durReduction - 1);
                            }

                        } else {                                                // Case 2b2: wrap occurs before duration reduction
                            endTime = endTime % numAnalysisPeriods;
                            // Reversal of AFs for incident duration reduction
                            for (int per = endTime - durReduction; per <= endTime - 1; per++) {
                                adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 6) - 2, 0);
                                tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                            }
                            int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_GP);
                            tempATDMScenario.LAF().add(newLAF,
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    endTime - 1);

                            //Application of non-diversion AFs
                            tempATDMScenario.SAF().multiply(atdmAdjFactors[2][1],
                                    seg,
                                    startTime,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.SAF().multiply(atdmAdjFactors[2][1],
                                    seg,
                                    0,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.CAF().multiply(atdmAdjFactors[2][2],
                                    seg,
                                    startTime,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.CAF().multiply(atdmAdjFactors[2][2],
                                    seg,
                                    0,
                                    seg,
                                    endTime - durReduction - 1);
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

                            // Application of incident diversion AFs
                            for (int divSeg : divSegments) {
                                tempATDMScenario.OAF().multiply(atdmAdjFactors[2][0],
                                        divSeg,
                                        startTime,
                                        divSeg,
                                        numAnalysisPeriods - 1);
                                tempATDMScenario.OAF().multiply(atdmAdjFactors[2][0],
                                        divSeg,
                                        0,
                                        divSeg,
                                        endTime - durReduction - 1);
                            }

                        }
                    }
                }
            }
        }

        if (atdmPlan.hasShoulderOpening()) {
            CA2DInt hsrMat = atdmPlan.getHSRMatrix();
            int numLanesSegment;
            for (int segment = 0; segment < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); segment++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    if (hsrMat.get(segment, period) == 1) {
                        numLanesSegment = seed.getValueInt(CEConst.IDS_MAIN_NUM_LANES_IN, segment, period);
                        tempATDMScenario.LAF().add(1, segment, period); // Adding lane

                        // Calculating new segment CAF using shoulder CAF
                        float rlCAF = seed.getRLCAF(group + 1, segment, period, CEConst.SEG_TYPE_GP); //To Lake: Maybe need to switch between GP and ML
                        float seedCAF = seed.getValueFloat(CEConst.IDS_GP_USER_CAF, segment, period);
                        float newCAF = ((numLanesSegment * seedCAF * rlCAF * tempATDMScenario.CAF().get(segment, period)) + atdmPlan.getHSRCAF(numLanesSegment)) / (numLanesSegment + 1);
                        //System.out.println(newCAF);
                        tempATDMScenario.CAF().set((newCAF / rlCAF), segment, period);
                    }
                }
            }
        }

        if (atdmPlan.hasRampMetering()) {
            tempATDMScenario.RM().setGlobalRMType(CEConst.IDS_RAMP_METERING_TYPE_FIX);
            tempATDMScenario.RM().getRampMeteringFixRate().deepCopyFrom(atdmPlan.getRMRate());
        }
        return tempATDMScenario;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ATDMScenario Generation for ML">
    /**
     * Returns an ATDMScenario object that reflects the application of the
     * strategies in the specified ATDM plan for ML segments.
     *
     * @param atdmPlan ATDM plan to be applied to the reliability scenario.
     * @return ATDMScenario instance with the ATDM plan strategies applied.
     */
    public ATDMScenario generateATDMScenarioML(ATDMPlan atdmPlan) {
        if (!seed.isManagedLaneUsed()) {
            return null;
        }
        int numAnalysisPeriods = seed.getValueInt(CEConst.IDS_NUM_PERIOD);
        int numSegments = seed.getValueInt(CEConst.IDS_NUM_SEGMENT);

        ATDMScenario tempATDMScenario = new ATDMScenario(numSegments, numAnalysisPeriods);
        tempATDMScenario.setName(atdmPlan.getName());
        tempATDMScenario.setDiscription(atdmPlan.getInfo());

        float[][] atdmAdjFactors = atdmPlan.getATDMadjFactors();

        // Demand management strategies (applied for all segments, all periods)
        tempATDMScenario.DAF().set(atdmAdjFactors[0][0],
                0, 0,
                numSegments - 1, numAnalysisPeriods - 1);
        tempATDMScenario.OAF().set(atdmAdjFactors[0][0],
                0, 0,
                numSegments - 1, numAnalysisPeriods - 1);

        // Weather applied only if weather event in period
        // (Does wrap time periods)
        int startTime;
        int dur;
        if (hasWeatherEvent) {
            for (int wIdx = 0; wIdx < numWeatherEvents; wIdx++) {
                startTime = weatherEventStartTimes.get(wIdx);
                dur = weatherEventDurations.get(wIdx);
                if (startTime + dur <= numAnalysisPeriods) {
                    tempATDMScenario.OAF().multiply(atdmAdjFactors[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                    tempATDMScenario.DAF().multiply(atdmAdjFactors[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                    tempATDMScenario.SAF().multiply(atdmAdjFactors[1][1],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                    tempATDMScenario.CAF().multiply(atdmAdjFactors[1][2],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            weatherEventStartTimes.get(wIdx) + dur - 1);
                } else {
                    int endTime = (startTime + dur) % numAnalysisPeriods;
                    tempATDMScenario.OAF().multiply(atdmAdjFactors[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.OAF().multiply(atdmAdjFactors[1][0],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);
                    tempATDMScenario.DAF().multiply(atdmAdjFactors[1][0],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.DAF().multiply(atdmAdjFactors[1][0],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);
                    tempATDMScenario.SAF().multiply(atdmAdjFactors[1][1],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.SAF().multiply(atdmAdjFactors[1][1],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);
                    tempATDMScenario.CAF().multiply(atdmAdjFactors[1][2],
                            0,
                            weatherEventStartTimes.get(wIdx),
                            numSegments - 1,
                            numAnalysisPeriods - 1);
                    tempATDMScenario.CAF().multiply(atdmAdjFactors[1][2],
                            0,
                            0,
                            numSegments - 1,
                            endTime - 1);

                }
            }
        }

        if (hasIncidentML) {
            int seg;
            int incType;
            int adjNumLanes;
            int endTime;
            int durReduction = atdmPlan.getIncidentDurationReduction();
            for (int incIdx = 0; incIdx < numIncidentsML; incIdx++) {
                seg = incidentMLEventLocations.get(incIdx);
                incType = incidentMLEventTypes.get(incIdx);
                //numLanes = seed.getValueInt(CEConst.IDS_ML_NUM_LANES,seg);
                startTime = incidentMLEventStartTimes.get(incIdx);
                dur = incidentMLEventDurations.get(incIdx);
                // Get list of upstream mainline and onramp segments
                ArrayList<Integer> divSegments = new ArrayList<>();
                divSegments.add(0);
                for (int segIdx = 1; segIdx < seg; segIdx++) {
                    if (seed.getValueInt(CEConst.IDS_ML_SEGMENT_TYPE, segIdx) == CEConst.SEG_TYPE_ONR || seed.getValueInt(CEConst.IDS_ML_SEGMENT_TYPE, segIdx) == CEConst.SEG_TYPE_W) {
                        divSegments.add(segIdx);
                    }
                }

                if (dur <= durReduction) {                                        // Case 1: Duration is shorter than duration reduction
                    // Reverse adjustmentFactors applied to scenario
                    endTime = startTime + dur;
                    if (endTime <= numAnalysisPeriods) {                        // Case 1a: incident does not wrap
                        for (int per = startTime; per <= endTime - 1; per++) {
                            adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 2) - 1, 0);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getMLIncidentSAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getMLIncidentCAF()[incType][adjNumLanes]),
                                    per,
                                    startTime,
                                    seg,
                                    per);

                            int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_ML);
                            tempATDMScenario.LAF().add(newLAF,
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - 1);
                        }

                    } else {                                                    // Case 1b: Incident wraps
                        endTime = endTime % numAnalysisPeriods;
                        for (int per = startTime; per <= numAnalysisPeriods - 1; per++) {
                            adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 2) - 1, 0);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getMLIncidentSAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getMLIncidentCAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);

                        }
                        for (int per = 0; per <= endTime - 1; per++) {
                            adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 2) - 1, 0);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getMLIncidentSAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getMLIncidentCAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                        }
                        int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_ML);
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
                        // Reversal of AFs for incident duration reduction
                        for (int per = endTime - durReduction; per <= endTime - 1; per++) {
                            adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 2) - 1, 0);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getMLIncidentSAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getMLIncidentCAF()[incType][adjNumLanes]),
                                    seg,
                                    per,
                                    seg,
                                    per);
                        }
                        int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_ML);
                        tempATDMScenario.LAF().add(newLAF,
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);

                        // Application of non-diversion AFs to incident segments
                        tempATDMScenario.SAF().multiply(atdmAdjFactors[2][1],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.CAF().multiply(atdmAdjFactors[2][2],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.LAF().add(0,
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);

                        // Application of AFs for incident diversion
                        for (int divSeg : divSegments) {
                            tempATDMScenario.OAF().multiply(atdmAdjFactors[2][0],
                                    divSeg,
                                    startTime,
                                    divSeg,
                                    endTime - durReduction - 1);
                        }
                    } else {                                                    // Case 2b: incident wraps
                        if ((endTime - durReduction) <= numAnalysisPeriods) {     // Case 2b1: wrap occurs during duration reduction
                            int wrappedEndTime = endTime % numAnalysisPeriods;
                            // Reversal of AFs for incident duration reduction
                            for (int per = endTime - durReduction; per <= numAnalysisPeriods - 1; per++) {
                                adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 2) - 1, 0);
                                tempATDMScenario.OAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.DAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.SAF().multiply((1.0f / seed.getMLIncidentSAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.CAF().multiply((1.0f / seed.getMLIncidentCAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);

                            }
                            for (int per = 0; per <= wrappedEndTime - 1; per++) {
                                adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 2) - 1, 0);
                                tempATDMScenario.OAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.DAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.SAF().multiply((1.0f / seed.getMLIncidentSAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.CAF().multiply((1.0f / seed.getMLIncidentCAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                            }

                            int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_ML);
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

                            // Applying ATDM AFs for incident segments
                            tempATDMScenario.SAF().multiply(atdmAdjFactors[2][1],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.CAF().multiply(atdmAdjFactors[2][2],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.LAF().add(0,
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);

                            // Applying Incident Diversion
                            for (int divSeg : divSegments) {
                                tempATDMScenario.OAF().multiply(atdmAdjFactors[2][0],
                                        divSeg,
                                        startTime,
                                        divSeg,
                                        endTime - durReduction - 1);
                            }

                        } else {                                                // Case 2b2: wrap occurs before duration reduction
                            endTime = endTime % numAnalysisPeriods;
                            // Reversal of AFs for incident duration reduction
                            for (int per = endTime - durReduction; per <= endTime - 1; per++) {
                                adjNumLanes = Math.min(Math.max(seed.getValueInt(CEConst.IDS_ML_NUM_LANES, seg, per), 2) - 1, 0);
                                tempATDMScenario.OAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.DAF().multiply((1.0f / seed.getMLIncidentDAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.SAF().multiply((1.0f / seed.getMLIncidentSAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                                tempATDMScenario.CAF().multiply((1.0f / seed.getMLIncidentCAF()[incType][adjNumLanes]),
                                        seg,
                                        per,
                                        seg,
                                        per);
                            }
                            int newLAF = -1 * seed.getRLLAFI(group + 1, seg, startTime, CEConst.SEG_TYPE_ML);
                            tempATDMScenario.LAF().add(newLAF,
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    endTime - 1);

                            //Application of non-diversion AFs
                            tempATDMScenario.SAF().multiply(atdmAdjFactors[2][1],
                                    seg,
                                    startTime,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.SAF().multiply(atdmAdjFactors[2][1],
                                    seg,
                                    0,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.CAF().multiply(atdmAdjFactors[2][2],
                                    seg,
                                    startTime,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.CAF().multiply(atdmAdjFactors[2][2],
                                    seg,
                                    0,
                                    seg,
                                    endTime - durReduction - 1);
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

                            // Application of incident diversion AFs
                            for (int divSeg : divSegments) {
                                tempATDMScenario.OAF().multiply(atdmAdjFactors[2][0],
                                        divSeg,
                                        startTime,
                                        divSeg,
                                        numAnalysisPeriods - 1);
                                tempATDMScenario.OAF().multiply(atdmAdjFactors[2][0],
                                        divSeg,
                                        0,
                                        divSeg,
                                        endTime - durReduction - 1);
                            }

                        }
                    }
                }
            }
        }

        // No hard shoulder running for Managed Lanes.
        // No ramp metering for Managed Lanes
        tempATDMScenario.RM().setGlobalRMType(CEConst.IDS_RAMP_METERING_TYPE_NONE);

        //if (atdmPlan.hasRampMetering()) {
        //    tempATDMScenario.setRampMetering(true);
        //    tempATDMScenario.RM().deepCopyFrom(atdmPlan.getRMRate());
        //}
        //System.out.println("Sucessfully Created ML ATDM");
        return tempATDMScenario;
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Deprecated original ATDM without diversion for incidents">
    /**
     * Returns an ATDMScenario object that reflects the application of the
     * strategies in the specified ATDM plan.
     *
     * @param atdmPlan
     * @return
     * @deprecated
     */
    public ATDMScenario generateATDMScenarioOrig(ATDMPlan atdmPlan) {
        int numAnalysisPeriods = seed.getValueInt(CEConst.IDS_NUM_PERIOD);
        int numSegments = seed.getValueInt(CEConst.IDS_NUM_SEGMENT);

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
        // Applied as a diversion strategy.  Upstream mainline demand reduced as
        // well as fo any on ramp segments
        // Work zone periods will never wrap
        if (hasWorkZone) {
            for (int wzIdx = 0; wzIdx < numWorkZones; wzIdx++) {
                if (false) { // Deprecated
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
                } else {
                    // Applying demand to upstream mainline demand
                    tempATDMScenario.OAF().multiply(afArray[3][0],
                            0,
                            workZones.get(wzIdx).getStartPeriod() - 1,
                            0,
                            workZones.get(wzIdx).getEndPeriod() - 1);
                    tempATDMScenario.DAF().multiply(afArray[3][0],
                            0,
                            workZones.get(wzIdx).getStartPeriod() - 1,
                            0,
                            workZones.get(wzIdx).getEndPeriod() - 1);
                    tempATDMScenario.SAF().multiply(afArray[3][1],
                            0,
                            workZones.get(wzIdx).getStartPeriod() - 1,
                            0,
                            workZones.get(wzIdx).getEndPeriod() - 1);
                    tempATDMScenario.CAF().multiply(afArray[3][2],
                            0,
                            workZones.get(wzIdx).getStartPeriod() - 1,
                            0,
                            workZones.get(wzIdx).getEndPeriod() - 1);

                    // Applying to all upstream segments
                    for (int seg = 1; seg < workZones.get(wzIdx).getStartSegment(); seg++) {
                        if (seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, seg) == CEConst.SEG_TYPE_ONR) {
                            tempATDMScenario.OAF().multiply(afArray[3][0],
                                    seg,
                                    workZones.get(wzIdx).getStartPeriod() - 1,
                                    seg,
                                    workZones.get(wzIdx).getEndPeriod() - 1);
                            tempATDMScenario.DAF().multiply(afArray[3][0],
                                    seg,
                                    workZones.get(wzIdx).getStartPeriod() - 1,
                                    seg,
                                    workZones.get(wzIdx).getEndPeriod() - 1);
                            tempATDMScenario.SAF().multiply(afArray[3][1],
                                    seg,
                                    workZones.get(wzIdx).getStartPeriod() - 1,
                                    seg,
                                    workZones.get(wzIdx).getEndPeriod() - 1);
                            tempATDMScenario.CAF().multiply(afArray[3][2],
                                    seg,
                                    workZones.get(wzIdx).getStartPeriod() - 1,
                                    seg,
                                    workZones.get(wzIdx).getEndPeriod() - 1);
                        }
                    }
                }
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

        if (hasIncidentGP) {
            int seg;
            int incType;
            int numLanes;
            int endTime;
            int durReduction = atdmPlan.getIncidentDurationReduction();
            for (int incIdx = 0; incIdx < numIncidentsGP; incIdx++) {
                seg = incidentGPEventLocations.get(incIdx);
                incType = incidentGPEventTypes.get(incIdx);
                numLanes = seed.getValueInt(CEConst.IDS_MAIN_NUM_LANES_IN, seg);
                startTime = incidentGPEventStartTimes.get(incIdx);
                dur = incidentGPEventDurations.get(incIdx);
                if (dur <= durReduction) {                                        // Case 1: Duration is shorter than duration reduction
                    // Reverse adjustmentFactors applied to scenario
                    endTime = startTime + dur;
                    if (endTime <= numAnalysisPeriods) {                        // Case 1a: incident does not wrap
                        tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                endTime - 1);
                        tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                endTime - 1);
                        tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                endTime - 1);
                        tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                endTime - 1);
                        // Setting the new LAF to be that of just the workzone (equivalant to removing incident LAF)
                        int newLAF = Math.min((-1 * seed.getGPIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
                        tempATDMScenario.LAF().add(newLAF,
                                seg,
                                startTime,
                                seg,
                                endTime - 1);

                    } else {                                                    // Case 1b: Incident wraps
                        endTime = endTime % numAnalysisPeriods;
                        tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                numAnalysisPeriods - 1);
                        tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                0,
                                seg,
                                endTime - 1);
                        tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                numAnalysisPeriods - 1);
                        tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                0,
                                seg,
                                endTime - 1);
                        tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                numAnalysisPeriods - 1);
                        tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][numLanes - 2]),
                                seg,
                                0,
                                seg,
                                endTime - 1);
                        tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][numLanes - 2]),
                                seg,
                                startTime,
                                seg,
                                numAnalysisPeriods - 1);
                        tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][numLanes - 2]),
                                seg,
                                0,
                                seg,
                                endTime - 1);

                        int newLAF = Math.min((-1 * seed.getGPIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
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
                        tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);
                        tempATDMScenario.DAF().multiply(afArray[2][0],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);
                        tempATDMScenario.SAF().multiply(afArray[2][1],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][numLanes - 2]),
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);
                        tempATDMScenario.CAF().multiply(afArray[2][2],
                                seg,
                                startTime,
                                seg,
                                endTime - durReduction - 1);
                        tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][numLanes - 2]),
                                seg,
                                endTime - durReduction,
                                seg,
                                endTime - 1);

                        int newLAF = Math.min((-1 * seed.getGPIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
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
                            tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    0,
                                    seg,
                                    wrappedEndTime - 1);
                            tempATDMScenario.DAF().multiply(afArray[2][0],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
                                    seg,
                                    0,
                                    seg,
                                    wrappedEndTime - 1);
                            tempATDMScenario.SAF().multiply(afArray[2][1],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][numLanes - 2]),
                                    seg,
                                    0,
                                    seg,
                                    wrappedEndTime - 1);
                            tempATDMScenario.CAF().multiply(afArray[2][2],
                                    seg,
                                    startTime,
                                    seg,
                                    endTime - durReduction - 1);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    numAnalysisPeriods - 1);
                            tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][numLanes - 2]),
                                    seg,
                                    0,
                                    seg,
                                    wrappedEndTime - 1);

                            int newLAF = Math.min((-1 * seed.getGPIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
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
                            tempATDMScenario.OAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
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
                            tempATDMScenario.DAF().multiply((1.0f / seed.getGPIncidentDAF()[incType][numLanes - 2]),
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
                            tempATDMScenario.SAF().multiply((1.0f / seed.getGPIncidentSAF()[incType][numLanes - 2]),
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
                            tempATDMScenario.CAF().multiply((1.0f / seed.getGPIncidentCAF()[incType][numLanes - 2]),
                                    seg,
                                    endTime - durReduction,
                                    seg,
                                    endTime - 1);

                            int newLAF = Math.min((-1 * seed.getGPIncidentLAF()[incType][numLanes - 2]), numLanes - 1); //TODO: account for workzone lane closures
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
            CA2DInt hsrMat = atdmPlan.getHSRMatrix();
            int numLanesSegment;
            for (int segment = 0; segment < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); segment++) {
                //System.out.println(numLanesSegment);
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    if (hsrMat.get(segment, period) == 1) {
                        numLanesSegment = seed.getValueInt(CEConst.IDS_MAIN_NUM_LANES_IN, segment);
                        tempATDMScenario.LAF().add(1, segment, period); // Adding lane

                        // Calculating new segment CAF using shoulder CAF
                        float rlCAF = seed.getRLCAF(group + 1, segment, period, CEConst.SEG_TYPE_GP); //To Lake: Maybe need to switch between GP and ML
                        //System.out.println(rlCAF);
                        float newCAF = ((numLanesSegment * rlCAF * tempATDMScenario.CAF().get(segment, period)) + atdmPlan.getHSRCAF(numLanesSegment)) / (numLanesSegment + 1);
                        //System.out.println(newCAF);
                        tempATDMScenario.CAF().set((newCAF / rlCAF), segment, period);
                    }
                }
            }
        }

        if (atdmPlan.hasRampMetering()) {
            //tempATDMScenario.RM().deepCopyFrom(atdmPlan.getRMRate());
        }
        return tempATDMScenario;
    }
//</editor-fold>

    /**
     *
     * @param atdmDatabase
     * @param planIdx
     * @return
     */
    public ATDMScenario[] applyAndGetATDMScenarios(ATDMDatabase atdmDatabase, int planIdx) {
        return new ATDMScenario[]{generateATDMScenario(atdmDatabase.getPlan(planIdx)), generateATDMScenarioML(atdmDatabase.getPlan(planIdx))};

    }
    // </editor-fold>
}
