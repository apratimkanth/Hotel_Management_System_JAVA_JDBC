# Hotel Management System

A Java-based Hotel Management System that interacts with a MySQL database using JDBC to manage hotel room availability, reservations, and customer details.

## Features

- **Room Availability Check**: Check if any room is available for reservation.
- **New Reservation**: Create a new reservation for customers by entering their name, ID proof, and checkout date.
- **Reservation Details**: Retrieve and display reservation details by customer name or ID.
- **Database Update**: Automatically update the room status based on check-in and check-out times.

## Technologies Used

- **Java**: Core language for building the application logic.
- **MySQL**: Database for storing hotel, room, and reservation details.
- **JDBC (Java Database Connectivity)**: Interface for connecting Java applications with MySQL.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server
- JDBC Driver for MySQL

## Database Setup

1. **Install MySQL** and ensure it is running.
2. **Create the database** using the following command:
   ```sql
   CREATE DATABASE javaproject;
# Hotel Management System

A Java-based Hotel Management System that interacts with a MySQL database using JDBC to manage hotel room availability, reservations, and customer details.

## Features

- **Room Availability Check**: Check if any room is available for reservation.
- **New Reservation**: Create a new reservation for customers by entering their name, ID proof, and checkout date.
- **Reservation Details**: Retrieve and display reservation details by customer name or ID.
- **Database Update**: Automatically update the room status based on check-in and check-out times.

## Technologies Used

- **Java**: Core language for building the application logic.
- **MySQL**: Database for storing hotel, room, and reservation details.
- **JDBC (Java Database Connectivity)**: Interface for connecting Java applications with MySQL.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server
- JDBC Driver for MySQL

## Database Setup

1. **Install MySQL** and ensure it is running.
2. **Create the database** using the following command:
   ```sql
   CREATE DATABASE javaproject;
Insert room details in the roomstatus table for initial data:
INSERT INTO roomstatus (Room_No, Status) VALUES (101, 1), (102, 1), (103, 1);
How to Run
Clone or download this repository.
Update the database credentials: In the Main class, update the url, username, and password as per your MySQL setup:
final static String url = "jdbc:mysql://localhost:3306/javaproject";
final static String username = "root";
final static String password = "your_password";

javac Main.java
java Main

## Menu Options
Upon running the program, the following options will be available in the console:

Room availability: Check the availability of rooms in the hotel.
New Reservation: Make a new reservation for a customer.
Check Reservation: View the reservation details for a customer.
Update Reservation: Update the reservation details (in future features).
Delete Reservation: Delete a reservation (in future features).
Exit: Exit the application.
Example Usage
Check Room Availability
When you choose "Room availability," the system will check the roomstatus table and return the first available room.

Make a New Reservation
To create a reservation, you'll need to provide:

Customer Name
Customer ID Proof Number
Customer Checkout Date (format: dd-MM-yyyy)
If a room is available, it will be allocated to the customer, and the room status will be updated in the roomstatus table.

View Reservation Details
Enter the customer name or ID proof to retrieve their reservation information, including room number, check-in, and check-out times.