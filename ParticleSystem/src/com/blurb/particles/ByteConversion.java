
package com.blurb.particles;

import java.nio.ByteBuffer;

/**
 *
 * @author Joshua Waring
 */
public class ByteConversion {
    byte[] cacheByte;
    float cacheFloat;
    int cacheInt;    
    
    public float ByteToFloat(byte[] b){
        ByteBuffer buf = ByteBuffer.wrap(b);
        return buf.getFloat(); 
    }
    public byte[] FloatToByte(float f) {
        cacheByte = new byte[4];
        int bits = Float.floatToIntBits(f); 
        cacheByte[0] = (byte) (bits & 0xff);
        cacheByte[1] = (byte) ((bits >> 8) & 0xff);
        cacheByte[2] = (byte) ((bits >> 16) & 0xff);
        cacheByte[3] = (byte) ((bits >> 24) & 0xff);
        return cacheByte;
    }
    public int ByteToInt(byte[] b) {
        cacheInt = 0;
        cacheInt <<= 8;
        cacheInt |= (int) b[0] & 0xFF;

        cacheInt <<= 8;
        cacheInt |= (int) b[1] & 0xFF;

        cacheInt <<= 8;
        cacheInt |= (int) b[2] & 0xFF;

        cacheInt <<= 8;
        cacheInt |= (int) b[3] & 0xFF;

        return cacheInt;
    }
 
    public byte[] IntToByte(int i){
        cacheByte = new byte[4];
        cacheByte[3] = (byte) ((i >>> 24) & 0xff);
        cacheByte[2] = (byte) ((i >>> 16) & 0xff);
        cacheByte[1] = (byte) ((i >>> 8) & 0xff);
        cacheByte[0] = (byte) (i & 0xff);
             
        return cacheByte;
    }
}
