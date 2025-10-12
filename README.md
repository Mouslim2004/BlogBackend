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

### Create all Comment business rule

1. We gonna start by create Comment dto for creating a comment within dto package
2. Then we gonna create Comment dto for response 
3. Then we gonna create comment service within service package 
4. As long as we work with comment service we gonna create comment mapper 
5. Then create comment controller within controller package 
6. Finish comment part

### We gonna work with the Tag 

1. We want to add the tag when the user will notify on his content word starting with this symbol (#)
2. So we gonna implement a class to extract tag within a content thanks to that pattern within utils package 
3. Then we gonna head to post service to get the service when creating a post and try to retrieve tags in content

### Searching post

1. When looking for a post we have different way to do it as by his content, category or tag
2. In this part we gonna start firstly start to look on a post by his tag
3. So go to post repository to implement method to search post related by specific tag
4. Then we gonna update tag repository which gonna look for a specific tag if exists 
5. Then we gonna implement tag name dto to not expose our table details 
6. Then go to post service to implement solution method 
7. Then close this by implement the request in controller 

### Searching post part 2

1. We gonna implement the business rule to search a post by his content category and title
2. So go to post repository to implement the query
3. Then create a specific post dto for search
4. Then go to post service to implement methods
5. Then finalize by going to post controller to implement api request

### Implement spring security

1. Right now we can say that we accomplished some of the important task to our application
2. But yet not the major part as you notice that any individual can modify or delete a post from another user 
3. Which is not good in a real time app, each user need to have the right to delete or edit only his own posts but not for others
4. So the solution manage this issue is to implement spring security using jwt 
5. In this part, as far as I know we gonna need four java class
6. Firstly we gonna need a custom user details service which is a spring security integration class that bridges our application's user system with spring security's authentication framework(implements user details service and override load by username method)
7. Secondly we gonna a jwt utility class for creating, parsing et validation of JWT tokens or authentication(generate tokens, extract user/role, validate token)
8. Thirdly we gonna need a jwt authentication filter class that intercepts every http request to validate jwt tokens and set up spring security authentication (extends once per request filter and override do filter internal)
9. To close those implementation we gonna need a security config class that defines the overall security behavior (passwordEncoder, authentication manager, cors configuration, filter chain)
10. First of all We gonna start by adding spring security dependencies
11. Then we gonna customize our user details service within config package 
12. Then we gonna create our jwt utility class within the security package 
13. Then implement jwt auth filter 
14. Then implement security config class 





