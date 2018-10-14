package com.home.file.upload;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Example how to use multipart/form encoded POST request.
 */
public class HttpClientSample {

	public static void main(String[] args) throws Exception {
		
		//File Check
		if (args.length != 1) {
			System.exit(1);
		}
		
		//Setting up the User Authentication
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("httpbin.org", 80),
                new UsernamePasswordCredentials("user", "passwd"));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
		
		try {
			//Creating the HttpPost
			HttpPost httppost = new HttpPost();

			//Creating the binary file and add the content of the file.
			FileBody bin = new FileBody(new File(args[0]));
			StringBody comment = new StringBody("A binary file of some kind",
					ContentType.MULTIPART_FORM_DATA);
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.addPart("bin", bin).addPart("comment", comment).build();
			httppost.setEntity(reqEntity);

			System.out
					.println("executing request " + httppost.getRequestLine());
			
			//Executing the statement.
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				//Reading the Output.
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println(resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

}