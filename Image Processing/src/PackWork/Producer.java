
package PackWork;

import java.awt.image.BufferedImage;

public class Producer extends Thread {
	private SharedBuffer buffer;
	private BufferedImage image;
	private int height, startColumn, endColumn;

	public Producer(SharedBuffer buffer, int height, int startColumn, int endColumn) {
		this.buffer = buffer;
		this.height = height;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public void run() {
		try {
			for (int i = startColumn; i <= endColumn; i++)
				for (int j = 0; j < height; j++) {
					// Read a segment from the image file
					buffer.produce(image.getRGB(i, j));
					//sleep(1000);
				}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
