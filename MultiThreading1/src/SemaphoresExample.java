import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class ReaderWriterSolution{


    private final Semaphore writerSemaphore=new Semaphore(1,true);
    private final Semaphore mutex=new Semaphore(1);
    private final Semaphore turnstile=new Semaphore(1);
    private int rdCnt=0;

    private int X=0;

    void read() {

        for(;true;){


            try{

                turnstile.acquire();


                mutex.acquire();

                rdCnt++;

                if(rdCnt==1){
                    writerSemaphore.acquire();
                }

                mutex.release();
                turnstile.release();


                System.out.println("Thread: "+ Thread.currentThread().getName()+" reading value of X: "+ X);
                Thread.sleep(200);

                mutex.acquire();

                rdCnt--;

                if(rdCnt==0){
                    writerSemaphore.release();
                }

                mutex.release();







            }
            catch (Exception e){
                e.printStackTrace();
            }
        }





    }

    void write() {
        for(;true;){
            try {

                turnstile.acquire();

                writerSemaphore.acquire();

                turnstile.release();
                X++;

                System.out.println("Thread:" + Thread.currentThread().getName() + "Updated the value of X to " + X);
                Thread.sleep(200);
                writerSemaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
public class SemaphoresExample {

    public static void main(String[] args) {


        ExecutorService executorService= Executors.newFixedThreadPool(3);

        ReaderWriterSolution readerWriterSolution=new ReaderWriterSolution();


        executorService.submit(()->{
            readerWriterSolution.read();
        });


        executorService.submit(()->{
            readerWriterSolution.read();
        });
        executorService.submit(()->{
            readerWriterSolution.write();
        });



        executorService.shutdown();

    }
}
