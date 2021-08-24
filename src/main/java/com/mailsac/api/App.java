package com.mailsac.java;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class App {
    
    private static String mailsacAPIKey = "";
    private static String mailsacToAddress = "";
    private static String mailsacFromAddress = "";

    private static String mailsacSubject = "Hello!"; 
    private static String mailsacText = "Check out https://example.com";
    private static String mailsacHTML = "Check out <a href='https://example.com'>My website</a>";

    static void purgeInbox() throws UnirestException {
        HttpResponse response = Unirest.delete(String.format("https://mailsac.com/api/addresses/%s/messages", mailsacToAddress))
        .header("Mailsac-Key", String.format("%s", mailsacAPIKey))
        .asString();
    }

    static void sendMail() throws UnirestException {
        HttpResponse response = Unirest.post("https://mailsac.com/api/outgoing-messages")
        .header("content-type", "application/json")
        .header("Mailsac-Key", String.format("%s", mailsacAPIKey))
        .body(String.format("{\"to\":\"%s\",\"from\":\"%s\",\"subject\":\"%s\",\"text\":\"%s\",\"html\":\"%s\"}", mailsacToAddress, mailsacFromAddress, mailsacSubject, mailsacText, mailsacHTML))
        .asString();

    }

    public static void main(String[] args) throws UnirestException {
        sendMail();
        purgeInbox();
    }
}
