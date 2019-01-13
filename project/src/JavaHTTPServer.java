import interfaces.IRoot;
import parsers.FileHelper;
import parsers.RequestParser;
import response.HTTPResponse;

import java.io.*;
import java.net.Socket;

public class JavaHTTPServer implements Runnable, IRoot {
    private static final String DEFAULT_FILE = "index.html";
    private static final String METHOD_NOT_SUPPORTED = "not_supported.html";
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
            var fileRequested = in.getRequestedResource();

            // we support only GET and HEAD methods, we check
            if (!method.equals("GET") && !method.equals("HEAD")) {
                if (VERBOSE)
                    System.out.println("501 Not Implemented : " + method + " method.");

                out.response(
                        "501 Not Implemented",
                        "text/html",
                        new File(WEB_ROOT, METHOD_NOT_SUPPORTED)
                        // we return the not supported file to the client
                );
            } else {
                // GET or HEAD method
                if (fileRequested.endsWith("/"))
                    fileRequested += DEFAULT_FILE;

                out.response(
                        "200 OK",
                        new FileHelper().getContentType(fileRequested),
                        new File(WEB_ROOT, fileRequested)
                );

                if (VERBOSE)
                    System.out.println("File " + fileRequested + " returned");

            }

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