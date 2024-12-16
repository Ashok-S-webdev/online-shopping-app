# online-shopping-app

A Simple Online Shopping App built using html, css and javascript for front-end, java + jersey for back-end and MySQL for database.

This app features,

- User Regisrtration
- Session handling
- Role based filters
- Bcrypt password encryption and checking
- Login using OTP sent to User mobile (Twilio SMS API)
- Admin Dashboard
- Adding, rmeoving or updating products
- User home page with pagination for products
- Cart for each users
- Adding product to the cart
- Incrementing, decrementing or removing product in cart
- Checkout option
- Generating bill as PDF
- Sending confirmation notification through mail with bill(pdf)


# Installation

## Cloning the project
Clone the online-shopping-app project

## Setting environment variables
Create a file named .env in the src/main/resource folder.

Copy the contents of the src/main/resource/.env.example file and replace all the values with your data.

## Running the app
run <mvn clean install>

Copy the generated online-shopping-app.war file and place it in the webapp folder of your tomcat

Run the tomcat servet. Go to localhost:<port_number>/online-shopping-app/
