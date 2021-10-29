package tools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Readpcap {

    public Readpcap(){
        
    }

    public void readfile(String path) {
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            String curLine;
            while ((curLine = bufferedReader.readLine()) != null) {
                // process the line as required
                System.out.println(curLine);
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("An error occured");
        }
    }

    private static final String UNKNOWN_CHARACTER = ".";


    public static void main(String[] args) throws IOException {

        String file = "/path/to/text.txt";

        String s = convertFileToHex(Paths.get(file));
        System.out.println(s);
    }

    public static String convertFileToHex(Path path) throws IOException {

        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File not found! " + path);
        }

        StringBuilder result = new StringBuilder();
        StringBuilder hex = new StringBuilder();
        StringBuilder input = new StringBuilder();
        
        StringBuilder raw = new StringBuilder();
        int count = 0;
        int value;

        // path to inputstream....
        try (InputStream inputStream = Files.newInputStream(path)) {

            while ((value = inputStream.read()) != -1) {

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

}