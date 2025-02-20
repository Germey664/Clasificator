package Classification.StoreDataInMem;

import Classification.DataParamWR;
import Classification.Dataset;

public class DataSetArrDef implements Dataset {
    private double[][] array;
    private int length;//Количество активных операций
    private int lengthBase;//Количество ячеек для хранения одной операции.
    private int deltaOperation;

    public DataSetArrDef(){

    }
    public DataSetArrDef(int length, int lengthBase){
        this.lengthBase = lengthBase;
        this.length = length;
        deltaOperation = 0;
        array =  new double[length][lengthBase];
    }
    private void movingOperation(int indexStart, int delta){
        if(delta >= 0){
            for(int j = deltaOperation; j >= indexStart; j--){
                for(int i = 0; i < lengthBase; i++)
                    array[j+delta][i] = array[j][i];
            }
        }else{
            for(int j = indexStart; j < deltaOperation; j++){
                for(int i = 0; i < lengthBase; i++)
                    array[j+delta][i] = array[j][i];
            }
        }
    }

    public double[] addOperationToTail(double[] operation){
        if(deltaOperation > length)
            throw new IllegalArgumentException("Количество элементов превысило размер массива.");
        for(int i = 0; i < lengthBase; i++)
            array[deltaOperation][i] = operation[i];
        deltaOperation++;
        return array[deltaOperation];
    }
    public double[] addOperationByIndex( int index, double[] operation){
        if(deltaOperation > length)
            throw new IllegalArgumentException("Количество элементов превысило размер массива.");
        if(index == deltaOperation)
            return addOperationToTail(operation);
        if(index > deltaOperation)
            index = deltaOperation;
        movingOperation(index,1);
        for(int i = 0; i < lengthBase; i++)
            array[index][i] = operation[i];
        deltaOperation++;
        return array[index];
    }

    public double[] deleteOperationToTail() {
        deltaOperation--;
        double[] operation = new double[lengthBase];
        for(int i = 0; i < lengthBase; i++){
            operation[i] = array[deltaOperation][i];
            array[deltaOperation][i] = 0;
        }
        return operation;
    }
    public double[] deleteOperationByIndex(int index){
        if(index > deltaOperation)
            throw new IllegalArgumentException("index не может быть больше length");
        double[] operation = new double[lengthBase];
        for(int i = 0; i < lengthBase; i++){
            operation[i] = array[index][i];
        }
        movingOperation(index+1,-1);
        deltaOperation--;
        return operation;
    }

    public double[] getOperationToTail(){
        return getOperationByIndex(deltaOperation);
    }
    public double[] getOperationByIndex(int index){
        double[] p = new double[lengthBase];
        for(int i = 0; i < lengthBase; i++){
            p[i] = array[index][i];
        }
        return p;
    }

    public double[] setOperationToTail(double[] operation){
        return setOperationByIndex(deltaOperation,operation);
    }
    public double[] setOperationByIndex(int index, double[] operation){
        for(int i = 0; i < lengthBase; i++){
            array[index][i] = operation[i];
        }
        return array[index];
    }

    public int getLength() {
        return length;
    }
    public int getLengthBase() {
        return lengthBase;
    }

    public String getString(){
        String s = "";
        for(int j = 0; j < deltaOperation; j++){
            for(int i = 0; i < lengthBase; i++){
                if(array[j][i]<10) s+=' ';
                s+= array[j][i] + " ";
            }
            s+='\n';
        }
        return s;
    }
}
