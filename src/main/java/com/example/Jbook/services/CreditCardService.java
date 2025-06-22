package com.example.Jbook.services;

import com.example.Jbook.entities.Booking;
import com.example.Jbook.entities.CreditCard;
import com.example.Jbook.entities.Role;

public interface CreditCardService {

    CreditCard add(Role role, CreditCard creditCard);
    CreditCard update(Role role, CreditCard creditCard);
    void Cancel(Booking booking, CreditCard creditCard);
}
