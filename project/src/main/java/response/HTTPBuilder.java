package response;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

class HTTPBuilder {
    // we get character output stream to client (for headers)
    private PrintWriter writer;
    // get binary output stream to client (for requested data)
    private BufferedOutputStream output;

    HTTPBuilder(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream());
            output = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setStartingLine(String status) {
        writer.println("HTTP/1.1 " + status);
    }

    void setHeaders(String content, int length) {
        writer.println("Date: " + new Date());
        writer.println("Server: Java HTTP Server");
        writer.println("Content-type: " + content + "; charset=utf-8");
        writer.println("Content-length: " + length);
        writer.println(); // blank line between headers and content, very important!
        writer.flush(); // flush character output stream buffer
    }

    void setBody(byte[] data) {
        try {
            output.write(data, 0, data.length);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        writer.close();
        output.close();
    }
}