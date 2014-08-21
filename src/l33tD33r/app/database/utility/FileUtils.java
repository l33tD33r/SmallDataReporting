package l33tD33r.app.database.utility;

import java.io.*;
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

    public static void copyFile(File existingFile, File newFile) throws IOException {
        FileInputStream existingFileInputStream = new FileInputStream(existingFile);

        FileOutputStream newFileOutputStream = new FileOutputStream(newFile);

        try {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = existingFileInputStream.read(buffer)) > 0) {
                newFileOutputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            existingFileInputStream.close();
            newFileOutputStream.close();
        }
    }
}
