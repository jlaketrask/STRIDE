package coreEngine;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Compressed 2-D int matrix (line compression version)
 *
 * @author Shu Liu
 */
public class CM2DInt implements Serializable {

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
    private int[][] fullMatrix;

    /**
     *
     */
    private int[] lineNum;

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
    public CM2DInt(int sizeX, int sizeY, int defaultValue) {
        if (sizeX <= 0 || sizeY <= 0) {
            throw new IllegalArgumentException("Invalid size");
        }

        this.sizeX = sizeX;
        this.sizeY = sizeY;

        fullMatrix = new int[sizeX][sizeY];

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
     */
    public void set(int value, int x, int y) {
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
    public void set(int value, int fromX, int fromY, int toX, int toY) {
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
     */
    public void add(int value, int x, int y) {
        fullMatrix[x][y] += value;
    }

    /**
     *
     * @param value
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     */
    public void add(int value, int fromX, int fromY, int toX, int toY) {
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
     */
    public void multiply(int value, int x, int y) {
        fullMatrix[x][y] *= value;
    }

    /**
     *
     * @param value
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     */
    public void multiply(int value, int fromX, int fromY, int toX, int toY) {
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
     * @return
     */
    public int get(int x, int y) {
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
        lineNum = (int[]) in.readObject();
        lineRep = (int[]) in.readObject();
        expandMatrix();
        lineNum = null;
        lineRep = null;
    }

    /**
     *
     */
    private void compressMatrix() {
        int temp = fullMatrix[0][0];
        int count = 0;
        ArrayList<Integer> number = new ArrayList<>();
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

        lineNum = new int[number.size()];
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
        int value = lineNum[index];
        int count = lineRep[index];

        fullMatrix = new int[sizeX][sizeY];

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

    public void deepCopyFrom(CM2DInt newMatrix) {
        if (newMatrix.sizeX == sizeX && newMatrix.sizeY == sizeY) {
            fullMatrix = newMatrix.fullMatrix.clone();
            for (int i  = 0; i < sizeX; i++) {
                fullMatrix[i] = newMatrix.fullMatrix[i].clone();
            }
        }
    }
}
