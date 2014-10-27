package coreEngine.atdm.DataStruct;

import CompressArray.CA2DFloat;
import CompressArray.CA2DInt;
import coreEngine.Helper.CEConst;
import java.io.Serializable;

/**
 * This class contains information (adjustment factors) for one scenario.
 *
 * @author Shu Liu
 * @author Lake Trask
 */
public class ATDMScenario implements Serializable {

    private static final long serialVersionUID = 15346363453532L;

    private final CA2DFloat CAF;

    private final CA2DFloat SAF;

    private final CA2DFloat DAF;

    private final CA2DFloat OAF;

    private final CA2DInt RM;

    private final CA2DInt LAF;

    private boolean hasRampMetering;

    private String name;

    private String discription;

    private int status = CEConst.SCENARIO_INPUT_ONLY;

    /**
     *
     * @param numSeg
     * @param numPeriod
     */
    public ATDMScenario(int numSeg, int numPeriod) {
        CAF = new CA2DFloat(numSeg, numPeriod, 1);
        SAF = new CA2DFloat(numSeg, numPeriod, 1);
        DAF = new CA2DFloat(numSeg, numPeriod, 1);
        OAF = new CA2DFloat(numSeg, numPeriod, 1);
        RM = new CA2DInt(numSeg, numPeriod, 2100);
        LAF = new CA2DInt(numSeg, numPeriod, 0);
        hasRampMetering = false;
    }

    /**
     *
     * @return
     */
    public CA2DFloat CAF() {
        return CAF;
    }

    /**
     *
     * @return
     */
    public CA2DFloat SAF() {
        return SAF;
    }

    /**
     *
     * @return
     */
    public CA2DFloat DAF() {
        return DAF;
    }

    /**
     *
     * @return
     */
    public CA2DFloat OAF() {
        return OAF;
    }

    /**
     *
     * @return
     */
    public CA2DInt RM() {
        return RM;
    }

    /**
     *
     * @return
     */
    public CA2DInt LAF() {
        return LAF;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getDiscription() {
        return discription;
    }

    /**
     *
     * @param discription
     */
    public void setDiscription(String discription) {
        this.discription = discription;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     *
     * @param val
     */
    public void setRampMetering(boolean val) {
        this.hasRampMetering = val;
    }

    /**
     *
     * @return
     */
    public boolean hasRampMetering() {
        return this.hasRampMetering;
    }
}
