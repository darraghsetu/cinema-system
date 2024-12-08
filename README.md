# Cinema System
A console application written in Kotlin of a menu-based system for a cinema to maintain information about movies, screenings, customers and bookings.

## Features
- Movies
  - Add movies to the system
  - View details of:
    - All movies
    - All movies of a certain age rating
- Screenings
  - Add screenings to the system
  - Delete screenings from the system
  - Update the details of a screening
  - View details of:
    - All screenings
    - All screenings for a movie
    - All screenings on a certain date
    - All screenings for a movie on a certain date
    - All remaining screenings today
    - All remaining screenings this week
    - All remaining screenings for a movie today
    - All remaining screenings for a movie this week
- Customers
  - Add customers to the system
  - Delete customers from the system
  - Update the details of a customer
  - View details of:
    - All customers
    - All customers with a certain first name
    - All customers with a certain last name
    - All customers of a certain age
    - All customers in a certain age group
    - All customers 18 and over
    - All customers under 18
- Bookings
  - Add bookings to the system
  - View details of:
    - All bookings
    - All non-cancelled bookings
    - All cancelled bookings
    - All bookings made by a customer
    - All bookings for a screening
    - All bookings for a movie
   
## Getting Started
To get started:
- Download the project code from the main page and build it in your IDE of choice
  - For Windows:
    - Right click the downloaded Zip file
    - Extract All
    - Open the project in your IDE
    - Build the project
    - Run Main.kt
- Alternatively:
  - Nagivate to the Actions tab
  - Click the most recent workflow run
  - Under Artifacts, click "Jar with Dependencies"
  - For Windows:
    - Right click the downloaded Zip file
    - Choose Extract All
    - Open the extracted folder
    - In the address bar, delete the folder path and type "cmd"
    - In cmd.exe type: java -jar cinema-system-1.0.jar
