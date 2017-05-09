package restructo.main;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import restructo.robot.context.WorkspaceContext;
import restructo.robot.doc.*;

public class Restructo extends Application {

	private WorkspaceContext workspace;

	private Button buttonScan = new Button("Scan");
	private Button buttonGenerate = new Button("Generate");
	private TableView<ObservableList<String>> primaryTable = new TableView<>();
	private TextField uriField = new TextField();
	private ProgressBar progressBar = new ProgressBar();
	private TableColumn<ObservableList<String>, String> cNumber = new TableColumn<>("No");
	private TableColumn<ObservableList<String>, String> cType = new TableColumn<>("Type");
	private TableColumn<ObservableList<String>, String> cName = new TableColumn<>("Name");
	private TableColumn<ObservableList<String>, String> cOrigin = new TableColumn<>("Original Location");
	private TableColumn<ObservableList<String>, String> cNewLoc = new TableColumn<>("New Page Document");
	private TableColumn<ObservableList<String>, String> cValue = new TableColumn<>("Value");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Restructo");
		initTable();

		uriField.setText("Type robot root folder URI here");
		uriField.setFont(Font.font("Tahoma"));
		uriField.setPrefSize(900, 36);
		uriField.setEditable(true);

		progressBar.setPrefSize(90, 27);
		progressBar.setProgress(1);
		progressBar.setStyle("-fx-accent: #02e830;");

		HBox upperBox = new HBox();
		upperBox.setPadding(new Insets(15, 15, 15, 15));
		upperBox.setSpacing(10);
		upperBox.setStyle("-fx-background-color: #336699;");
		upperBox.getChildren().addAll(getButtonScan(), getButtonGenerate(), uriField, progressBar);

		HBox body = new HBox();
		body.setPadding(new Insets(15, 15, 15, 15));
		body.setSpacing(10);
		body.getChildren().add(primaryTable);

		GridPane outerPane = new GridPane();
		outerPane.setHgap(9);
		outerPane.setVgap(9);
		outerPane.add(upperBox, 0, 0);
		outerPane.add(body, 0, 1);

		Scene scene = new Scene(outerPane, 1170, 630);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private Button getButtonScan() {
		buttonScan.setFont(Font.font("Tahoma"));
		buttonScan.setPrefSize(72, 36);
		buttonScan.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				File folder = new File(uriField.getText());
				Thread t = new Thread() {
					public void run() {
						double value = 0;
						while (WorkspaceContext.PROGRESS != 0.9) {
							if (value != WorkspaceContext.PROGRESS) {
								value = WorkspaceContext.PROGRESS;
								setProgressBar(value);
							}
						}
						setProgressBar(0.9);
					}
				};
				t.start();
				buttonScan.setDisable(true);
				buttonGenerate.setDisable(true);
				try {
					workspace = WorkspaceContext.parseWorkspace(folder);
					setTable(workspace);
					setProgressBar(1);
				} catch (Exception e) {
					e.printStackTrace();
					t.interrupt();
					uriField.setText("FAILED TO PARSING WORKSPACE!");
					setProgressBar(-1);
				} finally {
					buttonScan.setDisable(false);
					buttonGenerate.setDisable(false);
				}
			}

		});
		return buttonScan;
	}

	protected void setTable(WorkspaceContext workspace) {
		List<List<String>> tableValues = workspaceToTableList(workspace);
		for (int i = 0; i < tableValues.size(); i++) {
			primaryTable.getItems().add(FXCollections.observableArrayList(tableValues.get(i)));
		}
	}

	private List<List<String>> workspaceToTableList(WorkspaceContext workspace) {
		List<List<String>> tableValues = new LinkedList<>();
		Keyword[] keys = workspace.getKeywords();
		Variable[] vars = workspace.getVariables();
		CompositeVariable[] compVars = workspace.getCompVariables();
		PlainLocator[] locators = workspace.getLocators();
		int number = 1;
		for (int i = 0; i < keys.length; i++) {
			tableValues.add(keywordToTableList(keys[i], number));
			number++;
		}
		for (int i = 0; i < vars.length; i++) {
			tableValues.add(variableToTableList(vars[i], number));
			number++;
		}
		for (int i = 0; i < compVars.length; i++) {
			tableValues.add(compVariableToTableList(compVars[i], number));
			number++;
		}
		for (int i = 0; i < locators.length; i++) {
			tableValues.add(locatorToTableList(locators[i], number));
			number++;
		}
		return tableValues;
	}

	private List<String> keywordToTableList(Keyword key, int number) {
		List<String> row = new LinkedList<>();
		row.add(Integer.toString(number));
		row.add("Keyword");
		row.add(key.getName());
		row.add(key.getOrigin().getName());
		row.add("");
		row.add("");
		return row;
	}

	private List<String> variableToTableList(Variable var, int number) {
		List<String> row = new LinkedList<>();
		row.add(Integer.toString(number));
		row.add("Variable");
		row.add(var.getName());
		row.add(var.getOrigin().getName());
		row.add("");
		row.add(var.getValue());
		return row;
	}

	private List<String> compVariableToTableList(CompositeVariable compVar, int number) {
		List<String> row = new LinkedList<>();
		row.add(Integer.toString(number));
		row.add("Keyword");
		row.add(compVar.getName());
		row.add(compVar.getOrigin().getName());
		row.add("");
		row.add(compVar.getMembers().toString());
		return row;
	}

	private List<String> locatorToTableList(PlainLocator locator, int number) {
		List<String> row = new LinkedList<>();
		row.add(Integer.toString(number));
		row.add("Locator");
		row.add("");
		row.add("");
		row.add("");
		row.add(locator.getValue());
		return row;
	}

	private void initTable() {
		cNumber.setPrefWidth(45);
		cNumber.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>((cellData.getValue().get(0))));
		cType.setPrefWidth(81);
		cType.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>((cellData.getValue().get(1))));
		cName.setPrefWidth(207);
		cName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>((cellData.getValue().get(2))));
		cOrigin.setPrefWidth(306);
		cOrigin.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>((cellData.getValue().get(3))));
		cNewLoc.setPrefWidth(306);
		cNewLoc.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>((cellData.getValue().get(4))));
		cValue.setPrefWidth(207);
		cValue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>((cellData.getValue().get(5))));
		primaryTable.setStyle("-fx-font-size: 12; -fx-font-family: \"Tahoma\";");
		primaryTable.setPrefHeight(540);
		primaryTable.setEditable(true);
		primaryTable.getColumns().addAll(cNumber, cType, cName, cOrigin, cNewLoc, cValue);
	}

	private Button getButtonGenerate() {
		buttonGenerate.setFont(Font.font("Tahoma"));
		buttonGenerate.setPrefSize(72, 36);
		buttonGenerate.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {

			}

		});

		return buttonGenerate;
	}

	private void setProgressBar(double value) {
		if (value < 0.25 && value >= 0) {
			progressBar.setStyle("-fx-accent: #e84500;");
		} else if (value < 0.50 && value >= 0) {
			progressBar.setStyle("-fx-accent: #e89600;");
		} else if (value < 0.75 && value >= 0) {
			progressBar.setStyle("-fx-accent: #e8d000;");
		} else if (value < 1 && value >= 0) {
			progressBar.setStyle("-fx-accent: #c5e801;");
		} else if (value == 1 && value >= 0) {
			progressBar.setStyle("-fx-accent: #02e830;");
		} else {
			value = -1;
			progressBar.setStyle("-fx-accent: #e84500;");
		}
		progressBar.setProgress(value);
	}

}
