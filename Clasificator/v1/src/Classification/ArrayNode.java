package Classification;

/**Класс занимается хранением данных об массиве из операций.
 * На основе класса реализуется связанный список.
 * Нужен для постепенного заполнения информации, с возможностью пересортировки.*/
public class ArrayNode{
    public ArrayNode next;//ссылка на следующий элемент списка
    public ArrayNode previous;//ссылка на предыдущий элемент списка

    protected double[][] array;
    protected int length;//Количество активных операций
    protected int lengthBase;//Количество ячеек для хранения одной операции.
    protected int arraySizeMax;//Количество сколько операций может быть записано максимально

    protected int arrayHashcode = 0;//Хэш код. После каждого обновления должен отличатся от прошлого за счет изменения shiftB
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
    public ArrayNode(int arraySizeMax, int length, int lengthBase, double[][] array){
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
        this.length=length;
    }

    public double[][] getArray(){return array;}
    /**Количество активных операций*/
    public int getLength() {return length;}
    /**Максимальное количество операций*/
    public int getArraySizeMax() {return arraySizeMax;}
    /**Количество ячеек для хранения одной операции.*/
    public int getLengthBase() {return lengthBase;}

    /** Добавляет одну операцию в конец массива и возвращает её. Операция должна соответствовать нормам. Если массив заполнен создает исключение*/
    public double[] addElementToTail(double[] operation) throws ArrayIndexOutOfBoundsException{
        if(array == null)
            throw new NullPointerException("Массив не создан");
        if(operation.length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        if(length >= arraySizeMax)
            throw new ArrayIndexOutOfBoundsException("Установлена максимальный размер. Массив не может быть больше чем: "+arraySizeMax);
        array[length] = operation;
        length++;
        return operation;
    }
    /** Удаляет одну операцию из конца массива и возвращает её.*/
    public double[] deleteElementToTail(){
        if(array == null)
            throw new NullPointerException("Массив не создан");
        if(length == 0)
            return null;
        double[] operation = new double[lengthBase];
        for(int i = 0; i < operation.length; i++)
            operation[i] = array[length-1][i];
        array[length-1] = new double[lengthBase];
        length = 0;
        return operation;
    }

    /** Получить одну операцию из массива по индексу.*/
    public double[] getElementByIndex(int index){
        if(array == null)
            throw new NullPointerException("Массив не создан");
        if(index >= length || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        return array[index];
    }
    /** Изменить одну операцию из массива по индексу на переданную операцию (не нулевая). Возвращает новую операцию из массива*/
    public double[] setElementByIndex(int index, double[] operation){
        if(array == null)
            throw new NullPointerException("Массив не создан");
        if(index >= length || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(operation.length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        for(int i = 0; i < operation.length; i++){
            array[index][i] = operation[i];
        }
        return  array[index];
    }
    /** Добавить переданную операцию в массив по индексу(не нулевая). Если массив заполнен создает исключение. Возвращает новую операцию из массива*/
    public double[] addElementByIndex(int index, double[] operation) throws ArrayIndexOutOfBoundsException{
        if(array == null)
            throw new NullPointerException("Массив не создан");
        if(operation == null)
            throw new NullPointerException("Операция не инициализированная");
        if(index >= length || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(operation.length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        if(length >= arraySizeMax)
            throw new ArrayIndexOutOfBoundsException("Установлена максимальный размер. Массив не может быть больше чем: "+arraySizeMax);
        for (int j = length - 1; j >= index; j--){
            for (int i = 0; i < operation.length; i++) {
                array[j+1][i] = array[j][i];
            }
        }
        array[index] = operation;
        length++;
        return this.array[index];
    }
    /** Изменить одну операцию из массива по индексу на переданную операцию (не нулевая). Возвращает копию удаленной операцию*/
    public double[] deleteElementByIndex(int index){
        if(array == null)
            throw new NullPointerException("Массив не создан");
        if(index > length || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if (length == 0)
            return null;
        double[] operation = array[index];
        for (int i = index; i < length; i++){
            array[i] = array[i+1];
        }
        length--;
        if (length<0)length=0;
        return operation;
    }

    /** Получить группу операций из массива по области (включительно)*/
    public double[][] getElements(int indexStart, int indexEnd){
        if(array == null)
            throw new NullPointerException("Массив не создан");
        if(indexStart >= length || indexStart < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+indexStart+"]");
        if(indexEnd+1 >= length || indexEnd+1 < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+indexEnd+"]");
        if(indexEnd - indexStart < 0 )
            throw new ArrayIndexOutOfBoundsException("Область для получения не может быть отрицательна");
        int diff = indexEnd - indexStart+1;
        double[][] arrayNew = new double[diff][lengthBase];
        for(int j = 0; j < diff; j++) {
            for (int i = 0; i < lengthBase; i++) {
                arrayNew[j][i] = array[j+indexStart][i];
            }
        }
        return arrayNew;
    }
    /** Изменить массив на переданный (не нулевой). Не больше максимального. Создает копий переданного массива. Возвращает ссылку на новый массив*/
    public double[][] setArray(double[][] array){
        if(array == null)
            throw new NullPointerException("Массив не инициализирован");
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
        return this.array;
    }
    /** Добавляет в массив группу переданных операций (не нулевых). Не больше максимумов. Если массив заполнен создает исключение. Возвращает ссылку на измененный массив*/
    public double[][] addElements(int indexStart, double[][] array) throws ArrayIndexOutOfBoundsException{
        if(this.array == null)
            throw new NullPointerException("Массив не создан");
        if(array == null)
            throw new NullPointerException("Операции не инициализированная");
        if(array[0].length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        if(length + array.length >= arraySizeMax)
            throw new ArrayIndexOutOfBoundsException("Установлена максимальный размер. Массив не может быть больше чем: "+arraySizeMax);
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
    /** Удалеят из массива группу операций из области (включительно).  Возвращает массив удаленных операций*/
    public double[][] deleteElements(int indexStart, int indexEnd){
        if(array == null)
            throw new NullPointerException("Массив не создан");
        if(indexStart > length || indexStart < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+indexStart+"]");
        if(indexEnd+1 > length || indexEnd+1 < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+indexEnd+"]");
        if(indexEnd - indexStart < 0 )
            throw new ArrayIndexOutOfBoundsException("Область для удаления не может быть отрицательна");
        int diff = indexEnd - indexStart+1;
        if (length == 0) return null;
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
        ArrayNode node  = new ArrayNode(arraySizeMax, length, lengthBase, array);
        node.next = next;
        node.previous = previous;
        node.arrayHashcode = arrayHashcode;
        node.shiftB = shiftB;
        node.updateHashCode = updateHashCode;
        return node;
    }
}

