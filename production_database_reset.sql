-- =============================================
-- THE NOCTURNE ARCHIVE: COMPLETE DATABASE RESET & SETUP
-- Project: Rare Book Dealer Inventory & Sales
-- Developer: Conner Wiley
-- Updated: March 2026 - Production Ready Version
-- =============================================

-- This script provides a complete database reset for maximum compatibility
-- with the current Java codebase and removes any test data pollution.

-- STEP 1: COMPLETE DATABASE RESET
-- =============================================
USE defaultdb;

-- Disable foreign key checks for clean reset
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all tables if they exist (clean slate)
DROP TABLE IF EXISTS Sales_Log;
DROP TABLE IF EXISTS Inventory_Items;
DROP TABLE IF EXISTS Titles;
DROP TABLE IF EXISTS Authors;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- STEP 2: TABLE CREATION (PRODUCTION SCHEMA)
-- =============================================

-- Authors Table - Enhanced for production
CREATE TABLE Authors (
    AuthorID INT AUTO_INCREMENT PRIMARY KEY,
    FullName VARCHAR(255) NOT NULL,
    Nationality VARCHAR(100),
    BirthYear INT,
    INDEX idx_author_name (FullName),
    INDEX idx_author_nationality (Nationality)
);

-- Titles Table - Enhanced with better indexing
CREATE TABLE Titles (
    TitleID INT AUTO_INCREMENT PRIMARY KEY,
    AuthorID INT NOT NULL,
    TitleName VARCHAR(255) NOT NULL,
    Genre VARCHAR(100),
    OriginalPublicationYear INT,
    INDEX idx_title_name (TitleName),
    INDEX idx_title_genre (Genre),
    INDEX idx_title_year (OriginalPublicationYear),
    CONSTRAINT fk_author FOREIGN KEY (AuthorID) REFERENCES Authors(AuthorID) ON DELETE CASCADE
);

-- Inventory_Items Table - Enhanced for better performance
CREATE TABLE Inventory_Items (
    ItemID INT AUTO_INCREMENT PRIMARY KEY,
    TitleID INT NOT NULL,
    Condition_Grade ENUM('Mint', 'Fine', 'Very Good', 'Good', 'Fair', 'Poor') NOT NULL,
    Edition_Details VARCHAR(255),
    Is_Signed BOOLEAN DEFAULT FALSE,
    Asking_Price DECIMAL(12, 2) NOT NULL CHECK (Asking_Price > 0),
    Stock_Status ENUM('Available', 'Sold', 'Reserved') DEFAULT 'Available',
    Date_Added DATE DEFAULT (CURRENT_DATE),
    INDEX idx_condition (Condition_Grade),
    INDEX idx_signed (Is_Signed),
    INDEX idx_price (Asking_Price),
    INDEX idx_status (Stock_Status),
    CONSTRAINT fk_title FOREIGN KEY (TitleID) REFERENCES Titles(TitleID) ON DELETE CASCADE
);

-- Sales_Log Table - Enhanced with better tracking
CREATE TABLE Sales_Log (
    SaleID INT AUTO_INCREMENT PRIMARY KEY,
    ItemID INT UNIQUE NOT NULL,
    Sale_Date DATE DEFAULT (CURRENT_DATE),
    Final_Price DECIMAL(12, 2) NOT NULL,
    Buyer_Name VARCHAR(255),
    Sale_Notes TEXT,
    INDEX idx_sale_date (Sale_Date),
    INDEX idx_final_price (Final_Price),
    INDEX idx_buyer (Buyer_Name),
    CONSTRAINT fk_item FOREIGN KEY (ItemID) REFERENCES Inventory_Items(ItemID)
);

-- STEP 3: PRODUCTION DATA SEEDING
-- =============================================

-- Seed Authors (Expanded Gothic/Horror Collection)
INSERT INTO Authors (FullName, Nationality, BirthYear) VALUES 
-- Original Gothic Masters
('Edgar Allan Poe', 'American', 1809),
('H.P. Lovecraft', 'American', 1890),
('Mary Shelley', 'British', 1797),
('Bram Stoker', 'Irish', 1847),
('Shirley Jackson', 'American', 1916),

-- Additional Classic Gothic/Horror Authors
('Arthur Conan Doyle', 'British', 1859),
('Washington Irving', 'American', 1783),
('Algernon Blackwood', 'British', 1869),
('M.R. James', 'British', 1862),
('William Hope Hodgson', 'British', 1877),

-- Victorian Gothic Masters
('Oscar Wilde', 'Irish', 1854),
('Robert Louis Stevenson', 'Scottish', 1850),
('Wilkie Collins', 'British', 1824),
('Charles Dickens', 'British', 1812),
('Arthur Machen', 'Welsh', 1863);

-- Seed Titles (Comprehensive Gothic Library)
INSERT INTO Titles (AuthorID, TitleName, Genre, OriginalPublicationYear) VALUES 
-- Edgar Allan Poe
(1, 'The Raven and Other Poems', 'Gothic Poetry', 1845),
(1, 'Tales of the Grotesque and Arabesque', 'Gothic Fiction', 1840),
(1, 'The Fall of the House of Usher', 'Gothic Short Story', 1839),

