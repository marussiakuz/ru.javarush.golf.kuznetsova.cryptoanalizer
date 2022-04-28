import java.io.*;
import java.util.*;

public class Cryptanalysis {
    private static Map<Character, Integer> characterFrequency = new HashMap<>();

    /*static {
        for (Character character : new CaesarsCipher().getAlphabet()) {
            characterFrequency.put(character, 0);
        }
    }*/

    public TreeMap<Double, Character> getStatistic (String text) {
        /*SortedSet<Map.Entry<Character, Double>> percentageRatio = new TreeSet<>(
                new Comparator<>() {
                    @Override
                    public int compare(Map.Entry<Character, Double> e1,
                                       Map.Entry<Character, Double> e2) {
                        return e1.getValue().compareTo(e2.getValue());
                    }
                });*/
        //TreeMap<Character, Double> percentageRatio = new TreeMap<>(new Comparator<Double>() {
            /*@Override
            public int compare(Double o1, Double o2) {
                return 0;
            }
        });*/

        TreeMap<Double, Character> percentageRatio = new TreeMap<>();
        char[] chars = text.toCharArray();
        for (char aChar : chars) {
            if (characterFrequency.containsKey(aChar)) {
                int frequency = characterFrequency.get(aChar);
                characterFrequency.put(aChar, ++frequency);
            }
            else characterFrequency.put(aChar, 1);
        }
        for (Character character : characterFrequency.keySet()) {
            double percent = (double) characterFrequency.get(character)*100/chars.length;
            percentageRatio.put(percent, character);
            //percentageRatio.put(character, percent);
        }
        return percentageRatio;
    }

    public HashMap<Character, Character> getMatchingCharacters (String fileSrc, String exampleFile) {
        HashMap<Character, Character> association = new HashMap<>();
        try (FileInputStream srcInput = new FileInputStream(fileSrc);
             FileInputStream exampleInput = new FileInputStream(exampleFile);
             InputStreamReader srcStreamReader = new InputStreamReader(srcInput);
             InputStreamReader exampleStreamReader = new InputStreamReader(exampleInput);
             BufferedReader srcReader = new BufferedReader(srcStreamReader);
             BufferedReader exampleReader = new BufferedReader(exampleStreamReader)) {
            StringBuilder buildExampleText = new StringBuilder();
            StringBuilder buildSrcText = new StringBuilder();
            while (exampleReader.ready()) {
                buildExampleText.append(exampleReader.ready());
            }
            while (srcReader.ready()) {
                buildSrcText.append(srcReader.read());
            }
            TreeMap<Double, Character> example = new Cryptanalysis().getStatistic(buildExampleText.toString());
            TreeMap<Double, Character> src = new Cryptanalysis().getStatistic(buildSrcText.toString());
            for (Double aDouble : src.keySet()) {
                association.put(src.get(aDouble), example.get(example.floorKey(aDouble)));
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении из файла, попробуйте снова");
        }
        return association;
    }

    public static void main(String[] args) {
        System.out.println("Введите путь к файлу или имя файла с текстом, который нужно расшифровать");
        Scanner scanner = new Scanner(System.in);
        String fileSrc = scanner.next();
        String fileResult = fileSrc.replace(".txt", "_DecryptedByStylisticAnalysis.txt");
        System.out.println("Введите путь к файлу или имя файла с похожим текстом");
        String exampleFile = scanner.next();
        HashMap<Character, Character> matchingCharacters = new Cryptanalysis().getMatchingCharacters(fileSrc,
                exampleFile);
        try (FileReader reader = new FileReader(fileSrc);
             FileWriter writer = new FileWriter(fileResult)) {
            while (reader.ready()) {
                char x = (char) reader.read();
                if (matchingCharacters.containsKey(x)) x = matchingCharacters.get(x);
                writer.write(x);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении из файла, попробуйте снова");
        }
    }
}
