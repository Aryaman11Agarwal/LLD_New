import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

enum VehicleType{
    CAR, BIKE ,CYCLE
}
class Vehicle{

    VehicleType vehicleType;
    String licensePlate;

    private static final AtomicInteger idCnt=new AtomicInteger(0);
    final int id;

    Vehicle(VehicleType vehicleType,String licensePlate){
       this.vehicleType=vehicleType;
       this.licensePlate=licensePlate;
       id=idCnt.incrementAndGet();
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getId() {
        return id;
    }
}

enum TicketStatus{

    ACTIVE, CLOSED
}

class ParkingSpot{


        int id;
        VehicleType vehicleType;

        boolean occupied;

}
class Ticket{
    final int id;

    static final AtomicInteger idCnt=new AtomicInteger(0);
    final Vehicle vehicle;
    final ParkingSpot spot;
    TicketStatus status;

    LocalDateTime inTime,outTime;

    Ticket(Vehicle vehicle,ParkingSpot spot){
        this.vehicle=vehicle;
        this.spot=spot;

        id=idCnt.incrementAndGet();
        status=TicketStatus.ACTIVE;

        inTime=LocalDateTime.now();



    }

    public int getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }


}



class ParkingLevel{

    final int id;

    List<ParkingSpot> parkingSpotList;

    final static AtomicInteger idCnt=new AtomicInteger(0);

    ParkingLevel(List<ParkingSpot> parkingSpotList){
        id=idCnt.incrementAndGet();
        this.parkingSpotList=parkingSpotList;
    }

    public int getId() {
        return id;
    }

    public List<ParkingSpot> getParkingSpotList() {
        return parkingSpotList;
    }
}



class ParkingLot{

    static final AtomicInteger idCnt=new AtomicInteger(0);
    List<ParkingLevel> parkingLevels;
    final int id;

    public List<ParkingLevel> getParkingLevels() {
        return parkingLevels;

    }

    ParkingLot(List<ParkingLevel> parkingLevels){
        this.parkingLevels=parkingLevels;
        this.id=idCnt.incrementAndGet();
    }

    Ticket parkVehicle(Vehicle vehicle){

    }

    double unparkVehicle(Ticket ticket){

    }

    int getAvailableSpots(){

    }


}

interface IPaymentStrategy{

    boolean pay(double amount);
}

class UPIPaymentStrategy implements IPaymentStrategy{


    @Override
    public boolean pay(double amount) {
        System.out.println("Paying amount: "+ amount+" via UPI ");
        return true;
    }
}


class ParkingLotService{


    //

    Ticket park(Vehicle vehicle){

    }

    double unpark(Ticket ticket){

    }

    int getAvailableSpots(){

    }

    boolean makePayment(double rs,IPaymentStrategy paymentStrategy){

    }
}
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
    }
}