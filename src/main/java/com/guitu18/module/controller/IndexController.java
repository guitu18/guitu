package com.guitu18.module.controller;

import com.guitu18.common.utils.JsonResult;
import com.guitu18.core.annonation.Autowired;
import com.guitu18.core.annonation.RequestMapping;
import com.guitu18.module.entity.User;
import com.guitu18.module.service.UserService;
import org.apache.log4j.Logger;


/**
 * IndexController
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
@RequestMapping
public class IndexController {

    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @RequestMapping
    public JsonResult index() {
        log.info(">>>>>>>>>>>>>>> IndexController.index() >>>>>>>>>>>>>>>");
        return JsonResult.ok("IndexController.index()");
    }

    @RequestMapping("login")
    public JsonResult login(String username, String password) {
        log.info(">>>>>>>>>>>>>>> IndexController.login() >>>>>>>>>>>>>>>");
        User user = userService.getByUserName(username);
        if (user == null || !user.getPassword().equals(password)) {
            return JsonResult.error("用户名或密码输入不正确");
        }
        return JsonResult.ok("登录成功", new User(username, password));
    }

}
