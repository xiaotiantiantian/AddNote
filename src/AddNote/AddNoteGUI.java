package AddNote;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.List;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class AddNoteGUI {

	protected Shell shlEbookTool;
	private Text textFilePath;
	private Label lblPleaseNoteOnly;
	private Text textLog;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AddNoteGUI window = new AddNoteGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		// set the icon of main window
		Image image = new Image(Display.getCurrent(), "res/duducat.png");
		shlEbookTool.setImage(image);
		
		CTabFolder tabFolder = new CTabFolder(shlEbookTool, SWT.BORDER);
		tabFolder.setBounds(34, 171, 425, 157);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmPush = new CTabItem(tabFolder, SWT.NONE);
		tbtmPush.setText("转换/推送");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmPush.setControl(composite);
		
		Button btnConvert = new Button(composite, SWT.NONE);
		btnConvert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// 1. change the mobi/azw3 file to another name
				// kindleUnpack could not work with Chinese words
				FileSystemOp FileOp = new FileSystemOp();
				File origEbook = new File(textFilePath.getText());

				// create parent dir for it is not exist

				String tempFolder = origEbook.getParent()+ "\\temp";
				File tempFolderFile = new File(tempFolder);
				if (!tempFolderFile.mkdirs()) {
					System.out.println("创建目标文件所在目录失败！");
				}
				
				//get the file name extension
				String[] GetFileExt = textFilePath.getText().split("\\.");
				String FileExt = GetFileExt[GetFileExt.length-1];

				File newEbook = new File(origEbook.getParent()
						+ "\\temp\\temp."
						+ FileExt);
				try {
					FileOp.nioTransferCopy(origEbook, newEbook);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// use kindleUnpack to unpackage the temp.mobi/azw3
				String shellExec = "res//KindleUnpack.exe " + "-r" +" "
						+"\""+newEbook.getAbsolutePath() +"\""+ " "
						+"\""+ newEbook.getParent()+"\"";
				//String shellExec = "\"C:\\Users\\Zhirun Tian\\Documents\\GitHub\\AddNote\\res\\KindleUnpack.exe\" -r  \"C:\\Users\\Zhirun Tian\\Desktop\\temp\\temp.mobi\" \"C:\\Users\\Zhirun Tian\\Desktop\\temp\"";
				try {
					Process p0 = Runtime.getRuntime().exec(shellExec);
					// 读取标准输出流
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(p0.getInputStream()));
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						// System.out.println(line);
						//textLog.setText(textLog.getText() + "\r\n" + line);
						textLog.setText(textLog.getText() + line + "\r\n");
					}
					// 读取标准错误流
					BufferedReader brError = new BufferedReader(
							new InputStreamReader(p0.getErrorStream(), "gb2312"));
					String errline = null;
					while ((errline = brError.readLine()) != null) {
						System.out.println(errline);
					}
					// waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
					int c = p0.waitFor();
					if (c != 0) {
						// baseRes.put("desc", "软件升级失败：执行zxvf.sh异常终止");
						// baseRes.setReturnFlag(false);
						// return baseRes;
					}
				} catch (IOException e1) {
					// baseRes.put("desc", "unpackage file failed");
					// baseRes.setReturnFlag(false);
					// return baseRes;
				} catch (InterruptedException e1) {
					// baseRes.put("desc", "unpackage file failed");
					// baseRes.setReturnFlag(false);
					// return baseRes;
				}
				
				//1. find the opf file, if it is not in default/mobi8/OEBPS/content.opf, it should at default/mobi7/....
				String opfPath = newEbook.getParent()+"\\mobi8\\OEBPS\\content.opf";
				File opfFile = new File(opfPath);
				if(!opfFile.exists()){
					opfFile = new File(newEbook.getParent()+"\\mobi7\\OEBPS\\content.opf");
				}
				
				if(!opfFile.exists()){
					opfFile = new File(newEbook.getParent()+"\\mobi7\\content.opf");
				}
				String shellExecPack = "res//Kindlegen.exe " +" "
						+"\""+opfFile.getAbsolutePath()+"\"";
				//String shellExec = "\"C:\\Users\\Zhirun Tian\\Documents\\GitHub\\AddNote\\res\\KindleUnpack.exe\" -r  \"C:\\Users\\Zhirun Tian\\Desktop\\temp\\temp.mobi\" \"C:\\Users\\Zhirun Tian\\Desktop\\temp\"";
				try {
					Process p0 = Runtime.getRuntime().exec(shellExecPack);
					// 读取标准输出流
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(p0.getInputStream()));
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						// System.out.println(line);
						textLog.setText(textLog.getText() + line+ "\\n\\r");
					}
					// 读取标准错误流
					BufferedReader brError = new BufferedReader(
							new InputStreamReader(p0.getErrorStream(), "gb2312"));
					String errline = null;
					while ((errline = brError.readLine()) != null) {
						System.out.println(errline);
					}
					// waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
					int c = p0.waitFor();
					if (c != 0) {
						// baseRes.put("desc", "package file failed");
						// baseRes.setReturnFlag(false);
						// return baseRes;
					}
				} catch (IOException e1) {
					// baseRes.put("desc", "package file failed");
					// baseRes.setReturnFlag(false);
					// return baseRes;
				} catch (InterruptedException e1) {
					// baseRes.put("desc", "软件升级失败：文件解压失败");
					// baseRes.setReturnFlag(false);
					// return baseRes;
				}
				
				//copy the converted mobi file to original path
				//from "orig.mobi/azw3" to "orig_converted.mobi"
				File FinalJointedEbook = new File(origEbook.getParent()
						+ "\\"+origEbook.getName().split("\\.")[0]+"_revised.mobi");
				File ConvertedEbook = new File(opfFile.getParent()+"\\content.mobi");
				try {
					FileOp.nioTransferCopy(ConvertedEbook, FinalJointedEbook);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnConvert.setBounds(94, 26, 90, 30);
		btnConvert.setText("转换");
		
		Label lblmobiazwazwmobi = new Label(composite, SWT.NONE);
		lblmobiazwazwmobi.setBounds(10, 0, 308, 20);
		lblmobiazwazwmobi.setText("将mobi/azw3变成可以推送成azw3的mobi");
		
		CTabItem tbtmAddNoteLink = new CTabItem(tabFolder, SWT.NONE);
		tbtmAddNoteLink.setText("生成脚注链接");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmAddNoteLink.setControl(composite_1);
		
				Button btnAddnotelink = new Button(composite_1, SWT.NONE);
				btnAddnotelink.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
					}
				});
				btnAddnotelink.setBounds(70, 37, 101, 30);
				btnAddnotelink.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						//1. 判断是epub/mobi/azw3
						FileSystemOp FileOp = new FileSystemOp();
						File origEbook = new File(textFilePath.getText());
						
						String tempFolder = origEbook.getParent()+ "\\temp";
						File tempFolderFile = new File(tempFolder);
						if (!tempFolderFile.mkdirs()) {
							System.out.println("创建目标文件所在目录失败！");
						}
						
						String origName = origEbook.getName();
						//1.1 set a string variable for the prefix
						String origPrefix = "";
						
						//get the prefix
						if ((origName != null) && (origName.length() > 0)) {   
				            int dot = origName.lastIndexOf('.');   
				            if ((dot >-1) && (dot < (origName.length() - 1))) {   
				            	 origPrefix = origName.substring(dot + 1);   
				            }    
				        }				
						else origPrefix = origName;
						
						//set the path of html files
						String htmlPath = "";
						
						//if the file is epub file, unzip the file to temp folder
						if(origPrefix.equals("epub")){
							Unzip unzip = new Unzip();
							boolean unzipSuccessful = unzip.unzip(origEbook.getAbsolutePath(), origEbook.getParent()+ "\\temp\\");
				               if (unzipSuccessful) {
				                    System.out.println("文件解压成功。");
				                    
				                    //set the html file path
				                    htmlPath = origEbook.getParent()+ "\\temp\\OPS\\";
				                    System.out.println("文件解压失败。");
				               }
						}
						//if the file is mobi or azw3, unzip it by using KindleUnpack
						else if(origPrefix.equals("mobi")||origPrefix.equals("azw3")){
							//get the file name extension
							String[] GetFileExt = textFilePath.getText().split("\\.");
							String FileExt = GetFileExt[GetFileExt.length-1];

							File newEbook = new File(origEbook.getParent()
									+ "\\temp\\temp."
									+ FileExt);
							try {
								FileOp.nioTransferCopy(origEbook, newEbook);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							// use kindleUnpack to unpackage the temp.mobi/azw3
							String shellExec = "res//KindleUnpack.exe " + "-r" +" "
									+"\""+newEbook.getAbsolutePath() +"\""+ " "
									+"\""+ newEbook.getParent()+"\"";
							//String shellExec = "\"C:\\Users\\Zhirun Tian\\Documents\\GitHub\\AddNote\\res\\KindleUnpack.exe\" -r  \"C:\\Users\\Zhirun Tian\\Desktop\\temp\\temp.mobi\" \"C:\\Users\\Zhirun Tian\\Desktop\\temp\"";
							try {
								Process p0 = Runtime.getRuntime().exec(shellExec);
								// 读取标准输出流
								BufferedReader bufferedReader = new BufferedReader(
										new InputStreamReader(p0.getInputStream()));
								String line;
								while ((line = bufferedReader.readLine()) != null) {
									// System.out.println(line);
									//textLog.setText(textLog.getText() + "\r\n" + line);
									textLog.setText(textLog.getText() + line + "\r\n");
								}
								// 读取标准错误流
								BufferedReader brError = new BufferedReader(
										new InputStreamReader(p0.getErrorStream(), "gb2312"));
								String errline = null;
								while ((errline = brError.readLine()) != null) {
									System.out.println(errline);
								}
								// waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
								int c = p0.waitFor();
								if (c != 0) {
									System.out.println("失败！");
								}
							} catch (IOException e1) {
								System.out.println("失败！");	
							} catch (InterruptedException e1) {
								System.out.println("失败！");
							}
							
							//set the html file path
							htmlPath = newEbook.getParent()+"\\mobi8\\OEBPS\\";
							File htmlFoldier = new File(htmlPath);
							if(!htmlFoldier.exists()){
								htmlFoldier = new File(newEbook.getParent()+"\\mobi7\\OEBPS\\");
								htmlPath = newEbook.getParent()+"\\mobi7\\OEBPS\\";
							}
							
							if(!htmlFoldier.exists()){
								htmlFoldier = new File(newEbook.getParent()+"\\mobi7\\content.opf");
								htmlPath = newEbook.getParent()+"\\mobi7\\";
							}
						}
						//part below is for read the html file list from html path
						GetFileList fileObj = new GetFileList();
						List<File> FileList= new ArrayList<File>();
						FileList = fileObj.traverseFolder2(htmlPath);
						
						List<File> htmlFileList= new ArrayList<File>();
						for(File htmlFile: FileList){
							if(htmlFile.getName().endsWith("html")){
								//make the htmlFileList only have htmlfile, but the class now is not used
								//this class is used for future 封装
								htmlFileList.add(htmlFile);
								System.out.println(htmlFile.getName());
								
								//add note link to the html files
//								FileModifyLinkSquareBracket modObj = new FileModifyLinkSquareBracket(htmlFile);
//								String tmp =  modObj.read();
//								modObj.write(tmp);
							}
						}
						
						//add note link to the html files
						String regex = "【|】";
						FileModifyLinkSquareBracket modObj = new FileModifyLinkSquareBracket(htmlFileList,regex);
						String tmp =  modObj.read();
						modObj.write(tmp);
						
						
						
					}
				});
				btnAddnotelink.setText("AddNoteLink");
				
				CTabItem tbtmConvertBtHV = new CTabItem(tabFolder, SWT.NONE);
				tbtmConvertBtHV.setText("横排直排互转");
				
				Composite composite_2 = new Composite(tabFolder, SWT.NONE);
				tbtmConvertBtHV.setControl(composite_2);
				
				Button btnHtoV = new Button(composite_2, SWT.NONE);
				btnHtoV.setBounds(61, 25, 101, 30);
				btnHtoV.setText("横排转直排");
				
				Button btnVtoH = new Button(composite_2, SWT.NONE);
				btnVtoH.setBounds(61, 67, 101, 30);
				btnVtoH.setText("直排转横排");
				
				CTabItem tbtmUnpack = new CTabItem(tabFolder, SWT.NONE);
				tbtmUnpack.setText("解包");
				
				Composite composite_3 = new Composite(tabFolder, SWT.NONE);
				tbtmUnpack.setControl(composite_3);
				
						Button btnUnpackEbook = new Button(composite_3, SWT.NONE);
						btnUnpackEbook.setBounds(98, 39, 101, 30);
						btnUnpackEbook.setText("Unpack Ebook");
						
						Label lblAboutAuthor = new Label(shlEbookTool, SWT.NONE);
						lblAboutAuthor.setBounds(33, 594, 426, 47);
						lblAboutAuthor.setText("About author:  please contact me by admin@si-you.com\r\nVersion 0.16  Homepage: https://si-you.com/?page_id=2586\r\n");

		shlEbookTool.open();
		shlEbookTool.layout();
		while (!shlEbookTool.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlEbookTool = new Shell();
		shlEbookTool.setSize(516, 698);
		shlEbookTool.setText("Ebook Toolkit");

		Button btnSelectButton = new Button(shlEbookTool, SWT.NONE);
		btnSelectButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog dialogOpen = new FileDialog(shlEbookTool, SWT.OPEN);
				dialogOpen.setFilterExtensions(new String[] { "*.azw3",
						"*.mobi", "*.epub" });

				String sys = System.getProperty("user.home");
				dialogOpen.setFilterPath(sys + "\\Desktop");

				String result = dialogOpen.open();
				textFilePath.setText(result);

			}
		});
		btnSelectButton.setBounds(358, 114, 101, 30);
		btnSelectButton.setText("Select File");

		textFilePath = new Text(shlEbookTool, SWT.BORDER);
		textFilePath.setBounds(34, 114, 314, 30);

		Label lblNewLabel = new Label(shlEbookTool, SWT.NONE);
		lblNewLabel.setBounds(34, 69, 278, 20);
		lblNewLabel.setText("Select the mobi/azw3/epub file to modify");

		lblPleaseNoteOnly = new Label(shlEbookTool, SWT.NONE);
		lblPleaseNoteOnly.setBounds(34, 10, 278, 20);
		lblPleaseNoteOnly
				.setText("Please note: only work with little ebooks");

		textLog = new Text(shlEbookTool, SWT.BORDER);
		textLog.setEditable(false);
		textLog.setBounds(34, 351, 425, 227);

	}
}
