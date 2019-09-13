package com.example.monitoreo.data.remote;

import com.example.monitoreo.data.model.User;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;

public class APIUtilsTest {

    @Test
    public void getAPIService() throws IOException {
        APIService mAPIService = APIUtils.getAPIService();

        assertNotNull(mAPIService);

        User user = new User("admin", "admin", -1);

        Call<User> callLogin = mAPIService.login(user);
        Response<User> responseLogin = callLogin.execute();
        assertNotNull(responseLogin);

        RetrofitClient.token = responseLogin.body().getId();

        Call<User> callGetUser = mAPIService.check(2);
        Response<User> responseUser = callGetUser.execute();
        assertEquals("admin", responseUser.body().getUsername());
        assertEquals("2", responseUser.body().getId());
    }
}