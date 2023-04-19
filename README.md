# **Java Web Crawler**

* ### System Requirements:
1. [Java 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) 
2. [Maven plugin](https://maven.apache.org/download.cgi)

* ### Run the project:
There are two steps to run the project. ( Run following commands from project root folder. )
1. First step is to build jar file with dependencies using maven with following command.
```
mvn clean compile test assembly:single
```
2. Next step is to run the jar file. The output from web crawl is printed to the console. 
```
java -jar .\target\web_crawler_vishvesh-1.0-SNAPSHOT-jar-with-dependencies.jar arg1 arg2
```
The command takes 2 arguments first argument (arg1) is the seed URL to start the web crawl. The second argument (arg2) is an optional parameter which indicates the maximum number of web pages to search. If not provided the default value is 2147483647. Following is an example for the command with http://www.rescale.com as seed URL and maximum number of pages to be searched as 10.
```
java -jar .\target\web_crawler_vishvesh-1.0-SNAPSHOT-jar-with-dependencies.jar http://www.rescale.com 10
```
* ### Testing:
1. **Unit Tests** : Tests are performed in the first step in project build but can be performed manually by using following command.
```
mvn test -Dtest="DocumentParserTaskTests,ThreadManagementServiceTest"
```
2. **Integration Test**: It is also performed while building jar in first step but can be performed using following command from project root folder.
```
mvn test -Dtest="IntegrationTest"
```