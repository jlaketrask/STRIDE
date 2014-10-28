package coreEngine.Helper;

import CompressArray.CA2DInt;
import coreEngine.Seed;
import java.io.Serializable;

/**
 *
 * @author Lake Trask
 */
public class RMHelper implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 92352654736L;

    private final CA2DInt RMType;
    private final CA2DInt RMRate;

    public RMHelper(Seed seed) {
        int numSegments = seed.getValueInt(CEConst.IDS_NUM_SEGMENT);
        int numPeriods = seed.getValueInt(CEConst.IDS_NUM_PERIOD);
        RMType = new CA2DInt(numSegments, numPeriods, CEConst.IDS_RAMP_METERING_TYPE_NONE);
        RMRate = new CA2DInt(numSegments, numPeriods, 2100);
    }

    public RMHelper(int numSegments, int numPeriods) {
        RMType = new CA2DInt(numSegments, numPeriods, CEConst.IDS_RAMP_METERING_TYPE_NONE);
        RMRate = new CA2DInt(numSegments, numPeriods, 2100);
    }

    public CA2DInt getRampMeteringType() {
        return RMType;
    }

    public CA2DInt getRampMeteringFixRate() {
        return RMRate;
    }

    public void setGlobalRMType(int rampMeteringType) {
        RMType.set(rampMeteringType, 0, 0, RMType.getSizeX() - 1, RMType.getSizeY() - 1);
    }

    public void removePeriod(int startPeriod, int numPeriodToBeDeleted) {
        getRampMeteringFixRate().removeColumn(startPeriod, startPeriod + numPeriodToBeDeleted - 1);
        getRampMeteringType().removeColumn(startPeriod, startPeriod + numPeriodToBeDeleted - 1);
    }

    public void removeSegment(int startSegment, int numSegmentToBeDeleted) {
        getRampMeteringFixRate().removeRow(startSegment, startSegment + numSegmentToBeDeleted - 1);
        getRampMeteringType().removeRow(startSegment, startSegment + numSegmentToBeDeleted - 1);
    }

    public void addPeriod(int startPeriod, int numPeriodToBeAdded) {
        getRampMeteringFixRate().addColumn(startPeriod, numPeriodToBeAdded, 2100);
        getRampMeteringType().addColumn(startPeriod, numPeriodToBeAdded, 0);
    }

    public void addSegment(int startSegment, int numSegmentToBeAdded) {
        getRampMeteringFixRate().addRow(startSegment, numSegmentToBeAdded, 2100);
        getRampMeteringType().addRow(startSegment, numSegmentToBeAdded, 0);
    }
}
