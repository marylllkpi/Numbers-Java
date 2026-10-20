import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/** Writer that encrypts every character: new code = code + key code. */
public class CipherWriter extends FilterWriter {
    private final char key;

    public CipherWriter(Writer out, char key) {
        super(out);
        this.key = key;
    }

    /** Encrypts one character. */
    private char encryptChar(int c) throws IOException {
        char result = (char) (c + key);
        if (Character.isSurrogate(result)) {
            throw new IOException("Key is too large for this text: result is not a valid character");
        }
        return result;
    }

    @Override
    public void write(int c) throws IOException {
        super.write(encryptChar(c));
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(cbuf[i]);
        }
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(str.charAt(i));
        }
    }
}
