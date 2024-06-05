package com.pet.hotel.businessLogic.domain.transferObjects.rent;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.hotel.data.enums.FoodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleFeedResponse {

    private int scheduleId;

    private LocalDateTime dateTime;

    @JsonProperty("portion")
    private float portion;

    private FoodType typeFood;
}
