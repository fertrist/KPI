/**
 * Memory representation is real-manner number of bytes
 * Allocator works with headers and their order numbers
 *
 * HEADER:
 * Each header is int which is 32b=4B
 * 00000000 00000000 00000000 00000000
 * MAX value for block is less then 2^15=32768, so it needs 15 bits
 *  * The first 16 bits(except first which maps to sign) map to current block size
 * The next 15 bits map to previous block size
 * The last bit shows if block is empty or not
 *
 * The first and the last headers are always marked as used
 *
 * Initialization headers: 1 = 0/0/1   2 = x/0/0    3 = 0/x/1
 */
package app;

import java.util.ArrayList;
import java.util.List;

import static app.UtilBinary.*;

public class Allocator {

    static short DOMAIN_SIZE = 0b00000000_00000000_01111111_11111111;  //32767
    static short LIMIT = 4;
    List<byte[]> domainsList;
    List<long[]> headersList;
    byte [] domain = new byte[DOMAIN_SIZE];
    long [] headers;
    short MIN = 1;

    public Allocator(){
        domainsList = new ArrayList<>();
        headersList = new ArrayList<>();
        allocateNewMemoryDomain();
    }

    private short allocateNewMemoryDomain(){
        byte [] newDomain = new byte[DOMAIN_SIZE];
        long [] domainHeaders = new long[3];
        for(int i = 0; i < 3; i++){domainHeaders[i] = setDomain(domainHeaders[i], domainsList.size());}
        domainHeaders[0] = markAsUsed(domainHeaders[0]);
        domainHeaders[0] = setCurrent(domainHeaders[0], LIMIT);
        domainHeaders[1] = setPrevious(domainHeaders[1], getCurrent(domainHeaders[0]));
        domainHeaders[1] = setCurrent(domainHeaders[1], DOMAIN_SIZE - getPrevious(domainHeaders[1]));
        domainHeaders[2] = setPrevious(domainHeaders[2], getCurrent(domainHeaders[1]));
        domainHeaders[2] = markAsUsed(domainHeaders[2]);
        domainsList.add(newDomain);
        if(headersList.add(domainHeaders)){
            System.out.printf("[SUCCESS] New memory domain have been allocated. It's number: %d.%n", headersList.size());
            return (short) headersList.size();
        }
        else{
            System.out.println("[ERROR] New memory domain haven't been allocated.");
            return -1;
        }
    }

    public void report(){
        System.out.println("\n[INFO] Allocated memory state: ");
        for(short d = 0; d < domainsList.size(); d++) {
            System.out.printf("[INFO] DOMAIN#%d%n", d+1);
            System.out.printf("%10s%15s%15s%15s%10s%n", "BLOCK_ID", "ADDRESS", "CURRENT", "PREVIOUS", "USED");
            long [] headers = headersList.get(d);
            for (int i = 0; i < headers.length; i++) {
                System.out.printf("%10s%15d%15d%15d%10d%n",
                        String.format("ID=%5d :", i + 1),
                        getDirectAdress(getAddressForUser(d, (short) i)),
                        getCurrent(headers[i]),
                        getPrevious(headers[i]),
                        getUsed(headers[i]));
            }
        }
    }

    public long allocateMemory(short size){
        System.out.printf("%n.[INFO] Allocating %dB of memory. ", size);
        if(size % LIMIT != 0) size = (short) (size - (size % LIMIT) + LIMIT);
        System.out.printf("Actual size required is %dB ", size);
        if(Math.abs(size) > DOMAIN_SIZE-LIMIT*2){
            System.out.println("Required memory can't be allocated."); return -1;}
        for(short d = 0; d < domainsList.size(); d++) {
            System.out.printf("%n[INFO] Browsing domain#%d...%n", d + 1);
            long [] headers = headersList.get(d);
            for (short i = MIN; i < headers.length; i++) {
                long header = headers[i];
                if (getUsed(header) == 0 && (getCurrent(header) - size >= LIMIT)) {
                    short headerNo = splitBlock(d, i, size);
                    removeBlockData(d, headerNo);
                    return getAddressForUser(d, headerNo);
                }
            }
        }
        System.out.println("[INFO] Memory have not been allocated. Allocating new memory domain...");
        short d = allocateNewMemoryDomain();
        if(d != -1){return allocateMemory(size);}
        else{
            System.out.println("[ERROR] Problem allocating memory.");
            return d;
        }
    }

