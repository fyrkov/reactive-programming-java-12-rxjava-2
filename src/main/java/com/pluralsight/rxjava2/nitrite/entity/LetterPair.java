package com.pluralsight.rxjava2.nitrite.entity;

public class LetterPair {

    private String greekLetter;
    private String englishRepresentation;

    public LetterPair() {

    }

    public LetterPair(String greekLetter, String englishRepresentation) {
        this.greekLetter = greekLetter;
        this.englishRepresentation = englishRepresentation;
    }

    public String getGreekLetter() {
        return greekLetter;
    }

    public void setGreekLetter(String greekLetter) {
        this.greekLetter = greekLetter;
    }

    public String getEnglishRepresentation() {
        return englishRepresentation;
    }

    public void setEnglishRepresentation(String englishRepresentation) {
        this.englishRepresentation = englishRepresentation;
    }

    @Override
    public String toString() {
        return "LetterPair{" +
                "greekLetter='" + greekLetter + '\'' +
                ", englishRepresentation='" + englishRepresentation + '\'' +
                '}';
    }
}
