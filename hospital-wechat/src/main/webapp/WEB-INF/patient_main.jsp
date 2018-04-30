<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html lang="zh-CN" class="ax-vertical-centered">
<head>
    <title>用户主页</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0,viewport-fit=cover">
    <link rel="stylesheet" href="css/weui.css"/>
</head>

<body style="background-image:url(./img/survey/bg.jpg);background-size: cover; margin:30px 30px 30px 30px;"
      name="top">
<br/>
<div class="page__hd">
    <div class="weui-panel weui-panel_access">
        <div class="weui-panel__bd">
            <a class="weui-media-box weui-media-box_appmsg">
                <div class="weui-media-box__hd">
                    <img class="weui-media-box__thumb" style="border-radius:200px" src='<s:property value="#request.imgUrl"/>' alt="">
                </div>
                <div class="weui-media-box__bd">
                    <h4 class="weui-media-box__title">
                        <s:property value="#request.patient.name" /> ( <s:property value="#request.nickname" /> )

                    </h4>
                    <p class="weui-media-box__desc">
                        <s:if test="#request.patient.sex==0">女</s:if>
                        <s:elseif test="#request.patient.sex==1">男</s:elseif>
                        <s:date name="#request.patient.birthday" format="yyyy-MM-dd"/>
                    </p>
                </div>
            </a>
        </div>
    </div>


    <s:if test="#request.emptyDeliverys.size>0">
        <div class="page__category js_categoryInner">
            <div class="weui-cells page__category-content">
                <div class="weui-cells__title">未完成问卷</div>
                <s:iterator value="#request.emptyDeliverys" var="delivery">
                    <a class="weui-cell weui-cell_access js_item"
                       href='doSurvey.jsp?deliveryID=<s:property value="#delivery.id"/>' >
                        <div class="weui-cell__bd">
                            <p><s:property value="#delivery.name" /></p>
                            <p class="weui-media-box__desc">
                                <s:property value="#delivery.date" />
                            </p>
                        </div>
                        <div class="weui-cell__ft"></div>
                    </a>
                </s:iterator>
            </div>
        </div>
    </s:if>

    <s:if test="#request.answeredDeliverys.size>0">
        <div class="page__category js_categoryInner">
            <div class="weui-cells page__category-content">
                <div class="weui-cells__title">已完成问卷</div>

                <s:iterator value="#request.answeredDeliverys" var="delivery">
                    <a class="weui-cell weui-cell_access js_item">
                        <div class="weui-cell__bd">
                            <p><s:property value="#delivery.name" /></p>
                            <p class="weui-media-box__desc">
                                <s:property value="#delivery.date" />
                                <s:date name="#delivery.date" format="yyyy-MM-dd"/>
                            </p>
                        </div>
                    </a>
                </s:iterator>
            </div>
        </div>
    </s:if>

    <s:if test="#request.overdueDeliverys.size>0">
        <div class="page__category js_categoryInner">
            <div class="weui-cells page__category-content">
                <div class="weui-cells__title">已过期问卷</div>

                <s:iterator value="#request.overdueDeliverys" var="delivery">
                    <a class="weui-cell weui-cell_access js_item">
                    <div class="weui-cell__bd">
                        <p><s:property value="#delivery.name" /></p>
                        <p class="weui-media-box__desc">
                            <s:property value="#delivery.date" />
                            <s:date name="#delivery.date" format="yyyy-MM-dd"/>
                        </p>
                    </div>
                    </a>
                </s:iterator>
            </div>
        </div>
    </s:if>

    <br/>

    <div class="weui-footer">
        <p class="weui-footer__text" id="footer">Copyright &copy; 2017-2018 呼吸天使</p>
    </div>
</div>


</body>
</html>