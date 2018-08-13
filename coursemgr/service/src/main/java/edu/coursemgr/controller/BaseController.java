package edu.coursemgr.controller;

import edu.coursemgr.utils.JacksonUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/8/9 0009 下午 9:18
 */
public class BaseController {

    protected BaseController() {

    }

    /**
     * 将json字符串转换为map  已解决跨域问题，此方法暂时无用
     * @param json
     * @return
     * @throws Exception
     */
    protected Map<String, Object> transfer(String json) throws Exception {
        json = URLDecoder.decode(json, "utf-8");
        json = json.replace("=", "");
        return JacksonUtil.json2MapDeeply(json);
    }

    /**
     * 通过sessionkey获取session
     * @param sessionKey
     * @return
     */
    protected String findSession(String sessionKey) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Object sessionValue = session.getAttribute(sessionKey);
        if (sessionValue != null) {
            return sessionValue.toString();
        }
        return null;
    }

    /**
     * 保存session
     * @param sessionKey
     * @param value
     */
    protected void setSession(String sessionKey, Object value) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setAttribute(sessionKey, value);
    }

    /**
     * 根据key，获取前端传过来的参数值
     * @param params
     * @param key
     * @return
     */
    protected String getParam(Map<String, Object> params, String key) {
        if (params.containsKey(key)) {
            Object value = params.get(key);
            return value == null ? null : value.toString();
        }
        return null;
    }

    /**
     * 自定义返回到前端的json对象，对于没必要新建一个param实体类的返回结果，
     * 可使用该方法向前端传递简单的json对象
     * @param key
     * @param value
     * @return
     */
    protected Map<String, Object> customResponse(String key, Object value) {
        Map<String, Object> cResponse = new HashMap<String, Object>(1);
        cResponse.put(key, value);
        return cResponse;
    }

}
