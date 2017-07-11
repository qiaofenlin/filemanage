package com.aicyber.filemanage.survey;

public class SurveyBean {
	
  private String surveyId;
  private String questionId;
  private String sentence;
  private String userId;
  private String appid;
  private String roundId;
  private String mark;

  public SurveyBean() {
	// TODO Auto-generated constructor stub
}

public String getSurveyId() {
	return surveyId;
}

public void setSurveyId(String surveyId) {
	this.surveyId = surveyId;
}

public String getQuestionId() {
	return questionId;
}

public void setQuestionId(String questionId) {
	this.questionId = questionId;
}

public String getSentence() {
	return sentence;
}

public void setSentence(String sentence) {
	this.sentence = sentence;
}

public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

public String getAppid() {
	return appid;
}

public void setAppid(String appid) {
	this.appid = appid;
}

public String getRoundId() {
	return roundId;
}

public void setRoundId(String roundId) {
	this.roundId = roundId;
}

public String getMark() {
	return mark;
}

public void setMark(String mark) {
	this.mark = mark;
}

@Override
public String toString() {
	return "SurveyBean [surveyId=" + surveyId + ", questionId=" + questionId
			+ ", sentence=" + sentence + ", userId=" + userId + ", appid="
			+ appid + ", roundId=" + roundId + ", mark=" + mark + "]";
}
  
}
