package parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class RequestParser {
    private StringTokenizer tokenizer;
    // we read characters from the client via input stream on the socket
    private BufferedReader reader;

    public RequestParser(Socket socket) {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            // we parse the request with a string tokenizer
            // get first line of the request from the client
            tokenizer = new StringTokenizer(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // we get the HTTP method of the client
    public String getMethod() {
        return tokenizer.nextToken().toUpperCase();
    }

    // we get file requested
    public String getRequestedResource() {
        return tokenizer.nextToken().toLowerCase();
    }

    public void close() throws IOException {
        reader.close();
    }
}