package com.pet.hotel.businessLogic.domain.transferObjects.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {

    private int hotelId;

    private String name;

    private String region;

    private String district;

    private String city;

    private String street;

    private String numberHouse;

    public HotelDto(String name, String region, String district, String city, String street, String numberHouse) {
        this.name = name;
        this.region = region;
        this.district = district;
        this.city = city;
        this.street = street;
        this.numberHouse = numberHouse;
    }
}
