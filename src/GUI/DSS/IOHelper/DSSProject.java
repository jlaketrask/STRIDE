/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.DSS.IOHelper;

import DSS.DataStruct.UserLevelParameterSet;
import coreEngine.Seed;
import java.io.Serializable;

/**
 *
 * @author jltrask
 */
public class DSSProject implements Serializable {
    
    private static final long serialVersionUID = 4681567916985L;
    
    private final Seed seed;
    private final UserLevelParameterSet userParams;
    
    //private String projectFileName;
    
    public DSSProject(Seed seed, UserLevelParameterSet userParams) {
        this.seed = seed;
        this.userParams = userParams;
    }

    public Seed getSeed() {
        return seed;
    }

    public UserLevelParameterSet getUserLevelParameterSet() {
        return userParams;
    }

//    public String getProjectFileName() {
//        return projectFileName;
//    }
//
//    public void setProjectFileName(String projectFileName) {
//        this.projectFileName = projectFileName;
//    }  
    
}
