# Zeus-Commons-APIs

## Überblick (Deutsch)
Zeus-Commons-APIs ist Ihr Schweizer Taschenmesser für Backend-Services. Erstellen Sie mächtige, verschachtelte SQL-Abfragen einfach über REST oder die Konsole. Läuft leichtgewichtig auf Spark Java und gibt Daten im JSON- oder XML-Format bis zu vier Verschachtelungsebenen zurück. Perfekt für alle, die Flexibilität ohne den Overhead wollen.

**Wichtiger Hinweis**: Für den Produktiveinsatz sind weitere Maßnahmen gegen SQL-Injection notwendig.

### Anwendungsmöglichkeiten
- API-Backend-Entwicklung
- Integration diverser Datenquellen
- Modernisierung von Legacy-Systemen wie System-i (AS/400)

### Endpunkte und Metadaten
Es gibt zwei Hauptendpunkte:

- POST localhost:4567/json gibt Daten im JSON-Format zurück.
- POST localhost:4567/xml gibt Daten im XML-Format zurück.

Sie können die Ausgabe von Metadaten ein- oder ausschalten, indem Sie das Feld includeMetadata im JSON-Body auf true oder false setzen.

Beispieldaten finden Sie unter [examples](https://github.com/gzeuner/Zeus-Commons-APIs/tree/main/examples).

### Datenverschachtelung
Die Anwendung unterstützt Datenverschachtelungen bis zu vier Ebenen. Dies erlaubt die Darstellung komplexer Abfragebeziehungen.


#### Beispiel mit bis zu vier Ebenen
```json
{
    "query": {
        "name": "agents",
        "statement": "select * from agents where agent_code = 'A105'",
        "subquery": {
            "name": "orders",
            "statement": "select * from orders where agent_code = '[$agent_code]'",
            "subquery": {
                "name": "customers",
                "statement": "select * from customers where cust_code = '[$cust_code]'",
                "subquery": {
                    "name": "revenue",
                    "statement": "select * from agent_revenue where agent_code = '[$agent_code]'"
                }
            }
        }
    },
    "includeMetadata": false
}
```
### Maven Build-Profile
Dieses Projekt verwendet Maven für den Build-Prozess und stellt verschiedene Profile zur Verfügung, um spezifische Abhängigkeiten hinzuzufügen oder zu entfernen.

### include-jt400 Profil
Dieses Profil fügt die JT400-Bibliothek als Abhängigkeit hinzu und ermöglicht die Integration mit IBM i (früher AS/400).
```bash
mvn clean install -P include-jt400
```
### include-ssl Profil
Dieses Profil fügt die Bouncy Castle Bibliothek als Abhängigkeit hinzu, um SSL-Unterstützung zu ermöglichen.
```bash
mvn clean install -P include-ssl
```
### fat-jar Profil
Dieses Profil erstellt ein "Uber-JAR", das alle Abhängigkeiten in einer einzigen JAR-Datei bündelt. Ideal für den einfachen Transport und Deployment.
```bash
mvn clean install -P fat-jar
```
### standard-jar Profil
Dieses Profil erstellt ein Standard-JAR ohne eingebettete Abhängigkeiten. Nützlich, wenn Sie die Abhängigkeiten selbst verwalten wollen.
```bash
mvn clean install -P standard-jar
```
### Kombinierte Profile
Es ist auch möglich, mehrere Profile gleichzeitig zu verwenden, um eine Kombination der jeweiligen Features zu erhalten.
```bash
mvn clean install -P include-jt400,include-ssl,fat-jar
```

### Provider
Der Provider fungiert als Einstiegspunkt, um die Anwendung als REST-Service oder über die Konsole zu betreiben. Er behandelt Befehlszeilenargumente, initialisiert die notwendigen Komponenten und startet den Service.
Aufruf des Provider als REST-Service:

```bash
java -cp zeus-commons-0.0.1-SNAPSHOT.jar de.zeus.commons.provider.Provider "REST" "<pfad-zur-jdbc-properties-datei>" "<pfad-zur-spark-properties-datei>"
```
Aufruf des Provider über die Konsole:
```bash
java -cp zeus-commons-0.0.1-SNAPSHOT.jar de.zeus.commons.provider.Provider "CONSOLE" "<pfad-zur-jdbc-properties-datei>" "<json-query-objekt>" "<modus>"
```
Als 'mode' kann application/xml oder application/xml angegeben werden, um das Ausgabeformat zu steuern. 
## License:
[Apache 2.0](LICENSE)
## Besuchen Sie:
[tiny-tool.de](https://tiny-tool.de/).
# Zeus-Commons-APIs

## Overview (English)
Zeus-Commons-APIs is your Swiss Army knife for backend services. Easily create powerful, nested SQL queries via REST or console input. Running lean on Spark Java, it returns data in JSON or XML formats with up to four levels of nesting. Perfect for those who want flexibility without the overhead.

**Important Note**: Further measures against SQL Injection are required for production use.

### Possible Use-Cases
- API Backend Development
- Integration of Various Data Sources
- Modernization of Legacy Systems like System-i (AS/400)

### Endpoints and Metadata
There are two main endpoints:

- POST localhost:4567/json returns data in JSON format.
- POST localhost:4567/xml returns data in XML format.

You can toggle metadata output on or off by setting the includeMetadata field in the JSON body to true or false.

Sample data can be found under [examples](https://github.com/gzeuner/Zeus-Commons-APIs/tree/main/examples).

### Data Nesting
The application supports data nesting up to four levels, allowing for complex query relationships to be modelled.

#### Example with Up to Four Levels
```json
{
    "query": {
        "name": "agents",
        "statement": "select * from agents where agent_code = 'A105'",
        "subquery": {
            "name": "orders",
            "statement": "select * from orders where agent_code = '[$agent_code]'",
            "subquery": {
                "name": "customers",
                "statement": "select * from customers where cust_code = '[$cust_code]'",
                "subquery": {
                    "name": "revenue",
                    "statement": "select * from agent_revenue where agent_code = '[$agent_code]'"
                }
            }
        }
    },
    "includeMetadata": false
}
```
### Maven Build Profiles
This project uses Maven for the build process and provides several profiles to add or remove specific dependencies.

### include-jt400 profile
This profile adds the JT400 library as a dependency and allows integration with IBM i (formerly AS/400).
```bash
mvn clean install -P include-jt400
```
### include-ssl profile
This profile adds the Bouncy Castle library as a dependency to enable SSL support.
```bash
mvn clean install -P include-ssl
```
### fat-jar profile
This profile creates an "Uber JAR" that bundles all dependencies into a single JAR file. Ideal for easy transport and deployment.
```bash
mvn clean install -P fat-jar
```
### standard-jar profile
This profile creates a standard JAR without embedded dependencies. Useful if you want to manage the dependencies yourself.
```bash
mvn clean install -P standard-jar
```
### Combined profiles
It is also possible to use several profiles at the same time to get a combination of their features.
```bash
mvn clean install -P include-jt400,include-ssl,fat-jar
```
### Provider
The provider acts as an entry point to run the application as a REST service or via the console. It handles command line arguments, initializes the necessary components and starts the service.
How to run the Provider as REST Service:

```bash
java -cp zeus-commons-0.0.1-SNAPSHOT.jar de.zeus.commons.provider.Provider "REST" "<path-to-jdbc-properties-file>" "<path-to-spark-properties-file>"
```
How to run the Provider from the console:
```bash
java -cp zeus-commons-0.0.1-SNAPSHOT.jar de.zeus.commons.provider.Provider "CONSOLE" "<path-to-jdbc-properties-file>" "<json-query-object>" "<mode>"
```
Application/xml or application/xml can be specified as 'mode' to control the output format.

## License:
[Apache 2.0](LICENSE)
## Visit
[tiny-tool.de](https://tiny-tool.de/).