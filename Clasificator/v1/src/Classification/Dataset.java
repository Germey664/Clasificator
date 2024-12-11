package Classification;

public class Dataset {
    private int arraySizeMax = 10;//Сколько по стандарту должен иметь размер 1 массив.
    private int coountReserveOperation = 2; //Сколько оставлять пустых операций, для последующих операций.

    private int length;//Количество хранящихся операций
    private int lengthList;//Количество активированных массивов для хранения.
    private int lengthBase;//Количество ячеек для хранения операции.
    private ArrayNode head;//Первый элемент списка
    private ArrayNode tail;//Последний элемент списка

    private ArrayNode lastUsed;//Последний обновленный элемент. Использование может ускорить работу
    private int lastUsedIndexStart;
    private int lastUsedIndexEnd;
    private int lastUsedIndex;

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

    public ArrayNode getNodeByIndex(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        ArrayNode node;
        if (lengthList== 0)
            throw new NullPointerException("Массив не инициализирован");
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

    public ArrayNode addNodeToTail(){
        ArrayNode  node =  new ArrayNode(arraySizeMax,lengthBase);
        return addNodeToTail(node);
    }
    public ArrayNode addNodeToTail(int arraySizeMax, int lengthBase){
        if(arraySizeMax < 0)
            throw new IllegalArgumentException("ArraySizeMax не может быть отрицательным");
        if(lengthBase < 0)
            throw new IllegalArgumentException("LengthBase не может быть отрицательным");
        ArrayNode  node =  new ArrayNode(arraySizeMax,lengthBase);
        return addNodeToTail(node);
    }
    public ArrayNode addNodeToTail(ArrayNode node){
        if(node == null)
            throw new NullPointerException("Массив не инициализирован");
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
        length+=node.getLength();
        return node;
    }

    public ArrayNode addNodeByIndex(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList == 0)
            return addNodeToTail();
        ArrayNode node = new ArrayNode(arraySizeMax,lengthBase);
        return addNodeByIndex(index,node);
    }
    public ArrayNode addNodeByIndex(int index, int arraySizeMax, int lengthBase){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList == 0)
            return addNodeToTail();
        if(arraySizeMax < 0)
            throw new IllegalArgumentException("ArraySizeMax не может быть отрицательным");
        if(lengthBase < 0)
            throw new IllegalArgumentException("LengthBase не может быть отрицательным");
        ArrayNode node = new ArrayNode(arraySizeMax,lengthBase);
        return addNodeByIndex(index,node);
    }
    public ArrayNode addNodeByIndex(int index, ArrayNode  nodeNew){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList == 0 || index == lengthList)
            return addNodeToTail(nodeNew);
        ArrayNode nodeLast = getNodeByIndex(index);
        if (index == 0){
            head = nodeNew;
            nodeNew.previous = nodeNew;
        }else {
            nodeNew.previous = nodeLast.previous;
            nodeNew.previous.next = nodeNew;
        }
        nodeNew.next = nodeLast;
        nodeLast.previous = nodeNew;
        lengthList++;
        length+=nodeNew.getLength();
        return  nodeNew;
    }

    public ArrayNode setNodeByIndex(int index, ArrayNode nodeNew){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList <= 0)
            throw new NullPointerException("Массив не инициализирован");
        ArrayNode nodeLast = getNodeByIndex(index);
        length-=nodeLast.getLength();
        length+=nodeNew.getLength();
        if(lengthList == 1){
            head = nodeNew;
            tail = nodeNew;
            nodeNew.previous = nodeNew;
            nodeNew.next = nodeNew;
            return nodeNew;
        }
        if (index == 0){
            nodeNew.previous = nodeNew;
            head = nodeNew;
            nodeNew.next =   nodeLast.next;
            nodeNew.next.previous = nodeNew;
            return nodeNew;
        }
        if (index == lengthList){
            nodeNew.next = nodeNew;
            tail = nodeNew;
            nodeNew.previous = nodeLast.previous;
            nodeNew.previous.next = nodeNew;
            return nodeNew;
        }
        nodeNew.next = nodeLast.next;
        nodeNew.previous = nodeLast.previous;
        nodeNew.next.previous = nodeNew;
        nodeNew.previous.next = nodeNew;
        return nodeNew;
    }

    public ArrayNode deleteNodeByIndex(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList <= 0)
            return null;
        ArrayNode nodeLast = getNodeByIndex(index);
        lengthList--;
        length-=nodeLast.getLength();
        if (lengthList == 1) return head;
        if (lengthList<0)lengthList=0;

        if (index == 0){
            head = nodeLast.next;
            nodeLast.next.previous =  nodeLast.next;
        } else{
            nodeLast.next.previous =  nodeLast.previous;
        }
        if (index == lengthList) {
            tail = nodeLast.previous;
            nodeLast.previous.next = nodeLast.previous;
        }else
            nodeLast.previous.next = nodeLast.next;
        return nodeLast;
    }

    public int searchIndexByNode(ArrayNode node) throws ArrayIndexOutOfBoundsException{
        if(node == null)
            throw new NullPointerException("Массив не инициализирован");
        if(lengthList <= 0)
            throw new NullPointerException("Массив не инициализирован");
        ArrayNode iNode = head;
        int i;
        for (i = 0; i < lengthList; i++){
            if(node == iNode)
                return i;
            iNode = iNode.next;
        }
            throw new ArrayIndexOutOfBoundsException("Не удалось найти объект в списке");
    }

    void initLastUsed(ArrayNode node, int start){
        lastUsed = node;
        lastUsedIndexStart = start;
        lastUsedIndexEnd = start + node.getLength();
    }
    void initLastUsed(ArrayNode node){

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




    int getMaxCountOptionCombination(int[] maskOptionValue){
        int count = 1;
        for(int i = 0; i < maskOptionValue.length; i++){
            count *= maskOptionValue[i];
        }
        return  count;
    }

    public String toString() {
        String s =  "Length list: "+lengthList+", length: "+length+", length base: "+lengthBase+"\n";
        s+=         "Array max size: "+arraySizeMax+", count reserve operation: "+coountReserveOperation+"\n";
        s+=         "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n";
        ArrayNode node = head;
        for (int i = 0; i < lengthList; i++) {
            s +=  node;
            s+="------------------------------------------------------\n";
            node = node.next;
        }
        s+=         "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n";
        return s;
    }
}

