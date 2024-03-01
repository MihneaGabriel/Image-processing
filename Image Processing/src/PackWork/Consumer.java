
package PackWork;

public class Consumer extends Thread {
	private SharedBuffer buffer;
	private int[][] pixelArray;
	private int height, startColumn, endColumn;

	public Consumer(SharedBuffer buffer, int height, int startColumn, int endColumn, int[][] pixelArray) {
		this.buffer = buffer;
		this.height = height;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.pixelArray = pixelArray;
	}

	@Override
	public void run() {
		try {
			for (int i = startColumn; i <= endColumn; i++)
				for (int j = 0; j < height; j++) {
					int pixel = buffer.consume();

					synchronized (pixelArray) {
						pixelArray[i][j] = pixel;
					}
					// sleep(1500);
				}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
