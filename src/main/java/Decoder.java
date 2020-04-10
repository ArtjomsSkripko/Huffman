import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

class Decoder {

    Decoder() {
    }

    void decode(File file) throws Exception {
        //Reading json file with huffman codes from last compression
        String huffManCodesSource = convertFileToString(
                new File(file.getAbsolutePath().replace(file.getName(), "zipped-codes.json")), Charset.defaultCharset());
        Map<String, String> huffmanCodes = new ObjectMapper().readValue(huffManCodesSource, HashMap.class);

        String unzippedFileName = file.getAbsolutePath().replace(file.getName(), "unzipped.txt");
        FileWriter fileWriter = new FileWriter(unzippedFileName);

        //Reading and converting compressed file to bits string
        BitInputStream inputStream = new BitInputStream(file.getAbsolutePath());
        StringBuilder encodedBitStringBuilder = new StringBuilder();
        while (true) {
            int currentBit = inputStream.readBit();
            if (currentBit != -1) encodedBitStringBuilder.append(currentBit);
            else break;
        }

        //Decoding huffman encoded string
        StringBuilder result = new StringBuilder();
        while (true) {
            Map.Entry<String, String> foundEntry = huffmanCodes.entrySet().stream()
                    .filter(entry -> encodedBitStringBuilder.toString().startsWith(entry.getValue()))
                    .findFirst().orElse(null);
            // Exit from loop if no match found or left only 0 bits. (when writing encoded binary string to file it is being split
            // to 8 bits and converted to byte. If total bits is not dividing by 8, extra 0 bits are added to the end of bits string
            if (foundEntry != null && !encodedBitStringBuilder.chars().allMatch(c -> c == '0')) {
                result.append(foundEntry.getKey());
                encodedBitStringBuilder.delete(0, foundEntry.getValue().length());
            } else {
                break;
            }
        }

        //Writing decoded string to file
        fileWriter.write(result.toString());
        fileWriter.close();
    }

    private String convertFileToString(File file, Charset charset) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), charset);
    }
}