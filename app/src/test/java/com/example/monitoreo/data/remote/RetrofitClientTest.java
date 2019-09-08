package com.example.monitoreo.data.remote;

import org.junit.Test;

import static org.junit.Assert.*;

public class RetrofitClientTest {

    @Test
    public void getClient() {
        assertNotNull(RetrofitClient.getClient("http://example.com"));
    }
}