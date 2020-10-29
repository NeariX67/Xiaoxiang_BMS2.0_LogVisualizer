package com.nearix.visualizer;

public class CSVData {
	
	private String readableTimestamp;
	private int relativeTimeStamp;
	private Double TotalVoltage;
	private Double ChargingCurrent;
	private Double DischargingCurrent;
	private Double remainingAh;
	private int soc;
	private Double[] temperatures;
	private Double[] cellVoltages;
	
	public CSVData(int cellCount, int temperatureCount) {
		temperatures = new Double[temperatureCount];
		cellVoltages = new Double[cellCount];
	}
	
	
	public String getReadableTimestamp() {
		return readableTimestamp;
	}
	public void setReadableTimestamp(String readableTimestamp) {
		this.readableTimestamp = readableTimestamp;
	}
	public int getTimeStamp() {
		return relativeTimeStamp;
	}
	public void setTimeStamp(int timeStamp) {
		this.relativeTimeStamp = timeStamp;
	}
	public Double getTotalVoltage() {
		return TotalVoltage;
	}
	public void setTotalVoltage(Double totalVoltage) {
		TotalVoltage = totalVoltage;
	}
	public Double getChargingCurrent() {
		return ChargingCurrent;
	}
	public void setChargingCurrent(Double chargingCurrent) {
		ChargingCurrent = chargingCurrent;
	}
	public Double getDischargingCurrent() {
		return DischargingCurrent;
	}
	public void setDischargingCurrent(Double dischargingCurrent) {
		DischargingCurrent = dischargingCurrent;
	}
	public Double getRemainingAh() {
		return remainingAh;
	}
	public void setRemainingAh(Double remainingAh) {
		this.remainingAh = remainingAh;
	}
	public int getSoc() {
		return soc;
	}
	public void setSoc(int soc) {
		this.soc = soc;
	}
	public Double getVoltage(int cell) {
		return cellVoltages[cell];
	}
	public void setVoltage(int cell, Double voltage) {
		cellVoltages[cell] = voltage;
	}
	public Double getTemperature(int sensorID) {
		return temperatures[sensorID];
	}
	public void setTemperature(int sensorID, Double temperature) {
		temperatures[sensorID] = temperature;
	}
}
