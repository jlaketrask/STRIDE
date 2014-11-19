package Toolbox.DSS.GUI.IOHelper;

import java.io.File;
import javax.swing.JFileChooser;

/**
 * This class is a .seed file chooser
 *
 * @author Shu Liu
 */
public class DSSProjectFileChooser extends JFileChooser {

    private class DSSProjectFileFilter extends javax.swing.filechooser.FileFilter {
        @Override
        public boolean accept(File file) {
            // Allow only directories, or files with ".seed" extension
            return file.isDirectory() || file.getAbsolutePath().endsWith(".dss");
        }

        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            return "FREEVAL-DSS Project files (*.dss)";
        }
    }

    /**
     * Constructor
     */
    public DSSProjectFileChooser() {
        super();
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setAcceptAllFileFilterUsed(false);
        setFileFilter(new DSSProjectFileFilter());
    }
}
