-- =============================================
-- dropTables.sql - Cleanup für Db2 for i Beispiel-Tabellen
-- Zweck: Entfernt alle erstellten Tabellen (umgekehrte Reihenfolge)
-- Hinweis: Ersetze YOUR_LIB oder verwende SET SCHEMA
-- Ausführung: db2 -t -f dropTables.sql
-- Erwartete Fehler möglich, falls Tabellen nicht existieren
-- =============================================

SET SCHEMA YOUR_LIB;

-- Drop in reverse dependency order: children first
DROP TABLE ORDERS;
DROP TABLE CUSTOMERS;
DROP TABLE AGENTS;
DROP TABLE AGENT_REVENUE;
DROP TABLE EAV_DATA;
DROP TABLE EAV_METADATA;
