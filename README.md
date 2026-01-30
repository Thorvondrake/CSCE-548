Project: The Nocturne Archive

Developer: Conner Wiley Course: CSCE - 548
Description

A professional-grade Relational Database and Data Access Layer (DAL) designed for a high-end rare book dealer. The system distinguishes between a literary "Work" and a "Physical Copy" to track provenance, condition, and value.
Tech Stack

    Database: MySQL 8.0

    Language: Java

    Driver: MySQL Connector/J (JDBC)

Database Schema

The database consists of 4 relational tables:

    Authors: Stores biographical data on writers.

    Titles: Tracks the abstract literary works.

    Inventory_Items: Tracks the specific physical assets, including condition grades and signatures.

    Sales_Log: Records transaction history.

How to Run

    Execute the provided NocturneArchive.sql script in your MySQL instance.

    Ensure the mysql-connector-j JAR is in your project's referenced libraries.

    Update the credentials in DatabaseManager.java (User: nocturne_admin).

    Run App.java to launch the console interface.