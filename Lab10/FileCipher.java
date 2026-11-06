import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileCipher {

    public static void encrypt(Reader in, Writer out, char key) throws IOException {
        CipherWriter cipherWriter = new CipherWriter(out, key);
        copy(in, cipherWriter);
        cipherWriter.flush();
    }

    public static void decrypt(Reader in, Writer out, char key) throws IOException {
        CipherReader cipherReader = new CipherReader(in, key);
        copy(cipherReader, out);
        out.flush();
    }

    public static void encryptFile(Path source, Path target, char key) throws IOException {
        AppLogger.LOG.info("Encryption started: " + source + " -> " + target);
        try (Reader in = Files.newBufferedReader(source, StandardCharsets.UTF_8);
            Writer out = Files.newBufferedWriter(target, StandardCharsets.UTF_8)) {
            encrypt(in, out, key);
        }
        AppLogger.LOG.info("Encryption finished");
    }

    public static void decryptFile(Path source, Path target, char key) throws IOException {
        AppLogger.LOG.info("Decryption started: " + source + " -> " + target);
        try (Reader in = Files.newBufferedReader(source, StandardCharsets.UTF_8);
        Writer out = Files.newBufferedWriter(target, StandardCharsets.UTF_8)) {
            decrypt(in, out, key);
        }
        AppLogger.LOG.info("Decryption finished");
    }

    private static void copy(Reader in, Writer out) throws IOException {
        char[] buffer = new char[1024];
        int count;
        while ((count = in.read(buffer, 0, buffer.length)) != -1) {
            AppLogger.LOG.fine("Block of " + count + " characters processed");
            out.write(buffer, 0, count);
        }
    }
}
