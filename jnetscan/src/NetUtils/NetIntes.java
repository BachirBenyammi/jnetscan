package NetUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author benbac
 */
public class NetIntes {
    
    /** Return list of local network interfaces devices names */
    public static Vector getListNetIntesNames() throws SocketException
    {    
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        Vector <String>vector = new Vector<String>();
        for (NetworkInterface netint : Collections.list(nets))
            vector.add(netint.getDisplayName());   
        return vector;
    }     
    
    /** Return infos about a specific network interface device */
    public static Vector getNetInteDataByIndex(int index) throws SocketException
    {    
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        NetworkInterface netint = Collections.list(nets).get(index);
        
        Vector <String>vector = new Vector<String>();
        vector.add("Name: " + netint.getName());
        String output ="";
        
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses))
            output += " " + inetAddress;
        vector.add("InetAddress: " + output);
        
        //MAC (Media Access Control ), Physical hardware address
        vector.add("Hardware address: " + Arrays.toString(netint.getHardwareAddress()));
        vector.add("Up? " + String.valueOf(netint.isUp())); // Runnning                        
        vector.add("Loopback? " + String.valueOf(netint.isLoopback()));
        //PPP connection through a modem
        vector.add("PointToPoint? " + String.valueOf(netint.isPointToPoint())); 
        vector.add("Supports multicast? " + String.valueOf(netint.supportsMulticast()));
        vector.add("Virtual? " + String.valueOf(netint.isVirtual())); // subinterface
        //Maximum Transmission Unit (the largest packet size)            
        vector.add("MTU: " + String.valueOf(netint.getMTU()));  
            
        /*Enumeration<NetworkInterface> subIfs = netint.getSubInterfaces();
        for (NetworkInterface subIf : Collections.list(subIfs)) {
            vector.add("Sub Interface Display name: " + subIf.getDisplayName());
            vector.add("Sub Interface Name: " + subIf.getName());
            for (InetAddress inetAddress : Collections.list(subIf.getInetAddresses())) {
                output += " " + inetAddress;
                vector.add("InetAddress: " + output);                
            }            
        }*/

        return vector;
    }     
    
    /** return host name and reachability of a specific ip address */
    public static String getInetAddressData(String IPAdr, String RechNetInter, int timeout)
    {
        String host, output = ""; 
        try {
            InetAddress address = InetAddress.getByName(IPAdr);
            host = address.getHostName();
            if (!host.equals(IPAdr))
                output = host;
            
            long timeInMillis = System.currentTimeMillis();   
            
            /*try {
                Process p = Runtime.getRuntime().exec("ping -n 1 " + IPAdr);
                //output += "\t " + (p.waitFor()==0 ? "true" : "false");
                if (p.waitFor() == 0)
                    output += "\ttrue\t" + (System.currentTimeMillis() - timeInMillis);
                else
                     output += "\tfalse\t0";
            }        
            catch (InterruptedException ie) {}*/
            
            if (address.isReachable(NetworkInterface.getByName(RechNetInter), 0, timeout))
                output += "\ttrue\t" + (System.currentTimeMillis() - timeInMillis);
            else
                output += "\tfalse\t0";              
    
        } catch (UnknownHostException e) { 
            System.err.println("Unable to lookup " + IPAdr);
        }  catch (IOException e) {
            System.err.println("Unable to reach " + IPAdr);
        }
        return output;        
    }      

}
