package com.pet.hotel.businessLogic.domain.transferObjects.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.hotel.data.enums.UserType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class UserDto {

    private int userId;

    private String fullName;

    @JsonProperty("password")
    private String passwordHash;

    private String email;

    private LocalDate birthDate;

    private String phoneNumber;

    private String photoLink;

    private UserType typeUser;

    public UserDto(String fullName, String passwordHash, String email, LocalDate birthDate, String phoneNumber, String photoLink, UserType typeUser) {
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.email = email;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.photoLink = photoLink;
        this.typeUser = typeUser;
    }
}
