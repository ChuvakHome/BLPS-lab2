package ru.itmo.se.bl.lab2.service;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.itmo.se.bl.lab2.entity.Hotel;
import ru.itmo.se.bl.lab2.entity.Travel;
import ru.itmo.se.bl.lab2.exception.HotelBookException;
import ru.itmo.se.bl.lab2.exception.HotelNotFoundException;
import ru.itmo.se.bl.lab2.exception.TravelBookException;
import ru.itmo.se.bl.lab2.exception.TravelNotFoundException;
import ru.itmo.se.bl.lab2.repository.HotelRepository;

@Service
public class HotelService {
	private final HotelRepository repo;

	@Autowired
	public HotelService(HotelRepository repo) {
		this.repo = repo;
	}
	
	public List<Hotel> getAll() {
		return repo.findAll();
	}
	
	public Hotel getById(Integer id) throws HotelNotFoundException {
		return repo.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
	}
	
	public List<Hotel> getByCountryId(Integer countryId) {
		return repo.findByCountryId(countryId);
	}
	
	public List<Hotel> getByCityId(Integer cityId) {
		return repo.findByCityId(cityId);
	}

	public void bookHotel(Integer id) throws HotelNotFoundException, HotelBookException {
		getById(id);
		Random random = new Random();

		for (int i = 0; i < 5; ++i) {
			if (random.nextInt(10) > 2)
				return;
		}

		throw new HotelBookException();
	}

	public boolean removeHotel(Integer id) {
		if (repo.findById(id).isPresent()) {
			repo.deleteById(id);

			return true;
		}
		else
			return false;
	}
}
