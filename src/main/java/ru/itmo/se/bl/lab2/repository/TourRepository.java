package ru.itmo.se.bl.lab2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.itmo.se.bl.lab2.entity.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
	List<Tour> findByTourCityName(String cityName);
}
