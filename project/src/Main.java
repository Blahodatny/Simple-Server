import java.io.IOException;

public class Main {
    // port to listen connection
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        var socket = new java.net.ServerSocket(PORT);
        System.out.println("Server started.\nListening for connections on port: " + PORT + " ...\n");

        // we listen until user halts server execution
        while (true) {
            System.out.println("Connection opened. (" + new java.util.Date() + ")");
            // create dedicated thread to manage the client connection
            new Thread(new HTTPServer(socket.accept())).start();
        }
    }
}