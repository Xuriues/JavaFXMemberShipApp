
package JavaFXMain;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;
import java.sql.Date;
import classOfProgram.*;
public class ModifyMemberFX extends Application{
	
	private static Member user;
	public static ManageMemberFX mm;


	//Holds all nodes
	private VBox modifyMain = new VBox();
	private Label modifyHeaderLbl = new Label("Edit/Delete",new ImageView(new Image(getClass().getResourceAsStream("iconManage.png"))));
	
	//Holds id related nodes
	private VBox idVPane = new VBox();
	private Label idLbl = new Label("ID");
	private TextField idTf = new TextField();

	
	
	//Holds name related nodes
	private VBox nameVPane = new VBox();
	private Label nameLbl = new Label("Name");
	private TextField nameTf = new TextField();

	
	//Holds category related nodes
	private VBox categoryVPane = new VBox();
	private Label categoryLbl = new Label("Category");
	private TextField categoryTf = new TextField();
	
	
	//Holds field for category nodes.
	private VBox itemsVPane = new VBox();
	private Label categoryItemLbl = new Label();
	private TextField itemTf = new TextField();
	private TextArea citationTa = new TextArea();
	
	
	//Holds button related things
	private HBox hPaneBtn = new HBox();
	private Button btnUpdate = new Button("Update");
	private Button btnDelete = new Button("Delete");
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub
		DBUtil.init("jdbc:mysql://localhost:3306/projectv1", "root", "");
		setId();
		configWindow();

