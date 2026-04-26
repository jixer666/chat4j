package com.abc.chat4j.system.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.system.domain.dto.CaptchaDTO;
import com.abc.chat4j.system.domain.dto.EmailDTO;
import com.abc.chat4j.system.domain.dto.LoginDTO;
import com.abc.chat4j.system.domain.dto.RegisterDTO;
import com.abc.chat4j.system.domain.vo.CaptchaVO;
import com.abc.chat4j.system.domain.vo.EmailVO;
import com.abc.chat4j.system.domain.vo.MenuRouterVO;
import com.abc.chat4j.system.service.IndexService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system")
public class IndexController {

    @Autowired
    private IndexService indexService;


    @ApiOperation("登录")
    @PostMapping("/login")
    public ApiResult<String> login(@RequestBody LoginDTO loginDTO) {
        String token = indexService.login(loginDTO);

        return ApiResult.success(token);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public ApiResult<Void> register(@RequestBody RegisterDTO registerDTO) {
        indexService.register(registerDTO);

        return ApiResult.success();
    }

    @ApiOperation("获取菜单权限")
    @GetMapping("/routes")
    public ApiResult<List<MenuRouterVO>> getMenuRoutes() {
        List<MenuRouterVO> routers = indexService.getMenuRoutes();

        return ApiResult.success(routers);
    }

    @ApiOperation("获取白名单菜单权限")
    @GetMapping("/white/routes")
    public ApiResult<List<MenuRouterVO>> getMenuWhiteRoutes() {
        List<MenuRouterVO> routers = indexService.getMenuWhiteRoutes();

        return ApiResult.success(routers);
    }

    @ApiOperation("获取图形验证码")
    @GetMapping("/captcha")
    public ApiResult<CaptchaVO> getCaptchaImg(CaptchaDTO captchaDTO) {
        CaptchaVO captchaVO = indexService.getCaptchaImg(captchaDTO);

        return ApiResult.success(captchaVO);
    }

    @ApiOperation("发送注册邮件")
    @PostMapping("/sendRegisterEmail")
    public ApiResult<EmailVO> sendRegisterEmail(@RequestBody EmailDTO emailDTO) {
        EmailVO emailRegisterVO = indexService.sendRegisterEmail(emailDTO);

        return ApiResult.success(emailRegisterVO);
    }
}
