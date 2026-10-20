import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Task 3: encryption and decryption of a character stream. */
public class FileCipher {

    /** Encrypts all characters from the reader and writes them to the writer. */
    public static void encrypt(Reader in, Writer out, char key) throws IOException {
        CipherWriter cipherWriter = new CipherWriter(out, key);
        copy(in, cipherWriter);
        cipherWriter.flush();
    }

    /** Reads encrypted characters, decrypts them and writes to the writer. */
    public static void decrypt(Reader in, Writer out, char key) throws IOException {
        CipherReader cipherReader = new CipherReader(in, key);
        copy(cipherReader, out);
        out.flush();
    }

    public static void encryptFile(Path source, Path target, char key) throws IOException {
        try (Reader in = Files.newBufferedReader(source, StandardCharsets.UTF_8);
             Writer out = Files.newBufferedWriter(target, StandardCharsets.UTF_8)) {
            encrypt(in, out, key);
        }
    }

    public static void decryptFile(Path source, Path target, char key) throws IOException {
        try (Reader in = Files.newBufferedReader(source, StandardCharsets.UTF_8);
             Writer out = Files.newBufferedWriter(target, StandardCharsets.UTF_8)) {
            decrypt(in, out, key);
        }
    }

    private static void copy(Reader in, Writer out) throws IOException {
        char[] buffer = new char[1024];
        int count;
        while ((count = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, count);
        }
    }
}
