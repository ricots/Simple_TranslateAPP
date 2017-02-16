package com.jonesrandom.testranslateapi.Translate;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseTranslate {

	@SerializedName("code")
	@Expose
	private int code;

	@SerializedName("text")
	@Expose
	private List<String> text;

	@SerializedName("lang")
	@Expose
	private String lang;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setText(List<String> text){
		this.text = text;
	}

	public List<String> getText(){
		return text;
	}

	public void setLang(String lang){
		this.lang = lang;
	}

	public String getLang(){
		return lang;
	}
}