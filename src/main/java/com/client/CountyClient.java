package com.client;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.domain.dto.CountyDTO;
import org.springframework.stereotype.Component;

@Component
public class CountyClient {

    private final String URL = "https://roloca.coldfuse.io/judete";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<CountyDTO> getCounties() {
        HttpClient client = new HttpClient();

        // Create a method instance.
        GetMethod method = new GetMethod(URL);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                return new ArrayList<>();
            }

            // Read the response body.
            String responseBodyContent = new String(method.getResponseBody());
            List<CountyDTO> countyDTOs = objectMapper.readValue(responseBodyContent, new TypeReference<>() {});
            return countyDTOs;

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

        return null;
    }
}
