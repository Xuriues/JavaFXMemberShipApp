package project1;

public class Student extends Member{
	private String school;
	public Student(String id,String name, String school) {
		super(id, name);
		this.school = school;
	
	}
	public String getSchool() {
		return school;
	}
}