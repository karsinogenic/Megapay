package com.mega.cicilan.cicilan.Services;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class HTTPRequest {
    public String postRequest(String uri, String body) throws Exception {
        // System.setProperty("http.nonProxyHosts",
        // "10.14.21.124:8097|10.14.21.65:8084");

        URL url = new URL(uri);

        // Create HttpURLConnection object
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        // Proxy proxy = new Proxy(Proxy.Type.HTTP, new
        // InetSocketAddress("10.14.21.124", 8080)); // Proxy server IP and
        // port

        // Set request method to POST
        conn.setRequestMethod("POST");

        // Set headers
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        // conn.setRequestProperty("Proxy-Connection", null);

        // Set request body
        String requestBody = body;
        byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
        conn.setDoOutput(true);
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(requestBodyBytes);
        outputStream.flush();
        outputStream.close();

        // Get response code
        int responseCode = conn.getResponseCode();

        // Get response body
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder responseBody = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseBody.append(inputLine);
        }
        in.close();

        // Print response
        // System.out.println("Response Code: " + responseCode);
        return responseBody.toString();
    }

    public String postRequestWithProxy(String uri, String body, String host, Integer port) throws Exception {
        URL url = new URL(uri);

        String proxyHost = host;
        int proxyPort = port;

        // Create a Proxy object with the proxy server details
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

        // Open a connection using the proxy
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);

        // Set request method to POST
        conn.setRequestMethod("POST");

        // Set headers
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        // Set request body
        String requestBody = body;
        byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
        conn.setDoOutput(true);
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(requestBodyBytes);
        outputStream.flush();
        outputStream.close();

        // Get response code
        int responseCode = conn.getResponseCode();

        // Get response body
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder responseBody = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseBody.append(inputLine);
        }
        in.close();

        // Print response
        // System.out.println("Response Code: " + responseCode);
        return responseBody.toString();
    }

    public String postRequestBasicAuth(String uri, String body, String username, String password) throws Exception {
        URL url = new URL(uri);

        // Create HttpURLConnection object
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set request method to POST
        conn.setRequestMethod("POST");

        // Set headers
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        if (username != null && password != null) {
            String authString = username + ":" + password;
            String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuthString);
        }

        // Set request body
        String requestBody = body;
        byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
        conn.setDoOutput(true);
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(requestBodyBytes);
        outputStream.flush();
        outputStream.close();

        // Get response code
        int responseCode = conn.getResponseCode();

        // Get response body
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder responseBody = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseBody.append(inputLine);
        }
        in.close();

        // Print response
        // System.out.println("Response Code: " + responseCode);
        return responseBody.toString();
    }

    public String getRequest(String uri, String body, String username, String password) throws Exception {
        URL url = new URL(uri);

        // Create HttpURLConnection object
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set request method to POST
        conn.setRequestMethod("GET");

        // Set headers
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        if (username != null && password != null) {
            String authString = username + ":" + password;
            String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuthString);
        }

        // Set request body
        String requestBody = body;
        byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
        conn.setDoOutput(true);
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(requestBodyBytes);
        outputStream.flush();
        outputStream.close();

        // Get response code
        int responseCode = conn.getResponseCode();

        // Get response body
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder responseBody = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseBody.append(inputLine);
        }
        in.close();

        // Print response
        // System.out.println("Response Code: " + responseCode);
        return responseBody.toString();
    }

    public String oldPostRequest(String uri, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                // .uri(URI.create("http://127.0.0.1:8081/api/toAscend"))
                .POST(BodyPublishers.ofString(body))
                .header("Content-type", "application/json")
                // .header("Authorization", getBasicAuthenticationHeader("7777777", "7777777"))
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<?> httpResponse;
        String response = "";
        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body().toString();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(httpResponse.body().toString());
        return response;
    }

    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    public Map<String, Object> getRequestParam(String uri) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                // .uri(URI.create("http://127.0.0.1:8081/api/toAscend"))
                .GET()
                .header("Content-type", "application/json")
                .header("Authorization", getBasicAuthenticationHeader("7777777", "7777777"))
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> httpResponse;
        String response = "";
        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body().toString();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> hasilMap = new HashMap<>();
        try {
            hasilMap = objectMapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(httpResponse.body().toString());
        return hasilMap;
    }

}