    private long getAddressForUser(short domainNo, short headerNo){
        int addressWithinDomain = getInitialAddress(domainNo, headerNo);
        return setDomain(addressWithinDomain, domainNo);
    }

    private short getInitialAddress(short domainNo, short headerNo){
        short initialAddress = 0;
        long [] headers = headersList.get(domainNo);
        for(int i = 0; i <= headerNo; i++){
            initialAddress += getPrevious(headers[i]);
        }
        return initialAddress;
    }

    public short getHeaderNo(long address){
        short domainNo = getDomain(address);
        long [] headers = headersList.get(domainNo);
        short directAddress = getDirectAdress(address);
        for(short i = 0; i < headers.length; i++){
            short initialAddress = getInitialAddress(domainNo, i);
            if((directAddress >= initialAddress)
                    && (directAddress < initialAddress + getCurrent(headers[i]))) return i;
        }
        return -1;
    }

    public long reallocateMemory(long address, int newSize){
        short d = getDomain(address);
        long [] headers = headersList.get(d);
        short oldHeaderNo = getHeaderNo(address);
        long oldSize = getCurrent(headers[oldHeaderNo]);
        if(newSize == oldSize) return -1;
        long newAddress = allocateMemory((short) newSize);
        if(newAddress != -1){
            copyData(address, newAddress);
            freeMemory(address);
            return newAddress;
        }else{
            System.out.println("Memory have not been re-allocated.");
            return -1;
        }
    }

    private void copyData(long address1, long address2) {
        short d1 = getDomain(address1);
        short d2 = getDomain(address2);
        long [] headers1 = headersList.get(d1);
        long [] headers2 = headersList.get(d2);
        byte [] domain1 = domainsList.get(d1);
        byte [] domain2 = domainsList.get(d2);
        short sourceHeaderNo = getHeaderNo(address1);
        short targetHeaderNo = getHeaderNo(address2);
        long sourceSize = getCurrent(headers1[sourceHeaderNo]);
        int sourceAddress = getInitialAddress(d1, sourceHeaderNo);
        long targetSize = getCurrent(headers2[targetHeaderNo]);
        int targetAddress = getDirectAdress(address2);

        if(sourceSize <= targetSize){
            for(int i = sourceAddress, j = targetAddress;
                i < sourceAddress + sourceSize; i++, j++){
                domain2[j] = domain1[i];
            }
        }else{
            for(int i = targetAddress, j = sourceAddress; i < targetAddress + targetSize; i++, j++){
                domain2[i] = domain1[j];
            }
        }
    }

    public void freeMemory(long address){
        short d = getDomain(address);
        long [] headers = headersList.get(d);
        short headerNo = getHeaderNo(address);
        headers[headerNo] = markUnused(headers[headerNo]);
        if(getUsed(headers[headerNo+1]) == 0){
            headers = concatHeaders(d, headerNo, (short) (headerNo+1));
            headersList.set(d, headers);
        }
        if(getUsed(headers[headerNo-1]) == 0){
            headers = concatHeaders(d, (short) (headerNo-1), headerNo);
        }
        headersList.set(d, headers);
    }

    private long[] concatHeaders(short d, short headerNo1, short headerNo2){
        long [] headers = headersList.get(d);
        long header1 = headers[headerNo1];
        long header2 = headers[headerNo2];
        header1 = setCurrent(header1, getCurrent(header1) + getCurrent(header2));
        header1 = markUnused(header1);
        headers = removeHeader(d, headerNo2);
        headers[headerNo1] = header1;
        headers[headerNo2] = setPrevious(headers[headerNo2], getCurrent(header1));
        return headers;
    }

