package pg.autyzm.przyjazneemocje.adapter;

public class LevelItem {
    private int levelId;
    private String name;
    private boolean isActive;
    private boolean canEdit;
    private boolean canRemove;

    public LevelItem(int levelId, String name, boolean isActive, boolean canEdit, boolean cenRemove) {
        this.levelId = levelId;
        this.name = name;
        this.isActive = isActive;
        this.canEdit = canEdit;
        this.canRemove = cenRemove;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public boolean isCanRemove() {
        return canRemove;
    }

    public int getLevelId() {
        return levelId;
    }
}
