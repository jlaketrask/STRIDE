/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreEngine.reliabilityAnalysis.DataStruct;

import coreEngine.Seed;

/**
 *
 * @author jltrask
 */
public class WeatherEvent extends ScenarioEvent {

    private static final long serialVersionUID = 156355516425L;

    public WeatherEvent(Seed seed, int scenarioIdx, int severity, int startPeriod, int duration) {
        super(seed, scenarioIdx, severity, startPeriod, duration);
    }

    /**
     * Checks to see if the event overlaps with the specified weather event. The
     * input event must be a weather event or else the method will automatically
     * return false.
     *
     * @param event Event with which to check overlap.
     * @return True if the events overlap, false otherwise.
     */
    @Override
    public boolean hasOverlap(ScenarioEvent event) {
        try {
            WeatherEvent wEvent = (WeatherEvent) event;
            if (this.startPeriod <= wEvent.startPeriod && this.startPeriod >= wEvent.duration) {
                return true;
            } else if (this.startPeriod >= wEvent.startPeriod && this.startPeriod <= wEvent.startPeriod + wEvent.duration) {
                return true;
            }
        } catch (ClassCastException e) {
            System.err.println("Comparing events of different types.");
            return false;
        }
        return false;
    }

    /**
     * Checks to see if the event overlaps with the specified weather event.
     *
     * @param wEvent Event with which to check overlap.
     * @return True if the events overlap, false otherwise.
     */
    public boolean hasOverlap(WeatherEvent wEvent) {
        if (this.startPeriod <= wEvent.startPeriod && this.startPeriod >= wEvent.duration) {
            return true;
        } else if (this.startPeriod >= wEvent.startPeriod && this.startPeriod <= wEvent.startPeriod + wEvent.duration) {
            return true;
        }
        return false;
    }

    @Override
    public float getEventCAF(int period, int segment) {
        return this.seed.getWeatherCAF()[severity];
    }

    public float getEventCAF() {
        return this.getEventCAF(0, 0);
    }

    @Override
    public float getEventOAF(int period, int segment) {
        return this.seed.getWeatherDAF()[severity];
    }

    public float getEventOAF() {
        return this.getEventOAF(0, 0);
    }

    @Override
    public float getEventDAF(int period, int segment) {
        return this.seed.getWeatherDAF()[severity];
    }

    public float getEventDAF() {
        return this.getEventDAF(0, 0);
    }

    @Override
    public float getEventSAF(int period, int segment) {
        return this.seed.getWeatherSAF()[severity];
    }

    public float getEventSAF() {
        return this.getEventSAF(0, 0);
    }

    @Override
    public int getEventLAF(int period, int segment) {
        return 0;
    }

    public float getEventLAF() {
        return this.getEventLAF(0, 0);
    }

}
