package Сlassification;

public class Dataset {
    private int arraySizeMax = 10;//Сколько по стандарту должен иметь размер 1 массив.
    private int coountReserveOperation = 5; //Сколько оставлять пустых операций, для последующих операций.
    private int length;//Количество хранящихся операций
    private int lengthList;//Количество активированных массивов для хранения.
    private int lengthBase;//Количество ячеек для хранения операции.
    private ArrayNode head;//Первый элемент списка
    private ArrayNode tail;//Последний элемент списка
    private ArrayNode lastUsed;
    private int lastUsedIndexStart;
    private int lastUsedIndexEnd;

    private int countOptionCombination;//Количество комбинаций значений. Варианты комбинирования значений.
    private int[] maskOptionValue;//Количество различных значений на каждое действие. 1 - константа 2 - включить выключить
    public int countParam;//Количество параметров. Первая часть данных. Входные данные
    public int countOption;//Количество значений. Условная вторая часть данных. Сколько есть доступных действий

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
    void addOperationByIndex(){

    }
    public void addOperation(double[][] array){
        if(array == null)
            throw new NullPointerException("Массив данных пустой");
        if(array[0].length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        ArrayNode node;
        if(lengthList == 0){//Если не инициализирован, то добавляем новую область и подгружаем ее
            addNewNode(arraySizeMax,lengthBase);
            node = searchNodeByIndexElement(0);
        }else {//Если уже есть доступные области, то ишим подходящую
            addNewNode(arraySizeMax,lengthBase);//???????????
            node = searchNodeByIndexElement(length);
        }
        for(int i = 0; i < array.length; i++){
            if(node.getLength() >= node.getArraySizeMax() - coountReserveOperation) {
                addNewNode(arraySizeMax,lengthBase);
                node = searchNodeByIndexElement(length);
            }
            node.addElementToTail(array[i]);
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
    ArrayNode searchNodeByIndexElement(int index){
        if(index > length || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList <= 0)
            throw new ArrayIndexOutOfBoundsException("Область памяти не создана");
        if(lengthList == 1) {//Создан только 1
            lastUsedIndexStart = 0;
            lastUsedIndexEnd = head.getLength();
            lastUsed = head;
            return head;
        }
        if(index >= lastUsedIndexStart && index < lastUsedIndexEnd)//Тот же что и в прошлый раз
            return lastUsed;
        if(index == lastUsedIndexEnd || (index >= lastUsedIndexEnd && index < lastUsedIndexEnd + lastUsed.next.getLength()))
            return lastUsed.next;
        ArrayNode node;
        int indexStart, indexEnd; //Начало и конец изучаемой области
        if(index > length / 2) {
            node = tail;
            indexEnd = length;
            indexStart = indexEnd - node.length;

        }else{
            node = head;
            indexStart = 0;
            indexEnd = node.getLength();
        }
        for(int i = 0; i < lengthList; i++){
            if(index >= indexEnd && indexEnd != indexStart){//идем вперед
                node = node.next;
                indexStart = indexEnd;
                indexEnd = indexStart + node.getLength();
            }else{
                if(index >= indexStart && (index < indexEnd || indexEnd == indexStart)){//Это текущий элемент
                    lastUsedIndexStart = indexStart;
                    lastUsedIndexEnd = indexEnd;
                    lastUsed = node;
                    return node;
                }else{//идем назад
                    node = node.previous;
                    indexEnd = indexStart;
                    indexStart = indexStart - node.length;
                }
            }
        }
        throw new IllegalArgumentException("Не удалось найти список который соответствовал бы этому индексу");
    }
    ArrayNode addNewNode(int arraySizeMax,int lengthBase){
        ArrayNode  node =  new ArrayNode(arraySizeMax,lengthBase);
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
    ArrayNode getNodeByIndex(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        ArrayNode node;
        if(index > lengthList / 2){
            node = tail;
            for(int i = lengthList-1; i > index; i--){
                node = node.previous;
            }
        }else{
            node = head;
            for(int i = 0; i < index; i++){
                node = node.next;
            }
        }
        return node;

    }
    ArrayNode addNewNode2(int arraySizeMax,int lengthBase, int index){
        ArrayNode  node =  new ArrayNode(arraySizeMax,lengthBase);
        return node;
    }

    int getMaxCountOptionCombination(int[] maskOptionValue){
        int count = 1;
        for(int i = 0; i < maskOptionValue.length; i++){
            count *= maskOptionValue[i];
        }
        return  count;
    }
    /**Класс занимается хранением данных об массиве из операций.
     * На основе класса реализуется связанный список
     * Нужен для постепенного заполнения информации*/
    class ArrayNode{
        public ArrayNode next;
        public ArrayNode previous;
        protected double[][] array;
        protected int length;//Количество активных ячеек
        protected int lengthBase;//Количество ячеек для хранения операции.
        protected int arraySizeMax;//Количество сколько ячеек может использоваться максимально

        protected int arrayHashcode = 0;//Нэш код. После каждого обновления должен отличатся от прошлого за счет изменения shiftB
        protected boolean updateHashCode = false;//Переменная отвечает за сверку значений хеш кода. Должна быть true после пересчета и false кода приемник проверил
        protected double shiftB = 0;

        ArrayNode(){

        }
        ArrayNode(int arraySizeMax, int lengthBase){
            if(arraySizeMax < 0)
                throw new IllegalArgumentException("Размер массива должен быть больше 0");
            this.arraySizeMax = arraySizeMax;
            array  =  new double[arraySizeMax][lengthBase];
            length = 0;
            this.lengthBase = lengthBase;
        }
        ArrayNode(int arraySizeMax, int lengthBase, double[][] array){
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

        double[] getElementByIndex(int index){
            if(array == null)
                throw new ArrayIndexOutOfBoundsException("Массив не создан");
            if(index >= arraySizeMax || index < 0)
                throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
            return array[index];
        }
        double[] setElementByIndex(int index, double[] operation){
            if(array == null)
                throw new ArrayIndexOutOfBoundsException("Массив не создан");
            if(index >= arraySizeMax || index < 0)
                throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
            if(array[0].length > lengthBase)
                throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
            for(int i = 0; i < operation.length; i++){
                array[index][i] = operation[i];
            }
            return  array[index];
        }

        double[][] getArray(){
            if(array == null)
                throw new ArrayIndexOutOfBoundsException("Массив не создан");
            return array;
        }
        double[][] setArray(double[][] array){
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

        double[] addElementByIndex(int index, double[] operation){
            if(array == null)
                throw new ArrayIndexOutOfBoundsException("Массив не создан");
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
            return this.array[index];
        }
        double[] addElementToTail(double[] operation){
            if(array == null)
                throw new ArrayIndexOutOfBoundsException("Массив не создан");
            if(length >= arraySizeMax)
                throw new ArrayIndexOutOfBoundsException("Превышен лимит по памяти в массиве. Установленный максимум = "+length);
            if(array[0].length > lengthBase)
                throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
            array[length] = operation;
            length++;
            return operation;
        }
        double[] deleteElementByIndex(int index){
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

    /**Класс занимается хранением данных об массиве из операций.
     * Наследуется от ArrayNode.
     * Описывает методы для работы с конкретным типом представления данных
     */
    class ArrayOperationsNode extends ArrayNode{
        private int countParamThis;//Количество параметров. Первая часть данных. Входные данные
        private int countOptionThis;//Количество значений. Условная вторая часть данных. Сколько есть доступных действий
        private int countOptionCombinationThis;//Количество комбинаций значений. Варианты комбинирования значений .

        ArrayOperationsNode(){
            super();
        }
        ArrayOperationsNode(int arraySizeMax){
            super(arraySizeMax, countParam+countOptionCombination*2);
        }
        ArrayOperationsNode(int arraySizeMax, double[][] array){
            super(arraySizeMax, countParam+countOptionCombination*2,array);
        }
    }
}

