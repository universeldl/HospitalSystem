<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html lang="zh-CN" class="ax-vertical-centered">

<head>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0,viewport-fit=cover">

    <title>随访</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="./css/weui.css"/>
    <link rel="stylesheet" href="./css/example.css"/>

</head>


<body style="background-image:url(./img/survey/bg.jpg);background-size: cover; margin:30px 30px 30px 30px;"

<br/>
<br/>
<br/>
<div class="container" id="container"></div>


<h3 style="text-align:center; color:darkred">
    ${param.msg}
    <s:if test="#errorMsg!=null">
        <s:property value="errorMsg"/>
    </s:if>
</h3>


<br/>
<br/>
<br/>


<a href="index.jsp" class="weui-btn weui-btn_primary" id="startButton">回到主页</a>

<!--
<h1 align="center">
        test123
</h1>
-->
<!--
<img src="img/error/error.png" width="100%"/>
-->
</body>
</html>


