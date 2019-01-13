import parsers.FileHelper;
import parsers.RequestParser;
import response.HTTPResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

public class HTTPServer implements Runnable {
    private static final String PATH = System.getProperty("user.dir");
    private static final String FILE_NOT_FOUND = PATH + "/src/resources/404.html";
    private static final String DEFAULT_FILE = PATH + "/src/resources/index.html";
    private static final String METHOD_NOT_SUPPORTED = PATH + "/src/resources/501.html";

    // Client Connection via Socket Class
    private Socket socket;

    HTTPServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        var in = new RequestParser(socket);
        var out = new HTTPResponse(socket);

        try {
            var method = in.getMethod();

            switch (method) {
                case "GET":
                case "HEAD":
                    var fileRequested = in.getRequestedResource();
                    if (fileRequested.endsWith("/")) // GET or HEAD method
                        fileRequested += DEFAULT_FILE;

                    out.response(
                            "200 OK",
                            new FileHelper().getContentType(fileRequested),
                            new File(fileRequested)
                    );
                    break;
                default:
                    out.response(
                            "501 Not Implemented",
                            "text/html",
                            new File(METHOD_NOT_SUPPORTED)
                    );
            }
        } catch (IOException exception) {
            if (exception.getClass() == FileNotFoundException.class)
                try {
                    out.response(
                            "404 File Not Found",
                            "text/html",
                            new File(FILE_NOT_FOUND)
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } finally {
            try {
                in.close();
                out.close();
                socket.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }
            System.out.println("Connection closed.\n");
        }
    }
}