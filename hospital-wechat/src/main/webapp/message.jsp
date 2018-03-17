<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html lang="zh-CN" class="ax-vertical-centered">

<head>
    <title>随访</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="./css/weui.css"/>
    <link rel="stylesheet" href="./css/example.css"/>

</head>


<body ontouchstart background="./img/survey/bg.jpg">


<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<div class="container" id="container"></div>

<div align="center">
    <font size="19" color="red">
        ${param.msg}
        <s:property value="errorMsg"/>
    </font>
</div>
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


