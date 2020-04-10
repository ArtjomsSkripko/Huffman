import java.io.File;
import java.io.IOException;
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

        boolean iscorect = false;
        while (!iscorect) {
            System.out.println("Select action with file: ");
            System.out.println("1    Encode File");
            System.out.println("2    Decode File");
            int answer = in.nextInt();
            switch (answer) {
                case 1:
                    System.out.println("Encoding.....");
                    iscorect = true;

                    File file = new File(pathname);
                    Encoder encoder = new Encoder();
                    encoder.encode(file);
                    System.out.print("Your compressed file 'zipped.huff' is ready, located in same folder");
                    break;
                case 2:
                    System.out.println("Decoding.....");
                    iscorect = true;

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
}



