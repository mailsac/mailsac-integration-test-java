package com.mailsac.java;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class App {

    static String mailsacAPIKey = "";
    static String mailsacToAddress = "";
    static String mailsacFromAddress = "";
    static String mailsacSubject = "";
    static String mailsacText = "string";

    static void sendMail() throws UnirestException {
        HttpResponse response = Unirest.post("https://mailsac.com/api/outgoing-messages")
        .header("content-type", "application/json")
        .header("Mailsac-Key", String.format("%s", mailsacAPIKey))
        .body(String.format("{\"to\":\"%s\",\"from\":\"%s\",\"subject\":\"%s\",\"text\":\"%s\"}", mailsacToAddress, mailsacFromAddress, mailsacSubject, mailsacText))
        .asString();
    }

    static void deleteMail() throws UnirestException {
        HttpResponse response = Unirest.delete("https://mailsac.com/api/addresses/rin@mailsac.com/messages")
        .header("Mailsac-Key", String.format("%s", mailsacAPIKey))
        .asString();
    }

    public static void main(String[] args) throws UnirestException {
        sendMail();
        deleteMail();
    }

}
