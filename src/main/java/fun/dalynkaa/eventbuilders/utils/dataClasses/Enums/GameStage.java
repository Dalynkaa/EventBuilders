package com.otsosity.spbuildrevrited.utils.dataClasses.Enums;

public enum GameStage {
    NO_GAME(1, "Игру не найденно", "Создать мир"),
    PREPARING_PLOT(2, "Подготовка поля", "Создать участки"),
    PREGENERATING_PLOT(3,"Генерация участков", "Выдать участки игрокам"),
    WAITING_START(4, "Ожидание начала", "Начать строительство"),
    WAITING_RESOURCES(5, "Ожидание выдачи ресурсов", "Начать выдачу ресурсов"),
    GETTING_RESOURCES(6, "Получение ресурсов", "Начать строительство"),
    GAME(7, "Идет игра", "Начать оценку"),
    EVALUATION(8, "Оценка", "Подведение итогов"),
    ENDING(9,"Итоги", "Закончить игру")
    ;
    private final Integer type;
    private final String translated;
    private final String action;
    GameStage(int type, String translated, String action) {
        this.type = type;
        this.translated = translated;
        this.action = action;
    }

    public String getTranslated() {
        return translated;
    }
    public String getAction() {
        return action;
    }

    public Integer getType() {
        return type;
    }

    @Override
    public String toString() {
        return "GameStage{" +
                "type=" + type +
                ", translated='" + translated + '\'' +
                '}';
    }
    private static final GameStage[] vals = values();

    public GameStage next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
