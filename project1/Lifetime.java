package project1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Lifetime extends Member implements Citation{
	private String citation = null;
	
	public Lifetime(String id, String name) {
		super(id, name);
		// TODO Auto-generated constructor stub
	}
	

	public String getCitation() {
		return citation;
	}
	
	private String formatCitation(String text) {
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
		if (this.citation == null) {
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectv1", "root", "");
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM member M INNER JOIN citation C ON C.ID = M.ID WHERE C.ID ='" + super.getId() + "'");
				while(rs.next()) {
					String c = rs.getString("Citation");
					this.citation = c;
				}
				conn.close();
				statement.close();
				rs.close();
			}
			catch (SQLException se) {
				System.out.println("Error: " + se.getMessage());
			}
		}
		return formatCitation(getCitation());
	}
}
