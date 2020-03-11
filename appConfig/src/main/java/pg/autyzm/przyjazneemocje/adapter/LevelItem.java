package pg.autyzm.przyjazneemocje.adapter;

public class LevelItem {
    private int levelId;
    private String name;
    private boolean isLearnMode;
    private boolean isTestMode;
    private boolean canEdit;
    private boolean canRemove;

    public LevelItem(int levelId, String name, boolean isLearnMode, boolean isTestMode, boolean canEdit, boolean cenRemove) {
        this.levelId = levelId;
        this.name = name;
        this.isLearnMode = isLearnMode;
        this.isTestMode = isTestMode;
        this.canEdit = canEdit;
        this.canRemove = cenRemove;
    }

    public String getName() {
        return name;
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

    public boolean isLearnMode() {
        return isLearnMode;
    }

    public void setLearnMode(boolean learnMode) {
        isLearnMode = learnMode;
    }

    public boolean isTestMode() {
        return isTestMode;
    }

    public void setTestMode(boolean testMode) {
        isTestMode = testMode;
    }
}
