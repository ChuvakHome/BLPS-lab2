package ru.itmo.se.bl.lab2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.itmo.se.bl.lab2.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
	List<City> findByName(String name);
	
	List<City> findAllByCountryName(String countryName);
}
