import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

import java.time.*;

enum VehicleType{
    CAR, BIKE
}
class Vehicle{

    int id;
    String licensePlate;
    VehicleType vehicleType;

    private static int cnt=0;

    Vehicle(VehicleType vehicleType,String licensePlate){
        id=cnt++;
        this.licensePlate=licensePlate;
        this.vehicleType=vehicleType;


    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getId() {

        return id;
    }

    public String getLicensePlate() {

        return licensePlate;
    }
}

class ParkingSpot{
    int id;
    VehicleType vehicleType;

    boolean isOccupied;

    private static int idCnt=0;
    ParkingSpot(VehicleType vehicleType){
        this.vehicleType=vehicleType;
        id=++idCnt;
        isOccupied=false;


    }

    public void occupy() {

        isOccupied = true;
    }

    public void vacate() {

        isOccupied = false;
    }
}

class Ticket{
    int id;
    Vehicle vehicle;
    ParkingSpot parkingSpot;
    LocalDateTime inTime;
    private static int idCnt=0;

    private TicketStatus ticketStatus;

    Ticket(Vehicle vehicle,ParkingSpot parkingSpot){
        id=++idCnt;
        this.vehicle=vehicle;
        this.parkingSpot=parkingSpot;
        inTime= LocalDateTime.now();

        ticketStatus=TicketStatus.ACTIVE;



        this.parkingSpot.occupy();
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    void closeTicket(){
        ticketStatus=TicketStatus.CLOSED;
    }
}

class ParkingLevel{

    int id;
    int floor;
    List<ParkingSpot> spots;

    ParkingLevel(List<ParkingSpot> spots){
        this.spots=spots;

    }

    int getFreeSpots(){


        int ans=0;
        for(ParkingSpot parkingSpot:spots){
            if(parkingSpot.isOccupied){

                continue;
            }


            ans++;
        }


        return ans;
    }

    public List<ParkingSpot> getSpots() {

        return spots;
    }
}

interface IFindParkingSpotStrategy {
    ParkingSpot getSpot(ParkingLot parkingLot,VehicleType vehicleType);
}

class lowerSpotFirstFindParkingSpotStrategy implements IFindParkingSpotStrategy{

    public ParkingSpot getSpot(ParkingLot parkingLot,VehicleType vehicleType){


        List<ParkingLevel> parkingLevels=parkingLot.getParkingLevels();

        for(ParkingLevel parkingLevel:parkingLevels){
            List<ParkingSpot> parkingSpots=parkingLevel.getSpots();

            for(ParkingSpot parkingSpot:parkingSpots){


                if(parkingSpot.isOccupied==false && vehicleType==parkingSpot.vehicleType) {

                    return parkingSpot;
                }
            }
        }


        return null;
    }
}

interface IParkingFeesStrategy{


    double getFees(Ticket ticket);
}

class fixedParkingFeesStrategy implements IParkingFeesStrategy{

    public double getFees(Ticket ticket){

        Duration duration=Duration.between(ticket.inTime,LocalDateTime.now());

        long minutes=duration.toMinutes();

        return (minutes)*(0.5);
    }
}


enum TicketStatus{
    ACTIVE, CLOSED
}

class ParkingLot{

    List<ParkingLevel> parkingLevels;
    IFindParkingSpotStrategy findParkingSpotStrategy;
    IParkingFeesStrategy parkingFeesStrategy;

    ParkingLot(List<ParkingLevel> parkingLevels){
        this.parkingLevels=parkingLevels;

        findParkingSpotStrategy=new lowerSpotFirstFindParkingSpotStrategy();
        parkingFeesStrategy=new fixedParkingFeesStrategy();


    }

    public List<ParkingLevel> getParkingLevels() {
        return parkingLevels;
    }

    synchronized Ticket parkVehicle(Vehicle vehicle){


        ParkingSpot parkingSpot=findParkingSpotStrategy.getSpot(this,vehicle.getVehicleType());

        if(parkingSpot==null) {

            return null;
        }

        Ticket ticket = new Ticket(vehicle,parkingSpot);


        return ticket;
    }

