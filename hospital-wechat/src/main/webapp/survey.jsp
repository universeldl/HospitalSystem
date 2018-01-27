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
    <!--
  <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0,viewport-fit=cover">
  -->
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <link rel="stylesheet" href="./css/weui.css"/>
    <link rel="stylesheet" href="./css/example.css"/>
    <script src="./js/zepto.min.js"></script>
    <script src="./js/example.js"></script>
    <script type="text/javascript">
        $(function () {
            var winH = $(window).height();
            var categorySpace = 10;

            $('.js_item').on('click', function () {
                alert();
                var id = $(this).data('id');
                window.pageManager.go(id);
            });
        });
        /*
        $('.js_category').on('click', function(){
            var $this = $(this),
                    $inner = $this.next('.js_categoryInner'),
                    $page = $this.parents('.page'),
                    $parent = $(this).parent('li');
            var innerH = $inner.data('height');
            bear = $page;

            if(!innerH){
                $inner.css('height', 'auto');
                innerH = $inner.height();
                $inner.removeAttr('style');
                $inner.data('height', innerH);
            }

            if($parent.hasClass('js_show')){
                $parent.removeClass('js_show');
            }else{
                $parent.siblings().removeClass('js_show');

                $parent.addClass('js_show');
                if(this.offsetTop + this.offsetHeight + innerH > $page.scrollTop() + winH){
                    var scrollTop = this.offsetTop + this.offsetHeight + innerH - winH + categorySpace;

                    if(scrollTop > this.offsetTop){
                        scrollTop = this.offsetTop - categorySpace;
                    }

                    $page.scrollTop(scrollTop);
                }
            }
        });
        */
    </script>
    <script type="text/html" id="tpl_page2">
        <div class="page">
            <div class="page__hd">
                <h1 class="page__title">page2</h1>
                <p class="page__desc">按钮</p>
            </div>
            <div class="page__bd page__bd_spacing">
                <a href="javascript:home();" class="weui-btn weui-btn_primary js_item">上一页面</a>
                <a href="#page3" class="weui-btn weui-btn_primary js_item">下个页面</a>
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
                <a href="#page2" class="weui-btn weui-btn_primary js_item">上一页面</a>
                <a href="#page4" class="weui-btn weui-btn_primary">下一页面</a>
            </div>
        </div>
    </script>

    <script type="text/html" id="tpl_page4">
        <div class="page">
            <div class="page__hd">
                <h1 class="page__title">page3</h1>
                <p class="page__desc">按钮</p>
            </div>
            <div class="page__bd page__bd_spacing">
                <a href="#page3" class="weui-btn weui-btn_primary js_item">上一页面</a>
                <a href="javascript:;" class="weui-btn weui-btn_primary">下一页面</a>
            </div>
        </div>
    </script>
</head>
<body ontouchstart>
<!--
<div class="weui-toptips weui-toptips_warn js_tooltips">错误提示</div>
-->

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
            <a href="javascript:;" class="weui-btn weui-btn_primary js_item" data-id="page2">page2</a>
            <a href="javascript:;" class="weui-btn weui-btn_primary js_item" data-id="page3">page3</a>
            <a href="javascript:;" class="weui-btn weui-btn_primary js_item" data-id="page4">page4</a>
        </div>
    </div>
</script>


</body>
</html>
