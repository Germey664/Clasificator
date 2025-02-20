package Functions;

import java.io.Serializable;

/** Интерфейс используется для задания математических функций;
 * С которыми можно производить операции при помощи Functions.
 * Которые могут быть преобразованы в TabulatedFunction при помощи TabulatedFunctions*/
public interface Function extends Serializable {
    /**возвращает значение левой границы области определения функции **/
    public double getLeftDomainBorder();

    /**возвращает значение правой границы области определения функции **/
    public double getRightDomainBorder();

    /**возвращает значение функции в заданной точке **/
    public double getFunctionValue(double x);
}
