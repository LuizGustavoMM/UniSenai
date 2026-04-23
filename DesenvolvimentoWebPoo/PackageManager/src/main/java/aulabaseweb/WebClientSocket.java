package aulabaseweb;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WebClientSocket {

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        Produto novoProduto = new Produto(6, "Webcam", 230.0f);
        String bodyRequest = gson.toJson(novoProduto);
        byte[] bodyBytes = bodyRequest.getBytes(StandardCharsets.UTF_8);

        try (Socket socket = new Socket("127.0.0.1", 8080)) {
            OutputStream outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8);

            printStream.println("POST /produtos HTTP/1.1");
            printStream.println("Host: localhost");
            printStream.println("Content-Type: application/json");
            printStream.println("Content-Length: " + bodyBytes.length);
            printStream.println();
            printStream.print(bodyRequest);

            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String line;
            StringBuilder responseHeaders = new StringBuilder();
            int responseLength = 0;

            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                responseHeaders.append(line).append("\n");
                if (line.toLowerCase().startsWith("content-length")) {
                    String[] split = line.split(":", 2);
                    responseLength = Integer.parseInt(split[1].trim());
                }
            }

            String responseBody = "";
            if (responseLength > 0) {
                char[] bodyChars = new char[responseLength];
                int totalRead = 0;
                while (totalRead < responseLength) {
                    int read = bufferedReader.read(bodyChars, totalRead, responseLength - totalRead);
                    if (read < 0) {
                        break;
                    }
                    totalRead += read;
                }
                responseBody = new String(bodyChars, 0, totalRead);
            }

            Map<String, Object> responseMap = gson.fromJson(responseBody, Map.class);

            System.out.println("Headers de resposta:\n" + responseHeaders);
            System.out.println("JSON bruto recebido: " + responseBody);
            System.out.println("Objeto desserializado (Map): " + responseMap);
        }
    }
}
