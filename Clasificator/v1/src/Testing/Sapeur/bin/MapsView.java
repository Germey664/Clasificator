package Testing.Sapeur.bin;


public class MapsView {
    private int[] mapsView;//Известная карта
    private int w, h;
    /**Класс работает с игровым полем открытым для игрока.
     * -1 - неоткрытая зона, -2 - стоит флаг -3 - стоит неправильно флаг,
     * -4 - взрыв бомбы, -5 - обезврежена бомбы, 0-9 количество бомб.
     * w - ширина h - высота.
     * Необходимо clear() перед началом работы.**/
    public MapsView(int w, int h){
        this.w = w;
        this.h = h;
        mapsView = new int[w * h];
        clear();
    }

    /**Очистка карты для начала работы**/
    public void clear(){
        for (int j = 0; j < h; j++)
            for (int i = 0; i < w; i++)
                mapsView[j * w + i] = -1;
    }

    /**Возвращает поле клеток, каждая клетка хранит информацию о своем состоянии**/
    public int[] getMapsView() {
        return mapsView;
    }

    /**Получить информацию для клетки по координатам**/
    public int getCell(int x, int y){
        return mapsView[y * w + x];
    }

    /**Получить информацию для клетки по координатам**/
    public Object getCellImage(int x, int y){
        if(getCell(x,y) == -1)
            return Box.CLOSED.image;
        if(getCell(x,y) == -2)
            return Box.FLAGED.image;
        if(getCell(x,y) == -3)
            return Box.NOBOMB.image;
        if(getCell(x,y) == -4)
            return Box.BOMBED.image;
        if(getCell(x,y) == -5)
            return Box.BOMB.image;
        Box box = Box.ZERO;
        for(int i = 0; i < mapsView[y * w + x]; i++)
            box = box.getNextNumberBox();
        return box.image;
    }

    /**Изменить информацию о клетки по координатам на num**/
    public void setCell(int x, int y, int num){
        mapsView[y * w + x]  =  num;
    }

    /**Устанавливает для клетки по координатам соответсвующее значение, maps - карта количества бомб рядом
     * Если клетка нулевая рекурсивно открывает клетки рядом.**/
    public void openCell(int x, int y, Maps maps){
        if(mapsView[y * w + x] != -1) return;
        mapsView[y * w + x]  =  maps.getCountBoomBesideCell(x,y);
        if (mapsView[y * w + x] == 0){
            if (y > 0) {
                if (x > 0)
                    openCell(x-1,y-1, maps);
                if (x < w - 1)
                    openCell(x+1,y-1, maps);
                openCell(x, y-1, maps);
            }
            if (y < h - 1) {
                if (x > 0)
                    openCell(x-1, y+1, maps);
                if (x < w - 1)
                    openCell(x+1, y+1, maps);
                openCell(x, y+1, maps);
            }
            if (x > 0)
                openCell(x-1, y, maps);
            if (x < w - 1)
                openCell(x+1, y, maps);
        }
    }

    /**Отображает расположение бомб и правильность флагов**/
    public void endingGame(Maps maps){
        for(int j = 0; j < h; j++){
            for (int i = 0; i < w; i++){
                if(maps.checkCellIsBoom(i,j) && getCell(i,j) == -2) setCell(i,j,-5);
                if(maps.checkCellIsBoom(i,j) && getCell(i,j) != -2) setCell(i,j,-4);
                if(!maps.checkCellIsBoom(i,j) && getCell(i,j) == -2) setCell(i,j,-3);
            }
        }
    }

    /**Рассчитывает сколько клеток уже было открыто.**/
    public int getCountOpenCell(){
        int count = 0;
        for (int j = 0; j < h; j++)
            for (int i = 0; i < w; i++) {
                if(mapsView[j*w + i] >= 0 && mapsView[j*w + i] < 9) count++;
            }
        return count;
    }
    /**Рассчитывает сколько установлен правильно флагов.**/
    public int getCountFlagCell(Maps maps){
        int count = 0;
        for (int j = 0; j < h; j++)
            for (int i = 0; i < w; i++) {
                if(mapsView[j*w + i] == -2 && maps.checkCellIsBoom(i,j)) count++;
            }
        return count;
    }

    public String getString(){
        String s = "";
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                if(mapsView[j * w + i] >= 0) s+= ' ';
                s = s + mapsView[j * w + i] + ' ';
            }
            s += "\n";
        }
        return  s;
    }
}
