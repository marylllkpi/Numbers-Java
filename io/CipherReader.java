import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class CipherReader extends FilterReader {
    private final char key;

    public CipherReader(Reader in, char key) {
        super(in);
        this.key = key;
    }

    @Override
    public int read() throws IOException {
        int c = super.read();
        if (c == -1) {
            return -1;
        }
        return (char) (c - key);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int count = super.read(cbuf, off, len);
        for (int i = off; i < off + count; i++) { 
            cbuf[i] = (char) (cbuf[i] - key);
        }
        return count;
    }
}
