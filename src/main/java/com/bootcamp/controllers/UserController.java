package com.bootcamp.controllers;

import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.entities.User;
import com.bootcamp.security.JwtAuthentification;
import com.bootcamp.services.UserService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;


@RestController("UserController")
@RequestMapping("/users")
@Api(value = "User API", description = "User API")
public class UserController {
    
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest request;

    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Create a new user", notes = "Create a new user")
    public ResponseEntity<User> create(@RequestBody @Valid User user) {

        HttpStatus httpStatus = null;

        try {
            userService.create(user);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<User>(user, httpStatus);
    }

    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "delete a new user", notes = "delete a new user")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        boolean done = false;
        HttpStatus httpStatus = null;

        try {
             done = userService.delete(id);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(done, httpStatus);
    }

    @RequestMapping(value="/{login}",method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "get a  user", notes = "get  a new user")
    public ResponseEntity<Boolean> loginByContact(@PathVariable String conatct,String password) throws Exception{
        boolean done = userService.getByLoginByContact( conatct,password );

        return new ResponseEntity<>(done, HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "get a  user", notes = "get  a new user")
    public ResponseEntity<List<User>> getAll() throws Exception{
        List<User> users = userService.read( request );
        return new ResponseEntity<List<User>>( users,HttpStatus.OK );
    }


    @RequestMapping(method = RequestMethod.PUT)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "update a new user", notes = "update a new user")
    public ResponseEntity<User> update(@RequestBody @Valid User user) {

        HttpStatus httpStatus = null;

        try {
            userService.update(user);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<User>(user, httpStatus);
    }
    
    @RequestMapping(value="/{login}",method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "login", notes = "login")
    public String authentification(@RequestBody @Valid User user) throws SQLException {

        HttpStatus httpStatus = null;
        String token = "";
        
        if(userService.getByLoginAndPwd(user.getId(), user.getContact())){
            token = JwtAuthentification.addAuthentication(user);
            httpStatus = HttpStatus.OK;        
        }

        return token;
    }
        //  THOSES METHODES ARE USELESS FOR THE MOMENT BY THEY WORK 
/*
    @RequestMapping(method = RequestMethod.PUT, value = "/")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Update a new user", notes = "Update a new user")
    public ResponseEntity<User> update(@RequestBody @Valid User user) {

        HttpStatus httpStatus = null;

        try {
            userService.update(user);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<User>(user, httpStatus);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Delete a user", notes = "Delete a user")
    public void delete(@PathVariable(name = "id") int id) {

        HttpStatus httpStatus = null;

        try {
            User user = userService.delete(id);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a user", notes = "Read a user")
    public ResponseEntity<User> read(@PathVariable(name = "id") int id) {

        HttpStatus httpStatus = null;
        User user = new User();
        try {
            user = userService.read(id);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<User>(user, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a user", notes = "Read a user")
    public ResponseEntity<User> read() {

        User user = new User();
        HttpStatus httpStatus = null;

        try {
            List<User> users = userService.read(request);
            httpStatus = HttpStatus.OK;
        }catch (SQLException | IllegalAccessException | DatabaseException | InvocationTargetException exception){
            
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<User>(user, httpStatus);
    }

   */
}
