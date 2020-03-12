package pg.autyzm.przyjazneemocje.lib.entities;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ann on 25.10.2016.
 */
public class Level {

    private int id;
    private boolean isLevelActive;

    private String photosOrVideosFlag = "photos";
    private int timeLimit;
    private int photosOrVideosShowedForOneQuestion;
    private int sublevelsPerEachEmotion;
    private int amountOfAllowedTriesForEachEmotion;
    private boolean isForTests;

    //ZlecenieIT - Test mode
    private boolean isLearnMode;
    private boolean isTestMode;
    private boolean isMaterialForTest;
    private int numberOfTriesInTest;
    private int timeLimitInTest;

    private static Level levelContext;
    private String amountOfEmotions;

    public Level(Cursor cur, Cursor cur2, Cursor cur3) {

        setPhotosOrVideosIdList(new ArrayList<Integer>());
        setEmotions(new ArrayList<Integer>());
        setPhotosOrVideosIdListInTest(new ArrayList<Integer>());
        setEmotionsInTest(new ArrayList<Integer>());

        while (cur.moveToNext()) {
            //setAmountOfEmotions(cur.getString(cur.getColumnIndex("nr_emotions")));
            setId(cur.getInt(cur.getColumnIndex("id")));

            setPhotosOrVideosFlag(cur.getString(cur.getColumnIndex("photos_or_videos")));

            setTimeLimit(cur.getInt(cur.getColumnIndex("time_limit")));
            setPhotosOrVideosShowedForOneQuestion(cur.getInt(cur.getColumnIndex("photos_or_videos_per_level")));
            int active = cur.getInt(cur.getColumnIndex("is_level_active"));

            //
            setPraises(cur.getString(cur.getColumnIndex("praises")));
            setShouldQuestionBeReadAloud(cur.getInt(cur.getColumnIndex("shouldQuestionBeReadAloud")) != 0);

            int isLevelForTests = cur.getInt(cur.getColumnIndex("is_for_tests"));
            setForTests(isLevelForTests != 0);

            setAmountOfAllowedTriesForEachEmotion(cur.getInt(cur.getColumnIndex("correctness")));
            setSublevelsPerEachEmotion(cur.getInt(cur.getColumnIndex("sublevels_per_each_emotion")));

            setLevelActive((active != 0));
            setName(cur.getString(cur.getColumnIndex("name")));
            setQuestionType(Question.valueOf(cur.getString(cur.getColumnIndex("question_type"))));
            setHintTypesAsNumber(cur.getInt(cur.getColumnIndex("hint_types_as_number")));

            //ZlecenieIT
            int isLearnMode = cur.getInt(cur.getColumnIndex("is_learn_mode"));
            int isTestMode = cur.getInt(cur.getColumnIndex("is_test_mode"));
            int isMaterialForTest = cur.getInt(cur.getColumnIndex("material_for_test"));

            setLearnMode((isLearnMode != 0));
            setTestMode((isTestMode != 0));
            setMaterialForTest((isMaterialForTest != 0));

            setNumberOfTriesInTest(cur.getInt(cur.getColumnIndex("number_of_tries_in_test")));
            setTimeLimitInTest(cur.getInt(cur.getColumnIndex("time_limit_in_test")));
        }

        if (cur2 != null) {
            while (cur2.moveToNext()) {
                if(cur2.getInt(cur2.getColumnIndex("material_for_test")) == 0){
                    getPhotosOrVideosIdList().add(cur2.getInt(cur2.getColumnIndex("photoid")));
                } else {
                    getPhotosOrVideosIdListInTest().add(cur2.getInt(cur2.getColumnIndex("photoid")));
                }
            }
        }

        if (cur3 != null) {
            while (cur3.moveToNext()) {
                if(cur3.getInt(cur2.getColumnIndex("material_for_test")) == 0){
                    getEmotions().add(cur3.getInt(cur3.getColumnIndex("emotionid")) - 1);
                } else {
                    getEmotionsInTest().add(cur3.getInt(cur3.getColumnIndex("emotionid")) - 1);
                }
            }
        }

    }
    private String name;

    public static Level getLevelContext() {
        return levelContext;
    }

    public String getAmountOfEmotions() {
        return amountOfEmotions;
    }

    private int hintTypesAsNumber = 0;

    private List<Integer> photosOrVideosIdList;
    private List<Integer> photosOrVideosIdListInTest;
    private List<Integer> emotions = new ArrayList<>();
    private List<Integer> emotionsInTest = new ArrayList<>();
    private String praises = "";

    private int secondsToHint;
    private boolean shouldQuestionBeReadAloud;

    private Question questionType;
    private List<Hint> hintTypes = new ArrayList<>();

