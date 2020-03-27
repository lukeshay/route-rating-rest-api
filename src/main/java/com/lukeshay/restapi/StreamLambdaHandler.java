package com.lukeshay.restapi;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.common.base.Splitter;
import com.lukeshay.restapi.wall.WallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class StreamLambdaHandler implements RequestStreamHandler {
	private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	private static Logger LOG = LoggerFactory.getLogger(StreamLambdaHandler.class.getName());

	private static String[] getProfiles(String activeProfiles) {
		Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();

		Set<String> profiles =
			activeProfiles == null
				? new HashSet<>()
				: new HashSet<>(splitter.splitToList(activeProfiles));
		profiles.add("lambda");
		return profiles.toArray(new String[0]);
	}

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
		throws IOException {
		if (handler == null) {
			try {
				String listOfActiveSpringProfiles = System.getenv("SPRING_PROFILES_ACTIVE");
				long startTime = Instant.now().toEpochMilli();

				handler =
					new SpringBootProxyHandlerBuilder()
						.defaultProxy()
						.asyncInit(startTime)
						.profiles(getProfiles(listOfActiveSpringProfiles))
						.springBootApplication(RestApiApplication.class)
						.buildAndInitialize();
			} catch (ContainerInitializationException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not initialize Spring Boot application", e);
			}
		}

		LOG.info("Context: " + context);

		handler.proxyStream(inputStream, outputStream, context);
	}
}
