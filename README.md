# **Java Web Crawler**

* ### System Requirements:
1. [Java 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) 
2. [Maven plugin](https://maven.apache.org/download.cgi)

* ### Run the project:
There are two steps to run the project. ( Run following commands from project root folder. )
1. First step is to build jar file with dependencies using maven with following command.
```
mvn clean compile assembly:single
```
2. Next step is to run the jar file. The output from web crawl is printed to the console.

- For Windows terminal.
```
java -jar .\target\web-crawler.jar arg1 arg2
```
- For Linux based terminals.
```
java -jar ./target/web-crawler.jar arg1 arg2
```
The command takes 2 arguments first argument (arg1) is the seed URL to start the web crawl. The second argument (arg2) is an optional parameter which indicates the maximum depth of search. If not provided the default value is 1000. Following is an example for the command with http://www.rescale.com as seed URL and maximum depth of search is 3.
- For Windows terminal.
```
java -jar .\target\web-crawler.jar http://www.rescale.com 3
```
- For Linux based terminals.
```
java -jar ./target/web-crawler.jar http://www.rescale.com 3
```
* ### Testing:
1. **Unit Tests** : Unit tests can be run by using following command.
```
mvn test -Dtest="DocumentParserTaskTests,ThreadManagementServiceTest"
```
2. **Integration Test**: Integration test can be run by using following command.
```
mvn test -Dtest="IntegrationTest"
```