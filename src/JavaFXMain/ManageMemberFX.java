

package JavaFXMain;
import java.util.ArrayList;
import java.util.function.Predicate;

import classOfProgram.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class ManageMemberFX {
	public static boolean flag = false;
	private VBox vbManageMember;
	private ObservableList<Member> ObList;
	public ArrayList<Member> arrList = new ArrayList<>();
	private TextField searchtf = new TextField();
	private Label searchlbl = new Label("Search: ");
	private HBox filterPane = new HBox();
	private VBox fieldAndTablePane = new VBox();
	private TableView<Member> tblView;
	private Member memberSelected;
	private VBox tableAndStuff = new VBox();
	private Label manageHeader = new Label("Manage Members", new ImageView(new Image(getClass().getResourceAsStream("iconManage.png"))));

	public ManageMemberFX() {
		initObjects();	
	}
	
	/*---------------------------Sets up tableView-------------------------- */
	public void setUpTable() {
		clearTable();
		TableColumn<Member, String> colID = new TableColumn<>("ID");
		colID.setCellValueFactory(new PropertyValueFactory<>("id"));
		colID.setPrefWidth(50);
		TableColumn<Member, String> colName = new TableColumn<>("Name");
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colName.setPrefWidth(200);
		TableColumn<Member, String> colCate = new TableColumn<>("Category");
		colCate.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue() instanceof Ordinary ? "Ordinary" : (cellData.getValue() instanceof Student ? "Student" : "Lifetime")));		
		colCate.setPrefWidth(85);
		TableColumn<Member, String> colDate = new TableColumn<>("Date of Expiry");
		colDate.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue() instanceof Ordinary ? ((Ordinary)cellData.getValue()).formatDate(((Ordinary)cellData.getValue()).getExpiryDate()) : "-"));
		colDate.setPrefWidth(100);
		TableColumn<Member, String> colSchool = new TableColumn<>("Date of Expiry");
		colSchool.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue() instanceof Student ? ((Student)cellData.getValue()).getSchool() : "-"));
		colSchool.setPrefWidth(200);
		TableColumn<Member, String> colCitation = new TableColumn<>("Citation");
		colCitation.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue() instanceof Lifetime ? ((Lifetime)cellData.getValue()).displayCitation() : "-"));
		tblView.setItems(ObList);	
		tblView.getColumns().addAll(colID,colName,colCate,colDate,colSchool,colCitation);
		addButtonToTable(this);
		filterTable();
	}



	/*---------------------------Clear table for setup-------------------------- */
	public void clearTable() {
		tblView.getItems().clear();
		tblView.getColumns().clear();
	}

	/*---------------------------Configuration of items in this pane-------------------------- */

	private void initObjects(){
		searchtf.setText("");
		tblView = new TableView<Member>();
		vbManageMember = new VBox();

		setID();
		addChildToPPane();
		Scene s = new Scene(vbManageMember);
		s.getStylesheets().add(getClass().getResource("mainStyle.css").toExternalForm());
	}

	private void addChildToPPane() {
		searchtf.setPrefColumnCount(20);
		filterPane.getChildren().addAll(searchlbl,searchtf);
		tableAndStuff.getChildren().addAll(tblView);
		fieldAndTablePane.getChildren().addAll(filterPane,tableAndStuff);
		vbManageMember.getChildren().addAll(manageHeader,fieldAndTablePane);

	}

	/*---------------------------Sets up ID-------------------------- */
	private void setID() {
		vbManageMember.setId("managePane");
		manageHeader.setId("manageHeader");
		tableAndStuff.setId("tableVB");
		searchlbl.setId("filterLabel");
		filterPane.setId("filterPane");
	}





	/*---------------------------Method for filtering base on textinput after key is entered.-------------------------- */

	private void filterTable() {
		FilteredList<Member> filterData = new FilteredList<>(ObList, e -> true);
		searchtf.setOnKeyReleased(e -> {
			searchtf.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filterData.setPredicate((Predicate<? super Member>) m -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					else if(m.getId().contains(newValue)) {
						return true;
					}
					else if(m.getName().toLowerCase().contains(newValue.toLowerCase())) {
						return true;
					}
					return false;
				});
			});
			SortedList<Member> sortData = new SortedList<>(filterData);
			sortData.comparatorProperty().bind(tblView.comparatorProperty());
			tblView.setItems(sortData);	
		});

	} 



	/*---------------------------Adding a button for each row in the table-------------------------- */

	private void addButtonToTable(ManageMemberFX m) {
		TableColumn<Member, Void> colBtn = new TableColumn<>("Actions");
		Callback<TableColumn<Member, Void>, TableCell<Member, Void>> cellFactory = new Callback<TableColumn<Member, Void>, TableCell<Member, Void>>() {
			@Override
			public TableCell<Member, Void> call(final TableColumn<Member, Void> param) {
				final TableCell<Member, Void> cell = new TableCell<Member, Void>() {
					private Button btn = new Button("Manage");
					{
						btn.setId("btnManageTable");
						btn.setOnAction((ActionEvent event) -> {
							memberSelected = getTableView().getItems().get(getIndex());
							ModifyMemberFX.setUser(memberSelected);
							ModifyMemberFX.mm = m;
							new ModifyMemberFX().start(new Stage());
						});
					}
					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		};
		colBtn.setCellFactory(cellFactory);
		tblView.getColumns().add(colBtn);
	}

	/*---------------------------Retrieves Pane for mainApp to use and sets up observable list-------------------------- */
	public VBox getPane() {
		return vbManageMember;
	}
	public void setObList(ArrayList<Member> m ) {
		ObList = FXCollections.observableList(m);	
	}
}
