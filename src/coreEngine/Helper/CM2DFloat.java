package coreEngine.Helper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Compressed 2-D float matrix (line compression version)
 *
 * @author Shu Liu
 */
public class CM2DFloat implements Serializable {

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
    private float[][] fullMatrix;

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
     * @param defaultValue
     */
    public CM2DFloat(int sizeX, int sizeY, float defaultValue) {
        if (sizeX <= 0 || sizeY <= 0) {
            throw new IllegalArgumentException("Invalid size");
        }

        this.sizeX = sizeX;
        this.sizeY = sizeY;

        fullMatrix = new float[sizeX][sizeY];

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                fullMatrix[x][y] = defaultValue;
            }
        }
    }

    /**
     *
     * @param value
     * @param x
     * @param y
     *
     */
    public void set(float value, int x, int y) {
        fullMatrix[x][y] = value;
    }

    /**
     *
     * @param value
     * @param fromX
     * @param fromY
     *
     * @param toX
     * @param toY
     *
     */
    public void set(float value, int fromX, int fromY, int toX, int toY) {
        for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
                fullMatrix[x][y] = value;
            }
        }
    }

    /**
     *
     * @param value
     * @param x
     * @param y
     *
     */
    public void add(float value, int x, int y) {
        fullMatrix[x][y] += value;
    }

    /**
     *
     * @param value
     * @param fromX
     * @param fromY
     *
     * @param toX
     * @param toY
     *
     */
    public void add(float value, int fromX, int fromY, int toX, int toY) {
        for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
                fullMatrix[x][y] += value;
            }
        }
    }

    /**
     *
     * @param value
     * @param x
     * @param y
     *
     */
    public void multiply(float value, int x, int y) {
        fullMatrix[x][y] *= value;
    }

    /**
     *
     * @param value
     * @param fromX
     * @param fromY
     *
     * @param toX
     * @param toY
     *
     */
    public void multiply(float value, int fromX, int fromY, int toX, int toY) {
        for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
                fullMatrix[x][y] *= value;
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     *
     * @return
     */
    public float get(int x, int y) {
        return fullMatrix[x][y];
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
     * @param out
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        compressMatrix();
        out.writeInt(sizeX);
        out.writeInt(sizeY);
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
        float temp = fullMatrix[0][0];
        int count = 0;
        ArrayList<Float> number = new ArrayList<>();
        ArrayList<Integer> repetition = new ArrayList<>();

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (fullMatrix[x][y] != temp) {
                    number.add(temp);
                    repetition.add(count);
                    temp = fullMatrix[x][y];
                    count = 1;
                } else {
                    count++;
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
        fullMatrix = new float[sizeX][sizeY];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (count == 0) {
                    index++;
                    value = lineNum[index];
                    count = lineRep[index];
                }
                fullMatrix[x][y] = value;
                count--;
            }
        }
    }
}
