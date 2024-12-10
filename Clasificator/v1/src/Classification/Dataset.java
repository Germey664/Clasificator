package Classification;

public class Dataset {
    private int arraySizeMax = 10;//Сколько по стандарту должен иметь размер 1 массив.
    private int coountReserveOperation = 2; //Сколько оставлять пустых операций, для последующих операций.
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
    void initLastUsed(ArrayNode node, int start){
        lastUsed = node;
        lastUsedIndexStart = start;
        lastUsedIndexEnd = start + node.getLength();
    }
    void addOperationByIndex(){

    }
    /**Функция добавляет данные в конец. Сразу создает новые ноды*/
    public void addOperation(double[][] array){
        if(array == null)
            throw new NullPointerException("Массив данных пустой");
        if(array[0].length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        ArrayNode node = addNodeToTail(arraySizeMax,lengthBase);
        for(int i = 0; i < array.length; i++){
            if(node.getLength() >= node.getArraySizeMax() - coountReserveOperation) {
                node = addNodeToTail(arraySizeMax, lengthBase);
            }
            node.addElementToTail(array[i]);
            length++;
            lastUsedIndexEnd++;
        }
    }
    public void addOperation(int indexStart, double[][] array){
        if(array == null)
            throw new NullPointerException("Массив данных пустой");
        if(array[0].length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        if (indexStart < 0 || indexStart > length)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+indexStart+"]");

        ArrayNode node = searchNodeByIndexElement(indexStart);
        if(node == null)
            node = addNodeToTail(arraySizeMax,lengthBase);
        for(int i = 0; i < array.length; i++){
            if(node.getLength() >= node.getArraySizeMax() - coountReserveOperation) {
                node = addNodeToTail(arraySizeMax, lengthBase);
                initLastUsed(node,length);
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
            return null;
        if(index >= lastUsedIndexStart && (index < lastUsedIndexEnd||lastUsedIndexStart == lastUsedIndexEnd))//Прошлый использованный подходит. Операции могут устанавливать это значение
            return lastUsed;
        ArrayNode node;
        int indexStart, indexEnd; //Начало и конец изучаемой области
        //Откуда будет начинать работу. От lastUsed, от head, от tail.
        if ((index - lastUsedIndexEnd <= (length/lengthList)) && (index - lastUsedIndexStart >= -(length/lengthList))) {//Проверяем область на близость lastUsed
            node = lastUsed;
            indexStart = lastUsedIndexStart;
            indexEnd = lastUsedIndexEnd;
        }else {
            if(index > length / 2) {
                node = tail;
                indexEnd = length;
                indexStart = indexEnd - node.length;
            }else{
                node = head;
                indexStart = 0;
                indexEnd = node.getLength();
            }
        }
        for(int i = 0; i < lengthList; i++){
            if(index >= indexEnd && indexEnd != indexStart){//идем вперед
                node = node.next;
                indexStart = indexEnd;
                indexEnd = indexStart + node.getLength();
            }else{
                if(index >= indexStart && (index < indexEnd || indexEnd == indexStart)){//Это текущий элемент
                    initLastUsed(node,indexStart);
                    return node;
                }else{//идем назад
                    node = node.previous;
                    indexEnd = indexStart;
                    indexStart = indexStart - node.length;
                }
            }
        }
        return null;
    }
    public ArrayNode addNodeToTail(int arraySizeMax, int lengthBase){
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
        initLastUsed(node,length);
        return node;
    }
    public ArrayNode getNodeByIndex(int index){
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


}

