<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html lang="zh-CN" class="ax-vertical-centered">
<head>
    <title>随访问卷</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0,viewport-fit=cover">

    <link rel="stylesheet" href="css/weui.css"/>



</head>

<body style="background-image:url(./img/survey/bg.jpg);background-size: cover; margin:30px 30px 30px 30px;"
      name="top"
      onLoad="getQuestions()">

<s:if test="#request.ossConfig!=null" >
    <input type="hidden" value='<%=request.getAttribute("ossConfig")%>' id="ossConfig"/>
</s:if>

<s:if test="#request.questions!=null" >
    <input type="hidden" value='<%=request.getAttribute("questions")%>' id="questionStr"/>
</s:if>

<s:if test="#request.surveyName!=null">
    <h3 id="surveyName" style="text-align:center;">
        <s:property value="#request.surveyName" />
    </h3>
</s:if>
<h3 class="page__title" id="questionIndex" style="display:none"></h3>

<br/>

<s:if test="#request.surveyDescription!=null">
    <p style="text-indent: 2em; text-align:justify; color:darkslategray" id="surveyDescription">
        <s:property value="#request.surveyDescription" />
    </p>
</s:if>


<h4 id="questionContent" style="text-indent: 2em; text-align:justify; color:darkslategray display:none"></h4>

<br/>
<%--<div class="weui-uploader__bd" style="display:none" id="uploaders">
    <ul class="weui-uploader__files" id="uploaderFiles">
        <li class="weui-uploader__file" style="background-image:url(./images/pic_160.png)"></li>
        <li class="weui-uploader__file" style="background-image:url(./images/pic_160.png)"></li>
        <li class="weui-uploader__file" style="background-image:url(./images/pic_160.png)"></li>
        <li class="weui-uploader__file weui-uploader__file_status" style="background-image:url(./images/pic_160.png)">
            <div class="weui-uploader__file-content">
                <i class="weui-icon-warn"></i>
            </div>
        </li>你
        <li class="weui-uploader__file weui-uploader__file_status" style="background-image:url(./images/pic_160.png)">
            <div class="weui-uploader__file-content">50%</div>
        </li>
    </ul>
    <div class="weui-uploader__input-box">
        <input id="uploaderInput" class="weui-uploader__input" type="file" accept="image/*" multiple/>
    </div>
</div>--%>



<%--
<input type="hidden" id="images_upload" name="images" value=""/>
--%>

<div class="weui-gallery" id="gallery">
    <span class="weui-gallery__img" id="galleryImg"></span>
    <div class="weui-gallery__opr">
        <a href="javascript:" class="weui-gallery__del" id="galleryImgDel">
            <i class="weui-icon-delete weui-icon_gallery-delete"></i>
        </a>
        <input id="curFileName" type="hidden" />
    </div>
</div>

<div id="container">
    <div class="weui-cells weui-cells_form" id="uploaderdiv" style="display:none">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <div class="weui-uploader">
                    <div class="weui-uploader__hd">
                        <div id="ossfile" class="weui-uploader__title">您的微信版本过低请升级!</div>
                    </div>
                    <div class="weui-uploader__bd">
                        <div class="weui-uploader__input-box">
                            <input id="selectfiles" class="weui-uploader__input" type="file" accept="image/*"  capture="camera"/>
                        </div>
                        <ul class="weui-uploader__files" id="file-list">
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<a href="javascript:questionStart();" class="weui-btn weui-btn_primary" id="startButton" style="display:none">开始填写</a>
<a href="javascript:" class="weui-btn weui-btn_primary" id="nextButton" style="display:none"></a>


<pre id="console"></pre>

<%--<div id="container">
    <div id="ossfile">您的微信版本过低请升级</div>
    <ul id="file-list">
    </ul>
    <div class="weui-uploader__input-box" id="uploaderdiv">
        <input id="selectfiles" class="weui-uploader__input" type="hidden file" accept="image/*"  capture="camera"/>
    </div>

    <br/>
    <br/>

    <a href="javascript:questionStart();" class="weui-btn weui-btn_primary" id="startButton" style="display:none">开始填写</a>
    <a href="javascript:next();" class="weui-btn weui-btn_primary" id="nextButton" style="display:none"></a>
</div>--%>

<%--<div id="container2" class="weui-uploader__bd">
    <ul id="file-list">
    </ul>

    <div class="weui-uploader__input-box">
        <input id="selectfiles" type="hidden" />
    </div>
</div>--%>





<s:if test="#request.deliveryID!=null" >
    <input type="hidden" value="<%=request.getAttribute("deliveryID")%>" id="deliveryID"/>
</s:if>

<br/>
<div class="weui-footer">
    <p class="weui-footer__text" id="footer">Copyright &copy; 2017-2018 呼吸天使</p>
</div>


<!--BEGIN toast-->
<div id="toast" style="display: none;">
    <div class="weui-mask_transparent"></div>
    <div class="weui-toast">
        <i class="weui-icon-success-no-circle weui-icon_toast"></i>
        <p class="weui-toast__content" id="toastStr">toastStr</p>
    </div>
</div>
<!--end toast-->

<!-- loading toast -->
<div id="loadingToast" style="display:none;">
    <div class="weui-mask_transparent"></div>
    <div class="weui-toast">
        <i class="weui-loading weui-icon_toast"></i>
        <p class="weui-toast__content" id="loadToastStr">loadToastStr</p>
    </div>
</div>
</div>
<!-- end loading toast -->

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
<!--BEGIN dialog1-->
<div class="js_dialog" id="iosDialog1" style="display: none;">
    <div class="weui-mask"></div>
    <div class="weui-dialog">
        <div class="weui-dialog__hd" id="dialog1Str1"><strong class="weui-dialog__title"></strong></div>
        <div class="weui-dialog__bd"></div>
        <div class="weui-dialog__ft">
            <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_default" id="dialog1Str2"></a>
            <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_primary" id="dialog1Str3"></a>
        </div>
    </div>
</div>
<!--END dialog1-->
</body>

<script src="jQuery/jquery-3.1.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="ajax-lib/ajaxutils.js"></script>
<script type="text/javascript" src="lib/plupload/js/moxie.js"></script>
<script type="text/javascript" src="lib/plupload/js/plupload.min.js"></script>
<script type="text/javascript" src="js/upload.js"></script>

<%--
    <script src="js/survey_upload.js"></script>
--%>
<script src="./js/common.js"></script>
</html>