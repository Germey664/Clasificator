package Functions;

/** Класс отвечает за создание аппроксимированной функции;
 * Точки хранятся в виде массива;*/
public class ArrayTabulatedFunction implements TabulatedFunction, Function{

    private FunctionPoint[] points;
    private int countWorkPoints;
    /**
     leftX - левая граница области определения;
     rightX - правая граница области определения;
     pointsCount - количество точек функции
     **/
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        if(pointsCount<2)
            throw new IllegalArgumentException("Количество точек функции ("+pointsCount+") должно быть более 1!");
        if(Double.compare(leftX, rightX) >= 0)
            throw new IllegalArgumentException("Правая граница области определения ("+rightX+") должна быть больше, чем левая("+leftX+")");

        points = new FunctionPoint[pointsCount];
        countWorkPoints = pointsCount;
        double delta = (rightX + 1 - leftX) / pointsCount;
        for(int i = 0; i < pointsCount; i++){
            points[i] = new FunctionPoint(leftX + delta*i,0);
        }
    }
    /**
     leftX - левая граница области определения;
     rightX - правая граница области определения;
     values - значения функции
     **/
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        if(values.length<2)
            throw new IllegalArgumentException("Количество точек функции ("+values.length+") должно быть более 1!");
        if(Double.compare(leftX, rightX) >= 0)
            throw new IllegalArgumentException("Правая граница области определения ("+rightX+") должна быть больше, чем левая("+leftX+")");

        points = new FunctionPoint[values.length];
        countWorkPoints = values.length;
        double delta = (rightX - leftX) / (values.length - 1);
        for(int i = 0; i <  values.length; i++) {
            points[i] = new FunctionPoint(leftX + delta * i, values[i]);
        }
    }
    /**
     values - массив точек функции с координатами x и  y
     **/
    public ArrayTabulatedFunction(FunctionPoint[] values){
        if(values.length<2)
            throw new IllegalArgumentException("Количество точек функции ("+values.length+") должно быть более 1!");
        for(int i = 0; i < values.length-1;i++){
            if(Double.compare(values[i].getX(), values[i+1].getX()) > 0)
                throw new IllegalArgumentException("Точки не соответствуют своим индексам ");
        }
        countWorkPoints = values.length;
        points = new FunctionPoint[values.length];

        for(int i = 0; i <  values.length; i++) {
            points[i] = new FunctionPoint(values[i].getX(), values[i].getY());
        }
    }
    /**Метод добавляющий новый элемент в конец списка и возвращающий ссылку на него**/
    public double getLeftDomainBorder(){
        double leftX = points[0].getX();
        for(int i = 0; i < points.length; i++)
            if(compare(points[i].getX(), leftX) < 1) leftX = points[i].getX();

        return leftX;
    }
    /**Метод возвращающий ссылку на объект элемента списка по его номеру.**/
    public double getRightDomainBorder(){
        double rightX = points[0].getX();
        for(int i = 0; i < points.length; i++)
            if(compare(rightX, points[i].getX()) < 1) rightX = points[i].getX();

        return rightX;
    }
    /**Метод возвращающий значение функции в точке x.**/
    public double getFunctionValue(double x){
        if(compare(x, getLeftDomainBorder()) > -1 && compare(x, getRightDomainBorder()) < 1){
            double y = 0d;
            for(int i = 0; i < points.length - 1 ; i++) {
                if (compare(x, points[i].getX()) > -1 && compare(x, points[i + 1].getX()) < 1) {
                    //System.out.println("now x ="+x+" and leftX = "+points[i].x+" and rightX = "+points[i+1].x);
                   double k = (points[i+1].getY() - points[i].getY()) / (points[i+1].getX() - points[i].getX());
                   double b = points[i].getY() - k * points[i].getX();
                   y = k * x + b;
                }
            }
            return y;
        }else {
            throw new IllegalArgumentException("Точка лежит вне границ определения функции");
        }
    }
    public int getPointsCount(){
        return points.length;
    }
    public FunctionPoint getPoint(int index){
        if(index<0 || index>=countWorkPoints)
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        return points[index];
    }
    /**Метод изменяющий элемент по индексу. Координаты точки должны соответствовать ее индексу**/
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if(index<0 || index>=countWorkPoints)
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        if(compare(point.getX(),getLeftDomainBorder()) > -1 && compare(point.getX(),getRightDomainBorder())<1) {
            if (index == 0) {
                if (compare(point.getX(), points[index + 1].getX()) < 0)
                    points[index] = point;
            } else if (index == points.length - 1) {
                if (compare(point.getX(), points[index - 1].getX()) > 0)
                    points[index] = point;
            } else if (compare(point.getX(), points[index - 1].getX()) > 0 && compare(point.getX(), points[index + 1].getX()) < 0)
                points[index] = point;
        }else{
            throw new InappropriateFunctionPointException("Точка лежит вне границ опрееления функции",  point.getX());
        }
    }
    public double getPointX(int index){
        if(index<0 || index>=countWorkPoints){
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        }
        return points[index].getX();
    }
    /**Метод изменяющий координаты x элемента по индексу. Координаты точки должны соответствовать ее индексу**/
    public void setPointX(int index, double x) throws InappropriateFunctionPointException{
        try {
            setPoint(index, new FunctionPoint(x, points[index].getY()));
        }catch (Exception e){
            throw e;
        }
    }
    public double getPointY(int index){
        if(index<0 || index>=countWorkPoints){
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        }
        return points[index].getY();
    }
    public void setPointY(int index, double y){
        if(index<0 || index>=countWorkPoints){
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        }
        points[index].setY(y);
    }
    /**Метод удаляющий элемент по индексу.**/
    public void deletePoint(int index){
        if(countWorkPoints<3){
            throw new IllegalStateException("Табличная функция должна иметь не менее 2 точек!");
        }
        if(index >= 0 && index < countWorkPoints) {
            for (int i = index; i < points.length - 1; i++) {
                points[i] = new FunctionPoint(points[i + 1]);
            }
            points[points.length - 1] = new FunctionPoint();
            countWorkPoints--;
        }else{
            throw  new FunctionPointIndexOutOfBoundsException("Такого элемента не существует!",index);
        }
    }
    /**Метод добавляющий элемент по индексу. Координаты точки должны соответствовать ее индексу**/
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        if(countWorkPoints >= points.length){
            FunctionPoint[] pointsBuffer =  new FunctionPoint[countWorkPoints];
            System.arraycopy(points,0,pointsBuffer,0,countWorkPoints);

            points = new FunctionPoint[((points.length+10)-((points.length+10)%10))];
            for(int i = countWorkPoints; i < points.length; i++)
                points[i] = new FunctionPoint();
            System.arraycopy(pointsBuffer,0,points,0,pointsBuffer.length);

        }
        if(countWorkPoints < points.length){
            int i;
            for(i = 0; i < countWorkPoints; i++){
                if (i == 0) {
                    if (compare(point.getX(), getLeftDomainBorder()) < 0)
                        break;
                }
                if(i > 0 && i < countWorkPoints)
                    if (compare(point.getX(), points[i-1].getX()) > 0 && compare(point.getX(), points[i].getX()) < 0)
                        break;
                if (i == countWorkPoints) {
                    if (compare(point.getX(), getRightDomainBorder()) > 0)
                        break;
                }
                if(compare(point.getX(),points[i].getX()) == 0){
                    throw new InappropriateFunctionPointException("Добавляемая точка уже существует ",point.getX());
                }
            }
            for(int i2 = countWorkPoints; i2 >= i+1; i2--){
                points[i2]= new FunctionPoint(points[i2-1]);
            }
            points[i] = point;
            countWorkPoints++;
        }
    }
    public void printPoints(){
        for(int i = 0; i < countWorkPoints;i++){
            System.out.println("index="+i+":"+ points[i].toString());
        }
    }
    int compare(double d1, double d2){
        final double epsilon = 0.001d;
        if(d1 - d2 < epsilon && d2 - d1 < epsilon)
            return 0;
        if(d1 - d2 < epsilon)
            return -1;
        if(d1 - d2 > epsilon)
            return 1;
        return -2;
    }

    @Override
    public String toString() {
        String string="{";
        for(int i=0;i<countWorkPoints;i++){
            string+=points[i].toString();
            if (i != countWorkPoints-1)
                string+=",";
        }
        string+='}';
        return string;
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
        if(obj.getClass()==ArrayTabulatedFunction.class){
            ArrayTabulatedFunction objArrayFunction=(ArrayTabulatedFunction)obj;
            for(int i=0;i<countWorkPoints;i++){
                if(!objArrayFunction.points[i].equals(points[i])){
                    return false;
                }
            }
            return true;
        }else{
            for(int i=0;i<this.getPointsCount();i++){
                if(!objFunction.getPoint(i).equals(this.points[i])){
                    return false;
                }
            }
            return true;
        }
    }
    @Override
    public int hashCode() {
        int result=1;
        for(int i=0;i<countWorkPoints;i++){
            result=result*31+points[i].hashCode();
        }
        return (int)result;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] copyPoints=new FunctionPoint[countWorkPoints];
        System.arraycopy(points,0,copyPoints,0,countWorkPoints);
        ArrayTabulatedFunction copy=new ArrayTabulatedFunction(copyPoints);
        return copy;
    }
}
