package fun.dalynkaa.eventbuilders.utils.dataClasses.Enums;

public enum GameType {
    NORMAL (1,"Постройка на теретории", GameStage.NO_GAME, -22, "dalynkaa:admin_build"),
    //RESOURCES (2,"Постройка с 1 шалкера"),
    SKINS (2, "Оценка скинов", GameStage.SKIN_NO_GAME, -60,"dalynkaa:admin_skins"),
    //FUGA_GAMES(4,"Игры фуги")
    ;
    private final Integer i;
    private final String translated;
    private final GameStage start;
    private final Integer Y;
    private final String icon;
    GameType(Integer i, String translated, GameStage start, Integer y, String icon) {
        this.i = i;
        this.translated = translated;
        this.start = start;
        Y = y;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public Integer getIntType(){
        return i;
    }

    public String getTranslated() {
        return translated;
    }

    public GameStage getStart() {
        return start;
    }

    public Integer getY() {
        return Y;
    }

    private static final GameType[] vals = values();
    public GameType next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }

}
