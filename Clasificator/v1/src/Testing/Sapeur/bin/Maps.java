package Testing.Sapeur.bin;

public class Maps {
    private int[] maps;//Карта количества бомб рядом
    private int[] boom;//Карта размещения бомб. 1 = бомба
    private int w, h;//Размеры карты для игры

    /** Класс отвечает за поле хранения мин, а также за поле количество мин рядом с клеткой.
     * w - ширина h - высота
     * Необходимо init() перед началом работы.**/
    public Maps(int w, int h){
        this.w = w;
        this.h = h;
        maps = new int[w * h];
        boom = new int[w * h];
        clear();
    }

    /**Очистка карты для начала работы**/
    private void clear(){
        for (int j = 0; j < h; j++)
            for (int i = 0; i < w; i++) {
                maps[j * w + i] = 0;
                boom[j * w + i] = 0;
            }
    }

    /**Создает новую карту расположения бомб, count - количество бомб**/
    public void init(int count){
        clear();
        for (int i = 0; i < count; i++) {
            int x = getRanNumber(0, w-1);
            int y = getRanNumber(0, h-1);
            if(boom[y * w + x] != 1)
                boom[y * w + x] = 1;
            else
                i--;
        }
        for (int j = 0; j < h; j++)
            for (int i = 0; i < w; i++) {
                maps[j * w + i] = countingBoomBesideCell(i,j);
            }
    }

    /**Рассчитывает сколько бомб рядом с клеткой по координатам x и y**/
    public int countingBoomBesideCell(int x, int y){
        int countBoom = 0;
        if(y > 0) {
            if(x > 0)
                countBoom += boom[(y - 1) * w + (x - 1)];
            if(x < w - 1)
                countBoom += boom[(y - 1) * w + (x + 1)];
            countBoom += boom[(y - 1) * w + x];
        }
        if(y < h - 1) {
            if (x > 0)
                countBoom += boom[(y + 1) * w + (x - 1)];
            if (x < w - 1)
                countBoom += boom[(y + 1) * w + (x + 1)];
            countBoom += boom[(y + 1) * w + x];
        }
        if (x > 0)
            countBoom += boom[y * w + (x - 1)];
        if (x < w - 1)
            countBoom += boom[y * w + (x + 1)];
        return countBoom;
    }
    /**Возвращает поле клеток, каждая клетка хранит количество бомб вокруг нее**/
    public int[] getMaps() {return maps;}
    /**Возвращает количество бомб рядом с клеткой, клетка с бомбой не выделяется**/
    public int getCountBoomBesideCell(int x, int y){
        return maps[y * w + x];
    }
    /**Проверяет, что по переданным координатам находится бомба**/
    public boolean checkCellIsBoom(int x,int y){
        if(boom[y * w + x] == 1)
            return true;
        else
            return false;
    }
    public String getString(){
        String s = "";
        s+="maps:\n";
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++)
                s = s + maps[j * w + i] + ' ';
            s += "\n";
        }
        s+="booms:\n";
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++)
                s = s + boom[j * w + i] + ' ';
            s += "\n";
        }
        return  s;
    }

    /** Случайное число от min до max;*/
    private static int getRanNumber(int min, int max) {
        return (int) (Math.random() * (max+1 - min) + min);
    }
}
