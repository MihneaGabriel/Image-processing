package PackWork;


public interface WriterResult {

	void setPixelValue(int value, int i, int j);
	int getPixelValue(int i, int j);
	int getHeight();
	int[][] getPixelArray();
	void initializeLocks();

}

// O interfata Java defineste un set de metode dar nu specifica nici o
// implementare
// pentru ele. O clasa care implementeaza o interfata trebuie obligatoriu sa
// specifice
// implementari pentru toate metodele interfetei.