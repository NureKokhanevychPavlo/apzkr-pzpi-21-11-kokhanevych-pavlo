package com.pet.hotel.businessLogic.domain.transferObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.hotel.businessLogic.domain.transferObjects.user.UserDto;
import com.pet.hotel.data.enums.PetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDto {

    private int petId;

    private String name;

    private int age;

    private float weight;

    private PetType typePet;

    private String description;

    private String photoLink;

    @JsonProperty("user")
    private UserDto userDto;

    public PetDto(String name, int age, float weight, PetType typePet, String description, String photoLink, UserDto userDto) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.typePet = typePet;
        this.description = description;
        this.photoLink = photoLink;
        this.userDto = userDto;
    }
}
