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

        User user=userRepository3.findById(userId).orElseGet(null);
        ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).orElseGet(null);

        if(user==null){
            throw new Exception("Cannot make reservation");
        }else if(parkingLot==null){
            throw new Exception("Cannot make reservation");
        }

        List<Spot> spotList=parkingLot.getSpotList();



        int min=Integer.MAX_VALUE;
        Integer minSpotId=null;
        Spot spot1=null;

        for(Spot spot:spotList){
            if(spot.getPricePerHour()*numberOfWheels<min && spot.getOccupied()==false && getType(spot.getSpotType())>=numberOfWheels){
                min=spot.getPricePerHour()*numberOfWheels;
                minSpotId=spot.getId();
            }
        }

        if(minSpotId==null){
            throw new Exception("Cannot make reservation");
        }

        Spot spot=spotRepository3.findById(minSpotId).get();

        List<Reservation> reservationList=spot1.getReservationList();

        List<Reservation> reservations=user.getReservationList();


        reservation.setUser(user);
        reservations.add(reservation);
        user.setReservationList(reservations);


        reservation.setSpot(spot1);

        reservationList.add(reservation);

        spot1.setReservationList(reservationList);
        spot.setOccupied(true);

        reservation.setNumberOfHours(timeInHours);


        spot.setReservationList(reservationList);


        userRepository3.save(user);
        spotRepository3.save(spot1);

        return reservation;
    }

    int getType(SpotType spotType){
        if(spotType==SpotType.TWO_WHEELER){
            return 2;
        }else if(spotType==SpotType.FOUR_WHEELER){
            return 4;
        }else{
            return Integer.MAX_VALUE;
        }
    }
}
