<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport"
          content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
    <meta name="format-detection" content="telephone=no">
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0;"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="format-detection" content="email=no"/>
    <title></title>
    <link href="/css/style.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<div class="whole">

    <!--头部信息-->
    <div class="head">
        <div class="t_pic">
            <img src="/img/head.jpg"/>
        </div>
        <div class="t_tit">
            <p>第一届动物保护调研</p>
        </div>
    </div>

    <!--选择单选题目-->
    <div class="t_con">
        <div class="con_tit">
            <p>(1/20)&nbsp;单选题</p>
            <p>1.噶石糕是什么意思？</p>
        </div>
        <div class="con_con">
            <ul>
                <li>
                    <div class="checkboxFour">
                        <input type="checkbox" value="1" id="checkboxFourInput" name="chkItem"/>
                        <label for="checkboxFourInput"></label>
                    </div>
                    <p>好吃的一种糕点.</p>
                </li>
                <li>
                    <div class="checkboxFour">
                        <input type="checkbox" value="1" id="checkboxFourInput1" name="chkItem"/>
                        <label for="checkboxFourInput1"></label>
                    </div>
                    <p>骂人的一句话.</p>
                </li>
                <li>
                    <div class="checkboxFour">
                        <input type="checkbox" value="1" id="checkboxFourInput2" name="chkItem"/>
                        <label for="checkboxFourInput2"></label>
                    </div>
                    <p>江西本土方言.</p>
                </li>
                <li>
                    <div class="checkboxFour">
                        <input type="checkbox" value="1" id="checkboxFourInput3" name="chkItem"/>
                        <label for="checkboxFourInput3"></label>
                    </div>
                    <p>土鳖的意思.</p>
                </li>
                <li>
                    <div class="checkboxFour">
                        <input type="checkbox" value="1" id="checkboxFourInput4" name="chkItem"/>
                        <label for="checkboxFourInput4"></label>
                    </div>
                    <p>狗的意思.</p>
                </li>
            </ul>
        </div>
    </div>

    <!--上提下题按钮-->
    <div class="t_btn">
        <button type="button">返回上一题</button>
        <button id="next" type="button">下一题</button>
    </div>
    <!--提示遮罩层-->
    <div class="ts_mask">
        <div class="tishi">
            <div class="qd"></div>
        </div>
    </div>
</div>
<script language="javascript" type="text/javascript" src="/jQuery/jquery-3.1.1.min.js"></script>
<script>
    //选择题选项动画
    aa(0);

    function aa(i) {
        //alert(i);

        $('.con_con ul').children('li').eq(i).animate({
            top: 0 + 'vw',
        }, 800);
        i++;
        var set = setTimeout("aa(" + i + ")", 500);

        var tim = $(".con_con ul").children("li").length;
        if (i <= tim) {
            clearTimeout(tim);
        }
    }

    //选择题标题动画
    $(".con_tit").animate({
        left: 80 + 'vw',
    }, 1500);

    //点击下一题没选择时执行的动作
    $(".qd").click(function () {
        $(".ts_mask").fadeOut(1000);
    });
</script>


<script>

    $(function () {
        $(':checkbox[name=chkItem]').each(function () {
            //点击下一题判断语句
            $("#next").click(function () {
                if ($("input[type=checkbox]:checked").length == 0) {
                    $(".ts_mask").fadeIn(1000);
                }

            });

            //让checkbox多选框变成单选
            $(this).click(function () {
                if ($(this).attr('checked')) {
                    $(':checkbox[name=chkItem]').removeAttr('checked');
                    $(this).attr('checked', 'checked');
                }


            });
        });
    });


</script>
</body>
</html>
