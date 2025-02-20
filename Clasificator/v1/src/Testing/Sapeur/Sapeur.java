package Testing.Sapeur;

import Classification.DataParamWR;
import Testing.ManualGame;
import Testing.Sapeur.bin.Box;
import Testing.Sapeur.bin.Maps;
import Testing.Sapeur.bin.MapsView;
import Testing.TestingGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Sapeur extends JFrame implements TestingGame, ManualGame {
    Maps maps;//Расположение бомб и информация скрытая от игрока
    MapsView mapsView;//Известная игроку информация и метки
    private boolean game = false;//Продолжается ли игра
    private int w, h, count;//Ширина, высота, количество бомб

    private boolean defeat = false;//Проигрыш
    private boolean win = false;//Победа
    private boolean enableMouseListener = true;//Нужно ли управление мышью
    private int countActions;

    private JPanel panel;
    private JLabel label;
    private final int IMAGE_SIZE = 50;//Размер иконок
    private Box[] boxImage;//Статически хранит изображения, нужна загрузка.

    /** Класс реализует игру в сапера. w - ширина, h - высота, count - количество бомб**/
    public Sapeur(int w, int h, int count) {
        this.w = w;
        this.h = h;
        this.count = count;
        maps = new Maps(w,h);
        mapsView = new MapsView(w,h);

        setImages();
        initLabel();
        initPanel();
        initFrame();
    }

    /**Расчет размещение бомб и расчет расстояния до них**/
    public void init(){
        game = true;
        defeat = false;
        win = false;
        countActions = 0;
        maps.init(count);
        mapsView.clear();
    }
    /**Идет ли игра**/
    public boolean isGame(){return game;}
    /** Статус игры -2 проигрыш, -1 - ничья, 0 - продолжается 1 - победа**/
    public int checkStatusGame(){
        if(defeat) {
            game = false;
            return -2;
        }
        if(win){
            game = false;
            return 1;
        }
        int countDetected = 0;
        for (int j = 0; j < h; j++)
            for (int i = 0; i < w; i++) {
                if(maps.checkCellIsBoom(i,j) &&  mapsView.getCell(i,j) == -2) countDetected++;
            }
        if (mapsView.getCountOpenCell() == (w * h - count) || countDetected == count) {
            game = false;
            win = true;
            return 1;
        }
        return 0;
    }
    /** Подсчет результатов игры. Чем лучше тем больше.**/
    public int getScore() {
        int score = mapsView.getCountOpenCell();
        score += mapsView.getCountFlagCell(maps)*2;
        score -= countActions/5;
        if(win) score*=2;
        if (defeat)score/=2;
        return score;
    }
    /**Отключение ненужного сейчас функционала. Управление мышью, отображение окна**/
    public void setStatusView(int view){
        if(view == 1){
            enableMouseListener = false;
        }
    }

    /**Совершить ход. Передача координат и действия. 1 - перемещение, 2 - поставить флаг
     * Возвращает 0 - нет действия, 1 - действие сделано, -1 - действие недопустимо, -2 - игра окончена**/
    public int game(int x, int y, int param){
        if(!isGame()) return -2;
        countActions++;
        if(param == 1) {
            if(mapsView.getCell(x,y) != -1)//Нельзя ходить
                return -1;
            if (maps.checkCellIsBoom(x, y)) {//Взрыв. Игра закончена.
                mapsView.endingGame(maps);
                defeat = true;
                game = false;
                return -2;
            }else {//Переход без взрыва.
                mapsView.openCell(x,y,maps);
                if(checkStatusGame() != 0) return -2;
                return 1;
            }
        }
        if(param == 2){//Поставить флаг.
            if(mapsView.getCell(x,y) == -2) {
                mapsView.openCell(x,y,maps);//Убираем флаг.
                if (maps.checkCellIsBoom(x,y)) {//Взрыв. Игра закончена.
                    mapsView.endingGame(maps);
                    defeat = true;
                    game = false;
                    return -2;
                }
            }
            else {
                mapsView.setCell(x,y,-2);//Ставим флаг.
                if(checkStatusGame() != 0) return -2;
                return 1;
            }
        }
        return 0;
    }

    /**Вывод карты с бомбами и карты расстояния до них**/
    public String getString(){
        String s = "";
        s+= maps.getString();
        return s;
    }
    /**Вывод игровой карты**/
    public String getStringView(){
        String s = "";
        s+=mapsView.getString();
        return s;
    }
    /**Описание происходящего для label**/
    public String getMessage(){
        String s = "";
        if(win)s+="Win! Game end: ";
        if(defeat)s+="Boom! Game end:";
        s+= getScore();
        return s;
    }

    /**Вывод данных игры для принятия решения ботом**/
    public void getDataParam(Object dataParamWR){
        DataParamWR _dataParamWR = (DataParamWR) dataParamWR;
        _dataParamWR.write(isGame());
        _dataParamWR.write(checkStatusGame());
        _dataParamWR.write(count);
        _dataParamWR.write(mapsView.getMapsView(),w*h);
    }

    @Override
    public int getCountParam() {
        return 1 + 1 + 1 + 5*5;
    }

    private void initLabel(){
        label = new JLabel("Welcome");
        add(label, BorderLayout.SOUTH);// Добавляет внизу текст о состоянии.
    }
    private void initPanel(){
        panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for(int j = 0; j < h; j++)
                    for(int i = 0; i < w; i++){
                        g.drawImage((Image) mapsView.getCellImage(i,j),
                                i*IMAGE_SIZE,j*IMAGE_SIZE,this);
                    }
            }
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(enableMouseListener) {
                    int x = e.getX() / IMAGE_SIZE;
                    int y = e.getY() / IMAGE_SIZE;

                    if (e.getButton() == MouseEvent.BUTTON1)
                        game(x, y, 1);
                    if (e.getButton() == MouseEvent.BUTTON3)
                        game(x, y, 2);
                    if (e.getButton() == MouseEvent.BUTTON2)
                        init();
                    label.setText(getMessage());
                    panel.repaint();
                }
            }
        });
        panel.setPreferredSize(new Dimension(w *IMAGE_SIZE,
                h *IMAGE_SIZE));
        add (panel);
    }

    private void initFrame(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Sweeper");
        setResizable(false);
        setLocation(0,0);
        setIconImage(getImage("icon"));
        setVisible(true);
        pack();
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        screenSize.setSize(screenSize.width/ 2,screenSize.height);
//        setSize(screenSize);
//        //setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /** Загрузить изображения в enum. Дальше можно статически получать их*/
    private void setImages(){
        for(Testing.Sapeur.bin.Box box : Box.values()){
            box.image = getImage(box.name());
        }
    }
    /** Загрузить картинку из ресурсов
     * name - Box.name (ZERO, NUM1, ...)*/
    private Image getImage(String name){
        String filename = "D:/FILE/Programming/GitHub/Clasificator/Clasificator/v1/src/Testing/Sapeur/res/Images/white/" + name.toLowerCase()+".png";
        ImageIcon icon = new ImageIcon(filename);
        return icon.getImage();

    }
}

/*
        Sapeur sapeur = new Sapeur(5,5,4);

        ManualGame manualGame = (ManualGame) sapeur;
        manualGame.setStatusView(0);

        TestingGame testingGame = (TestingGame) sapeur;
        testingGame.init();
        Scanner scanner = new Scanner(System.in);
        System.out.println(testingGame.getString());
        while (manualGame.isGame()){
            System.out.println(manualGame.getScore()+"\n"+manualGame.getStringView());
            System.out.print("x: ");
            int x = scanner.nextInt();
            System.out.print("y: ");
            int y = scanner.nextInt();
            System.out.print("p(1-2): ");
            int p = scanner.nextInt();

            testingGame.game(x,y,p);
        }
        System.out.println(manualGame.getMessage());
        System.out.println(manualGame.getString());
 */