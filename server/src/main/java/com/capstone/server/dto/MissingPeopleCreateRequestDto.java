package com.capstone.server.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.aspectj.weaver.ast.Not;

import com.capstone.server.model.GuardianEntity;
import com.capstone.server.model.MissingPeopleDetailEntity;
import com.capstone.server.model.MissingPeopleEntity;
import com.capstone.server.model.SearchHistoryEntity;
import com.capstone.server.model.UserEntity;
import com.capstone.server.model.enums.*;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MissingPeopleCreateRequestDto {
    
    // MissingPeople
    @NotBlank
    private String missingPeopleName; 

    @Past
    private LocalDate birthdate;

    private String gender; 

    @PastOrPresent
    private LocalDateTime missingAt; 

    @NotBlank
    private String missingLocation;

    @NotNull
    private String description;

    // MissingPeopleDetail
    @NotBlank
    private String hairStyle;

    @NotBlank
    private String topType;

    @NotBlank
    private String topColor;

    @NotBlank
    private String bottomType;

    @NotBlank
    private String bottomColor;

    @NotBlank
    private String bagType;

    @NotBlank
    private String shoesColor;

    // Guardian
    @NotBlank
    private String guardianName;

    @NotBlank
    private String relationship;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "010-1234-5678 형식이어야 합니다.")
    private String phoneNumber;

    // SearchHistory
    @Past
    private LocalDateTime startTime;

    @PastOrPresent
    private LocalDateTime endTime;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @NotBlank
    private String locationAddress;

    private Status status;
    
    public MissingPeopleEntity toMissingPeopleEntity() {

        return MissingPeopleEntity.builder()
            .name(missingPeopleName)
            .birthdate(birthdate)
            .gender(Gender.fromKor(gender))
            .missingAt(missingAt)
            .missingLocation(missingLocation)
            .description(description)
            .status(Status.getDefault())
            .build();
    }

    public MissingPeopleDetailEntity toMissingPeopleDetailEntity() {

        return MissingPeopleDetailEntity.builder()
            .hairStyle(HairStyle.fromKor(hairStyle))
            .topType(TopType.fromKor(topType))
            .topColor(Color.fromKor(topColor))
            .bottomType(BottomType.fromKor(bottomType))
            .bottomColor(Color.fromKor(bottomColor))
            .bagType(BagType.fromKor(bagType))
            .shoesColor(Color.fromKor(shoesColor))
            .build();
    }

    public GuardianEntity toGuardianEntity() {

        return GuardianEntity.builder()
            .name(guardianName)
            .phoneNumber(phoneNumber)
            .relationship(relationship)
            .build();
    }

    public SearchHistoryEntity toSearchHistoryEntity() {

        return SearchHistoryEntity.builder()
            .startTime(startTime)
            .endTime(endTime)
            .latitude(latitude)
            .longitude(longitude)
            .locationAddress(locationAddress)
            .build();
    }
}
