package response;

import interfaces.IRoot;
import parsers.FileHelper;

import java.io.*;

public class HTTPResponse implements IRoot {
    private static final String FILE_NOT_FOUND = "404.html";
    // we get character output stream to client (for headers)
    private PrintWriter writer;
    // get binary output stream to client (for requested data)
    private BufferedOutputStream output;

    public HTTPResponse(java.net.Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream());
            output = new BufferedOutputStream(socket.getOutputStream());
        } catch (FileNotFoundException e) {
            response(
                    "404 File Not Found",
                    "text/html",
                    new File(new File("."), FILE_NOT_FOUND)
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStartingLine(String status) {
        writer.println("HTTP/1.1 " + status);
    }

    private void setHeaders(String content, int length) {
        writer.println("Server: Java HTTP Server");
        writer.println("Date: " + new java.util.Date());
        writer.println("Content-type: " + content);
        writer.println("Content-length: " + length);
        writer.println(); // blank line between headers and content, very important!
        writer.flush(); // flush character output stream buffer
    }

    private void setBody(byte[] data, int length) {
        try {
            output.write(data, 0, length);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void response(String status, String content, java.io.File file) {
        setStartingLine(status);
        var length = (int) file.length();
        setHeaders(content, length);
        setBody(new FileHelper().readFileData(file, length), length);
    }

    public void close() throws IOException {
        writer.close();
        output.close();
    }
}