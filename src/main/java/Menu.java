import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);
    private static String userInput;
    private static CaesarsCipher caesarsCipher = new CaesarsCipher();
    private static int letterOffset;

    public static void main (String[] args) throws IOException {
        while (true) {
            printMenu();
            userInput = scanner.next();

            switch (userInput) {
                case "1" -> encryptText();
                case "2" -> decryptText();
                case "3" -> decryptByBruteForce();
                case "0" -> {
                    System.out.println("Выход");
                    return;
                }
                default -> System.out.println("Ошибка, такой команды нет.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("Что вы хотите сделать? ");
        System.out.println("1 - Зашифровать текст");
        System.out.println("2 - Расшифровать текст с известным ключем");
        System.out.println("3 - Расшифровать текст с неизвестным ключем");
        System.out.println("0 - Выход");
    }

    public static void encryptText() {
        System.out.println("Введите путь к файлу или имя файла с текстом, который хотите зашифровать");
        String fileSrc = scanner.next();
        String fileResult = fileSrc.replace(".txt", "_Encrypted.txt");
        letterOffset = new Random().nextInt(72) + 1;
        try (FileReader reader = new FileReader(fileSrc);
             FileWriter writer = new FileWriter(fileResult)) {
            while (reader.ready()) {
                char current = (char) reader.read();
                if (caesarsCipher.contains(current)) current = caesarsCipher.encryptChar(current, letterOffset);
                writer.write(current);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении из файла, попробуйте снова");
        }
        System.out.println(String.format("Готово. Зашифрованный текст находится в %s. Ключ для расшифровки: %s",
                fileResult, letterOffset));
    }

    public static void decryptText() {
        System.out.println("Введите путь к файлу или имя файла с текстом, который хотите расшифровать");
        String fileSrc = scanner.next();
        String fileResult = fileSrc.replace(".txt", "_Decrypted.txt");
        System.out.println("Введите ключ, которым зашифрован текст");
        int letterOffset = 0;
        try {
            letterOffset = Integer.parseInt(scanner.next());
        } catch (NumberFormatException e) {
            System.out.println("Неправильно введен ключ, ключ имеет числовое значение и не может содержать буквы или "
                    + "символы");
        }
        try (FileReader reader = new FileReader(fileSrc);
             FileWriter writer = new FileWriter(fileResult)) {
            while (reader.ready()) {
                char current = (char) reader.read();
                if (caesarsCipher.contains(current)) current = caesarsCipher.decryptChar(current, letterOffset);
                writer.write(current);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении из файла, попробуйте снова");
        }
        System.out.println(String.format("Готово. Расшифрованный текст находится в %s.", fileResult));
    }

    public static void decryptByBruteForce () {
        System.out.println("Введите путь к файлу или имя файла с текстом, который хотите расшифровать");
        String fileSrc = scanner.next();
        int letterOffset = determineTheOffsetLine(fileSrc);
        String fileResult = fileSrc.replace(".txt", "_DecryptedUsingBrute.txt");
        try (FileReader reader = new FileReader(fileSrc);
             FileWriter writer = new FileWriter(fileResult)) {
            while (reader.ready()) {
                char current = (char) reader.read();
                if (caesarsCipher.contains(current)) current = caesarsCipher.decryptChar(current, letterOffset);
                writer.write(current);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении из файла, попробуйте снова");
        }
        System.out.println(String.format("Готово. Расшифрованный текст находтся в %s", fileResult));
    }

    public static int determineTheOffsetLine (String file) {
        int letterOffset = 0;
        try (FileReader reader = new FileReader(file)) {
            ArrayList<Character> chars = new ArrayList<>();
            while (reader.ready() && chars.size()<=1000) {
                char read = (char)reader.read();
                chars.add(read);
            }
            letterOffset = caesarsCipher.determineTheOffset(chars);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении из файла, попробуйте снова");
        }
        return letterOffset;
    }
}
