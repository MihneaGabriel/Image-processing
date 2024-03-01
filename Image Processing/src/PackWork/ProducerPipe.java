package PackWork;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;

class ProducerPipe extends Thread {
	private DataOutputStream out;
	private BufferedImage image;
	private WriterResult buffer;
	private int startColumn, endColumn;

	public ProducerPipe(DataOutputStream out, BufferedImage image, WriterResult buffer, int startColumn,
			int endColumn) {
		this.out = out;
		this.image = image;
		this.buffer = buffer;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}

	@Override
	public void run() {
		try {
			for (int x = startColumn; x <= endColumn; x++) {
				for (int y = 0; y < buffer.getHeight(); y++) {
					int pixelValue = image.getRGB(x, y);

					// Send pixel value to the consumer through the pipe
					out.writeInt(pixelValue);
					out.flush();

					// Store pixel value in the shared buffer
					buffer.setPixelValue(pixelValue, x, y);
					System.out.println("Produced pixel Value :\t" + pixelValue);

					// Simulate some processing time
					//sleep((int) (Math.random() * 100));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
