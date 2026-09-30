import java.util.concurrent.*;
import java.util.concurrent.Callable;

//class MyThread extends Thread{
//
//    @Override
//    public void run() {
//        for(int i=0;i<100;i++){
//            System.out.println("Thread: "+ currentThread().getName()+ " running iteration "+ i);
//            try{
//                Thread.sleep(500);
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
//class MyRunnable implements Runnable{
//    @Override
//    public void run() {
//        for(int i=0;i<100;i++){
//            System.out.println("Thread: "+ Thread.currentThread().getName()+ " running iteration "+ i);
//            try{
//                Thread.sleep(5);
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
//class SharedResource{
//
//    Object lock;
//
//    SharedResource(){
//        lock=new Object();
//    }
//
//
//    synchronized void waitExample(){
//        System.out.println("Thread"+ Thread.currentThread().getName()+" is waiting");
//
//        try{
//            wait();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//        System.out.println("continuing after waiting....");
//    }
//
//     void notifyExample(){
//
//        synchronized (this){
//            System.out.println(Thread.currentThread().getName() +" notifying theads....");
//
//            try{
//                System.out.println("Thread: "+ Thread.currentThread().getName()+ " notifying threads");
//                notifyAll();
//            }
//            catch (Exception e){
//
//            }
//        }
//
//    }
//
//}
public class Main {
    public static void main(String[] args) {

//         SharedResource sharedResource=new SharedResource();
//
//         Thread t1=new Thread(()->{
//
//            sharedResource.waitExample();
//         });
//
//         Thread t2=new Thread(()->{
//
//             try{
//                 Thread.sleep(1000);
//             }
//             catch (Exception e){
//
//             }
//             sharedResource.notifyExample();
//         });

//
//
//        Thread t2=new Thread(new MyRunnable());
//        t2.start();

//        try{
//            t1.join();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//        try{
//            t2.join();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }


//        ExecutorService executor= Executors.newFixedThreadPool(2);
//
//        Callable<Integer> task=()->{
//            return 42;
//        };
//
//        Future<Integer> future=executor.submit(task);
//
//        try{
//            System.out.println(future.get());
//            System.out.println(future.get());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//        executor.shutdown();


//        t1.start();
//        t2.start();




        ExecutorService executorService= Executors.newFixedThreadPool(2);
        WaitNotifyExample waitNotifyExample=new WaitNotifyExample();
        executorService.submit(()->{
            waitNotifyExample.waitExample();
        });


        try{
            Thread.sleep(4000);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        executorService.submit(()->
        {
            waitNotifyExample.notifyExample();
        });

        executorService.shutdown();


//        try{
//            Thread.sleep(2000);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }



      //  executorService.shutdownNow();

    }
}


class WaitNotifyExample{

    private final Object lock=new Object();
    public void waitExample(){

        System.out.println("Entering the locked resource");

        synchronized (lock){

            System.out.println("going to wait");

            try{
                wait();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            System.out.println("Thread"+ Thread.currentThread().getName() +"Continuing after waiting.....");
        }
    }

    public void notifyExample(){

        synchronized (this){

            try{
                Thread.sleep(2000);;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            System.out.println("Thread: "+Thread.currentThread().getName()+" is notifying all");
            notifyAll();
        }
    }


}

class MyRunnable implements Runnable{

    final int id;

    MyRunnable(int taskId){
        this.id=taskId;
    }

    @Override
    public void run() {

//


          for(int i=0;i<5;i++){
              System.out.println("Thread "+ Thread.currentThread().getName()+ " Waiting....");
            //  Thread.sleep(500);
          }

        //System.out.println("Thread got interrupted");

    }
}