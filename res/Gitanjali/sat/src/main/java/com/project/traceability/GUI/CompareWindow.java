package com.project.traceability.GUI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;

public class CompareWindow {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CompareWindow window = new CompareWindow();
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
		shell.setLayout(new GridLayout(2, false));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		List list_1 = new List(shell, SWT.BORDER);
		GridData gd_list_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_list_1.heightHint = 130;
		gd_list_1.widthHint = 206;
		list_1.setLayoutData(gd_list_1);
		
		List list = new List(shell, SWT.BORDER);
		GridData gd_list = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_list.heightHint = 131;
		gd_list.widthHint = 201;
		list.setLayoutData(gd_list);
		
		List list_2 = new List(shell, SWT.BORDER);
		GridData gd_list_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_list_2.widthHint = 419;
		list_2.setLayoutData(gd_list_2);

	}
}
