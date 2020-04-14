import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Scanner;

public class UserInterface {

    public static void displayUI() throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Input pathname to a file: ");
        String pathname = in.nextLine();
        File f = new File(pathname);

        while (!f.exists()) {
            System.out.println("Error: This file does not exist");
            System.out.println("Try again:");
            pathname = in.nextLine();
            f = new File(pathname);
        }

        while(isFileEmpty(f)) {
            System.out.println("Error: Selected file is empty");
            System.out.println("Select different file:");
            pathname = in.nextLine();
            f = new File(pathname);
        }

        boolean isCorrect = false;
        while (!isCorrect) {
            System.out.println("Select action with file: ");
            System.out.println("1    Encode File");
            System.out.println("2    Decode File");
            int answer = in.nextInt();
            switch (answer) {
                case 1:
                    System.out.println("Encoding.....");
                    isCorrect = true;

                    File file = new File(pathname);
                    Encoder encoder = new Encoder();
                    encoder.encode(file);
                    System.out.print("Your compressed file 'zipped.huff' is ready, located in same folder");
                    break;
                case 2:
                    System.out.println("Decoding.....");
                    isCorrect = true;

                    File file2 = new File(pathname);
                    Decoder decoder = new Decoder();
                    decoder.decode(file2);
                    System.out.print("Your decompressed file unzipped.txt is ready, located in same folder");

                    break;
                default:
                    System.out.println("Invalid command, try again...");

                    in.close();
            }
        }
    }

    private static boolean isFileEmpty(File file) throws IOException {
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        return fileBytes.length == 0;
    }
}



