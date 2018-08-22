<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSSDK之上传图片</title>
<%--
    <link rel="stylesheet" type="text/css" href="css/style_oss.css"/>
--%>


<%--    <script type="text/javascript"
            src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <script type="text/javascript" src="js/auth.js"></script>--%>

</head>
<body>

<s:if test="#request.config!=null" >
<%--
    <input type="hidden" value='<%=request.getAttribute("wechatConfig")%>' id="wechatConfig"/>
--%>
    <input type="hidden" value='<%=request.getAttribute("ossConfig")%>' id="ossConfig"/>
</s:if>

<s:if test="#request.config!=null" >
<%--
    <h3><%=request.getAttribute("wechatConfig")%></h3>
--%>
    <h3><%=request.getAttribute("ossConfig")%></h3>
</s:if>

<%--<div align="center">
    <img id="userImg" alt="头像" src="">-
</div>

<div align="center">
    <span>UserName:</span>
    <div id="userName" style="display: inline-block"></div>
</div>

<div align="center">
    <span>UserId:</span>
    <div id="userId" style="display: inline-block"></div>
</div>


<div align="center">
    <span class="desc">是否验证成功</span>
    <button class="btn btn_primary" id="yanzheng">验证</button>
</div>

<div align="center">
    <span class="desc">测试按钮</span>
    <button class="btn btn_primary" id="ceshi">测试</button>
</div>

<div align="center">
    <span class="desc">上传图片按钮</span>
    <button class="btn btn_primary" id="uploadImg">上传图片</button>
</div>

<div align="center">
    <span class="desc">拍照上传图片按钮</span>
    <button class="btn btn_primary" id="uploadImgFromCamera">拍照上传</button>
</div>


<div align="center">
    <span class="desc">扫码按钮</span>
    <button class="btn btn_primary" id="qrcode" >扫码</button>
</div>--%>

<style>
    ul{
        list-style:none;
    }
    #file-list {overflow: hidden;padding-left: initial;}
    #file-list li {
        width:160px;
        float: left;
        height:200px;
        position: relative;
        height: inherit;
        margin-bottom: inherit;
    }
    #file-list li a {
        width:150px;
        height:150px;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        margin:0 auto;
        border:1px solid #ccc;
        padding: 5px 5px 5px 5px;
    }
    .close{
        background-image: url("img/register/mail.png");
        width: 30px;
        height: 30px;
        background-size: contain;
        position: absolute;
        right: 2%;
        top: 0;
    }
    #file-list li a img {max-width:100%;max-height: 100%;}
    .progress{
        position: absolute;
        background-color: rgba(4, 4, 4, 0.53);
        color: #fff;
        padding: 3px 3px 3px 3px;
        border-radius: 10%;
    }
</style>

<div id="ossfile">你的浏览器不支持flash,Silverlight或者HTML5！</div>

<form name=theform>
    <input type="radio" name="myradio" value="local_name" checked=true/> 上传文件名字保持本地文件名字
    <input type="radio" name="myradio" value="random_name" /> 上传文件名字是随机文件名, 后缀保留
</form>


<input type="hidden" id="images_upload" name="images" value=""/>
<div id="container">
    <a id="selectfiles" href="javascript:void(0);" class='btn'>选择文件</a>
    <a id="postfiles" href="javascript:void(0);" class='btn'>开始上传</a>
    <ul id="file-list">
    </ul>
</div>

<pre id="console"></pre>

</body>
<script type="text/javascript" src="lib/plupload-2.1.2/js/moxie.js"></script>
<script type="text/javascript" src="lib/plupload-2.1.2/js/plupload.min.js"></script>
<script type="text/javascript" src="jQuery/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="js/upload.js"></script>
<%--
<script type="text/javascript" src="lib/plupload-2.1.2/js/plupload.full.min.js"></script>
--%>
</html>