import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CaesarsCipher {
    private final static ArrayList<Character> ALPHABET = new ArrayList<>(List.of('А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж',
            'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы',
            'Ь', 'Э', 'Ю', 'Я', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п',
            'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', '.', ',', '”', ':', '-',
            '!', '?', ' '));

    public ArrayList<Character> getAlphabet () {
        return ALPHABET;
    }

    public char encryptChar (char current, int letterOffset) {
        return ALPHABET.get((ALPHABET.indexOf(current) + letterOffset)%ALPHABET.size());
    }

    public char decryptChar (char current, int letterOffset) {
        int index = ALPHABET.indexOf(current) - (letterOffset % ALPHABET.size());
        index = index >= 0 ? index : (ALPHABET.size() + index);
        return ALPHABET.get(index);
    }

    public String decryptToString (ArrayList<Character> chars, int letterOffset) {
        char[] newStringChars = new char[chars.size()];
        for (int i = 0; i < chars.size(); i++) {
            char current = chars.get(i);
            newStringChars[i] = this.contains(current)? decryptChar(current, letterOffset) : current;
        }
        return String.valueOf(newStringChars);
    }

    public int determineTheOffset (ArrayList<Character> chars) {
        int count = 0;
        int maxCount = 0;
        int estimatedOffset = -1;
        for (int i = 1; i<ALPHABET.size() ; i++) {
            String decryptedString = decryptToString(chars, i);
            if (decryptedString.contains(", ")) count += countTheNumberOfOccurrences(decryptedString, ", ");
            if (decryptedString.contains(". ")) count += countTheNumberOfOccurrences(decryptedString, ". ");
            String[] partsOfDecryptedString = decryptedString.split(" ");
            for (int j = 0; j < partsOfDecryptedString.length; j++) {
                count = switch (partsOfDecryptedString[j].toLowerCase(Locale.ROOT)) {
                    case "и", "в", "не", "на", "что", "для", "быть", "а", "по", "как", "чтобы", "к", "при", "о", "зто",
                            "с", "но", "из", "у", "то", "за", "от", "же", "или", "бы", "до", "если", "без" -> ++count;
                    default -> count;
                };
            }
            if (count > maxCount) {
                maxCount = count;
                estimatedOffset = i;
            }
            else count = 0;
        }
        return estimatedOffset;
    }

    public boolean contains (char ch) {
        return ALPHABET.contains(ch);
    }

    private static int countTheNumberOfOccurrences (String src, String target) {
        return (src.length() - src.replace(target, "").length()) / target.length();
    }
}
