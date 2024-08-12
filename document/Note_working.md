### Create and run H2 database
### Run servicesSetup Swagger: using only the new spring open-api
### Call authentication from api-gateway to auth-service
    1.  Yes, you can delegate requests from the api-gateway to the target service without creating a specific controller in the api-gateway by using Spring Cloud Gateway. This allows you to route requests to the appropriate service based on the URL path. 

### Call get users list from api-gateway to user-service.