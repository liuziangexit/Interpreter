package fire.interpreter;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 8/30/2019
 * @description serialization and deserialization for primitive numeric types and java.lang.String
 */

public class TypePunning {

	private static final long DOUBLE_EXP_BIT_MASK = 0x7FF0000000000000L;
	private static final long DOUBLE_SIGNIF_BIT_MASK = 0x000FFFFFFFFFFFFFL;

	// serialization 64 bit floating-point number to bytes
	// supports negative infinity and positive infinity
	// if the argument is NaN, RuntimeException will be thrown
	static public byte[] fp64s(double i) {
		long lng = Double.doubleToRawLongBits(i);
		// check whether the argument is a NAN
		// see https://en.wikipedia.org/wiki/IEEE_754-1985
		if (((lng & DOUBLE_EXP_BIT_MASK) == DOUBLE_EXP_BIT_MASK)//
				&& (lng & DOUBLE_SIGNIF_BIT_MASK) != 0L)
			throw new RuntimeException("TypePunning.fp64s checked an NAN");
		return i64s(lng);
	}

	// deserialization 64 bit floating-point number from bytes
	static public double fp64d(byte[] i) {
		return Double.longBitsToDouble(i64d(i));
	}

	// serialization 32 bit integer number to bytes
	static public byte[] i32s(int i) {
		byte[] result = new byte[4];
		for (int f = 0; f < 4; f++)
			result[f] = (byte) (i >>> ((3 - f) * 8));
		return result;
	}

	// deserialization 32 bit integer number from bytes
	static public int i32d(byte[] i) {
		int result = 0;
		for (int f = 0; f < 4; f++)
			result |= ((0xFF & (int) i[f]) << ((3 - f) * 8));
		return result;
	}

	// serialization 64 bit integer number to bytes
	static public byte[] i64s(long i) {
		byte[] result = new byte[8];
		for (int f = 0; f < 8; f++)
			result[f] = (byte) ((i >>> ((7 - f) * 8)));
		return result;
	}

	// deserialization 64 bit integer number from bytes
	static public long i64d(byte[] i) {
		long i64 = 0;
		for (int f = 0; f < 8; f++)
			i64 |= ((0xFFL & (long) i[f]) << ((7 - f) * 8));
		return i64;
	}

	static public byte[] strs(String i) {
		return i.getBytes();
	}

	static public String strd(byte[] i) {
		return new String(i);
	}

}
