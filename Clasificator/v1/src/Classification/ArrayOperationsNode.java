package Classification;

/**Класс занимается хранением данных об массиве из операций.
 * Наследуется от ArrayNode.
 * Описывает методы для работы с конкретным типом представления данных
 */
public class ArrayOperationsNode extends ArrayNode{
    private int countParamThis;//Количество параметров. Первая часть данных. Входные данные
    private int countOptionThis;//Количество значений. Условная вторая часть данных. Сколько есть доступных действий
    private int countOptionCombinationThis;//Количество комбинаций значений. Варианты комбинирования значений.

    ArrayOperationsNode(){
        super();
    }
    ArrayOperationsNode(int arraySizeMax, int countParam, int countOptionCombination){
        super(arraySizeMax, countParam+countOptionCombination*2);
    }
    ArrayOperationsNode(int arraySizeMax, int countParam, int countOptionCombination, double[][] array){
        super(arraySizeMax, array.length, countParam+countOptionCombination*2,array);
    }
}