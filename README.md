# Zeus Commons Suite

## Überblick (Deutsch)
Dieses Repository enthält den Kerncode für die Zeus Commons Suite. Als leichtgewichtige Alternative zu Spring Boot wurde hier Spark Java verwendet. Die Suite dient als Grundlage für verschiedene Backend-Services.

**Wichtiger Hinweis**: Für den Produktiveinsatz sind weitere Maßnahmen gegen SQL-Injection notwendig.

### Anwendungsmöglichkeiten
- API-Backend-Entwicklung
- Integration diverser Datenquellen
- Modernisierung von Legacy-Systemen wie System-i (AS/400)

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
# Zeus Commons Suite

## Overview (English)
This repository contains the core code for Zeus Commons Suite. As a lightweight alternative to Spring Boot, Spark Java has been employed here. The suite serves as a foundation for various backend services.

**Important Note**: Further measures against SQL Injection are required for production use.

### Possible Use-Cases
- API Backend Development
- Integration of Various Data Sources
- Modernization of Legacy Systems like System-i (AS/400)

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