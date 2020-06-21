package editor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFinder extends Thread {
    private String text;
    private String patternText;
    private List<Indexes> indexes = new ArrayList<>();

    public TextFinder(String text, String patternText) {
        this.text = text;
        this.patternText = patternText;
    }

    @Override
    public void run() {
        Pattern pattern = Pattern.compile(patternText);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            indexes.add(new Indexes(startIndex, endIndex));
        }
    }

    public List<Indexes> listOfIndexes() {
        return indexes;
    }
}
