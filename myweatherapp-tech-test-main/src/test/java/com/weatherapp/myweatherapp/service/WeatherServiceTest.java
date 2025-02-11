package com.weatherapp.myweatherapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.model.CityInfo.CurrentConditions;
import com.weatherapp.myweatherapp.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import com.weatherapp.myweatherapp.controller.WeatherController;




class WeatherServiceTest {
	
	private WeatherController weatherController;
    private WeatherService weatherService;
    
    
    @BeforeEach
    void setUp() {
        weatherService = mock(WeatherService.class);
        weatherController = new WeatherController();
        weatherController.weatherService = weatherService;
    }
    
    // Helper method to build a dummy CityInfo instance.
    private CityInfo createCityInfo(String address, String sunrise, String sunset, String conditions) {
        CityInfo cityInfo = new CityInfo();
        cityInfo.setAddress(address);
        CurrentConditions cc = new CurrentConditions();
        cc.setSunrise(sunrise);
        cc.setSunset(sunset);
        cc.setConditions(conditions);
        cityInfo.setCurrentConditions(cc);
        return cityInfo;
    }
  
    // TODO: 12/05/2023 write unit tests
    //Testing the case where city1 has longer daylight hours
	@Test
	void testCompareDayLight_city1Longer() {
        CityInfo cityInfo1 = createCityInfo("CityOne", "00:00", "20:00", "clear");
        CityInfo cityInfo2 = createCityInfo("CityTwo", "00:00", "18:00", "clear");
        when(weatherService.forecastByCity("CityOne")).thenReturn(cityInfo1);
        when(weatherService.forecastByCity("CityTwo")).thenReturn(cityInfo2);

        ResponseEntity<String> response = weatherController.compareDayLight("CityOne", "CityTwo");
        
        assertEquals(200, response.getStatusCode().value());
        assertEquals("CityOne", response.getBody());
    }
	// Testing the case where both cities have equal daylight hours
	@Test
    void testCompareDayLight_equalDaylight() {
        CityInfo cityInfo1 = createCityInfo("CityOne", "00:00", "18:00", "clear");
        CityInfo cityInfo2 = createCityInfo("CityTwo", "00:00", "18:00", "clear");
        when(weatherService.forecastByCity("CityOne")).thenReturn(cityInfo1);
        when(weatherService.forecastByCity("CityTwo")).thenReturn(cityInfo2);

        ResponseEntity<String> response = weatherController.compareDayLight("CityOne", "CityTwo");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Both cities have the same daylight hours", response.getBody());
    }
	
	// Testing the case where the city cannot be found from the list
	@Test
    void testCompareDayLight_cityNotFound() {
        when(weatherService.forecastByCity("CityOne")).thenReturn(null);

        ResponseEntity<String> response = weatherController.compareDayLight("CityOne", "CityTwo");

        assertEquals(404, response.getStatusCode().value());
        assertTrue(response.getBody().contains("City not found: CityOne"));
    }
	
	// When both cities are raining
	@Test
    void testCurrentlyRaining_bothRaining() {
        CityInfo cityInfo1 = createCityInfo("CityOne", "00:00", "18:00", "rainy conditions");
        CityInfo cityInfo2 = createCityInfo("CityTwo", "00:00", "18:00", "rain storm");
        when(weatherService.forecastByCity("CityOne")).thenReturn(cityInfo1);
        when(weatherService.forecastByCity("CityTwo")).thenReturn(cityInfo2);

        ResponseEntity<String> response = weatherController.currentlyRaining("CityOne", "CityTwo");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("CityOne and CityTwo", response.getBody());
    }
	// when one of the city is raining
	@Test
    void testCurrentlyRaining_oneRaining() {
        CityInfo cityInfo1 = createCityInfo("CityOne", "00:00", "18:00", "rain showers");
        CityInfo cityInfo2 = createCityInfo("CityTwo", "00:00", "18:00", "sunny");
        when(weatherService.forecastByCity("CityOne")).thenReturn(cityInfo1);
        when(weatherService.forecastByCity("CityTwo")).thenReturn(cityInfo2);

        ResponseEntity<String> response = weatherController.currentlyRaining("CityOne", "CityTwo");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("CityOne", response.getBody());
    }
	//none of the cities are raining
	@Test
    void testCurrentlyRaining_noneRaining() {
        CityInfo cityInfo1 = createCityInfo("CityOne", "00:00", "18:00", "cloudy");
        CityInfo cityInfo2 = createCityInfo("CityTwo", "00:00", "18:00", "sunny");
        when(weatherService.forecastByCity("CityOne")).thenReturn(cityInfo1);
        when(weatherService.forecastByCity("CityTwo")).thenReturn(cityInfo2);

        ResponseEntity<String> response = weatherController.currentlyRaining("CityOne", "CityTwo");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("None of the cities are raining", response.getBody());
    }
	// some cities do not have sunrise and sunset
	// In this case I have assumed that the daylight hours are 24
	@Test
	void dayLightNull() {
		CityInfo cityInfo1 = createCityInfo("CityOne", null, null, "clear");
        CityInfo cityInfo2 = createCityInfo("CityTwo", null, "18:00", "clear");
        when(weatherService.forecastByCity("CityOne")).thenReturn(cityInfo1);
        when(weatherService.forecastByCity("CityTwo")).thenReturn(cityInfo2);

        ResponseEntity<String> response = weatherController.compareDayLight("CityOne", "CityTwo");
        
        assertEquals(200, response.getStatusCode().value());
        assertEquals("CityOne", response.getBody());
	}
	
	@Test
	void testCurrentlyRaining_null() {
        CityInfo cityInfo1 = createCityInfo("CityOne", "06:00", "18:00", "");
        CityInfo cityInfo2 = createCityInfo("CityTwo", "06:00", "18:00", "");
        when(weatherService.forecastByCity("CityOne")).thenReturn(cityInfo1);
        when(weatherService.forecastByCity("CityTwo")).thenReturn(cityInfo2);

        ResponseEntity<String> response = weatherController.currentlyRaining("CityOne", "CityTwo");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("None of the cities are raining", response.getBody());
    }
	
}