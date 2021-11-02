package tools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Readpcap {
    static final String UNKNOWN_CHARACTER = ".";
    static boolean flagGHdr;
    static boolean flagHdr;

    static List<Object> pktList = new ArrayList<>();


    public static String convertFileToHex(Path path) throws IOException, NumberFormatException {

        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File not found! " + path);
        }

        StringBuilder result = new StringBuilder();
        StringBuilder hex = new StringBuilder();
        StringBuilder input = new StringBuilder();
        
        StringBuilder raw = new StringBuilder();
        StringBuilder tmp = new StringBuilder();

        String hdr = tmp.toString().replace(" ", "");
        String header = tmp.reverse().toString().replace(" ", "");

        int count = 0;
        int value;
        int n= 0;
        int m=0 ;
        boolean isinv= true;
        int pktLenght=0 ;
        
        // path to inputstream....
        try (InputStream inputStream = Files.newInputStream(path)) {
            while ((value = inputStream.read()) != -1) {

                tmp.append(String.format("%02X ", value));
                if(n == 23){ // < >
                    hdr = tmp.toString().replace(" ", "");
                    header = tmp.reverse().toString().replace(" ", "");
                    if(hdr.substring(0,2).equals("D4")){  // swapped
                        System.out.println(Integer.parseInt(readNBytes(inv2(header), 4, 8),16));
                        pktList.add(new GlobalHeader(readNBytes(inv2(header), 4, 40),
                                                   Integer.parseInt(readNBytes(inv2(header), 2, 36),16),
                                                   Integer.parseInt(readNBytes(inv2(header), 2, 32),16),
                                                   Integer.parseInt(readNBytes(inv2(header), 4, 24),16),
                                                   Integer.parseInt(readNBytes(inv2(header), 4, 16),16),
                                                   Integer.parseInt(readNBytes(inv2(header), 4, 8),16),
                                                   Integer.parseInt(readNBytes(inv2(header), 4, 0),16)));
                        
                        isinv = true;
                    }else{
                        pktList.add(new GlobalHeader(readNBytes(inv2(hdr), 4, 0),
                                                   Integer.parseInt(readNBytes(inv2(hdr), 2, 8),16),
                                                   Integer.parseInt(readNBytes(inv2(hdr), 2, 12),16),
                                                   Integer.parseInt(readNBytes(inv2(hdr), 4, 16),16),
                                                   Integer.parseInt(readNBytes(inv2(hdr), 4, 24),16),
                                                   Integer.parseInt(readNBytes(inv2(hdr), 4, 32),16),
                                                   Integer.parseInt(readNBytes(inv2(hdr), 4, 40),16)));
                        isinv = false;
                    }
                    tmp.setLength(0);
                    System.out.println(("Global header: \n"+pktList.get(0).toString()));

                    
                }
                n++;


                if(n == 16+24 && isinv){ // < >
                    hdr = tmp.toString().replace(" ", "");
                    header = tmp.reverse().toString().replace(" ", "");
                    System.out.println(inv2(header));
                    System.out.println(Integer.parseInt(readNBytes(inv2(header), 4, 0),16));
                    System.out.println(Integer.parseInt(readNBytes(inv2(header), 4, 8),16));
                    System.out.println(Integer.parseInt(readNBytes(inv2(header), 4, 16),16));
                    System.out.println(Integer.parseInt(readNBytes(inv2(header), 4, 24),16));

                    pktList.add(new Pkt(new PktHeader(Integer.parseInt(readNBytes(inv2(header), 4, 0),16),
                                                Integer.parseInt(readNBytes(inv2(header), 4, 8),16),
                                                Integer.parseInt(readNBytes(inv2(header), 4, 16),16),
                                                Integer.parseInt(readNBytes(inv2(header), 4, 24),16))
                                                ,
                                                "dkljf")
                                                );
                    //m=0;
                    tmp.setLength(0);
                    System.out.println(("Global header: \n"+pktList.get(1).toString()));
                }

                if(n == 15+23 && !isinv){ // < >
                    hdr = tmp.toString().replace(" ", "");
                    header = tmp.reverse().toString().replace(" ", "");
                    
                    System.out.println(Integer.parseInt(readNBytes(inv2(hdr), 4, 24),16));

                    pktList.add(new Pkt(new PktHeader(Integer.parseInt(readNBytes(inv2(hdr), 4, 0),16),
                                                Integer.parseInt(readNBytes(inv2(hdr), 4, 8),16),
                                                Integer.parseInt(readNBytes(inv2(hdr), 4, 16),16),
                                                Integer.parseInt(readNBytes(inv2(hdr), 4, 24),16))
                                                , 
                                                
                                                "dkljf")
                                                );
                    //m=0;
                    tmp.setLength(0);
                    System.out.println(("Global header: \n"+pktList.get(1).toString()));
                }

               // m++;
                
                ///// for writing raw dump files


                hex.append(String.format("%02X ", value));

                // If the character is unable to convert, just prints a dot "."
                if (!Character.isISOControl(value)) {
                    raw.append(String.format("%02X ", value) + " " +(char)value +"\n");
                    input.append((char) value);
                } else {
                    input.append(UNKNOWN_CHARACTER);
                }
                // After 16 bytes, reset everything for formatting purpose
                if (count == 15) {
                    result.append(String.format("%40s | %s%n", hex, input));
                    hex.setLength(0);
                    input.setLength(0);
                    count = 0;
                } else {
                    count++;
                }


                
            }

            // if the count>0, meaning there is remaining content
            if (count > 0) {
                result.append(String.format("%-40s | %s%n", hex, input));
            }
            PrintWriter writer1 = new PrintWriter("rawdump.txt", "UTF-8");
            writer1.write(raw.toString());
            writer1.close();

            PrintWriter writer = new PrintWriter("hexdump.txt", "UTF-8");
            writer.write(result.toString());
            writer.close();
        }

        return result.toString();
    }

    public static String inv2(String s){
        String result="";
        for (int i=0; i< s.length()/2 ; i++){
                result +=Character.toString(s.charAt(2*i+1)) + Character.toString(s.charAt(2*i));
        }
        return result;
    }

    public static String readNBytes(String s, int n, int index ){
        String result="";
        for (int i=index; i< index+n*2 ; i++){

                result +=s.charAt(i);
        }
        return result;
    }

}