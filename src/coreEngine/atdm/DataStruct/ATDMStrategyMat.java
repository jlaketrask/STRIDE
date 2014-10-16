/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreEngine.atdm.DataStruct;

import coreEngine.Helper.CEConst;
import coreEngine.Helper.CM2DInt;

/**
 *
 * @author jltrask
 */
public class ATDMStrategyMat extends ATDMStrategy {

    /**
     *
     */
    private CM2DInt strategyMatrix;

    /**
     *
     */
    private final int numSeg;

    /**
     *
     */
    private final int numPeriods;

    /**
     *
     */
    private final String strategyType;

    /**
     *
     */
    private float shoulderCapacity[];

    /**
     *
     * @param id
     * @param description
     * @param numSegments
     * @param numPeriods
     * @param strategyType
     */
    public ATDMStrategyMat(int id, String description, int numSegments, int numPeriods, String strategyType) {
        super(id, description);
        this.strategyType = strategyType;
        this.numSeg = numSegments;
        this.numPeriods = numPeriods;
        this.shoulderCapacity = new float[5];
        switch (this.strategyType) {
            case CEConst.IDS_ATDM_STRAT_TYPE_RAMP_METERING:
                this.strategyMatrix = new CM2DInt(numSeg, this.numPeriods, 2100);
                break;
            case CEConst.IDS_ATDM_STRAT_TYPE_HARD_SHOULDER_RUNNING:
                this.strategyMatrix = new CM2DInt(numSeg, this.numPeriods, 0);
                fillShoulderDefaults();
                break;
            default:
                throw new RuntimeException("Invalid Strategy Type");
        }
        
    }

    /**
     *
     * @return
     */
    public CM2DInt getStrategyMatrix() {
        return strategyMatrix;
    }

    /**
     *
     * @param newMatrix
     */
    public void setStrategyMatrix(CM2DInt newMatrix) {
        this.strategyMatrix = newMatrix;
    }

    /**
     *
     * @param numLanes
     * @return
     */
    public float getShoulderCapacity(int numLanes) {
        return shoulderCapacity[numLanes];
    }

    /**
     * 
     * @return 
     */
    public float[] getShoulderCapacity() {
        return shoulderCapacity;
    }
    
    /**
     *
     * @param newValue
     * @param numLanes
     */
    public void setShoulderCapacity(float newValue, int numLanes) {
        shoulderCapacity[numLanes] = newValue;
    }
    
    /**
     *
     * @param newValues
     */
    public void setShoulderCapacity(float[] newValues) {
        shoulderCapacity = newValues;
    }
    
    private void fillShoulderDefaults() {
        shoulderCapacity[0] = 0.7f;
        shoulderCapacity[1] = 0.75f;
        shoulderCapacity[2] = 0.8f;
        shoulderCapacity[3] = 0.85f;
        shoulderCapacity[4] = 0.9f;
        
    }

}