-- H.P. Lovecraft
(2, 'The Call of Cthulhu', 'Cosmic Horror', 1928),
(2, 'At the Mountains of Madness', 'Cosmic Horror', 1936),
(2, 'The Shadow over Innsmouth', 'Cosmic Horror', 1931),

-- Mary Shelley
(3, 'Frankenstein', 'Gothic Fiction', 1818),
(3, 'The Last Man', 'Gothic Science Fiction', 1826),

-- Bram Stoker
(4, 'Dracula', 'Vampire Fiction', 1897),
(4, 'The Jewel of Seven Stars', 'Gothic Horror', 1903),

-- Shirley Jackson
(5, 'The Haunting of Hill House', 'Psychological Horror', 1959),
(5, 'We Have Always Lived in the Castle', 'Gothic Fiction', 1962),

-- Arthur Conan Doyle
(6, 'The Hound of the Baskervilles', 'Gothic Mystery', 1902),
(6, 'The Adventure of the Speckled Band', 'Mystery', 1892),

-- Washington Irving
(7, 'The Legend of Sleepy Hollow', 'Gothic Tale', 1820),
(7, 'Rip Van Winkle', 'Fantasy', 1819),

-- Algernon Blackwood
(8, 'The Willows', 'Supernatural Horror', 1907),
(8, 'The Wendigo', 'Supernatural Horror', 1910),

-- M.R. James
(9, 'Ghost Stories of an Antiquary', 'Ghost Stories', 1904),
(9, 'A Thin Ghost and Others', 'Ghost Stories', 1919),

-- William Hope Hodgson
(10, 'The House on the Borderland', 'Weird Fiction', 1908),
(10, 'The Night Land', 'Weird Fiction', 1912),

-- Oscar Wilde
(11, 'The Picture of Dorian Gray', 'Gothic Fiction', 1890),
(11, 'The Canterville Ghost', 'Gothic Comedy', 1887),

-- Robert Louis Stevenson
(12, 'Strange Case of Dr Jekyll and Mr Hyde', 'Gothic Fiction', 1886),
(12, 'The Master of Ballantrae', 'Gothic Fiction', 1889),

-- Wilkie Collins
(13, 'The Moonstone', 'Gothic Mystery', 1868),
(13, 'The Woman in White', 'Gothic Mystery', 1860),

-- Charles Dickens
(14, 'A Christmas Carol', 'Gothic Ghost Story', 1843),
(14, 'The Mystery of Edwin Drood', 'Gothic Mystery', 1870),

-- Arthur Machen
(15, 'The Great God Pan', 'Horror', 1894),
(15, 'The Hill of Dreams', 'Decadent Fiction', 1907);

-- Advanced Inventory Seeding (55+ Realistic Rare Book Items)
-- Using dynamic generation to ensure exactly 55+ inventory items
INSERT INTO Inventory_Items (TitleID, Condition_Grade, Edition_Details, Is_Signed, Asking_Price, Stock_Status)
SELECT 
    -- Cycle through titles (1-30)
    ((n.num - 1) % 30) + 1 AS TitleID,
    
    -- Realistic condition distribution
    CASE 
        WHEN n.num % 15 = 0 THEN 'Mint'
        WHEN n.num % 8 = 0 THEN 'Fine' 
        WHEN n.num % 4 = 0 THEN 'Very Good'
        WHEN n.num % 3 = 0 THEN 'Good'
        WHEN n.num % 7 = 0 THEN 'Fair'
        ELSE 'Very Good'
    END AS Condition_Grade,
    
    -- Varied edition details
    CASE 
        WHEN n.num <= 10 THEN CONCAT('First Edition, Item #', n.num)
        WHEN n.num <= 20 THEN CONCAT('Limited Edition #', n.num, '/1000')
        WHEN n.num <= 30 THEN CONCAT('Illustrated Edition, Item #', n.num)
        WHEN n.num <= 40 THEN CONCAT('Modern Reprint, Copy #', n.num)
        WHEN n.num <= 50 THEN CONCAT('Special Binding, Item #', n.num)
        ELSE CONCAT('Collector Edition #', n.num)
    END AS Edition_Details,
    
    -- Signed books (10% of inventory)
    CASE WHEN n.num % 10 = 0 THEN TRUE ELSE FALSE END AS Is_Signed,
    
    -- Realistic pricing based on condition and signed status
    CASE 
        -- Mint condition pricing
        WHEN n.num % 15 = 0 AND n.num % 10 = 0 THEN 15000.00 + (n.num * 200)  -- Mint + Signed
        WHEN n.num % 15 = 0 THEN 8000.00 + (n.num * 150)                       -- Mint
        
        -- Fine condition pricing  
        WHEN n.num % 8 = 0 AND n.num % 10 = 0 THEN 12000.00 + (n.num * 180)   -- Fine + Signed
        WHEN n.num % 8 = 0 THEN 3500.00 + (n.num * 120)                       -- Fine
        
        -- Very Good condition pricing
        WHEN n.num % 4 = 0 AND n.num % 10 = 0 THEN 5000.00 + (n.num * 100)    -- VG + Signed
        WHEN n.num % 4 = 0 THEN 1200.00 + (n.num * 80)                        -- Very Good
        
        -- Good condition pricing
        WHEN n.num % 3 = 0 AND n.num % 10 = 0 THEN 2800.00 + (n.num * 60)     -- Good + Signed
        WHEN n.num % 3 = 0 THEN 400.00 + (n.num * 50)                         -- Good
        
        -- Fair condition pricing
        WHEN n.num % 7 = 0 THEN 150.00 + (n.num * 25)                         -- Fair
        
        -- Default Very Good pricing
        ELSE 800.00 + (n.num * 75)
    END AS Asking_Price,
    
    -- Most items available, some sold for sales history
    CASE 
        WHEN n.num <= 5 THEN 'Sold'
        WHEN n.num = 55 THEN 'Reserved'
        ELSE 'Available' 
    END AS Stock_Status
    
