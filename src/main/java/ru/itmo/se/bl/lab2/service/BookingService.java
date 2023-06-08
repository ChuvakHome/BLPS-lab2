package ru.itmo.se.bl.lab2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.itmo.se.bl.lab2.entity.Booking;
import ru.itmo.se.bl.lab2.entity.Hotel;
import ru.itmo.se.bl.lab2.entity.Travel;
import ru.itmo.se.bl.lab2.exception.*;
import ru.itmo.se.bl.lab2.model.PaymentRequest;
import ru.itmo.se.bl.lab2.repository.BookingRepository;

@Service
public class BookingService {
	private final TravelService travelService;
	private final HotelService hotelService;
	private final BookingRepository bookingRepository;
	private final PaymentService paymentService;
	private final PlatformTransactionManager txManager;

	@Autowired
	public BookingService(TravelService travelService, HotelService hotelService, BookingRepository bookingRepository, PaymentService paymentService, PlatformTransactionManager transactionManager) {
		this.travelService = travelService;
		this.hotelService = hotelService;
		this.paymentService = paymentService;
		this.bookingRepository = bookingRepository;
		this.txManager = transactionManager;
	}

	public void addBooking(Booking booking, PaymentRequest req) throws HotelBookException, TravelNotFoundException, TravelBookException, HotelNotFoundException, PaymentException {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);

		TransactionStatus status = txManager.getTransaction(def);

		try {
			int travelId = booking.getTravel().getId();
			int hotelId = booking.getHotel().getId();

			Travel travel = travelService.getById(travelId);
			travelService.bookTravel(travelId);
			booking.setTravel(travel);

			Hotel hotel = hotelService.getById(hotelId);
			hotelService.bookHotel(hotelId);
			booking.setHotel(hotel);

			paymentService.doPayment(req);

			bookingRepository.save(booking);
		} catch (TravelNotFoundException | TravelBookException | HotelNotFoundException | HotelBookException | PaymentException e) {
			txManager.rollback(status);

			throw e;
		}

		txManager.commit(status);
	}
}
