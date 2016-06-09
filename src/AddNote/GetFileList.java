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

    GetFileList(String Path){
    	this.strPath = Path;
//        List<File> fileList = new ArrayList<File>();
//        File dir = new File(strPath);
//        File[] files = dir.listFiles();
//        if(files != null) {
//            for(int i = 0; i< files.length; i++){
//                String fileName = files[i].getName();
//                
//                if(files[i].isDirectory()) {
//                    ;
//                }
//                else if(fileName.endsWith("xhtml")||fileName.endsWith("html")){
//                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println(strFileName );
//                    fileList.add(files[i]);
//                }
//            }
//        }
        
//        FileList = fileList;
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
    
    public List<File> traverseFolder2(String path) {  
        
        File file = new File(path);  
        if (file.exists()) {  
            File[] files = file.listFiles();  
            if (files.length == 0) {  
                System.out.println("文件夹是空的!");  
                //return;  
            } else {  
                for (File file2 : files) {  
                    if (file2.isDirectory()) {  
                        System.out.println("文件夹:" + file2.getAbsolutePath());  
                        traverseFolder2(file2.getAbsolutePath());  
                    } else {  
                        System.out.println("文件:" + file2.getAbsolutePath());  
                        this.FileList.add(file2);
                    }  
                }  
            }  
        } else {  
            System.out.println("文件不存在!");  
        }  
        return this.FileList;
    }
    
    public List<File> traverseFolder3(String path){
    	this.FileList = new ArrayList<File>();
    	return this.traverseFolder2(path);
    }
    
    
    
}
