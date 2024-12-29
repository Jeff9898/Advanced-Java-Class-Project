package edu.wgu.d387_sample_code.model;


public class LocalizedWelcomeMessage {
    private String englishMessage;
    private String frenchMessage;

    public LocalizedWelcomeMessage() {

    }

    public LocalizedWelcomeMessage(String englishMessage, String frenchMessage) {
        this.englishMessage = englishMessage;
        this.frenchMessage = frenchMessage;
    }

    public String getEnglishMessage() {
        return englishMessage;
    }

    public void setEnglishMessage(String englishMessage) {
        this.englishMessage = englishMessage;
    }

    public String getFrenchMessage() {
        return frenchMessage;
    }

    public void setFrenchMessage(String frenchMessage) {
        this.frenchMessage = frenchMessage;
    }

}