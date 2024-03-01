
package PackWork;

import java.io.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;

public class FileManipulation extends RGB {
	protected String file, OutputPath;
	private BufferedImage resizedImage;
	public static long endTime3;

	// Static initialization block
	static {
		System.out.println("Trying to open the file...");
	}

	public FileManipulation(String file, String OutputPath) {
		this.file = file;
		this.OutputPath = OutputPath;
	}

	public void FileRead() {
		BufferedImage image = null;
		try {
			// FILE OPEN
			System.out.println("File open " + file);
			image = ImageIO.read(new File(file));
			// Read an Write the file char by char
			System.out.println("Image width: " + image.getWidth());
			System.out.println("Image height: " + image.getHeight());
			// '5' means that is an image with RGB colors
			System.out.println("Image type: " + image.getType());

			DisplayImage(image);

			resizedImage = RGBRead(image);

			DisplayImage(RGBRead(image)); // Manipulate RGB's

			FileManipulation.endTime3 = System.nanoTime();
			System.out.println("Elapsed Time Processing: " + (endTime3 - getEndTime2()) + " milliseconds");

		} catch (FileNotFoundException e) {

			System.out.println("File not found!");
			System.out.println("Exception : " + e.getMessage());
		} catch (IOException e) {

			System.out.println("Error on writing the file!");
			e.printStackTrace();
		}
	}

	public void FileWirte() throws IOException, InterruptedException {
		try {
			// Shared buffer to hold pixel values
			WriterResult buffer = new ImageResultWriter(resizedImage);
			int[][] pixelArray = new int[resizedImage.getWidth()][resizedImage.getHeight()];

			// Create piped streams and data streams
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn = new PipedInputStream(pipeOut);
			DataOutputStream dataOut = new DataOutputStream(pipeOut);
			DataInputStream dataIn = new DataInputStream(pipeIn);

			// Create producer and consumer threads for each segment
			int numSegments = 4;
			int segmentWidth = resizedImage.getWidth() / numSegments;

			ProducerPipe[] producers = new ProducerPipe[numSegments];
			ConsumerPipe[] consumers = new ConsumerPipe[numSegments];

			for (int i = 0; i < numSegments; i++) {
				int startColumn = i * segmentWidth;
				int endColumn = (i + 1) * segmentWidth - 1;

				producers[i] = new ProducerPipe(dataOut, resizedImage, buffer, startColumn, endColumn);
				consumers[i] = new ConsumerPipe(dataIn, buffer, startColumn, endColumn);

				// Start producer and consumer threads for each segment
				producers[i].start();
				consumers[i].start();
			}

			// Wait for all producer threads to finish before closing the pipe
			for (int i = 0; i < numSegments; i++) {
				producers[i].join();
			}

			// Close the PipedOutputStream to signal the end of data
			pipeOut.close();

			// Wait for all consumer threads to finish
			for (int i = 0; i < numSegments; i++) {
				consumers[i].join();
			}

			// Access processed image from the buffer
			pixelArray = buffer.getPixelArray();
			// Put the pixels into image
			BufferedImage finalImage = new BufferedImage(resizedImage.getWidth(), resizedImage.getHeight(), resizedImage.getType());
			finalImage = Traverse(pixelArray, finalImage); 
			
			DisplayImage(finalImage);
		
			// Save the image
			SaveImage(finalImage, OutputPath);

			long endTime4 = System.nanoTime();
			System.out.println("Elapsed Time Writing: " + (endTime4 - endTime3) + " milliseconds");

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	private BufferedImage Traverse(int[][] pixelArray, BufferedImage finalImage) {
		for (int i = 0; i < pixelArray.length; i++)
			for (int j = 0; j < pixelArray[i].length; j++) {
				finalImage.setRGB(i, j, pixelArray[i][j]);
			}
		return finalImage;
	}

	private void SaveImage(BufferedImage finalImage, String OutputPath) {
		try {
			File output = new File(OutputPath);
			ImageIO.write(finalImage, "BMP", output);
			System.out.println("Image saved successfully to: " + OutputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Optional , I'm using it just to see the image
	private static void DisplayImage(BufferedImage image) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
