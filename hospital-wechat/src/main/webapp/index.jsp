<%@ page language="java" pageEncoding="utf-8"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="jQuery/jquery-3.1.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="ajax-lib/ajaxutils.js"></script>
</head>


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
            var postdata = "";
            ajax(
                {
                    method:'POST',
                    url:'wechatLoginAction_getAppId.action',
                    params: postdata,
                    callback:function(data) {
                        var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=";
                        url = url + data;
                        url = url + "&redirect_uri=http%3a%2f%2fab60eb22.ngrok.io%2fhospital-wechat%2f&response_type=code&scope=snsapi_base&state=test#wechat_redirect";
                        window.location.href = url;
                    }
                }
            );
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
