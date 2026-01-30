-- =============================================
-- THE NOCTURNE ARCHIVE: MASTER SETUP SCRIPT
-- Project: Rare Book Dealer Inventory & Sales
-- Developer: Conner Wiley
-- =============================================

-- 1. DATABASE INITIALIZATION
CREATE DATABASE IF NOT EXISTS NocturneArchive;
USE NocturneArchive;

-- 2. TABLE CREATION (High-Caliber Relational Model)
-- Authors Table
CREATE TABLE Authors (
    AuthorID INT AUTO_INCREMENT PRIMARY KEY,
    FullName VARCHAR(255) NOT NULL,
    Nationality VARCHAR(100),
    BirthYear INT
);

-- Titles Table (The 'Work')
CREATE TABLE Titles (
    TitleID INT AUTO_INCREMENT PRIMARY KEY,
    AuthorID INT NOT NULL,
    TitleName VARCHAR(255) NOT NULL,
    Genre VARCHAR(100),
    OriginalPublicationYear INT,
    CONSTRAINT fk_author FOREIGN KEY (AuthorID) REFERENCES Authors(AuthorID) ON DELETE CASCADE
);

-- Inventory_Items Table (The Physical Asset)
CREATE TABLE Inventory_Items (
    ItemID INT AUTO_INCREMENT PRIMARY KEY,
    TitleID INT NOT NULL,
    Condition_Grade ENUM('Mint', 'Fine', 'Very Good', 'Good', 'Fair', 'Poor') NOT NULL,
    Edition_Details VARCHAR(255),
    Is_Signed BOOLEAN DEFAULT FALSE,
    Asking_Price DECIMAL(12, 2) NOT NULL CHECK (Asking_Price > 0),
    Stock_Status ENUM('Available', 'Sold', 'Reserved') DEFAULT 'Available',
    CONSTRAINT fk_title FOREIGN KEY (TitleID) REFERENCES Titles(TitleID) ON DELETE CASCADE
);

-- Sales_Log Table
CREATE TABLE Sales_Log (
    SaleID INT AUTO_INCREMENT PRIMARY KEY,
    ItemID INT UNIQUE NOT NULL,
    Sale_Date DATE DEFAULT (CURRENT_DATE),
    Final_Price DECIMAL(12, 2) NOT NULL,
    Buyer_Name VARCHAR(255),
    CONSTRAINT fk_item FOREIGN KEY (ItemID) REFERENCES Inventory_Items(ItemID)
);

-- 3. DATA SEEDING (55+ Rows)
-- Seed Authors
INSERT INTO Authors (FullName, Nationality, BirthYear) VALUES 
('Edgar Allan Poe', 'American', 1809),
('H.P. Lovecraft', 'American', 1890),
('Mary Shelley', 'British', 1797),
('Bram Stoker', 'Irish', 1847),
('Shirley Jackson', 'American', 1916);

-- Seed Titles
INSERT INTO Titles (AuthorID, TitleName, Genre, OriginalPublicationYear) VALUES 
(1, 'The Raven', 'Gothic Poetry', 1845),
(2, 'The Call of Cthulhu', 'Cosmic Horror', 1928),
(3, 'Frankenstein', 'Gothic Fiction', 1818),
(4, 'Dracula', 'Vampire Fiction', 1897),
(5, 'The Haunting of Hill House', 'Psychological Horror', 1959);

-- Bulk Seed Inventory (55 Items)
INSERT INTO Inventory_Items (TitleID, Condition_Grade, Edition_Details, Is_Signed, Asking_Price)
SELECT 
    (t.TitleID),
    (CASE WHEN r.n % 5 = 0 THEN 'Mint' WHEN r.n % 2 = 0 THEN 'Fine' ELSE 'Very Good' END),
    CONCAT('Special Printing #', r.n),
    (CASE WHEN r.n % 10 = 0 THEN TRUE ELSE FALSE END),
    (500.00 + (r.n * 150))
FROM Titles t
JOIN (
    SELECT a.N + b.N * 10 + 1 n
    FROM (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a
    CROSS JOIN (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) b
) r
WHERE r.n <= 55 AND t.TitleID = (r.n % 5) + 1;