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
import com.lukeshay.restapi.config.CORSConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.core.Application;

public class StreamLambdaHandler implements RequestStreamHandler {
  private static final Splitter SPLITTER = Splitter.on(',')
          .trimResults()
          .omitEmptyStrings();

  private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

//  static {
//    try {
//      long startTime = Instant.now().toEpochMilli();
//
//      LambdaContainerHandler.getContainerConfig().setInitializationTimeout(20_000);
//
//      //      handler =
//      // SpringBootLambdaContainerHandler.getAwsProxyHandler(RestApiApplication.class);
//
//      handler =
//          new SpringBootProxyHandlerBuilder()
//              .defaultProxy()
//              .asyncInit(startTime)
//              .springBootApplication(RestApiApplication.class)
//              .profiles("lambda")
//              .buildAndInitialize();
//
//      // we use the onStartup method of the handler to register our custom filter
//      handler.onStartup(
//          servletContext -> {
//            FilterRegistration.Dynamic registration =
//                servletContext.addFilter("CORSConfiguration", CORSConfiguration.class);
//            registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
//          });
//    } catch (ContainerInitializationException e) {
//      e.printStackTrace();
//      throw new RuntimeException("Could not initialize Spring Boot application", e);
//    }
//  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
      throws IOException {
    if (handler == null) {
      try {
        String listOfActiveSpringProfiles = System.getenv("SPRING_PROFILES_ACTIVE");
        if (listOfActiveSpringProfiles != null) {
          handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class,
                  splitToArray(listOfActiveSpringProfiles));
        } else {
          long startTime = Instant.now().toEpochMilli();

          LambdaContainerHandler.getContainerConfig().setInitializationTimeout(20_000);

          handler =
                  new SpringBootProxyHandlerBuilder()
                          .defaultProxy()
                          .asyncInit(startTime)
                          .springBootApplication(RestApiApplication.class)
                          .profiles("lambda")
                          .buildAndInitialize();
        }
      } catch (ContainerInitializationException e) {
        // if we fail here. We re-throw the exception to force another cold start
        e.printStackTrace();
        throw new RuntimeException("Could not initialize Spring Boot application", e);
      }
    }
    handler.proxyStream(inputStream, outputStream, context);

    outputStream.flush();
    outputStream.close();
  }

  private static String[] splitToArray(String activeProfiles) {
    return SPLITTER.splitToList(activeProfiles).toArray(new String[0]);
  }
}
