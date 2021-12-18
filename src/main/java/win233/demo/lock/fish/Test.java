package win233.demo.lock.fish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    private Logger logger = LoggerFactory.getLogger("test");
    public Fish day1() throws InterruptedException {
        Fish fish = new Fish();
        Thread tom = new Thread(() -> {

            if(fish.noFeed()) {
    //                Thread.yield();
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fish.feed();
            }

        });
        Thread alice = new Thread(() -> {
            if(fish.noFeed()) {
//                Thread.yield();
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fish.feed();
            }
        });
        tom.start();
        alice.start();
        return fish;
    }


    public Fish day2() throws InterruptedException {
        Fish fish = new Fish();
        Thread tom = new Thread(() -> {
            if(!fish.note) {
                sleep(1);
                fish.leaveANote();
                if(fish.noFeed()) {
                    logger.info("喂鱼");
                    sleep(1);
                    fish.feed();
                }
                fish.removeNote();
            } else {
                logger.info("有人喂过了");
            }


        });
        tom.setName("tom");
        Thread alice = new Thread(() -> {

            if(!fish.note) {
                sleep(1);
                fish.leaveANote();
                if (fish.noFeed()) {
                    logger.info("喂鱼");
                    fish.feed();
                }
                fish.removeNote();
            } else {
                logger.info("有人喂过了");
            }
        });
        alice.setName("alice");

        tom.start();
        alice.start();
        return fish;
    }







    public Fish day3() throws InterruptedException {
        Fish fish = new Fish();
        Thread tom = new Thread(() -> {
            fish.leaveNoteAlice();
            if(fish.noNoteTom()) {
                sleep(1);
                if(fish.noFeed()) {
                    logger.info("喂鱼");
                    sleep(1);
                    fish.feed();
                }
                fish.removeNote();
            } else {
                logger.info("已经喂过了");
            }


        },"tom");


        Thread alice = new Thread(() -> {
            fish.leaveNoteTom();
            if(fish.noNoteAlice()) {
                sleep(1);
                if (fish.noFeed()) {
                    logger.info("喂鱼");
                    fish.feed();
                }
                fish.removeNote();
            } else {
                logger.info("已经喂过了");
            }
        },"alice");

        tom.start();
        alice.start();
        return fish;
    }

    public static ExecutorService taskPool = new ThreadPoolExecutor(5, 15, 1000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
    
    public List<Future<Fish>> day4(int count) throws ExecutionException, InterruptedException {
        List<Future<Fish>> futureList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Fish fish = new Fish();
            Callable tomTask = () -> {
                fish.leaveNoteAlice();
//                sleep(1);
                if(fish.noNoteTom()) {
                    sleep(1);
                    if(fish.noFeed()) {
                        logger.info("喂鱼");
                        sleep(1);
                        fish.feed();
                    }
                    fish.removeNote();
                } else {
                    logger.info("已经喂过了");
                }
                return fish;
            };

            Callable aliceTask = () -> {
                fish.leaveNoteTom();
//                sleep(1);
                if(fish.noNoteAlice()) {
                    sleep(1);
                    if (fish.noFeed()) {
                        logger.info("喂鱼");
                        fish.feed();
                    }
                    fish.removeNote();
                } else {
                    logger.info("已经喂过了");
                }
                return fish;
            };
            futureList.add(Test.submitTaskWithResult(tomTask));
            futureList.add(Test.submitTaskWithResult(aliceTask));
        }
        return futureList;
    }
    public void sleep(int m) {
        try {
//            Thread.sleep(m);
//            Thread.yield();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public static<V> Future<V> submitTaskWithResult(Callable<V> callable) throws ExecutionException, InterruptedException {
        Future<V> future = taskPool.submit(callable);
        return future;
    }

    public static void testDay4(int count) throws ExecutionException, InterruptedException {
        Test t = new Test();
        List<Fish> fishResult = new ArrayList<>();
        List<Future<Fish>> futures = t.day4(count);
        futures.forEach(fishFuture -> {
            try {
                Fish fish = fishFuture.get();
                fishResult.add(fish);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        printFishStatus(fishResult);
        taskPool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        /*Test t = new Test();
        List<Fish> fishResult = new ArrayList<>();
        for (int i=0;i<3000;i++) {
            Fish result = t.day3();
            fishResult.add(result);
        }
        System.out.println("数据准备完毕，开始等待线程执行结束");
        TimeUnit.SECONDS.sleep(5);
        printFishStatus(fishResult);
*/
        try {
            testDay4(30000);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private static void printFishStatus(List<Fish> fishResult) {
        AtomicInteger liveCount = new AtomicInteger();
        AtomicInteger hungryCount = new AtomicInteger();
        AtomicInteger deadCount = new AtomicInteger();
        fishResult.forEach(fish -> {
            if(fish.isLive()) {
                liveCount.getAndIncrement();
            } else if(fish.isHungry()){
                hungryCount.incrementAndGet();
            } else if(fish.isDead()){
                deadCount.incrementAndGet();
            }
            System.out.println("Fish is "+fish);
        });
        System.out.println("live count "+ liveCount.get());
        System.out.println("hungryCount count "+ hungryCount.get());
        System.out.println("dead count "+ deadCount.get());
    }


}
