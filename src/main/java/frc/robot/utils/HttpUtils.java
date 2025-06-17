package frc.robot.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;

/**
 * Utility class for making HTTP requests in FRC robot code.
 * Uses async requests to avoid blocking the robot's main thread.
 */
public class HttpUtils {

  private static final HttpClient client = HttpClient
    .newBuilder()
    .connectTimeout(Duration.ofSeconds(2))
    .build();

  /**
   * Make an async GET request
   * @param url The URL to request
   * @param onSuccess Callback for successful response (response body as String)
   * @param onError Callback for errors (exception)
   */
  public static void getAsync(
    String url,
    Consumer<String> onSuccess,
    Consumer<Exception> onError
  ) {
    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofSeconds(5))
        .GET()
        .build();

      client
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenAccept(response -> {
          if (response.statusCode() >= 200 && response.statusCode() < 300) {
            onSuccess.accept(response.body());
          } else {
            onError.accept(
              new RuntimeException("HTTP " + response.statusCode())
            );
          }
        })
        .exceptionally(throwable -> {
          onError.accept(new Exception(throwable));
          return null;
        });
    } catch (Exception e) {
      onError.accept(e);
    }
  }

  /**
   * Make an async POST request with JSON body
   * @param url The URL to request
   * @param jsonBody The JSON body to send
   * @param onSuccess Callback for successful response (response body as String)
   * @param onError Callback for errors (exception)
   */
  public static void postJsonAsync(
    String url,
    String jsonBody,
    Consumer<String> onSuccess,
    Consumer<Exception> onError
  ) {
    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofSeconds(5))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

      client
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenAccept(response -> {
          if (response.statusCode() >= 200 && response.statusCode() < 300) {
            onSuccess.accept(response.body());
          } else {
            onError.accept(
              new RuntimeException(
                "HTTP " + response.statusCode() + ": " + response.body()
              )
            );
          }
        })
        .exceptionally(throwable -> {
          onError.accept(new Exception(throwable));
          return null;
        });
    } catch (Exception e) {
      onError.accept(e);
    }
  }

  /**
   * Make a synchronous GET request (use sparingly in robot code!)
   * @param url The URL to request
   * @return The response body as String
   * @throws Exception if the request fails
   */
  public static String getSync(String url) throws Exception {
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(url))
      .timeout(Duration.ofSeconds(2))
      .GET()
      .build();

    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    if (response.statusCode() >= 200 && response.statusCode() < 300) {
      return response.body();
    } else {
      throw new RuntimeException(
        "HTTP " + response.statusCode() + ": " + response.body()
      );
    }
  }

  /**
   * Make a synchronous POST request with JSON body (use sparingly in robot code!)
   * @param url The URL to request
   * @param jsonBody The JSON body to send
   * @return The response body as String
   * @throws Exception if the request fails
   */
  public static String postJsonSync(String url, String jsonBody)
    throws Exception {
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(url))
      .timeout(Duration.ofSeconds(2))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
      .build();

    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    if (response.statusCode() >= 200 && response.statusCode() < 300) {
      return response.body();
    } else {
      throw new RuntimeException(
        "HTTP " + response.statusCode() + ": " + response.body()
      );
    }
  }
}
