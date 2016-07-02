package AddNote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Unzip {

	public Unzip() {
		// TODO Auto-generated constructor stub
	}
	/*
     * @param srcZipFile 需解压的文件名
     * @return  如果解压成功返回true
     */
     public boolean unzip(String srcZipFile, String OutputFolder) {
          boolean isSuccessful = true;
          try {
        	  File file = new File(srcZipFile);//压缩文件
              ZipFile zipFile = new ZipFile(file);//实例化ZipFile，每一个zip压缩文件都可以表示为一个ZipFile
              //实例化一个Zip压缩文件的ZipInputStream对象，可以利用该类的getNextEntry()方法依次拿到每一个ZipEntry对象
              ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
              ZipEntry zipEntry = null;
              while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                  String fileName = zipEntry.getName();
                  File temp = new File(OutputFolder + fileName);
                  if (! temp.getParentFile().exists())
                      temp.getParentFile().mkdirs();
                  BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(temp));//文件缓冲区
                  //OutputStream os = new FileOutputStream(temp);
                  //通过ZipFile的getInputStream方法拿到具体的ZipEntry的输入流
                  InputStream is = zipFile.getInputStream(zipEntry);
                  byte[] datas = new byte[2048];
                  int len = 0;
                  while ((len = is.read(datas)) != -1)
                      os.write(datas,0,len);
                  os.close();
                  is.close();
              }
              zipInputStream.close();
          } catch (IOException e) {
               isSuccessful = false;
          }
          return isSuccessful;
     }

}
