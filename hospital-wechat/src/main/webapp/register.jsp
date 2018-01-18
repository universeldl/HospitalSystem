<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags"   prefix="s"%>
<html lang="zh-CN" class="ax-vertical-centered">
<head>
	<title>注册页面</title>
	<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0,viewport-fit=cover">

	<link rel="stylesheet" href="css/weui.css"/>
    <script src="js/register.js"></script>
    <script src="jQuery/jquery-3.1.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="ajax-lib/ajaxutils.js"></script>
</head>

<body>



<img src="img/register/title.jpg" width="100%"  />

<form>
	<div class="container">
		<div class="weui-cells__title">注册</div>
		<div class="weui-cells weui-cells_form">
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">姓名</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" placeholder="请输入患儿姓名" id="username"/>
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">手机号</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="tel" pattern="[0-9]*" placeholder="请输入手机号" id="tel"/>
                </div>
            </div>

            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label for="sex" class="weui-label">性别</label>
                </div>
                <div class="weui-cell__bd">
                    <select class="weui-select" id="sex">
                        <option disabled selected value></option>
                        <option value="male">男</option>
                        <option value="female">女</option>
                    </select>
                </div>
            </div>
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label for="hospital" class="weui-label">首诊医院</label>
                </div>
                <div class="weui-cell__bd">
                    <select class="weui-select" id="hospital">
                        <option disabled selected value></option>
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
                    <label for="doctor" class="weui-label">首诊医生</label>
                </div>
                <div class="weui-cell__bd">
                    <select class="weui-select" id="doctor">
                        <option disabled selected value></option>
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
				<div class="weui-cell__hd"><label for="birthday" class="weui-label">出生日期</label></div>
				<div class="weui-cell__bd">
					<input class="weui-input" type="date" value="" id="birthday"/>
				</div>
			</div>
            <div class="weui-cell weui-cell_vcode">
                <div class="weui-cell__hd"><label class="weui-label">验证码</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" placeholder="请输入验证码" id="captchaIN"/>
                </div>
                <div class="weui-cell__ft">
                    <img class="weui-vcode-img" id="CAPTCHAIMG" src="captchaAction_getCaptchaImg.action" />
                </div>
            </div>
		</div>

        <!--
        <label for="weuiAgree" class="weui-agree">
            <input id="weuiAgree" type="checkbox" class="weui-agree__checkbox" checked="checked"/>
            <span class="weui-agree__text">
                阅读并同意<a href="javascript:void(0);">《相关条款》</a>
            </span>
        </label>
        -->


        <div class="weui-btn-area">
            <a class="weui-btn weui-btn_primary" id="register_submit">提交</a>
        </div>
    </div>
    </form>

    <div class="weui-footer weui-footer_fixed-bottom">
        <p class="weui-footer__text">Copyright &copy; 2017-2018 呼吸天使</p>
    </div>


    <script>
        document.getElementById("CAPTCHAIMG").onclick=function(){reloadCaptchaImg()};
        document.getElementById("register_submit").onclick=function(){registerSubmit()};
    </script>


    <!--BEGIN toast-->
    <div id="toast" style="display: none;">
        <div class="weui-mask_transparent"></div>
        <div class="weui-toast">
            <i class="weui-icon-success-no-circle weui-icon_toast"></i>
            <p class="weui-toast__content" id="toastStr">toastStr</p>
        </div>
    </div>
    <!--end toast-->

    <!-- loading toast -->
    <div id="loadingToast" style="display:none;">
        <div class="weui-mask_transparent"></div>
        <div class="weui-toast">
            <i class="weui-loading weui-icon_toast"></i>
            <p class="weui-toast__content" id="loadToastStr">loadToastStr</p>
        </div>
    </div>
    </div>
    <!-- end loading toast -->

    <!--BEGIN dialog2-->
    <div id="dialogs">
        <div class="js_dialog" id="iosDialog2" style="display: none;">
            <div class="weui-mask"></div>
            <div class="weui-dialog">
                <div class="weui-dialog__bd" id="dialog2Str1">dialog2Str1</div>
                <div class="weui-dialog__ft">
                    <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_primary" id="dialog2Str2">dialog2Str2</a>
                </div>
            </div>
        </div>
    </div>
    <!--END dialog2-->

</body>
</html>