package GUI.seedEditAndIOHelper;

import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * This class contains static helper functions for .seed file input and output
 *
 * @author Shu Liu
 */
public class SeedIOHelper {

    /**
     * Save seed to original .seed file
     *
     * @param seed seed to be saved
     * @return whether save seed is successful
     */
    public static String saveSeed(Seed seed) {
        if (seed == null) {
            JOptionPane.showMessageDialog(null, "No seed is selected", "Error", JOptionPane.ERROR_MESSAGE);
            return "Fail to save seed";
        }

        if (seed.getValueString(CEConst.IDS_SEED_FILE_NAME) == null) {
            return saveAsSeed(seed);
        } else {
            //save seed to file
            try {
                FileOutputStream fos = new FileOutputStream(seed.getValueString(CEConst.IDS_SEED_FILE_NAME));
                GZIPOutputStream gzos = new GZIPOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(gzos);
                oos.writeObject(seed);
                oos.close();
                return "Seed saved to " + seed.getValueString(CEConst.IDS_SEED_FILE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                return "Fail to save seed " + e.toString();
            }
        }
    }

    /**
     * Save seed to another .seed file
     *
     * @param seed seed to be saved
     * @return whether save seed is successful
     */
    public static String saveAsSeed(Seed seed) {
        if (seed == null) {
            JOptionPane.showMessageDialog(null, "No seed is selected", "Error", JOptionPane.ERROR_MESSAGE);
            return "Fail to save seed";
        }

        SeedFileChooser seedFileChooser = new SeedFileChooser();
        int option = seedFileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            String saveFileName = seedFileChooser.getSelectedFile().getAbsolutePath();
            if (!saveFileName.endsWith(".seed")) {
                saveFileName += ".seed";
            }
            //save seed to file

            try {
                FileOutputStream fos = new FileOutputStream(saveFileName);
                GZIPOutputStream gzos = new GZIPOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(gzos);
                oos.writeObject(seed);
                oos.close();
                seed.setValue(CEConst.IDS_SEED_FILE_NAME, saveFileName);
                return "Seed saved to " + seed.getValueString(CEConst.IDS_SEED_FILE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                return "Fail to save seed " + e.toString();
            }
        } else {
            return "Save cancelled by user";
        }
    }

    /**
     * Open a .seed file
     *
     * @return a Seed class object contains the .seed file
     */
    public static Seed openSeed() {
        Seed seed = null;
        SeedFileChooser seedFileChooser = new SeedFileChooser();
        int option = seedFileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            String openFileName = seedFileChooser.getSelectedFile().getAbsolutePath();

            //open seed from file
            try {
                FileInputStream fis = new FileInputStream(openFileName);
                GZIPInputStream gzis = new GZIPInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(gzis);
                seed = (Seed) ois.readObject();
                seed.resetSeedToInputOnly();
                ois.close();
                seed.setValue(CEConst.IDS_SEED_FILE_NAME, openFileName);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.toString());
            }
        }
        return seed;
    }

}
