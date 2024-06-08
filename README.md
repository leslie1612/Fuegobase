# Fuegobase
![Static Badge](https://img.shields.io/badge/aws-service-grey?logo=amazonwebservices&color=orange) ![Static Badge](https://img.shields.io/badge/Backend-SpringBoot-grey?logo=springboot&color=green) 
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
- Query - Quickly search for data within a Collection using field key.
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
When you finish processing the data on the Fuegobase web page, you can use API calls to use the data on your own website:
1. Copy the `Path` displayed on the Database or Query Builder page.
2. Include `x-api-key: "your_project_api_key"` in the HTTP request header, and ensure that the website domain is on the authorized list.
3. (Optional) If you haven't registered a domain yet, you can use Postman for testing. Please add `origin: "http://localhost:80/"` to the HTTP request header.
4. Retrieve the data in JSON format via API calls, and process or apply it further according to your needs.


## Technique
#### Frameworks and Libraries
- Spring Boot
- React
#### Database 
- MySQL
- Redis
#### Cloud Service (AWS)
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
🧑‍💻 Chou, Chih-Yu         
✉️ leslie20100430@gmail.com
