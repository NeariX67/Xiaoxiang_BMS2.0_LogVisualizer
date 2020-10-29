package com.nearix.visualizer;

import java.io.File;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Visualizer extends Application {

	File csvFile;

	ComboBox<String> graphTypeCB;
	ComboBox<Integer> selectorCB;

	LineChart<Number, Number> lineChart;
	Series<Number, Number> series;

	NumberAxis yAxis;
	NumberAxis xAxis;
	
	SpinnerValueFactory<Integer> svfStart = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1, 0);
	SpinnerValueFactory<Integer> svfEnd = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1, 0);
	Spinner<Integer> startSpinner = new Spinner<Integer>(svfStart);
	Spinner<Integer> endSpinner = new Spinner<Integer>(svfEnd);

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {

		stage.setTitle("JavaFX Test application");
		stage.setWidth(1280);
		stage.setHeight(720);
		stage.show();

		BorderPane bPane = new BorderPane();

		HBox spinnerBox = new HBox();
		
//		BorderPane.setAlignment(pickerBox, Pos.TOP_CENTER);
//	    BorderPane.setMargin(pickerBox, new Insets(12,12,12,12));
		HBox.setMargin(startSpinner, new Insets(12, 12, 12, 12));
		HBox.setMargin(endSpinner, new Insets(12, 12, 12, 12));
		startSpinner.setEditable(true);
		endSpinner.setEditable(true);
		spinnerBox.setAlignment(Pos.CENTER);
		spinnerBox.getChildren().addAll(startSpinner, endSpinner);

		ObservableList<String> options = FXCollections.observableArrayList("Voltage", "Charging current",
				"Discharging current", "Power", "remaining Ah", "Percentage (SOC)", "Temperature", "Cell Voltage");
		graphTypeCB = new ComboBox<>(options);
		graphTypeCB.setValue("Voltage");
		selectorCB = new ComboBox<>();

		FileChooser logFC = new FileChooser();
		Button btOpen = new Button("Open");

		HBox.setMargin(graphTypeCB, new Insets(12, 12, 12, 12));
		HBox.setMargin(selectorCB, new Insets(12, 12, 12, 12));
		HBox.setMargin(btOpen, new Insets(12, 12, 12, 12));
		spinnerBox.getChildren().addAll(graphTypeCB, selectorCB, btOpen);

		// -----------LineChart------------//
		yAxis = new NumberAxis();
		xAxis = new NumberAxis();
		xAxis.setLabel("Measurement number");
		yAxis.setLabel("Voltage");
		yAxis.setAutoRanging(false);
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		lineChart.setTitle("Smart BMS readings");
		series = new XYChart.Series<>();
		lineChart.getData().add(series);
		lineChart.setCreateSymbols(false);
		lineChart.setAnimated(false);

		bPane.setTop(spinnerBox);
		bPane.setCenter(lineChart);
		stage.setScene(new Scene(bPane, 300, 400));

		csvFile = new File("C:\\Users\\Kuehner\\Downloads\\Xiaoxia.csv");
		loadCSV();
		series.setName(csvFile.getName().replace(".csv", ""));
		reloadData();
		updateBounds();
		updateValueFactory(false, 0);

		btOpen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				csvFile = logFC.showOpenDialog(stage);
				if (csvFile != null) {
					loadCSV();
					series.setName(csvFile.getName().replace(".csv", ""));
					reloadData();
				}
			}
		});

		graphTypeCB.getSelectionModel().selectedItemProperty().addListener((x, oldValue, newValue) -> {
			reloadData();
			updateBounds();
		});

		selectorCB.getSelectionModel().selectedItemProperty().addListener((x, oldValue, newValue) -> {
			reloadData();
		});

		startSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
			updateValueFactory(true, newValue);
		});

		endSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
			updateValueFactory(false, newValue);
		});
	}

	void loadCSV() {
		csvParser.parseData(filemanager.readFile(csvFile));
		selectorCB.getItems().clear();
		if (graphTypeCB.getValue().equals("Cell Voltage")) {
			selectorCB.setVisible(true);
			for (int i = 1; i <= csvParser.cellCount; i++) {
				selectorCB.getItems().add(i);
			}
			selectorCB.setValue(1);
		} else if (graphTypeCB.getValue().equals("Temperature")) {
			selectorCB.setVisible(true);
			for (int i = 1; i <= csvParser.temperatureCount; i++) {
				selectorCB.getItems().add(i);
			}
			selectorCB.setValue(1);
		} else {
			selectorCB.setVisible(false);
		}
	}

	void reloadData() {
		series.getData().clear();
		switch (graphTypeCB.getValue()) {
		case "Voltage":
			selectorCB.setVisible(false);
			for (int i = 0; i < csvParser.dataArray.size(); i++) {
				series.getData().add(new XYChart.Data<Number, Number>(i, csvParser.dataArray.get(i).getTotalVoltage()));
			}

			break;
		case "Charging current":
			selectorCB.setVisible(false);
			for (int i = 0; i < csvParser.dataArray.size(); i++) {
				series.getData()
						.add(new XYChart.Data<Number, Number>(i, csvParser.dataArray.get(i).getChargingCurrent()));
			}

			break;
		case "Discharging current":
			selectorCB.setVisible(false);
			for (int i = 0; i < csvParser.dataArray.size(); i++) {
				series.getData()
						.add(new XYChart.Data<Number, Number>(i, csvParser.dataArray.get(i).getDischargingCurrent()));
			}

			break;
		case "Power":
			selectorCB.setVisible(false);
			for (int i = 0; i < csvParser.dataArray.size(); i++) {
				series.getData()
						.add(new XYChart.Data<Number, Number>(i, csvParser.dataArray.get(i).getDischargingCurrent()
								* csvParser.dataArray.get(i).getTotalVoltage()));
			}

			break;
		case "remaining Ah":
			selectorCB.setVisible(false);
			for (int i = 0; i < csvParser.dataArray.size(); i++) {
				series.getData().add(new XYChart.Data<Number, Number>(i, csvParser.dataArray.get(i).getRemainingAh()));
			}

			break;
		case "Percentage (SOC)":
			selectorCB.setVisible(false);
			for (int i = 0; i < csvParser.dataArray.size(); i++) {
				series.getData().add(new XYChart.Data<Number, Number>(i, csvParser.dataArray.get(i).getSoc()));
			}

			break;
		case "Temperature":
			reloadSelector(csvParser.temperatureCount);
			for (int i = 0; i < csvParser.dataArray.size(); i++) {
				series.getData().add(new XYChart.Data<Number, Number>(i,
						csvParser.dataArray.get(i).getTemperature(selectorCB.getValue() - 1)));
			}

			break;
		case "Cell Voltage":
			reloadSelector(csvParser.cellCount);
			for (int i = 0; i < csvParser.dataArray.size(); i++) {
				series.getData().add(new XYChart.Data<Number, Number>(i,
						csvParser.dataArray.get(i).getVoltage(selectorCB.getValue() - 1)));
			}

			break;
		default:
			System.out.println("Not found: " + graphTypeCB.getValue());
		}

	}

	void reloadSelector(int values) {
		selectorCB.setVisible(true);
		if (selectorCB.getValue() == null || selectorCB.getValue() == 0) {
			selectorCB.getItems().clear();
			for (int i = 1; i <= values; i++) {
				selectorCB.getItems().add(i);
			}
			selectorCB.setValue(1);
		} else if (!(selectorCB.getValue() > 0) || selectorCB.getItems().size() != values) {
			selectorCB.getItems().clear();
			for (int i = 1; i <= values; i++) {
				selectorCB.getItems().add(i);
			}
			selectorCB.setValue(1);
		}
	}
	
	void updateValueFactory(Boolean isStart, int value) {
		System.out.println("UpdateValueFactory: " + value);
		if(isStart) {
			startSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, csvParser.dataArray.size()-1));
			startSpinner.getValueFactory().setValue(value+1);
			if(value >= endSpinner.getValue()) {
				endSpinner.getValueFactory().setValue(startSpinner.getValue()+1);
				System.out.println("EndValue: " + endSpinner.getValue());
			}
			return;
		}
		endSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, csvParser.dataArray.size()));
		endSpinner.getValueFactory().setValue(value+1);
	}

	void updateBounds() {
		final NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
		yAxis.setLabel(graphTypeCB.getValue());
		switch (graphTypeCB.getValue()) {
		case "Voltage":
			yAxis.setLowerBound(2.9 * csvParser.cellCount);
			yAxis.setUpperBound(4.3 * csvParser.cellCount);
			yAxis.setTickUnit(1);
			break;
		case "Charging current":
			yAxis.setLowerBound(0.0);
			yAxis.setUpperBound(csvParser.getMaxChargingCurrent() * 1.05);
			yAxis.setTickUnit(Math.round(csvParser.getMaxChargingCurrent() / 10));
			break;
		case "Discharging current":
			yAxis.setLowerBound(0.0);
			yAxis.setUpperBound(csvParser.getMaxDischargingCurrent() * 1.05);
			yAxis.setTickUnit(Math.round(csvParser.getMaxDischargingCurrent() / 10));
			break;
		case "Power":
			yAxis.setLowerBound(0.0);
			yAxis.setUpperBound(csvParser.getMaxVoltage() * csvParser.getMaxDischargingCurrent());
			yAxis.setTickUnit(Math.round(csvParser.getMaxVoltage() * csvParser.getMaxDischargingCurrent() / 10));
			break;
		case "remaining Ah":
			yAxis.setLowerBound(csvParser.getMinRemainingCap() * 0.95);
			yAxis.setUpperBound(csvParser.getMaxRemainingCap() * 1.05);
			yAxis.setTickUnit(Math.round((csvParser.getMaxRemainingCap() - csvParser.getMinRemainingCap()) / 10));
			break;
		case "Percentage (SOC)":
			yAxis.setLowerBound(0.0);
			yAxis.setUpperBound(100.0);
			yAxis.setTickUnit(10);
			break;
		case "Temperature":
			yAxis.setLowerBound(csvParser.getMinTemp() * 0.95);
			yAxis.setUpperBound(csvParser.getMaxTemp() * 1.05);
			yAxis.setTickUnit(Math.round((csvParser.getMaxTemp() - csvParser.getMinTemp()) / 100));
			break;
		case "Cell Voltage":
			yAxis.setLowerBound(2.9);
			yAxis.setUpperBound(4.3);
			yAxis.setTickUnit(0.1);
			break;
		default:
			System.out.println("Not found: " + graphTypeCB.getValue());
		}
	}

}
