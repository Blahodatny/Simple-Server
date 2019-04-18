import java.net.Socket;
import parsers.RequestParser;
import response.HTTPResponse;

class HTTPServer implements Runnable {
    // Client Connection via Socket Class
    private Socket socket;

    HTTPServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        var in = new RequestParser(socket);
        var out = new HTTPResponse(socket);

        try {
            out.response(in.getMethod(), in.getRequestedResource());
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