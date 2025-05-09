package com.master.project.dto;

public class DeviceStatusDto {
    private Boolean power;
    private Boolean motion;
    private Double brightness;
    private String color;
    private Double colorTemperature;
    private Double temperature;
    private Double humidity;
    private Double speed;
    private String mode;
    private Double targetTemperature;
    private Double targetHumidity;
    private Double lux;
    private Double batteryLevel;
    private Double powerConsumption;

    // Getters
    public Boolean getPower() { return power; }
    public Boolean getMotion() { return motion; }
    public Double getBrightness() { return brightness; }
    public String getColor() { return color; }
    public Double getColorTemperature() { return colorTemperature; }
    public Double getTemperature() { return temperature; }
    public Double getHumidity() { return humidity; }
    public Double getSpeed() { return speed; }
    public String getMode() { return mode; }
    public Double getTargetTemperature() { return targetTemperature; }
    public Double getTargetHumidity() { return targetHumidity; }
    public Double getLux() { return lux; }
    public Double getBatteryLevel() { return batteryLevel; }
    public Double getPowerConsumption() { return powerConsumption; }

    // Setters
    public void setPower(Boolean power) { this.power = power; }
    public void setMotion(Boolean motion) { this.motion = motion; }
    public void setBrightness(Double brightness) { this.brightness = brightness; }
    public void setColor(String color) { this.color = color; }
    public void setColorTemperature(Double colorTemperature) { this.colorTemperature = colorTemperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }
    public void setSpeed(Double speed) { this.speed = speed; }
    public void setMode(String mode) { this.mode = mode; }
    public void setTargetTemperature(Double targetTemperature) { this.targetTemperature = targetTemperature; }
    public void setTargetHumidity(Double targetHumidity) { this.targetHumidity = targetHumidity; }
    public void setLux(Double lux) { this.lux = lux; }
    public void setBatteryLevel(Double batteryLevel) { this.batteryLevel = batteryLevel; }
    public void setPowerConsumption(Double powerConsumption) { this.powerConsumption = powerConsumption; }
}
