package com.nearix.visualizer;

import java.util.ArrayList;

public class csvParser {

	private static String header = "";
	public static int temperatureCount = 0;
	public static int cellCount = 0;
	public static ArrayList<CSVData> dataArray = new ArrayList<>();
	
	public static void parseData(ArrayList<String> data) {
		if(data.size() > 1) {
			header = data.get(0);
			parseHeader();
			for(int i = 1; i < data.size();i++) {
				if(!data.get(i).contains("totalVoltage")) {
					dataArray.add(parseLine(data.get(i)));
				}
			}
		}
	}
	
	public static Double getMaxChargingCurrent() {
		Double max = 0.0;
		for(int i = 0;i < dataArray.size();i++) {
			if(dataArray.get(i).getChargingCurrent() > max) {
				max = dataArray.get(i).getChargingCurrent();
			}
		}
		return max;
	}
	
	public static Double getMaxDischargingCurrent() {
		Double max = 0.0;
		for(int i = 0;i < dataArray.size();i++) {
			if(dataArray.get(i).getDischargingCurrent() > max) {
				max = dataArray.get(i).getDischargingCurrent();
			}
		}
		return max;
	}
	
	public static Double getMaxVoltage() {
		Double max = 0.0;
		for(int i = 0;i < dataArray.size();i++) {
			if(dataArray.get(i).getTotalVoltage() > max) {
				max = dataArray.get(i).getTotalVoltage();
			}
		}
		return max;
	}
	
	public static Double getMaxRemainingCap() {
		Double max = 0.0;
		for(int i = 0;i < dataArray.size();i++) {
			if(dataArray.get(i).getRemainingAh() > max) {
				max = dataArray.get(i).getRemainingAh();
			}
		}
		return max;
	}
	
	public static Double getMinRemainingCap() {
		Double min = 99999.0;
		for(int i = 0;i < dataArray.size();i++) {
			if(dataArray.get(i).getRemainingAh() < min) {
				min = dataArray.get(i).getRemainingAh();
			}
		}
		return min;
	}
	
	
	public static Double getMaxTemp() {
		Double max = 0.0;
		for(int i = 0;i < dataArray.size();i++) {
			for(int j = 0;j < temperatureCount;j++) {
				if(dataArray.get(i).getTemperature(j) > max) {
					max = dataArray.get(i).getTemperature(j);
				}
			}
		}
		return max;
	}
	
	public static Double getMinTemp() {
		Double min = 100.0;
		for(int i = 0;i < dataArray.size();i++) {
			for(int j = 0;j < temperatureCount;j++) {
				if(dataArray.get(i).getTemperature(j) < min) {
					min = dataArray.get(i).getTemperature(j);
				}
			}
		}
		return min;
	}
	
	private static CSVData parseLine(String line) {
		CSVData tmp = new CSVData(cellCount, temperatureCount);
		String[] dataArr = line.split(";");
		tmp.setReadableTimestamp(dataArr[0]);
		tmp.setTotalVoltage(Double.parseDouble(dataArr[1].replace(",", ".")));
		tmp.setChargingCurrent(Double.parseDouble(dataArr[2].replace(",", ".")));
		tmp.setDischargingCurrent(Double.parseDouble(dataArr[3].replace(",", ".")));
		tmp.setRemainingAh(Double.parseDouble(dataArr[4].replace(",", ".")));
		tmp.setSoc(Integer.parseInt(dataArr[5]));
		for(int i = 0; i < temperatureCount;i++) {
			tmp.setTemperature(i, Double.parseDouble(dataArr[6+i].replace(",", ".")));
		}
		for(int i = 0; i < cellCount;i++) {
			tmp.setVoltage(i, Double.parseDouble(dataArr[6+temperatureCount+i].replace(",", ".")));
		}
		return tmp;
	}
	
	private static void parseHeader() {
		String[] headerArr = header.split(";");
		int maxTempID = 0;
		int maxCellID = 0;
		if(headerArr.length > 6) {
			for(int i = 6; i < headerArr.length;i++) {
				if(headerArr[i].startsWith("t")) {
					int tempID = Integer.parseInt(headerArr[i].replace("t", ""));
					if(tempID > maxTempID) {
						maxTempID = tempID;
					}
				}
				else if(headerArr[i].startsWith("c")) {
					int cellID = Integer.parseInt(headerArr[i].replace("c", ""));
					if(cellID > maxCellID) {
						maxCellID = cellID;
					}
				}
			}
		}
		cellCount = maxCellID;
		temperatureCount = maxTempID;
	}

}
