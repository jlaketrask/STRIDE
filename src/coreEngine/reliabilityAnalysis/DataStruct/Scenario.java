package coreEngine.reliabilityAnalysis.DataStruct;

import CompressArray.CA3DFloat;
import CompressArray.CA3DInt;
import java.io.Serializable;

/**
 * This class contains information (adjustment factors) for one scenario.
 *
 * @author Shu Liu
 * @author Lake Trask
 */
public class Scenario implements Serializable {

    private static final long serialVersionUID = 59009123790834L;

    private CA3DFloat CAF;

    private CA3DFloat SAF;

    private CA3DFloat DAF;

    private CA3DFloat OAF;

    private CA3DInt LAFI;

    private CA3DInt LAFWZ;

    private int numScen;

    /**
     *
     * @param numScen
     * @param numSeg
     * @param numPeriod
     */
    public Scenario(int numScen, int numSeg, int numPeriod) {
        this.numScen = numScen;
        CAF = new CA3DFloat(numScen, numSeg, numPeriod, 1);
        SAF = new CA3DFloat(numScen, numSeg, numPeriod, 1);
        DAF = new CA3DFloat(numScen, numSeg, numPeriod, 1);
        OAF = new CA3DFloat(numScen, numSeg, numPeriod, 1);
        LAFI = new CA3DInt(numScen, numSeg, numPeriod, 0);
        LAFWZ = new CA3DInt(numScen, numSeg, numPeriod, 0);
    }

    /**
     *
     * @return
     */
    public CA3DFloat CAF() {
        return CAF;
    }

    /**
     *
     * @return
     */
    public CA3DFloat SAF() {
        return SAF;
    }

    /**
     *
     * @return
     */
    public CA3DFloat DAF() {
        return DAF;
    }

    /**
     *
     * @return
     */
    public CA3DFloat OAF() {
        return OAF;
    }

    /**
     *
     * @return
     */
    public CA3DInt LAFI() {
        return LAFI;
    }

    /**
     *
     * @return
     */
    public CA3DInt LAFWZ() {
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
