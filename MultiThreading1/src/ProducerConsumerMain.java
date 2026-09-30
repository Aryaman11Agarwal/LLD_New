import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumerMain {


    private final Queue<Integer> buffer=new LinkedList<>();
    private final int BUFFER_CAPACITY=5;

    public void Produce(){

        while(true){

            try{
                Thread.sleep(2000);
            }
            catch (Exception e){
                e.printStackTrace();
            }
          //  System.out.println("Thread: "+Thread.currentThread().getName()+"Producer starting to produce");



            synchronized (ProducerConsumerMain.class){

                while(buffer.size()==BUFFER_CAPACITY){
                    System.out.println("Thread: "+Thread.currentThread().getName()+" Buffer is full....");

                    try{
                        ProducerConsumerMain.class.wait();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                System.out.println("Thread: "+Thread.currentThread().getName()+ " Adding item to the buffer");
                buffer.add(1);

                ProducerConsumerMain.class.notifyAll();
            }
        }


    }

    public void Consume(){

        while(true){
            try{
                Thread.sleep(2000);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        //    System.out.println("Thread: "+Thread.currentThread().getName()+"Consumer starting to consume");

            synchronized (ProducerConsumerMain.class){

                while(buffer.size()==0){
                    System.out.println("Thread: "+Thread.currentThread().getName()+" Buffer is empty....");

                    try{
                        ProducerConsumerMain.class.wait();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                System.out.println("Thread: "+Thread.currentThread().getName()+ " eating item from the buffer");
                buffer.poll();

                ProducerConsumerMain.class.notifyAll();
            }
        }


    }

    public static void main(String[] args) {

        ExecutorService executorService= Executors.newFixedThreadPool(5);
        ProducerConsumerMain producerConsumerMain=new ProducerConsumerMain();

        executorService.submit(()->{
            producerConsumerMain.Produce();
        });
        executorService.submit(()->{
            producerConsumerMain.Produce();
        });
        executorService.submit(()->{
            producerConsumerMain.Produce();
        });
        executorService.submit(()->{
            producerConsumerMain.Consume();
        });
        executorService.submit(()->{
            producerConsumerMain.Consume();
        });

        executorService.shutdown();

    }
}
