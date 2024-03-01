package PackWork;

import java.awt.image.BufferedImage;

public class RGB extends AbstractClass {
	private static int numThreads;
	private static int w, h;
	protected static long endTime1, endTime2;
	
	// Initialization block
	{
		numThreads = 4; // For the case he don't select something
	}
	public long getEndTime2() {
		return endTime2;
	}
	

	public void setThreads(int threads) {
		RGB.numThreads = threads;
	}

	public void setDimension(int w, int h, long endTime1) {
		RGB.w = w;
		RGB.h = h;
		RGB.endTime1 = endTime1;
	}

	protected BufferedImage RGBRead(BufferedImage image) {
		SharedBuffer buffer = new SharedBuffer(image.getHeight());
		
		int columnPerThread = image.getWidth() / numThreads;
		int[][] pixelArray = new int[image.getWidth()][image.getHeight()];

		Consumer[] consumers = new Consumer[numThreads];
		Producer[] producers = new Producer[numThreads];

		// WORKING ON THREADS IN PARALLEL
		for (int i = 0; i < numThreads; i++) {
			int startColumn = i * columnPerThread;
			int endColumn = (i + 1) * columnPerThread - 1;
			producers[i] = new Producer(buffer, image.getHeight(), startColumn, endColumn);
			consumers[i] = new Consumer(buffer, image.getHeight(), startColumn, endColumn, pixelArray);

			producers[i].setImage(image);

			producers[i].start();
			consumers[i].start();
		}

		try {
			// Wait for all Consumer threads to finish
			for (int i = 0; i < numThreads; i++) {
				consumers[i].join();
			}
		} catch (InterruptedException e) {
			// Restore the interrupted status
			Thread.currentThread().interrupt(); 
			e.printStackTrace();
		}

		RGB.endTime2 = System.nanoTime();
		System.out.println("Elapsed Time Reading: " + (endTime2 - endTime1) + " milliseconds");
		
		BufferedImage resizedImage = imageProcess(pixelArray, image, w, h);

		return resizedImage;

	}

	protected BufferedImage imageProcess(int[][] pixelArray, BufferedImage image, int newWidth, int newHeight) {
		double aspectRatio = (double) image.getWidth() / image.getHeight();

		if ((double) newWidth / aspectRatio > newHeight)
			newWidth = (int) Math.ceil(newHeight * aspectRatio);
		else
			newHeight = (int) Math.ceil(newWidth / aspectRatio);

		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType());

		double scaleX = (double) image.getWidth() / newWidth;
		double scaleY = (double) image.getHeight() / newHeight;

		for (int y = 0; y < newHeight; y++)
			for (int x = 0; x < newWidth; x++) {
				int originalX = (int) (x * scaleX);
				int originalY = (int) (y * scaleY);

				int[] rgbValues = getRGBValues(image, originalX, originalY);

				for (int i = 0; i < rgbValues.length; i++) {

					// Adjust the multiplication factor as needed
					rgbValues[i] = (int) (rgbValues[i] * 1.5);

					// Ensure values stay within the valid range (0-255)
					rgbValues[i] = Math.min(255, rgbValues[i]);
					setRGBVaules(resizedImage, x, y, rgbValues);
				}
			}
		return resizedImage;
	}

	protected int[] getRGBValues(BufferedImage image, int x, int y) {
		int rgb = image.getRGB(x, y);

		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = rgb & 0xFF;

		return new int[] { red, green, blue };

	}

	protected void setRGBVaules(BufferedImage resizedImage, int x, int y, int[] rgbValues) {
		int red = rgbValues[0];
		int green = rgbValues[1];
		int blue = rgbValues[2];

		// Ensure values stay within the valid range (0-255)
		red = Math.min(255, Math.max(0, red));
		green = Math.min(255, Math.max(0, green));
		blue = Math.min(255, Math.max(0, blue));

		// Combine RGB values into a single pixel value
		int pixelValue = (red << 16) | (green << 8) | blue;

		resizedImage.setRGB(x, y, pixelValue);
	}
}
