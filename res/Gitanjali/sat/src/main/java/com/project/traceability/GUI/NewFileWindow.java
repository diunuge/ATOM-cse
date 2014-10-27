package com.project.traceability.GUI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NewFileWindow {

	protected Shell shell;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			NewFileWindow window = new NewFileWindow();
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
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		Label lblFileName = new Label(shell, SWT.NONE);
		lblFileName.setBounds(35, 42, 67, 15);
		lblFileName.setText("File Name :");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(108, 36, 233, 21);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 FileDialog fileDialog=new FileDialog(shell,SWT.OPEN);				  
				 String localFilePath=fileDialog.open();
				 if (localFilePath != null) {
					    //return pull(sync,localFilePath,remoteFilePath);
				 }
			}
		});
		btnNewButton.setBounds(349, 32, 75, 25);
		btnNewButton.setText("Browse");
		
		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.setBounds(359, 227, 49, 25);
		btnSave.setText("Save");

	}

}
