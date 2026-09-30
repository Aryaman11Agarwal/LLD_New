import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReaderWriterProblem {

    private final ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);
    private int X=0;
    public void read(){
        while(true){




            reentrantReadWriteLock.readLock().lock();


            System.out.println("Reading the value of X: "+ X);
            try{
                Thread.sleep(2000);
            }
            catch (Exception e){
                e.printStackTrace();
            }


            reentrantReadWriteLock.readLock().unlock();
        }
    }

    public void write(){

        while(true){
            reentrantReadWriteLock.writeLock().lock();

            X++;

            System.out.println("Updated the value of X to :"+X);

            try{
                Thread.sleep(2000);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            reentrantReadWriteLock.writeLock().unlock();
        }


    }
    public static void main(String[] args) {


        ReaderWriterProblem readerWriterProblem=new ReaderWriterProblem();


        ExecutorService executorService= Executors.newFixedThreadPool(5);

        executorService.submit(()->{
            readerWriterProblem.read();
        });
        executorService.submit(()->{
            readerWriterProblem.read();
        });
        executorService.submit(()->{
            readerWriterProblem.read();
        });
        executorService.submit(()->{
            readerWriterProblem.write();
        });
        executorService.submit(()->{
            readerWriterProblem.write();
        });

        executorService.shutdown();


    }
}
