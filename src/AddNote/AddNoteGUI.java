package AddNote;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class AddNoteGUI {

	protected Shell shlAddnoteonlyWork;
	private Text textFilePath;
	private Label lblPleaseNoteOnly;

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
		shlAddnoteonlyWork.open();
		shlAddnoteonlyWork.layout();
		while (!shlAddnoteonlyWork.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlAddnoteonlyWork = new Shell();
		shlAddnoteonlyWork.setSize(574, 416);
		shlAddnoteonlyWork.setText("AddNote");

		Button btnSelectButton = new Button(shlAddnoteonlyWork, SWT.NONE);
		btnSelectButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog dialogOpen = new FileDialog(shlAddnoteonlyWork, SWT.OPEN);
				dialogOpen.setFilterExtensions(new String[] { "*.mobi","*.azw3","*.epub" });
				//dialogOpen.

				dialogOpen.setFilterPath("c:\\");
				String result = dialogOpen.open();
				textFilePath.setText(result);

			}
		});
		btnSelectButton.setBounds(358, 114, 79, 30);
		btnSelectButton.setText("Select File");

		textFilePath = new Text(shlAddnoteonlyWork, SWT.BORDER);
		textFilePath.setBounds(34, 114, 314, 30);

		Label lblNewLabel = new Label(shlAddnoteonlyWork, SWT.NONE);
		lblNewLabel.setBounds(34, 69, 278, 20);
		lblNewLabel.setText("Select the mobi/azw3/epub file to modify");
		
		lblPleaseNoteOnly = new Label(shlAddnoteonlyWork, SWT.NONE);
		lblPleaseNoteOnly.setBounds(10, 10, 383, 20);
		lblPleaseNoteOnly.setText("Please note: only work with CUHKseries ebook in Amazon.");

	}
}
