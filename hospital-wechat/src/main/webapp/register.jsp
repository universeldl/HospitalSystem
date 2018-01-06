<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags"   prefix="s"%>
<html lang="zh-CN" class="ax-vertical-centered">
<head>
	<title>注册页面</title>
	<link rel="stylesheet" href="css/weui.css">
	<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0,viewport-fit=cover">
	<link rel="stylesheet" href="css/weui.css"/>
    <script src="js/register.js"></script>
    <script src="jQuery/jquery-3.1.1.min.js"></script>
</head>



<body>
    <s:if test="#session.patient!=null"><!-- 判断是否已经登录 -->
	<div class="container">
		<div class="weui-cells__title">注册</div>
		<div class="weui-cells weui-cells_form">
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">姓名</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" placeholder="请输入患儿姓名"/>
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">手机号</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="tel" pattern="[0-9]*" placeholder="请输入手机号"/>
                </div>
            </div>

            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label for="" class="weui-label">性别</label>
                </div>
                <div class="weui-cell__bd">
                    <select class="weui-select" name="sex">
                        <option value="male">男</option>
                        <option value="female">女</option>
                    </select>
                </div>
            </div>
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label for="" class="weui-label">首诊医院</label>
                </div>
                <div class="weui-cell__bd">
                    <select class="weui-select" name="hospital">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                    </select>
                </div>
            </div>
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label for="" class="weui-label">首诊医生</label>
                </div>
                <div class="weui-cell__bd">
                    <select class="weui-select" name="doctor">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                    </select>
                </div>
            </div>
			<div class="weui-cell">
				<div class="weui-cell__hd"><label for="" class="weui-label">出生日期</label></div>
				<div class="weui-cell__bd">
					<input class="weui-input" type="date" value=""/>
				</div>
			</div>
            <div class="weui-cell weui-cell_vcode">
                <div class="weui-cell__hd"><label class="weui-label">验证码</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="number" placeholder="请输入验证码"/>
                </div>
                <div class="weui-cell__ft">
                    <img class="weui-vcode-img" id="CAPTCHA" src="captchaAction_getCaptchaImg.action" />
                </div>
            </div>
		</div>
		<div class="weui-cells__tips">点击提交按钮即表示您同意《注册须知》相关内容</div>

        <div class="weui-btn-area">
            <a class="weui-btn weui-btn_primary" href="javascript:" id="showTooltips">确定</a>
        </div>
    </div>
    </s:if>
	<s:else>
	</s:else>


    <div class="weui-footer weui-footer_fixed-bottom">
	    <p class="weui-footer__text">Copyright &copy; 2017-2018 呼吸天使</p>
	</div>

    <script>
        document.getElementById("CAPTCHA").onclick=function(){reloadCaptchaImg()};
    </script>

</body>
</html>