package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        Reservation reservation=reservationRepository2.findById(reservationId).get();
        reservation.getSpot().setOccupied(false);
        PaymentMode paymentMode=null;

        if(mode.equalsIgnoreCase("cash")){
            paymentMode=PaymentMode.CASH;
        } else if (mode.equalsIgnoreCase("card")) {
            paymentMode=PaymentMode.CASH;
        }else if(mode.equalsIgnoreCase("upi")){
            paymentMode=PaymentMode.UPI;
        }else {
            throw new Exception("Payment mode not detected");
        }

        User user=reservation.getUser();
        Spot spot=reservation.getSpot();

        int bill= spot.getPricePerHour()*reservation.getNumberOfHours();

        if(amountSent<bill){
            throw new Exception("Insufficient Amount");
        }

        Payment payment=new Payment(true,paymentMode);
        payment.setReservation(reservation);

        paymentRepository2.save(payment);

        return payment;
    }
}
