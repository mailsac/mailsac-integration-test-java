# Email Integration Tests in Java

Mailsac uses the [REST API](https://docs.mailsac.com/en/latest/api_examples/getting_started/getting_started.html) to fetch, read, and send emails. The REST API also allows you to reserve a private email address that can forward messages to another email address, Slack, web sockets, etc.

This article describes how to integrate Mailsac with Java and the unit testing framework: [JUnit](https://junit.org/junit5/). We will also use the [JavaMail API](https://javaee.github.io/javamail/) to send an email via an SMTP server.

## What is JUnit?

JUnit is a unit testing framework for the Java programming language. The latest version of the framework, JUnit 5, requires Java 8 and above. It supports testing on command-lines, build automation tools, and IDEs.

JUnit can be used to test individual components of code to ensure that each unit is performing the way it should.

## Setting Up Your Environment

Depending on the environment, there are multiple ways to run tests.

### Testing Using Command-Line

Testing from the command-line requires you to download the **ConsoleLauncher** which is the executable ```junit-platform-console-standalone-1.7.2.jar```. It is published in the Maven Central repository under the [junit-platform-console-standalone](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/) directory.

1. Navigate to the Maven Central [directory](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.7.2/) and download `junit-platform-console-standalone-1.7.2.jar`.
2. Create a directory for the project: `mkdir mailsac-tests`.
3. Move the jar file you downloaded into the directory `mailsac-tests`.
4. Create a directory inside `mailsac-tests`: `mkdir test`.

    Note: `mailsac-tests/test` will contain your source code.

#### JUnit Testing Introduction

This JUnit code example presents a basic usage of the testing framework.

Inside the directory `mailsac-tests/test`, create a java file: `touch TestClass.java`.

Write the following:

```java
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("tests truth")
class TestClass {

    @Test
    @DisplayName("true equals true")
    void trueEqualsTrue() {
        // The assertTrue method asserts that the supplied condition is true.
        // static void assertTrue(condition)
        assertTrue(true);
    }

    @Test
    @DisplayName("false equals false")
    void falseEqualsFalse() {
        // The assertEquals method asserts that expected and actual are equal.
        // static void assertEquals(expected, actual)
        assertEquals(false, false);
    }

}
```

`@Test` Denotes that a method is a test method.

`@DisplayName` Declares a custom display name for the test class or test method.

Refer to [JUnit annotations](https://junit.org/junit5/docs/current/user-guide/#writing-tests-annotations) and [JUnit Assertions](https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html) for further reading.

#### Running JUnit Tests From The Command-Line

1. Inside the directory `mailsac-tests`, compile:

    `javac -cp junit-platform-console-standalone-1.7.2.jar -d test test/TestClass.java`.

2. Then run:

    `java -jar junit-platform-console-standalone-1.7.2.jar --class-path test --scan-class-path`.

The output should appear similar to this:

```zsh
╷
├─ JUnit Jupiter ✔
│  └─ tests truth ✔
│     ├─ false equals false ✔
│     └─ true equals true ✔
└─ JUnit Vintage ✔

Test run finished after 92 ms
[         3 containers found      ]
[         0 containers skipped    ]
[         3 containers started    ]
[         0 containers aborted    ]
[         3 containers successful ]
[         0 containers failed     ]
[         2 tests found           ]
[         0 tests skipped         ]
[         2 tests started         ]
[         0 tests aborted         ]
[         2 tests successful      ]
[         0 tests failed          ]
```

### Testing Using Build Tools

Testing from build automation tools, like Maven, is another option. In many ways, using build tools is the best option. For instance, they provide a standard directory layout that encourages better development practices.

Maven, for example, abstracts many underlying mechanisms allowing developers to run a single command for validating, compiling, testing, packaging, verifying, installing, and deploying. 

This section will describe how to set up Maven for building, managing, and testing a project.

1. Navigate to the [Apache Maven download page](https://maven.apache.org/download.cgi) and follow its [installation instructions](https://maven.apache.org/install.html). If you have Homebrew you can just run: `brew install maven`.

2. After installing Maven, run on the command-line:

    `mvn archetype:generate -DgroupId=com.mailsac.api -DartifactId=mailsac-integration-test-java -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false`

3. Navigate into the directory: `cd mailsac-integration-test-java`

4. Make the following changes to the `pom.xml` file:

    ```xml
    <!-- ... -->
    <dependencies>
      <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.7.2</version>
        <scope>test</scope>
      </dependency>
      <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.7.2</version>
        <scope>test</scope>
      </dependency>
    </dependencies>
    <!-- ... -->
    <build>
      <pluginManagement>
        <!-- ... -->
        <plugins>
          <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.22.2</version>
          </plugin>
          <plugin>
            <artifactId>maven-failsafe-plugin</artifactId>
            <version>2.22.2</version>
          </plugin>
        </plugins>
        <!-- ... -->
      </pluginManagement>
    </build>
    <!-- ... -->
    ```

4. Edit the `AppTest.java` file: `$EDITOR src/test/java/com/mailsac/api/AppTest.java`

    ```java
    package com.mailsac.api;

    import static org.junit.jupiter.api.Assertions.assertTrue;
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import org.junit.jupiter.api.Test;

    class TestClass {

        @Test
        void trueEqualsTrue() {
            // The assertTrue method asserts that the supplied condition is true.
            // static void assertTrue(condition)
            assertTrue(true);
        }

        @Test
        void falseEqualsFalse() {
            // The assertEquals method asserts that expected and actual are equal.
            // static void assertEquals(expected, actual)
            assertEquals(false, false);
        }

    }
    ```

5. In the directory `mailsac-integration-test-java`, run this command which deletes the folder `target` \(if it does not already exist\) and packages the project into a new `target` folder. It also runs a test:

    `mvn clean package`.

6. You can do a quick test in the directory `mailsac-integration-test-java` again by running: `mvn test`.

    The output should appear somewhat like this:

    ```zsh
    [INFO] -------------------------------------------------------
    [INFO]  T E S T S
    [INFO] -------------------------------------------------------
    [INFO] Running com.mailsac.api.TestClass
    [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.029 s - in com.mailsac.api.TestClass
    [INFO] 
    [INFO] Results:
    [INFO] 
    [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
    ```

## Mailsac Java Integration Test

This section describes how to use the JavaMail API with Mailsac and JUnit. We will be sending an email to Mailsac and validating with JUnit to ensure that the email ewas sent. We will also use the Unirest library to send requests to the Mailsac API, and [Jackson](https://github.com/FasterXML/jackson) to parse JSON.

### What is the JavaMail API?

The JavaMail API is used to build Java technology based email client applications through a platform independent and protocol independent framework.

### Integration Test Example

1. With Maven, add the following dependencies:

    ```xml
    <!-- ... -->
    <dependencies>
      <!-- ... -->
      <dependency>
        <groupId>com.mashape.unirest</groupId>
        <artifactId>unirest-java</artifactId>
        <version>1.4.9</version>
      </dependency>
      <dependency>
        <groupId>com.sun.mail</groupId>
        <artifactId>javax.mail</artifactId>
        <version>1.6.2</version>
      </dependency>
      <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.12.5</version>
      </dependency>
    </dependencies>
    <!-- ... -->
    ```

    Note: If you are not using Maven, include the JAR files in the classpath:

    https://mvnrepository.com/artifact/com.mashape.unirest/unirest-java/1.4.9

    https://mvnrepository.com/artifact/com.sun.mail/javax.mail/1.6.2

    https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind/2.12.5


2. Edit the `AppTest.java` file: `$EDITOR src/test/java/com/mailsac/api/AppTest.java`

    Import the required modules:
    ```java
    package com.mailsac.api;

    import static org.junit.jupiter.api.Assertions.assertTrue;
    import static org.junit.jupiter.api.Assertions.fail;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.AfterAll;

    import java.util.Properties;

    import javax.mail.Session;
    import javax.mail.internet.MimeMessage;
    import javax.mail.Transport;
    import javax.mail.Message;
    import javax.mail.MessagingException;
    import javax.mail.internet.InternetAddress;

    import com.mashape.unirest.http.HttpResponse;
    import com.mashape.unirest.http.Unirest;
    import com.mashape.unirest.http.exceptions.UnirestException;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.fasterxml.jackson.databind.JsonNode;

    import java.io.IOException;

    ```

3. Acquire a [Mailsac API key](https://mailsac.com/docs/api), and decide what to use as the FROM and TO addresses for your test.
Export these parameters as environment variables:

    ```bash
    export MAILSAC_API_KEY=k_eoj1mn_your_mailsac_key;
    export MAILSAC_TO_ADDRESS=your_address@mailsac.com
    export MAILSAC_FROM_ADDRESS=your_address@mailsac.com
    ```
   and use the environment variables in your java code:
   
   ```java
    public class AppTest {
        // MAILSAC_API_KEY environment variable. Generated by mailsac. See https://mailsac.com/api-keys
        static String mailsacAPIKey = "";
        // MAILSAC_TO_ADDRESS environment variable. Who you're sending an email to.
        static String mailsacToAddress = "";
        // MAILSAC_FROM_ADDRESS environment variable. Necessary if you are sending through out.mailsac.com (unlikely - you most likely will replace
        // sendMail() below.
        static String mailsacFromAddress = "";

        @BeforeAll
        static void setup() throws Exception {
            mailsacAPIKey = System.getenv().get("MAILSAC_API_KEY");
            mailsacToAddress = System.getenv().get("MAILSAC_TO_ADDRESS");
            mailsacFromAddress = System.getenv().get("MAILSAC_FROM_ADDRESS");
            if (mailsacAPIKey == "" || mailsacToAddress == "" || mailsacFromAddress == "") {
                throw new Exception("Missing environment variable setup!");
            }
            System.out.println(mailsacAPIKey);
            System.out.println(mailsacToAddress);
            System.out.println(mailsacFromAddress);
        }
    }
   ```


5. Add a `purgeInbox()` method which makes a DELETE request to `api/addresses/{email}/messages`. This requires the inbox to be private, which is a paid feature of Mailsac.

    ```java
   public class AppTest {
     @BeforeEach
     @AfterEach
     // purgeInbox cleans up all messages in the inbox before and after running each test,
     // so there is a clean state.
     void purgeInbox() throws UnirestException, JsonProcessingException {
         HttpResponse<String> response = Unirest.get(String.format("https://mailsac.com/api/addresses/%s/messages", mailsacToAddress))
                 .header("Mailsac-Key", mailsacAPIKey)
                 .asString();
    
         // Parse JSON
         ObjectMapper objectMapper = new ObjectMapper();
         Object[] messagesArray = objectMapper.readValue(response.getBody(), Object[].class);
    
         for (int i = 0; i < messagesArray.length; i++) {
             JsonNode m = objectMapper.convertValue(messagesArray[i], JsonNode.class);
             String id = m.get("_id").toString();
             System.out.printf("Purging inbox message %s", id);
             Unirest.delete(String.format("https://mailsac.com/api/addresses/%s/messages/%s", mailsacToAddress, id))
                     .header("Mailsac-Key", mailsacAPIKey)
                     .asString();
         }
     }
   }
    ```

6. Implement a `sendMail()` method which sends a message, if needed. However most likely you are already sending mail
   and just using Mailsac to test it. This part will likely be different.

    ```java
    public class AppTest {
        //...
        static void sendMail(String subject, String textMessage, String htmlMessage) throws UnsupportedEncodingException, MessagingException {
            Session session = Session.getDefaultInstance(new Properties());
            javax.mail.Transport transport = session.getTransport("smtp");
            MimeMessage msg = new MimeMessage(session);
    
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
    
            msg.setFrom(mailsacFromAddress);
            msg.setReplyTo(InternetAddress.parse(mailsacFromAddress));
            msg.setSubject(subject, "UTF-8");
            msg.setText(textMessage, "UTF-8");
            msg.setContent(htmlMessage, "text/html");
    
            msg.setSentDate(new Date());
    
            msg.setRecipients(Message.RecipientType.TO, mailsacToAddress);
            msg.saveChanges();
            System.out.println("Email message is ready to send");
            transport.connect("out.mailsac.com", 587, mailsacFromAddress, mailsacAPIKey);
            transport.sendMessage(msg, msg.getAllRecipients());
    
            System.out.println("Email sent successfully");
        }
    }
    ```

8. Add test. Use a `for` loop to check if the message was received by scanning the recipient inbox periodically. If the recipient inbox is not empty, a message was found and it verifies the message content:

    ```java
    public class AppTest {
        //...
        @Test
        void checkEmailWithLink() throws MessagingException, UnirestException, IOException, InterruptedException {
            sendMail("Hello!", "Check out https://example.com", "Check out <a href='https://example.com'>My website</a>");
            // Check inbox for the message up to 10x, waiting 5 seconds between checks.
            found:
            {
                for (int i = 0; i < 10; i++) {
                    // Send request to fetch a JSON array of email message objects from mailsac
                    HttpResponse<String> response = Unirest.get(String.format("https://mailsac.com/api/addresses/%s/messages", mailsacToAddress))
                            .header("Mailsac-Key", mailsacAPIKey)
                            .asString();
    
                    // Parse JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    Object[] messagesArray = objectMapper.readValue(response.getBody(), Object[].class);
    
                    System.out.printf("Fetched %d messages from Mailsac for address %s", messagesArray.length, mailsacToAddress);
                    eachMessage:
                    {
                        for (int m = 0; m < messagesArray.length; m++) {
                            // Convert object into JSON to fetch a field
                            JsonNode thisMessage = objectMapper.convertValue(messagesArray[m], JsonNode.class);
    
                            // After a message is found, the JSON object is checked to see if the link was sent correctly
                            assertTrue(thisMessage.get("links").toString().contains("https://example.com"), "Missing / Incorrect link in email");
    
                            return; // end the tests
                        }
                    }
    
                    System.out.println("Message not found yet, waiting 5 secs");
                    Thread.sleep(5000);
                }
    
                // Fail the test if we haven't reached assertTrue above
                fail("Never received expected message!");
            }
        }
    }
    ```

    This test uses the [Mailsac API endpoint](https://mailsac.com/docs/api#tag/Email-Messages-API/paths/~1addresses~1{email}~1messages/get) `/api/addresses/{email}/messages` which lists all messages in an inbox.

9. At this point, the code is complete. Package the project: `mvn clean package`. This will also run a test.

    The output should appear similar to this:

    ```zsh
    [INFO] -------------------------------------------------------
    [INFO]  T E S T S
    [INFO] -------------------------------------------------------
    [INFO] Running com.mailsac.api.AppTest
    [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 9.148 s s - in com.mailsac.api.AppTest
    [INFO] 
    [INFO] Results:
    [INFO] 
    [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
    ```

    Subsequent changes to the source file do not require you to run `mvn clean package` again. Instead, run `mvn test`.

## Github Repository

If you encounter any difficulties, `git clone https://github.com/mailsac/mailsac-integration-test-java`. Make edits as necessary, and run `mvn package`.

Alternatively, if your tests fail because of error codes when making requests to the Mailsac API, please refer to the [API Specification](https://mailsac.com/docs/api) for further reading.

## Next Steps

The Mailsac API Specification has generated code examples in Java + Unirest for making requests. It also has code examples in other languages.

This example can be adjusted to get all private email addresses for an account and purge their inboxes if necessary.

Please visit our [forums](https://forum.mailsac.com/) if you have any questions!