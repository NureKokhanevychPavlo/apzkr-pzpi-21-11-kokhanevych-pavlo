package com.pet.hotel.businessLogic.domain.transferObjects.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRentingResponse {

    private LocalDateTime beginDate;

    private LocalDateTime endDate;

    private String petName;

    private int roomId;

    private int rentId;

    private int numberRoom;

    private String hotelName;

    private int totalPayment;
}
