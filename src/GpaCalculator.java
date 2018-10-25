
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class GpaCalculator {


	JFrame frame;
	JPanel inputPanel, coursePanel, currentPanel, futurePanel, pastPanel;
	//JScrollPane  currentCoursesScrollPane, pastCoursesScrollPane, futureCoursesScrollPane;
	ArrayList<Course> currentCourses, pastCourses, futureCourses;
	JButton addCourse, calculate, clear, add15;
	JLabel gpaLabel, courseLabel, creditLabel, instructionsLabel, calculatedGpa, display1, display2;
	JTextField gpaInput, courseInput, creditInput, targetGpaInput;
	JComboBox courseType;

	public GpaCalculator() {
		//Initizalizing the main frame.  Layout will be two seperate grid layouts on the frame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GPA Calculator");
		frame.setLayout(new GridLayout(2, 1));
		frame.setBounds(325, 150, 800, 600);

		//Initilizing generic instance variables
		//Specific ones setup when needed
		this.coursePanel = new JPanel();
		this.inputPanel = new JPanel();
		this.instructionsLabel = new JLabel();
		this.currentCourses = new ArrayList<Course>();
		this.pastCourses = new ArrayList<Course>();
		this.futureCourses = new ArrayList<Course>();

		//Setting up Buttons
		this.addCourse = new JButton();
		this.addCourse.setAlignmentX(SwingConstants.CENTER);
		this.addCourse.addActionListener(new AddCourse());
		this.addCourse.setText("Add Course");

		//used to clear the program
		this.clear = new JButton();
		this.clear.setAlignmentX(SwingConstants.CENTER);
		this.clear.addActionListener(new ClearCourses());
		this.clear.setText("Clear Courses");

		//Button used to calculate gpa
		this.calculate = new JButton();
		this.calculate.setText("Calculate !");
		this.calculate.addActionListener(new CalculateGpa());

		//adds 15 default credit hours to the current courses
		this.add15 = new JButton();
		this.add15.setText("Add 15 default credits");
		this.add15.setAlignmentX(SwingConstants.CENTER);
		this.add15.addActionListener(new Add15());


		//Setting up labels
		this.courseLabel = new JLabel("Course Name", SwingConstants.CENTER);
		this.gpaLabel = new JLabel("GPA", SwingConstants.CENTER);
		this.creditLabel = new JLabel("Credits", SwingConstants.CENTER);
		this.calculatedGpa = new JLabel("0.0", SwingConstants.CENTER);
		this.display1 = new JLabel("");
		this.display2 = new JLabel("");

		//Setting up Text Fields
		this.gpaInput = new JTextField(SwingConstants.CENTER);
		this.courseInput = new JTextField(SwingConstants.CENTER);
		this.creditInput = new JTextField(SwingConstants.CENTER);
		this.targetGpaInput = new JTextField(SwingConstants.CENTER);

		//Setting up Type ComboBox
		String [] types = { "Past", "Current", "Future"};
		courseType = new JComboBox(types);
		//coursType.addActionListener()

		//Setting up input Panel
		//This panel will be a grid layout 5 x 4 with the text boxes and labels for
		//input, the output displays, and where the buttons will be
		inputPanel.setLayout(new GridLayout(5, 4));
		this.inputPanel.add(this.courseLabel);
		this.inputPanel.add(this.gpaLabel);
		this.inputPanel.add(this.creditLabel);
		this.inputPanel.add(this.clear);
		this.inputPanel.add(this.courseInput);
		this.inputPanel.add(this.gpaInput);
		this.inputPanel.add(this.creditInput);
		this.inputPanel.add(this.addCourse);
		this.inputPanel.add(new JLabel("Select Type", SwingConstants.RIGHT));
		this.inputPanel.add(courseType);
		this.inputPanel.add(add15);
		this.inputPanel.add(this.calculate);
		this.inputPanel.add(new JLabel("Enter Target GPA (optional)", SwingConstants.RIGHT));
		this.inputPanel.add(targetGpaInput);
		this.inputPanel.add(new JLabel("Current Calculated GPA :  ", SwingConstants.RIGHT));
		this.inputPanel.add(calculatedGpa);
		JLabel info = new JLabel("Information Window", SwingConstants.CENTER);
		this.inputPanel.add(info);
		this.inputPanel.add(display1);
		this.inputPanel.add(display2);
		Border border = BorderFactory.createTitledBorder("Enter course information, must input a GPA if course is a Past course");
		this.inputPanel.setBorder(border);


		//Courses window
		//This will be a 1 x 3 grid layout for past, current, and future courses
		//The panels to hold the labels were set to (0, 1) so the number of rows could
		//Increase dynamically.  These panels will hold courses as buttons in each category
		//so that when they are clicked they disappear. 
		Border b = BorderFactory.createTitledBorder("Course List, click a course to remove it from the list. GPA will show as -1 if left empty on entry");
		this.coursePanel.setLayout(new GridLayout(1, 3));
		this.coursePanel.setBorder(b);

		this.currentPanel = new JPanel(new GridLayout(0, 1));
		this.currentPanel.setBorder(BorderFactory.createTitledBorder("Current Courses"));

		this.pastPanel = new JPanel(new GridLayout(0, 1));
		this.pastPanel.setBorder(BorderFactory.createTitledBorder("Past Courses"));

		this.futurePanel = new JPanel(new GridLayout(0, 1));
		this.futurePanel.setBorder(BorderFactory.createTitledBorder("Future Courses"));

		coursePanel.add(futurePanel, SwingConstants.CENTER);
		coursePanel.add(currentPanel, SwingConstants.CENTER);
		coursePanel.add(pastPanel, SwingConstants.CENTER);


		frame.add(this.inputPanel);
		frame.add(this.coursePanel);

		frame.setVisible(true);


	}

	public static void main(String []args) {
		GpaCalculator calc = new GpaCalculator();
	}

	private class CalculateGpa implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			double credits = 0.0;
			double gradePoints = 0.0;
			double creditsWithNoGrade = 0.0;
			double gpa = 0.0;
			double targetGpa;
			boolean hasTargetGpa;

			//This will determine whether or not a target gpa has been input
			//and will assign the value appropriately if so.
			try {
				targetGpa = Double.parseDouble(targetGpaInput.getText());
				hasTargetGpa = true;
			}
			catch(Exception noTargetGpa) {
				hasTargetGpa = false;
				targetGpa = 0.0;
			}

			//Next three loops will add the credits for each course to creditsWithNoGrade
			//if the grade wasn't inputed, and will start totaling grade points if it has
			//been inputed.  Either way the credits will be added to the credits sum.
			for(Course c : currentCourses) {
				if(c.getGpa() == -1.0) {
					creditsWithNoGrade += c.getCredits();
				}
				else {
					gradePoints += c.getGpa()*c.getCredits();
				}
				credits += c.getCredits();
			}

			for(Course c : pastCourses) {
				if(c.getGpa() == -1.0) {
					creditsWithNoGrade += c.getCredits();
				}
				else {
					gradePoints += c.getGpa()*c.getCredits();
				}
				credits += c.getCredits();
			}
			for(Course c : futureCourses) {
				if(c.getGpa() == -1.0) {
					creditsWithNoGrade += c.getCredits();
				}
				else {
					gradePoints += c.getGpa()*c.getCredits();
				}
				credits += c.getCredits();
			}
			
			//The string that will be used to update the gpa label
			String s = "";
			
			//Calculating the current gpa
			DecimalFormat df = new DecimalFormat("#.##");
			gpa = gradePoints/(credits-creditsWithNoGrade);
			s += df.format(gpa);
			calculatedGpa.setText(s);

			if(hasTargetGpa) {
				//formula for determining gpa needed in current courses to achieve target gpa
				double gpaNeeded = (targetGpa*(credits) - gradePoints)/creditsWithNoGrade;
				
				//used the html tags to get displayed on multiple lines.  Will print messages if > 4 or < 2
				display1.setText("<html>You need a gpa of " + df.format(gpaNeeded) + "<br> in courses with no gpa");
				if(gpaNeeded > 4.0) {
					display2.setText("<html>GPA uncreachable <br>take more credits");
				}
				else if(gpaNeeded < 2.0) {
					display2.setText("<html>Could take fewer credits<br>if desired");
				}
			}

		}

	}

	private class Add15 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//Generates 5 courses, 3 credits each for 15 blank credits and 
			//adds them to the current courses list
			Course c = new Course("Course", 3.0, -1.0);
			for(int i = 0; i < 5; i++) {
				currentCourses.add(c);
			}
			//Remove all from the panel because we will rebuild it to update it properly
			currentPanel.removeAll();
			
			for(Course temp : currentCourses) {
				//This generates a button that will be used to display the course
				//This is so that the course can be clikced on to be removed.
				JButton courseListLabel = new JButton();
				courseListLabel.setText(temp.toString());
				courseListLabel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Container parent = courseListLabel.getParent();
						parent.remove(courseListLabel);
						parent.revalidate();
						parent.repaint();
						currentCourses.remove(temp);
					}
				});
				//Add this new course button to the panel
				currentPanel.add(courseListLabel);
			}
			
			//Repaint and revalidate
			coursePanel.revalidate();
			coursePanel.repaint();
		}

	}

	private class ClearCourses implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			//Clears courses and resets all display boxes
			
			display1.setText("");
			display2.setText("");

			currentCourses.clear();
			futureCourses.clear();
			pastCourses.clear();


			pastPanel.removeAll();
			pastPanel.repaint();

			currentPanel.removeAll();
			currentPanel.repaint();

			futurePanel.removeAll();
			futurePanel.repaint();

			calculatedGpa.setText("0.0");
		}

	}

	private class AddCourse implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Logic throughout is same as add15, just seperated for input and 3 different types
			
			int type = courseType.getSelectedIndex();
			String name;
			double gpa, credit;

			//For past courses
			if(type == 0) {
				try {
					name = courseInput.getText();
					//Determines if a gpa has been input
					try {
						gpa = Double.parseDouble(gpaInput.getText());
					}
					catch(Exception noGpa) {
						gpa = -1.0;
					}
					credit = Double.parseDouble(creditInput.getText());
					Course c = new Course(name, credit, gpa);
					
					//add course to the arraylist of courses
					pastCourses.add(c);

					//remove to rebuild the panel
					pastPanel.removeAll();
					for(Course temp : pastCourses) {
						//Adds course as a button so it can be removed on click
						JButton courseListLabel = new JButton();
						courseListLabel.setText(temp.toString());
						courseListLabel.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Container parent = courseListLabel.getParent();
								parent.remove(courseListLabel);
								parent.revalidate();
								parent.repaint();
								pastCourses.remove(c);
							}
						});
						pastPanel.add(courseListLabel);
					}
				}
				catch(Exception error) {
					error.printStackTrace();
				}
				pastPanel.validate();
				pastPanel.repaint();
			}

			//For present courses
			else if(type == 1) {
				try {
					name = courseInput.getText();
					//Determines if a gpa has been input
					try {
						gpa = Double.parseDouble(gpaInput.getText());
					}
					catch(Exception noGpa) {
						gpa = -1.0;
					}
					credit = Double.parseDouble(creditInput.getText());
					Course c = new Course(name, credit, gpa);
					
					//add course to the arraylist of courses
					currentCourses.add(c);

					//remove to rebuild the panel
					currentPanel.removeAll();
					for(Course temp : currentCourses) {
						//Adds course as a button so it can be removed on click
						JButton courseListLabel = new JButton();
						courseListLabel.setText(temp.toString());
						courseListLabel.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Container parent = courseListLabel.getParent();
								parent.remove(courseListLabel);
								parent.revalidate();
								parent.repaint();
								currentCourses.remove(c);
							}
						});
						currentPanel.add(courseListLabel);
					}
				}
				catch(Exception error) {
					error.printStackTrace();
				}
				currentPanel.validate();
				currentPanel.repaint();
			}

			//For future courses
			else if(type == 2) {
				try {
					name = courseInput.getText();
					//Determines if a gpa has been input
					try {
						gpa = Double.parseDouble(gpaInput.getText());
					}
					catch(Exception noGpa) {
						gpa = -1.0;
					}
					credit = Double.parseDouble(creditInput.getText());
					Course c = new Course(name, credit, gpa);
					
					//add course to the arraylist of courses
					futureCourses.add(c);

					//remove to rebuild the panel
					futurePanel.removeAll();
					for(Course temp : futureCourses) {
						//Adds course as a button so it can be removed on click
						JButton courseListLabel = new JButton();
						courseListLabel.setText(temp.toString());
						courseListLabel.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Container parent = courseListLabel.getParent();
								parent.remove(courseListLabel);
								parent.revalidate();
								parent.repaint();
								futureCourses.remove(c);
							}
						});
						futurePanel.add(courseListLabel);
					}
				}
				catch(Exception error) {
					error.printStackTrace();
				}
				futurePanel.validate();
				futurePanel.repaint();
			}
			coursePanel.validate();
			coursePanel.repaint();
		}
	}
}

