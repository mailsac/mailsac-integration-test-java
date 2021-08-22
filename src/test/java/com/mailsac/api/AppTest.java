package com.mailsac.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AppTest {
    
    private static String mailsacAPIKey = "";
    private static String mailsacToAddress = "";
    private static String mailsacFromAddress = "";
    private static String mailsacSubject = "";
    private static String mailsacText = "string";

    @Test
    void sendMail() throws UnirestException {
        HttpResponse response = Unirest.post("https://mailsac.com/api/outgoing-messages")
        .header("content-type", "application/json")
        .header("Mailsac-Key", String.format("%s", mailsacAPIKey))
        .body(String.format("{\"to\":\"%s\",\"from\":\"%s\",\"subject\":\"%s\",\"text\":\"%s\"}", mailsacToAddress, mailsacFromAddress, mailsacSubject, mailsacText))
        .asString();

        assertEquals(200, response.getStatus());
    }

    @Test
    @AfterEach
    void purgeInbox() throws UnirestException {
        HttpResponse response = Unirest.delete(String.format("https://mailsac.com/api/addresses/%s/messages", mailsacToAddress))
        .header("Mailsac-Key", String.format("%s", mailsacAPIKey))
        .asString();

        assertEquals(204, response.getStatus());
    }

}
