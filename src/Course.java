/*
 * Very basic class to keep track of the information for courses
 */
public class Course {

	private double credits;
	private double gpa;
	private String courseName;
	
	public Course() {
		this.credits = 0.0;
		this.gpa = 0.0;
		this.courseName = "";
	}
	
	public Course(String courseName, double credits, double gpa) {
		this.credits = credits;
		this.gpa = gpa;
		this.courseName = courseName;
	}
	
	public double getCredits() {
		return credits;
	}

	public void setCredits(double credits) {
		this.credits = credits;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String toString() {
		return courseName + " - credits: " + credits + " GPA: "+ gpa;
	}
}
