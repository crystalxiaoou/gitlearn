package javathreads.examples.ch06;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hombre on 2015/8/22.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/22 18:39
 */
public class AlternateDeadLockDetectingLock extends ReentrantLock {
    private static final int DELAY_TWO_SECOND = 2;
    private static final int DEALY_TEN_SECOND = 10;
    private static final int DELAY_FOUR_SECOND = 4;
    private static final int WAIT_TEN_SECOND = 10;
    private static final int DELAY_ONE_SECOND = 1;
    private static final int DELAY_SIXTY_SECOND = 60;
    private static final int EXIT_STATUS = 0;
    private static List deadLockLocksRegistry = new ArrayList();

    private static synchronized void registerLock(AlternateDeadLockDetectingLock alternateDeadLockDetectingLock){
        if(!deadLockLocksRegistry.contains(alternateDeadLockDetectingLock)){
            deadLockLocksRegistry.add(alternateDeadLockDetectingLock);
        }
    }

    private static synchronized  void unregisterLock(AlternateDeadLockDetectingLock alternateDeadLockDetectingLock){
        if(deadLockLocksRegistry.contains(alternateDeadLockDetectingLock)){
            deadLockLocksRegistry.remove(alternateDeadLockDetectingLock);
        }
    }

    private List hardwaitingThreads = new ArrayList();
    private static synchronized void markAsHardwait(List l, Thread t){
        if(!l.contains(t)){
            l.add(t);
        }
    }

    private static synchronized void freeIfHardwait(List l, Thread t){
        if(l.contains(t)){
            l.remove(t);
        }
    }

    private static Iterator getAllLocksOwned(Thread t){
        AlternateDeadLockDetectingLock current;
        ArrayList results = new ArrayList();

        Iterator itr = deadLockLocksRegistry.iterator();
        while(itr.hasNext()){
            current = (AlternateDeadLockDetectingLock) itr.next();
            if(current.getOwner() == t){
                results.add(current);
            }
        }
        return  results.iterator();
    }

    private static Iterator getAllThreadsHardwaiting(AlternateDeadLockDetectingLock l){
        return l.hardwaitingThreads.iterator();
    }

    private static boolean canThreadWaitOnLock0(Thread t, AlternateDeadLockDetectingLock l){
        Iterator locksOwned = getAllLocksOwned(t);
        while(locksOwned.hasNext()){
            AlternateDeadLockDetectingLock current = (AlternateDeadLockDetectingLock) locksOwned.next();

            if(current == l) return  false;
            Iterator waitingThreads = getAllThreadsHardwaiting(current);
            while(waitingThreads.hasNext()){
                Thread otherthread = (Thread) waitingThreads.next();

                if(!canThreadWaitOnLock0(otherthread, l)){
                    return false;
                }
            }
        }
        return true;
    }

    private static synchronized boolean canThreadWaitOnLock(Thread t, AlternateDeadLockDetectingLock l){
        if(l.getOwner() == null){
            return true;
        }
        return canThreadWaitOnLock(t, l);
    }

    private static boolean DDLFastFail = true;
    private static boolean DDLCleanUp = true;
    private static int DDLHWSWTime = 60;

    public AlternateDeadLockDetectingLock(){
        this(false, false);
    }

    private AlternateDeadLockDetectingLock(boolean fair){
        this(fair, false);
    }
    private boolean debugging;

    private AlternateDeadLockDetectingLock(boolean fair, boolean debug){
        super(fair);
        debugging = debug;
        registerLock(this);
    }

    private static boolean DDLdeadlockDETECTED = false;

