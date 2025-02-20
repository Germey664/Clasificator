package Testing;

public interface TestingGame {
    /**Расчет перед игрой**/
    public void init();

    /** Подсчет результатов игры. Чем лучше тем больше.**/
    public int getScore();

    /**Совершить ход. Передача координат и действия.**/
    public int game(int x, int y, int param);

    /**Вывод общих данных**/
    public String getString();

    /**Вывод данных игры для принятия решения ботом**/
    void getDataParam(Object dataParamWR);
    /**Количество параметров данных игры для принятия решения ботом**/
    int getCountParam();
}
