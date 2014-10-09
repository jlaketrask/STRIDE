package atdm.DataStruct;

import coreEngine.CEConst;
import coreEngine.CM2DFloat;
import coreEngine.CM2DInt;
import java.io.Serializable;

/**
 * This class contains information (adjustment factors) for one scenario.
 *
 * @author Shu Liu
 * @author Lake Trask
 */
public class ATDMScenario implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 15346363453532L;

    /**
     *
     */
    private final CM2DFloat CAF;

    /**
     *
     */
    private final CM2DFloat SAF;

    /**
     *
     */
    private final CM2DFloat DAF;

    /**
     *
     */
    private final CM2DFloat OAF;

    /**
     *
     */
    private final CM2DInt RM;
    
    /**
     *
     */
    private final CM2DInt LAF;
    
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
        CAF = new CM2DFloat(numSeg, numPeriod, 1);
        SAF = new CM2DFloat(numSeg, numPeriod, 1);
        DAF = new CM2DFloat(numSeg, numPeriod, 1);
        OAF = new CM2DFloat(numSeg, numPeriod, 1);
        RM = new CM2DInt(numSeg, numPeriod, 2100);
        LAF = new CM2DInt(numSeg, numPeriod, 0);
        hasRampMetering = false;
    }

    /**
     *
     * @return
     */
    public CM2DFloat CAF() {
        return CAF;
    }

    /**
     *
     * @return
     */
    public CM2DFloat SAF() {
        return SAF;
    }

    /**
     *
     * @return
     */
    public CM2DFloat DAF() {
        return DAF;
    }

    /**
     *
     * @return
     */
    public CM2DFloat OAF() {
        return OAF;
    }
    
    /**
     *
     * @return
     */
    public CM2DInt RM() {
        return RM;
    }

    /**
     *
     * @return
     */
    public CM2DInt LAF() {
        return LAF;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public void setRampMetering(boolean val) {
        this.hasRampMetering = val;
    }
    
    public boolean hasRampMetering() {
        return this.hasRampMetering;
    }
}
