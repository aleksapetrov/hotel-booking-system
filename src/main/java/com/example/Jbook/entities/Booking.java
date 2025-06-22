package com.example.Jbook.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import java.time.LocalDate;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private long id;

    private LocalDate createdAt;
    private LocalDate modifiedAt;
    private LocalDate CancelledAt;
    private boolean isCancelled;

    @ManyToOne
    private User client;
    @ManyToOne
    private User hotel;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;
    @OneToOne
    private CreditCard creditCard;

}
