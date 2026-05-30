# Db2 for i Beispiel-SQL-Skripte (Qshell db2 Utility)

Dieser Ordner enthält einfache Beispiel-SQL-Skripte für die Ausführung mit dem **Qshell `db2` Utility** auf IBM i (Db2 for i).

Die Skripte dienen **ausschließlich Test- und Lernzwecken** (z. B. für Artikel oder Demos). Sie sind **nicht** für produktive Deployments gedacht.

## Vorbereitung
- Ersetze `YOUR_LIB` durch den Namen deiner **Test-Library** (z. B. `MYTESTLIB`).
- Führe die Skripte **nur in einer dedizierten Test-Library** aus – **nie in Produktion**!

## Ausführung in Qshell

```bash
# Tabellen erstellen
db2 -t -f createTables.sql

# Beispieldaten einfügen
db2 -t -f insertData.sql

# Optional: Tabellen wieder entfernen (Cleanup)
db2 -t -f dropTables.sql
```

Alternative mit mehr Verbosität:
```bash
db2 -tvf createTables.sql
```

## Enthaltene Dateien
- **createTables.sql**: Erstellt relationale Tabellen (AGENTS, CUSTOMERS, ORDERS, AGENT_REVENUE) + einfache EAV-Tabellen (EAV_DATA, EAV_METADATA).
- **insertData.sql**: Fügt plausible Beispieldaten ein (Parent-Daten vor Child-Daten).
- **dropTables.sql**: Entfernt alle Tabellen in umgekehrter Reihenfolge.

## Hinweise
- Alle Skripte verwenden `SET SCHEMA YOUR_LIB;` – Tabellen müssen nicht mehr qualifiziert werden.
- Kompatibel mit `db2 -t -f` (Semikolon als Terminator).
- EAV-Beispiel zeigt einfache Entity-Attribute-Value-Modellierung.
