package coreEngine.Helper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Compressed 3-D float matrix (line compression version)
 *
 * @author Shu Liu
 */
public class CM3DFloat implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 33491658567234L;

    /**
     *
     */
    private int sizeX;

    /**
     *
     */
    private int sizeY;

    /**
     *
     */
    private int sizeZ;

    /**
     *
     */
    private float[][][] fullMatrix;

    /**
     *
     */
    private float[] lineNum;

    /**
     *
     */
    private int[] lineRep;

    /**
     *
     * @param sizeX
     * @param sizeY
     * @param sizeZ
     * @param defaultValue
     */
    public CM3DFloat(int sizeX, int sizeY, int sizeZ, int defaultValue) {
        if (sizeX <= 0 || sizeY <= 0 || sizeZ <= 0) {
            throw new IllegalArgumentException("Invalid size");
        }

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        fullMatrix = new float[sizeX][sizeY][sizeZ];

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    fullMatrix[x][y][z] = defaultValue;
                }
            }
        }
    }

    /**
     *
     * @param value
     * @param x
     * @param y
     * @param z
     */
    public void set(float value, int x, int y, int z) {
        fullMatrix[x][y][z] = value;
    }

    /**
     *
     * @param value
     * @param fromX
     * @param fromY
     * @param fromZ
     * @param toX
     * @param toY
     * @param toZ
     */
    public void set(float value, int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
                for (int z = fromZ; z <= toZ; z++) {
                    fullMatrix[x][y][z] = value;
                }
            }
        }
    }

    /**
     *
     * @param value
     * @param x
     * @param y
     * @param z
     */
    public void add(float value, int x, int y, int z) {
        fullMatrix[x][y][z] += value;
    }

    /**
     *
     * @param value
     * @param fromX
     * @param fromY
     * @param fromZ
     * @param toX
     * @param toY
     * @param toZ
     */
    public void add(float value, int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
                for (int z = fromZ; z <= toZ; z++) {
                    fullMatrix[x][y][z] += value;
                }
            }
        }
    }

    /**
     *
     * @param value
     * @param x
     * @param y
     * @param z
     */
    public void multiply(float value, int x, int y, int z) {
        fullMatrix[x][y][z] *= value;
    }

    /**
     *
     * @param value
     * @param fromX
     * @param fromY
     * @param fromZ
     * @param toX
     * @param toY
     * @param toZ
     */
    public void multiply(float value, int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
                for (int z = fromZ; z <= toZ; z++) {
                    fullMatrix[x][y][z] *= value;
                }
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public float get(int x, int y, int z) {
        return fullMatrix[x][y][z];
    }

    /**
     *
     * @return
     */
    public int getSizeX() {
        return sizeX;
    }
    
    /**
     *
     * @return
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     *
     * @return
     */
    public int getSizeZ() {
        return sizeZ;
    }

    /**
     *
     * @param out
     * @throws IOException
     */
    
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        compressMatrix();
        out.writeInt(sizeX);
        out.writeInt(sizeY);
        out.writeInt(sizeZ);
        out.writeObject(lineNum);
        out.writeObject(lineRep);
        lineNum = null;
        lineRep = null;
    }

    /**
     *
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        sizeX = in.readInt();
        sizeY = in.readInt();
        sizeZ = in.readInt();
        lineNum = (float[]) in.readObject();
        lineRep = (int[]) in.readObject();
        expandMatrix();
        lineNum = null;
        lineRep = null;
    }

    /**
     *
     */
    private void compressMatrix() {
        float temp = fullMatrix[0][0][0];
        int count = 0;
        ArrayList<Float> number = new ArrayList<>();
        ArrayList<Integer> repetition = new ArrayList<>();

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    if (fullMatrix[x][y][z] != temp) {
                        number.add(temp);
                        repetition.add(count);
                        temp = fullMatrix[x][y][z];
                        count = 1;
                    } else {
                        count++;
                    }
                }
            }
        }
        number.add(temp);
        repetition.add(count);

        lineNum = new float[number.size()];
        for (int index = 0; index < lineNum.length; index++) {
            lineNum[index] = number.get(index);
        }
        lineRep = new int[repetition.size()];
        for (int index = 0; index < lineRep.length; index++) {
            lineRep[index] = repetition.get(index);
        }
    }

    /**
     *
     */
    private void expandMatrix() {

        int index = 0;
        float value = lineNum[index];
        int count = lineRep[index];

        fullMatrix = new float[sizeX][sizeY][sizeZ];

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    if (count == 0) {
                        index++;
                        value = lineNum[index];
                        count = lineRep[index];
                    }
                    fullMatrix[x][y][z] = value;
                    count--;
                }
            }
        }
    }
}
