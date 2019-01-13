package response;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

public class HTTPResponse {
    PrintWriter writer;
    private BufferedOutputStream output;

    HTTPResponse() {
    }

    public HTTPResponse(java.io.OutputStream stream) {
        writer = new PrintWriter(stream);
        output = new BufferedOutputStream(stream);
    }

    public void response(String status, String content, String length) {
        new StartingLine().setLine(status);
        new Header().setHeaders(content, length);
    }
}