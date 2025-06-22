package com.example.Jbook.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private long id;


    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private boolean isDeleted;

    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany (mappedBy = "client")
    private List<Booking> clientBookings;
    @OneToMany (mappedBy = "client")
    private List<CreditCard> clientCreditCards;
    @OneToMany (mappedBy = "hotel")
    private List<Booking> hotelBookings;
    @OneToMany (mappedBy = "hotel")
    private List<Room> rooms;

}
