package AddNote;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

				File newEbook = new File(origEbook.getParent()
						+ "\\temp\\temp."
						+ textFilePath.getText().split("\\.")[1]);
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
				File ConvertedEbook = new File(newEbook.getParent()+"\\mobi8\\OEBPS\\content.mobi");
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
				btnAddnotelink.setBounds(73, 55, 101, 30);
				btnAddnotelink.addMouseListener(new MouseAdapter() {
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

						File newEbook = new File(origEbook.getParent()
								+ "\\temp\\temp."
								+ textFilePath.getText().split("\\.")[1]);
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
								textLog.setText(textLog.getText() + "\r\n" + line);
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
							// baseRes.put("desc", "软件升级失败：文件解压失败");
							// baseRes.setReturnFlag(false);
							// return baseRes;
						} catch (InterruptedException e1) {
							// baseRes.put("desc", "软件升级失败：文件解压失败");
							// baseRes.setReturnFlag(false);
							// return baseRes;
						}
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
						lblAboutAuthor.setBounds(33, 594, 426, 20);
						lblAboutAuthor.setText("About author:  please contact me by admin@si-you.com");

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
		shlEbookTool.setText("EbookToolkit");

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
