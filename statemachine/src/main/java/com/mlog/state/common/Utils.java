package com.mlog.state.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class Utils {
	private static Logger logger = LoggerFactory.getLogger(Utils.class);
	
	private static String configPath = null;
	private static Properties config = null;

	/**
	 * Convert JSON format string to token object.
	 * @param <T>
	 * @param jsonStr
	 * @return
	 */
	public static <T> Object jsonStrToObject(String jsonStr, Class<T> classType) {
		Object jsonObj = null;
		if (jsonStr == null) {
			return jsonObj;
		}

		try {
			Gson gson = new GsonBuilder().create();
			jsonObj = gson.fromJson(jsonStr, classType);
		} catch (Exception e) {
			logger.error("", e);
		}

		return jsonObj;
	}
	
	/**
	 * Convert JSON format string to Map.
	 * @param string
	 * @return
	 */
//	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonStrToObject(String jsonStr) {
		
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		if (jsonStr == null) {
			return null;
		}

		try {
//			Gson gson = new GsonBuilder().create();
//			jsonMap = gson.fromJson(jsonStr, jsonMap.getClass());
			
//			ObjectMapper mapper = new ObjectMapper();
//			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
//			};
//
//			jsonMap = mapper.readValue(jsonStr, typeRef);
//			
			
	        JSONObject jObject = new JSONObject(jsonStr);
	        Iterator<?> keys = jObject.keys();

	        while( keys.hasNext() ){
	            String key = (String)keys.next();
	            Object value = jObject.get(key); 
	            if(value instanceof JSONObject){
	            	value = jsonStrToObject(value.toString());
	            }else if(value instanceof JSONArray){
	            	JSONArray array = (JSONArray) value;
	            	List<Object> list = new ArrayList<Object>();
	            	for(int i = 0; i < array.length(); i++){
	            		Map<String, Object> mapObj = jsonStrToObject(array.getString(i));
	            		list.add(mapObj);
	            	}
	            	value = list;
	            }
	            jsonMap.put(key, value);
	        }
	        
//			logger.info("Util jsonStr : {} / jsonMap : {}", jsonStr, jsonMap);

		} catch (Exception e) {
			logger.error("", e);
		}

		return jsonMap;
	}

	/**
	 * Convert object to JSON format string.
	 * @param object
	 * @return
	 */
	public static String objectToJsonStr(Object json) {
		String jsonStr = null;
		if (json == null) {
			return null;
		}

		try {
			Gson gson = new GsonBuilder().create();
			jsonStr = gson.toJson(json);
		} catch (Exception e) {
			logger.error("", e);
		}

		return jsonStr;
	}

	public static Properties getProperties() {
		if (config == null) {
			config = new Properties();
		} else {
			return config;
		}

		try {
			if (configPath == null) {
				configPath = "./conf/server.properties";
			}
			
			FileInputStream fis = new FileInputStream(configPath);
			config.load(fis);

			fis.close();
		} catch (Exception e) {
			logger.error("", e);
			
			return null;
		}

		return config;
	}
	
	public static void setConfigPath(String configPath) {
		Utils.configPath = configPath;
		logger.info("Set config path(" + configPath + ")");
	}
	
	public static String getProperty(String key) {
		if (config == null) {
			config = getProperties();
		}

		if (config == null) {
			logger.error("Could not load propeties.");
			return null;
		}

		String value = config.getProperty(key);
		if (value == null || value.length() == 0) {
			logger.error("Could not find property with key=[" + key + "] / val=[" + value + "]");
			return null;
		}

		return value;
	}
	
	public static int loadProperties(String path) {

		configPath = path;

		return 0;
	}

    public static int loadLogConfigs(String path) {
    	LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();

        try {
            configurator.doConfigure(path);
        } catch (JoranException e) {
            e.printStackTrace();
        }
        
        return 0;
    }


	/**
	 * 파일의 내용을 스트링으로 반환.
	 * @param path
	 * @return
	 */
	public static String readFile(String path) {
		String jsonStr = "";

		File file = new File(path);
		
		if (file.exists() == false) {
			logger.error("Could not find file in {}", path);
			return null;
		}

		try {
			String temp;

			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((temp = br.readLine()) != null) {
				jsonStr += temp;
			}

			br.close();
		} catch (Exception e) {
			logger.error("", e);
		}

		return jsonStr;
	}

	/**
	 * 워크플로우 템플릿 변수 파라메터를 추출하여 맵으로 반환.
	 * @param input
	 * @return
	 */
	public static Map<String, Object> getParameter(String input) {
		String regex = "\\$\\{([a-zA-Z0-9]*)\\}";
		Map<String, Object> parameter = new HashMap<String, Object>();

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		while (matcher.find() == true) {
			String key = matcher.group();
			String val = null;

			if (key != null && key.length() != 0) {
				val = key.replace("${", "").replace("}", "");
				parameter.put(key, val);
			}
		}

		return parameter;
	}

//	public static void setClientExchange(Map<String, Object> map, String name, int index) {
//		List<LinkedHashMap<String, String>> list = Utils.getRabbitConfig(name);
//		if (list == null || list.size() == 0) {
//			return;
//		}
//
//		LinkedHashMap<String, String> config = list.get(index % list.size());
//		map.put(CommonString.ROUTING_KEY, config.get("consumer"));
//		map.put(CommonString.CLIENT_EXCHANGE, config.get("exchange"));
//	}

//	public static void setServerExchange(Map<String, Object> map, String name, int index) {
//		List<LinkedHashMap<String, String>> list = Utils.getRabbitConfig(name);
//		if (list == null || list.size() == 0) {
//			return;
//		}
//
//		LinkedHashMap<String, String> config = list.get(index % list.size());
//		map.put("exchange", config.get("exchange"));
//		map.put("producer", config.get("producer"));
//		map.put(CommonString.SERVER_EXCHANGE, config.get("exchange"));
//	}

//	public static String getExchange(String name) {
//		String exchange = null;
//
//		List<LinkedHashMap<String, String>> list = Utils.getRabbitConfig(name);
//		LinkedHashMap<String, String> tmp = list.get(0);
//		exchange = tmp.get("exchange");
//
//		return exchange;
//	}
	
	
	public static void main(String[] args) {
/*		
		HashMap<String, Object> body = new HashMap<String, Object>();
		List<Object> cmdSet = new ArrayList<Object>();

		try {
			String value = getProperty("test");

			Map<String, Object> cmd0 = new HashMap<String, Object>();
			cmd0.put("cmd", value);
			Map<String, Object> cmd1 = new HashMap<String, Object>();
			cmd1.put("cmd", value);

			cmdSet.add(cmd0);
			cmdSet.add(cmd1);
			body.put("CMD_SET", cmdSet);

			System.out.println(value);
		} catch (Exception e) {
			logger.error("", e);
		}

		System.out.println(body.get("CMD_SET"));
		String s = objectToJsonStr(body);
		System.out.println(s);
*/
		
//		List<LinkedTreeMap<String, String>> x = getRabbitConfig("mmi_tx_config");
//		LinkedTreeMap<String, String> m = x.get(0);
//		String s = m.get("exchange");
//		
//		System.out.println(s);
	}

}
