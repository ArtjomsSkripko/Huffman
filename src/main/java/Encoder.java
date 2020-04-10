import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import com.fasterxml.jackson.databind.ObjectMapper;

class Encoder {

    private static void createHuffmanCode(Node currentNode, String currentString, Map<Character, String> huffCode) {
        if (currentNode == null) return; // if there are no nodes left to look at, then stop recursion
        if (currentNode.getLeft() == null && currentNode.getRight() == null) { // found a leaf node
            huffCode.put(currentNode.getCharacter(), currentString);
        }
        // when using recursion, every next call adds either 0 or 1 to the code, based on the branch it looks at
        createHuffmanCode(currentNode.getLeft(), currentString + "0", huffCode);
        createHuffmanCode(currentNode.getRight(), currentString + "1", huffCode);
    }

    /**
     * Creates map of symbols and their huffman codes
     *
     * @param file inputFile
     */
    void encode(File file) throws IOException {
        String fileContentAsString = convertFileToString(file, Charset.defaultCharset());

        // frequency of each symbol in String
        Map<Character, Integer> freq = new HashMap<>(); // map for holding symbol and its frequency
        for (int i = 0; i < fileContentAsString.length(); i++) {
            char character = fileContentAsString.charAt(i);
            if (!freq.containsKey(character)) { // if symbol is not present, then add it to the map
                freq.put(character, 0);
            }
            freq.put(character, freq.get(character) + 1); // add 1 to value if symbol exists in string
        }

        // priority queue holds nodes in natural order using comparator
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getFrequency));

        // add newly created node to priority queue
        freq.forEach((character, frequency) -> queue.add(new Node(character, frequency)));

        while (queue.size() != 1) {
            // remove first 2 nodes from priority queue and and create left and right nodes
            Node left = queue.poll();
            Node right = queue.poll();
            queue.add(new Node('\0', left.getFrequency() + right.getFrequency(), left, right)); // create and add parent node of left and right to the priority queue
        }
        Node root = queue.peek(); // create a root node for the whole tree

        Map<Character, String> huffmanCode = new HashMap<>(); // map to hold
        createHuffmanCode(root, "", huffmanCode); // starts the recursive method to get huffman codes for each symbol in text

        //Write huffman code data (e.g. character vs huffman code in json file)
        String encodingDataFileName = file.getAbsolutePath().replace(file.getName(), "zipped-codes.json");
        new ObjectMapper().writeValue(new File(encodingDataFileName), huffmanCode);

        //Write encoded string
        String zippedFileName = file.getAbsolutePath().replace(file.getName(), "zipped.huff");
        FileWriter fileWriter = new FileWriter(zippedFileName);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < fileContentAsString.length(); i++) {
            char key = fileContentAsString.charAt(i);
            String str = huffmanCode.get(key);
            result.append(str);
        }
        BitOutputStream bitOutputStream = new BitOutputStream(zippedFileName);

        String encodedBinaryString = result.toString();
        for (int i = 0; i < encodedBinaryString.length(); i++) {
            String s1 = String.valueOf(encodedBinaryString.charAt(i));
            bitOutputStream.writeBit(Integer.parseInt(s1));
        }
        bitOutputStream.close();

        fileWriter.close();
    }

    private String convertFileToString(File file, Charset charset) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), charset);
    }
}
