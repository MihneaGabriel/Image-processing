
package PackTest;

import PackWork.FileManipulation;

import java.io.IOException;
import java.util.Scanner;

public class MyMain {
	public static final String file1 = "Tesla.bmp";
	public static final String file2 = "Corgi.bmp";
	public static final String file3 = "Something.bmp";

	public static void main(String[] args) throws IOException, InterruptedException { // MAIN CLASS
		Scanner scanner = new Scanner(System.in);

		long startTime = System.nanoTime();
		
		
		System.out.println("Image to choose:");
		System.out.println(" 1-Tesla\n 2-Corgi\n 3-Something");
		System.out.println("Select:");
		int number = scanner.nextInt();

		System.out.println("Number of proceses for execution: ");
		int threads = scanner.nextInt();
		System.out.println("Resize width and height: "); // 400x427 for example
		int width = scanner.nextInt();
		int height = scanner.nextInt();
		System.out.println("Name an output: ");
		String OutputPath = scanner.next() + ".bmp";

		long endTime1 = System.nanoTime();
		System.out.println("Elapsed Time Identification: " + (endTime1 - startTime) + " milliseconds");
		
		switch (number) {
		case 1:
			FileManipulation f1 = new FileManipulation(file1, OutputPath);
			f1.setThreads(threads);
			f1.setDimension(width, height, endTime1);
			f1.FileRead();
			f1.FileWirte();
			break;
		case 2:
			FileManipulation f2 = new FileManipulation(file2, OutputPath);
			f2.setThreads(threads);
			f2.setDimension(width, height, endTime1);
			f2.FileRead();
			f2.FileWirte();
			break;
		case 3:
			FileManipulation f3 = new FileManipulation(file3, OutputPath);
			f3.setThreads(threads);
			f3.setDimension(width, height, endTime1);
			f3.FileRead();
			f3.FileWirte();
			break;
		default:
			System.out.println("Invalid number. Please enter a number between 1 and 3.");
		}

		scanner.close();

	}

}
