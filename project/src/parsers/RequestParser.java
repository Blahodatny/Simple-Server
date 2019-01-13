package parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class RequestParser {
    private StringTokenizer tokenizer;
    private BufferedReader reader;

    public RequestParser(java.io.InputStream input) {
        try {
            reader = new BufferedReader(
                    new java.io.InputStreamReader(input)
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