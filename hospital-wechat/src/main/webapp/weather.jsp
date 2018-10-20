<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html lang="zh-cn" data-useragent="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36" class="ng-scope"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><style type="text/css">[uib-typeahead-popup].dropdown-menu{display:block;}</style><style type="text/css">.uib-time input{width:50px;}</style><style type="text/css">.uib-datepicker .uib-title{width:100%;}.uib-day button,.uib-month button,.uib-year button{min-width:100%;}.uib-datepicker-popup.dropdown-menu{display:block;}.uib-button-bar{padding:10px 9px 2px;}</style><style type="text/css">.ng-animate.item:not(.left):not(.right){-webkit-transition:0s ease-in-out left;transition:0s ease-in-out left}</style><style type="text/css">@charset "UTF-8";[ng\:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide:not(.ng-hide-animate){display:none !important;}ng\:form{display:block;}.ng-animate-shim{visibility:hidden;}.ng-anchor{position:absolute;}</style>

    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta http-equiv="x-rim-auto-match" content="none">
    <meta id="metaDescription" name="description" content="">

    <script src="./weather_files/hm.js"></script><script>
        document.documentElement.setAttribute('data-useragent', navigator.userAgent);
    </script>

    <title>哮喘预警</title>

    <link rel="stylesheet" href="./weather_files/a5681e.paper.min.css">

<%--    <%@ include file="include/html_head.jsp" %>
    <%@page import="java.util.*"%>
    <%@page import="childbreath.service.HealthWeather2" language="java" %>--%>

</head>
<body class="tn-transition-end tn-platform-class tn-platform-desktop">

<%--<%
    HealthWeather2 hw = new HealthWeather2();
    hw.getWeatherInfo();
%>--%>

<div id="shareImage" style="display: none;">
    <img src="http://c.xiumi.us/board/v5/2rxqt/48008002">
</div>

<%--<h1>
    <s:property value="#request.weather"/>
</h1>--%>


<div class="tn-reader-paper container ng-scope" ng-controller="PaperReaderController" tn-scroll-touch="onPaperScroll($event)" tn-scroll-touch-for="document">

    <section class="row tn-board-head-space ng-hide" ng-show="!displaySystemTitle"></section>

    <div class="loading-progress ng-hide" ng-show="!isReady">
        <span class="item"></span>
        <span class="item"></span>
        <span class="item"></span>
    </div>

    <section class="row tn-article-body" ng-show="isReady" tn-opera-house="" tn-atom-context="{ type: &#39;paper&#39;, inReader: true, animationEnabled: false }">
        <article class="dock-loader tn-scene-paper tn-show-root ng-scope tn-comp-inst tn-comp tn-from-house-reader_paper-cp ng-hide" tn-bind-comp-tpl-id="reader_paper-cp:sys/loader-flow-paper" tn-comp-role="root" ng-show="!htmlForPreview" tn-comp="cubes[0]" tn-comp-pose="compAttr.pose"><!-- ngInclude: compTemplatePath --><section class="tn-comp-pin tn-comp-style-pin tn-comp-anim-pin ng-scope" ng-include="compTemplatePath" tn-link="compAttr.link || comp.link" tn-link-frame="compAttr.linkFrame || comp.linkFrame" tn-sound="compAttr.sound" ng-style="compAttr.style" tn-pre-load-image="compAttr.style.backgroundImage" tn-animate="compAttr.anim"><ul class="dock paper tn-page-slot ng-scope tn-cell-inst tn-cell tn-cell-group tn-child-position-absolute group-empty" tn-cell-type="group" tn-cell="pages" tn-link="cell.link" tn-link-frame="cell.linkFrame" ng-style="cell.style" tn-animate="(!cell ? &#39;__NOT_MOUNT__&#39; : cell.anim)" tn-animate-on-self="true" tn-sound="cell.sound" ng-class="{ &#39;group-empty&#39;: !(cellGroupItems.length) }"> <!-- ngRepeat: chd in cellGroupItems track by $index --> </ul></section></article>
        <div class="dock-loader tn-scene-paper tn-show-root ng-binding ng-scope" tn-renderer-accelerate="htmlForPreview" ng-bind-html="htmlForPreview | unsafe" ng-show="!!htmlForPreview">
            <section style="background-color: rgb(255, 255, 255); box-sizing: border-box;" class="ng-scope">
                <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                    <section class="" style="margin-top: 10px; margin-bottom: 10px; position: static; box-sizing: border-box;">
                        <section class="" style="padding: 10px; background-color: rgb(254, 255, 245); box-sizing: border-box;">
                            <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                <section class="" style="position: static; box-sizing: border-box;">
                                    <section class="" style="text-align: center; font-size: 27px; color: rgb(254, 178, 38); box-sizing: border-box;">
                                        <p style="margin: 0px; padding: 0px; box-sizing: border-box;">
	<span style="font-size: 27px; line-height: 43.2px; box-sizing: border-box;">
	<strong style="box-sizing: border-box;">
        <span style="font-size:27px; line-height: 43.2px; box-sizing: border-box;">哮</span>
        <span style="font-size:27px; line-height: 43.2px; color: rgb(129, 190, 161); box-sizing: border-box;">喘</span>
        <span style="font-size:27px; line-height: 43.2px; color: rgb(253, 103, 102); box-sizing: border-box;">预</span>
        <span style="font-size:27px; line-height: 43.2px; color: rgb(4, 147, 200); box-sizing: border-box;">警</span></strong></span></p></section></section></section>
                            <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                <!--
                                <section class="" style="text-align: center; margin: 0px 0%; position: static; box-sizing: border-box;">
                                    <img style="max-width: 100%; vertical-align: middle; width: 26%; box-sizing: border-box;" class="" src="./weather_files/2016-5-29-5.png" data-ratio="0.228125" data-w="320" _width="26%">
                                </section></section>
                                -->
                                <hr/>
                                <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                    <section style="text-align: right">上海儿童医学中心呼吸科</section>
                                    <section class="" style="margin-top: 10px; margin-bottom: 10px; position: static; box-sizing: border-box;">
                                        <section class="" style="padding-left: 1em; padding-right: 1em; display: inline-block; text-align: center; box-sizing: border-box;">
	<span style="display: inline-block; padding: 0.3em 0.5em; border-radius: 0.5em; background-color: rgb(249, 110, 87); font-size: 18px; color: rgb(255, 255, 255); box-sizing: border-box;" class="">
	<p style="margin: 0px; padding: 0px; box-sizing: border-box;"><s:property value="#request.year1"/>年
        <s:property value="#request.month1"/>月
        <s:property value="#request.date1"/>日预警信息</p></span> </section>
                                        <section class="" style="border: 1px solid rgb(192, 200, 209); margin-top: -1em; padding: 20px 10px 10px; background-color: rgb(239, 239, 239); text-align: center; box-sizing: border-box;">
                                            <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                <section class="" style=" transform: translate3d(0px, 0px, 0px); -webkit-transform: translate3d(0px, 0px, 0px); -moz-transform: translate3d(0px, 0px, 0px); -o-transform: translate3d(0px, 0px, 0px); position: static; box-sizing: border-box;">
                                                    <section class="" style="display: inline-table; border-collapse: collapse; table-layout: fixed; width: 100%; box-sizing: border-box;">
                                                        <section class="Powered-by-XIUMI V5" style="position: static; display: table-row-group; box-sizing: border-box;" powered-by="xiumi.us">
                                                            <section class="" style="display: table-row; width: 100%; position: static; box-sizing: border-box;">
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 40%; border-style: none none dotted; border-width: 1px 1px 0px; border-radius: 0px; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; color: rgb(249, 110, 87); box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;">
                                                                                    <strong style="box-sizing: border-box;">哮喘等级</strong></p></section></section></section> </section>
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 60%; border-width: 1px 1px 0px; border-radius: 0px; border-style: none none dotted; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;"><s:property value="#request.warning1"/></p></section></section></section> </section></section></section>
                                                        <section class="Powered-by-XIUMI V5" style="position: static; display: table-row-group; box-sizing: border-box;" powered-by="xiumi.us">
                                                            <section class="" style="display: table-row; width: 100%; position: static; box-sizing: border-box;">
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 40%; border-style: dotted none none; border-width: 1px 0px 0px; border-radius: 0px; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; color: rgb(249, 110, 87); box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;">
                                                                                    <strong style="box-sizing: border-box;">受影响人群</strong></p></section></section></section> </section>
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 60%; border-width: 1px; border-radius: 0px; border-style: dotted none none; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;"><s:property value="#request.influ1"/></p></section></section></section> </section></section></section>
                                                        <section class="Powered-by-XIUMI V5" style="position: static; display: table-row-group; box-sizing: border-box;" powered-by="xiumi.us">
                                                            <section class="" style="display: table-row; width: 100%; position: static; box-sizing: border-box;">
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 40%; border-style: dotted none none; border-width: 1px; border-radius: 0px; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; color: rgb(249, 110, 87); box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;">
                                                                                    <strong style="box-sizing: border-box;">温馨提示</strong></p></section></section></section> </section>
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 60%; border-width: 1px; border-radius: 0px; border-style: dotted none none; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="text-align: justify; padding: 0px 5px; box-sizing: border-box;">
                                                                                <p style="white-space: normal; margin: 0px; padding: 0px; box-sizing: border-box;"><s:property value="#request.guide1"/></p></section></section></section> </section></section></section></section></section></section> </section></section></section>
                                <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                    <section class="" style="margin-top: 10px; margin-bottom: 10px; position: static; box-sizing: border-box;">
                                        <section class="" style="padding-left: 1em; padding-right: 1em; display: inline-block; text-align: center; box-sizing: border-box;">
	<span style="display: inline-block; padding: 0.3em 0.5em; border-radius: 0.5em; background-color: rgb(249, 110, 87); font-size: 18px; color: rgb(255, 255, 255); box-sizing: border-box;" class="">
	<p style="margin: 0px; padding: 0px; box-sizing: border-box;"><s:property value="#request.year2"/>年
        <s:property value="#request.month2"/>月
        <s:property value="#request.date2"/>日预警信息</p></span> </section>
                                        <section class="" style="border: 1px solid rgb(192, 200, 209); margin-top: -1em; padding: 20px 10px 10px; background-color: rgb(239, 239, 239); text-align: center; box-sizing: border-box;">
                                            <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                <section class="" style=" transform: translate3d(0px, 0px, 0px); -webkit-transform: translate3d(0px, 0px, 0px); -moz-transform: translate3d(0px, 0px, 0px); -o-transform: translate3d(0px, 0px, 0px); position: static; box-sizing: border-box;">
                                                    <section class="" style="display: inline-table; border-collapse: collapse; table-layout: fixed; width: 100%; box-sizing: border-box;">
                                                        <section class="Powered-by-XIUMI V5" style="position: static; display: table-row-group; box-sizing: border-box;" powered-by="xiumi.us">
                                                            <section class="" style="display: table-row; width: 100%; position: static; box-sizing: border-box;">
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 40%; border-style: none none dotted; border-width: 1px 1px 0px; border-radius: 0px; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; color: rgb(249, 110, 87); box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;">
                                                                                    <strong style="box-sizing: border-box;">哮喘等级</strong></p></section></section></section> </section>
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 60%; border-width: 1px 1px 0px; border-radius: 0px; border-style: none none dotted; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;"><s:property value="#request.warning2"/></p></section></section></section> </section></section></section>
                                                        <section class="Powered-by-XIUMI V5" style="position: static; display: table-row-group; box-sizing: border-box;" powered-by="xiumi.us">
                                                            <section class="" style="display: table-row; width: 100%; position: static; box-sizing: border-box;">
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 40%; border-style: dotted none none; border-width: 1px 0px 0px; border-radius: 0px; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; color: rgb(249, 110, 87); box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;">
                                                                                    <strong style="box-sizing: border-box;">受影响人群</strong></p></section></section></section> </section>
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 60%; border-width: 1px; border-radius: 0px; border-style: dotted none none; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;"><s:property value="#request.influ2"/></p></section></section></section> </section></section></section>
                                                        <section class="Powered-by-XIUMI V5" style="position: static; display: table-row-group; box-sizing: border-box;" powered-by="xiumi.us">
                                                            <section class="" style="display: table-row; width: 100%; position: static; box-sizing: border-box;">
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 40%; border-style: dotted none none; border-width: 1px; border-radius: 0px; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="padding: 0px 5px; color: rgb(249, 110, 87); box-sizing: border-box;">
                                                                                <p style="margin: 0px; padding: 0px; box-sizing: border-box;">
                                                                                    <strong style="box-sizing: border-box;">温馨提示</strong></p></section></section></section> </section>
                                                                <section class="" style="display: table-cell; vertical-align: middle; width: 60%; border-width: 1px; border-radius: 0px; border-style: dotted none none; border-color: rgb(62, 62, 62); box-sizing: border-box;">
                                                                    <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                                                        <section class="" style="margin: 5px 0%; position: static; box-sizing: border-box;">
                                                                            <section class="" style="text-align: justify; padding: 0px 5px; box-sizing: border-box;">
                                                                                <p style="white-space: normal; margin: 0px; padding: 0px; box-sizing: border-box;"><s:property value="#request.guide2"/></p></section></section></section> </section></section></section></section></section></section> </section></section></section>
                                <section class="Powered-by-XIUMI V5" powered-by="xiumi.us" style="box-sizing: border-box;">
                                    <section class="" style="position: static; box-sizing: border-box;">
                                        <section class="" style="font-size: 16px; color: rgb(249, 110, 87); text-align: center; box-sizing: border-box;">
                                            <p style="margin: 0px; padding: 0px; box-sizing: border-box;">信息来源：上海气象与健康重点实验室</p></section></section></section>
                                <section class="Powered-by-XIUMI V5" style="position: static; box-sizing: border-box;" powered-by="xiumi.us">
                                    <section class="" style="text-align: center; position: static; box-sizing: border-box;">
                                        <img style="max-width: 100%; width: 100%; box-sizing: border-box;" class="" src="./weather_files/2016-4-29-10.png" data-ratio="0.11875" data-w="640" _width="100%"></section></section> </section>
                            <section class="" style="width: 100%; text-align: right; margin-top: -4px; box-sizing: border-box;">
                                <section style="width: 50px; height: 4px; background-color: rgb(161, 158, 158); box-sizing: border-box;"></section> </section></section></section></section></div>
    </section>

</div>

<section class="tn-scripts tn-last-block">
    <script src="./weather_files/jweixin-1.0.0.js"></script>

    <script src="./weather_files/22b0eb.ng-tpl.min.js"></script>

    <script type="text/javascript" src="./weather_files/ng-tpl.js"></script>

    <script src="./weather_files/9b8e76.main.min.js"></script>

</section>


</body></html>
