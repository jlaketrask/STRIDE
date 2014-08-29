/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package atdm.DataStruct;

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
    
    private static final String[] categoriesString = {"Control Strategy","Advisory Strategy", "Treatment Strategy",
                                               "Site Management & Traffic Control", "Detection & Verification",
                                                "Quick Clearance & Recovery"};
    
    public ATDMStrategy(int id, String description) {
        
        this.id = id;
        this.description = description;
        this.category = categoriesString[0];
        
        this.adjFactors = new float[3];
        Arrays.fill(adjFactors, 1.0f);
        incidentDurationReduction = 0; 
        
    }
    
    public ATDMStrategy(int id, String description, int categoryType) {
        
        this.id = id;
        this.description = description;
        this.category = categoriesString[categoryType];
        
        this.adjFactors = new float[4];
        Arrays.fill(adjFactors, 1.0f);
        
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public float[] getAdjFactor() {
        return adjFactors;
    }
    
    public float getAdjFactor(int afIdx) {
        return adjFactors[afIdx];
    }

    public int getIncidentDurationReduction() {
        return incidentDurationReduction;
    }
    
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Setters">
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setCategory(int categoryIdx) {
        this.category = categoriesString[categoryIdx];
    }
    
    public void setAdjFactors(float[] adjFactors) {
        this.adjFactors = adjFactors;
    }
    
    public void setAdjFactor(float newVal, int afIdx) {
        this.adjFactors[afIdx] = newVal;
    }
    
    public void setIncidentDurationReduction(int incidentDurationReduction) {
        this.incidentDurationReduction = incidentDurationReduction;
    }
    
//</editor-fold>

    @Override
    public ATDMStrategy clone() throws CloneNotSupportedException {
        return (ATDMStrategy) super.clone();
    }
    

}
