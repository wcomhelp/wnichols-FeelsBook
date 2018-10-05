package com.williamnichols.wnichols_feelsbook;

import java.util.Date;

/**
 * Created by William Nichols on 10/2/18.
 */

public class Emotion {
    /**
     * Declaration of variables:
     * emotionName:    the name of the emotion
     * emotionDate:    the date the emotion took place
     * emotionMessage: the optional message included with the emotion
     */
    private String emotionName;
    private Date emotionDate;
    private String emotionMessage;


    public String getEmotionName() {
        return emotionName;
    }

    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }

    public Date getEmotionDate() {
        return emotionDate;
    }

    public void setEmotionDate(Date emotionDate) {
        this.emotionDate = emotionDate;
    }

    public String getEmotionMessage() {
        return emotionMessage;
    }

    public void setEmotionMessage(String emotionMessage) {
        this.emotionMessage = emotionMessage;
    }
}
