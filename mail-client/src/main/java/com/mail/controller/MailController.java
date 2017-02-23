package com.mail.controller;

import com.mail.common.ResponseResult;
import com.mail.service.MailQueueService;
import com.mail.utils.WebUtils;
import com.mail.vo.MailQueueVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by zengping on 2017/1/9.
 */

@RestController
@RequestMapping("/mail")
public class MailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private MailQueueService mailQueueService;

    @RequestMapping(value = "/add", method={RequestMethod.PUT})
    public ResponseResult<MailQueueVo> submitMailInfo(@RequestBody @Valid MailQueueVo mailQueueVo, HttpServletRequest request){
        mailQueueVo.setIp(WebUtils.getIpAddr(request));
        mailQueueService.add(mailQueueVo);
        LOGGER.info("mail info success add 2 database mailid : {}", mailQueueVo.getId());
        return ResponseResult.OK(mailQueueVo);
    }
}
