package javathreads.examples.ch15;

/**
 * Created by hombre on 2015/8/27.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/27 15:12
 */
public class LoopHandler implements Runnable {
    protected class LoopRange{
        public int start, end;
    }
    protected Thread lookupThreads[];
    protected int startLoop, endLoop, curLoop, numThreads;

    public LoopHandler(int start, int end, int threads){
        startLoop = curLoop = start;
        endLoop = end;
        numThreads = threads;
        lookupThreads = new Thread[numThreads];
    }
    protected synchronized LoopRange loopGetRange(){
        if(curLoop >= endLoop)
            return null;
        LoopRange ret = new LoopRange();
        ret.start = curLoop;
        curLoop += (endLoop - startLoop) / numThreads + 1;
        ret.end = (curLoop < endLoop) ? curLoop : endLoop;
        return ret;
    }

    public void loopDoRange(int start, int end){
    }

    public void loopProcess(){
        for(int i = 0; i < numThreads; i++){
            lookupThreads[i] = new Thread(this);
            lookupThreads[i].start();
        }
        for(int i = 0; i < numThreads; i++){
            try {
                lookupThreads[i].join();
                lookupThreads[i] = null;
            } catch (InterruptedException iex){}
        }
    }

    public void run() {
        LoopRange str;
        while((str = loopGetRange()) != null){
            loopDoRange(str.start, str.end);
        }
    }

}
