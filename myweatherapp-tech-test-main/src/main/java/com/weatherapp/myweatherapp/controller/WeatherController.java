package com.weatherapp.myweatherapp.controller;

import java.time.Duration;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;

@Controller
public class WeatherController {

  @Autowired
  public WeatherService weatherService;

  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {

    CityInfo ci = weatherService.forecastByCity(city);

    return ResponseEntity.ok(ci);
  }

  // TODO: given two city names, compare the length of the daylight hours and return the city with the longest day
  @GetMapping("/compareDaylight/{city1}/{city2}")
  public ResponseEntity<String> compareDayLight(@PathVariable("city1") String city1,@PathVariable("city2") String city2) {
      
      try{
    	  // Retrieve the information of the cities
          CityInfo cityInfo1 = weatherService.forecastByCity(city1);
          CityInfo cityInfo2 = weatherService.forecastByCity(city2);

          //cases where city is null
          if (cityInfo1 == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found: " + city1);
          }

          if (cityInfo2 == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found: " + city2);
          }

          // parsing the output into localtime
          LocalTime sunRise1;
          if (cityInfo1.getCurrentConditions().getSunrise() == null) {
        	  sunRise1 = LocalTime.MIDNIGHT;
          }else {
        	  sunRise1 = LocalTime.parse(cityInfo1.getCurrentConditions().getSunrise());
          }
          
          
          LocalTime sunSet1;
          if (cityInfo1.getCurrentConditions().getSunset() == null) {
        	  sunSet1 = LocalTime.MAX;
          }else {
        	  sunSet1 = LocalTime.parse(cityInfo1.getCurrentConditions().getSunset());
          }
    
          // daylight hours of city1
          Duration dayLight1 = Duration.between(sunRise1, sunSet1);

          


          
          LocalTime sunRise2;
          if (cityInfo2.getCurrentConditions().getSunrise() == null) {
        	  sunRise2 = LocalTime.MIDNIGHT;
          }else {
        	  sunRise2 = LocalTime.parse(cityInfo2.getCurrentConditions().getSunrise());
          }
          
          
          LocalTime sunSet2;
          if (cityInfo2.getCurrentConditions().getSunset() == null) {
        	  sunSet2 = LocalTime.MAX;
          }else {
        	  sunSet2 = LocalTime.parse(cityInfo2.getCurrentConditions().getSunset());
          }
          Duration dayLight2 = Duration.between(sunRise2,sunSet2);

          //deals with all cases
          String result = null;
          if (dayLight1.compareTo(dayLight2) > 0){
            result = cityInfo1.getAddress();
          }else if (dayLight1.compareTo(dayLight2) < 0){
            result = cityInfo2.getAddress();
          }else{
            result = "Both cities have the same daylight hours";
          }

        return ResponseEntity.ok(result);
      } catch (Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error comparing daylight hours " + e.getMessage());
      }
    }

  // TODO: given two city names, check which city its currently raining in
  @GetMapping("/currentlyRaining/{city1}/{city2}")
  public ResponseEntity<String> currentlyRaining(@PathVariable("city1") String city1, @PathVariable("city2") String city2){
    try{
	    String result = null;
      
	    //Retrieving the information of the two cities
	    CityInfo cityInfo1 = weatherService.forecastByCity(city1);
	    CityInfo cityInfo2 = weatherService.forecastByCity(city2);

	    if (cityInfo1 == null){
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found: " + city1);
	    }

	    if (cityInfo2 == null){
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found: " + city2);
	    }
      
	    //check if any of the cities are raining
	    boolean isRainingCity1 = cityInfo1.getCurrentConditions().getConditions().toLowerCase().contains("rain");
	    boolean isRainingCity2 = cityInfo2.getCurrentConditions().getConditions().toLowerCase().contains("rain");
      
      
	    if (isRainingCity1 & isRainingCity2) {
		    result = cityInfo1.getAddress()  + " and " + cityInfo2.getAddress();
	    }else if (isRainingCity1) {
		    result = cityInfo1.getAddress();
	    }else if (isRainingCity2) {
		    result = cityInfo2.getAddress();
	    }else {
		    result = "None of the cities are raining";
	    }
    
	    return ResponseEntity.ok(result);
    }catch(Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking rain conditions: " + e.getMessage());
    }
  }
  





}
