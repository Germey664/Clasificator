package Classification;

public class DataParamWR {
    double[] data;
    int deltaData;
    int deltaParam;
    int count;
    int[] param;
    public DataParamWR(int count){
        this.count = count;
        deltaData = 0;
        deltaParam = 0;
        data = new double[count];
        param = new int[count];
    }
    public void write(double p){
        data[deltaData] = p;
        param[deltaParam] = 1;
        deltaData++;
        deltaParam++;
    }
    public void write(boolean p){
        if(p) write(1); else write(0);
    }
    public void write(double[] p, int size){
        param[deltaParam] = size;
        deltaParam++;
        for(int i = 0; i < size; i++) {
            data[deltaData] = p[i];
            deltaData++;
        }
    }
    public void write(int[] p, int size){
        double[] p_ = new double[size];
        for(int i = 0; i < size; i++) {
            p_[i] = p[i];
        }
        write(p_,size);
    }

    public Object read(){
        if(param[deltaParam-1] > 1){//Массив
            deltaParam--;
            double[] p = new double[param[deltaParam]];
            for(int i = 0; i < param[deltaParam]; i++){
                deltaData--;
                p[i] = data[deltaData];
            }
            param[deltaParam] = 0;
            return p;
        }else {
            deltaData--;
            param[deltaParam]--;
            return data[deltaData];
        }
    }
    public double[] readDataParamWR(){
        double[] arrayData = new double[getCount()];
        int delta = 0;
        for(int j = 0; j < getCount(); j++) {
            Object d = read();
            if (d.getClass() == double.class) {
                arrayData[delta] = (double) d;
                delta++;
            } else if (d.getClass() == double[].class) {
                double[] buff = (double[]) d;
                for (int i = 0; i < buff.length; i++) {
                    arrayData[delta] = (double) d;
                    delta++;
                }
            }
        }
        return arrayData;
    }
    public double[] getData(){
        return data;
    }
    public double[] readAllData(){
        deltaData = 0;
        deltaParam = 0;
        return data;
    }
    public int getDeltaData() {
        return deltaData;
    }
    public int getCount() {
        return count;
    }
}
