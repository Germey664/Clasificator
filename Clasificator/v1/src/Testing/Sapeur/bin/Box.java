package Testing.Sapeur.bin;

public enum Box {
    ZERO,
    NUM1,
    NUM2,
    NUM4,
    NUM3,
    NUM5,
    NUM6,
    NUM7,
    NUM8,
    BOMB,
    OPENED,
    CLOSED,
    FLAGED,
    BOMBED,
    NOBOMB;

    public Object image;

    Box getNextNumberBox (){
        return Box.values()[this.ordinal() + 1];
    }
    int getNumber(){
        return this.ordinal();
    }
}