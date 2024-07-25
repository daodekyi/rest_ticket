package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
//use @RestController to return data to HTTP method
//if we put @Controller we return View (in MVC)
@RequestMapping("/api/v1/user")
//user is base endpoint
@Tag(name = "UserController", description = "User API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ExecutionTime
    @GetMapping
    @RolesAllowed({"Manager", "Admin"})
    //this will work with get method (to connect to HTTP Get request method)
    @Operation(summary = "Get users")
    public ResponseEntity<ResponseWrapper> getUsers(){
        List<UserDTO> userDTOList = userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userDTOList, HttpStatus.OK));

        // ResponseEntity is used to represent the entire HTTP response including response status code, response headers and response body
        // it is response to the constructor: public ResponseWrapper(String message, Object data,HttpStatus httpStatus)
        // the 1st .ok after ResponseEntity is at the right side of postman (in the output), the 2nd .ok status is inside the output content (in JSON file)
        // return statement means give the result to API
    }

    @ExecutionTime
    @GetMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Get user by username")
        public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("username") String username){
        UserDTO user = userService.findByUserName(username);// username is called parameter
        return ResponseEntity.ok(new ResponseWrapper("User is successfully retrieved", user, HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Create user")
    // @PostMapping annotation maps HTTP POST request to /api/v1/user URL to the create method
    // @RequestBody tells Spring to convert JSON request body into '' object
    // this user will come from API via @RequestBody
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO user){
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User is successfully created", HttpStatus.CREATED));
    }

    // @RequestBody: This annotation is applied to a method parameter to indicate that this parameter should be bound to the body of the HTTP request.
    // The body is then converted into a Java object using a message converter (such as Jackson for JSON)

    @PutMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Update user")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user){
        userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully updated", HttpStatus.OK));
    }

    @DeleteMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Delete user by username")
    //we need to do the soft delete, change the filed to true (true mean: delete, false means: not delete)
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username) throws TicketingProjectException {
        userService.delete(username);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully deleted", HttpStatus.OK));
    }
/*
All of these above actions means creating API end points
 */



}
