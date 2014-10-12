/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DSS.DataStruct;

/**
 *
 * @author jltrask
 */
public class ScenarioEvent {

    public float caf = 1.0f;
    public float daf = 1.0f;
    public float saf = 1.0f;
    public int laf = 0;
    public int startPeriod = 0;
    public int endPeriod = 0;
    public int startSegment;
    public int endSegment;

    public final String type;

    public final static String WEATHER_EVENT = "WEATHER_EVENT";
    public final static String INCIDENT_EVENT = "INCIDENT_EVENT";

    public ScenarioEvent(String type) {
        this.type = type;
    }
}
