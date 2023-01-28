package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private boolean paymentCompleted;

    private PaymentMode paymentMode;

    @OneToOne
    @JoinColumn
    private Reservation reservation;


}
