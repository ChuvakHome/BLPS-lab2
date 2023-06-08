package ru.itmo.se.bl.lab2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.se.bl.lab2.entity.Hotel;

@Data
@NoArgsConstructor
public class HotelDTO {
    private int id;
    private String hotelName;
    private int nightCost;
    private int cityId;

    public static HotelDTO fromEntity(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setId(hotel.getId());
        dto.setHotelName(hotel.getHotelName());
        dto.setNightCost(hotel.getNightCost());
        dto.setCityId(hotel.getCity().getId());

        return dto;
    }
}
