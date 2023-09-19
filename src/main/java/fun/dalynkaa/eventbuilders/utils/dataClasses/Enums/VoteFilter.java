package fun.dalynkaa.eventbuilders.utils.dataClasses.Enums;

public enum VoteFilter {
    NORMAL("Без фильта"),
    NO_VOTE("Не проголосовал"),
    VOTED("Проголосовал");

    public String name;

    VoteFilter(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public VoteFilter setName(String name) {
        this.name = name;
        return this;
    }
    private static final VoteFilter[] vals = values();
    public VoteFilter next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
