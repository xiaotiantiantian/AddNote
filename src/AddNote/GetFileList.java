/*
b * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddNote;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Zhirun Tian
 */
public class GetFileList {
    private List<File> FileList;
    private String strPath;
    
    GetFileList(){
    	
    }

    GetFileList(String strPath){
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
//                else if(fileName.endsWith("xhtml")){
//                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println(strFileName );
//                    fileList.add(files[i]);
//                }
//            }
//        }
//        
//        FileList = fileList;
//        //return fileList;
    	this.strPath = strPath;
        
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
    
    public void traverseFolder1(String path) {
		int fileNum = 0, folderNum = 0;
		File file = new File(path);
		if (file.exists()) {
			LinkedList<File> list = new LinkedList<File>();
			File[] files = file.listFiles();
			for (File file2 : files) {
				if (file2.isDirectory()) {
					System.out.println("文件夹:" + file2.getAbsolutePath());
					list.add(file2);
					fileNum++;
				} else {
					System.out.println("文件:" + file2.getAbsolutePath());
					folderNum++;
				}
			}
			File temp_file;
			while (!list.isEmpty()) {
				temp_file = list.removeFirst();
				files = temp_file.listFiles();
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("文件夹:" + file2.getAbsolutePath());
						list.add(file2);
						fileNum++;
					} else {
						System.out.println("文件:" + file2.getAbsolutePath());
						folderNum++;
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);

	}
    
    public List<File> traverseFolder2(String path) {
    	List<File> FileList = new ArrayList<File>();

		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹是空的!");
			} 
			else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("文件夹:" + file2.getAbsolutePath());
						traverseFolder2(file2.getAbsolutePath());
					} else {
						System.out.println("文件:" + file2.getAbsolutePath());
						FileList.add(file2);
					}
					
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		return FileList;
	}
    
    
    
    
}
