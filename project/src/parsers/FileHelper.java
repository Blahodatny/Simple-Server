package parsers;

public class FileHelper {
    public byte[] readFileData(java.io.File file, int length) throws java.io.IOException {
        var data = new byte[length];
        try (var fileIn = new java.io.FileInputStream(file)) {
            System.out.println(
                    "The total number of bytes read into the buffer: "
                            + fileIn.read(data)
            );
        }
        return data;
    }

    // return supported MIME Types
    public String getContentType(String fileRequested) {
        return fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")
                ? "text/html" : "text/plain";
    }
}