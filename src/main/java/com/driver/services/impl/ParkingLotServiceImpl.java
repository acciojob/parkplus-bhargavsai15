package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.PaymentRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot=new ParkingLot(name,address);

        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();

        Spot spot=new Spot();
        
        spot.setOccupied(false);
        if(numberOfWheels<=2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        } else if (numberOfWheels>2 && numberOfWheels<=4) {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else {
            spot.setSpotType(SpotType.OTHERS);
        }
        spot.setPricePerHour(pricePerHour);

        spot.setParkingLot(parkingLot);
        
        List<Spot> spotList=parkingLot.getSpotList();

        spotList.add(spot);

        parkingLot.setSpotList(spotList);

        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();

        List<Spot> spotList=parkingLot.getSpotList();

        Spot spot1=null;
        for(Spot spots:spotList){
            if(spots.getId()==spotId){
                spots.setPricePerHour(pricePerHour);
                spots.setParkingLot(parkingLot);
                spot1=spots;
            }
        }

        parkingLotRepository1.save(parkingLot);
        spotRepository1.save(spot1);

        return spot1;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        try{
            parkingLotRepository1.deleteById(parkingLotId);
        }catch (Exception e){
            System.out.println("error");
        }
    }
}
