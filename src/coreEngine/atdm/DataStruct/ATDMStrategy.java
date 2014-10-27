package coreEngine.atdm.DataStruct;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author jltrask
 */
public class ATDMStrategy implements Serializable, Cloneable {

    private String description;

    private int id;

    private String category;

    private float[] adjFactors;

    private int incidentDurationReduction;

    private static final long serialVersionUID = 24865713845L;

    private static final String[] categoriesString = {"Control Strategy", "Advisory Strategy", "Treatment Strategy",
        "Site Management & Traffic Control", "Detection & Verification",
        "Quick Clearance & Recovery"};

    /**
     *
     * @param id
     * @param description
     */
    public ATDMStrategy(int id, String description) {

        this.id = id;
        this.description = description;
        this.category = categoriesString[0];

        this.adjFactors = new float[3];
        Arrays.fill(adjFactors, 1.0f);
        incidentDurationReduction = 0;

    }

    /**
     *
     * @param id
     * @param description
     * @param categoryType
     */
    public ATDMStrategy(int id, String description, int categoryType) {

        this.id = id;
        this.description = description;
        this.category = categoriesString[categoryType];

        this.adjFactors = new float[4];
        Arrays.fill(adjFactors, 1.0f);

    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @return
     */
    public float[] getAdjFactor() {
        return adjFactors;
    }

    /**
     *
     * @param afIdx
     * @return
     */
    public float getAdjFactor(int afIdx) {
        return adjFactors[afIdx];
    }

    /**
     *
     * @return
     */
    public int getIncidentDurationReduction() {
        return incidentDurationReduction;
    }

    // </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setters">
    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @param categoryIdx
     */
    public void setCategory(int categoryIdx) {
        this.category = categoriesString[categoryIdx];
    }

    /**
     *
     * @param adjFactors
     */
    public void setAdjFactors(float[] adjFactors) {
        this.adjFactors = adjFactors;
    }

    /**
     *
     * @param newVal
     * @param afIdx
     */
    public void setAdjFactor(float newVal, int afIdx) {
        this.adjFactors[afIdx] = newVal;
    }

    /**
     *
     * @param incidentDurationReduction
     */
    public void setIncidentDurationReduction(int incidentDurationReduction) {
        this.incidentDurationReduction = incidentDurationReduction;
    }

//</editor-fold>
    @Override
    public ATDMStrategy clone() throws CloneNotSupportedException {
        return (ATDMStrategy) super.clone();
    }

}
