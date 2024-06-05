package com.pet.hotel.businessLogic.domain.transferObjects.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
