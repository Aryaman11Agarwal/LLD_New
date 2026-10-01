import java.time.Duration;
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

        spot.occupied=true;



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


    public void checkout(){

        outTime=LocalDateTime.now();

        status=TicketStatus.CLOSED;
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


    int getAvailableSpots(VehicleType vehicleType){
        int ans=0;

        for(ParkingSpot parkingSpot:parkingSpotList){
            if(parkingSpot.occupied==true) continue;

            if(parkingSpot.vehicleType==vehicleType) ans++;
        }

        return ans;
    }
}

interface IParkingSpotFindStrategy{
    ParkingSpot findSpot(ParkingLot parkingLot,VehicleType vehicleType);
}

class lowerSpotFirst implements IParkingSpotFindStrategy{

    @Override
    public ParkingSpot findSpot(ParkingLot parkingLot, VehicleType vehicleType) {
        List<ParkingLevel> levels=parkingLot.getParkingLevels();

        for(ParkingLevel parkingLevel:levels){
            for(ParkingSpot parkingSpot:parkingLevel.getParkingSpotList()){
                if(parkingSpot.occupied) continue;

                return parkingSpot;
            }
        }

        return null;
    }
}

interface IPricingCalculator{

    double getPrice(Ticket ticket);
}

class FixedPricingCalulator implements IPricingCalculator{

    @Override
    public double getPrice(Ticket ticket) {
        Duration d=Duration.between(ticket.inTime, ticket.outTime);

        long mins=d.toMinutes();

        return mins*(0.5);
    }
}



class ParkingLot{

    static final AtomicInteger idCnt=new AtomicInteger(0);
    List<ParkingLevel> parkingLevels;

    IParkingSpotFindStrategy parkingSpotFindStrategy;
    IPricingCalculator pricingCalculator;
    final int id;

    public List<ParkingLevel> getParkingLevels() {
        return parkingLevels;

    }

    ParkingLot(List<ParkingLevel> parkingLevels, IParkingSpotFindStrategy parkingSpotFindStrategy,IPricingCalculator pricingCalculator){
        this.parkingLevels=parkingLevels;
        this.id=idCnt.incrementAndGet();

        this.parkingSpotFindStrategy=parkingSpotFindStrategy;
        this.pricingCalculator=pricingCalculator;
    }

   synchronized Ticket parkVehicle(Vehicle vehicle){

        ParkingSpot spot=parkingSpotFindStrategy.findSpot(this,vehicle.getVehicleType());

        if(spot==null) return null;

        return new Ticket(vehicle,spot);
    }

    synchronized double unparkVehicle(Ticket ticket){

        if(ticket.status==TicketStatus.CLOSED){
            throw new IllegalStateException("Ticket already closed");
        }
        ticket.checkout();

       return pricingCalculator.getPrice(ticket);
    }

    synchronized int getAvailableSpots(VehicleType vehicleType){
     int ans=0;
        for(ParkingLevel parkingLevel:parkingLevels){
            ans+=parkingLevel.getAvailableSpots(vehicleType);

        }

        return ans;
    }


}

interface IPaymentStrategy{

    boolean pay(double amount);
}

class UPIPaymentStrategy implements IPaymentStrategy{


    @Override
    public synchronized boolean pay(double amount) {
        System.out.println("Paying amount: "+ amount+" via UPI ");
        return true;
    }
}


class ParkingLotService{

    ParkingLot parkingLot;

    ParkingLotService(ParkingLot parkingLot){
        this.parkingLot=parkingLot;
    }
    //

    Ticket park(Vehicle vehicle){
            return parkingLot.parkVehicle(vehicle);
    }

    double unpark(Ticket ticket){
      return parkingLot.unparkVehicle(ticket);

    }

    int getAvailableSpots(){
       return parkingLot.getAvailableSpots();
    }

    boolean makePayment(double rs,IPaymentStrategy paymentStrategy)
    {

       return paymentStrategy.pay(rs);
    }
}
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
    }
}