FROM (
    -- Generate numbers 1 through 60 to ensure 55+ items
    SELECT a.N + b.N * 10 + c.N * 100 + 1 as num
    FROM 
        (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION 
         SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a
    CROSS JOIN 
        (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION 
         SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b
    CROSS JOIN
        (SELECT 0 AS N) c
    WHERE a.N + b.N * 10 + c.N * 100 + 1 <= 60
) n
WHERE n.num <= 60;

-- Sample Sales Data (Historical Records for Sold Items)
INSERT INTO Sales_Log (ItemID, Sale_Date, Final_Price, Buyer_Name, Sale_Notes) VALUES
(1, '2025-12-15', 950.00, 'Dr. Margaret Whitmore', 'Academic collection purchase'),
(2, '2025-11-28', 1120.00, 'Vincent Blackthorne Esq.', 'Private collector, rare poetry collection'),
(3, '2025-10-10', 1040.00, 'Ravenshollow University', 'Library special collections acquisition'),
(4, '2025-09-22', 1180.00, 'Hannah Morrison', 'Gothic literature enthusiast, first edition'),
(5, '2025-08-05', 1225.00, 'James Crawford', 'Collector specializing in Victorian horror');

-- STEP 4: VERIFICATION & SUMMARY
-- =============================================

-- Show final counts
SELECT 'DATABASE RESET COMPLETE' AS Status;

SELECT 
    'Authors' AS Entity, 
    COUNT(*) AS Total_Records,
    MIN(BirthYear) AS Earliest_Birth,
    MAX(BirthYear) AS Latest_Birth
FROM Authors

UNION ALL

SELECT 
    'Titles' AS Entity, 
    COUNT(*) AS Total_Records,
    MIN(OriginalPublicationYear) AS Earliest_Publication,
    MAX(OriginalPublicationYear) AS Latest_Publication
FROM Titles

UNION ALL

SELECT 
    'Inventory' AS Entity, 
    COUNT(*) AS Total_Records,
    CAST(MIN(Asking_Price) AS SIGNED) AS Lowest_Price,
    CAST(MAX(Asking_Price) AS SIGNED) AS Highest_Price
FROM Inventory_Items

UNION ALL

SELECT 
    'Sales' AS Entity, 
    COUNT(*) AS Total_Records,
    CAST(MIN(Final_Price) AS SIGNED) AS Lowest_Sale,
    CAST(MAX(Final_Price) AS SIGNED) AS Highest_Sale
FROM Sales_Log;

-- Show sample inventory by condition
SELECT 
    Condition_Grade,
    COUNT(*) AS Item_Count,
    ROUND(AVG(Asking_Price), 2) AS Avg_Price,
    SUM(CASE WHEN Is_Signed = TRUE THEN 1 ELSE 0 END) AS Signed_Items
FROM Inventory_Items 
GROUP BY Condition_Grade
ORDER BY 
    CASE Condition_Grade 
        WHEN 'Mint' THEN 1
        WHEN 'Fine' THEN 2
        WHEN 'Very Good' THEN 3
        WHEN 'Good' THEN 4
        WHEN 'Fair' THEN 5
        WHEN 'Poor' THEN 6
    END;

SELECT 'NOCTURNE ARCHIVE PRODUCTION DATABASE READY' AS Final_Status;

-- =============================================
-- PRODUCTION NOTES:
-- - All test data removed
-- - 15 legitimate authors added  
-- - 30 classic gothic/horror titles
-- - 60 realistic inventory items with proper pricing (55+ requirement met)
-- - 5 sample historical sales matching sold inventory
-- - Enhanced indexing for better performance
-- - Full compatibility with Java codebase
-- - Realistic rare book pricing ($150-$27000+ range)
-- =============================================