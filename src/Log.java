
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class Log {
    
    private static final String name = "gsp4pdb-";
    private static final String extension = ".log";
    private static boolean firstTime = true;
    private static String time;
    
    public static void writeAndShow (String class_name, String method_name, String message){
        write (class_name, method_name, message);
        String line = "["+ class_name +"]" + "[" + method_name + "] " + message;
        System.out.println(message);
    }
    
    
    public static void write (String class_name, String method_name, String message){
        
        if (firstTime){
            time = getDateTime();
            firstTime = false;
        }
        try {
            FileWriter fw = new FileWriter(name + time + extension, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            String line = "["+ class_name +"]" + "[" + method_name + "] " + message;
            out.println(line);
            out.close();
        } catch (IOException ex) {
            //Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("[Log.java][Write] Error writing log");
        }
    }
    
    public static String getDateTime(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        return "" + day + month + year + hour + minute + second;
    }
}
