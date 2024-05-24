# Fuegobase
![Static Badge](https://img.shields.io/badge/AWS-Service-grey?logo=amazonaws&color=orange) ![Static Badge](https://img.shields.io/badge/Backend-SpringBoot-grey?logo=springboot&color=green) 
![Static Badge](https://img.shields.io/badge/Frontend-React-grey?logo=react&color=1b81a6)

Fuegobase is a service that operates a database without the need for setting up a backend server, achieving CRUD functionality through a visual interface.  

- Website link：https://fuegobase.store/      
- Front-End repository：https://github.com/leslie1612/Fuegobase-react     
- Test account：       
	|  Email  |  Password  |  
	|  :----: |  :----:  | 
	|  fuegobaseadmin@gmail.com  |  fuegobaseadmin  |  



## Main Features
- Database - Store data of different types as required: String, Number, Boolean, Array, Map.
- Query - Quickly search for data within a Collection using f key.
- Details - Display the project's API Key and manage the list of authorized domains.
- Dashboard - Display the project's current storage amount and daily read/write count records.

## Architecture
![image](https://github.com/leslie1612/Fuegobase/blob/main/assets/fuegobase-infrastructure.png)

## How to use
### Website 
#### Database   
![image](https://github.com/leslie1612/Fuegobase/blob/main/assets/fuegobase-database-converter.gif)   	  
#### Query    
- Number     
	![image](https://github.com/leslie1612/Fuegobase/blob/main/assets/fuegobase-query-number-converter.gif)    
- Map    
	![image](https://github.com/leslie1612/Fuegobase/blob/main/assets/fuegobase-query-map-converter.gif)    
#### Details    
![image](https://github.com/leslie1612/Fuegobase/blob/main/assets/fuegobase-details-converter.gif)     
#### Dashboard     
![image](https://github.com/leslie1612/Fuegobase/blob/main/assets/fuegobase-date-pick-converter.gif)     

### API calls
- Include `x-api-key: "your_project_api_key"` in the HTTP request header, and ensure that the website domain is on the authorized list.
- Obtain the same data (in JSON format) through the API calls as displayed on the interface, allowing users to process and use the data for further applications.

## Technique
#### Frameworks and Libraries
 ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)  ![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)   ![React Router](https://img.shields.io/badge/React_Router-CA4245?style=for-the-badge&logo=react-router&logoColor=white)
- Spring Boot
- React
#### Database 
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white) 
- MySQL
- Redis
#### Cloud Service (AWS)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white) 
- Elastic Compute Cloud (EC2)
- Relational Database Service (RDS)
- Simple Storage Service (S3)
- ElastiCache
- Lambda
- CloudWatch
- Simple Notification Service (SNS)
- Application Load Balancer (ALB)
- Route 53

## Contact
🧑‍💻 Chih-Yu, Chou   
✉️ leslie20100430@gmail.com
