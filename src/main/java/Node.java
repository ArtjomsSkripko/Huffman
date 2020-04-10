class Node {

    private char character;
    private int frequency;
    private Node left = null, right = null;

    Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    Node(char character, int frequency, Node left, Node right) {
        this.character = character;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    char getCharacter() {
        return character;
    }

    int getFrequency() {
        return frequency;
    }

    Node getLeft() {
        return left;
    }

    Node getRight() {
        return right;
    }
}