package Classification;

import static java.lang.Math.abs;

public class Dataset {
    private int arraySizeMax = 10;//Сколько по стандарту должен иметь размер 1 массив.
    private int countReserveOperation = 2; //Сколько оставлять пустых операций, для последующих операций.

    private int length;//Количество хранящихся операций
    private int lengthList;//Количество активированных массивов для хранения.
    private int lengthBase;//Количество ячеек для хранения одной операции.
    private ArrayNode head;//Первый элемент списка
    private ArrayNode tail;//Последний элемент списка

    private boolean lastUsedActivate = true;//Нужно ли использовать область памяти lastUsed. Она может работать не стабильно.
    private ArrayNode lastUsed;//Последний использованный элемент. Применяется в ускорении поиска элементов.
    private boolean lastUsedCorrect = false;//Если данные не надежны, а изменить их на корректные не получается, то нужно поставить false
    private int lastUsedIndexStart;//Используется для поиска операций
    private int lastUsedIndex;//Используется для поиска индексов областей

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

    /** Короткая запись. Заполняет параметры lastUsed переданными параметрами.*/
    void initLastUsed(ArrayNode node, int start, int index){
        if(!lastUsedActivate)
            return;
        lastUsed = node;
        lastUsedIndex = index;
        lastUsedIndexStart = start;
        lastUsedCorrect = true;
    }

    /** Ищет индекс ближайшей области среди областей head, tail, lastUsed*/
    public int searchNearbyIndexByNode(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if (lengthList== 0)
            throw new NullPointerException("Массив не инициализирован");
        if(lastUsedCorrect && index == lastUsedIndex){
            return lastUsedIndex;
        }
        int indexDistHead = index;
        int indexDistTail = lengthList - 1 - index;
        int indexDistLast = abs(lastUsedIndex - index);
        if(!lastUsedCorrect)
            indexDistLast = 999999999;
        if(indexDistHead <= indexDistLast && indexDistHead <= indexDistTail){
            return  0;
        }
        if(indexDistTail <= indexDistLast && indexDistTail < indexDistHead){
            return lengthList - 1;
        }
        if(indexDistLast < indexDistHead && indexDistLast < indexDistTail){
            return lastUsedIndex;
        }
        return -1;
    }
    /** Ищет индекс первого действия ближайшей области среди областей head, tail, lastUsed*/
    public int searchNearbyIndexStartByNode(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if (lengthList== 0)
            throw new NullPointerException("Массив не инициализирован");
        if(lastUsedCorrect && index == lastUsedIndex){
            return lastUsedIndexStart;
        }
        int indexDistHead = index;
        int indexDistTail = lengthList - 1 - index;
        int indexDistLast = abs(lastUsedIndex - index);
        if(!lastUsedCorrect)
            indexDistLast = 999999999;
        if(indexDistHead <= indexDistLast && indexDistHead <= indexDistTail){
            return  0;
        }
        if(indexDistTail <= indexDistLast && indexDistTail < indexDistHead){
            return length - tail.getLength();
        }
        if(indexDistLast < indexDistHead && indexDistLast < indexDistTail){
            return lastUsedIndexStart;
        }
        return -1;
    }
    /** Возвращает область: head, tail, lastUsed от индекса 0, lengthList - 1, ?*/
    public ArrayNode searchNearbyNodeByItIndex(int index){
        if(index == 0)
            return head;
        else if(index == lengthList - 1)
            return tail;
        else
            return lastUsed;
    }
    /** Получить область по её индексу. Использует lastUsed*/
    public ArrayNode getNodeByIndex(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if (lengthList== 0)
            throw new NullPointerException("Массив не инициализирован");

        if(lastUsedCorrect && index == lastUsedIndex){
            return lastUsed;
        }

        int indexNodeStart = searchNearbyIndexByNode(index);
        int indexStart = searchNearbyIndexStartByNode(index);
        ArrayNode node = searchNearbyNodeByItIndex(indexNodeStart);
        if(index > indexNodeStart){
            for(int i = indexNodeStart; i < index; i++){
                indexStart += node.getLength();
                node = node.next;
            }
        }else{
            for (int i = indexNodeStart; i > index; i--) {
                indexStart -= node.getLength();
                node = node.previous;
            }
        }
        initLastUsed(node,indexStart,index);
        return node;
    }
    /** Возвращает индекс в списке для переданной области (не пустая). Если область не найдена создает исключение*/
    public int searchIndexByNode(ArrayNode node) throws ArrayIndexOutOfBoundsException{
        if(node == null)
            throw new NullPointerException("Массив не инициализирован");
        if(lengthList <= 0)
            throw new NullPointerException("Массив не инициализирован");
        if(lastUsedCorrect && node == lastUsed)
            return lastUsedIndex;
        ArrayNode iNode = head;
        int i;
        for (i = 0; i < lengthList; i++){
            if(node == iNode)
                return i;
            iNode = iNode.next;
        }
        throw new ArrayIndexOutOfBoundsException("Не удалось найти объект в списке");
    }