		Scene mainScene = new Scene(modifyMain);
		mainScene.getStylesheets().add(getClass().getResource("mainStyle.css").toExternalForm());
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Manage User");
		primaryStage.setWidth(380);
		primaryStage.setHeight(550);
		eventStuff(primaryStage);
		primaryStage.show();
	}
	/*---------------------------Configuration for events-------------------------- */

	private void eventStuff(Stage ps) {
		EventHandler<ActionEvent> handleUpdate = (ActionEvent e ) -> doUpdate(ps);
		btnUpdate.setOnAction(handleUpdate);
		EventHandler<ActionEvent> handleDelete = (ActionEvent e ) -> doDelete(ps);
		btnDelete.setOnAction(handleDelete);

	}
	/*---------------------------Method for updating that takes in a Stage in order to close it.-------------------------- */	
	private void doUpdate(Stage ps) {
		String updateSQL = "";
		int rowUpdated = 0;
		System.out.println(user.getId());
		if (validateInput()) {
			if (user instanceof Ordinary) {
				Date newDate = Date.valueOf(itemTf.getText());
				updateSQL = "UPDATE member SET Name ='" + nameTf.getText() + 
						"', MemberUntil= '" + newDate + "' WHERE ID ='" + idTf.getText() + "'" ;
			}
			else if (user instanceof Student) {
				updateSQL = "UPDATE member SET Name ='" + nameTf.getText() + 
						"', School= '" + itemTf.getText() + "' WHERE ID ='" + idTf.getText() + "'" ;
			}
			else {
				updateSQL = "UPDATE member SET Name ='" + nameTf.getText() + "' WHERE ID = '" + idTf.getText() + "'";
				int rowUpdatedCitation = DBUtil.execSQL("UPDATE citation SET Citation ='" + citationTa.getText() +  "' WHERE ID ='" + idTf.getText() + "'");
				if (DBUtil.execSQL(updateSQL) == 1 && rowUpdatedCitation == 1) {
					updateSucess(idTf.getText() + " has been updated.", "Update Successful", ps);
				}
				return;
			}
		}
		rowUpdated = DBUtil.execSQL(updateSQL);
		if (rowUpdated == 1) {
			updateSucess(idTf.getText() + " has been updated.", "Update Successful", ps);
		}
		else {
			setAlert("Failed to Update.");
		}
	}
	
	/*---------------------------Method for deleting an record and takes in a stage to close it-------------------------- */

	private void doDelete(Stage ps) {
		String deleteSQL = "";
		String deleteSQL2 = "";
		if (user instanceof Student || user instanceof Ordinary) {
			deleteSQL = "DELETE FROM member WHERE ID='" + idTf.getText() + "'";
			int rowsAffected = DBUtil.execSQL(deleteSQL);
			if (rowsAffected == 1) {
				updateSucess(idTf.getText() + " has been Deleted!","Delete Successful", ps);
			}
			else {
				setAlert("Failed to Delete.");
			}
		}
		else {
			deleteSQL = "DELETE FROM member WHERE ID='" + idTf.getText() + "'";
			deleteSQL2 = "DELETE FROM citation WHERE ID='" + idTf.getText() + "'";
			if (DBUtil.execSQL(deleteSQL) == 1 && DBUtil.execSQL(deleteSQL2) == 1) {
				updateSucess(idTf.getText() + " has been Deleted!","Delete Successful",ps);
			}
			else  {setAlert("Failed to Delete.");
			}
		}
	}
	
	
	/*---------------------------Alert Boxes for success or fail-------------------------- */

	private void setAlert(String errorMsg) {
		Alert alert = new Alert(AlertType.ERROR, errorMsg);
		alert.setTitle("Error");
		alert.show();
	}
	private void updateSucess(String msg, String header, Stage ps) {
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(header);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        	ManageMemberFX.flag = true;
        	mm.setObList(initRecords());
        	mm.setUpTable();
        	ps.close();
        }
    }

	
	
	/*---------------------------Sets up ID-------------------------- */
	private void setId() {
		modifyHeaderLbl.setId("modifyHeaderLbl");
		
		idVPane.setId("idVPane");
		nameVPane.setId("nameVPane");
		itemsVPane.setId("itemsVPane");
		categoryVPane.setId("categoryVPane");
		hPaneBtn.setId("hPaneBtn");
		
		idTf.setText("idTf");
		nameTf.setText("nameTf");
		categoryTf.setText("categoryTf");
		
		idLbl.setId("idLbl");
		nameLbl.setId("nameLbl");
		categoryLbl.setId("categoryLbl");
		categoryItemLbl.setId("itemLbl");
		btnUpdate.setId("btnUpdate");
		btnDelete.setId("btnDelete");


	}
	
	/*---------------------------Configure pane for the view-------------------------- */
	private void initWindow(VBox handle) {
		idVPane.getChildren().addAll(idLbl,idTf);
		idVPane.setMinWidth(300);
		nameVPane.getChildren().addAll(nameLbl,nameTf);
		categoryVPane.getChildren().addAll(categoryLbl,categoryTf);
		hPaneBtn.getChildren().addAll(btnUpdate,btnDelete);
		modifyMain.getChildren().addAll(modifyHeaderLbl,idVPane,nameVPane,categoryVPane,handle,hPaneBtn);
		
		modifyMain.setAlignment(Pos.TOP_CENTER);
		
	}
	/*---------------------------Shows proper fields depending on the category-------------------------- */
	private void configWindow() {
		itemsVPane.getChildren().clear();
		if (user != null) {
			setItems();
			if (user instanceof Student) {
				Student s = (Student)user;
				categoryTf.setText("Student");
				categoryItemLbl.setText("School: ");
				itemTf.setText(s.getSchool());
				itemsVPane.getChildren().addAll(categoryItemLbl,itemTf);
			}
			else if (user instanceof Ordinary) {
				Ordinary o = (Ordinary)user;
				categoryTf.setText("Ordinary");
				categoryItemLbl.setText("Expiry date of membership (YYYY-MM-DD):");
				itemTf.setText(""+o.getExpiryDate());
				itemsVPane.getChildren().addAll(categoryItemLbl,itemTf);
			}
			else{
				Lifetime lt = (Lifetime)user;
				categoryTf.setText("LifeTime");
				categoryItemLbl.setText("Citation (Enter new line to add more): ");
				citationTa.setText(lt.getCitation());
				itemsVPane.getChildren().addAll(categoryItemLbl,citationTa);

			}
			initWindow(itemsVPane);
		}
	
	}
	/*---------------------------Sets up correct details and whether the field is disabled.-------------------------- */

	private void setItems() {
		idTf.setEditable(false);
		idTf.setText(user.getId());
		categoryTf.setEditable(false);
		nameTf.setText(user.getName());
	}
	
	
	/*---------------------------Passes in the user from Manage Table into this view in order to show the correct details.-------------------------- */

	public static void setUser(Member m) {
		user = m;
	}
	
	
	/*---------------------------Validates user input-------------------------- */

	private boolean validateInput() {
		boolean flag = false;
		String errorMsg = "Please fill in all fields to proceed!";
		if (!Pattern.matches("\\s*", nameTf.getText())) {
			if (((user instanceof Ordinary || user instanceof Student) && !(Pattern.matches("\\s*",itemTf.getText()))) || (user instanceof Lifetime && !Pattern.matches("\\s*", citationTa.getText()))) {
				String pattern = "([2][0][0-9]{2})[-.]((0[1-9])|1[012])[-.](0[1-9]|[12][0-9]|3[01])";
				flag = user instanceof Lifetime ? true : (user instanceof Student ? true : false);
				if (Pattern.matches(pattern, itemTf.getText())) {
					String[] dateSplit = itemTf.getText().split("-");
					int dayInThatMonth = YearMonth.of(Integer.parseInt(dateSplit[0]),Integer.parseInt(dateSplit[1])).lengthOfMonth();
					Date currDate = Date.valueOf(LocalDate.now());
					Ordinary od = (Ordinary)user;
					if (Integer.parseInt(dateSplit[2]) <= dayInThatMonth && Date.valueOf(itemTf.getText()).after(currDate)) {
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
