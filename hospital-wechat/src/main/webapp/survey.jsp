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

            <h1 class="page__title">Question1 (1/4)</h1>
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
                <div class="weui-cells__title">文本域</div>
                <div class="weui-cells weui-cells_form">
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" placeholder="请输入文本" rows="3"></textarea>
                            <div class="weui-textarea-counter"><span>0</span>/200</div>
                        </div>
                    </div>
                </div>
            </div>

            <br/>

            <h1 class="page__title">Question11</h1>
            <div class="weui-cells__title">question description11</div>
            <div class="weui-cells weui-cells_checkbox">
                <label class="weui-cell weui-check__label" for="s11">
                    <div class="weui-cell__hd">
                        <input type="checkbox" class="weui-check" name="checkbox1" id="s11" checked="checked"/>
                        <i class="weui-icon-checked"></i>
                    </div>
                    <div class="weui-cell__bd">
                        <p>standard is dealt for u.</p>
                    </div>
                </label>
                <label class="weui-cell weui-check__label" for="s12">
                    <div class="weui-cell__hd">
                        <input type="checkbox" name="checkbox1" class="weui-check" id="s12"/>
                        <i class="weui-icon-checked"></i>
                    </div>
                    <div class="weui-cell__bd">
                        <p>standard is dealicient for u.</p>
                    </div>
                </label>
                <label class="weui-cell weui-check__label" for="s13">
                    <div class="weui-cell__hd">
                        <input type="checkbox" name="checkbox1" class="weui-check" id="s13"/>
                        <i class="weui-icon-checked"></i>
                    </div>
                    <div class="weui-cell__bd">
                        <p>standard is dealicient for u.</p>
                    </div>
                </label>
                <label class="weui-cell weui-check__label" for="s14">
                    <div class="weui-cell__hd">
                        <input type="checkbox" name="checkbox1" class="weui-check" id="s14"/>
                        <i class="weui-icon-checked"></i>
                    </div>
                    <div class="weui-cell__bd">
                        <p>standard is dealicient for u.</p>
                    </div>
                </label>
                <div class="weui-cells__title">文本域</div>
                <div class="weui-cells weui-cells_form">
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" placeholder="请输入文本" rows="3"></textarea>
                            <div class="weui-textarea-counter"><span>0</span>/200</div>
                        </div>
                    </div>
                </div>
            </div>
            <h1 class="page__title">Question12</h1>
            <div class="weui-cells__title">question description12</div>
            <div class="weui-cells__title">文本域</div>
            <div class="weui-cells weui-cells_form">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <textarea class="weui-textarea" placeholder="请输入文本" rows="3"></textarea>
                        <div class="weui-textarea-counter"><span>0</span>/200</div>
                    </div>
                </div>
            </div>

            <br/>

            <div class="page__bd page__bd_spacing">
                <div class="weui-flex">
                    <div class="weui-flex__item">
                    </div>
                    <div class="weui-flex__item">
                    </div>
                    <div class="weui-flex__item">
                        <a href="javascript:jump('page2');" class="weui-btn weui-btn_primary">page2</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>

<script type="text/html" id="tpl_page2">
    <div class="page">
        <div class="page__hd">
            <h1 class="page__title">Question2 (2/4)</h1>
            <div class="page__desc">question description2</div>

            <br/>

            <div class="page__bd page__bd_spacing">
                <div class="weui-flex">
                    <div class="weui-flex__item">
                        <a href="javascript:jump('page1');" class="weui-btn weui-btn_default">page1</a>
                    </div>
                    <div class="weui-flex__item">
                    </div>
                    <div class="weui-flex__item">
                        <a href="javascript:jump('page3');" class="weui-btn weui-btn_primary">page3</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>


<script type="text/html" id="tpl_page3">
    <div class="page">
        <div class="page__hd">
            <h1 class="page__title">Question3 (3/4)</h1>
            <div class="page__desc">question description3</div>

            <br/>
            <div class="page__bd page__bd_spacing">
                <div class="weui-flex">
                    <div class="weui-flex__item">
                        <a href="javascript:jump('page2');" class="weui-btn weui-btn_default">page2</a>
                    </div>
                    <div class="weui-flex__item">
                    </div>
                    <div class="weui-flex__item">
                        <a href="javascript:jump('page4');" class="weui-btn weui-btn_primary">page4</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>

<script type="text/html" id="tpl_page4">
    <div class="page">
        <div class="page__hd">
            <h1 class="page__title">Question3 (4/4)</h1>
            <div class="page__desc">question description3</div>
            <br/>
            <div class="page__bd page__bd_spacing">
                <div class="weui-flex">
                    <div class="weui-flex__item">
                        <a href="javascript:jump('page3');" class="weui-btn weui-btn_default">page3</a>
                    </div>
                    <div class="weui-flex__item">
                    </div>
                    <div class="weui-flex__item">
                        <a href="javascript:jump('submit');" class="weui-btn weui-btn_primary">submit</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>

<script src="./js/zepto.min.js"></script>
<script src="./js/survey.js"></script>

</body>
</html>
