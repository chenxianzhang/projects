package edu.coursemgr.controller;

import edu.coursemgr.service.interfaces.VerificationCodeService;
import edu.coursemgr.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;

@Controller
@RequestMapping("/verificationCode")
public class VerificationCodeController extends BaseController {

    @Autowired
    private VerificationCodeService verificationCodeService;


    @RequestMapping(value = "/generate", method = { RequestMethod.POST,
            RequestMethod.GET })
    public void generate(HttpServletResponse response) throws IOException {
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        EnumMap<VerificationCodeService.VerCodeProperty, Object> verCodeMap =
                verificationCodeService.generateVerCode();

        // 将认证码存入SESSION
//        request.getSession().setAttribute(Constant.SessionKey.SESSION_KEY_OF_RAND_CODE,
//                verCodeMap.get(VerificationCodeService.VerCodeProperty.RESULT_CODE));
        setSession(Constant.SessionKey.SESSION_KEY_OF_RAND_CODE,
                verCodeMap.get(VerificationCodeService.VerCodeProperty.RESULT_CODE));

        // 输出图象到页面
        ImageIO.write((BufferedImage)verCodeMap.get(VerificationCodeService.VerCodeProperty.IMAGE),
                "JPEG", response.getOutputStream());
    }

}
