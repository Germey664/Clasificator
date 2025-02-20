package Testing;

public interface ManualGame {
    /**Состояние. Идет ли игра**/
    public boolean isGame();

    /** Статус игры -2 проигрыш, -1 - ничья, 0 - продолжается 1 - победа**/
    public int checkStatusGame();

    /**Отключение ненужного сейчас функционала. Управление мышью, отображение окна**/
    public void setStatusView(int view);

    /**Вывод текстового представления игры**/
    public String getStringView();
    /** Подсчет результатов игры. Чем лучше тем больше.**/
    public int getScore();
    /**Вывод общих данных**/
    public String getString();

    /**Вывод описания текущего состояния игры**/
    public String getMessage();

}
