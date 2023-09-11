-- Add new records to the AGENTS table
INSERT INTO AGENTS (AGENT_CODE, AGENT_NAME, WORKING_AREA, COMMISSION, PHONE_NO, COUNTRY)
VALUES
    ('A101', 'John Smith', 'Berlin', 10.5, '1234567890', 'Germany'),
    ('A102', 'Maria García', 'Paris', 12.0, '9876543210', 'France'),
    ('A103', 'Hans Müller', 'Madrid', 9.75, '5678901234', 'Spain'),
    ('A104', 'Sophia Rossi', 'Rome', 11.25, '3456789012', 'Italy'),
    ('A105', 'James Johnson', 'London', 13.0, '6789012345', 'UK');

-- Add new records to the CUSTOMERS table
INSERT INTO CUSTOMERS (CUST_CODE, CUST_NAME, CUST_CITY, WORKING_AREA, CUST_COUNTRY, GRADE, OPENING_AMT, RECEIVE_AMT, PAYMENT_AMT, OUTSTANDING_AMT, PHONE_NO, AGENT_CODE)
VALUES
    ('C101', 'Luisa Torres', 'Berlin', 'Berlin', 'Germany', 1, 5000.00, 3000.00, 2000.00, 1000.00, '+491122334455', 'A101'),
    ('C102', 'Sven Hansen', 'Paris', 'Paris', 'France', 2, 6000.00, 3500.00, 2500.00, 1500.00, '+331122334455', 'A102'),
    ('C103', 'Elena López', 'Madrid', 'Madrid', 'Spain', 3, 7000.00, 4000.00, 3000.00, 1000.00, '+341122334455', 'A103'),
    ('C104', 'Giovanni Russo', 'Rome', 'Rome', 'Italy', 4, 8000.00, 4500.00, 3500.00, 1500.00, '+391122334455', 'A104'),
    ('C105', 'Emily Smith', 'London', 'London', 'UK', 5, 9000.00, 5000.00, 4000.00, 1000.00, '+441122334455', 'A105'),
    ('C106', 'François Dubois', 'Paris', 'Paris', 'France', 2, 6500.00, 3800.00, 2700.00, 1800.00, '+331122334466', 'A102'),
    ('C107', 'Isabella Ferrari', 'Rome', 'Rome', 'Italy', 3, 7200.00, 4200.00, 3200.00, 1200.00, '+391122334477', 'A104'),
    ('C108', 'Elena López', 'Barcelona', 'Barcelona', 'Spain', 4, 8100.00, 4600.00, 3600.00, 1500.00, '+341122334488', 'A103'),
    ('C109', 'Müller Schmidt', 'Berlin', 'Berlin', 'Germany', 1, 5500.00, 3200.00, 2200.00, 1100.00, '+491122334499', 'A101'),
    ('C110', 'Oliver Smith', 'London', 'London', 'UK', 5, 9200.00, 5200.00, 4200.00, 1000.00, '+441122334411', 'A105');

-- Add new records to the ORDERS table
INSERT INTO ORDERS (ORD_NUM, ORD_AMOUNT, ADVANCE_AMOUNT, ORD_DATE, CUST_CODE, AGENT_CODE, ORD_DESCRIPTION)
VALUES
    (1, 1000.00, 500.00, '2023-09-10', 'C101', 'A101', 'Luxuriöse Urlaubsbuchung für eine Traumreise'),
    (2, 1500.00, 700.00, '2023-09-09', 'C102', 'A102', 'Commande exclusive de bijoux artisanaux'),
    (3, 2000.00, 900.00, '2023-09-08', 'C103', 'A103', 'Desarrollo de software personalizado para tu negocio'),
    (4, 2500.00, 1100.00, '2023-09-07', 'C104', 'A104', 'Abito da sera elegante per un''occasione speciale'),
    (5, 3000.00, 1300.00, '2023-09-06', 'C105', 'A105', 'Smartphone haut de gamme avec les dernières fonctionnalités'),
    (6, 1200.00, 600.00, '2023-09-05', 'C106', 'A102', 'Commande spéciale de bijoux exquis'),
    (7, 1800.00, 800.00, '2023-09-04', 'C107', 'A104', 'Abito elegante per una serata speciale'),
    (8, 2200.00, 1000.00, '2023-09-03', 'C108', 'A103', 'Desarrollo de software de vanguardia para empresas'),
    (9, 2700.00, 1300.00, '2023-09-02', 'C109', 'A101', 'Luxuriöses Wellness-Paket für Entspannung pur'),
    (10, 3200.00, 1500.00, '2023-09-01', 'C110', 'A105', 'Smartphone premium avec une caméra de pointe'),
    (11, 1400.00, 600.00, '2023-08-31', 'C101', 'A101', 'Weitere Buchung für den Traumurlaub'),
    (12, 800.00, 400.00, '2023-08-30', 'C101', 'A101', 'Zusätzliche Reiseaktivitäten'),
    (13, 1700.00, 800.00, '2023-08-29', 'C102', 'A102', 'Zusätzliche Schmuckstücke'),
    (14, 2200.00, 1000.00, '2023-08-28', 'C102', 'A102', 'Ersatzteile für Schmuckdesign'),
    (15, 2800.00, 1200.00, '2023-08-27', 'C102', 'A102', 'Weitere handgefertigte Schmuckstücke'),
    (16, 2500.00, 1000.00, '2023-08-26', 'C103', 'A103', 'Zusätzliche Lizenzgebühren für Software'),
    (17, 2800.00, 1200.00, '2023-08-25', 'C103', 'A103', 'Software-Update und Wartung'),
    (18, 3200.00, 1400.00, '2023-08-24', 'C103', 'A103', 'Erweiterung des CRM-Moduls'),
    (19, 2000.00, 900.00, '2023-08-23', 'C104', 'A104', 'Weitere maßgeschneiderte Abendkleider'),
    (20, 1500.00, 700.00, '2023-08-22', 'C105', 'A105', 'Zusätzliches Zubehör für das Smartphone');

-- Insert the accumulated sales for each agent into AGENT_REVENUE
INSERT INTO AGENT_REVENUE (AGENT_CODE, AGENT_NAME, CUMULATIVE_REVENUE)
SELECT 
    O.AGENT_CODE, 
    A.AGENT_NAME,
    SUM(O.ORD_AMOUNT)
FROM 
    ORDERS O
JOIN
    AGENTS A ON O.AGENT_CODE = A.AGENT_CODE
GROUP BY 
    O.AGENT_CODE, A.AGENT_NAME;
