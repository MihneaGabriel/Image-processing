package PackWork;

import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ImageResultWriter implements WriterResult {
	private final int[][] pixelArray;
	private final Lock[][] locks; // Array of locks for fine-grained locking

	public ImageResultWriter(BufferedImage img) {
		this.pixelArray = new int[img.getWidth()][img.getHeight()];
		this.locks = new Lock[img.getWidth()][img.getHeight()];
		initializeLocks();
	}

	public void initializeLocks() {
		for (int i = 0; i < locks.length; i++) {
			for (int j = 0; j < locks[i].length; j++) {
				locks[i][j] = new ReentrantLock();
			}
		}
	}

	@Override
	public void setPixelValue(int value, int i, int j) {
		// Acquire lock for this specific pixel
		locks[i][j].lock();
		try {
			pixelArray[i][j] = value;
		} finally {
			// Release the lock
			locks[i][j].unlock();
		}
	}

	public int getPixelValue(int i, int j) {
		// Acquire lock for this specific pixel
		locks[i][j].lock();
		try {
			return pixelArray[i][j];
		} finally {
			// Release the lock
			locks[i][j].unlock();
		}
	}

	public int getHeight() {
		return pixelArray[0].length;
	}

	public int[][] getPixelArray() {
		return pixelArray;
	}

}
