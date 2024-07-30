# Custom Authentication with Spring security
* Built a custom secure token using Symmetric Advanced Encryption Algorithm. 
* This token is a custom version token similar to JWT token.
* This token can be achieved by /token endpoint associated with mobile number.
* Token acquired needs to be passed in request header with name **x-auth-token** for secured APIs.
* Implemented custom security filters to do custom authentication reading custom http header.
* User will be authenticated based upon the validation of token value.

### Components Used

* spring starter web
* spring starter security