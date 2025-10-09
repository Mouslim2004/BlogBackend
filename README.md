We are going to create a blog for my portfolio using spring boot 

### Create Posts entity

1. Create a package called entity with a class Posts in it which is going to represent our model 
2. While creating posts entity we are going to create also Status enumeration 

### Create Posts repository

1. Create a package called repository with an interface PostsRepository which is going to simplifies database interactions

### Create Posts service 

1. Create a package called service with a class PostsService which is going to apply the business rules to fulfill the app
2. Within post service initialize post repository

### Create Posts controller

1. Create a package called controller with a class PostsController to handle incoming http request 
2. Within post controller initialize post service
3. Then we are going to implement the logic to manage our posts using post service
4. But we don't want to expose our internal database entity to the client so we are going to use dto 
5. So create a package called dto which is going to be used for transferring data between different layers of the app
6. With that we gonna need a mapper to convert java object

### Create docker-compose file to set up our images

### Refactor application.properties to .yml in order to link to our db

### Create all entity 

1. In this part we are going to set all model of our application 
2. Some of them are : User, Comment, Tag, Category
3. So go to entity package and create them 















