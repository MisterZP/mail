package com.mail;

import com.mail.config.DruidServlet;
import com.mail.mapper.MailQueueMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
  Created by zengping on 2016/11/1.
 */
@EnableWebMvc
@SpringBootApplication
@ServletComponentScan(basePackageClasses = DruidServlet.class)
@MapperScan(basePackageClasses = {MailQueueMapper.class})
public class ClientApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
        LOGGER.info("" +
                "\n启动成功：\n" +
                "                             _ooOoo_\n" +
                "                            o8888888o\n" +
                "                            88\" . \"88\n" +
                "                            (| -_- |)\n" +
                "                             O\\ = /O\n" +
                "                         ____/`---'\\____\n" +
                "                       .   ' \\\\| |// `.\n" +
                "                        / \\\\||| : |||// \\\n" +
                "                      / _||||| -:- |||||- \\\n" +
                "                        | | \\\\\\ - /// | |\n" +
                "                      | \\_| ''\\---/'' | |\n" +
                "                       \\ .-\\__ `-` ___/-. /\n" +
                "                    ___`. .' /--.--\\ `. . __\n" +
                "                 .\"\" '< `.___\\_<|>_/___.' >'\"\".\n" +
                "                | | : `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
                "                  \\ \\ `-. \\_ __\\ /__ _/ .-` / /\n" +
                "          ======`-.____`-.___\\_____/___.-`____.-'======\n" +
                "                             `=---='\n" +
                "          .............................................\n" +
                "               佛曰：\n" +
                "                    永无bug。\n");
    }
}