    public Question getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Question questionType) {
        this.questionType = questionType;
    }

    public List<Hint> getHintTypes() {
        return hintTypes;
    }

    public void setHintTypes(List<Hint> hintTypes) {
        this.hintTypes = hintTypes;
    }

    public String getPraises() {
        return praises;
    }

    public void setPraises(String praises) {
        this.praises = praises;
    }

    public void addPraise(String newPraise) {
        if(this.praises.equals("")){
            this.praises = newPraise;
        }else {
            this.praises += ";" + newPraise;
        }
    }

    public int getHintTypesAsNumber() {
        return hintTypesAsNumber;
    }

    public void setHintTypesAsNumber(int hintTypesAsNumber) {
        this.hintTypesAsNumber = hintTypesAsNumber;
    }

    public int getNumberOfTriesInTest() {
        return numberOfTriesInTest;
    }

    public void setNumberOfTriesInTest(int numberOfTriesInTest) {
        this.numberOfTriesInTest = numberOfTriesInTest;
    }

    public int getTimeLimitInTest() {
        return timeLimitInTest;
    }

    public void setTimeLimitInTest(int timeLimitInTest) {
        this.timeLimitInTest = timeLimitInTest;
    }

    public boolean isMaterialForTest() {
        return isMaterialForTest;
    }

    public void setMaterialForTest(boolean isMaterialForTest) {
        this.isMaterialForTest = isMaterialForTest;
    }

    public enum Question {
        EMOTION_NAME, SHOW_WHERE_IS_EMOTION_NAME, SHOW_EMOTION_NAME
    }

    public enum Hint {
        FRAME_CORRECT, ENLARGE_CORRECT, MOVE_CORRECT, GREY_OUT_INCORRECT
    }

    public void addHintType(Hint hint){
        hintTypes.add(hint);
    }

    public void setAmountOfEmotions(String amountOfEmotions) {
        this.getEmotions().size();
    }


    public Level(){

        setPhotosOrVideosIdList(new ArrayList<Integer>());
        setEmotions(new ArrayList<Integer>());
        setLearnMode(true);
        setLevelActive(false);
        setId(0);

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotosOrVideosFlag() {
        return photosOrVideosFlag;
    }

    public void setPhotosOrVideosFlag(String photosOrVideosFlag) {
        this.photosOrVideosFlag = photosOrVideosFlag;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getPhotosOrVideosShowedForOneQuestion() {
        return photosOrVideosShowedForOneQuestion;
    }

    public void setPhotosOrVideosShowedForOneQuestion(int photosOrVideosShowedForOneQuestion) {
        this.photosOrVideosShowedForOneQuestion = photosOrVideosShowedForOneQuestion;
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

    public boolean isLevelActive() {
        return isLevelActive;
    }


    public void setLevelActive(boolean levelActive) {
        isLevelActive = levelActive;
    }

    public int getSublevelsPerEachEmotion() {
        return sublevelsPerEachEmotion;
    }

    public void setSublevelsPerEachEmotion(int sublevelsPerEachEmotion) {
        this.sublevelsPerEachEmotion = sublevelsPerEachEmotion;
    }

    public int getAmountOfAllowedTriesForEachEmotion() {
        return amountOfAllowedTriesForEachEmotion;
    }

    public void setAmountOfAllowedTriesForEachEmotion(int amountOfAllowedTriesForEachEmotion) {
        this.amountOfAllowedTriesForEachEmotion = amountOfAllowedTriesForEachEmotion;
    }

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return generateDefaultName();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPhotosOrVideosIdList() {
        return photosOrVideosIdList;
    }

    public void setPhotosOrVideosIdList(List<Integer> photosOrVideosIdList) {
        this.photosOrVideosIdList = photosOrVideosIdList;
    }

    public List<Integer> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<Integer> emotions) {
        this.emotions = emotions;
    }

    public List<Integer> getPhotosOrVideosIdListInTest() {
        return photosOrVideosIdListInTest;
    }

    public void setPhotosOrVideosIdListInTest(List<Integer> photosOrVideosIdList) {
        this.photosOrVideosIdListInTest = photosOrVideosIdList;
    }

    public List<Integer> getEmotionsInTest() {
        return emotionsInTest;
    }

    public void setEmotionsInTest(List<Integer> emotions) {
        this.emotionsInTest = emotions;
    }

    public void addEmotion(int newEmotionId) {

        // make sure the emotion is new

        boolean isNew = true;

        for(Integer emotion : emotions){
            if(emotion.equals(newEmotionId)) {
                isNew = false;
                break;
            }
        }

        if(isNew) {
            this.emotions.add(newEmotionId);
        }
    }

    public void deleteEmotion(int i) {
        this.emotions.remove(emotions.get(i));
    }

    public Map getInfo(){
        Map out = new HashMap();
        for(Field field : this.getClass().getDeclaredFields()){
            try {
                out.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    public int getSecondsToHint() {
        return secondsToHint;
    }

    public void setSecondsToHint(int secondsToHint) {
        this.secondsToHint = secondsToHint;
    }

    public boolean isShouldQuestionBeReadAloud() {
        return shouldQuestionBeReadAloud;
    }

    public void setShouldQuestionBeReadAloud(boolean shouldQuestionBeReadAloud) {
        this.shouldQuestionBeReadAloud = shouldQuestionBeReadAloud;
    }

    public void addPhoto(Integer photoId){
        photosOrVideosIdList.add(photoId);
    }

    public void removePhoto(Integer photoId){
        photosOrVideosIdList.remove(photoId);
    }


    public int getAllSublevelsInLevelAmount(){
        return emotions.size() * sublevelsPerEachEmotion;
    }

    public boolean isForTests() {
        return isForTests;
    }

    public void setForTests(boolean forTests) {
        isForTests = forTests;
    }

    public void addHintTypeAsNumber(int newType){
        setHintTypesAsNumber(getHintTypesAsNumber() + newType);
    }

    public void removeHintTypeAsNumber(int newType){
        setHintTypesAsNumber(getHintTypesAsNumber() - newType);
    }

    public void incrementEmotionIdsForGame(){

        List<Integer> newEmotions = new ArrayList<>();

        for(Integer emotionId : emotions){
            newEmotions.add(emotionId + 1);
        }

        emotions = newEmotions;

    }

    private String generateDefaultName() {
        String name = "";
        for (int emotion : emotions) {
            name += emotion + " ";
        }
        return name;
    }

    public static Level defaultLevel(){
        Level level = new Level();

        level.addEmotion(0);
        level.addEmotion(1);

        return level;
    }


}
