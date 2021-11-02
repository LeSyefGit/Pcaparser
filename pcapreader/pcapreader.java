package pcapreader;
import tools.*;
import java.nio.file.Paths;

public class pcapreader {
    public static void main(String[] args){

        String file = "capture.pcap";
        String s ="";
        try{
            s = Readpcap.convertFileToHex(Paths.get(file));
        }catch(Exception e){
            System.out.println("there was an error");
        }

        //System.out.println(s);
    }

}
