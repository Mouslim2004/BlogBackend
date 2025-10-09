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

### Set the relationship

1. We are going to set the relationship between all entities 

### Create User, Tag, Comment and Category repository within repository package 

### Create User and Category service within service package

1. We are going to start with the user entity
2. Now we are going to continue with setting category service 

### Create User and Category controller 

1. For the user we are going to have two controller one to create user(auth) and the other one for others requests
2. Then as long as we implement user controller we gonna set dto and mapper for user
3. Then we gonna proceed the same way for category

### Improve post service

1. Now we gonna handle the case for the post a post need to be submitted by an user
2. This is gonna imply some modification on our dto, mapper and controller 
3. While improving post service we noticed than when we get a post we get also the user but with his credentials as the password
4. So to hide some of those important details, we need to create another dto w/ less fields or details 
5. That's why we create UserInfoDTO 

### Assign category to a post 

1. When a post is created it need to be assigned to a category in order to identify what this post is talking about
2. So we gonna add CategoryDTO to post dto and map to post mapper then improve post service



