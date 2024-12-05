package Сlassification;

public class Dataset {
    private int arraySizeMax = 10;//Сколько по стандарту должен иметь размер 1 массив.
    private int coountReserveOperation = 5; //Сколько оставлять пустых операций, для последующих операций.

    private int countOptionCombination;//Количество комбинаций значений. Варианты комбинирования значений.
    private int[] maskOptionValue;//Количество различных значений на каждое действие. 1 - константа 2 - включить выключить
    public int countParam;//Количество параметров. Первая часть данных. Входные данные
    public int countOption;//Количество значений. Условная вторая часть данных. Сколько есть доступных действий
    private int lengthBase;//Количество ячеек для хранения операции.

    private ArrayOperationsNode head;//Первый элемент списка
    private ArrayOperationsNode tail;//Последний элемент списка
    private ArrayOperationsNode lastUsed;
    private int lastUsedIndexStart;
    private int lastUsedIndexEnd;
    private int length;//Количество хранящихся операций
    private int lengthList;//Количество активированных массивов для хранения.
    public Dataset(){

    }
    /**Класс занимается хранением данных операций.
     * Данные хранятся в виде:
     * [{параметр1, параметр2, ..., countParam}
     * {сумма комбинации1, положительная часть комбинации1, сумма комбинации 2, положительная часть комбинации2, ..., countOptionCombination}]
     * int[] maskOptionValue - дает информацию о количестве различных значений на каждое действий.
     * После создания данные должны быть загружены.*/
    public Dataset(int countParam, int[] maskOptionValue){
        this.countParam = countParam;
        countOption = maskOptionValue.length;
        this.maskOptionValue = maskOptionValue.clone();
        countOptionCombination = getMaxCountOptionCombination(maskOptionValue);
        lengthBase = countParam + countOptionCombination*2;
    }
    int getMaxCountOptionCombination(int[] maskOptionValue){
        int count = 1;
        for(int i = 0; i < maskOptionValue.length; i++){
            count *= maskOptionValue[i];
        }
        return  count;
    }
    void addOperationByIndex(){

    }
    public void addOperation(double[][] array){
        if(array == null)
            throw new NullPointerException("Массив данных пустой");
        if(array[0].length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        ArrayOperationsNode node;
        if(lengthList == 0){//Если не инициализирован, то добавляем новую область
            addNewNode(arraySizeMax);
            node = searchNodeByIndex(0);
        }else {
            node = searchNodeByIndex(length);//???????????
        }
        for(int i = 0; i < array.length; i++){
            if(node.getLength() >= node.getArraySizeMax()) {
                addNewNode(arraySizeMax);
                node = searchNodeByIndex(i);
            }
            node.addOperation(array[i]);
            length++;
            lastUsedIndexEnd++;
        }
    }
    void deleteOperationByIndex(){

    }
    void getOperationByIndex(int index){

    }
    void searchOperation(){

    }
    /** Найти область ответственную за операцию по номеру*/
    ArrayOperationsNode searchNodeByIndex(int index){
        if(index > length || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList <= 0)
            throw new ArrayIndexOutOfBoundsException("Область памяти не создана");
        if(lengthList == 1) {
            lastUsedIndexStart = 0;
            lastUsedIndexEnd = head.getLength();
            lastUsed = head;
            return head;
        }
        if(index >= lastUsedIndexStart && index < lastUsedIndexEnd)
            return lastUsed;
        ArrayOperationsNode node;
        int indexStart, indexEnd; //Начало и конец изучаемой области
        if(index > length / 2) {
            node = tail;
            indexStart = length - node.getLength();
            indexEnd = length;
        }else{
            node = head;
            indexStart = 0;
            indexEnd = node.getLength();
        }
        for(int i = 0; i < lengthList; i++){
            if(index > indexEnd){
                node = node.next;
                indexStart += node.getLength();
                indexEnd += node.getLength();
            }else{
                if(index < indexStart){
                    node = node.previous;
                    indexStart -= head.getLength();
                    indexEnd -=  head.getLength();
                }else{
                    lastUsedIndexStart = indexStart;
                    lastUsedIndexEnd = indexEnd;
                    lastUsed = node;
                    return node;
                }
            }
        }
        throw new IllegalArgumentException("Произошла не предвиденная ошибка. Операция не найдена");
    }
    ArrayOperationsNode addNewNode(int arraySizeMax){
        ArrayOperationsNode  node =  new ArrayOperationsNode(arraySizeMax);
        if(lengthList == 0) {
            head = node;
            node.previous = node;
        }else{
            node.previous = tail;
            tail.next = node;
        }
        tail = node;
        node.next = node;
        lengthList += 1;
        return node;
    }

    /**Класс занимается хранением данных об массиве из операций.
     * На основе класса реализуется связанный список
     * Нужен для постепенного заполнения информации*/
    class ArrayOperationsNode{
        public ArrayOperationsNode next;
        public ArrayOperationsNode previous;
        private double[][] array;
        // [{параметр1, параметр2, ...}{сумма комбинации1, положительная часть комбинации1, сумма комбинации 2, положительная часть комбинации2}]
        private int length;//Количество активных ячеек
        private int lengthBase;//Количество ячеек для хранения операции.
        private int arraySizeMax;//Количество сколько ячеек может использоваться максимально
        private int countParamThis;//Количество параметров. Первая часть данных. Входные данные
        private int countOptionThis;//Количество значений. Условная вторая часть данных. Сколько есть доступных действий
        private int countOptionCombinationThis;//Количество комбинаций значений. Варианты комбинирования значений .
        ArrayOperationsNode(){

        }

        ArrayOperationsNode(int arraySizeMax){
            if(arraySizeMax < 0)
                throw new IllegalArgumentException("Размер массива должен быть больше 0");
            this.arraySizeMax = arraySizeMax;
            array  =  new double[arraySizeMax][countParam+countOptionCombination*2];
            length = 0;
            lengthBase = countParam+countOptionCombination*2;
        }
        ArrayOperationsNode(int arraySizeMax, double[][] array){
            if(arraySizeMax < 0)
                throw new IllegalArgumentException("Размер массива должен быть больше 0");
            if(array.length > arraySizeMax)
                throw new IllegalArgumentException("Массив не может быть больше чем: "+arraySizeMax);
            this.arraySizeMax = arraySizeMax;
            this.array = new double[arraySizeMax][countParam+countOptionCombination*2];
            lengthBase = countParam+countOptionCombination*2;
            for(int j = 0; j < array.length; j++) {
                for (int i = 0; i < array[0].length; i++) {
                    this.array[j][i] = array[j][i];
                }
            }
            length=array.length;
        }

        double[] getOperationByIndex(int index){
            if(index >= arraySizeMax || index < 0)
                throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
            return array[index];
        }
        double[] setOperationByIndex(int index, double[] operation){
            if(index >= arraySizeMax || index < 0)
                throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
            if(array[0].length > lengthBase)
                throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
            for(int i = 0; i < operation.length; i++){
                array[index][i] = operation[i];
            }
            return  array[index];
        }

        double[][] getArray(){return array;}
        double[][] setArray(double[][] array){
            if(array.length > arraySizeMax)
                throw new IllegalArgumentException("Массив не может быть больше чем: "+arraySizeMax);
            if(array[0].length > lengthBase)
                throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
            for(int j = 0; j < array.length; j++) {
                for (int i = 0; i < array[0].length; i++) {
                    array[j][i] = array[j][i];
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

        double[] addOperationByIndex(int index, double[] operation){
            if(index >= arraySizeMax || index < 0)
                throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
            if(length >= arraySizeMax)
                throw new ArrayIndexOutOfBoundsException("Превышен лимит по памяти в массиве. Установленный максимум = "+length);
            if(array[0].length > lengthBase)
                throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
            for (int j = length - 1; j > index; j--){
                for (int i = 0; i < operation.length; i++) {
                    array[j+1][i] = array[j][i];
                }
            }
            array[index] = operation;
            length++;
            return array[index];
        }
        double[] addOperation(double[] operation){
            if(length >= arraySizeMax)
                throw new ArrayIndexOutOfBoundsException("Превышен лимит по памяти в массиве. Установленный максимум = "+length);
            if(array[0].length > lengthBase)
                throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
            array[length] = operation;
            length++;
            return operation;
        }
        double[] deleteOperationByIndex(int index){
            if(index >= arraySizeMax || index < 0)
                throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
            double[] operation = array[index];
            for (int i = index; i < length; i++){
                array[i] = array[i+1];
            }
            length--;
            return operation;
        }
        @Override
        public int hashCode() {
            long result = 31;
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
            ArrayOperationsNode node  = new ArrayOperationsNode(arraySizeMax,array);
            node.next = next;
            node.previous = previous;
            return node;
        }
    }
}

