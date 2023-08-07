package com.otsosity.spbuildrevrited.utils.dataClasses.Enums;

public enum GameType {
    NORMAL (1,"Постройка на теретории"),
    RESOURCES (2,"Постройка с 1 шалкера"),
    SKINS (3, "Оценка скинов"),
    FUGA_GAMES(4,"Игры фуги")
    ;
    private final Integer i;
    private final String translated;
    GameType(Integer i, String translated) {
        this.i = i;
        this.translated = translated;
    }
    public Integer getIntType(){
        return i;
    }

    public String getTranslated() {
        return translated;
    }
}
