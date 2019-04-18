import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;

class Main {
    // port to listen connection
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        var socket = new ServerSocket(PORT);
        System.out.println(
                "Server started.\nListening for connections on port: " + PORT +
                        " ...\n");

        // we listen until user halts server execution
        while (true) {
            System.out.println("Connection opened. (" + new Date() + ")");
            // create dedicated thread to manage the client connection
            new Thread(new HTTPServer(socket.accept())).start();
        }
    }
}