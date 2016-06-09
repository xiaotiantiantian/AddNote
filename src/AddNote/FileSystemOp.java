/**
 * 
 */
package AddNote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.*
import java.nio.channels.FileChannel;

/**
 * @author Zhirun Tian
 *
 */
public class FileSystemOp {

	/**
	 * 
	 */
	public FileSystemOp() {
		// TODO Auto-generated constructor stub
	}
	
    public void nioTransferCopy(File source, File target) throws IOException {  
        FileChannel in = null;  
        FileChannel out = null;  
        FileInputStream inStream = null;  
        FileOutputStream outStream = null;  
        try {  
            inStream = new FileInputStream(source);  
            outStream = new FileOutputStream(target);  
            in = inStream.getChannel();  
            out = outStream.getChannel();  
            in.transferTo(0, in.size(), out);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
//            close(inStream);  
//            close(in);  
//            close(outStream);  
//            close(out);  
        	inStream.close();
        	in.close();
        	outStream.close();
        	out.close();
        }  
    }  

}
