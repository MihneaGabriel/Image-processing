package PackWork;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

class ConsumerPipe extends Thread {
	private DataInputStream in;
	private WriterResult buffer;
	private int startColumn, endColumn;

	public ConsumerPipe(DataInputStream in, WriterResult buffer, int startColumn, int endColumn) {
		this.in = in;
		this.buffer = buffer;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}

	@Override
	public void run() {
		try {
			for (int x = startColumn; x <= endColumn; x++) {
				for (int y = 0; y < buffer.getHeight(); y++) {
					try {
						int pixelValue = in.readInt();

						// Store pixel value in the shared buffer
						buffer.setPixelValue(pixelValue, x, y);
						System.out.println("Received pixel value at position (" + x + ", " + y + "): " + pixelValue);


						// Simulate some processing time
						//sleep((int) (Math.random() * 100));
					} catch (EOFException e) {
						// Break out of the loop when EOF is encountered
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
