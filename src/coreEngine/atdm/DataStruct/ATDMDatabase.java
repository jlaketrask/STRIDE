/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreEngine.atdm.DataStruct;

import coreEngine.Helper.CEConst;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jltrask
 */
public class ATDMDatabase implements Serializable {

    /**
     *
     */
    private final ArrayList<ATDMStrategy> demandStrat;

    /**
     *
     */
    private final ArrayList<ATDMStrategy> weatherStrat;

    /**
     *
     */
    private final ArrayList<ATDMStrategy> incidentStrat;

    /**
     *
     */
    private final ArrayList<ATDMStrategy> workZoneStrat;

    /**
     *
     */
    private final ArrayList<ATDMStrategy> rmStrat;

    /**
     *
     */
    private final ArrayList<ATDMStrategy> hsrStrat;

    /**
     *
     */
    private final ArrayList<ATDMPlan> atdmPlans;

    /**
     *
     */
    private static final long serialVersionUID = 51348672125L;

    /**
     *
     */
    public ATDMDatabase() {

        demandStrat = new ArrayList<>();
        weatherStrat = new ArrayList<>();
        incidentStrat = new ArrayList<>();
        workZoneStrat = new ArrayList<>();
        rmStrat = new ArrayList<>();
        hsrStrat = new ArrayList<>();

        atdmPlans = new ArrayList<>();

    }

    //<editor-fold defaultstate="collapsed" desc="Initialize Defaults">

    /**
     *
     */
        public void initDefaultDatabase() {
        initDemandDefaultStrategies();
        initWeatherDefaultStrategies();
        initIncidentDefaultStrategies();
        initWorkZoneDefaultStrategies();

        initATDMPlans();
    }

    /**
     *
     */
    private void initDemandDefaultStrategies() {
        int startId = demandStrat.size() + 1;

        demandStrat.add(new ATDMStrategy(startId++, "HOT Lane - Static Toll", 0));
        demandStrat.add(new ATDMStrategy(startId++, "HOT Lane - Congestion Price", 0));
        demandStrat.add(new ATDMStrategy(startId++, "Full Facility Static Toll", 0));
        demandStrat.add(new ATDMStrategy(startId++, "Full Facility Dynamic Congestion Pricing", 0));
        demandStrat.add(new ATDMStrategy(startId++, "Pre-Trip Traveler Info", 1));
        demandStrat.add(new ATDMStrategy(startId++, "Variable Message Signs", 1));
        demandStrat.add(new ATDMStrategy(startId, "Employer TDM", 1));
    }

    /**
     *
     */
    private void initWeatherDefaultStrategies() {
        int startId = weatherStrat.size() + 1;

        weatherStrat.add(new ATDMStrategy(startId++, "Vehicle Restrictions (chain controls)", 0));
        weatherStrat.add(new ATDMStrategy(startId++, "Pre-trip Travel Info", 1));
        weatherStrat.add(new ATDMStrategy(startId++, "Variable Message Signs", 1));
        weatherStrat.add(new ATDMStrategy(startId++, "Employer TDM", 1));
        weatherStrat.add(new ATDMStrategy(startId++, "Snow Removal", 2));
        weatherStrat.add(new ATDMStrategy(startId++, "Anit-Icing", 2));
        weatherStrat.add(new ATDMStrategy(startId++, "Fog Dispersion", 2));
    }

    /**
     *
     */
    private void initIncidentDefaultStrategies() {
        int startId = incidentStrat.size() + 1;

        incidentStrat.add(new ATDMStrategy(startId++, "Incident Command System", 3));
        incidentStrat.add(new ATDMStrategy(startId++, "Traffic Control With On-Site Traffic Management Teams", 3));
        incidentStrat.add(new ATDMStrategy(startId++, "End-of-Queue Advance Warnign Systems", 3));
        incidentStrat.add(new ATDMStrategy(startId++, "Pre-trip Travel Info", 1));
        incidentStrat.add(new ATDMStrategy(startId++, "Variable Message Signs", 1));
        incidentStrat.add(new ATDMStrategy(startId++, "Portable Message Signs", 1));
        incidentStrat.add(new ATDMStrategy(startId++, "Employer TDM", 1));
        incidentStrat.add(new ATDMStrategy(startId++, "Field Verifciation by On-Site Responders", 4));
        incidentStrat.add(new ATDMStrategy(startId++, "Closed-Circuit Television Cameras", 4));
        incidentStrat.add(new ATDMStrategy(startId++, "Frequent/Enhanced Roadway Reference Markers", 4));
        incidentStrat.add(new ATDMStrategy(startId++, "Enhanced 911/Automated Positioning Systems", 4));
        incidentStrat.add(new ATDMStrategy(startId++, "Motorist Aid Call Boxes", 4));
        incidentStrat.add(new ATDMStrategy(startId++, "Automated Collision Notification Systems", 4));
    }

