

package JavaFXMain;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import classOfProgram.*
;

public class MainAppliction extends Application{
	private ArrayList<Member> memberList = new ArrayList<>();
	
	//Anchor and menu section box
	private AnchorPane aPane = new AnchorPane();
	private VBox vbPane = new VBox();

	//Header Pane
	private VBox hvbPane = new VBox();
	 
	//SideView Stack
	private VBox vAll = new VBox();
	private VBox vAdd = new VBox();
	private VBox vManage = new VBox();
	private StackPane mainStack = new StackPane();
	
	//Buttons
	private Button btnViewAll = new Button("View Members");
	private Button btnAdd = new Button("Add Member");
	private Button btnManage = new Button("Manage Member");
	private Button btnQuit = new Button("Exit");
	
	//Labels & Text stuff
	private Label header = new Label( "SCS Membership\n     App",new ImageView(new Image(getClass().getResourceAsStream("iconLogo.png"))));
	private Label viewAllHeader = new Label("Viewing All Member", new ImageView(new Image(getClass().getResourceAsStream("iconMember.png"))));
	private TextArea viewAllOutput = new TextArea();
	
	private HBox checkboxHB = new HBox();
	private static final CheckBox oCheck = new CheckBox("Ordinary");
	private static final CheckBox sCheck = new CheckBox("Student");
	private static final CheckBox lCheck = new CheckBox("Lifetime");
	
	//Classes to reuse code
	private AddMemberFX am = new AddMemberFX();
	private ManageMemberFX mm = new ManageMemberFX();
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		memberList = initRecords();
		runAllConfig();
		aPane.getChildren().addAll(hvbPane,vbPane,mainStack);
		Scene s = new Scene(aPane,1450,700);
		s.getStylesheets().add(getClass().getResource("mainStyle.css").toExternalForm());
		primaryStage.setTitle("SCS Membership App");
		primaryStage.setScene(s);
		primaryStage.show();
		