    synchronized double unparkVehicle(Ticket ticket){
        if(ticket.getTicketStatus()==TicketStatus.CLOSED){
            throw new IllegalStateException("Ticket already closed");
        }


        double fees = parkingFeesStrategy.getFees(ticket);
        ticket.parkingSpot.vacate();
        ticket.closeTicket();

        return fees;
    }

    synchronized int getFreeSpots(){



        int cnt=0;

        for(ParkingLevel parkingLevel:parkingLevels){
            cnt+= parkingLevel.getFreeSpots();
        }



        return cnt;
    }
}


class ParkingLotService{
    ParkingLot parkingLot;


    ParkingLotService(ParkingLot parkingLot){
        this.parkingLot=parkingLot;

    }

    Ticket parkVehicle(Vehicle vehicle){


        return parkingLot.parkVehicle(vehicle);

    }

    double unparkVehicle(Ticket ticket){



        return parkingLot.unparkVehicle(ticket);
    }

    int getAvailableSpots(){


        return parkingLot.getFreeSpots();
    }
}

public class Main {
    public static void main(String[] args) {



        ParkingSpot carspot1=new ParkingSpot(VehicleType.CAR);
        ParkingSpot carspot2=new ParkingSpot(VehicleType.CAR);
        ParkingSpot carspot3=new ParkingSpot(VehicleType.CAR);
        ParkingSpot carspot4=new ParkingSpot(VehicleType.CAR);
        ParkingSpot carspot5=new ParkingSpot(VehicleType.CAR);

        ParkingSpot bikespot1=new ParkingSpot(VehicleType.BIKE);
        ParkingSpot bikespot2=new ParkingSpot(VehicleType.BIKE);
        ParkingSpot bikespot3=new ParkingSpot(VehicleType.BIKE);
        ParkingSpot bikespot4=new ParkingSpot(VehicleType.BIKE);
        ParkingSpot bikespot5=new ParkingSpot(VehicleType.BIKE);

        List<ParkingSpot> list1=new ArrayList<>();
        List<ParkingSpot> list2=new ArrayList<>();

        list1.add(carspot1);
        list2.add(carspot2);
        list1.add(carspot3);
        list2.add(carspot4);
        list1.add(carspot5);
        list2.add(bikespot1);
        list1.add(bikespot2);
        list2.add(bikespot3);
        list1.add(bikespot4);
        list2.add(bikespot5);

        ParkingLevel parkingLevel1=new ParkingLevel(list1);
        ParkingLevel parkingLevel2=new ParkingLevel(list2);

        List<ParkingLevel> parkingLevels=new ArrayList<>();
        parkingLevels.add(parkingLevel1);
        parkingLevels.add(parkingLevel2);

        ParkingLot parkingLot=new ParkingLot(parkingLevels);

        ParkingLotService parkingLotService=new ParkingLotService(parkingLot);

        Vehicle car1=new Vehicle(VehicleType.CAR,"XYZ1");
        Vehicle car2=new Vehicle(VehicleType.CAR,"XYZ2");
        Vehicle car3=new Vehicle(VehicleType.CAR,"XYZ3");
        Vehicle car4=new Vehicle(VehicleType.CAR,"XYZ4");
        Vehicle bike1=new Vehicle(VehicleType.BIKE,"ABC1");
        Vehicle bike2=new Vehicle(VehicleType.BIKE,"ABC2");
        Vehicle bike3=new Vehicle(VehicleType.BIKE,"ABC3");



        Ticket ticket1 = parkingLotService.parkVehicle(car1);
        Ticket ticket2 = parkingLotService.parkVehicle(car2);
        Ticket ticket3 = parkingLotService.parkVehicle(car3);



        int availableSpots = parkingLotService.getAvailableSpots();




        Ticket ticket4 = parkingLotService.parkVehicle(bike1);
        Ticket ticket5 = parkingLotService.parkVehicle(bike2);



        availableSpots = parkingLotService.getAvailableSpots();


        if(ticket1 != null){
            double fees = parkingLotService.unparkVehicle(ticket1);

            try{
                double fees2 = parkingLotService.unparkVehicle(ticket1);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }


        }



        availableSpots = parkingLotService.getAvailableSpots();

    }
}