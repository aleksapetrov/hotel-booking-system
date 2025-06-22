package com.example.Jbook.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class CreditCard {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private long id;

    private String type;
    private String ccNumber;
    private String CVC;
    private LocalDate validDate;
    private String ccHolder;

    @ManyToOne
    private User client;
    @OneToOne
    private Booking booking;

}