    /**
     *
     */
    private void initWorkZoneDefaultStrategies() {
        int startId = workZoneStrat.size() + 1;

        workZoneStrat.add(new ATDMStrategy(startId++, "End-of-Queue Advance Warnign Systems", 3));
        workZoneStrat.add(new ATDMStrategy(startId++, "Speed Feedback Signs", 3));
        workZoneStrat.add(new ATDMStrategy(startId++, "Automated Speed Enforcement", 3));
        workZoneStrat.add(new ATDMStrategy(startId++, "Pre-trip Travel Info", 1));
        workZoneStrat.add(new ATDMStrategy(startId++, "Variable Message Signs", 1));
        workZoneStrat.add(new ATDMStrategy(startId++, "Portable Message Signs", 1));
        workZoneStrat.add(new ATDMStrategy(startId++, "Employer TDM", 1));
        workZoneStrat.add(new ATDMStrategy(startId++, "Detours", 1));
    }

    /**
     *
     */
    private void initATDMPlans() {
        // If no plans exist, create one empty plan
        if (atdmPlans.isEmpty()) {
            atdmPlans.add(new ATDMPlan(1, "Plan " + 1));
        }
    }
//</editor-fold>

    /**
     *
     * @param categoryID
     * @param newStrategy
     */
    public void addStrategy(String categoryID, ATDMStrategy newStrategy) {

        switch (categoryID) {
            case CEConst.IDS_ATDM_STRAT_TYPE_DEMAND:
                demandStrat.add(newStrategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_WEATHER:
                weatherStrat.add(newStrategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT:
                incidentStrat.add(newStrategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_WORKZONE:
                workZoneStrat.add(newStrategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_RAMP_METERING:
                rmStrat.add(newStrategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING:
                hsrStrat.add(newStrategy);
                break;
        }
    }

    /**
     *
     * @param categoryID
     * @param newStrategy
     */
    public void addStrategy(String categoryID, ATDMStrategyMat newStrategy) {
        switch (categoryID) {
            case CEConst.IDS_ATDM_STRAT_TYPE_RAMP_METERING:
                rmStrat.add(newStrategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING:
                hsrStrat.add(newStrategy);
                break;
        }
    }

    /**
     *
     * @param newPlan
     */
    public void addPlan(ATDMPlan newPlan) {
        atdmPlans.add(newPlan);
    }

    /**
     *
     * @param categoryID
     * @return
     */
    public ArrayList<ATDMStrategy> getStrategy(String categoryID) {
        switch (categoryID) {
            case CEConst.IDS_ATDM_STRAT_TYPE_DEMAND:
                return demandStrat;
            case CEConst.IDS_ATDM_STRAT_TYPE_WEATHER:
                return weatherStrat;
            case CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT:
                return incidentStrat;
            case CEConst.IDS_ATDM_STRAT_TYPE_WORKZONE:
                return workZoneStrat;
            case CEConst.IDS_ATDM_STRAT_TYPE_RAMP_METERING:
                return rmStrat;
            case CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING:
                return hsrStrat;
            default:
                return null;
        }
    }

    /**
     *
     * @param categoryID
     * @param stratIdx
     * @return
     */
    public ATDMStrategy getStrategy(String categoryID, int stratIdx) {
        switch (categoryID) {
            case CEConst.IDS_ATDM_STRAT_TYPE_DEMAND:
                return demandStrat.get(stratIdx);
            case CEConst.IDS_ATDM_STRAT_TYPE_WEATHER:
                return weatherStrat.get(stratIdx);
            case CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT:
                return incidentStrat.get(stratIdx);
            case CEConst.IDS_ATDM_STRAT_TYPE_WORKZONE:
                return workZoneStrat.get(stratIdx);
            case CEConst.IDS_ATDM_STRAT_TYPE_RAMP_METERING:
                return rmStrat.get(stratIdx);
            case CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING:
                return hsrStrat.get(stratIdx);
            default:
                return null;
        }
    }

    /**
     *
     * @param planIdx
     * @return
     */
    public ATDMPlan getPlan(int planIdx) {
        return atdmPlans.get(planIdx);
    }

    /**
     *
     * @param planName
     * @return
     */
    public ATDMPlan getPlan(String planName) {
        for (ATDMPlan atdmPlan : atdmPlans) {
            if (atdmPlan.getName().equalsIgnoreCase(planName)) {
                return atdmPlan;
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public int getNumberOfATDMPlans() {
        return atdmPlans.size();
    }

    /**
     *
     * @param categoryID
     * @return
     */
    public int getNumberOfStrategies(String categoryID) {
        switch (categoryID) {
            case CEConst.IDS_ATDM_STRAT_TYPE_DEMAND:
                return demandStrat.size();
            case CEConst.IDS_ATDM_STRAT_TYPE_WEATHER:
                return weatherStrat.size();
            case CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT:
                return incidentStrat.size();
            case CEConst.IDS_ATDM_STRAT_TYPE_WORKZONE:
                return workZoneStrat.size();
            case CEConst.IDS_ATDM_STRAT_TYPE_RAMP_METERING:
                return rmStrat.size();
            case CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING:
                return hsrStrat.size();
            default:
                return 0;
        }
    }

    /**
     * Removes the strategy at index strategyIdx from the given category/type
     * (Demand, Weather, Incident, Workzone).
     *
     * @param categoryID
     * @param strategyIdx
     */
    public void removeStrategy(String categoryID, int strategyIdx) {

        ArrayList<ATDMStrategy> currStratDB;
        switch (categoryID) {
            case CEConst.IDS_ATDM_STRAT_TYPE_DEMAND:
                currStratDB = demandStrat;
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_WEATHER:
                currStratDB = weatherStrat;
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT:
                currStratDB = incidentStrat;
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_WORKZONE:
                currStratDB = workZoneStrat;
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_RAMP_METERING:
                currStratDB = rmStrat;
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING:
                currStratDB = hsrStrat;
                break;
            default: // never called
                currStratDB = new ArrayList<>();
                break;
        }

        ATDMStrategy removedStrat = currStratDB.remove(strategyIdx);
        // Removing from all plans in the active ATDMDatabase.
        for (ATDMPlan atdmPlan : atdmPlans) {
            atdmPlan.getAppliedStrategies().remove(removedStrat);
        }

    }

    /**
     * Removes a strategy from the specified category/type. Pointers must match.
     *
     * @param categoryID
     * @param strategy
     */
    public void removeStrategy(String categoryID, ATDMStrategy strategy) {
        switch (categoryID) {
            case CEConst.IDS_ATDM_STRAT_TYPE_DEMAND:
                demandStrat.remove(strategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_WEATHER:
                weatherStrat.remove(strategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT:
                incidentStrat.remove(strategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_WORKZONE:
                workZoneStrat.remove(strategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_RAMP_METERING:
                rmStrat.remove(strategy);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING:
                hsrStrat.remove(strategy);
                break;
        }

        for (ATDMPlan atdmPlan : atdmPlans) {
            atdmPlan.getAppliedStrategies().remove(strategy);
        }
    }

    /**
     * Removes the plan at index planIdx from the database
     *
     * @param planIdx
     */
    public void removePlan(int planIdx) {
        atdmPlans.remove(planIdx);
    }

    /**
     * Removes the specified ATDMPlan (pointers much match)
     *
     * @param atdmPlan
     */
    public void removePlan(ATDMPlan atdmPlan) {
        atdmPlans.remove(atdmPlan);
    }

}
