<%@ page language="java" pageEncoding="utf-8" %>

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

    function UrlEncode(str) {
        str = (str + '').toString();
        return encodeURIComponent(str).replace(/!/g, '%21').replace(/'/g, '%27').replace(/\(/g, '%28').
                replace(/\)/g, '%29').replace(/\*/g, '%2A').replace(/%20/g, '+');
    }


    function redirect() {
        var code = GetQueryString("code")
        if (code == null) {
            var att = window.location.search.substr(1);
            var postdata = "";
            ajax(
                {
                    method: 'POST',
                    url: 'wechatLoginAction_getAppId.action',
                    params: postdata,
                    callback: function (data) {
                        var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=";
                        url = url + data;
                        url = url + "&redirect_uri=";
                        var re_url =  "https://" + window.location.host;
                        re_url = re_url + "/hospital-wechat/uploadImg.jsp?";
                        re_url += att;
                        var en_url = UrlEncode(re_url);
                        url = url + en_url;
                        url = url + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
                        window.location.href = url;
                    }
                }
            );
        } else {
            var action_url = "http://" + window.location.host + "/hospital-wechat/uploadImgAction_upload.action?";
            action_url = action_url + window.location.search.substr(1);
            alert("action_url = " + action_url);
            window.location.href = action_url;
        }
    }
</script>

<body onLoad="redirect()">

</body>

</html>
