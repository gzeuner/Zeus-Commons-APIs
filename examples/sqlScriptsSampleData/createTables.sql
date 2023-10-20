-- Create the AGENTS table with a primary key constraint
CREATE TABLE AGENTS (
   AGENT_CODE    VARCHAR(6)            NOT NULL,
   AGENT_NAME    VARCHAR(40)           NOT NULL,
   WORKING_AREA  VARCHAR(35)           NOT NULL,
   COMMISSION    DECIMAL(10,2)         NOT NULL,
   PHONE_NO      VARCHAR(15)           NOT NULL,
   COUNTRY       VARCHAR(25)           NOT NULL,
   PRIMARY KEY (AGENT_CODE)
);

-- Create the CUSTOMERS table with primary key and foreign key constraints
CREATE TABLE CUSTOMERS (
   CUST_CODE        VARCHAR(6)          NOT NULL,
   CUST_NAME        VARCHAR(40)         NOT NULL,
   CUST_CITY        VARCHAR(35)         NOT NULL,
   WORKING_AREA     VARCHAR(35)         NOT NULL,
   CUST_COUNTRY     VARCHAR(20)         NOT NULL,
   GRADE            INTEGER             NOT NULL,
   OPENING_AMT      DECIMAL(12,2)       NOT NULL,
   RECEIVE_AMT      DECIMAL(12,2)       NOT NULL,
   PAYMENT_AMT      DECIMAL(12,2)       NOT NULL,
   OUTSTANDING_AMT  DECIMAL(12,2)       NOT NULL,
   PHONE_NO         VARCHAR(17)         NOT NULL,
   AGENT_CODE       VARCHAR(6)          NOT NULL,
   PRIMARY KEY (CUST_CODE),
   FOREIGN KEY (AGENT_CODE) REFERENCES AGENTS(AGENT_CODE)
);

-- Create the ORDERS table with primary key and foreign key constraints
CREATE TABLE ORDERS (
   ORD_NUM          DECIMAL(6)          NOT NULL,
   ORD_AMOUNT       DECIMAL(12,2)       NOT NULL,
   ADVANCE_AMOUNT   DECIMAL(12,2)       NOT NULL,
   ORD_DATE         DATE                NOT NULL,
   CUST_CODE        VARCHAR(6)          NOT NULL,
   AGENT_CODE       VARCHAR(6)          NOT NULL,
   ORD_DESCRIPTION  VARCHAR(90)         NOT NULL,
   PRIMARY KEY (ORD_NUM),
   FOREIGN KEY (CUST_CODE) REFERENCES CUSTOMERS(CUST_CODE),
   FOREIGN KEY (AGENT_CODE) REFERENCES AGENTS(AGENT_CODE)
);

-- Create the AGENT_REVENUE table with a primary key constraint
CREATE TABLE AGENT_REVENUE (
   AGENT_CODE          VARCHAR(6)        NOT NULL,
   AGENT_NAME          VARCHAR(40)       NOT NULL,
   CUMULATIVE_REVENUE  DECIMAL(12,2)     NOT NULL,
   PRIMARY KEY (AGENT_CODE)
);
