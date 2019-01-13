package parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class RequestParser {
    private StringTokenizer tokenizer;
    // we read characters from the client via input stream on the socket
    private BufferedReader reader;

    public RequestParser(java.net.Socket socket) {
        try {
            reader = new BufferedReader(
                    new java.io.InputStreamReader(
                            socket.getInputStream()
                    )
            );
            // we parse the request with a string tokenizer
            tokenizer = new StringTokenizer(
                    reader.readLine()  // get first line of the request from the client
            );
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