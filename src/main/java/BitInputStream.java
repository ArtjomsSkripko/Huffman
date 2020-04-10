import java.io.FileInputStream;
import java.io.IOException;

//Class for reading bits from file
public class BitInputStream {

    private FileInputStream input;
    private int digits;     // next set of digits (buffer)
    private int numDigits;  // how many digits from buffer have been used

    private static final int BYTE_SIZE = 8;  // digits per byte

    // pre : given file name is legal
    // post: creates a BitInputStream reading input from the file
    BitInputStream(String file) {
        try {
            input = new FileInputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        nextByte();
    }

    // post: reads next bit from input (-1 if at end of file)
    int readBit() {
        // if at eof, return -1
        if (digits == -1)
            return -1;
        int result = digits % 2;
        digits /= 2;
        numDigits++;
        if (numDigits == BYTE_SIZE)
            nextByte();
        return result;
    }

    // post: refreshes the internal buffer with the next BYTE_SIZE bits
    private void nextByte() {
        try {
            digits = input.read();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        numDigits = 0;
    }

    // post: input is closed
    private void close() {
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    // included to ensure that the stream is closed
    protected void finalize() {
        close();
    }
}