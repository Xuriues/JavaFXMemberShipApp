package classOfProgram;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Lifetime extends Member implements Citation{
	private String citation = null;
	
	public Lifetime(String id, String name) {
		super(id, name);
		// TODO Auto-generated constructor stub
	}
	

	public String getCitation() {
		return citation;
	}
	
	public String formatCitation(String text) {
		String citation = text;
		if (text.contains("\n")) {
			String[] allCitation = text.split("\n");
			citation = allCitation[0].trim() + "\n";
			for(int i = 1; i < allCitation.length; i ++) {
				citation += String.format("%-42s%s\n","",allCitation[i].trim());
			}
		}
		return citation;
	}
	
	
	@Override
	public String displayCitation() {
		// TODO Auto-generated method stub
			DBUtil.init("jdbc:mysql://localhost:3306/projectv1", "root", "");
			ResultSet rs =  DBUtil.getTable("SELECT * FROM member M INNER JOIN citation C ON C.ID = M.ID WHERE C.ID ='" + super.getId() + "'");
			try {
				while(rs.next()) {
					String c = rs.getString("Citation");
					this.citation = c;
				}
				DBUtil.close();
			}
			catch (SQLException se) {
				System.out.println("Error: " + se.getMessage());
			}
		return getCitation();
	}
}
