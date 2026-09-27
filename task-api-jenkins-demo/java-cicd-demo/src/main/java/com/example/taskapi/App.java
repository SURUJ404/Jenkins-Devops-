package com.example.taskapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Minimal Task API used as a Jenkins CI/CD demo target.
 * Zero third-party runtime dependencies: builds and runs with just the JDK,
 * so the Docker image and pipeline stay small and fast.
 */
public class App {

    private final TaskStore store = new TaskStore();
    private final int port;

    public App(int port) {
        this.port = port;
    }

    public HttpServer start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/health", exchange -> respond(exchange, 200, "{\"status\":\"ok\"}"));

        server.createContext("/tasks", exchange -> {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            try {
                if (method.equals("GET") && path.equals("/tasks")) {
                    String body = store.list().stream()
                        .map(Task::toJson)
                        .collect(Collectors.joining(",", "[", "]"));
                    respond(exchange, 200, body);
                } else if (method.equals("POST") && path.equals("/tasks")) {
                    String title = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task task = store.add(title);
                    respond(exchange, 201, task.toJson());
                } else if (method.equals("DELETE") && path.matches("/tasks/\\d+")) {
                    int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                    boolean removed = store.delete(id);
                    respond(exchange, removed ? 204 : 404, "");
                } else if (method.equals("POST") && path.matches("/tasks/\\d+/complete")) {
                    int id = Integer.parseInt(path.split("/")[2]);
                    boolean ok = store.complete(id);
                    respond(exchange, ok ? 200 : 404, "");
                } else {
                    respond(exchange, 404, "{\"error\":\"not found\"}");
                }
            } catch (IllegalArgumentException e) {
                respond(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
            }
        });

        server.start();
        System.out.println("task-api listening on port " + port);
        return server;
    }

    private void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length == 0 ? -1 : bytes.length);
        if (bytes.length > 0) {
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        new App(port).start();
    }
}
