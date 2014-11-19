/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Toolbox.DSS.Core.DataStruct;

import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import coreEngine.reliabilityAnalysis.DataStruct.IncidentEvent;

/**
 *
 * @author jltrask
 */
public class DSSIncidentEvent extends IncidentEvent {

    public float caf = 1.0f;
    public float daf = 1.0f;
    public float saf = 1.0f;
    public int laf = 0;

    public DSSIncidentEvent(Seed seed, int severity, int startPeriod, int duration, int segment) {
        super(seed, seed.getRLScenarioInfo().get(1), severity, startPeriod, duration, segment, CEConst.SEG_TYPE_GP);
    }

    @Override
    public float getEventCAF(int period, int segment) {
        return caf;
    }

    @Override
    public float getEventOAF(int period, int segment) {
        return daf;
    }

    @Override
    public float getEventDAF(int period, int segment) {
        return daf;
    }

    @Override
    public float getEventSAF(int period, int segment) {
        return saf;
    }

    @Override
    public int getEventLAF(int period, int segment) {
        return laf;
    }

}