    /** Добавить пустую область со стандартными размерами в конец списка. Возвращает добавленную область*/
    public ArrayNode addNodeToTail(){
        ArrayNode  node =  new ArrayNode(arraySizeMax,lengthBase);
        return addNodeToTail(node);
    }
    /** Добавить пустую область с нестандартными размерами в конец списка. Возвращает добавленную область*/
    public ArrayNode addNodeToTail(int arraySizeMax, int lengthBase){
        if(arraySizeMax < 0)
            throw new IllegalArgumentException("ArraySizeMax не может быть отрицательным");
        if(lengthBase < 0)
            throw new IllegalArgumentException("LengthBase не может быть отрицательным");
        ArrayNode  node =  new ArrayNode(arraySizeMax,lengthBase);
        return addNodeToTail(node);
    }
    /** Добавить переданную область в конец списка (не пустая). Возвращает добавленную область*/
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

    /** Добавить пустую область со стандартными размерами в список по индексу. Возвращает добавленную область*/
    public ArrayNode addNodeByIndex(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList == 0)
            return addNodeToTail();
        ArrayNode node = new ArrayNode(arraySizeMax,lengthBase);
        return addNodeByIndex(index,node);
    }
    /** Добавить пустую область с нестандартными размерами в список по индексу. Возвращает добавленную область*/
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
    /** Добавить переданную область в список по индексу (не пустая). Возвращает добавленную область*/
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
        if(lastUsedActivate && lastUsedCorrect){
            if(index < lastUsedIndex+1){
                lastUsedIndex++;
                lastUsedIndexStart+=nodeNew.getLength();
            }
        }
        return  nodeNew;
    }

    /** Изменяет область в списке по индексу на переданную область (не пустая). Возвращает переданную область*/
    public ArrayNode setNodeByIndex(int index, ArrayNode nodeNew){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList == 0)
            throw new NullPointerException("Массив не инициализирован");
        ArrayNode nodeLast = getNodeByIndex(index);
        length-=nodeLast.getLength();
        length+=nodeNew.getLength();
        if(lastUsedActivate && lastUsedCorrect){
            if(index < lastUsedIndex){
                lastUsedIndexStart-=nodeLast.getLength();
                lastUsedIndexStart+=nodeNew.getLength();
            }
            if(index == lastUsedIndex)
                lastUsed = nodeNew;
        }
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
    /** Удаляет область из списка по индексу. Возвращает удаленную область*/
    public ArrayNode deleteNodeByIndex(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList == 0)
            return null;
        ArrayNode nodeLast = getNodeByIndex(index);
        lengthList--;
        length-=nodeLast.getLength();
        if(lastUsedActivate && lastUsedCorrect){
            if(index < lastUsedIndex){
                lastUsedIndex--;
                lastUsedIndexStart-=nodeLast.getLength();
            }
            if(index == lastUsedIndex)
                lastUsedCorrect = false;
        }
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


    /** Ищет индекс ближайшей области среди областей head, tail, lastUsed*/
    public int searchNearbyIndex(int indexElement){
        if(indexElement > length || indexElement < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+indexElement+"]");
        if (lengthList== 0)
            throw new NullPointerException("Массив не инициализирован");
        if(lastUsedCorrect && indexElement == lastUsedIndexStart){
            return lastUsedIndex;
        }
        int indexDistHead = indexElement;
        int indexDistTail = length - indexElement;
        int indexDistLast = abs(lastUsedIndex - indexElement);
        if(!lastUsedCorrect)
            indexDistLast = 999999999;
        if(indexDistHead <= indexDistLast && indexDistHead <= indexDistTail){
            return  0;
        }
        if(indexDistTail <= indexDistLast && indexDistTail < indexDistHead){
            return lengthList - 1;
        }
        if(indexDistLast < indexDistHead && indexDistLast < indexDistTail){
            return lastUsedIndex;
        }
        return -1;
    }
    /** Ищет индекс первого действия ближайшей области среди областей head, tail, lastUsed*/
    public int searchNearbyIndexStart(int indexElement){
        if(indexElement > length || indexElement < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+indexElement+"]");
        if (lengthList== 0)
            throw new NullPointerException("Массив не инициализирован");
        if(lastUsedCorrect && indexElement == lastUsedIndexStart){
            return lastUsedIndexStart;
        }
        int indexDistHead = indexElement;
        int indexDistTail = length - 1 - indexElement;
        int indexDistLast = abs(lastUsedIndex - indexElement);
        if(!lastUsedCorrect)
            indexDistLast = 999999999;
        if(indexDistHead <= indexDistLast && indexDistHead <= indexDistTail){
            return  0;
        }
        if(indexDistTail <= indexDistLast && indexDistTail < indexDistHead){
            return length - tail.getLength();
        }
        if(indexDistLast < indexDistHead && indexDistLast < indexDistTail){
            return lastUsedIndexStart;
        }
        return -1;
    }
    /** Получить индекс первой операции для области по общему массиву. Передается индекс области. Использует lastUsed*/
    public int getIndexStartByNode(int index){
        if(index > lengthList || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if (lengthList== 0)
            throw new NullPointerException("Массив не инициализирован");
        if(lastUsedCorrect && index == lastUsedIndex)
            return lastUsedIndexStart;

        int indexNodeStart = searchNearbyIndexByNode(index);
        int indexStart = searchNearbyIndexStartByNode(index);
        ArrayNode node = searchNearbyNodeByItIndex(indexNodeStart);
        if(index > indexNodeStart){
            for(int i = indexNodeStart; i < index; i++){
                indexStart += node.getLength();
                node = node.next;
            }
        }else{
            for (int i = indexNodeStart; i > index; i--) {
                indexStart -= node.getLength();
                node = node.previous;
            }
        }
        initLastUsed(node,indexStart,index);
        return indexStart;
    }
    /** Найти область в которой находится операция по индексу.*/
    public ArrayNode searchNodeByIndexElement(int index){
        if(index > length || index < 0)
            throw new ArrayIndexOutOfBoundsException("Выход за пределы массива index["+index+"]");
        if(lengthList <= 0)
            throw new NullPointerException("Массив не инициализирован");
        if(lastUsedCorrect)
            if(index >= lastUsedIndexStart && (index < lastUsedIndexStart+lastUsed.getLength()))
                return lastUsed;
        int indexNodeStart = searchNearbyIndex(index);
        int indexStart = searchNearbyIndexStart(index);
        ArrayNode node = searchNearbyNodeByItIndex(indexNodeStart);
        if(index > indexStart){
            indexStart+=node.getLength();
            for(; indexStart + node.getLength() < index+1; indexStart+=node.getLength()){
                indexNodeStart++;
                node = node.next;
            }
        }else{
            for (int i = indexStart; i > index; i-=node.getLength()) {
                node = node.previous;
                indexNodeStart--;
            }
        }
        initLastUsed(node,indexStart,indexNodeStart);
        return node;
    }

    int getIndexStartByIndexNode(int index){
        return 0;
    }

    void getOperationByIndex(int index){

    }
    void setOperationByIndex(){

    }
    void addOperationByIndex(){

    }
    void deleteOperationByIndex(){

    }
    /**Функция добавляет данные в конец. Сразу создает новые ноды*/
    /*public void addOperationsToTail(double[][] array){
        if(array == null)
            throw new NullPointerException("Массив данных пустой");
        if(array[0].length > lengthBase)
            throw new IllegalArgumentException("Количество параметров не может быть больше чем: "+lengthBase);
        ArrayNode node = addNodeToTail(arraySizeMax,lengthBase);
        for(int i = 0; i < array.length; i++){
            if(node.getLength() >= node.getArraySizeMax() - countReserveOperation) {
                node = addNodeToTail(arraySizeMax, lengthBase);
            }
            node.addElementToTail(array[i]);
            length++;
            lastUsedIndexEnd++;
        }
    }
    public void addOperations(int indexStart, double[][] array){
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
            if(node.getLength() >= node.getArraySizeMax() - countReserveOperation) {
                node = addNodeToTail(arraySizeMax, lengthBase);
            }
            node.addElementToTail(array[i]);
            length++;
            lastUsedIndexEnd++;
        }
    }
*/
    public double[][] getOperations(int indexStart, int indexEnd){
        return null;
    }
    public double[][] deleteOperations(int indexStart, int indexEnd){
        return null;
    }
    public double[][] setOperations(int indexStart, double[][] array){
        return null;
    }


    void searchOperation(){

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
        s+=         "Array max size: "+arraySizeMax+", count reserve operation: "+ countReserveOperation +"\n";
        s+=         "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n";
        ArrayNode node = head;
        for (int i = 0; i < lengthList; i++) {
            s += "Node: "+ i +"\n";
            s +=  node;
            s +="------------------------------------------------------\n";
            node = node.next;
        }
        s+=         "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n";
        return s;
    }
}

