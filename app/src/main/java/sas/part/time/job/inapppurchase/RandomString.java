package sas.part.time.job.inapppurchase;

import java.util.Random;

public class RandomString {

	private static final char[] symbols = new char[47];
	private static final char[] special = {'!','@','#','$','%','^','&','*','+','-','/'};

	static {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
        for (int idx = 36, i=0; idx < 47; i++, ++idx)
        	symbols[idx] = special[i];
    }
	
	/*
     * static { for (int idx = 0; idx < 10; ++idx) symbols[idx] = (char)
     * ('0' + idx); for (int idx = 10; idx < 36; ++idx) symbols[idx] =
     * (char) ('a' + idx - 10); }
     */

    private final Random random = new Random();

    private final char[] buf;

    public RandomString(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx) {
            int position = random.nextInt(symbols.length);
            if(position<10 || position >=36 || position % 2 == 0) {
            	buf[idx] = symbols[position];
        	} else {
        		buf[idx] = Character.toUpperCase(symbols[position]);
        	}
        }
        return new String(buf);
    }
    
    
    
    
}
