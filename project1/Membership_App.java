package project1;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Membership_App {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Membership_App app = new Membership_App();
		app.start();
	}
	
	private void start() {
		ArrayList<Member> memberList = loadRecords();
		int option = 0;
		while (option != 3) {
			menu(1);
			option = Helper.readInt("Enter choice >");
			if (option ==1) {
				viewAllMember(memberList);
			}
			else if (option ==2) {
				viewByCategory(memberList);
			}
			else if (option > 3 || option <= 0){
				System.out.println("Please enter an option from 1 - 3.");
			}
		}
		System.out.println("Thank you for using the SCS Membership App! Goodbye!");
	}
	
	/**
	 * All menus being used for the start method and for displaying of option
	 * @param subMenu and viewAllMember takes in a Member Array list for looping for its respective methods.
	 */
	private void menu(int op) {
		String menuName = op == 1 ? "SCS MEMBERSHIP" : "SELECT CATEGORY";
		Helper.line(120, "=");
		System.out.println(menuName);
		Helper.line(120, "=");
		if (op == 1) {
			System.out.println("   1. View All Members");
			System.out.println("   2. View members by Category");
			System.out.println("   3. Quit");
		}
		else {
			System.out.println("   1. Ordinary");
			System.out.println("   2. Student");
			System.out.println("   3. Lifetime");
			System.out.println("   4. Exit");
		}
	}
	
	private void viewByCategory(ArrayList<Member> memberList) {
		menu(2);
		int op = Helper.readInt("Enter option >");
		while (op > 4 || op < 1) {
			System.out.println("That is not a category option please try again.");
			menu(2);
			op = Helper.readInt("Enter option >");
		}
		if (op == 4) {return;}
		Helper.line(100, "=");
		System.out.println(op == 1 ? viewOrdinary(memberList) : (op == 2 ? viewStudent(memberList) : viewLifeTime(memberList)));
	}
	
	private void viewAllMember(ArrayList<Member> memberList) {
		Helper.line(120, "-");
		System.out.println(viewOrdinary((memberList)));
		Helper.line(120, "-");
		System.out.println(viewStudent((memberList)));
		Helper.line(120, "-");
		System.out.println(viewLifeTime((memberList)));
	}	
	
	
	/**
	 * Following methods to extract the relevant data from the list and adds them to a completed text.
	 * @param Accepts the Array list memberList
	 * @return String returns a String variable to be used in other methods.
	 */
	private String viewOrdinary(ArrayList<Member> memberList) {
		String odOutput = "Ordinary\n" + String.format("%-15s %-25s %s\n", "ID", "NAME", "Membership Expiry");
		for(Member m : memberList) {
			if (m instanceof Ordinary) {
				Ordinary o = (Ordinary)m;
				odOutput += String.format("%-15s %-25s %s\n", o.getId(), o.getName(), o.formatDate(o.getExpiryDate()));
			}
		}
		return odOutput;
	}
	
	private String viewStudent(ArrayList<Member> memberList) {
		String sOutput = "Student\n" +String.format("%-15s %-25s %s\n", "ID", "NAME", "School");
		for(Member m : memberList) {
			if (m instanceof Student) {
				Student s = (Student)m;
				sOutput += String.format("%-15s %-25s %s\n", s.getId(), s.getName(), s.getSchool());
			}
		}
		return sOutput;
	}
	
	private String viewLifeTime(ArrayList<Member> memberList) {
		String ltOutput = "Lifetime\n" + String.format("%-15s %-25s %s\n", "ID", "NAME","CITATION");
		for (Member m : memberList) {
			if (m instanceof Lifetime) {
				Lifetime l = (Lifetime)m;
				ltOutput += String.format("%-15s %-26s%s\n", l.getId(), l.getName(),l.displayCitation());
			}
		}
		return ltOutput;
	}
	
	
	/**
	 * Loading of data from DB and adds them into memberList.
	 * @return returns memberList with the data added into it for the main program to use .
	 */
	private ArrayList<Member> loadRecords() {
		ArrayList<Member> memberList = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectv1", "root", "");
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM member");
			while(rs.next()) {
				String id = rs.getString("ID");
				String name = rs.getString("Name");
				String category = rs.getString("Category");
				if (category.equals("Ordinary")) {
					Date expiryDate = rs.getDate("MemberUntil");
					memberList.add(new Ordinary(id, name, expiryDate));
				}
				else if (category.equals("Student")) {
					String school = rs.getString("School");
					memberList.add(new Student(id, name, school));
				}
				else if (category.equals("Lifetime")) {
					memberList.add(new Lifetime(id,name));
				}
			}
			conn.close();
			statement.close();
			rs.close();
		}
		catch (SQLException se) {
			System.out.println("Error: " + se.getMessage());
		}
		return memberList;
	}
}