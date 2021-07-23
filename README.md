# Agents Registration Frontend

This service allows a agent to enter all their details to create their account, to do so the agent
follows our registration path which consists of the pages listed below:

- Business Name Page
- Email Page
- Contact Number Page
- Business Address Page
- Corrorspondence Page
- Password page

### Info

This project is a Scala Web application using <a href="https://github.com/hmrc/hmrc-frontend-scaffold.g8">code scaffolds</a>

### Standards:

To keep the pages accessible to a wide audience, the service follows hmrc guidlines through 
the whole service keeping uniformity throughout.

### Before Service is run 

Please make sure the following setup:
- Have nothing running on ports (9000 & 9009)
- Have scala version: 2.12.13
- Have sbt version: 1.5.2, installed on your computer 
- Have Client-Backend running this uses port (9009)

### Running the service

```
sbt run
```

### Running the service tests

```
sbt test it:test
```

### Running the service tests with Coverage Report

```
sbt clean coverage test it:test coverageReport
```

### Dependencies

This service is dependant the folowing services:
* agents-registration-frontend
* agents-backend

### Routes

Start the service locally by going to http://localhost:9000/registration/business-name

## Authors

* **Ayub Yusuf**
* **Isabel Lee**
* **Nathan Jackson**
* **Chetan Pardeep**
* **Ekip Kalir**
* **Daniel Carter**

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

