package com.pet.hotel.businessLogic.domain.transferObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.hotel.businessLogic.domain.transferObjects.rent.RentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutogenerateScheduleRequest {

    @JsonProperty("rent")
    private RentDto rentDto;

    @JsonProperty("number_of_blocks")
    private int numberOfBlocks;
}
