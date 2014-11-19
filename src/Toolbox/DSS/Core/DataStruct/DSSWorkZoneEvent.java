/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Toolbox.DSS.Core.DataStruct;

import coreEngine.Helper.CEDate;
import coreEngine.reliabilityAnalysis.DataStruct.WorkZoneData;

/**
 *
 * @author jltrask
 */
public class DSSWorkZoneEvent extends WorkZoneData {

    public float caf;
    public float daf;
    public float saf;
    public float laf;

    public DSSWorkZoneEvent(int startSegment, int endSegment, int startPeriod, int endPeriod, int severity) {
        super(new CEDate(), new CEDate(), startSegment, endSegment, startPeriod, endPeriod, severity);
    }
}
