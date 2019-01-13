package response;

class StartingLine extends HTTPResponse {
    void setLine(String status) {
        writer.println("HTTP/1.1 " + status);
    }
}