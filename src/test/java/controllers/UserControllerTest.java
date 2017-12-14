package controllers;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.controllers.UserController;
import com.bootcamp.entities.User;
import com.bootcamp.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
import org.junit.runner.RunWith;
*/
/*
 *
 * Created by darextossa on 12/5/17.
 */

//@TestExecutionListeners(MockitoTestExecutionListener.class)
//@ActiveProfiles({"controller-unit-test"})
//@SpringBootApplication(scanBasePackages={"com.bootcamp"})
//@ContextConfiguration(classes = ControllerUnitTestConfig.class)
//@WebMvcTest(value = PilierController.class, secure = false, excludeAutoConfiguration = {HealthIndicatorAutoConfiguration.class, HibernateJpaAutoConfiguration.class, FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class})

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
@ContextConfiguration(classes={Application.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;


    @Test
    public void getAllUser() throws Exception{
        List<User> users =  loadDataUserFromJsonFile();
        //Mockito.mock(UserCRUD.class);
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.
                when(userService.read(Mockito.any(HttpServletRequest.class))).thenReturn(users);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());
        System.out.println("*********************************Test for get all pilar  in secteur controller done *******************");


    }

    @Test
    public void getUserById() throws Exception{
        int id = 1;
        User user = getUserById(id);
        when(userService.read(id)).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{id}",id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        System.out.println(response.getContentAsString());
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for get pilar by id in pilar controller done *******************");

    }


    @Test
    public void createUser() throws Exception{
        List<User> users =  loadDataUserFromJsonFile();
        User user = getUserById( 1 );

        when(userService.exist(1)).thenReturn(false);
                when(userService.create(user)).thenReturn(true);

        RequestBuilder requestBuilder =
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(user));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        System.out.println(response.getContentAsString());

        System.out.println("*********************************Test for create user in user controller done *******************");
    }

/*
    @Test
    public void updateUser() throws Exception{
        List<User> users =  loadDataUserFromJsonFile();
        User user = getUserById( 1 );
        when(userService.exist(user.getId())).thenReturn(true);
        when(userService.update(user)).thenReturn(true);

        RequestBuilder requestBuilder =
                put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(user));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for update user in user controller done *******************");


    }*/

    @Test
    public void deleteUser() throws Exception{
        int id = 5;
        when(userService.exist(id)).thenReturn(true);
              when(userService.delete(id)).thenReturn(true);

        RequestBuilder requestBuilder =
                delete("/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for delete user in user controller done *******************");


    }

    public List<User> loadDataUserFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile( "data-json" + File.separator + "users.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<User>>() {
        }.getType();
        List<User> users = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return users;
    }

    private User getUserById(int id) throws Exception {
        List<User> users = loadDataUserFromJsonFile();
        User user = users.stream().filter(item -> item.getId() == id).findFirst().get();

        return user;
    }



    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if(!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    private static String objectToJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
