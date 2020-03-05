package com.pluralsight.rxjava2.utility.datasets;

public class GreekLetterPair {

    private String greekLetter;
    private String englishLetter;

    public GreekLetterPair(String greekLetter, String englishLetter) {
        this.greekLetter = greekLetter;
        this.englishLetter = englishLetter;
    }

    public String getGreekLetter() {
        return greekLetter;
    }

    public String getEnglishLetter() {
        return englishLetter;
    }

    @Override
    public String toString() {
        return "GreekLetterPair{" +
                "greekLetter='" + greekLetter + '\'' +
                ", englishLetter='" + englishLetter + '\'' +
                '}';
    }
}
