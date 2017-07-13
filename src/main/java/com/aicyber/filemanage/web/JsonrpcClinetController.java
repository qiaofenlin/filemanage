package com.aicyber.filemanage.web;

import java.net.URL;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aicyber.c4.base.web.controller.BaseController;
import com.aicyber.c4.base.web.model.AjaxObj;
import com.aicyber.filemanage.survey.SurveyBean;
import com.aicyber.filemanage.survey.SurveyImpl;
import com.aicyber.tools.IOUtil;
import com.aicyber.tools.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;

@Controller
@RequestMapping("zn/web/api/v1/survey")
public class JsonrpcClinetController extends BaseController {

	private String encode = "UTF-8";
	public static final String JSON_RPC_URL_Aicyber = "http://106.75.47.69:60010";
	public static final String JSON_RPC_URL_GC = "http://106.75.47.69:60008";

	@RequestMapping({ "startSurvey.json" })
	@ResponseBody
	public AjaxObj Aicyber_startSurvey(HttpServletRequest request)throws Exception {
		ServletInputStream inputStream = request.getInputStream();
		String json = IOUtil.getStreamString(inputStream, encode);
		logger.info("----------json:" + json);
		SurveyBean survey = (SurveyBean) JsonUtil.parseObject(json,SurveyBean.class);
		JsonRpcHttpClient client = new JsonRpcHttpClient(new URL(JSON_RPC_URL_Aicyber));
		SurveyImpl service = (SurveyImpl) ProxyUtil.createClientProxy(
				new JsonrpcClinetController().getClass().getClassLoader(),
				SurveyImpl.class, client);
		try {
			logger.info("----------survey surveyId:" + survey.getSurveyId());
			if (survey.getSurveyId() == null) {
				return new AjaxObj(false, "surveyId is null", null);
			}
		} catch (Exception e) {
			this.logger.info("error message is " + e.getMessage());
			return new AjaxObj(false, e.getMessage(), null);
		}
		String result = service.Aicyber_startSurvey(survey.getAppid(),survey.getUserId(), survey.getSurveyId());
		return new AjaxObj(result);
	}

	@RequestMapping({ "replySurvey.json" })
	@ResponseBody
	public AjaxObj Aicyber_replySurvey(HttpServletRequest request)throws Exception {
		ServletInputStream inputStream = request.getInputStream();
		String json = IOUtil.getStreamString(inputStream, encode);
		SurveyBean survey = (SurveyBean) JsonUtil.parseObject(json,SurveyBean.class);
		JsonRpcHttpClient client = new JsonRpcHttpClient(new URL(JSON_RPC_URL_Aicyber));
		SurveyImpl service = (SurveyImpl) ProxyUtil.createClientProxy(
				new JsonrpcClinetController().getClass().getClassLoader(),
				SurveyImpl.class, client);
		try {
			if (survey == null) {
				return new AjaxObj(false, "surveyId is null", null);
			}
		} catch (Exception e) {
			this.logger.info("error message is " + e.getMessage());
			return new AjaxObj(false, e.getMessage(), null);
		}
		String result = service.Aicyber_replySurvey(survey.getQuestionId(),
				survey.getSentence(), survey.getUserId(), survey.getAppid());
		return new AjaxObj(result);
	}

	@RequestMapping({ "recordList.json" })
	@ResponseBody
	public AjaxObj Aicyber_listRecord(HttpServletRequest request)throws Exception {
		ServletInputStream inputStream = request.getInputStream();
		String json = IOUtil.getStreamString(inputStream, encode);
		SurveyBean survey = (SurveyBean) JsonUtil.parseObject(json,SurveyBean.class);
		JsonRpcHttpClient client = new JsonRpcHttpClient(new URL(JSON_RPC_URL_Aicyber));
		SurveyImpl service = (SurveyImpl) ProxyUtil.createClientProxy(
				new JsonrpcClinetController().getClass().getClassLoader(),
				SurveyImpl.class, client);
		try {
			if (survey == null) {
				return new AjaxObj(false, "roundId is null", null);
			}
		} catch (Exception e) {
			this.logger.info("error message is " + e.getMessage());
			return new AjaxObj(false, e.getMessage(), null);
		}
		logger.info("userId is ..."+survey.getUserId());
		logger.info("appId is ..."+survey.getAppid());
		logger.info("surveyId is ..."+survey.getSurveyId());
		String str = service.Aicyber_listRecord(survey.getUserId(),survey.getAppid(),survey.getSurveyId());
		return new AjaxObj(str);
	}

//==========================================================================================
	@RequestMapping({ "/gc/startsurvey.json" })
	@ResponseBody
	public AjaxObj GC_startSurvey(HttpServletRequest request) throws Exception {
		JsonRpcHttpClient client = new JsonRpcHttpClient(new URL(JSON_RPC_URL_GC));
		SurveyImpl service = (SurveyImpl) ProxyUtil.createClientProxy(
				new JsonrpcClinetController().getClass().getClassLoader(),
				SurveyImpl.class, client);
		this.logger.info(" GC_startSurvey ........");
		String result = service.GC_startSurvey();
		System.out.println(result);
		return new AjaxObj(result);
	}
	
	@RequestMapping({ "/gc/replysurvey.json" })
	@ResponseBody
	public AjaxObj GC_replySurvey(HttpServletRequest request) throws Exception {
		ServletInputStream inputStream = request.getInputStream();
		String json = IOUtil.getStreamString(inputStream, encode);
		SurveyBean survey1 = (SurveyBean) JSONObject.parseObject(json,SurveyBean.class);
		SurveyBean survey = (SurveyBean) JsonUtil.parseObject(json,SurveyBean.class);
		JsonRpcHttpClient client = new JsonRpcHttpClient(new URL(JSON_RPC_URL_GC));
		SurveyImpl service = (SurveyImpl) ProxyUtil.createClientProxy(
				new JsonrpcClinetController().getClass().getClassLoader(),
				SurveyImpl.class, client);
		try {
			if (survey == null) {
				return new AjaxObj(false, "surveyId is null", null);
			}
		} catch (Exception e) {
			this.logger.info("error message is " + e.getMessage());
			return new AjaxObj(false, e.getMessage(), null);
		}
		String result = service.GC_replySurvey(survey.getQuestionId(),
				survey.getSentence(), survey.getUserId(), survey.getAppid(),
				survey.getRoundId());
		System.out.println(result);
		return new AjaxObj(result);
	}
}
