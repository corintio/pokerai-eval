package pokerai.game.eval.hammer.classes;

public final class EquivalenceClass {
    private final int id;
    private final int groupid;
    private final String cards;
    private final String desc;
    private final float domination;

    public String toString() {
        return id + " (" + desc + ": " + cards + ")";
    }
    
    public EquivalenceClass(int i, int gid, String cards, String desc, float domination) {
        this.id = i;
        this.groupid = gid;
        this.cards = cards;
        this.desc = desc;
        this.domination = domination;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupid;
    }

    public String getCards() {
        return cards;
    }

    public String getDescription() {
        return desc;
    }

    public float getDomination() {
        return domination;
    }

}
