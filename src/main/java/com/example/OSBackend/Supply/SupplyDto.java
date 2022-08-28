package com.example.OSBackend.Supply;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;


@AllArgsConstructor
@Getter
@Setter
@ToString
public class SupplyDto {

    private Long supplyId;
    private String supplyName;
    private Double supplyQuantity;
}
