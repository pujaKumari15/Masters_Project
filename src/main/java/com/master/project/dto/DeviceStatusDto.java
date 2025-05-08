package com.master.project.dto;

public class DeviceStatusDto {
	private Boolean power;
	private Boolean motion;
	private Double brightness;
	private Double temperature;
	private Double humidity;

	public Boolean getPower() { return power; }
	public Boolean getDetected() { return motion; }
	public Double getBrightness() { return brightness; }
	public Double getTemperature() { return temperature; }
	public Double getHumidity() { return humidity; }

	public void setPower(Boolean power) { this.power = power; }
	public void setDetected(Boolean motion) { this.motion = motion; }
	public void setBrightness(Double brightness) { this.brightness = brightness; }
	public void setTemperature(Double temperature) { this.temperature = temperature; }
	public void setHumidity(Double humidity) { this.humidity = humidity; }
}
