package project1;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class Ordinary extends Member {
	private Date expiryDate;
	public Ordinary(String id,String name,  Date expiryDate) {
		super(id, name);
		this.expiryDate =  expiryDate;
		// TODO Auto-generated constructor stub
	}
	
	public Date getExpiryDate() {
		return expiryDate;
	}
	
	public String formatDate(Date date) {
		LocalDate exDate = LocalDate.parse(String.valueOf(date));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		return exDate.format(formatter);
	} 
}