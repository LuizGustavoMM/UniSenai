package aulabaseweb;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class WebServerSocket {

    public static void main(String[] args) throws IOException {
        List<Produto> produtos = new ArrayList<>();
        Gson gson = new Gson();

        produtos.add(new Produto(1, "Teclado", 50.0f));
        produtos.add(new Produto(2, "Mouse", 150.0f));
        produtos.add(new Produto(3, "Monitor", 1500.0f));
        produtos.add(new Produto(4, "Processador", 950.0f));
        produtos.add(new Produto(5, "Memoria", 500.0f));

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server Started!");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Client Connected!");

                    InputStream inputStream = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                    String line;
                    StringBuilder requestBuilder = new StringBuilder();
                    int contentLength = 0;

                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        requestBuilder.append(line).append("\n");
                        if (line.toLowerCase().startsWith("content-length")) {
                            String[] split = line.split(":", 2);
                            if (split.length == 2 && StringUtils.isNotBlank(split[1])) {
                                contentLength = Integer.parseInt(split[1].trim());
                            }
                        }
                    }

                    String requestBody = "";
                    if (contentLength > 0) {
                        char[] bodyChars = new char[contentLength];
                        int totalRead = 0;
                        while (totalRead < contentLength) {
                            int read = reader.read(bodyChars, totalRead, contentLength - totalRead);
                            if (read < 0) {
                                break;
                            }
                            totalRead += read;
                        }
                        requestBody = new String(bodyChars, 0, totalRead);
                    }

                    Produto produtoRecebido = null;
                    if (StringUtils.isNotBlank(requestBody)) {
                        produtoRecebido = gson.fromJson(requestBody, Produto.class);
                        if (produtoRecebido != null) {
                            produtos.add(produtoRecebido);
                        }
                    }

                    System.out.println("Requisicao Recebida:");
                    System.out.println(requestBuilder);
                    if (StringUtils.isNotBlank(requestBody)) {
                        System.out.println("Body Recebido:");
                        System.out.println(requestBody);
                    }

                    OutputStream outputStream = socket.getOutputStream();
                    PrintStream printStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8);

                    Map<String, Object> responseMap = new LinkedHashMap<>();
                    responseMap.put("status", "OK");
                    responseMap.put("message", "Servidor funcionando!");
                    responseMap.put("produtos", produtos);
                    if (produtoRecebido != null) {
                        responseMap.put("produtoRecebido", produtoRecebido);
                    }

                    String bodyResponse = gson.toJson(responseMap);
                    byte[] bodyBytes = bodyResponse.getBytes(StandardCharsets.UTF_8);

                    printStream.println("HTTP/1.1 200 OK");
                    printStream.println("Content-Type: application/json");
                    printStream.println("Content-Length: " + bodyBytes.length);
                    printStream.println();
                    printStream.print(bodyResponse);
                }
            }
        }
    }
}
