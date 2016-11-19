package main;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import bean.Root;
import java.util.HashMap;
import java.util.Map;

public class ParseJson {
	private String JsonString;

	//
	ParseJson(String jsonstring) {
		this.JsonString = jsonstring;
	}

	// ½âÎö Json
	public Root parse() {
		Root rt = JSON.parseObject(this.JsonString, Root.class);

		return rt;

	}// Map<Object,Object>_ParseJson_end

}// class_ParseJson_end
