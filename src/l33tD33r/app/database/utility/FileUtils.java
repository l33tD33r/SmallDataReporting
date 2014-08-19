package l33tD33r.app.database.utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


/**
 * Created by Simon on 2/14/14.
 */
public class FileUtils {
    public static void writeBytes(FileOutputStream fos, byte[] bytes) throws IOException {
        try {
            byte[] buffer;
            int bufferSize = 1024;
            int startIndex = 0, endIndex;
            while (startIndex < bytes.length) {
                endIndex = Math.min(startIndex + bufferSize, bytes.length);
                buffer = Arrays.copyOfRange(bytes, startIndex, endIndex);
                fos.write(buffer);
                startIndex = endIndex;
            }
            fos.flush();
        } finally {
            fos.close();
        }
    }
}
