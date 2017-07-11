package com.aicyber.filemanage.survey;

public interface SurveyImpl {
	 String Aicyber_startSurvey(String appId, String userId, String surveyId)throws Exception;
     String Aicyber_replySurvey(String questionId, String sentence, String userId, String appId)throws Exception;
     String Aicyber_listRecord(String paramString) throws Exception;
     String Aicyber_listrecord_ad(String userId,String appId,String surveyId) throws Exception;
     
     String GC_startSurvey()throws Exception;
     String GC_replySurvey(String questionId, String sentence, String userId, String appId, String roundId)throws Exception;

}