    private long[] removeHeader(short d, short indx){
        long [] headers = headersList.get(d);
        long[] newHeaders = new long[headers.length-1];
        for(int i = 0, j = 0; i < headers.length; i++){
            if(i == indx) continue;
            newHeaders[j] = headers[i];
            j++;
        }
        return newHeaders;
    }

    private void removeBlockData(short domainNo, short headerNo){
        short begin = getInitialAddress(domainNo, headerNo);
        long [] headers = headersList.get(domainNo);
        long end = begin + getCurrent(headers[headerNo]);
        byte [] domain = domainsList.get(domainNo);
        for(int i = begin; i < end; i++){
            domain[i] = 0;
        }
        domainsList.set(domainNo, domain);
    }

    private short splitBlock(short d, short headerNo, short size){
            long [] headers = headersList.get(d);
            long oldHeader = headers[headerNo];
            //configure new header
            long newHeader = setPrevious(0, getPrevious(oldHeader));
            newHeader = setCurrent(newHeader, size);
            newHeader = markAsUsed(newHeader);
            newHeader = setDomain(newHeader, d);

            //configure old header
            oldHeader = setCurrent(oldHeader, getCurrent(oldHeader) - size);
            oldHeader = setPrevious(oldHeader, size);

            //split old header
            headers = insertEmptyHeader(d, headerNo);

            headers[headerNo] = newHeader;
            headers[headerNo+1] = oldHeader;
            headers[headerNo+2] = setPrevious(headers[headerNo+2], getCurrent(headers[headerNo+1]));
            headersList.set(d, headers);
            return headerNo;
    }

    /**
     * places an empty header on 'index+1' position
     */
    private long[] insertEmptyHeader(short d, int indx){
        long [] headers = headersList.get(d);
        long[] newHeaders = new long[headers.length + 1];
        int f = 0;
        for(int i = 0; i < newHeaders.length;){
            if(i == indx){
                //pass through empty header, use step-back
                i++; f = -1; continue;
            }
            newHeaders[i] = headers[i+f];
            i++;
        }
        return newHeaders;
    }

    public void showContent(long address){
        int d = getDomain(address);
        headers = headersList.get(d);
        int headerNo = getHeaderNo(address);
        byte [] domain = domainsList.get(d);
        System.out.printf("\nContent of D%d/H%d: \n", d+1, headerNo+1);
        int width = 0;
        for(long i = address, j = 0; i < address + getCurrent(headers[headerNo]); i++, j++){
            System.out.printf("%10s", "[" + toBinaryForm(domain[((int) i)]) + "]");
            width++;
            if(width == 10) {System.out.println(); width = 0;}
            if(j == 100){
                System.out.println("\t\t.................................");
                break;
            }
        }
        headers = null;
    }

    public void writeDataToBlock(long address, byte[] data){
        short d = getDomain(address);
        short directAddress = getDirectAdress(address);
        headers = headersList.get(d);
        byte [] domain = domainsList.get(d);
        long end = directAddress + getCurrent(headers[getHeaderNo(address)]);
        if(data.length <= end - directAddress){
            for(int i = 0, j = directAddress; i < data.length; i++, j++){
                domain[j] = data[i];
            }
        }
        else{
            for(int i = 0, j = directAddress; j < end; i++, j++){
                domain[j] = data[i];
            }
        }
        headers = null;
    }

    public String toBinaryForm(byte value){
        byte bits[] = new byte[]{0b00000001, 0b00000010,
                0b00000100, 0b00001000, 0b00010000, 0b00100000, 0b01000000};
        String binaryForm = "0";
        for(int i = bits.length - 1; i >= 0; i--){
            binaryForm += ((value & bits[i]) >> i);
        }
        return binaryForm;
    }

}
