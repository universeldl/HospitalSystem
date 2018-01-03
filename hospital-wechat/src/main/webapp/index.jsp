<%@ page language="java" pageEncoding="utf-8"%>

<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript">
    function GetQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return null;
    }

    function redirect() {
        var code = GetQueryString("code")
        if (code == null) {
            window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf7622c6c9856841c&redirect_uri=http%3a%2f%2f5c3c3db5.ngrok.io%2fhospital-wechat%2f&response_type=code&scope=snsapi_base&state=test#wechat_redirect";
        } else {
            var action_url = "wechatLoginAction_login.action?code=";
            action_url = action_url + code;
            window.location.href=action_url;
        }
    }
</script>

<body onLoad="redirect()">

</body>

</html>
