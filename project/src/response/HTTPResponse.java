package response;

import java.io.*;

public class HTTPResponse {
    // we get character output stream to client (for headers)
    private PrintWriter writer;
    // get binary output stream to client (for requested data)
    private BufferedOutputStream output;

    public HTTPResponse(java.net.Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream());
            output = new BufferedOutputStream(socket.getOutputStream());
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

    public void response(String status, String content, java.io.File file) throws IOException {
        setStartingLine(status);
        var length = (int) file.length();
        setHeaders(content, length);
        setBody(new parsers.FileHelper().readFileData(file, length), length);
    }

    public void close() throws IOException {
        writer.close();
        output.close();
    }
}