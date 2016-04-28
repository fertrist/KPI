package app;

public class UtilBinary {

    static long CURRENT
            = 0b00000000_00000000_00000000_00000000_01111111_11111111_00000000_00000000L;
    static long ERASE_CURRENT
            = 0b11111111_11111111_11111111_11111111_10000000_00000000_11111111_11111111L;
    static long PREVIOUS
            = 0b00000000_00000000_00000000_00000000_00000000_00000000_11111111_11111110L;
    static long ERASE_PREVIOUS
            = 0b11111111_11111111_11111111_11111111_11111111_11111111_00000000_00000001L;
    static long USED
            = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000001L;
    static long UN_USED
            = 0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111110L;
    static long DOMAIN
            = 0b00000000_00000000_11111111_11111111_00000000_00000000_00000000_00000000L;
    static long ERASE_DOMAIN
            = 0b11111111_11111111_00000000_00000000_11111111_11111111_11111111_11111111L;
    static long DIRECT_ADDRESS
            = 0b00000000_00000000_00000000_00000000_00000000_00000000_01111111_11111111L;

    public static short getDirectAdress(long address){
       return (short) (address & DIRECT_ADDRESS);
    }

    public static long setDomain(long header, long domain){
        header = header & ERASE_DOMAIN;
        return header | domain << 32;
    }

    public static short getDomain(long header){
        return (short) ((header & DOMAIN) >> 32);
    }

    public static long markAsUsed(long header){
        return header | USED;
    }

    public static long markUnused(long header){
        return header & UN_USED;
    }

    public static long getCurrent(long header){
        return (header & CURRENT) >> 16;
    }

    public static long setCurrent(long header, long value){
        header = header & ERASE_CURRENT;
        return header | (value << 16);
    }

    public static short getPrevious(long header){
        return (short) ((header & PREVIOUS) >> 1);
    }

    public static long setPrevious(long header, long value){
        header = header & ERASE_PREVIOUS;
        return header | (value << 1);
    }

    public static long getUsed(long header){
        return header & USED;
    }
}
