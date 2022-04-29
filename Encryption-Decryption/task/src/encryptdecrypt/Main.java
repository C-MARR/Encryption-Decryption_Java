package encryptdecrypt;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> options = new ArrayList<>(List.of(args));
        String mode = options.contains("-mode") ? args[options.indexOf("-mode") + 1] : "enc";
        String encodeType = options.contains("-alg") ? args[options.indexOf("-alg") + 1] : "shift";
        int key = options.contains("-key") ? Integer.parseInt(args[options.indexOf("-key") + 1]) : 0;
        String data = options.contains("-data") ? args[options.indexOf("-data") + 1] : "";
        if (options.contains("-in") && !options.contains("-data")) {
            File input = new File(args[options.indexOf("-in") + 1]);
            try (FileReader reader = new FileReader(input)) {
                StringBuilder readData = new StringBuilder();
                var charc = reader.read();
                while (charc != -1) {
                    readData.append((char) charc);
                    charc = reader.read();
                }
                data = readData.toString();
            } catch (IOException e) {
                System.out.println("Error");
            }
        }
        if (mode.equals("dec")) key -= (key * 2);
        StringBuilder changed = new StringBuilder();
        for (char letter : data.toCharArray()) {
            if (!"shift".equals(encodeType)) {
                changed.append((char) (letter + key));
            } else if (Character.isUpperCase(letter) && !Character.isUpperCase(letter + key) ||
                      Character.isLowerCase(letter) && !Character.isLowerCase(letter + key)) {
                changed.append((char) (letter + key + (key > 0 ? -26 : 26)));
            } else {
                changed.append(!Character.isLetter(letter) ? letter : (char) (letter + key));
            }
        }
        if (!options.contains("-out")) {
            System.out.println(changed);
        } else {
            File output = new File(args[options.indexOf("-out") + 1]);
            try (FileWriter writer = new FileWriter(output)) {
                for (char letter : changed.toString().toCharArray()) {
                    writer.write(letter);
                }
            } catch (IOException e) {
                System.out.println("Error");
            }
        }
    }
}
