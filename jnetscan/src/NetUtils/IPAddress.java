package NetUtils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author benbac
 */

public class IPAddress {
    
    /** Return true if IPAdr is a valid ip adresse */
    public static boolean validIPAddress(String IPAdr) {
        StringTokenizer st = new StringTokenizer(IPAdr, ".");
        if (st.countTokens()!= 4) return false;
        while (st.hasMoreTokens()){
            try {
                int value = Integer.parseInt(st.nextToken());
                if (value < 0 || value > 255) return false;     
            } catch (NumberFormatException e) { return false;}    
        }
        return true;
    }
    
    /** Return string array list of ip addresses from spesific text */
    public static ArrayList<Integer> getIPAddresses (String object)
    {
        int FirstInt = getFirstAdrHost(object);
        int LastInt = getLastAdrHost(object);
        int count = LastInt - FirstInt+1;
        ArrayList<Integer> list = new ArrayList<Integer>(count);
        for(int i=count; i>0; i--)
            list.add(i+FirstInt-1);  
        return list;
    } 
    
    /** Return true if object formats is similar to x.x.x.x or x.x.x.0 or x.x.x.x-x */
    public static boolean validObject(String object) {
        if (object == null) return false;
        int FirstInt, LastInt;
        String FirstIP, LastIP;        
        FirstIP = LastIP = object;
      
        int sprpos = object.indexOf("-");
        if (sprpos != -1) { // Object format x.x.x.x-x
            FirstIP = object.substring(0, sprpos);

            LastIP = object.substring(sprpos + 1);
            LastIP = getNetAdrPart(object) + LastIP;

            FirstInt = getHostAdrPart(FirstIP); 
            LastInt = getHostAdrPart(LastIP);               
        }
        else {
            // Object format x.x.x.x 
            int zerostr = getHostAdrPart(object);
            if (zerostr == 0) { // IP Adr format x.x.x.0
                FirstInt = 1;
                LastInt = 254;
            }
            else
                FirstInt = LastInt = zerostr;     
        }

        // Return true if we have a valid ip addresses
        return (FirstInt != -1) && (LastInt != -1) && (LastInt >= FirstInt) 
            && validIPAddress(FirstIP) && validIPAddress(LastIP);
    }   
    
    /** Return last part of ip address (host number) */
    public static int getHostAdrPart(String object){
        try {
            return Integer.parseInt(object.substring(object.lastIndexOf(".") + 1));
        } catch (NumberFormatException e) {return -1;}
    }
    
    /** Return network addresse part */
    public static String getNetAdrPart(String IPAdr){
        return IPAdr.substring(0, IPAdr.lastIndexOf(".") + 1);
    }
    
    /** Return first host number in a specific object */
    public static int getFirstAdrHost(String object){
        if (object == null) return -1;
        int FirstInt = 0;
      
        int sprpos = object.indexOf("-");
        if (sprpos != -1)
            FirstInt = getHostAdrPart(object.substring(0, sprpos));               
        else {
            int hostpart = getHostAdrPart(object);
            if (hostpart == 0) 
                FirstInt = 1;
            else 
                FirstInt = hostpart;     
        }        
        return FirstInt;
    }

    /** Return last host number in a specific object */
    public static int getLastAdrHost(String object){
        if (object == null) return -1;
        int LastInt = 0;
      
        int sprpos = object.indexOf("-");
        if (sprpos != -1)
            LastInt = getHostAdrPart(getNetAdrPart(object) + object.substring(sprpos + 1));               
        else {
            int hostpart = getHostAdrPart(object);
            if (hostpart == 0)
                LastInt = 254;
            else
                LastInt = hostpart;     
        }        
        return LastInt;        
    }  
}