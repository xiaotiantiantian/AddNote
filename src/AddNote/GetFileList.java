/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddNote;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zhirun Tian
 */
public class GetFileList {
    private List<File> FileList;
    private String strPath;

    GetFileList(String strPath){
        List<File> fileList = new ArrayList<File>();
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        if(files != null) {
            for(int i = 0; i< files.length; i++){
                String fileName = files[i].getName();
                
                if(files[i].isDirectory()) {
                    ;
                }
                else if(fileName.endsWith("xhtml")){
                    String strFileName = files[i].getAbsolutePath();
                    System.out.println(strFileName );
                    fileList.add(files[i]);
                }
            }
        }
        
        FileList = fileList;
        //return fileList;
        
    }

    public List<File> getFileList() {
        return FileList;
    }

    public void setFileList(List<File> FileList) {
        this.FileList = FileList;
    }

    public String getStrPath() {
        return strPath;
    }

    public void setStrPath(String strPath) {
        this.strPath = strPath;
    }
    
    
    
    
    
}
