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

<s:if test="#request.surveyName!=null">
    <h3 id="surveyName" style="text-align:center;">
        <s:property value="#request.surveyName" />
    </h3>
</s:if>

<s:if test="#request.questions!=null" >
    <input type="hidden" value='<%=request.getAttribute("questions")%>' id="questionStr"/>
</s:if>

<s:if test="#request.ossConfig!=null" >
    <input type="hidden" value='<%=request.getAttribute("ossConfig")%>' id="ossConfig"/>
</s:if>

<s:if test="#request.openid!=null" >
    <input type="hidden" value='<%=request.getAttribute("openid")%>' id="openid"/>
</s:if>

<s:if test="#request.deliveryID!=null" >
    <input type="hidden" value="<%=request.getAttribute("deliveryID")%>" id="deliveryID"/>
</s:if>

<h3 class="page__title" id="questionIndex" style="display:none"></h3>

<br/>

<s:if test="#request.surveyDescription!=null">
    <p style="text-indent: 2em; text-align:justify; color:darkslategray" id="surveyDescription">
        <s:property value="#request.surveyDescription" />
    </p>
</s:if>


<h4 id="questionContent" style="text-indent: 2em; text-align:justify; color:darkslategray display:none"></h4>

<form>
    <div class="weui-cells weui-cells_checkbox" id="checkBoxArea" style="display:none">
    </div>

    <div class="weui-cells weui-cells_radio" id="radioBoxArea" style="display:none">
    </div>

    <div id="dateSelection" style="display:none">
        <br/>
        <p style="color:grey">请点击选择：</p>
        <div class="weui-cells weui-cells_form">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="month" id="dateQuestion" value=""/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="weui-cells weui-cells_form" style="display:none" id="textArea">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <textarea class="weui-textarea" placeholder="" rows="3"
                    id='textQuestionArea'
                    disabled="disabled"></textarea>
                <div class="weui-textarea-counter"><span>0</span>/200</div>
            </div>
        </div>
    </div>

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
                                <input id="selectfiles" class="weui-uploader__input" type="file" accept="image/*" multiple capture="camera"/>
                            </div>
                            <ul class="weui-uploader__files" id="file-list">
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br/>
</form>

<a href="javascript:questionStart();" class="weui-btn weui-btn_primary" id="startButton" style="display:none">开始填写</a>

<s:if test="#request.skipDeliveryInfo!=null">
<a href="javascript:;" class="weui-btn weui-btn_default" id="skipDeliveryInfo" style="display:none">跳过本次问卷</a>
</s:if>

<s:if test="#request.skipSurvey!=null">
<a href="javascript:;" class="weui-btn weui-btn_default" id="skipSurvey" style="display:none">跳过此类所有问卷</a>
</s:if>


<div class="page__bd page__bd_spacing">
    <div class="weui-flex">
        <div class="weui-flex__item">
            <a href="javascript:last();" class="weui-btn weui-btn_primary" id="lastButton" style="display:none">上一题</a>
        </div>
        <div class="weui-flex__item">
        </div>
        <div class="weui-flex__item">
            <a href="javascript:;" class="weui-btn weui-btn_primary" id="nextButton" style="display:none">下一题</a>
        </div>
    </div>
</div>



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
<script src="js/survey_main.js"></script>
<script src="./js/common.js"></script>

</html>