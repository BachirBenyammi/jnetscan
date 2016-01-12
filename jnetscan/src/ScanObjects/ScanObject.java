package ScanObjects;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author benbac
 */
public class ScanObject extends Thread {  

    protected int currentHost = 0;
    protected int foundHosts = 0;
    public ArrayList<Integer> list;
    private Scan scan;

    public ScanObject(Scan scan) {
        // initialisation
        currentHost = 0;
        foundHosts = 0;
        this.scan = scan;
        setAdrsList(scan.object);                   
    }

    public void setAdrsList(String object)
    {
        list = NetUtils.IPAddress.getIPAddresses(object);          
    }
    
    public  void StopScan(){ 
        currentHost = scan.COUNT;
    }
    
    public  boolean isDone(){ 
        return currentHost == scan.COUNT; 
    }    
   
    // Remove element from the list, Scan it, add its results to the table
    public  void getNextListElement()
    {
        String IP = "";
        int index = 0;
        
        synchronized (list) {                
            try { 
                index = list.size() - 1;
                IP = scan.NetAdr + list.remove(index);                        
            } catch (UnsupportedOperationException usoe) 
              {System.out.println(usoe.getMessage());}
        }
        String element = NetUtils.NetIntes.getInetAddressData(IP, 
                scan.ReachNetInter, 
                scan.TIMEOUT); 
        String host = element.substring(0, element.indexOf("\t"));
        Boolean reachable = new Boolean(element.substring(element.indexOf("\t") + 1, 
                element.lastIndexOf("\t")));
        int response = new Integer(element.substring(element.lastIndexOf("\t")+1));
        
        if (isDone()) return;
    
        scan.of.AddNewRow(scan.COUNT - index -1, 
                new Object[]{host, reachable, response});   
        if (reachable  == true) foundHosts++;          
        currentHost++;       
    }
    
    @Override
    public void run() {
        int numWorkers = scan.COUNT;
        ExecutorService tpes = Executors.newCachedThreadPool();
        CallableWorkerThread workers[] = new CallableWorkerThread[numWorkers];
        Future futures[] = new Future[numWorkers];

       for (int i = 0; i < numWorkers; i++) {
        try {
            workers[i] = new CallableWorkerThread(i);
            futures[i] = tpes.submit(workers[i]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
       }

       for (int i = 0; i < numWorkers; i++) {
        try {
            futures[i].get();
        } catch (Exception e) {
            System.out.println(e.getMessage()); 
        }
       }        
          
       throw new UnsupportedOperationException("Not supported yet.");
    }
    
    class CallableWorkerThread implements Callable<Integer> {
        private int workerNumber; 

        CallableWorkerThread(int number) {
            workerNumber = number;
        }

        public Integer call() {                   
            getNextListElement();        
            return(workerNumber);
        }
    }        
}
