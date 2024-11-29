
package JavaFXMain;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;

import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.*;

public class AddMemberFX {
	private VBox vbAddMember = new VBox();
	private Label addHeader = new Label("Add a new Member", new ImageView(new Image(getClass().getResourceAsStream("iconAddMember.png"))));
	private Label lbName = new Label("Enter name of member: ");
	private TextField tfName = new TextField();
	private Label lbCategory = new Label("Select member category: ");
	private VBox mainThings = new VBox();
	private HBox buttonHB = new HBox();
	
	
	private ComboBox<String> categoryMenu = new ComboBox<>();
	
	
	private Label lbSelected = new Label("Enter expiry date of membership (YYYY-MM-DD): ");
	
	private VBox afterChoice = new VBox();
	private TextArea taSelected = new TextArea();
	private TextField tfSelected = new TextField();
	
	
	private Button addBtn = new Button("Add Member");
	private Button resetBtn = new Button("Reset");
	
	public AddMemberFX() {
		setUpChoiceBox();
		setId();
		initObjects();
		eventStuff();
	}
	
	/*---------------------------Sets up the choices for box-------------------------- */
	
	private void setUpChoiceBox() {
		String[] list = {"Ordinary","Student","Lifetime"};
		categoryMenu.getItems().addAll(list);
		categoryMenu.getSelectionModel().selectFirst();
	}
	
	
	/*---------------------------Loads nodes for the view-------------------------- */

	private void initObjects(){
		afterChoice.getChildren().add(tfSelected);
		buttonHB.getChildren().addAll(addBtn,resetBtn);
		mainThings.getChildren().addAll(lbName,tfName,lbCategory,categoryMenu,lbSelected,afterChoice,buttonHB);
		vbAddMember.getChildren().addAll(addHeader,mainThings);
		Scene s = new Scene(vbAddMember);
		s.getStylesheets().add(getClass().getResource("mainStyle.css").toExternalForm());
	}
	/*---------------------------Sets up ID for CSS-------------------------- */

	private void setId() {
		vbAddMember.setId("addPane");
		addHeader.setId("addHeader");
		lbName.setId("lbName");
		mainThings.setId("mainThingForAdd");
		lbCategory.setId("lbCategory");
		categoryMenu.setId("categoryMenu");
		lbSelected.setId("lbSelected");
		buttonHB.setId("HBbtn");
		addBtn.setId("addBtn");
		resetBtn.setId("resetBtn");
		taSelected.setId("taSelected");
		tfSelected.setId("tfSelected");
		tfName.setId("tfNameAdd");
		
	}
	/*---------------------------Event Configuration-------------------------- */

	private void eventStuff() {
		EventHandler<ActionEvent> handleCategoryMenu = (ActionEvent e ) -> doCategory();
		categoryMenu.setOnAction(handleCategoryMenu);
		EventHandler<ActionEvent> handleReset = (ActionEvent e ) -> resetAll();
		resetBtn.setOnAction(handleReset);
		EventHandler<ActionEvent> handleAdd = (ActionEvent e ) -> doAddMember();
		addBtn.setOnAction(handleAdd);
	}
	
	
	
	
	/*---------------------------Sets up choices for user to enter the correct details-------------------------- */
	private void doCategory() {
		String output = categoryMenu.getValue().equals("Ordinary") ? "Enter expiry date of membership (YYYY-MM-DD): " : (categoryMenu.getValue().equals("Student") ? "Enter School or institution of learning: " : "\t\t\tEnter Citation: \n(If there are more than 1 enter a new line.): ");
		if (categoryMenu.getValue().equals("Ordinary")) {
			tfSelected.setText("");
			setUpAfterChoice(1);
		}
		else if (categoryMenu.getValue().equals("Student")) {
			tfSelected.setText("");
			setUpAfterChoice(1);
		}
		else {
			setUpAfterChoice(3);
		}
		afterChoice.setVisible(true);
		lbSelected.setText(output);
		buttonHB.setVisible(true);
	}
	
	
	/*---------------------------Swaps between textfield and textarea depending on the category-------------------------- */
	
	private void setUpAfterChoice(int input) {
		afterChoice.getChildren().clear();
		if (input == 1) {
			afterChoice.getChildren().add(tfSelected);
		}
		else {
			afterChoice.getChildren().add(taSelected);
		}
	}

	
	
	/*---------------------------Method for inserting a new record-------------------------- */	
	
