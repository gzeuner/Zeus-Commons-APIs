# Zeus Commons Suite

## Überblick
Dieses Repository enthält eine Sammlung von gemeinsamen Modulen, die als Basis für verschiedene Backend-Services in der Zeus-Infrastruktur dienen.

## Was macht diese Anwendung?
Die Anwendung stellt verschiedene APIs bereit, die von anderen Services genutzt werden können. Sie handelt als eine Art Brücke zwischen diversen Datenquellen und Frontend-Applikationen.

### Request & Response Beispiele

#### XML-Daten abrufen
- **Request**: `POST localhost:4567/get_xml`
  ```json
  {
    "query": {
      "name": "agents",
      "statement": "select * from agents where agent_code = 'A105'",
      "subquery": {...}
    },
    "includeMetadata": false
  }
  ```

- **Response**:
  ```xml
  <result>
    <contentData>
      <agents>
        ...
      </agents>
    </contentData>
  </result>
  ```

#### JSON-Daten abrufen
- **Request**: `POST localhost:4567/get_json`
  ```json
  {
    "query": {
      "name": "agents",
      "statement": "select * from agents where agent_code = 'A105'",
      "subquery": {...}
    },
    "includeMetadata": false
  }
  ```

- **Response**:
  ```json
  {
    "contentData": {
      "agents": {
        "A105": {
          ...
        }
      }
    }
  }
  ```

### Fehlerbehandlung
Die Anwendung gibt bei Fehlern JSON-basierte Fehlermeldungen zurück, um eine einfache Integration und Fehlersuche zu ermöglichen.

## Module
- **zeus-commons-base**: Grundlegende Komponenten für Logging, Konfiguration usw.
- **zeus-commons-connector**: Verbindungseinstellungen für Datenbanken und andere Dienste
- **zeus-commons-provider**: Hauptanbieter für die gesamte Suite

## Voraussetzungen
- Java 8 oder höher
- Maven 3.x

## Build
Dieses Projekt verwendet Maven als Build-Tool und ist mit einem Parent-POM ausgestattet, der gemeinsame Dependencies und Plugins definiert.

### Build mit Maven
```bash
mvn clean install
```

### Erzeugen der schattierten JARs
```bash
mvn package
```

## Nutzung
Um eine der Komponenten zu verwenden, fügen Sie die entsprechende Dependency in Ihrer `pom.xml` hinzu.

## Lizenz
[MIT License](LICENSE.md)


# Zeus Commons Suite

## Overview
This repository contains a collection of common modules that serve as the foundation for various backend services in the Zeus infrastructure.

## What Does This Application Do?
The application provides various APIs that can be utilized by other services. It acts as a sort of bridge between various data sources and frontend applications.

### Request & Response Examples

#### Fetching XML Data
- **Request**: `POST localhost:4567/get_xml`
  ```json
  {
    "query": {
      "name": "agents",
      "statement": "select * from agents where agent_code = 'A105'",
      "subquery": {...}
    },
    "includeMetadata": false
  }
  ```

- **Response**:
  ```xml
  <result>
    <contentData>
      <agents>
        ...
      </agents>
    </contentData>
  </result>
  ```

#### Fetching JSON Data
- **Request**: `POST localhost:4567/get_json`
  ```json
  {
    "query": {
      "name": "agents",
      "statement": "select * from agents where agent_code = 'A105'",
      "subquery": {...}
    },
    "includeMetadata": false
  }
  ```

- **Response**:
  ```json
  {
    "contentData": {
      "agents": {
        "A105": {
          ...
        }
      }
    }
  }
  ```

### Error Handling
The application returns JSON-based error messages to enable easy integration and debugging.

## Modules
- **zeus-commons-base**: Basic components for logging, configuration, etc.
- **zeus-commons-connector**: Connection settings for databases and other services
- **zeus-commons-provider**: The main provider for the entire suite

## Prerequisites
- Java 8 or higher
- Maven 3.x

## Build
This project uses Maven as its build tool and comes with a parent POM that defines common dependencies and plugins.

### Build with Maven
```bash
mvn clean install
```

### Generating the shaded JARs
```bash
mvn package
```

## Usage
To use any of the components, add the corresponding dependency in your `pom.xml`.

## License
[MIT License](LICENSE.md)
