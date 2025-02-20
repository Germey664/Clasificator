package Functions;

import java.io.Serializable;
/** Интерфейс отвечает за создание аппроксимированной функции
 * Его реализуют ArrayTabulatedFunction и LinkedListTabulatedFunction*/
public interface TabulatedFunction extends Function, Serializable, Cloneable {
    int getPointsCount();

    FunctionPoint getPoint(int index);
    double getPointX(int index);
    double getPointY(int index);

    /**Метод изменяющий элемент в списке по индексу. Координаты точки должны соответствовать ее индексу**/
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
    /**Метод изменяющий координаты x элемента в списке по индексу. Координаты точки должны соответствовать ее индексу**/
    void setPointX(int index, double x) throws InappropriateFunctionPointException;
    void setPointY(int index, double y);

    /**Метод создает новый элемент по координатам. Координаты точки должны соответствовать ее индексу**/
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    /**Метод удаляющий элемент из списка и возвращающий ссылку на него.**/
    void deletePoint(int index);
    void printPoints();

    Object clone() throws CloneNotSupportedException;
}
