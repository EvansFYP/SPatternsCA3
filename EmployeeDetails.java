
/* * 
 * This is a menu driven system that will allow users to define a data structure representing a collection of 
 * records that can be displayed both by means of a dialog that can be scrolled through and by means of a table
 * to give an overall view of the collection contents.
 * 
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class EmployeeDetails extends JFrame implements ActionListener, ItemListener, DocumentListener, WindowListener {
	
	private CommandInvoker commandInvoker;
	
	public EmployeeDetails() {
        // Initialize the command invoker
        commandInvoker = new CommandInvoker(this);

        // Set commands for each button
        commandInvoker.setOpenCommand(new OpenFileCommand(this));
        commandInvoker.setSaveCommand(new SaveFileCommand(this));
        commandInvoker.setSaveAsCommand(new SaveFileCommand(this));
        commandInvoker.setCreateCommand(new CreateRecordCommand(this));
        commandInvoker.setEditCommand(new EditRecordCommand(this));
        commandInvoker.setDeleteCommand(new DeleteRecordCommand(this));
        commandInvoker.setFirstCommand(new FirstRecordCommand(this));
        commandInvoker.setPrevCommand(new PreviousRecordCommand(this));
        commandInvoker.setNextCommand(new NextRecordCommand(this));
        commandInvoker.setLastCommand(new LastRecordCommand(this));
        commandInvoker.setDisplayAllCommand(new DisplayAllRecordsCommand(this));
        commandInvoker.setSearchByIdCommand(new SearchByIdCommand(this));
        commandInvoker.setSearchBySurnameCommand(new SearchBySurnameCommand(this));
        commandInvoker.setCancelChangeCommand(new CancelChangeCommand(this));
        
       
    }
	// decimal format for inactive currency text field
	private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");
	// decimal format for active currency text field
	private static final DecimalFormat fieldFormat = new DecimalFormat("0.00");
	// hold object start position in file
	private long currentByteStart = 0;
	private RandomFile application = new RandomFile();
	// display files in File Chooser only with extension .dat
	private FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");
	// hold file name and path for current file in use
	private File file;
	// holds true or false if any changes are made for text fields
	private boolean change = false;
	// holds true or false if any changes are made for file content
	boolean changesMade = false;
	private JMenuItem open, save, saveAs, create, modify, delete, firstItem, lastItem, nextItem, prevItem, searchById,
			searchBySurname, listAll, closeApp;
	private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname,
			saveChange, cancelChange;
	private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
	private static EmployeeDetails frame = new EmployeeDetails();
	// font for labels, text fields and combo boxes
	Font font1 = new Font("SansSerif", Font.BOLD, 16);
	// holds automatically generated file name
	String generatedFileName;
	// holds current Employee object
	Employee currentEmployee;
	JTextField searchByIdField, searchBySurnameField;
	// gender combo box values
	String[] gender = { "", "M", "F" };
	// department combo box values
	String[] department = { "", "Administration", "Production", "Transport", "Management" };
	// full time combo box values
	String[] fullTime = { "", "Yes", "No" };

	// initialize menu bar
	private JMenuBar menuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu, recordMenu, navigateMenu, closeMenu;

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		recordMenu = new JMenu("Records");
		recordMenu.setMnemonic(KeyEvent.VK_R);
		navigateMenu = new JMenu("Navigate");
		navigateMenu.setMnemonic(KeyEvent.VK_N);
		closeMenu = new JMenu("Exit");
		closeMenu.setMnemonic(KeyEvent.VK_E);

		menuBar.add(fileMenu);
		menuBar.add(recordMenu);
		menuBar.add(navigateMenu);
		menuBar.add(closeMenu);

		fileMenu.add(open = new JMenuItem("Open")).addActionListener(this);
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(save = new JMenuItem("Save")).addActionListener(this);
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(saveAs = new JMenuItem("Save As")).addActionListener(this);
		saveAs.setMnemonic(KeyEvent.VK_F2);
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));

		recordMenu.add(create = new JMenuItem("Create new Record")).addActionListener(this);
		create.setMnemonic(KeyEvent.VK_N);
		create.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		recordMenu.add(modify = new JMenuItem("Modify Record")).addActionListener(this);
		modify.setMnemonic(KeyEvent.VK_E);
		modify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		recordMenu.add(delete = new JMenuItem("Delete Record")).addActionListener(this);

		navigateMenu.add(firstItem = new JMenuItem("First"));
		firstItem.addActionListener(this);
		navigateMenu.add(prevItem = new JMenuItem("Previous"));
		prevItem.addActionListener(this);
		navigateMenu.add(nextItem = new JMenuItem("Next"));
		nextItem.addActionListener(this);
		navigateMenu.add(lastItem = new JMenuItem("Last"));
		lastItem.addActionListener(this);
		navigateMenu.addSeparator();
		navigateMenu.add(searchById = new JMenuItem("Search by ID")).addActionListener(this);
		navigateMenu.add(searchBySurname = new JMenuItem("Search by Surname")).addActionListener(this);
		navigateMenu.add(listAll = new JMenuItem("List all Records")).addActionListener(this);

		closeMenu.add(closeApp = new JMenuItem("Close")).addActionListener(this);
		closeApp.setMnemonic(KeyEvent.VK_F4);
		closeApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));

		return menuBar;
	}// end menuBar

	// initialize search panel
	private JPanel searchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout());

		searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		searchPanel.add(new JLabel("Search by ID:"), "growx, pushx");
		searchPanel.add(searchByIdField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchByIdField.addActionListener(this);
		searchByIdField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(searchId = new JButton("Go"),
				"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchId.addActionListener(this);
		searchId.setToolTipText("Search Employee By ID");

		searchPanel.add(new JLabel("Search by Surname:"), "growx, pushx");
		searchPanel.add(searchBySurnameField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchBySurnameField.addActionListener(this);
		searchBySurnameField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(
				searchSurname = new JButton("Go"),"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchSurname.addActionListener(this);
		searchSurname.setToolTipText("Search Employee By Surname");

		return searchPanel;
	}// end searchPanel

	// initialize navigation panel
	private JPanel navigPanel() {
		JPanel navigPanel = new JPanel();

		navigPanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
		navigPanel.add(first = new JButton(new ImageIcon(
				new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		first.setPreferredSize(new Dimension(17, 17));
		first.addActionListener(this);
		first.setToolTipText("Display first Record");

		navigPanel.add(previous = new JButton(new ImageIcon(new ImageIcon("prev.png").getImage()
				.getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		previous.setPreferredSize(new Dimension(17, 17));
		previous.addActionListener(this);
		previous.setToolTipText("Display next Record");

		navigPanel.add(next = new JButton(new ImageIcon(
				new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		next.setPreferredSize(new Dimension(17, 17));
		next.addActionListener(this);
		next.setToolTipText("Display previous Record");

		navigPanel.add(last = new JButton(new ImageIcon(
				new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		last.setPreferredSize(new Dimension(17, 17));
		last.addActionListener(this);
		last.setToolTipText("Display last Record");

		return navigPanel;
	}// end naviPanel

	private JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(add = new JButton("Add Record"), "growx, pushx");
		add.addActionListener(this);
		add.setToolTipText("Add new Employee Record");
		buttonPanel.add(edit = new JButton("Edit Record"), "growx, pushx");
		edit.addActionListener(this);
		edit.setToolTipText("Edit current Employee");
		buttonPanel.add(deleteButton = new JButton("Delete Record"), "growx, pushx, wrap");
		deleteButton.addActionListener(this);
		deleteButton.setToolTipText("Delete current Employee");
		buttonPanel.add(displayAll = new JButton("List all Records"), "growx, pushx");
		displayAll.addActionListener(this);
		displayAll.setToolTipText("List all Registered Employees");

		return buttonPanel;
	}

	// initialize main/details panel
	private JPanel detailsPanel() {
		JPanel empDetails = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();
		JTextField field;

		empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		empDetails.add(new JLabel("ID:"), "growx, pushx");
		empDetails.add(idField = new JTextField(20), "growx, pushx, wrap");
		idField.setEditable(false);

		empDetails.add(new JLabel("PPS Number:"), "growx, pushx");
		empDetails.add(ppsField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Surname:"), "growx, pushx");
		empDetails.add(surnameField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("First Name:"), "growx, pushx");
		empDetails.add(firstNameField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Gender:"), "growx, pushx");
		empDetails.add(genderCombo = new JComboBox<String>(gender), "growx, pushx, wrap");

		empDetails.add(new JLabel("Department:"), "growx, pushx");
		empDetails.add(departmentCombo = new JComboBox<String>(department), "growx, pushx, wrap");

		empDetails.add(new JLabel("Salary:"), "growx, pushx");
		empDetails.add(salaryField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Full Time:"), "growx, pushx");
		empDetails.add(fullTimeCombo = new JComboBox<String>(fullTime), "growx, pushx, wrap");

		buttonPanel.add(saveChange = new JButton("Save"));
		saveChange.addActionListener(this);
		saveChange.setVisible(false);
		saveChange.setToolTipText("Save changes");
		buttonPanel.add(cancelChange = new JButton("Cancel"));
		cancelChange.addActionListener(this);
		cancelChange.setVisible(false);
		cancelChange.setToolTipText("Cancel edit");

		empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");

		// loop through panel components and add listeners and format
		for (int i = 0; i < empDetails.getComponentCount(); i++) {
			empDetails.getComponent(i).setFont(font1);
			if (empDetails.getComponent(i) instanceof JTextField) {
				field = (JTextField) empDetails.getComponent(i);
				field.setEditable(false);
				if (field == ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
					field.setDocument(new JTextFieldLimit(20));
				field.getDocument().addDocumentListener(this);
			} // end if
			else if (empDetails.getComponent(i) instanceof JComboBox) {
				empDetails.getComponent(i).setBackground(Color.WHITE);
				empDetails.getComponent(i).setEnabled(false);
				((JComboBox<String>) empDetails.getComponent(i)).addItemListener(this);
				((JComboBox<String>) empDetails.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
					// set foregroung to combo boxes
					public void paint(Graphics g) {
						setForeground(new Color(65, 65, 65));
						super.paint(g);
					}// end paint
				});
			} // end else if
		} // end for
		return empDetails;
	}// end detailsPanel

	// display current Employee details
	public void displayRecords(Employee thisEmployee) {
	    // If Employee is null or ID is 0, do nothing
	    if (thisEmployee == null || thisEmployee.getEmployeeId() == 0) {
	        return;
	    }

	    // Clear search fields
	    searchByIdField.setText("");
	    searchBySurnameField.setText("");

	    // Update fields with employee data
	    idField.setText(Integer.toString(thisEmployee.getEmployeeId()));
	    ppsField.setText(thisEmployee.getPps().trim());
	    surnameField.setText(thisEmployee.getSurname().trim());
	    firstNameField.setText(thisEmployee.getFirstName());
	    genderCombo.setSelectedIndex(findIndexInArray(gender, Character.toString(thisEmployee.getGender())));
	    departmentCombo.setSelectedIndex(findIndexInArray(department, thisEmployee.getDepartment().trim()));
	    salaryField.setText(format.format(thisEmployee.getSalary()));
	    fullTimeCombo.setSelectedIndex(thisEmployee.getFullTime() ? 1 : 2);

	    change = false;
	}

	private int findIndexInArray(String[] array, String value) {
	    boolean found = false;
	    int index = 0;
	    while (!found && index < array.length - 1) {
	        if (value.equalsIgnoreCase(array[index])) {
	            found = true;
	        } else {
	            index++;
	        }
	    }

	    return found ? index : -1; // Return -1 if the value is not found
	}
	
	// display Employee summary dialog
	public void displayEmployeeSummaryDialog() {
		// display Employee summary dialog if these is someone to display
		if (isSomeoneToDisplay())
			new EmployeeSummaryDialog(getAllEmloyees());
	}

	// display search by ID dialog
	public void displaySearchByIdDialog() {
		if (isSomeoneToDisplay())
			new SearchByIdDialog(EmployeeDetails.this);
	}

	// display search by surname dialog
	public void displaySearchBySurnameDialog() {
		if (isSomeoneToDisplay())
			new SearchBySurnameDialog(EmployeeDetails.this);
	}

	public void firstRecord() {
	    if (isSomeoneToDisplay()) {
	        currentEmployee = readRecord(application.getFirst());

	        // If the first record is invalid, look for the next valid record
	        if (currentEmployee == null || currentEmployee.getEmployeeId() == 0) {
	            nextRecord();
	        } else {
	            displayRecords(currentEmployee);
	        }
	    }
	}

	public void previousRecord() {
	    if (isSomeoneToDisplay()) {
	        currentEmployee = readPreviousOrNextRecord(true);

	        // If the previous record is invalid, do nothing
	        if (currentEmployee == null || currentEmployee.getEmployeeId() == 0) {
	            return;
	        }

	        displayRecords(currentEmployee);
	    }
	}
	
	public void nextRecord() {
	    if (isSomeoneToDisplay()) {
	        currentEmployee = readPreviousOrNextRecord(false);

	        // If the next record is invalid, do nothing
	        if (currentEmployee == null || currentEmployee.getEmployeeId() == 0) {
	            return;
	        }

	        displayRecords(currentEmployee);
	    }
	}

	public void lastRecord() {
	    if (isSomeoneToDisplay()) {
	        currentEmployee = readRecord(application.getLast());

	        // If the last record is invalid, do nothing
	        if (currentEmployee == null || currentEmployee.getEmployeeId() == 0) {
	            return;
	        }

	        displayRecords(currentEmployee);
	    }
	}

	public Employee readRecord(long byteStart) {
	    application.openReadFile(file.getAbsolutePath());
	    try {
	        currentByteStart = byteStart;
	        Employee employee = application.readRecords(currentByteStart);

	        // If the employee is invalid, return null
	        if (employee == null || employee.getEmployeeId() == 0) {
	            return null;
	        }

	        return employee;
	    } finally {
	        application.closeReadFile();
	    }
	}

	public Employee readPreviousOrNextRecord(boolean isPrevious) {
	    application.openReadFile(file.getAbsolutePath());
	    try {
	        do {
	            currentByteStart = isPrevious ? application.getPrevious(currentByteStart) : application.getNext(currentByteStart);
	            currentEmployee = application.readRecords(currentByteStart);
	        } while (currentEmployee != null && currentEmployee.getEmployeeId() == 0);

	        // If no valid employee is found, return null
	        if (currentEmployee == null || currentEmployee.getEmployeeId() == 0) {
	            return null;
	        }

	        return currentEmployee;
	    } finally {
	        application.closeReadFile();
	    }
	}
	
	public void searchEmployeeById() {
		boolean found = false;

		try {// try to read correct correct from input
				// if any active Employee record search for ID else do nothing
			if (isSomeoneToDisplay()) {
				firstRecord();// look for first record
				int firstId = currentEmployee.getEmployeeId();
				// if ID to search is already displayed do nothing else loop
				// through records
				if (searchByIdField.getText().trim().equals(idField.getText().trim()))
					found = true;
				else if (searchByIdField.getText().trim().equals(Integer.toString(currentEmployee.getEmployeeId()))) {
					found = true;
					displayRecords(currentEmployee);
				} // end else if
				else {
					nextRecord();// look for next record
					// loop until Employee found or until all Employees have
					// been checked
					while (firstId != currentEmployee.getEmployeeId()) {
						// if found break from loop and display Employee details
						// else look for next record
						if (Integer.parseInt(searchByIdField.getText().trim()) == currentEmployee.getEmployeeId()) {
							found = true;
							displayRecords(currentEmployee);
							break;
						} else
							nextRecord();// look for next record
					} // end while
				} // end else
					// if Employee not found display message
				if (!found)
					JOptionPane.showMessageDialog(null, "Employee not found!");
			} // end if
		} // end try
		catch (NumberFormatException e) {
			searchByIdField.setBackground(new Color(255, 150, 150));
			JOptionPane.showMessageDialog(null, "Wrong ID format!");
		} // end catch
		searchByIdField.setBackground(Color.WHITE);
		searchByIdField.setText("");
	}// end searchEmployeeByID

	// search Employee by surname
	public void searchEmployeeBySurname() {
		boolean found = false;
		// if any active Employee record search for ID else do nothing
		if (isSomeoneToDisplay()) {
			firstRecord();// look for first record
			String firstSurname = currentEmployee.getSurname().trim();
			// if ID to search is already displayed do nothing else loop through
			// records
			if (searchBySurnameField.getText().trim().equalsIgnoreCase(surnameField.getText().trim()))
				found = true;
			else if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
				found = true;
				displayRecords(currentEmployee);
			} // end else if
			else {
				nextRecord();// look for next record
				// loop until Employee found or until all Employees have been
				// checked
				while (!firstSurname.trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
					// if found break from loop and display Employee details
					// else look for next record
					if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
						found = true;
						displayRecords(currentEmployee);
						break;
					} // end if
					else
						nextRecord();// look for next record
				} // end while
			} // end else
				// if Employee not found display message
			if (!found)
				JOptionPane.showMessageDialog(null, "Employee not found!");
		} // end if
		searchBySurnameField.setText("");
	}// end searchEmployeeBySurname
	// get next free ID from Employees in the file
	
	
	public int getNextFreeId() {
		int nextFreeId = 0;
		// if file is empty or all records are empty start with ID 1 else look
		// for last active record
		if (file.length() == 0 || !isSomeoneToDisplay())
			nextFreeId++;
		else {
			lastRecord();// look for last active record
			// add 1 to last active records ID to get next ID
			nextFreeId = currentEmployee.getEmployeeId() + 1;
		}
		return nextFreeId;
	}// end getNextFreeId

	// get values from text fields and create Employee object
	public Employee getChangedDetails() {
		boolean fullTime = false;
		Employee theEmployee;
		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;

		theEmployee = new Employee(Integer.parseInt(idField.getText()), ppsField.getText().toUpperCase(),
				surnameField.getText().toUpperCase(), firstNameField.getText().toUpperCase(),
				genderCombo.getSelectedItem().toString().charAt(0), departmentCombo.getSelectedItem().toString(),
				Double.parseDouble(salaryField.getText()), fullTime);

		return theEmployee;
	}// end getChangedDetails

	// add Employee object to fail
	public void addRecord(Employee newEmployee) {
		// open file for writing
		application.openWriteFile(file.getAbsolutePath());
		// write into a file
		currentByteStart = application.addRecords(newEmployee);
		application.closeWriteFile();// close file for writing
	}// end addRecord

	// delete (make inactive - empty) record from file
	public void deleteRecord() {
		if (isSomeoneToDisplay()) {// if any active record in file display
									// message and delete record
			int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to delete record?", "Delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			// if answer yes delete (make inactive - empty) record
			if (returnVal == JOptionPane.YES_OPTION) {
				
				application.openWriteFile(file.getAbsolutePath());
				
		
				application.deleteRecords(currentByteStart);
				
				application.closeWriteFile();
				// if any active record in file display next record
				if (isSomeoneToDisplay()) {
					nextRecord();
					displayRecords(currentEmployee);
				}
			} 
		} 
	}

	// Retrieve all employee details as a vector of vectors
	private Vector<Object> getAllEmloyees() {
	    Vector<Object> allEmployees = new Vector<>(); // Vector to hold all employee details
	    Vector<Object> empDetails; // Vector to hold details of a single employee
	    long byteStart = currentByteStart; // Save the current byte start position
	    int firstId;

	    firstRecord(); // Navigate to the first record
	    firstId = currentEmployee.getEmployeeId(); // Get the ID of the first employee

	    // Loop through all employees and add their details to the vector
	    do {
	        empDetails = new Vector<>();
	        empDetails.addElement(currentEmployee.getEmployeeId()); // Employee ID
	        empDetails.addElement(currentEmployee.getPps()); // PPS number
	        empDetails.addElement(currentEmployee.getSurname()); // Surname
	        empDetails.addElement(currentEmployee.getFirstName()); // First name
	        empDetails.addElement(currentEmployee.getGender()); // Gender
	        empDetails.addElement(currentEmployee.getDepartment()); // Department
	        empDetails.addElement(currentEmployee.getSalary()); // Salary
	        empDetails.addElement(currentEmployee.getFullTime()); // Full-time status

	        allEmployees.addElement(empDetails); // Add employee details to the main vector
	        nextRecord(); // Navigate to the next record
	    } while (firstId != currentEmployee.getEmployeeId()); // Stop when we loop back to the first employee

	    currentByteStart = byteStart; // Restore the original byte start position
	    return allEmployees; // Return the vector of all employee details
	}

	// Enable fields for editing employee details
	public void editDetails() {
	    if (isSomeoneToDisplay()) { // Check if there are records to display
	        salaryField.setText(fieldFormat.format(currentEmployee.getSalary())); // Format and display salary
	        change = false; // Reset the change flag
	        setEnabled(true); // Enable text fields for editing
	    }
	}

	// Cancel changes and disable editing fields
	public void cancelChange() {
	    setEnabled(false); // Disable text fields
	    displayRecords(currentEmployee); // Re-display the current employee record
	}

	// Check if there are any active records in the file (ID is not 0)
	public boolean isSomeoneToDisplay() {
	    boolean someoneToDisplay = false;

	    application.openReadFile(file.getAbsolutePath()); // Open the file for reading
	    someoneToDisplay = application.isSomeoneToDisplay(); // Check for active records
	    application.closeReadFile(); // Close the file after reading

	    if (!someoneToDisplay) { // If no active records are found
	        currentEmployee = null; // Clear the current employee
	        idField.setText(""); // Clear ID field
	        ppsField.setText(""); // Clear PPS field
	        surnameField.setText(""); // Clear surname field
	        firstNameField.setText(""); // Clear first name field
	        salaryField.setText(""); // Clear salary field
	        genderCombo.setSelectedIndex(0); // Reset gender combo box
	        departmentCombo.setSelectedIndex(0); // Reset department combo box
	        fullTimeCombo.setSelectedIndex(0); // Reset full-time combo box
	        JOptionPane.showMessageDialog(null, "No Employees registered!"); // Show message
	    }

	    return someoneToDisplay; // Return whether there are active records
	}

	// Check for correct PPS format and verify if PPS is already in use
	public boolean correctPps(String pps, long currentByte) {
	    if (!isValidPpsFormat(pps)) {
	        return true; // PPS format is invalid
	    }

	    return isPpsAlreadyInUse(pps, currentByte); // Check if PPS exists in the file
	}

	// Validate PPS format 
	private boolean isValidPpsFormat(String pps) {
	    if (pps.length() != 8 && pps.length() != 9) {
	        return false; // PPS length must be 8 or 9
	    }

	    // Check that the first 7 characters are digits and the last 1 or 2 are letters
	    for (int i = 0; i < 7; i++) {
	        if (!Character.isDigit(pps.charAt(i))) {
	            return false;
	        }
	    }

	    if (!Character.isLetter(pps.charAt(7))) {
	        return false;
	    }

	    if (pps.length() == 9 && !Character.isLetter(pps.charAt(8))) {
	        return false;
	    }

	    return true; // PPS format is valid
	}

	// Check if PPS is already in use in the file
	private boolean isPpsAlreadyInUse(String pps, long currentByte) {
	    application.openReadFile(file.getAbsolutePath()); // Open file for reading
	    boolean ppsExists = application.isPpsExist(pps, currentByte); // Check if PPS exists
	    application.closeReadFile(); // Close file after reading
	    return ppsExists;
	}

	// check if file name has extension .dat
	public boolean checkFileName(File fileName) {
		boolean checkFile = false;
		int length = fileName.toString().length();

		// check if last characters in file name is .dat
		if (fileName.toString().charAt(length - 4) == '.' && fileName.toString().charAt(length - 3) == 'd'
				&& fileName.toString().charAt(length - 2) == 'a' && fileName.toString().charAt(length - 1) == 't')
			checkFile = true;
		return checkFile;
	}// end checkFileName

	// check if any changes text field where made
	public boolean checkForChanges() {
		boolean anyChanges = false;
		// if changes where made, allow user to save there changes
		if (change) {
			saveChanges();// save changes
			anyChanges = true;
		} // end if
			// if no changes made, set text fields as unenabled and display
			// current Employee
		else {
			setEnabled(false);
			displayRecords(currentEmployee);
		} // end else

		return anyChanges;
	}// end checkForChanges

	// check for input in text fields
	public boolean checkInput() {
	    boolean valid = true;

	    // Validate PPS field
	    valid &= validateTextField(ppsField, "PPS cannot be empty");
	    valid &= validatePpsField(ppsField, currentByteStart);

	    // Validate surname and first name fields
	    valid &= validateTextField(surnameField, "Surname cannot be empty");
	    valid &= validateTextField(firstNameField, "First name cannot be empty");

	    // Validate combo boxes
	    valid &= validateComboBox(genderCombo, "Gender must be selected");
	    valid &= validateComboBox(departmentCombo, "Department must be selected");
	    valid &= validateComboBox(fullTimeCombo, "Full-time status must be selected");

	    // Validate salary field
	    valid &= validateSalaryField(salaryField);

	    // Display error message if any validation fails
	    if (!valid) {
	        JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
	    }

	    // Reset fields to white if editable
	    if (ppsField.isEditable()) {
	        setToWhite();
	    }

	    return valid;
	}

	// Validate a text field for empty input
	private boolean validateTextField(JTextField field, String errorMessage) {
	    if (field.isEditable() && field.getText().trim().isEmpty()) {
	        field.setBackground(new Color(255, 150, 150));
	        return false;
	    }
	    return true;
	}

	// Validate the PPS field
	private boolean validatePpsField(JTextField ppsField, long currentByteStart) {
	    if (ppsField.isEditable() && correctPps(ppsField.getText().trim(), currentByteStart)) {
	        ppsField.setBackground(new Color(255, 150, 150));
	        return false;
	    }
	    return true;
	}

	// Validate a combo box for selection
	private boolean validateComboBox(JComboBox<?> comboBox, String errorMessage) {
	    if (comboBox.isEnabled() && comboBox.getSelectedIndex() == 0) {
	        comboBox.setBackground(new Color(255, 150, 150));
	        return false;
	    }
	    return true;
	}

	// Validate the salary field
	private boolean validateSalaryField(JTextField salaryField) {
	    if (salaryField.isEditable()) {
	        try {
	            double salary = Double.parseDouble(salaryField.getText());
	            if (salary < 0) {
	                salaryField.setBackground(new Color(255, 150, 150));
	                return false;
	            }
	        } catch (NumberFormatException e) {
	            salaryField.setBackground(new Color(255, 150, 150));
	            return false;
	        }
	    }
	    return true;
	}

	// set text field background colour to white
	private void setToWhite() {
		ppsField.setBackground(UIManager.getColor("TextField.background"));
		surnameField.setBackground(UIManager.getColor("TextField.background"));
		firstNameField.setBackground(UIManager.getColor("TextField.background"));
		salaryField.setBackground(UIManager.getColor("TextField.background"));
		genderCombo.setBackground(UIManager.getColor("TextField.background"));
		departmentCombo.setBackground(UIManager.getColor("TextField.background"));
		fullTimeCombo.setBackground(UIManager.getColor("TextField.background"));
	}// end setToWhite

	// enable text fields for editing
	public void setEnabled(boolean booleanValue) {
		boolean search;
		if (booleanValue)
			search = false;
		else
			search = true;
		ppsField.setEditable(booleanValue);
		surnameField.setEditable(booleanValue);
		firstNameField.setEditable(booleanValue);
		genderCombo.setEnabled(booleanValue);
		departmentCombo.setEnabled(booleanValue);
		salaryField.setEditable(booleanValue);
		fullTimeCombo.setEnabled(booleanValue);
		saveChange.setVisible(booleanValue);
		cancelChange.setVisible(booleanValue);
		searchByIdField.setEnabled(search);
		searchBySurnameField.setEnabled(search);
		searchId.setEnabled(search);
		searchSurname.setEnabled(search);
	}// end setEnabled

	// open file
	private int showSaveDialog(String message) {
	    return JOptionPane.showOptionDialog(frame, message, "Save",
	            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
	}
	
	private void openFileForReading() {
	    application.openReadFile(file.getAbsolutePath());
	}

	private void closeFileForReading() {
	    application.closeReadFile();
	}

	private void openFileForWriting() {
	    application.openWriteFile(file.getAbsolutePath());
	}

	private void closeFileForWriting() {
	    application.closeWriteFile();
	}

	private void deleteGeneratedFile() {
	    if (file.getName().equals(generatedFileName)) {
	        file.delete();
	    }
	}
	
	public void openFile() {
	    JFileChooser fc = new JFileChooser();
	    fc.setDialogTitle("Open");
	    fc.setFileFilter(datfilter);

	    // Offer to save changes if the file is not empty or changes have been made
	    if (file.length() != 0 || change) {
	        int returnVal = showSaveDialog("Do you want to save changes?");
	        if (returnVal == JOptionPane.YES_OPTION) {
	            saveFile();
	        }
	    }

	    int returnVal = fc.showOpenDialog(EmployeeDetails.this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File newFile = fc.getSelectedFile();
	        deleteGeneratedFile(); // Delete the old generated file if necessary
	        file = newFile;

	        openFileForReading();
	        firstRecord();
	        displayRecords(currentEmployee);
	        closeFileForReading();
	    }
	}
	// save file
	public void saveFile() {
	    if (file.getName().equals(generatedFileName)) {
	        saveFileAs();
	    } else {
	        if (change) {
	            int returnVal = showSaveDialog("Do you want to save changes?");
	            if (returnVal == JOptionPane.YES_OPTION && !idField.getText().isEmpty()) {
	                openFileForWriting();
	                currentEmployee = getChangedDetails();
	                application.changeRecords(currentEmployee, currentByteStart);
	                closeFileForWriting();
	            }
	        }
	        displayRecords(currentEmployee);
	        setEnabled(false);
	    }
	}

	// save changes to current Employee
	public void saveChanges() {
	    int returnVal = showSaveDialog("Do you want to save changes to current Employee?");
	    if (returnVal == JOptionPane.YES_OPTION) {
	        openFileForWriting();
	        currentEmployee = getChangedDetails();
	        application.changeRecords(currentEmployee, currentByteStart);
	        closeFileForWriting();
	        changesMade = false;
	    }
	    displayRecords(currentEmployee);
	    setEnabled(false);
	}

	// save file as 'save as'
	public void saveFileAs() {
	    JFileChooser fc = new JFileChooser();
	    fc.setDialogTitle("Save As");
	    fc.setFileFilter(datfilter);
	    fc.setApproveButtonText("Save");
	    fc.setSelectedFile(new File("new_Employee.dat"));

	    int returnVal = fc.showSaveDialog(EmployeeDetails.this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File newFile = fc.getSelectedFile();
	        if (!checkFileName(newFile)) {
	            newFile = new File(newFile.getAbsolutePath() + ".dat");
	        }
	        application.createFile(newFile.getAbsolutePath());

	        try {
	            Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	            deleteGeneratedFile();
	            file = newFile;
	        } catch (IOException e) {
	            // Handle exception
	        }
	    }
	    changesMade = false;
	}

	// allow to save changes to file when exiting the application
	public void exitApp() {
	    if (file.length() != 0 && changesMade) {
	        int returnVal = showSaveDialog("Do you want to save changes?");
	        if (returnVal == JOptionPane.YES_OPTION) {
	            saveFile();
	            deleteGeneratedFile();
	            System.exit(0);
	        } else if (returnVal == JOptionPane.NO_OPTION) {
	            deleteGeneratedFile();
	            System.exit(0);
	        }
	    } else {
	        deleteGeneratedFile();
	        System.exit(0);
	    }
	}

	// generate 20 character long file name
	public String getFileName() {
		String fileNameChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
		StringBuilder fileName = new StringBuilder();
		Random rnd = new Random();
		// loop until 20 character long file name is generated
		while (fileName.length() < 20) {
			int index = (int) (rnd.nextFloat() * fileNameChars.length());
			fileName.append(fileNameChars.charAt(index));
		}
		String generatedfileName = fileName.toString();
		return generatedfileName;
	}// end getFileName

	// create file with generated file name when application is opened
	public void createRandomFile() {
		generatedFileName = getFileName() + ".dat";
		// assign generated file name to file
		file = new File(generatedFileName);
		// create file
		application.createFile(file.getName());
	}// end createRandomFile

	
	
	public Employee getCurrentEmployee() {
	    return currentEmployee;
	}
	

	


	

	
	@Override
	public void actionPerformed(ActionEvent e) {
	    // Handle the button clicks or other events
	    if (e.getSource() == closeApp) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeSave(); // Save before closing
	        
	    } else if (e.getSource() == open) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeOpen(); // Open file
	        
	    } else if (e.getSource() == save) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeSave();
	        // Save file
	        
	    } else if (e.getSource() == saveAs) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeSaveAs(); 
	        // Save file as
	        
	    } else if (e.getSource() == create || e.getSource() == add) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeCreate(); // Create new record
	        
	    } else if (e.getSource() == modify || e.getSource() == edit) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeEdit(); // Edit existing record
	        
	    } else if (e.getSource() == delete || e.getSource() == deleteButton) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeDelete(); // Delete record
	        
	    } else if (e.getSource() == firstItem || e.getSource() == first) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeFirst(); // Show first record
	        
	    } else if (e.getSource() == prevItem || e.getSource() == previous) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokePrev(); // Show previous record
	        
	    } else if (e.getSource() == nextItem || e.getSource() == next) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeNext(); // Show next record
	        
	    } else if (e.getSource() == lastItem || e.getSource() == last) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeLast(); // Show last record
	        
	    } else if (e.getSource() == listAll || e.getSource() == displayAll) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeDisplayAll(); // Display all records
	        
	    } else if (e.getSource() == searchById) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeSearchByIdDialog(); // Open search by ID dialog
	        
	    } else if (e.getSource() == searchBySurname) {
	        if (checkInput() && !checkForChanges()) 
	            commandInvoker.invokeSearchBySurnameDialog(); // Open search by surname dialog
	        
	    } else if (e.getSource() == searchId || e.getSource() == searchByIdField) {
	        commandInvoker.invokeSearchById(); // Perform search by ID directly
	        
	    } else if (e.getSource() == searchSurname || e.getSource() == searchBySurnameField) {
	        commandInvoker.invokeSearchBySurname(); // Perform search by surname directly
	        
	    } else if (e.getSource() == cancelChange) {
	        commandInvoker.invokeCancelChange(); // Cancel any changes
	    }
	}
	// content pane for main dialog
	private void createContentPane() {
		setTitle("Employee Details");
		createRandomFile();// create random file name
		JPanel dialog = new JPanel(new MigLayout());

		setJMenuBar(menuBar());// add menu bar to frame
		// add search panel to frame
		dialog.add(searchPanel(), "width 400:400:400, growx, pushx");
		// add navigation panel to frame
		dialog.add(navigPanel(), "width 150:150:150, wrap");
		// add button panel to frame
		dialog.add(buttonPanel(), "growx, pushx, span 2,wrap");
		// add details panel to frame
		dialog.add(detailsPanel(), "gap top 30, gap left 150, center");

		JScrollPane scrollPane = new JScrollPane(dialog);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		addWindowListener(this);
	}// end createContentPane

	// create and show main dialog
	private static void createAndShowGUI() {

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.createContentPane();// add content pane to frame
		frame.setSize(760, 600);
		frame.setLocation(250, 200);
		frame.setVisible(true);
	}// end createAndShowGUI

	// main method
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}// end main

	// DocumentListener methods
	public void changedUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	public void insertUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	public void removeUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	// ItemListener method
	public void itemStateChanged(ItemEvent e) {
		change = true;
	}

	// WindowsListener methods
	public void windowClosing(WindowEvent e) {
		// exit application
		exitApp();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}
}// end class EmployeeDetails
