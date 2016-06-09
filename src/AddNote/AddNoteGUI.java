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

public class AddNoteGUI {

	protected Shell shlAddnote;
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
		shlAddnote.setImage(image);

		shlAddnote.open();
		shlAddnote.layout();
		while (!shlAddnote.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlAddnote = new Shell();
		shlAddnote.setSize(593, 699);
		shlAddnote.setText("AddNote");

		Button btnSelectButton = new Button(shlAddnote, SWT.NONE);
		btnSelectButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog dialogOpen = new FileDialog(shlAddnote, SWT.OPEN);
				dialogOpen.setFilterExtensions(new String[] { "*.mobi",
						"*.azw3", "*.epub" });

				String sys = System.getProperty("user.home");
				dialogOpen.setFilterPath(sys + "\\Desktop");

				String result = dialogOpen.open();
				textFilePath.setText(result);

			}
		});
		btnSelectButton.setBounds(358, 114, 79, 30);
		btnSelectButton.setText("Select File");

		textFilePath = new Text(shlAddnote, SWT.BORDER);
		textFilePath.setBounds(34, 114, 314, 30);

		Label lblNewLabel = new Label(shlAddnote, SWT.NONE);
		lblNewLabel.setBounds(34, 69, 278, 20);
		lblNewLabel.setText("Select the mobi/azw3/epub file to modify");

		lblPleaseNoteOnly = new Label(shlAddnote, SWT.NONE);
		lblPleaseNoteOnly.setBounds(10, 10, 383, 20);
		lblPleaseNoteOnly
				.setText("Please note: only work with CUHKseries ebook in Amazon.");

		Button btnUnpackEbook = new Button(shlAddnote, SWT.NONE);
		btnUnpackEbook.setBounds(443, 114, 107, 30);
		btnUnpackEbook.setText("Unpack Ebook");

		textLog = new Text(shlAddnote, SWT.BORDER);
		textLog.setEditable(false);
		textLog.setBounds(34, 383, 491, 259);

		Button btnAddnotelink = new Button(shlAddnote, SWT.NONE);
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

				// String tempPath =
				// "C:\\Users\\Zhirun Tian\\Desktop\\CUHK Series\\zdx2\\mobi8\\OEBPS\\Text";
				// //textFilePath.getText().split(".")[0]+"\\mobi8\\OEBPS\\Text";
				// String DictionaryPath = tempPath;
				 GetFileList getfl = new GetFileList(newEbook.getParent());
				 List<File> FileList = getfl.traverseFolder3(newEbook.getParent());
				
				
				 for(File tmp: FileList){
				 //System.out.println(tmp.getName());
				 FileModifyLinkSquareBracket tempFile = new
				 FileModifyLinkSquareBracket(tmp);
				
				 tempFile.write(tempFile.read());
				 }
			}
		});
		btnAddnotelink.setBounds(34, 186, 105, 30);
		btnAddnotelink.setText("AddNoteLink");

	}
}
