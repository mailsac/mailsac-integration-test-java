# Email Integration Tests in Java

Mailsac uses the [REST API](https://docs.mailsac.com/en/latest/api_examples/getting_started/getting_started.html) to fetch, read, and send emails. The REST API also allows you to reserve a private email address that can forward messages to another email address, Slack, web sockets, etc.

This article describes how to integrate Mailsac with Java and the unit testing framework: [JUnit](https://junit.org/junit5/).

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

Testing from build automation tools, like Maven, is another option. In many ways, using build tools is the best option. For instance, it provides a standard directory layout that encourages better development practices. Maven also abstracts many underlying mechanisms allowing developers to run a single command for validating, compiling, testing, packaging, verifying, installing, and deploying. 

This section will describe how to set up Maven for building, managing, and testing a project.

1. Navigate to the [Apache Maven download page](https://maven.apache.org/download.cgi) and follow its [installation instructions](https://maven.apache.org/install.html). If you have Homebrew you can just run: `brew install maven`.

2. There are [many](https://maven.apache.org/archetypes/index.html) directory layouts to choose from. We will use `maven-archetype-quickstart`. Run on the command-line:

    `mvn archetype:generate -DgroupId=com.mailsac.api -DartifactId=mailsac-integration-test-java -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false`

    Note: Refer to [naming conventions](https://maven.apache.org/guides/mini/guide-naming-conventions.html) for further reading.

3. Navigate into the directory `mailsac-integration-test-java`: `cd mailsac-integration-test-java`

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
    import org.junit.jupiter.api.DisplayName;
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

    The output should appear somwhat like this:

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

Your environment is now appropriate for further development!

## Mailsac Java Integration Test

This section describes how to use the Unirest library with Mailsac and JUnit. We will be sending an email to Mailsac and validating with JUnit to ensure that the email is sent.

### What is Unirest? 

Unirest is a HTTP client library available in multiple languages including Java, Node.js, Python, etc. Unirest is used to make HTTP requests and has JSON support.

### Integration Test Example

1. With Maven, add the following dependency:

    ```xml
    <!-- ... -->
    <dependencies>
      <!-- ... -->
      <dependency>
        <groupId>com.mashape.unirest</groupId>
        <artifactId>unirest-java</artifactId>
        <version>1.4.9</version>
      </dependency>
      <!-- ... -->
    </dependencies>
    <!-- ... -->
    ```

    Note: If you are not using Maven, include the JAR file in the classpath: https://mvnrepository.com/artifact/com.mashape.unirest/unirest-java/1.4.9

2. Edit the `AppTest.java` file: `$EDITOR src/test/java/com/mailsac/api/AppTest.java`

    Import the required modules:
    ```java
    package com.mailsac.api

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.AfterEach;

    import com.mashape.unirest.http.HttpResponse;
    import com.mashape.unirest.http.Unirest;
    import com.mashape.unirest.http.exceptions.UnirestException;
    ```

3. Configure Mailsac credentials and settings:

    ```java
    public class AppTest {

        private static String mailsacAPIKey = "";
        private static String mailsacToAddress = "";
        private static String mailsacFromAddress = "";
        private static String mailsacSubject = "";
        private static String mailsacText = "string";

    }
    ```

4. Add a `sendMail()` method which makes a POST request to `api/outgoing-messages` and checks whether or not the message was sent using `assertEquals`:

    ```java
    public class AppTest {
        //...
        void sendMail() throws UnirestException {
            HttpResponse response = Unirest.post("https://mailsac.com/api/outgoing-messages")
            .header("content-type", "application/json")
            .header("Mailsac-Key", String.format("%s", mailsacAPIKey))
            .body(String.format("{\"to\":\"%s\",\"from\":\"%s\",\"subject\":\"%s\",\"text\":\"%s\"}", mailsacToAddress, mailsacFromAddress, mailsacSubject, mailsacText))
            .asString();

            assertEquals(200, response.getStatus());
        }

    }
    ```

5. Add a `purgeInbox()` method which makes a DELETE request to `api/addresses/{email}/messages`. This attempts to deletes all messages for an inbox and confirms if the request was successful with `assertEquals`.

    ```java
    public class AppTest {
        //...
        @Test
        @AfterEach
        void purgeInbox() throws UnirestException {
            HttpResponse response = Unirest.delete("https://mailsac.com/api/addresses/rin@mailsac.com/messages")
            .header("Mailsac-Key", String.format("%s", mailsacAPIKey))
            .asString();

            assertEquals(204, response.getStatus());
        }

    }
    ```

    The annotation `@AfterEach` signals that a method should be executed after each `@Test` method in the current class.

6. Package the project: `mvn clean package`. This will also run a test.

    The output should appear similar to this:

    ```zsh
    [INFO] -------------------------------------------------------
    [INFO]  T E S T S
    [INFO] -------------------------------------------------------
    [INFO] Running com.mailsac.java.AppTest
    [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.861 s - in com.mailsac.java.AppTest
    [INFO] 
    [INFO] Results:
    [INFO] 
    [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
    ```

    Subsequent changes to the source file do not require you to run `mvn clean package` again. Instead, just run `mvn test`.

## Github Repository

If you are having any difficulties, `git clone https://github.com/mailsac/mailsac-integration-test-java`. Make edits as necessary, and run `mvn package`.

## Next Steps

This example can be adjusted to get all private email addresses for an account and purge their inboxes if necessary.

Please refer to our [forums](https://forum.mailsac.com/) if you have any questions!