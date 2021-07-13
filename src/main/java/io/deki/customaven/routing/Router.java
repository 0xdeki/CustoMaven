package io.deki.customaven.routing;

import io.deki.customaven.config.MvcConfig;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Routing class for all pages that aren't repository files
 *
 * @author Deki on 13.07.2021
 * @project CustoMaven
 **/
@Controller
public class Router implements ErrorController {

  /**
   * Placeholder page for CustoMaven
   * @return
   */
  @RequestMapping("/")
  public @ResponseBody
  String index() {
    return "Running CustoMaven";
  }

  /**
   * Placeholder page for repositories
   * @return
   */
  @RequestMapping("/repos")
  public @ResponseBody
  String reposIndex() {
    return "CustoMaven repository root";
  }

  /**
   * Simple informational index page for individual repositories
   * @param repository
   * @return
   */
  @RequestMapping("/repos/{repository}")
  public @ResponseBody
  String repo(@PathVariable String repository) {
    StringBuilder page = new StringBuilder();
    page.append("<h1>Repo index</h1>");
    //enumerate the repository, count jars and all files (including jars)
    File repo = new File(MvcConfig.REPO_DIRECTORY + File.separator + repository);
    if (!repo.exists() || !repo.isDirectory()) {
      page.append(String.format("<p>%s repository does not exist</p>", repository));
      return page.toString();
    }
    AtomicInteger jars = new AtomicInteger();
    AtomicInteger files = new AtomicInteger();
    traverseFiles(repo, file -> {
      if (file.getName().endsWith(".jar")) {
        jars.incrementAndGet();
      }
      files.incrementAndGet();
    });
    //present the data
    page.append(String.format("<p>%s repository currently serving %s jars, %s files</p>",
        repository, jars.get(), files.get()));
    return page.toString();
  }

  /**
   * Simple error page
   * @param httpRequest
   * @return
   */
  @RequestMapping(value = "error")
  public @ResponseBody
  String renderErrorPage(HttpServletRequest httpRequest) {

    //fetch the error code
    int httpErrorCode = (int) httpRequest.getAttribute("javax.servlet.error.status_code");
    String errorMsg = "" + httpErrorCode;

    //some placeholder messages for common error codes
    switch (httpErrorCode) {
      case 400:
        errorMsg = "400 Bad Request";
        break;
      case 401:
        errorMsg = "401 Unauthorized";
        break;
      case 404:
        errorMsg = "404 not found";
        break;
      case 500:
        errorMsg = "500 Internal Server Error";
        break;
      default:
        System.out.println("Unhandled error code " + httpErrorCode);
        break;
    }

    return errorMsg;
  }

  /**
   * Helper method to recursively traverse a directory
   * @param file
   * @param consumer
   */
  private void traverseFiles(File file, Consumer<File> consumer) {
    if (file.isDirectory()) {
      for (File child : file.listFiles()) {
        traverseFiles(child, consumer);
      }
    } else {
      consumer.accept(file);
    }
  }

}
