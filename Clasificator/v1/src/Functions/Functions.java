package Functions;

import Functions.meta.*;

import java.io.Serializable;

/**
 * Класс Functions
 * Данный класс содержит вспомогательные статические методы для работы с функциями
 * */
public class Functions implements Serializable {

    private Functions() { }

    /**Возвращает объект функции, полученный сдвигом вдоль осей**/
    public static Function shift(Function f, double shiftX, double shiftY)
    {
        return new Shift(f, shiftX, shiftY);
    }

    /**Метод возвращает объект функции, полученной масштабированием вдоль осей**/
    public static Function scale(Function f, double scaleX, double scaleY)
    {
        return new Scale(f, scaleX, scaleY);
    }

    /**Метод возвращает объект функции, являющейся функцией в степени**/
    public static Function power(Function f, double power)
    {
        return new Power(f, power);
    }

    /**Метод возвращает объект функции, являющейся суммой двух исходных**/
    public static Function sum(Function f1, Function f2)
    {
        return new Sum(f1,f2);
    }

    /**Метод возвращает объект функции, являющейся произведением двух исходных**/
    public static Function mult(Function f1, Function f2)
    {
        return new Mult(f1,f2);
    }

    /**Метод возвращает объект функции, являющейся композицией двух исходных**/
    public  static Function composition(Function f1, Function f2)
    {
        return new Composition(f1,f2);
    }
}

