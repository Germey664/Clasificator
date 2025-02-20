package Functions;


import java.io.Serializable;

/** Класс отвечает за создание аппроксимированной функции;
 * Точки хранятся в виде связанного списка;*/
public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable, Function {

    /**FunctionNode head - ссылка на головной элемент**/
    private FunctionNode head;
    /**FunctionNode tail - ссылка на хвостовой элемент**/
    private FunctionNode tail;
    private int countWorkPoints;

    /**
     leftX - левая граница области определения;
     rightX - правая граница области определения;
     pointsCount - количество точек функции
     **/
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        countWorkPoints = pointsCount;
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек (" + pointsCount + ") должно быть больше 1!");
        }
        if (Double.compare(leftX, rightX) >= 0) {
            throw new IllegalArgumentException("Правая граница области определения (" + rightX + ") должна быть больше левой (" + leftX + ")!");
        }
        // Инициализируем головной и хвостовой элементы списка
        head = new FunctionNode();
        head.previous = head;
        head.next = head;
        tail = head;

        double delta = (rightX + 1 - leftX) / pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            addNodeToTail().point =  new FunctionPoint(leftX + delta*i,0);
        }
    }

    /**
     leftX - левая граница области определения;
     rightX - правая граница области определения;
     values - значения функции
     **/
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if(values.length < 2)
            throw new IllegalArgumentException("Количество точек функции ("+values.length+") должно быть более 1!");
        if(Double.compare(leftX, rightX) >= 0)
            throw new IllegalArgumentException("Правая граница области определения ("+rightX+") должна быть больше, чем левая("+leftX+")");
        // Инициализируем головной и хвостовой элементы списка
        head = new FunctionNode();
        countWorkPoints++;
        head.previous = head;
        head.next = head;
        tail = head;
        head.point = new FunctionPoint(leftX, values[0]);
        double delta = (rightX - leftX) / (values.length - 1);
        for(int i = 1; i <  values.length; i++) {
            addNodeToTail().point = new FunctionPoint(leftX + delta * i, values[i]);
        }
    }
    /**
     values - массив точек функции с координатами x и  y
     **/
    public LinkedListTabulatedFunction(FunctionPoint[] values){
        if(values.length<2)
            throw new IllegalArgumentException("Количество точек функции ("+values.length+") должно быть более 1!");
        for(int i = 0; i < values.length-1;i++){
            if(Double.compare(values[i].getX(), values[i+1].getX()) > 0)
                throw new IllegalArgumentException("Точки не соответствуют своим индексам ");
        }

        head = new FunctionNode();
        countWorkPoints++;
        head.previous = head;
        head.next = head;
        tail = head;
        head.point = new FunctionPoint(values[0].getX(), values[0].getY());

        for(int i = 1; i <  values.length; i++) {
            addNodeToTail().point = new FunctionPoint(values[i].getX(), values[i].getY());
        }
    }
    /**Метод добавляющий новый элемент в конец списка и возвращающий ссылку на него**/
    private FunctionNode addNodeToTail() {
        FunctionNode node = new FunctionNode();
        tail.next = node;
        node.previous = tail;
        tail = node;
        node.next = node;
        countWorkPoints++;
        return node;
    }

    /**Метод возвращающий ссылку на объект элемента списка по его номеру.**/
    private FunctionNode getNodeByIndex(int index) {
        if(index < 0 || index > countWorkPoints)
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        FunctionNode node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    /**Метод добавляющий новый элемент в указанную позицию списка и возвращающий ссылку на него.**/
    private FunctionNode addNodeByIndex(int index) {
        if(index < 0 || index > countWorkPoints)
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        FunctionNode nodeNew = new FunctionNode();
        // Получаем элемент по текущем индексе
        FunctionNode nodeByIndex = getNodeByIndex(index);
        if(index == 0){
            head = nodeNew;
            head.previous = head;
            head.next = nodeByIndex;
        }else if(index == countWorkPoints){
            tail = nodeNew;
            tail.next = tail;
            tail.previous = nodeByIndex;
        }else{
            nodeNew.next = nodeByIndex;
            nodeNew.previous = nodeByIndex.previous;
            nodeByIndex.previous.next = nodeNew;
            nodeByIndex.previous = nodeNew;
        }
        countWorkPoints++;
        return nodeNew;
    }

    /**Метод удаляющий элемент из списка и возвращающий ссылку на него.**/
    private FunctionNode deleteNodeByIndex(int index) {
        if(index < 0 || index > countWorkPoints)
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        FunctionNode nodeByIndex = getNodeByIndex(index);
        if(index == 0){
            head = head.next;
            head.previous = head;
        } else if (index == countWorkPoints-1) {
            tail = tail.previous;
            tail.next = tail;
        } else{
            nodeByIndex.next.previous = nodeByIndex.previous;
            nodeByIndex.previous.next = nodeByIndex.next;
        }
        countWorkPoints--;
        return nodeByIndex;
    }

    public int getPointsCount() {return countWorkPoints;}

    /**Метод возвращающий левую границу определения функции**/
    public double getLeftDomainBorder(){
        FunctionNode node = head;
        double leftX = node.point.getX();
        for(int i = 1; i < countWorkPoints; i++) {
            node = node.next;
            if (Double.compare(node.point.getX(), leftX) < 1) leftX = node.point.getX();
        }
        return leftX;
    }
    /**Метод возвращающий правую границу определения функции**/
    public double getRightDomainBorder(){
        FunctionNode node = head;
        double rightX = node.point.getX();
        for(int i = 0; i < countWorkPoints; i++) {
            if (Double.compare(rightX, node.point.getX()) < 1) rightX = node.point.getX();
            node = node.next;
        }
        return rightX;
    }
    /**Метод возвращающий значение функции в точке x.**/
    public double getFunctionValue(double x) {
        if(Double.compare(x, getLeftDomainBorder()) > -1 && Double.compare(x, getRightDomainBorder()) < 1){
            double y = 0d;
            FunctionNode node = head;
            for(int i = 0; i < countWorkPoints -1; i++) {
                if (Double.compare(x, node.point.getX()) > -1 && Double.compare(x, node.next.point.getX()) < 1) {
                    //System.out.println("now x ="+x+" and leftX = "+node.point.getX()+" and rightX = "+node.next.point.getX());
                    double k = (node.next.point.getY() - node.point.getY()) / (node.next.point.getX() - node.point.getX());
                    double b = node.point.getY() - k * node.point.getX();
                    y = k * x + b;
                }
                node = node.next;
            }
            return y;
        }else {
            throw new IllegalArgumentException("Точка лежит вне границ определения функции");
        }
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= countWorkPoints)
            throw new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!", index);
        return getNodeByIndex(index).point;
    }

    public double getPointX(int index) {
        if (index < 0 || index >= countWorkPoints)
            throw new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!", index);
        return getNodeByIndex(index).point.getX();
    }

    public double getPointY(int index) {
        if (index < 0 || index >= countWorkPoints)
            throw new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!", index);
        return getNodeByIndex(index).point.getY();
    }
    /**Метод изменяющий элемент в списке по индексу. Координаты точки должны соответствовать ее индексу**/
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= countWorkPoints)
            throw new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!", index);
        FunctionNode node  = getNodeByIndex(index);
        if (index == 0) {
            if (Double.compare(point.getX(), node.next.point.getX()) < 0)
                node.point = point;
        } else if (index == countWorkPoints-1) {
            if (Double.compare(point.getX(), node.previous.point.getX()) > 0)
                node.point = point;
        } else if (Double.compare(point.getX(), node.previous.point.getX()) > 0 && Double.compare(point.getX(), node.next.point.getX()) < 0)
            node.point = point;
        else{
            throw new InappropriateFunctionPointException("Точка не соответсвует индексу!", point.getX());
        }
    }
    /**Метод изменяющий координаты x элемента в списке по индексу. Координаты точки должны соответствовать ее индексу**/
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        try {
            setPoint(index, new FunctionPoint(x, getNodeByIndex(index).point.getY()));
        }catch (Exception e){
            throw e;
        }
    }

    public void setPointY(int index, double y) {
        if(index<0 || index>=countWorkPoints)
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        getNodeByIndex(index).point.setY(y);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (Double.compare(point.getX(), getLeftDomainBorder()) < 0 ) {
            addNodeByIndex(0).point = point;
        }else if (Double.compare(point.getX(), getRightDomainBorder()) > 0) {
            addNodeToTail().point = point;
        }else {
            FunctionNode node = head;
            for (int i = 0; i < countWorkPoints; i++) {
                if (Double.compare(point.getX(), node.point.getX()) == 0)
                    throw new InappropriateFunctionPointException("Объявленная точка уже существует!", point.getX());

                if (Double.compare(point.getX(), node.point.getX()) < 0) {
                    addNodeByIndex(i).point = point;
                    return;
                }
                node = node.next;
            }
        }
    }

    public void deletePoint(int index) {
        if (countWorkPoints < 3)
            throw new IllegalStateException("Табулированная функция должна иметь не менее 2 точек!");
        if (index < 0 || index >= countWorkPoints)
            throw new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!", index);
        deleteNodeByIndex(index);
    }

    public void printPoints(){
        FunctionNode node = head;
        for(int i = 0; i < countWorkPoints;i++){
            System.out.println("index="+i+":"+ node.point.toString());
            node = node.next;
        }
    }

    @Override
    public String toString() {
        String string="{";
        FunctionNode node= head;
        for(int i=0;i<countWorkPoints;i++){
            string+=node.point.toString();
            node = node.next;
            if (i != countWorkPoints-1)
                string+=",";
        }
        string+='}';
        return string;
    }
    @Override
    public int hashCode() {
        long result=1;
        FunctionNode node= head;
        for(int i=0;i<countWorkPoints;i++){
            result=result*31+node.point.hashCode();
            node = node.next;
        }
        return (int)result;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] points=new FunctionPoint[countWorkPoints];
        FunctionNode node=head;
        for(int i=0;i<countWorkPoints;i++){
            points[i]=node.point;
            node=node.next;
        }
        return new LinkedListTabulatedFunction(points);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null || obj.getClass().getInterfaces()[0] != getClass().getInterfaces()[0])
            return false;
        TabulatedFunction objFunction=(TabulatedFunction)obj;
        if(objFunction.getPointsCount()!=this.countWorkPoints){
            return false;
        }
        if(obj.getClass()==LinkedListTabulatedFunction.class){
            LinkedListTabulatedFunction objLinkedFunction=(LinkedListTabulatedFunction) obj;
            FunctionNode nodeObj= objLinkedFunction.head;
            FunctionNode node= head;
            for(int i=0;i<countWorkPoints;i++){
                if(!node.point.equals(nodeObj.point))
                    return false;
                nodeObj = nodeObj.next;
                node = node.next;
            }
            return true;
        }else{
            FunctionNode node= head;
            for(int i=0;i<this.getPointsCount();i++){
                if(!objFunction.getPoint(i).equals(node.point))
                    return false;
                node = node.next;
            }
            return true;
        }
    }
}


/**
    Класс содержит информационное поле для хранения данных типа FunctionPoint
    А также поля для хранения ссылок на предыдущий и следующий элемент.
 **/
class FunctionNode{
    /**
     FunctionNode next - ссылка на следующий элемент
     FunctionNode previous - ссылка на предыдущий элемент
     **/
    public FunctionNode next;
    public FunctionNode previous;
    public FunctionPoint point;

    public FunctionNode(){
        next=null;
        previous=null;
        point=null;
    }
}
