package response;

class Header extends HTTPResponse {
    void setHeaders(String content, String length) {
        writer.println("Server: Java HTTP Server");
        writer.println("Date: " + new java.util.Date());
        writer.println("Content-type: " + content);
        writer.println("Content-length: " + length);
        writer.println(); // blank line between headers and content, very important!
        writer.flush(); // flush character output stream buffer
    }
}