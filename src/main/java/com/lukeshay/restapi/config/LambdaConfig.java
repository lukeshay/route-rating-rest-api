package com.lukeshay.restapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableAspectJAutoProxy
@EnableWebMvc
@Profile("lambda")
public class LambdaConfig {
  // silence console logging
   @Value("${logging.level.root:OFF}")
   String message = "";

  /**
   * Create required HandlerMapping, to avoid several default HandlerMapping instances being created
   */
  @Bean
  public HandlerMapping handlerMapping() {
    return new RequestMappingHandlerMapping();
  }

  /**
   * Create required HandlerAdapter, to avoid several default HandlerAdapter instances being created
   */
  @Bean
  public HandlerAdapter handlerAdapter() {
    return new RequestMappingHandlerAdapter();
  }

  /**
   * optimization - avoids creating default exception resolvers; not required as the serverless
   * container handles all exceptions
   *
   * <p>By default, an ExceptionHandlerExceptionResolver is created which creates many dependent
   * object, including an expensive ObjectMapper instance.
   */
  @Bean
  public HandlerExceptionResolver handlerExceptionResolver() {
    return (request, response, handler, ex) -> null;
  }
}
