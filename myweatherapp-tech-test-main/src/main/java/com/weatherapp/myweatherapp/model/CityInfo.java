package com.weatherapp.myweatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// Added getters and setters method in this change

public class CityInfo {

  @JsonProperty("address")
  String address;
  
  public String getAddress() {
	  return address;
  }
  
  public void setAddress(String city) {
	  address = city;
	  return;
  }
  
  

  @JsonProperty("description")
  String description;

  @JsonProperty("currentConditions")
  CurrentConditions currentConditions;
  
  public CurrentConditions getCurrentConditions() {
	  return currentConditions;
  }
  
  public void setCurrentConditions(CurrentConditions c) {
	  currentConditions = c;
	  return;
  }

  @JsonProperty("days")
  List<Days> days;

  public static class CurrentConditions {
    @JsonProperty("temp")
    String currentTemperature;

    @JsonProperty("sunrise")
    String sunrise;
    
    public String getSunrise() {
    	return sunrise;
    }
    
    public void setSunrise(String time) {
    	sunrise = time;
    	return;
    }

    @JsonProperty("sunset")
    String sunset;
    
    public String getSunset() {
    	return sunset;
    }
    
    public void setSunset(String time) {
    	sunset = time;
    	return;
    }

    @JsonProperty("feelslike")
    String feelslike;

    @JsonProperty("humidity")
    String humidity;

    @JsonProperty("conditions")
    String conditions;
    
    public String getConditions() {
    	return conditions;
    }
    
    public void setConditions(String condition) {
    	conditions = condition;
    	return;
    }
  }

  static class Days {

    @JsonProperty("datetime")
    String date;

    @JsonProperty("temp")
    String currentTemperature;

    @JsonProperty("tempmax")
    String maxTemperature;

    @JsonProperty("tempmin")
    String minTemperature;

    @JsonProperty("conditions")
    String conditions;

    @JsonProperty("description")
    String description;

  }

}
