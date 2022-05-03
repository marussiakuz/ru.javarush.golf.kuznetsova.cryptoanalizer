import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);
    private static String userInput;
    private static CaesarsCipher caesarsCipher = new CaesarsCipher();

    public static void main (String[] args) {
        while (true) {
            printMenu();
            userInput = scanner.next();

            try {
                switch (userInput) {
                    case "1" -> encryptText();
                    case "2" -> decryptText();
                    case "3" -> decryptByBruteForce();
                    case "0" -> {
                        System.out.println("exit");
                        return;
                    }
                    default -> System.out.println("There is no such command, please try again");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading from a file, check the file name and try again");
            }
        }
    }

    public static void printMenu() {
        System.out.println("What do you want to do? ");
        System.out.println("1 - Encrypt text");
        System.out.println("2 - Decrypt the text with a known key");
        System.out.println("3 - Decrypt the text with an unknown key");
        System.out.println("0 - Exit");
    }

    public static void encryptText() throws IOException {
        System.out.println("Enter the path to the file or the name of the file with the text you want to encrypt");
        String fileSrc = scanner.next();
        String fileResult = fileSrc.replace(".txt", "_Encrypted.txt");
        int letterOffset = initializeTheKey();

        try (FileReader reader = new FileReader(fileSrc);
             FileWriter writer = new FileWriter(fileResult)) {
            while (reader.ready()) {
                char current = (char) reader.read();
                if (caesarsCipher.contains(current)) current = caesarsCipher.encryptChar(current, letterOffset);
                writer.write(current);
            }
        }
        System.out.println(String.format("Success. The encrypted text is in the %s. The decryption key is %s",
                fileResult, letterOffset));
    }

    public static void decryptText() throws IOException {
        System.out.println("Enter the path to the file or the name of the file with the text you want to decrypt");
        String fileSrc = scanner.next();
        String fileResult = fileSrc.replace(".txt", "_Decrypted.txt");
        System.out.println("Enter the decryption key");
        int letterOffset = 0;
        try {
            letterOffset = Integer.parseInt(scanner.next());
        } catch (NumberFormatException e) {
            System.out.println("The key was entered incorrectly, it must have a numeric value and does not contain any"
                    + " letters or symbols");
        }

        try (FileReader reader = new FileReader(fileSrc);
             FileWriter writer = new FileWriter(fileResult)) {
            while (reader.ready()) {
                char current = (char) reader.read();
                if (caesarsCipher.contains(current)) current = caesarsCipher.decryptChar(current, letterOffset);
                writer.write(current);
            }
        }
        System.out.println(String.format("Success. The decrypted text is in the %s.", fileResult));
    }

    public static void decryptByBruteForce () throws IOException {
        System.out.println("Enter the path to the file or the name of the file with the text you want to decrypt");
        String fileSrc = scanner.next();
        int letterOffset = determineTheLetterOffset(fileSrc);
        String fileResult = fileSrc.replace(".txt", "_DecryptedUsingBrute.txt");

        try (FileReader reader = new FileReader(fileSrc);
             FileWriter writer = new FileWriter(fileResult)) {
            while (reader.ready()) {
                char current = (char) reader.read();
                if (caesarsCipher.contains(current)) current = caesarsCipher.decryptChar(current, letterOffset);
                writer.write(current);
            }
        }
        System.out.println(String.format("Success. The decrypted text is in the %s", fileResult));
    }

    public static int determineTheLetterOffset (String file) throws IOException {
        int letterOffset = 0;
        try (FileReader reader = new FileReader(file)) {
            ArrayList<Character> chars = new ArrayList<>();
            while (reader.ready() && chars.size()<=1000) {
                char read = (char)reader.read();
                chars.add(read);
            }
            letterOffset = caesarsCipher.determineTheOffset(chars);
        }
        return letterOffset;
    }

    public static int initializeTheKey () {
        System.out.println("Enter any positive integer greater than zero");
        boolean isTheKeyEnteredCorrectly = false;
        int key = 0;
        while (!isTheKeyEnteredCorrectly) {
            userInput = scanner.next();
            try {
                key = Integer.parseInt(userInput);
                if (key < 0) {
                    System.out.println("A negative number was entered. Please enter a positive integer greater than "
                            + "zero");
                }
                else if (key == 0) {
                    System.out.println("The encryption key cannot be zero, since the text will not be encrypted. Please"
                            + " try again");
                }
                else isTheKeyEnteredCorrectly = true;
            } catch (NumberFormatException e) {
                System.out.println("A non-integer numeric value has been entered, it is necessary to enter a positive "
                        + "integer greater than zero. Please try again");
            }
        }
        return key;
    }
}
