package io.deki.customaven.config;

import io.deki.customaven.util.OS;
import java.io.File;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class to configure static file delivery for spring
 *
 * @author Deki on 13.07.2021
 * @project CustoMaven
 **/
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

  public static final String USER_HOME = System.getProperty("user.home") + File.separator;

  //default directory for repositories: /user/CustoMaven/repositories
  public static final String REPO_DIRECTORY = USER_HOME + "CustoMaven"
      + File.separator + "repositories";

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //make sure the repositories directory exists
    File repo = new File(REPO_DIRECTORY);
    if (!repo.exists() || !repo.isDirectory()) {
      repo.mkdirs();
    }
    //configure the static files (repository files) disk location
    registry
        .addResourceHandler("/repos/**")
        .addResourceLocations(getResourceLocation());
  }

  /**
   * Method to determine the disk location of the repositories and format it properly
   * so spring can use it
   * @return
   */
  private String getResourceLocation() {
    //make sure all slashes are forward slashes, just in case
    String location = REPO_DIRECTORY.replaceAll("\\\\", "/") + "/";
    //windows file paths are prepended by three forward slashes
    if (OS.getOs().equals(OS.WINDOWS)) {
      return "file:///" + location;
    }
    //linux file paths are prepended by one forward slash, which is already provided when
    //grabbing user home
    return "file:" + location;
  }

}
