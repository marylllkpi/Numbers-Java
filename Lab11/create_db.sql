IF DB_ID('Lab11') IS NULL
    CREATE DATABASE Lab11;
GO

USE Lab11;
GO

DROP TABLE IF EXISTS Tasks;
DROP TABLE IF EXISTS Employees;
DROP TABLE IF EXISTS Departments;
GO

CREATE TABLE Departments (
    DepartmentId INTEGER PRIMARY KEY,
    Name         CHAR(30) NOT NULL,
    Phone        CHAR(15) NULL
);

CREATE TABLE Employees (
    EmployeeId   INTEGER PRIMARY KEY,
    LastName     VARCHAR(20) NOT NULL,
    FirstName    VARCHAR(10) NOT NULL,
    Position     VARCHAR(20) NULL,
    DepartmentId INTEGER NOT NULL,
    CONSTRAINT Departments_Employees_FK FOREIGN KEY (DepartmentId)
        REFERENCES Departments (DepartmentId)
);

CREATE TABLE Tasks (
    TaskId       INTEGER IDENTITY(1,1) PRIMARY KEY,
    Description  VARCHAR(50) NOT NULL,
    EmployeeId   INTEGER NOT NULL,
    CONSTRAINT Employees_Tasks_FK FOREIGN KEY (EmployeeId)
        REFERENCES Employees (EmployeeId) ON DELETE CASCADE
);
GO

INSERT INTO Departments (DepartmentId, Name, Phone) VALUES (1, 'IT', '044-111-11-11');
INSERT INTO Departments (DepartmentId, Name, Phone) VALUES (2, 'Sales', '044-222-22-22');
INSERT INTO Departments (DepartmentId, Name, Phone) VALUES (3, 'Accounting', NULL);

INSERT INTO Employees (EmployeeId, LastName, FirstName, Position, DepartmentId) VALUES (1, 'Shevchenko', 'Olena', 'Developer', 1);
INSERT INTO Employees (EmployeeId, LastName, FirstName, Position, DepartmentId) VALUES (2, 'Kovalenko', 'Andrii', 'Tester', 1);
INSERT INTO Employees (EmployeeId, LastName, FirstName, Position, DepartmentId) VALUES (3, 'Bondarenko', 'Iryna', 'Manager', 2);
INSERT INTO Employees (EmployeeId, LastName, FirstName, Position, DepartmentId) VALUES (4, 'Melnyk', 'Taras', 'Sales agent', 2);
INSERT INTO Employees (EmployeeId, LastName, FirstName, Position, DepartmentId) VALUES (5, 'Tkachenko', 'Maria', 'Accountant', 3);
INSERT INTO Employees (EmployeeId, LastName, FirstName, Position, DepartmentId) VALUES (6, 'Kravchenko', 'Oleh', NULL, 3);

INSERT INTO Tasks (Description, EmployeeId) VALUES ('Write the login page', 1);
INSERT INTO Tasks (Description, EmployeeId) VALUES ('Fix bugs in the report module', 1);
INSERT INTO Tasks (Description, EmployeeId) VALUES ('Test the new version', 2);
INSERT INTO Tasks (Description, EmployeeId) VALUES ('Call new customers', 3);
INSERT INTO Tasks (Description, EmployeeId) VALUES ('Prepare the sales plan', 3);
INSERT INTO Tasks (Description, EmployeeId) VALUES ('Visit a customer in Lviv', 4);
INSERT INTO Tasks (Description, EmployeeId) VALUES ('Check the monthly report', 5);
GO

SELECT * FROM Departments;
SELECT * FROM Employees;
SELECT * FROM Tasks;
GO

IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'lab11user')
    CREATE LOGIN lab11user WITH PASSWORD = 'Lab11_Pass123', CHECK_POLICY = OFF;
GO
USE Lab11;
GO
IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'lab11user')
    CREATE USER lab11user FOR LOGIN lab11user;
ALTER ROLE db_owner ADD MEMBER lab11user;
GO