    public void lock(){
        if(DDLFastFail && DDLdeadlockDETECTED){
            throw new DeadLockDetectedException("EARILER DEADLOCK DETECTED");
        }

        if(isHeldByCurrentThread()){
            if(debugging){
                System.out.println("Already Own Lock");
                super.lock();
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
                return;
            }
        }
        markAsHardwait(hardwaitingThreads, Thread.currentThread());
        if(canThreadWaitOnLock(Thread.currentThread(), this)){
            if(debugging) {
                System.out.println("Waiting For Lock");
            }
            super.lock();
            freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            if (debugging) {
                System.out.println("Got New Lock");
            }
        }else{
            DDLdeadlockDETECTED = true;
            if(DDLCleanUp){
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
            throw new DeadLockDetectedException("DEADLOCK DETECTED");
        }
    }

    public void lockInterruptibly() throws InterruptedException{
        if(DDLFastFail && DDLdeadlockDETECTED){
            throw new DeadLockDetectedException("EARILER DEADLOCK DETECTED");
        }
        if(isHeldByCurrentThread()){
            if(debugging){
                System.out.println("Already Own Lock");
            }
            try{
                super.lockInterruptibly();
            } finally {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
            return;
        }

        markAsHardwait(hardwaitingThreads, Thread.currentThread());
        if(canThreadWaitOnLock(Thread.currentThread(), this)){
            if(debugging){
                System.out.println("Waiting For Lock");
            }
            try {
                super.lockInterruptibly();
            } finally {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
            if(debugging){
                System.out.println("Got New Lock");
            }
        }else {
            DDLdeadlockDETECTED = true;
            if(DDLCleanUp) {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
            throw new DeadLockDetectedException("DEADLOCK DETECTED");
        }
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if(DDLFastFail && DDLdeadlockDETECTED){
            throw new DeadLockDetectedException("EARILER DEADLOCK DETECTED");
        }
        if(unit.toSeconds(time) < DDLHWSWTime){
            return super.tryLock(time, unit);
        }

        if(isHeldByCurrentThread()){
            if(debugging){
                System.out.println("Already Own Lock");
            }
            try{
                return super.tryLock(time, unit);
            } finally {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
        }
        markAsHardwait(hardwaitingThreads, Thread.currentThread());
        if(canThreadWaitOnLock(Thread.currentThread(), this)){
            if(debugging){
                System.out.println("Waiting For Lock");
            }
            try{
                return super.tryLock(time, unit);
            } finally {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
                if(debugging){
                    System.out.println("Got New Lock");
                }
            }
        }else {
            DDLdeadlockDETECTED = true;
            if(DDLCleanUp) {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
            throw new DeadLockDetectedException("DEADLOCK DETECTED");
        }
    }

    public class DeadLockDetectingCondition implements Condition {
        Condition embedded;
        protected DeadLockDetectingCondition(ReentrantLock lock, Condition e){
            embedded = e;
        }
        public void await() throws InterruptedException {
            try{
                markAsHardwait(hardwaitingThreads, Thread.currentThread());
                embedded.await();
            } finally {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
        }


        public void awaitUninterruptibly() {
            markAsHardwait(hardwaitingThreads, Thread.currentThread());
            embedded.awaitUninterruptibly();
            freeIfHardwait(hardwaitingThreads, Thread.currentThread());
        }


        public long awaitNanos(long nanosTimeout) throws InterruptedException {
            try {
                markAsHardwait(hardwaitingThreads, Thread.currentThread());
                return embedded.awaitNanos(nanosTimeout);
            } finally {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
        }


        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            try {
                markAsHardwait(hardwaitingThreads, Thread.currentThread());
                return embedded.await(time, unit);
            } finally {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
        }

        public boolean awaitUntil(Date deadline) throws InterruptedException {
            try {
                markAsHardwait(hardwaitingThreads, Thread.currentThread());
                return embedded.awaitUntil(deadline);
            } finally {
                freeIfHardwait(hardwaitingThreads, Thread.currentThread());
            }
        }


        public void signal() {
            embedded.signal();
        }

        public void signalAll() {
            embedded.signalAll();
        }
    }

    public Condition  newCondition(){
        return new DeadLockDetectingCondition(this, super.newCondition());
    }

    private static Lock a = new DeadLockDetectingLock(false, true);
    private static Lock b = new DeadLockDetectingLock(false, true);
    private static Lock c = new DeadLockDetectingLock(false, true);

    private static Condition wa = a.newCondition();
    private static Condition wb = b.newCondition();
    private static Condition wc = c.newCondition();

    private static void delaySeconds(int seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex){

        }
    }

    private static void awaitSeconds(Condition c, int seconds){
        try {
            c.await(seconds, TimeUnit.SECONDS);
        } catch (InterruptedException ex){

        }
    }

    private static void testOne(){
        new Thread(new Runnable(){
            public void run() {
                System.out.println("thread one grab a");
                a.lock();
                delaySeconds(DELAY_TWO_SECOND);
                System.out.println("thread one grab b");
                b.lock();
                delaySeconds(DELAY_TWO_SECOND);
                a.unlock();
                b.unlock();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                System.out.println("thread two grab b");
                b.lock();
                delaySeconds(DELAY_TWO_SECOND);
                System.out.println("thread two grab a");
                a.lock();
                delaySeconds(DELAY_TWO_SECOND);
                a.unlock();
                b.unlock();
            }
        }).start();
    }

    private static void testTwo(){
        new Thread(new Runnable() {
            public void run() {
                System.out.println("thread one grab a");
                a.lock();
                delaySeconds(DELAY_TWO_SECOND);
                System.out.println("thread one grab b");
                b.lock();
                delaySeconds(DEALY_TEN_SECOND);
                a.unlock();
                b.unlock();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                System.out.println("thread two grab b");
                b.lock();
                delaySeconds(DELAY_TWO_SECOND);
                System.out.println("thread two grab c");
                c.lock();
                delaySeconds(DEALY_TEN_SECOND);
                b.unlock();
                c.unlock();
            }
        }).start();
        new Thread(new Runnable(){
            public void run() {
                System.out.println("thread three grab c");
                c.lock();
                delaySeconds(DELAY_FOUR_SECOND);
                System.out.println("thread three grab a");
                a.lock();
                delaySeconds(DEALY_TEN_SECOND);
                c.unlock();
                a.unlock();
            }
        }).start();
    }

    private static void testThree(){
        new Thread(new Runnable() {
            public void run() {
                System.out.println("thread one grab b");
                b.lock();
                System.out.println("thread one grab a");
                a.lock();
                delaySeconds(DELAY_TWO_SECOND);
                System.out.println("thread one waits on b");
                awaitSeconds(wb, WAIT_TEN_SECOND);
                a.unlock();
                b.unlock();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                delaySeconds(DELAY_ONE_SECOND);
                System.out.println("thread two grab b");
                b.lock();
                System.out.println("thread two grab a");
                a.lock();
                delaySeconds(DEALY_TEN_SECOND);
                b.unlock();
                a.unlock();
            }
        }).start();
    }

    public static void main(String[] args){
        int test = 3;
        if(args.length > 0){
            test = Integer.parseInt(args[0]);
        }
        switch (test){
            case 1:
                testOne();
                break;
            case 2:
                testTwo();
                break;
            case 3:
                testThree();
                break;
            default:
                System.err.println("usage: java DeadLockDetectingLock [ test# ]");
        }
        delaySeconds(DELAY_SIXTY_SECOND);
        System.out.println("--- End Program ---");
        System.exit(EXIT_STATUS);
    }

}
