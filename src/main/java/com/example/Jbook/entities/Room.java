package com.example.Jbook.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Room {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private long id;

    private String guestFirstName;
    private String guestLastName;
    private String name;
    private String type;
    private String description;
    private double price;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private LocalDate deadline;
    private boolean cancelled;

    @ManyToOne
    private Booking booking;

}
