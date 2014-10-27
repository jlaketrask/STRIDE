package coreEngine.atdm.DataStruct;

import CompressArray.CA2DInt;
import coreEngine.Helper.CEConst;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author jltrask
 */
public class ATDMPlan implements Serializable, Cloneable {

    private static final long serialVersionUID = 7234909154855844L;

    private int id;

    private String name;

    private String description;

    private HashMap<ATDMStrategy, String> strategies;

    //private CA2DInt RMRate;
    private boolean hasRampMetering = false;

    //private CA2DInt HSRMatrix;
    //private float hardShoulderRunningCAF = 1.0f;
    private boolean hasShoulderOpening = false;

    /**
     *
     * @param id
     * @param name
     */
    public ATDMPlan(int id, String name) {

        this.id = id;
        this.name = name;
        this.description = "";

        this.strategies = new HashMap<>();
    }

    /**
     * Creates a new ATDMPlan instance with all the strategies of the plan
     * specified in the constructor.
     *
     * @param id
     * @param name
     * @param basePlan
     */
    public ATDMPlan(int id, String name, ATDMPlan basePlan) {
        this.id = id;
        this.name = name;
        this.description = "";

        copyStrategies(basePlan);
    }

    /**
     *
     * @param strategyType
     * @param newStrat
     */
    public void addStrategy(String strategyType, ATDMStrategy newStrat) {
        if (strategies.containsKey(newStrat) == false) {
            strategies.put(newStrat, strategyType);
        }
    }

    /**
     *
     * @param strat
     */
    public void removeStrategy(ATDMStrategy strat) {
        if (strategies.containsKey(strat)) {
            strategies.remove(strat);
        }
    }

    private void copyStrategies(ATDMPlan basePlan) {
        strategies = (HashMap<ATDMStrategy, String>) basePlan.getAppliedStrategies().clone();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        String descD = "";
        String descW = "";
        String descI = "";
        String descWZ = "";
        String descRM = "Ramp Metering: ";
        String descSO = "";
        for (ATDMStrategy strategy : strategies.keySet()) {
            switch (strategies.get(strategy)) {
                case CEConst.IDS_ATDM_STRAT_TYPE_DEMAND:
                    descD = descD + "DM:" + strategy.getDescription() + ",";
                    break;
                case CEConst.IDS_ATDM_STRAT_TYPE_WEATHER:
                    descW = descD + "WM:" + strategy.getDescription() + ",";
                    break;
                case CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT:
                    descI = descD + "IM:" + strategy.getDescription() + ",";
                    break;
                case CEConst.IDS_ATDM_STRAT_TYPE_WORKZONE:
                    descWZ = descD + "WZM:" + strategy.getDescription() + ",";
                    break;
            }
        }

        if (hasRampMetering) {
            descRM = descRM + "On,";
        } else {
            descRM = descRM + "Off,";
        }

        if (hasShoulderOpening) {
            descSO = "Shoulder Opening,";
        }

        description = descD + descW + descI + descWZ + descRM + descSO;
        return description;
    }

    /**
     *
     * @return
     */
    public String getInfo() {
        String descD = "";
        String descW = "";
        String descI = "";
        String descWZ = "";
        String descRM = "Ramp Metering: ";
        String descSO = "";
        for (ATDMStrategy strategy : strategies.keySet()) {
            switch (strategies.get(strategy)) {
                case CEConst.IDS_ATDM_STRAT_TYPE_DEMAND:
                    descD = descD + "DM:" + strategy.getDescription() + "\n";
                    break;
                case CEConst.IDS_ATDM_STRAT_TYPE_WEATHER:
                    descD = descD + "WM:" + strategy.getDescription() + "\n";
                    break;
                case CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT:
                    descD = descD + "IM:" + strategy.getDescription() + "\n";
                    break;
                case CEConst.IDS_ATDM_STRAT_TYPE_WORKZONE:
                    descD = descD + "WZM:" + strategy.getDescription() + "\n";
                    break;
            }
        }

        if (hasRampMetering) {
            descRM = descRM + "On\n";
        } else {
            descRM = descRM + "Off\n";
        }

        if (hasShoulderOpening) {
            descSO = "Shoulder Opening\n";
        }

        return name + ":\n" + descD + descW + descI + descWZ + descRM + descSO;
    }

    /**
     *
     * @return
     */
    public int getID() {
        return this.id;
    }

    /**
     *
     * @return
     */
    public HashMap<ATDMStrategy, String> getAppliedStrategies() {
        return strategies;
    }

