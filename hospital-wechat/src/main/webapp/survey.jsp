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

    <style type="text/css">
        .page {background-image:url(./img/survey/bg.jpg);background-size: cover;}
    </style>
</head>
<body ontouchstart>

<div class="container" id="container"></div>

<script type="text/html" id="tpl_home">
    <div class="page">
        <div class="page__hd">
            <s:if test="#request.survey!=null">
                <h1 class="page__title">
                    <s:property value="#request.survey.surveyName" />
                </h1>
                <p class="page__desc">
                    <s:property value="#request.survey.description" />
                </p>
            </s:if>
            <br/>
            <s:if test="#request.questions.size>0">
                <div class="page__bd page__bd_spacing">
                    <a href="javascript:jump('question1');" class="weui-btn weui-btn_primary">开始填写问卷</a>
                </div>
            </s:if>
        </div>
    </div>
</script>

<s:if test="#request.questions.size>0">
    <s:iterator value="#request.questions" id="question" status="qindex">
        <script type="text/html" id='tpl_question<s:property value="#qindex.index+1" />' >
            <div class="page" id='tpl_question<s:property value="#qindex.index+1" />' >
                <div class="page__hd">
                    <h1 class="page__title">问题<s:property value="#qindex.index+1"/>
                        （<s:property value="#qindex.index+1" />/<s:property value="#request.questions.size" />)
                    </h1>

                    <br/>

                    <h3><s:property value="#question.questionContent" /></h3>

                    <!--多选题-->
                    <s:if test="#question.questionType==1">
                        <s:if test="#question.choices.size > 0">
                            <div class="weui-cells weui-cells_checkbox">
                            <s:iterator id="choice" value="#question.sortedChoices" status="cindex">
                                    <label class="weui-cell weui-check__label"
                                           for='q<s:property value="#qindex.index+1"/>c<s:property value="#cindex.index" />'>
                                        <div class="weui-cell__hd">
                                            <input type="checkbox" class="weui-check"
                                                   name='q<s:property value="#qindex.index+1"/>c<s:property value="#cindex.index" />'
                                                   id='q<s:property value="#qindex.index+1"/>c<s:property value="#cindex.index" />' />
                                            <i class="weui-icon-checked"></i>
                                        </div>
                                        <div class="weui-cell__bd">
                                            <p><s:property value="#choice.choiceContent" /></p>
                                        </div>
                                    </label>
                            </s:iterator>
                            </div>
                        </s:if>
                        <s:if test="#question.textChoice==1" >
                            <div class="weui-cells__title">其他</div>
                            <div class="weui-cells weui-cells_form">
                                <div class="weui-cell">
                                    <div class="weui-cell__bd">
                                        <textarea class="weui-textarea" placeholder="请输入文本" rows="3"></textarea>
                                        <div class="weui-textarea-counter"><span>0</span>/200</div>
                                    </div>
                                </div>
                            </div>
                        </s:if>
                    </s:if>

                    <!--单选题-->
                    <s:elseif test="#question.questionType==2">
                        <s:if test="#question.choices.size > 0">
                            <div class="weui-cells weui-cells_radio">
                                <s:iterator id="choice" value="#question.sortedChoices" status="cindex">
                                    <label class="weui-cell weui-check__label"
                                           for='q<s:property value="#qindex.index+1"/>c<s:property value="#cindex.index" />'>
                                        <div class="weui-cell__bd">
                                            <p><s:property value="#choice.choiceContent" /></p>
                                        </div>
                                        <div class="weui-cell__ft">
                                            <input type="radio" class="weui-check" name="radio1"
                                                   id='q<s:property value="#qindex.index+1"/>c<s:property value="#cindex.index" />'/>
                                            <span class="weui-icon-checked"></span>
                                        </div>
                                    </label>
                                </s:iterator>
                            </div>
                        </s:if>
                        <s:if test="#question.textChoice==1" >
                            <div class="weui-cells__title">其他</div>
                            <div class="weui-cells weui-cells_form">
                                <div class="weui-cell">
                                    <div class="weui-cell__bd">
                                        <textarea class="weui-textarea" placeholder="请输入文本" rows="3"></textarea>
                                        <div class="weui-textarea-counter"><span>0</span>/200</div>
                                    </div>
                                </div>
                            </div>
                        </s:if>
                    </s:elseif>
                    <!--问答题-->
                    <s:else>
                        <div class="weui-cells weui-cells_form">
                            <div class="weui-cell">
                                <div class="weui-cell__bd">
                                    <textarea class="weui-textarea" placeholder="请输入文本" rows="3" id='q<s:property value="#qindex.index+1"/>c0'></textarea>
                                    <div class="weui-textarea-counter"><span>0</span>/200</div>
                                </div>
                            </div>
                        </div>
                    </s:else>

                    <br/>

                    <div class="page__bd page__bd_spacing">
                        <div class="weui-flex">
                            <div class="weui-flex__item">
                                <s:if test="#qindex.index>0">
                                    <a href="javascript:jump('question<s:property value="#qindex.index"/>');" class="weui-btn weui-btn_primary">上一题</a>

                                </s:if>
                            </div>
                            <div class="weui-flex__item">
                            </div>
                            <div class="weui-flex__item">
                                <s:if test="#qindex.index+1<#request.questions.size">
                                    <a href="javascript:jump('question<s:property value="#qindex.index+2"/>');" class="weui-btn weui-btn_primary">下一题</a>
                                </s:if>
                                <s:else>
                                    <a href="javascript:submit();" class="weui-btn weui-btn_primary">提 交</a>
                                </s:else>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </script>
    </s:iterator>
</s:if>

<script src="./jQuery/jquery-3.1.1.min.js"></script>
<script src="./js/zepto.min.js"></script>
<script src="./js/survey.js"></script>


<!--BEGIN toast-->
<div id="toast" style="display: none;">
    <div class="weui-mask_transparent"></div>
    <div class="weui-toast">
        <i class="weui-icon-success-no-circle weui-icon_toast"></i>
        <p class="weui-toast__content" id="toastStr">toastStr</p>
    </div>
</div>
<!--end toast-->


<!--BEGIN dialog2-->
<div id="dialogs">
    <div class="js_dialog" id="iosDialog2" style="display: none;">
        <div class="weui-mask"></div>
        <div class="weui-dialog">
            <div class="weui-dialog__bd" id="dialog2Str1">dialog2Str1</div>
            <div class="weui-dialog__ft">
                <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_primary"
                   id="dialog2Str2">dialog2Str2</a>
            </div>
        </div>
    </div>
</div>
<!--END dialog2-->

</body>
</html>
