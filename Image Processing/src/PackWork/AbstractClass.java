package PackWork;

import java.awt.image.BufferedImage;

public abstract class AbstractClass {

	public abstract void setThreads(int threads);

	public abstract void setDimension(int w, int h, long endTime1);

	protected abstract BufferedImage RGBRead(BufferedImage image);

	protected abstract BufferedImage imageProcess(int[][] pixelArray, BufferedImage image, int newWidth, int newHeight);

	protected abstract int[] getRGBValues(BufferedImage image, int x, int y);
	
	protected abstract void setRGBVaules(BufferedImage resizedImage, int x, int y, int[] rgbValues);
}
