package fun.dalynkaa.eventbuilders.utils.dataClasses.Enums;

public enum GameStage {
    NO_GAME(1, "Игру не найденно", "Начать игру", "dalynkaa:admin_plus_button"),
    PREPARING_PLOT(2, "Подготовка поля", "Создать участки", "dalynkaa:admin_plot_button"),
    PREGENERATING_PLOT(3,"Генерация участков", "Выдать участки игрокам","dalynkaa:admin_players_button"),
    WAITING_START(4, "Ожидание начала", "Начать строительство", "dalynkaa:admin_watch_button"),
    GAME(5, "Идет игра", "Начать оценку","dalynkaa:admin_evualation_button"),
    WAIT_EVALUATION(6, "Ожидание оценки", "Начать оценку","dalynkaa:admin_evualation_button"),
    EVALUATION(7, "Оценка", "Подведение итогов","dalynkaa:admin_ending_button"),
    ENDING(8,"Итоги", "Закончить игру", "dalynkaa:admin_cross_button"),
    SKIN_NO_GAME(9, "Игру не найденно", "Начать игру", "dalynkaa:admin_plus_button"),
    SKIN_PREGENERATING_PLOT(10, "Создание плотов", "Создать плоты", "dalynkaa:admin_plot_button"),
    SKIN_EVALUATION(11, "Оценка скинов", "Закончить игру", "dalynkaa:admin_cross_button")
    ;
    private final Integer type;
    private final String translated;
    private final String action;
    private final String texture;
    GameStage(int type, String translated, String action, String texture) {
        this.type = type;
        this.translated = translated;
        this.action = action;
        this.texture = texture;
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

    public String getTexture() {
        return texture;
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
