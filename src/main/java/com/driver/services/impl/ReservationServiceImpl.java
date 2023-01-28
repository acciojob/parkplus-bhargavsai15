package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Reservation reservation=new Reservation();

        User user=userRepository3.findById(userId).get();
        ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();

        if(user==null){
            throw new Exception("Cannot make reservation");
        }else if(parkingLot==null){
            throw new Exception("Cannot make reservation");
        }

        List<Spot> spotList=parkingLot.getSpotList();

        if(spotList==null){
            throw new Exception("Cannot make reservation");
        }

        int min=Integer.MAX_VALUE;
        Integer minSpotId=null;

        for(Spot spot:spotList){
            if(spot.getPricePerHour()<min && spot.getOccupied()==false){
                min=spot.getPricePerHour();
                minSpotId=spot.getId();
            }
        }
        if(minSpotId== Integer.MAX_VALUE){
            throw new Exception("Cannot make reservation");
        }

        Spot spot=spotRepository3.findById(minSpotId).get();
        List<Reservation> reservationList=spot.getReservationList();
        List<Reservation> reservations=user.getReservationList();
        int totalPrice=min*timeInHours;

        reservation.setUser(user);
        reservation.setSpot(spot);
        reservation.setNumberOfHours(timeInHours);

        reservationList.add(reservation);
        reservations.add(reservation);
        spot.setReservationList(reservationList);
        user.setReservationList(reservations);

        userRepository3.save(user);

        return reservation;
    }
}
