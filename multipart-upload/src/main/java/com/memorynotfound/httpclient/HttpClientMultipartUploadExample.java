package com.memorynotfound.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.IOException;

/**
 * This example demonstrates the use of {@link HttpPost} request method.
 * And sending Multipart Form requests
 */
public class HttpClientMultipartUploadExample {

    public static void main(String... args) throws IOException {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

           // File file = new File(HttpClientMultipartUploadExample.class.getResource("/Users/kpkumar/Desktop/sample.txt").getFile());
            String message = "This is a multipart post";
            byte[] bytes = "binary code".getBytes(); 

            // build multipart upload request
            HttpEntity data = MultipartEntityBuilder.create()
                   // .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("upfile", bytes, ContentType.MULTIPART_FORM_DATA, "sample.txt")
                   // .addTextBody("text", message, ContentType.DEFAULT_BINARY)
                    .build();

            // build http request and assign multipart upload data
            HttpUriRequest request = RequestBuilder
                    .post("http://10.210.242.53:19000/api/sample")
                    .setEntity(data)
                    .build();

            System.out.println("Executing request " + request.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            String responseBody = httpclient.execute(request, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        }
    }
}