	private void doAddMember() {
		if(validateInput()) {
			String getNextId = getLastId();
			String insertSql = "";
			String insertSql2 = "";
			if (categoryMenu.getValue().equals("Ordinary")) {
				Date newDate = Date.valueOf(tfSelected.getText());
				insertSql = "INSERT INTO member (ID, Name, Category, MemberUntil) VALUES ('" + getNextId + "', '" + tfName.getText() +"', '" + "Ordinary" + "', '" + newDate + "')";
			}
			else if (categoryMenu.getValue().equals("Student")) {
				insertSql = "INSERT INTO member (ID, Name, Category, School) VALUES ('" + getNextId + "', '" + tfName.getText() + "', '"+ "Student" + "','" + tfSelected.getText() + "')";
			}
			else {
				insertSql = "INSERT INTO member (ID, Name, Category) VALUES ('" + getNextId + "', '" + tfName.getText() + "', '"+ "Lifetime" + "')";
				insertSql2 = "INSERT INTO citation(ID, Citation) VALUES ('" + getNextId + "', '" + taSelected.getText() + "')";
				if (DBUtil.execSQL(insertSql) == 1 && DBUtil.execSQL(insertSql2) == 1) {
					addSuccessful(tfName.getText() + " has been added!");
				}
				else  {setAlert("Fail to add this member please try again.");}
				return;
 			}
			int rowsAdded = DBUtil.execSQL(insertSql);
			if (rowsAdded == 1) {
				addSuccessful(tfName.getText() + " has been added!");
			}else {setAlert("Fail to add this member please try again.");}
		}
		
	}
	
	
	/*---------------------------Retrieves the last ID in the DB to avoid any errors.-------------------------- */
	
	private String getLastId(){
		String id = "";
		DBUtil.init("jdbc:mysql://localhost:3306/projectv1", "root", "");
		ResultSet rs = DBUtil.getTable("SELECT * FROM member");
		try {
			rs.last();
			id = rs.getString("ID");
			DBUtil.close();
		}catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		}
		int idNum = Integer.parseInt(id.substring(1));
		return id.substring(0,1) + (idNum+1);
	}
	
	
	
	/*---------------------------Method to valdiate user inputs E.G a dateFormat-------------------------- */
	
	private boolean validateInput() {
		boolean flag = false;
		String errorMsg = "Please fill in all fields to proceed!";
		if (!Pattern.matches("\\s*", tfName.getText())) {
			if (((categoryMenu.getValue().equals("Ordinary") || categoryMenu.getValue().equals("Student")) && !(Pattern.matches("\\s*",tfSelected.getText()))) || (categoryMenu.getValue().equals("Lifetime") && !Pattern.matches("\\s*", taSelected.getText()))) {
				String pattern = "([2][0][0-9]{2})[-.]((0[1-9])|1[012])[-.](0[1-9]|[12][0-9]|3[01])";
				flag = categoryMenu.getValue().equals("Lifetime") ? true : (categoryMenu.getValue().equals("Student") ? true : false);
				if (Pattern.matches(pattern, tfSelected.getText())) {
					String[] dateSplit = tfSelected.getText().split("-");
					int dayInThatMonth = YearMonth.of(Integer.parseInt(dateSplit[0]),Integer.parseInt(dateSplit[1])).lengthOfMonth();
					Date currDate = Date.valueOf(LocalDate.now());
					if (Integer.parseInt(dateSplit[2]) <= dayInThatMonth && Date.valueOf(tfSelected.getText()).after(currDate)) {
						flag = true;
					}
					else {
						errorMsg = "Error with the date that you entered. Please try again.";
					}
				}
				else {
					errorMsg = "Please enter the date in the specified format.";
				}
			}
		}
		if (!flag) {
			setAlert(errorMsg);
		}
		return flag;
	}
	
	
	
	/*---------------------------Sets up alert box for success/failure -------------------------- */
	private void setAlert(String errorMsg) {
		Alert alert = new Alert(AlertType.ERROR, errorMsg);
		alert.setTitle("Error");
		alert.show();
	}
	private void addSuccessful(String msg) {
		Alert alert = new Alert(AlertType.INFORMATION, msg);
		alert.setTitle("Added Successfully!");
		alert.setHeaderText("Add Successful.");
		alert.show();
	}
	
	/*---------------------------Method for reset.-------------------------- */
	
	private void resetAll() {
		tfName.setText("");
		tfSelected.setText("");
		taSelected.setText("");
		lbSelected.setText("Enter expiry date of membership (YYYY-MM-DD): ");
		categoryMenu.getSelectionModel().selectFirst();
		setUpAfterChoice(1);
	}
	public void getResetAll() {
		resetAll();
	}
	
	/*---------------------------Retrieves this pane for the mainApplication to use.-------------------------- */

	
	public VBox getPane() {
		return vbAddMember;
	}
}