		buttonConfig(primaryStage);
	}
	
	//Runs all COnfiguration
	
	private void runAllConfig() {
		loadOtherPaneConfig();
		setUpId();
		anchorConfig();
		setButtonCSS();
		doView();
	}
	
	/*------------------------------------------------- Handle Changing of Views via StackPane ---------------------------------- */
	
	private void doView() {
		memberList = initRecords();
		vAll.toFront();
		viewAllOutput.setText(viewOrdinary());
		viewAllOutput.appendText(viewStudent());
		viewAllOutput.appendText(viewLifeTime());
	}

	private void doAdd() {
		memberList = initRecords();
		vAdd.toFront();
		am.getResetAll();
	} 
	private void doManage(Stage ps) {
		memberList = initRecords();
		mm.setObList(memberList);
		mm.setUpTable();
		vManage.toFront();
	}
	
	private void doQuit(Stage s) {
		s.close();
	}
	
	
	
	/*------------------------------------- Button Config --------------------------------------------------- */
	private void buttonConfig(Stage s) {
		EventHandler<ActionEvent> handleAll = (ActionEvent e ) -> doView();
		btnViewAll.setOnAction(handleAll);
		
		EventHandler<ActionEvent> handleAdd = (ActionEvent e ) -> doAdd();
		btnAdd.setOnAction(handleAdd);

		EventHandler<ActionEvent> handleManage = (ActionEvent e ) -> doManage(s);
		btnManage.setOnAction(handleManage);

		EventHandler<ActionEvent> handleQuit = (ActionEvent e ) -> doQuit(s);
		btnQuit.setOnAction(handleQuit);
	}
	
	
	/*----------------------------------- Loads Pane from other classes ---------------------------------------*/
	
	private void loadViewAllView() {
		checkboxHB.getChildren().addAll(oCheck,sCheck,lCheck);
		checkBoxConfig();
		VBox outputAndCheckBox = new VBox();
		outputAndCheckBox.getChildren().addAll(checkboxHB,viewAllOutput);
		vAll.getChildren().addAll(viewAllHeader,outputAndCheckBox);
		viewAllOutput.setEditable(false);
		hvbPane.getChildren().add(header);
		vbPane.getChildren().addAll(btnViewAll,btnAdd,btnManage,btnQuit);
		
	}
	
	private void loadOtherPaneConfig() {
		vAdd = am.getPane();
		vManage = mm.getPane();
		loadViewAllView();
		mainStack.getChildren().setAll(vAdd,vManage,vAll);
		
	}
	
	/*----------------------------------------- Method for configuration -------------------------------------- */

	private void anchorConfig() {
		AnchorPane.setTopAnchor(hvbPane, 0.0);
		AnchorPane.setLeftAnchor(hvbPane, 0.0);
		AnchorPane.setTopAnchor(vbPane, 150.0);
		AnchorPane.setBottomAnchor(vbPane, 0.0);
		AnchorPane.setLeftAnchor(vbPane, 0.0);
		AnchorPane.setTopAnchor(mainStack, 0.0);
		AnchorPane.setLeftAnchor(mainStack, 250.0);
		AnchorPane.setBottomAnchor(mainStack, 0.0);
		AnchorPane.setRightAnchor(mainStack, 0.0);
	}
	
	private void setButtonCSS() {
		btnViewAll.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
		btnAdd.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
		btnManage.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
		btnQuit.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
	}
	
	private void checkBoxConfig() {
		oCheck.getStylesheets().add(getClass().getResource("checkBox.css").toExternalForm());
		sCheck.getStylesheets().add(getClass().getResource("checkBox.css").toExternalForm());
		lCheck.getStylesheets().add(getClass().getResource("checkBox.css").toExternalForm());
		
		EventHandler<ActionEvent> handleSelection = (ActionEvent e ) -> getSelectedItem();
		oCheck.setOnAction(handleSelection);
		sCheck.setOnAction(handleSelection);
		lCheck.setOnAction(handleSelection);

	}
	private void setUpId() {
		viewAllOutput.setId("viewAllOutput");
		viewAllHeader.setId("viewHeader");
		hvbPane.setId("hvb");
		header.setId("mainHeader");
		vAll.setId("vAll");
		vbPane.setId("vbPane");
		oCheck.setId("ocheck");
		sCheck.setId("scheck");
		lCheck.setId("lcheck");
		checkboxHB.setId("cBox");
	}
	
	
	/*----------------------------------------------------------- Method for viewing and retrieving ------------------------------------------------------------ */
	private void getSelectedItem() {
		String showAll = viewOrdinary() + viewStudent() + viewLifeTime();
		String outputText = "";
		outputText += oCheck.isSelected() ?   viewOrdinary() : "";
		outputText += sCheck.isSelected() ? (outputText.isEmpty() ? viewStudent() : viewStudent()) :"";		
		outputText += lCheck.isSelected() ? (outputText.isEmpty() ? viewLifeTime() : viewLifeTime()) : "";	
		if ((!oCheck.isSelected() && !sCheck.isSelected() && !lCheck.isSelected())) {
			outputText = showAll;
		}
		viewAllOutput.setText(outputText);
	}
	
	
	public String viewOrdinary() {
		String odOutput = "Ordinary\n" + String.format("%-15s %-25s %s\n", "ID", "NAME", "Membership Expiry");
		for(Member m : memberList) {
			if (m instanceof Ordinary) {
				Ordinary o = (Ordinary)m;
				odOutput += String.format("%-15s %-25s %s\n", o.getId(), o.getName(), o.formatDate(o.getExpiryDate()));
			}
		}
		return odOutput+ lineThing(113,"-") + "\n";
	}
	
	public String viewStudent() {
		String sOutput = "Student\n" +String.format("%-15s %-25s %s\n", "ID", "NAME", "School");
		for(Member m : memberList) {
			if (m instanceof Student) {
				Student s = (Student)m;
				sOutput += String.format("%-15s %-25s %s\n", s.getId(), s.getName(), s.getSchool());
			}
		}
		return sOutput+ lineThing(113,"-") + "\n";
	}
	
	public String viewLifeTime() {
		String ltOutput = "Lifetime\n" + String.format("%-15s %-25s %s\n", "ID", "NAME","CITATION");
		for (Member m : memberList) {
			if (m instanceof Lifetime) {
				Lifetime l = (Lifetime)m;
				ltOutput += String.format("%-15s %-26s%s\n", l.getId(), l.getName(),l.formatCitation(l.displayCitation()));
			}
		}
		return ltOutput + lineThing(113,"-") + "\n\n";
	}
	
	private String lineThing(int len, String c) {
		return String.format("%" + len + "s", " ").replaceAll(" ", c);
	}
	
	private ArrayList<Member> initRecords() {
		ArrayList<Member> mList = new ArrayList<>();
		DBUtil.init("jdbc:mysql://localhost:3306/projectv1", "root", "");
		ResultSet rs = DBUtil.getTable("SELECT * FROM member");
		try {
			while(rs.next()) {
				String id = rs.getString("ID");
				String name = rs.getString("Name");
				String category = rs.getString("Category");
				if (category.equals("Ordinary")) {
					Date expiryDate = rs.getDate("MemberUntil");
					mList.add(new Ordinary(id, name, expiryDate));
				}
				else if (category.equals("Student")) {
					String school = rs.getString("School");
					mList.add(new Student(id, name, school));
				}
				else if (category.equals("Lifetime")) {
					mList.add(new Lifetime(id,name));
				}
			}
			rs.last();
			DBUtil.close();
		}
		catch (SQLException se) {
			System.out.println("Error: " + se.getMessage());
		}
		return mList;
	}	
}