    /**
     *
     * @return
     */
    public float[][] getATDMadjFactors() {
        float[][] afArray = new float[4][3];
        Arrays.fill(afArray[0], 1.0f);
        Arrays.fill(afArray[1], 1.0f);
        Arrays.fill(afArray[2], 1.0f);
        Arrays.fill(afArray[3], 1.0f);

        for (ATDMStrategy strategy : strategies.keySet()) {
            switch (strategies.get(strategy)) {
                case CEConst.IDS_ATDM_STRAT_TYPE_DEMAND:
                    for (int afIdx = 0; afIdx < afArray[0].length; afIdx++) {
                        afArray[0][afIdx] *= strategy.getAdjFactor(afIdx);
                    }
                    break;
                case CEConst.IDS_ATDM_STRAT_TYPE_WEATHER:
                    for (int afIdx = 0; afIdx < afArray[0].length; afIdx++) {
                        afArray[1][afIdx] *= strategy.getAdjFactor(afIdx);
                    }
                    break;
                case CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT:
                    for (int afIdx = 0; afIdx < afArray[0].length; afIdx++) {
                        afArray[2][afIdx] *= strategy.getAdjFactor(afIdx);
                    }
                    break;
                case CEConst.IDS_ATDM_STRAT_TYPE_WORKZONE:
                    for (int afIdx = 0; afIdx < afArray[0].length; afIdx++) {
                        afArray[3][afIdx] *= strategy.getAdjFactor(afIdx);
                    }
                    break;
            }
        }
        return afArray;
    }

    /**
     *
     * @return
     */
    public int getIncidentDurationReduction() {
        int durAdj = 0;
        for (ATDMStrategy strategy : strategies.keySet()) {
            if (CEConst.IDS_ATDM_STRAT_TYPE_INCIDENT.equalsIgnoreCase(strategies.get(strategy))) {
                durAdj += (strategy.getIncidentDurationReduction() / 15);
            }
        }
        return durAdj;
    }

    /**
     *
     * @return
     */
    public CA2DInt getRMRate() {
        for (ATDMStrategy strategy : strategies.keySet()) {
            if (CEConst.IDS_ATDM_STRAT_TYPE_RAMP_METERING.equalsIgnoreCase(strategies.get(strategy))) {
                return ((ATDMStrategyMat) strategy).getStrategyMatrix();
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public CA2DInt getHSRMatrix() {
        for (ATDMStrategy strategy : strategies.keySet()) {
            if (CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING.equalsIgnoreCase(strategies.get(strategy))) {
                return ((ATDMStrategyMat) strategy).getStrategyMatrix();
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public float[] getHSRCAF() {
        for (ATDMStrategy strategy : strategies.keySet()) {
            if (CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING.equalsIgnoreCase(strategies.get(strategy))) {
                return ((ATDMStrategyMat) strategy).getShoulderCapacity();
            }
        }
        throw new RuntimeException("Error: No HSR Strategy assigned to plan");
    }

    /**
     * @param numLanes
     * @return
     */
    public float getHSRCAF(int numLanes) {
        for (ATDMStrategy strategy : strategies.keySet()) {
            if (CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING.equalsIgnoreCase(strategies.get(strategy))) {
                return ((ATDMStrategyMat) strategy).getShoulderCapacity(numLanes);
            }
        }
        throw new RuntimeException("Error: No HSR Strategy assigned to plan");
    }

    //</editor-fold>
    /**
     *
     * @param strategy
     * @return
     */
    public boolean hasStrategy(ATDMStrategy strategy) {
        for (ATDMStrategy strat : strategies.keySet()) {
            if (strat == strategy) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean hasRampMetering() {
        return hasRampMetering;
    }

    /**
     *
     * @return
     */
    public boolean hasShoulderOpening() {
        return hasShoulderOpening;
    }

//    public void setHSRCAF(float newVal) {
//        hardShoulderRunningCAF = newVal;
//    }
    // <editor-fold defaultstate="collapsed" desc="Setters">
    /**
     *
     * @param newID
     */
    public void setID(int newID) {
        this.id = newID;
    }

    /**
     *
     * @param newName
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     *
     * @param newDescription
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

//    public void setRMRate(CA2DInt newRMRate) {
//        RMRate = newRMRate;
//    }
//
//    public void setHSRMatrix(CA2DInt newHSRMatrix) {
//        HSRMatrix = newHSRMatrix;
//    }
    /**
     *
     * @param val
     */
    public void useRampMetering(boolean val) {
        hasRampMetering = val;
    }

    /**
     *
     * @param val
     */
    public void useShoulderOpening(boolean val) {
        hasShoulderOpening = val;
    }

    //</editor-fold>
    /**
     * Returns a deep copy of the ATDMPlan object. Same as clone (overridden and
     * implemented to be a deep copy in the source code).
     *
     * @return
     */
    public ATDMPlan getDeepCopy() {
        try {
            return this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public ATDMPlan clone() throws CloneNotSupportedException {
        ATDMPlan cloned = (ATDMPlan) super.clone();
        HashMap<ATDMStrategy, String> tempHash = (HashMap<ATDMStrategy, String>) cloned.getAppliedStrategies().clone();
        for (ATDMStrategy strat : tempHash.keySet()) {
            tempHash.put(strat.clone(), tempHash.remove(strat));
        }
        return cloned;
    }

}
