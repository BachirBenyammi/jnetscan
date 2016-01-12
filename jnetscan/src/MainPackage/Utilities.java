package MainPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import javax.swing.JTextArea;

/**
 *
 * @author benbac
 */

public class Utilities {

    /** Execute line command and get back its result */
    public static void executeCmd(JTextArea textarea, String cmd)
    {
        String line="";
        try 
        {    
            Process command = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader
                (command.getInputStream()));
            while ((line = in.readLine()) != null)
                    textarea.append("\n" + line);   
            in.close();                
        }      
            catch (IOException ioEx) {} 
    }    
   
    public static void ExecuteProcess (String process)
    {
        try 
        {          
            Process p = Runtime.getRuntime().exec(process);
            p.waitFor();
        }      
            catch (Exception Ex) {}             
    }    
    
    
    /** Convert string to integer*/
    public static int StrToInt(String str){
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());            
            return -1;
        }
    }      
    
    public static boolean ValidMachine(String machine)
    {        
        return Pattern.matches("/*(\\w|\\.)+:\\d+", machine);
    }
}
