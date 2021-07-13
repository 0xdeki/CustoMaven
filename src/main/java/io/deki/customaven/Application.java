package io.deki.customaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Boot class for CustoMaven.
 * Note that the server runs on port 80, this can be changed in the application.properties
 * configuration file.
 *
 * @author Deki on 13.07.2021
 * @project CustoMaven
 **/
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

}
