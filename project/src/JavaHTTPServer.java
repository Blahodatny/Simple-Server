import parsers.FileHelper;
import parsers.RequestParser;
import response.HTTPResponse;

import java.io.*;
import java.net.Socket;

public class JavaHTTPServer implements Runnable {
    private static final File WEB_ROOT = new File(".");
    private static final String DEFAULT_FILE = "index.html";
    private static final String FILE_NOT_FOUND = "404.html";
    private static final String METHOD_NOT_SUPPORTED = "not_supported.html";
    private static final boolean VERBOSE = true; // VERBOSE mode

    // Client Connection via Socket Class
    private Socket socket;

    JavaHTTPServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        // we manage our particular client connection
        RequestParser in = null;
        HTTPResponse out = null;
        String fileRequested = null;

        try {
            in = new RequestParser(socket.getInputStream());
            out = new HTTPResponse(socket.getOutputStream());

            var method = in.getMethod();
            fileRequested = in.getRequestedResource();

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

        } catch (FileNotFoundException fnfe) {

            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                socket.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (VERBOSE) System.out.println("Connection closed.\n");
        }


    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

//        out.println("HTTP/1.1 404 File Not Found");
//        out.println("Server: Java HTTP Server from SSaurel : 1.0");
//        out.println("Date: " + new Date());
//        out.println("Content-type: " + content);
//        out.println("Content-length: " + fileLength);
//        out.println(); // blank line between headers and content, very important !
//        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (VERBOSE) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

}