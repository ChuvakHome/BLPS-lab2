package ru.itmo.se.bl.lab2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ru.itmo.se.bl.lab2.controller.CityController;
import ru.itmo.se.bl.lab2.controller.CountryController;
import ru.itmo.se.bl.lab2.controller.HotelController;
import ru.itmo.se.bl.lab2.controller.TourController;

@SpringBootApplication
public class BLLab2 {
	public static void main(String[] args) {
		SpringApplication.run(new Class[] {
				BLLab2.class,
				CountryController.class,
				CityController.class,
				HotelController.class,
				TourController.class,
			}, args);
	}
}
