package Classification;

/**Класс занимается хранением данных об массиве из операций.
 * На основе класса реализуется связанный список
 * Нужен для постепенного заполнения информации*/
public class ArrayNode{
    public ArrayNode next;
    public ArrayNode previous;

    protected double[][] array;
    protected int length;//Количество активных ячеек
    protected int lengthBase;//Количество ячеек для хранения операции.
    protected int arraySizeMax;//Количество сколько ячеек может использоваться максимально

    protected int arrayHashcode = 0;//Нэш код. После каждого обновления должен отличатся от прошлого за счет изменения shiftB
    protected boolean updateHashCode = false;//Переменная отвечает за сверку значений хеш кода. Должна быть true после пересчета и false кода приемник проверил
    protected double shiftB = 0;

    public ArrayNode(){

    }

    public ArrayNode(int arraySizeMax, int lengthBase){
        if(arraySizeMax < 0)
            throw new IllegalArgumentException("Размер массива должен быть больше 0");
        this.arraySizeMax = arraySizeMax;
        array  =  new double[arraySizeMax][lengthBase];
        length = 0;
        this.lengthBase = lengthBase;
    }
    public ArrayNode(int arraySizeMax, int lengthBase, double[][] array){
        if(arraySizeMax < 0)
            throw new IllegalArgumentException("Размер массива должен быть больше 0");
        if(array.length > arraySizeMax)
            throw new IllegalArgumentException("Массив не может быть больше чем: "+arraySizeMax);
        this.arraySizeMax = arraySizeMax;
        this.array = new double[arraySizeMax][lengthBase];
        this.lengthBase = lengthBase;
        for(int j = 0; j < array.length; j++) {
            for (int i = 0; i < array[0].length; i++) {
                this.array[j][i] = array[j][i];
            }
        }
        length=array.length;
    }

    public double[] getElementByIndex(int index){
        if(array == null)
            throw new ArrayIndexOutOfBoundsException("Массив не создан");
        if(index >= arraySizeMax || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        return array[index];
    }
    public double[] setElementByIndex(int index, double[] operation){
        if(array == null)
            throw new ArrayIndexOutOfBoundsException("Массив не создан");
        if(index >= arraySizeMax || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(operation.length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        for(int i = 0; i < operation.length; i++){
            array[index][i] = operation[i];
        }
        return  array[index];
    }

    public double[][] getArray(){
        return array;
    }
    public double[][] setArray(double[][] array){
        if(array == null)
            throw new ArrayIndexOutOfBoundsException("Массив не инициализирован");
        if(array.length > arraySizeMax)
            throw new IllegalArgumentException("Массив не может быть больше чем: "+arraySizeMax);
        if(array[0].length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        if(this.array == null )
            if(arraySizeMax != 0 && lengthBase != 0)
                this.array = new double[arraySizeMax][lengthBase];
            else
                throw new ArrayIndexOutOfBoundsException("Массив не создан. Возможно следует передать параметры для создания");
        for(int j = 0; j < array.length; j++) {
            for (int i = 0; i < array[0].length; i++) {
                this.array[j][i] = array[j][i];
            }
        }
        length = array.length;
        return array;
    }

    /**Количество активных операций*/
    public int getLength() {return length;}
    /**Максимальное количество операций*/
    public int getArraySizeMax() {return arraySizeMax;}
    /**Количество ячеек для хранения операции.*/
    public int getLengthBase() {return lengthBase;}

    public double[] addElementByIndex(int index, double[] operation){
        if(array == null)
            throw new NullPointerException("Массив не создан");
        if(operation == null)
            throw new NullPointerException("Операция не инициализированная");
        if(index >= arraySizeMax || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(operation.length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        if(length >= arraySizeMax)
            return null;
        for (int j = length - 1; j >= index; j--){
            for (int i = 0; i < operation.length; i++) {
                array[j+1][i] = array[j][i];
            }
        }
        array[index] = operation;
        length++;
        return this.array[index];
    }
    public double[] addElementToTail(double[] operation){
        if(array == null)
            throw new ArrayIndexOutOfBoundsException("Массив не создан");
        if(operation.length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        if(length >= arraySizeMax)
            return null;
        array[length] = operation;
        length++;
        return operation;
    }
    public double[][] addElements(int indexStart, double[][] array){
        if(this.array == null)
            throw new NullPointerException("Массив не создан");
        if(array == null)
            throw new NullPointerException("Операции не инициализированная");
        if(array[0].length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        if(length + array.length >= arraySizeMax)
            return null;
        for (int j = length + array.length; j >= indexStart+array.length; j--){
            for (int i = 0; i < array[0].length; i++) {
                this.array[j][i] = this.array[j-array.length][i];
            }
        }
        for (int j =  0; j < array.length; j++){
            for (int i = 0; i < array[0].length; i++) {
                this.array[j+indexStart][i] = array[j][i];
            }
        }
        length+=array.length;
        return this.array;
    }
    public double[] deleteElementByIndex(int index){
        if(array == null)
            throw new ArrayIndexOutOfBoundsException("Массив не создан");
        if(index >= arraySizeMax || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        double[] operation = array[index];
        for (int i = index; i < length; i++){
            array[i] = array[i+1];
        }
        length--;
        return operation;
    }

    public double[][] deleteElements(int indexStart, int indexEnd){
        if(array == null)
            throw new ArrayIndexOutOfBoundsException("Массив не создан");
        if(indexStart >= arraySizeMax || indexStart < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+indexStart+"]");
        if(indexEnd+1 >= arraySizeMax || indexEnd+1 < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+indexEnd+"]");
        if(indexEnd - indexStart < 0 )
            throw new ArrayIndexOutOfBoundsException("Область для удаления не может быть отрицательна");
        int diff = indexEnd - indexStart+1;
        double[][] operation = new double[diff][lengthBase];
        for (int i = 0; i < operation.length; i++){
            operation[i] = array[indexStart+i];
        }
        for (int i = indexStart; i < length - diff; i++){
            array[i] = array[i+diff];
        }
        length = length - diff;
        if(length<0)length= 0;
        return operation;
    }

    @Override
    public String toString() {
        String s = "size max: "+arraySizeMax+", length: "+length+", length base: "+lengthBase+", hash: "+arrayHashcode+"\n";
        for (int j = 0; j < length; j++) {
            s+=j+":{ ";
            for (int i = 0; i < lengthBase; i++)
                s += array[j][i] + " ";
            s+="}\n";
        }
        return s;
    }

    @Override
    public int hashCode() {
        long result = 31;
        result = 31 * result + (int) (Double.doubleToLongBits(shiftB) | (Double.doubleToLongBits(shiftB) >>> 32));
        result = 31 * result + arraySizeMax;

        for(int j = 0; j < length; j++){
            for(int i=0;i<lengthBase;i++){
                result = 31 * result + (int) (Double.doubleToLongBits(array[j][i]) | (Double.doubleToLongBits(array[j][i]) >>> 32));
            }
        }
        return (int)result*31;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        ArrayNode node  = new ArrayNode(arraySizeMax, lengthBase, array);
        node.next = next;
        node.previous = previous;
        node.arrayHashcode = arrayHashcode;
        node.shiftB = shiftB;
        node.updateHashCode = updateHashCode;
        return node;
    }
}

