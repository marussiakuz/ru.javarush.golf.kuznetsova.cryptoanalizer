import java.util.ArrayList;
import java.util.List;

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
        int index = ALPHABET.indexOf(current) - letterOffset;
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
            String[] partsOfDecryptedString = decryptedString.split(" ");
            for (int j = 0; j < partsOfDecryptedString.length; j++) {
                count = switch (partsOfDecryptedString[j]) {
                    case "и", "в", "не", "на", "что", "для", "быть", "а", "по", "как", "чтобы", "к", "при", "о" -> ++count;
                    default -> count;
                };
            }
            if (count > maxCount) {
                maxCount = count;
                estimatedOffset = i;
            }
        }
        return estimatedOffset;
    }

    public boolean contains (char ch) {
        return ALPHABET.contains(ch);
    }
}
