package GUI.DSS.IOHelper;

import DSS.DataStruct.UserLevelParameterSet;
import GUI.seedEditAndIOHelper.*;
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
public class DSSIOHelper {

    /**
     * Save seed to original .dss file
     *
     * @param seed Seed to be saved
     * @param userParams User Level Parameters to be used.
     * @return whether save seed is successful
     */
    public static String saveDSSProject(Seed seed, UserLevelParameterSet userParams) {
        if (seed == null || userParams == null) {
            JOptionPane.showMessageDialog(null, "Invalid seed or parameter selection", "Error", JOptionPane.ERROR_MESSAGE);
            return "Failed to save";
        }

        if (seed.getValueString(CEConst.IDS_SEED_FILE_NAME) == null) {
            return saveAsDSSProject(seed, userParams);
        } else {
            //save seed to file
            try {
                DSSProject dssProject = new DSSProject(seed, userParams);
                FileOutputStream fos = new FileOutputStream(seed.getValueString(CEConst.IDS_SEED_FILE_NAME));
                GZIPOutputStream gzos = new GZIPOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(gzos);
                oos.writeObject(dssProject);
                oos.close();
                return "DSS Project saved to " + seed.getValueString(CEConst.IDS_SEED_FILE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to save " + e.toString();
            }
        }
    }

    /**
     * Save seed to another .seed file
     *
     * @param seed Seed to be saved.
     * @param userParams User Level Parameter Set to be saved.
     * @return whether save seed is successful
     */
    public static String saveAsDSSProject(Seed seed, UserLevelParameterSet userParams) {
        if (seed == null || userParams == null) {
            JOptionPane.showMessageDialog(null, "No seed or parameter set is selected", "Error", JOptionPane.ERROR_MESSAGE);
            return "Failed to save";
        }

        DSSProjectFileChooser dssProjectFileChooser = new DSSProjectFileChooser();
        int option = dssProjectFileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            String saveFileName = dssProjectFileChooser.getSelectedFile().getAbsolutePath();
            if (!saveFileName.endsWith(".dss")) {
                saveFileName += ".dss";
            }
            //save seed to file

            try {
                DSSProject dssProject = new DSSProject(seed, userParams);
                FileOutputStream fos = new FileOutputStream(saveFileName);
                GZIPOutputStream gzos = new GZIPOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(gzos);
                oos.writeObject(dssProject);
                oos.close();
                seed.setValue(CEConst.IDS_SEED_FILE_NAME, saveFileName);
                return "DSS Project saved to " + seed.getValueString(CEConst.IDS_SEED_FILE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to save " + e.toString();
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
    public static DSSProject openDSSProject() {
        Seed seed;
        DSSProject dssProject = null;
        DSSProjectFileChooser dssProjectFileChooser = new DSSProjectFileChooser();
        int option = dssProjectFileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            String openFileName = dssProjectFileChooser.getSelectedFile().getAbsolutePath();

            //open DSSProject from file
            try {
                FileInputStream fis = new FileInputStream(openFileName);
                GZIPInputStream gzis = new GZIPInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(gzis);
                dssProject = (DSSProject) ois.readObject();
                ois.close();
                seed = dssProject.getSeed();
                seed.resetSeedToInputOnly();
                seed.setValue(CEConst.IDS_SEED_FILE_NAME, openFileName);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.toString());
            }
        }
        return dssProject;
    }

}
