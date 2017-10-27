---

Database
=========
##Berkeley CS61B Project 2

This project is an Implement of domain-specific language like SQL developed by using Java.

I built a small version of relational database management system with standard functions as below.

### Function
*   **Create Table** 
*   **Load Table**
*   **Store Table** 
*   **Drop Table** 
*   **Print Table** 
*   **Extract specific data** from database with complicated request `Select`.

### Example
Below are serveral examples of interaction with an instance of the database. A line starting with '> ' 
indicates the input of the user. And successive lines following is the output returned by the management

*    Load and print table from disk by using `Load` and `Pring` respectively.

[![image](https://github.com/Bruce-Chan/Database/raw/master/ScreenShot/1.png)](#capture)


*    Find all fans who's last name comes after 'Lee', and their favorite teams by using `Select`

[![image](https://github.com/Bruce-Chan/Database/raw/master/ScreenShot/2.png)](#capture)


*    Find all the mascots for teams younger than 75 years

[![image](https://github.com/Bruce-Chan/Database/raw/master/ScreenShot/3.png)](#capture)


*    Find all the seasons in which a sports team did poorly, and the city in which they were based

[![image](https://github.com/Bruce-Chan/Database/raw/master/ScreenShot/4.png)](#capture)
