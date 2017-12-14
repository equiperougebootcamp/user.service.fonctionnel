package service;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.crud.UserCRUD;
import com.bootcamp.entities.User;
import com.bootcamp.services.UserService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by Ibrahim on 12/9/17.
 */

@RunWith(PowerMockRunner.class)
@WebMvcTest(value = UserService.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(UserCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class UserServiceTest {


    @InjectMocks
    private UserService userService;

    @Test
    public void getAllUser() throws Exception {
        List<User> users = loadDataUserFromJsonFile();
        PowerMockito.mockStatic(UserCRUD.class);
        Mockito.
                when(UserCRUD.read()).thenReturn(users);
        List<User> usersr = loadDataUserFromJsonFile();
        Assert.assertEquals(users.size(), usersr.size());

    }


    @Test
    public void create() throws Exception{
        List<User> users = loadDataUserFromJsonFile();
        User user = users.get(1);

        PowerMockito.mockStatic(UserCRUD.class);
        Mockito.
                when(UserCRUD.create(user)).thenReturn(true);
    }

    @Test
    public void delete() throws Exception{
        List<User> users = loadDataUserFromJsonFile();
        User user = users.get(1);

        PowerMockito.mockStatic(UserCRUD.class);
        Mockito.
                when(UserCRUD.delete(user)).thenReturn(true);
    }

    @Test
    public void update() throws Exception{
        List<User> users = loadDataUserFromJsonFile();
        User user = users.get(1);

        PowerMockito.mockStatic(UserCRUD.class);
        Mockito.
                when(UserCRUD.update(user)).thenReturn(true);
    }


    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
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

}