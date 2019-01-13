import parsers.FileHelper;
import parsers.RequestParser;
import response.HTTPResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

public class JavaHTTPServer implements Runnable {
    private static final String PATH = System.getProperty("user.dir");
    private static final String FILE_NOT_FOUND = "404.html";
    private static final String DEFAULT_FILE = PATH + "/src/resources/index.html";
    private static final String METHOD_NOT_SUPPORTED = PATH + "src/resources/not_supported.html";
    private static final boolean VERBOSE = true; // VERBOSE mode

    // Client Connection via Socket Class
    private Socket socket;

    JavaHTTPServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        var in = new RequestParser(socket);
        var out = new HTTPResponse(socket);

        try {
            var method = in.getMethod();

            // we support only GET and HEAD methods, we check
            if (!method.equals("GET") && !method.equals("HEAD")) {
                if (VERBOSE)
                    System.out.println("501 Not Implemented : " + method + " method.");

                out.response(
                        "501 Not Implemented",
                        "text/html",
                        new File(METHOD_NOT_SUPPORTED)
                        // we return the not supported file to the client
                );
            } else {
                var fileRequested = in.getRequestedResource();
                if (fileRequested.endsWith("/")) // GET or HEAD method
                    fileRequested += DEFAULT_FILE;

                out.response(
                        "200 OK",
                        new FileHelper().getContentType(fileRequested),
                        new File(fileRequested)
                );

                if (VERBOSE)
                    System.out.println("File " + fileRequested + " returned");
            }
        } catch (FileNotFoundException exception) {
            try {
                out.response(
                        "404 File Not Found",
                        "text/html",
                        new File(FILE_NOT_FOUND)
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (VERBOSE)
                System.out.println("Connection closed.\n");
        }
    }
}