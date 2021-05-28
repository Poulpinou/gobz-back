# Gobz Back

Backend for [gobz-app](https://github.com/Poulpinou/gobz-app)

## Install
Create a local *.env* file from the *.env.template* file and fill it with your env variables. A mysql database is required.

## Documentation
Api documentation at *{{host}}/swagger-ui/index.html*.
To access restricted data, you have to authenticate. To do it, try out the */auth/login* route with your email and password.
Copy the token from the response and click on Authorize and paste it with the prefix 'Bearer '. You have now access to your data.