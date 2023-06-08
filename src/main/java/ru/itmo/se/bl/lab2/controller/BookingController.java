package ru.itmo.se.bl.lab2.controller;

import static ru.itmo.se.bl.lab2.utils.ValidationUtils.nullOrEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.itmo.se.bl.lab2.dto.BookingDTO;
import ru.itmo.se.bl.lab2.dto.InternationalPassportDTO;
import ru.itmo.se.bl.lab2.dto.PassportDTO;
import ru.itmo.se.bl.lab2.dto.PaymentInfoDTO;
import ru.itmo.se.bl.lab2.entity.Booking;
import ru.itmo.se.bl.lab2.entity.Hotel;
import ru.itmo.se.bl.lab2.entity.InternationalPassport;
import ru.itmo.se.bl.lab2.entity.Passport;
import ru.itmo.se.bl.lab2.entity.TouristInfo;
import ru.itmo.se.bl.lab2.entity.Travel;
import ru.itmo.se.bl.lab2.exception.*;
import ru.itmo.se.bl.lab2.model.BookingResult;
import ru.itmo.se.bl.lab2.model.CardInfo;
import ru.itmo.se.bl.lab2.model.PaymentRequest;
import ru.itmo.se.bl.lab2.model.PaymentResult;
import ru.itmo.se.bl.lab2.service.BookingService;
import ru.itmo.se.bl.lab2.service.HotelService;
import ru.itmo.se.bl.lab2.service.InternationalPassportService;
import ru.itmo.se.bl.lab2.service.PassportService;
import ru.itmo.se.bl.lab2.service.PaymentService;
import ru.itmo.se.bl.lab2.service.TouristInfoService;
import ru.itmo.se.bl.lab2.service.TravelService;
import ru.itmo.se.bl.lab2.utils.ValidationUtils;
import ru.itmo.se.bl.lab2.response.BookingResponse;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
	@Autowired
	private PassportService passportService;
	
	@Autowired
	private InternationalPassportService internationalPassportService;
	
	@Autowired
	private TouristInfoService touristInfoService;
	
	@Autowired
	private BookingService bookingService;

	@PostMapping("/book")
	public BookingResponse doBook(@RequestBody BookingDTO bookingDTO) {
		PaymentInfoDTO paymentInfoDTO = bookingDTO.getPaymentInfo();

		InternationalPassportDTO[] internationalPassportsDTO = paymentInfoDTO.getInternationalPassports();
		PassportDTO[] passportsDTO =  paymentInfoDTO.getPassports();

		if (nullOrEmpty(internationalPassportsDTO) && nullOrEmpty(passportsDTO))
			return new BookingResponse(false, "EMPTY_TOURIST_INFO", "Tourists information is missed");

		List<TouristInfo> touristInfoList = new ArrayList<>();

		if (passportsDTO != null) {
			if (Arrays.stream(passportsDTO).allMatch(ValidationUtils::validatePassport)) {
				List<Passport> passports = Arrays.stream(passportsDTO).map(PassportDTO::toEntity).toList();

				passports.forEach(passport -> {
					Passport passportEntity = passportService.getBySeriesAndNumber(passport.getSeries(), passport.getNumber());
					TouristInfo touristInfo = new TouristInfo(null, passport, null);

					if (passportEntity != null) {
						passport = passportEntity;
						touristInfo = touristInfoService.getByPassportId(passport.getId());
					}

					touristInfoList.add(touristInfo);
				});
			}
			else
				return new BookingResponse(false, "INVALID_PASSPORT", "Invalid passport(s)");
		}

		if (internationalPassportsDTO != null) {
			if (Arrays.stream(internationalPassportsDTO).allMatch(ValidationUtils::validateInternationPassport)) {
				List<InternationalPassport> internationalPassports = Arrays.stream(internationalPassportsDTO).map(InternationalPassportDTO::toEntity).toList();

				internationalPassports.forEach(internationalPassport -> {
					InternationalPassport internationalPassportEntity = internationalPassportService.getBySeriesAndNumber(internationalPassport.getSeries(), internationalPassport.getNumber());
					TouristInfo touristInfo = new TouristInfo(null, null, internationalPassport);

					if (internationalPassportEntity != null) {
						internationalPassport = internationalPassportEntity;
						touristInfo = touristInfoService.getByInternationalPassportId(internationalPassport.getId());
					}

					touristInfoList.add(touristInfo);
				});
			}
			else
				return new BookingResponse(false, "INVALID_INTERNATIONAL_PASSPORT", "Invalid internation passport(s)");
		}

		Booking booking = bookingDTO.toRawEntity();

		CardInfo cardInfo = paymentInfoDTO.getCardInfo().toEntity();

		Travel travel = new Travel();
		travel.setId(bookingDTO.getTravelId());

		Hotel hotel = new Hotel();
		hotel.setId(bookingDTO.getHotelId());

		booking.setTouristInfo(touristInfoList);
		booking.setHotel(hotel);
		booking.setTravel(travel);

		int charge = travel.getCost() + hotel.getNightCost() * touristInfoList.size() * booking.getDays();

		try {
			bookingService.addBooking(booking, new PaymentRequest(cardInfo, charge));

			return new BookingResponse(BookingResult.SUCCESSFUL_BOOKING);
		} catch (TravelNotFoundException e) {
			return new BookingResponse(BookingResult.INVALID_TRAVEL);
		} catch (TravelBookException e) {
			return new BookingResponse(BookingResult.TRAVEL_BOOK_FAILURE);
		} catch (HotelNotFoundException e) {
			return new BookingResponse(BookingResult.INVALID_HOTEL);
		} catch (HotelBookException e) {
			return new BookingResponse(BookingResult.HOTEL_BOOK_FAILURE);
		} catch (PaymentException e) {
			return new BookingResponse(e.getPaymentResult());
		}
	}
}
