package ScanObjects;

import MainPackage.ObjectFrame;
import MainPackage.*;
/**
 *
 * @author benbac
 */
public class Scan {
    
    public ScanObject so;
    public ScanPorts sp; 
    public ScanUpdates su;

    public int TIMEOUT = 3000; 
    public int COUNT = 0;
    public String PORTS = "21 23 80 445"; 
    public String ReachNetInter = "127.0.0.1";
    public String object = "127.0.0.1";
    public String NetAdr = "127.0.0.";   
    public ObjectFrame of;
    private Boolean running = false;

    public Scan(ObjectFrame of, String[] args)
    {
        this.of = of;
        IntitialScan(
            String.valueOf(args[0]),
            String.valueOf(args[1]),
            String.valueOf(args[2]),
            String.valueOf(args[3]));  
    }
    
    public void IntitialScan(String object, String ReachAdr, String timeout, String ports)
    {
        this.object = object;
        this.ReachNetInter = ReachAdr;
        this.TIMEOUT = Integer.valueOf(timeout);
        this.PORTS = ports;
        this.NetAdr = NetUtils.IPAddress.getNetAdrPart(object);        
        this.COUNT = 1 + NetUtils.IPAddress.getLastAdrHost(object) - 
                NetUtils.IPAddress.getFirstAdrHost(object);   
        sp = new ScanPorts(this);       
        so = new ScanObject(this);       
        su = new ScanUpdates(this); 
        of.InitialTableData(this.NetAdr, so.list, this.COUNT);           
    } 
    
    public  void Stop()
    {
        so.StopScan();   
        sp.StopScan();  
    }
    
    public  boolean isDone()
    {
        running = !(so.isDone() && sp.isDone());
        return so.isDone() && sp.isDone();
    }
    
    public  boolean isRunning()
    {
        return running;
    }    
}
