# Zeus-Commons-APIs

## Überblick (Deutsch)
Zeus-Commons-APIs ist Dein Schweizer Taschenmesser für Backend-Services. Erstellen Sie mächtige, verschachtelte SQL-Abfragen einfach über REST oder die Konsole. Läuft leichtgewichtig auf Spark Java und gibt Daten im JSON- oder XML-Format bis zu vier Verschachtelungsebenen zurück. Perfekt für alle, die Flexibilität ohne den Overhead wollen.

**Wichtiger Hinweis**: Für den Produktiveinsatz sind weitere Maßnahmen gegen SQL-Injection notwendig.

### Anwendungsmöglichkeiten
- API-Backend-Entwicklung
- Integration diverser Datenquellen
- Modernisierung von Legacy-Systemen wie System-i (AS/400)

### Endpunkte und Metadaten
Es gibt zwei Hauptendpunkte:

- GET localhost:4567/get_json gibt Daten im JSON-Format zurück.
- GET localhost:4567/get_xml gibt Daten im XML-Format zurück.

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
## License:
[MIT](LICENSE)
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

- GET localhost:4567/get_json returns data in JSON format.
- GET localhost:4567/get_xml returns data in XML format.

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
## License:
[MIT](LICENSE)
## Visit
[tiny-tool.de](https://tiny-tool.de/).