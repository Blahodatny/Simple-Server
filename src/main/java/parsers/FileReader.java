package parsers;

import java.io.IOException;

public class FileReader {
    public byte[] readFileData(String file) {
        var stream = getClass().getClassLoader().getResourceAsStream(file);
        if (stream != null)
            try {
                return stream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}