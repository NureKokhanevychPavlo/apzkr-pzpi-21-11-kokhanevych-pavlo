package com.pet.hotel.businessLogic.domain.transferObjects.rent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.hotel.businessLogic.domain.transferObjects.PetDto;
import com.pet.hotel.businessLogic.domain.transferObjects.RoomDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentDto {

    private int rentId;

    private LocalDateTime beginDate;

    private LocalDateTime endDate;

    @JsonProperty("room")
    private RoomDto roomDto;

    @JsonProperty("pet")
    private PetDto petDto;

    public RentDto(LocalDateTime beginDate, LocalDateTime endDate, RoomDto roomDto, PetDto petDto) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.roomDto = roomDto;
        this.petDto = petDto;
    }
}
