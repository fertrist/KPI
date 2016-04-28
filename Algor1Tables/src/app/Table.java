package app;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private static int MAX = 254;
    private final Record[] records = new Record[MAX + 1];
    /*
     * Direct address actions
     */
    public Record select(int indx) {
        if (indx >= MAX) indx = MAX - 1;
        if (indx < 0) indx = 0;
        return records[indx].clone();
    }

    public boolean insert(Record record, int indx) {
        if (indx >= MAX) indx = MAX - 1;
        if (indx < 0) indx = 0;
        if (records[indx] != null) shift(indx);
        if (record != null) records[indx] = record.clone();
        else records[indx] = null;
        return true;
    }

    public void shift(int indx) {
        int upper = findFirstNull(indx);
        for (int i = upper; i > indx; i--) {
            swap(i, i - 1);
        }
    }

    public int insert(Record record) {
        for (int i = 0; i < records.length; i++) {
            if (records[i] == null) {
                if (insert(record, i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void delete(int indx) {
        if (indx >= MAX) indx = MAX;
        if (indx < 0) indx = 0;
        records[indx].delete();
    }

    public void update(Record record, int indx) {
        if (record == null) return;
        if (indx >= MAX) indx = MAX;
        if (indx < 0) indx = 0;
        Record ref = records[indx];
        if (ref.equals(record)) return;
        if (!ref.getKey().equals(record.getKey())) {
            if (!ref.getKey().getStr().equals(record.getKey().getStr())) ref.getKey().setStr(record.getKey().getStr());
            if (ref.getKey().getMod() != record.getKey().getMod()) ref.getKey().setMod(record.getKey().getMod());
        }
        if (ref.getFunctional().getValue() != record.getFunctional().getValue())
            ref.getFunctional().setValue(record.getFunctional().getValue());
    }

    /*
     * Complicated actions
     */
    //Linear selection
    public List<Record> select(Key keyArg) {
        List<Record> resultList = new ArrayList<Record>();
        for (Record record : records) {
            if (record != null && record.getKey().equals(keyArg)){
                resultList.add(record.clone());
                resultList.addAll(getNearest(record));
            }
        }
        if (resultList.isEmpty()) return null;
        return resultList;
    }

    //Binary selection
    public List<Record> selectBinary(Key keyArg) {
        List<Record> resultList = new ArrayList<Record>();
        //find first 'not null' counting from the end
        int down = 0;
        int up = 0;
        //find 'up' rate
        for(int i = records.length; i >= 0; i--){
            up = findFirstNotNull(i);
            if(up != 0) break;
        }
        int mid = (up + down) / 2;
        if(records[mid] == null ){
            mid = findFirstNotNull(mid);
        }
        int result = records[mid].getKey().compareTo(keyArg);
        //if similar then record was found
        while (result != 0) {
            if (result > 0) up = mid;
            else down = mid;

            mid = (up + down) / 2;
            if (mid == down) return null;
            if(records[mid] == null ){
                mid = findFirstNotNull(mid, up);
                if(mid == 0) throw new RuntimeException("Something wrong with table...");
            }
            result = records[mid].getKey().compareTo(keyArg);
        }
        resultList.add(records[mid].clone());
        resultList.addAll(getNearest(records[mid]));
        return resultList;
    }

    private List<Record> getNearest(Record record){
        List<Record> resultList = new ArrayList<Record>();
        for(Record r : records){
            if(r != null && !r.equals(record) && r.isSimilar(record)){
                resultList.add(r.clone());
            }
        }
        return resultList;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < records.length; i++) {
            if (records[i] != null && !records[i].isRemoved()) {
                str.append(i + " : " + records[i] + "\n");
            }
        }
        return str.toString();
    }

    //implements Insertion algorithm
    public void sort() {
        for (int i = 0; i < records.length; i++) {
            for (int k = i; k >= 1; k--) {
                Record r1 = records[k];
                Record r2 = records[k - 1];
                if (r1 == null || r1.compareTo(r2) >= 0) break;
                swap(k, k - 1);
            }
        }
    }

    private void swap(int r1, int r2) {
        if (records[r1] == null && records[r2] == null) return;
        Record tmp;
        if (records[r1] != null) tmp = records[r1].clone();
        else tmp = null;
        records[r1] = records[r2].clone();
        if (tmp != null) records[r2] = tmp.clone();
        else records[r2] = null;
    }

    public int countRecords() {
        int i = 0;
        for (Record record : records) {
            if (record != null && !record.isRemoved()) i++;
        }
        return i;
    }

    public Record[] getRecords() {
        return records;
    }

    //Help functions

    public int findFirstNull(int from) {
        for (int i = from; i < records.length; i++) {
            if (records[i] == null) return i;
        }
        return MAX;
    }

    public int findFirstNotNull(int from){
        for (int i = from; i < records.length; i++) {
            if (records[i] != null) return i;
        }
        return 0;
    }

    public int findFirstNotNull(int from, int to){
        for (int i = from; i < to; i++) {
            if (records[i] != null) return i;
        }
        return 0;
    }
}
