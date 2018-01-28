<%--
  Created by IntelliJ IDEA.
  User: QQQ
  Date: 2018/1/27
  Time: 上午7:41
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html lang="zh-CN" class="ax-vertical-centered">
<head>
    <title>问卷名称</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <link rel="stylesheet" href="./css/weui.css"/>
    <link rel="stylesheet" href="./css/example.css"/>
</head>
<body ontouchstart>

<div class="container" id="container"></div>

<script type="text/html" id="tpl_home">
    <div class="page">
        <div class="page__hd">
            <h1 class="page__title">
                home page
            </h1>
            <p class="page__desc">home page</p>
        </div>
        <div class="page__bd page__bd_spacing">
            <a href="javascript:jump('page1');" class="weui-btn weui-btn_primary">page1</a>
        </div>
    </div>
</script>

<script type="text/html" id="tpl_page1">
    <div class="page">
        <div class="page__hd">
            <h1 class="page__title">Question1</h1>
            <div class="weui-cells__title">question description1</div>
            <div class="weui-cells weui-cells_radio">
                <label class="weui-cell weui-check__label" for="x11">
                    <div class="weui-cell__bd">
                        <p>cell standard</p>
                    </div>
                    <div class="weui-cell__ft">
                        <input type="radio" class="weui-check" name="radio1" id="x11"/>
                        <span class="weui-icon-checked"></span>
                    </div>
                </label>
                <label class="weui-cell weui-check__label" for="x12">
                    <div class="weui-cell__bd">
                        <p>cell standard</p>
                    </div>
                    <div class="weui-cell__ft">
                        <input type="radio" name="radio1" class="weui-check" id="x12" checked="checked"/>
                        <span class="weui-icon-checked"></span>
                    </div>
                </label>
            </div>


        </div>
        <div class="page__bd page__bd_spacing">
            <a href="javascript:jump('page2');" class="weui-btn weui-btn_primary">page2</a>
        </div>
    </div>
</script>

<script type="text/html" id="tpl_page2">
    <div class="page">
        <div class="page__hd">
            <h1 class="page__title">Question2</h1>
            <div class="weui-cells__title">question description2</div>
            <div class="weui-cells weui-cells_radio">
                <label class="weui-cell weui-check__label" for="x21">
                    <div class="weui-cell__bd">
                        <p>cell standard</p>
                    </div>
                    <div class="weui-cell__ft">
                        <input type="radio" class="weui-check" name="radio2" id="X21"/>
                        <span class="weui-icon-checked"></span>
                    </div>
                </label>
                <label class="weui-cell weui-check__label" for="x22">
                    <div class="weui-cell__bd">
                        <p>cell standard</p>
                    </div>
                    <div class="weui-cell__ft">
                        <input type="radio" name="radio2" class="weui-check" id="x22" checked="checked"/>
                        <span class="weui-icon-checked"></span>
                    </div>
                </label>
            </div>
        </div>
        <div class="page__bd page__bd_spacing">
            <a href="javascript:jump('page1');" class="weui-btn weui-btn_primary">page1</a>
            <a href="javascript:jump('page3');" class="weui-btn weui-btn_primary">page3</a>
        </div>
    </div>
</script>


<script type="text/html" id="tpl_page3">
    <div class="page">
        <div class="page__hd">
            <h1 class="page__title">page3</h1>
            <p class="page__desc">按钮</p>
        </div>
        <div class="page__bd page__bd_spacing">
            <a href="javascript:jump('page2');" class="weui-btn weui-btn_primary">page2</a>
            <a href="javascript:jump('page4');" class="weui-btn weui-btn_primary">page4</a>
        </div>
    </div>
</script>

<script type="text/html" id="tpl_page4">
    <div class="page">
        <div class="page__hd">
            <h1 class="page__title">page4</h1>
            <p class="page__desc">按钮</p>
        </div>
        <div class="page__bd page__bd_spacing">
            <a href="javascript:jump('page3');" class="weui-btn weui-btn_primary">page3</a>
            <a href="javascript:" class="weui-btn weui-btn_primary">submit</a>
        </div>
    </div>
</script>

<script src="./js/zepto.min.js"></script>
<script src="./js/survey.js"></script>

</body>
</html>
