package coreEngine.reliabilityAnalysis.DataStruct;

import coreEngine.Helper.CM3DFloat;
import coreEngine.Helper.CM3DInt;
import java.io.Serializable;

/**
 * This class contains information (adjustment factors) for one scenario.
 *
 * @author Shu Liu
 * @author Lake Trask
 */
public class Scenario implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 59009123790834L;

    /**
     *
     */
    private CM3DFloat CAF;

    /**
     *
     */
    private CM3DFloat SAF;

    /**
     *
     */
    private CM3DFloat DAF;

    /**
     *
     */
    private CM3DFloat OAF;

    /**
     *
     */
    private CM3DInt LAFI;

    /**
     *
     */
    private CM3DInt LAFWZ;

    /**
     *
     */
    private int numScen;

    /**
     *
     * @param numScen
     * @param numSeg
     * @param numPeriod
     */
    public Scenario(int numScen, int numSeg, int numPeriod) {
        this.numScen = numScen;
        CAF = new CM3DFloat(numScen, numSeg, numPeriod, 1);
        SAF = new CM3DFloat(numScen, numSeg, numPeriod, 1);
        DAF = new CM3DFloat(numScen, numSeg, numPeriod, 1);
        OAF = new CM3DFloat(numScen, numSeg, numPeriod, 1);
        LAFI = new CM3DInt(numScen, numSeg, numPeriod, 0);
        LAFWZ = new CM3DInt(numScen, numSeg, numPeriod, 0);
    }

    /**
     *
     * @return
     */
    public CM3DFloat CAF() {
        return CAF;
    }

    /**
     *
     * @return
     */
    public CM3DFloat SAF() {
        return SAF;
    }

    /**
     *
     * @return
     */
    public CM3DFloat DAF() {
        return DAF;
    }

    /**
     *
     * @return
     */
    public CM3DFloat OAF() {
        return OAF;
    }

    /**
     *
     * @return
     */
    public CM3DInt LAFI() {
        return LAFI;
    }

    /**
     *
     * @return
     */
    public CM3DInt LAFWZ() {
        return LAFWZ;
    }

    /**
     *
     * @return
     */
    public int size() {
        return numScen;
    }
}
