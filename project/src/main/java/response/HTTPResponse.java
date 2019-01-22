package response;

import parsers.FileReader;

import java.net.Socket;

public class HTTPResponse extends HTTPBuilder {
    public HTTPResponse(Socket socket) {
        super(socket);
    }

    private void set(String status, byte[] data) {
        setStartingLine(status);
        setHeaders("text/html", data.length);
        setBody(data);
    }

    public void response(String method, String path) {
        var reader = new FileReader();
        byte[] data;
        switch (method) {
            case "GET":
            case "HEAD":
                path = path.equals("/") ? "index.html" : path.substring(1);
                data = reader.readFileData(path);
                if (data == null)
                    set("404 Not Found", reader.readFileData("404.html"));
                else {
                    setStartingLine("200 OK");
                    setHeaders("text/" + (path.endsWith("html") ? "html" : "plain"), data.length);
                    if (method.equals("GET"))
                        setBody(data);
                }
                break;
            default:
                set("501 Not Implemented", reader.readFileData("501.html"));
        }
    }
}