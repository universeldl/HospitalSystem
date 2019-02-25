var quesitonIndex = 0;
var questions;
var postdata = "";
var accessid = ''
var accesskey = ''
var host = ''
var policyBase64 = ''
var signature = ''
var callbackbody = ''
var key = ''
var expire = 0
var filenames = [];
var uploaded_count = 0;
var uploader;
var skipType = 0;

function getQuestions() {
    questions = $.parseJSON($.trim($("#questionStr").val()));
    get_signature();

    $("#startButton").fadeIn();
    $('#skipSurvey').fadeIn();
    $('#skipDeliveryInfo').fadeIn();
}

function questionStart() {
    $('#surveyName').hide();
    $('#surveyDescription').hide();
    $('#startButton').hide();
    $('#skipSurvey').hide();
    $('#skipDeliveryInfo').hide();
    displayPage(quesitonIndex);
}

function displayPage(index) {
    var showIndex = index + 1;
    var question = questions[index];

    $('#questionIndex').text("问题（" + showIndex + "/" + questions.length + "）").show();
    $('#questionContent').text(question.questionContent).show();

    displayChoices(question);
    displayButton(index);
}


function displayButton(index) {
    if (index == 0) {
        document.getElementById("lastButton").style.display = "none";

        document.getElementById("nextButton").innerHTML = "下一题"
        document.getElementById("nextButton").style.display = "";
    } else if (index == questions.length - 1) {
        document.getElementById("lastButton").innerHTML = "上一题"
        document.getElementById("lastButton").style.display = "";

        document.getElementById("nextButton").innerHTML = "提  交"
        document.getElementById("nextButton").style.display = "";
    } else {
        document.getElementById("lastButton").innerHTML = "上一题"
        document.getElementById("lastButton").style.display = "";

        document.getElementById("nextButton").innerHTML = "下一题"
        document.getElementById("nextButton").style.display = "";
    }
}

function displayChoices(question) {
    $("#checkBoxArea").empty().hide();
    $("#radioBoxArea").empty().hide();
    $("#textArea").hide();
    $("#dateSelection").hide();
    $("#uploaderdiv").hide();

    $("#textQuestionArea").val("");
    $("#textQuestionArea").attr("name","")
    $("#dateQuestion").attr("name","")

    if (question.questionType == 1) {
        $("#checkBoxArea").append(getMultiChoiceLabel(question));
        $("#checkBoxArea").show();
        if (question.textChoice == 1) {
            $("#textArea").show();
            $("#textQuestionArea").attr("placeholder","其他...");
            $("#textQuestionArea").attr("name","tq"+question.questionId);
            $("#textQuestionArea").attr("disabled",false);
        } else {
            $("#textArea").hide();
        }
    } else if (question.questionType == 2) {
        $("#radioBoxArea").append(getSingleChoiceLabel(question));
        $("#radioBoxArea").show();
        if (question.textChoice == 1) {
            $("#textArea").show();
            $("#textQuestionArea").attr("placeholder","其他...");
            $("#textQuestionArea").attr("name","tq"+question.questionId);
            $("#textQuestionArea").attr("disabled",true);
        }
    } else if (question.questionType == 3) {
        $("#textArea").show();
        $("#textQuestionArea").attr("placeholder","请输入答案...");
        $("#textQuestionArea").attr("name","tq"+question.questionId);
        $("#textQuestionArea").attr("disabled",false);
    } else if (question.questionType == 4) {
        $("#dateSelection").show();
        $("#dateQuestion").attr("name","tq"+question.questionId);
    } else if (question.questionType == 5) {
        document.getElementById("uploaderdiv").style.display = "";
        $("#selectfiles").fadeIn();
        $("#file-list").each(function(){
            var y = $(this).children();
            y.remove();
        });
        filenames = [];
    }
}

function getMultiChoiceLabel(question) {
    var str = "";
    var choices = question.choices;
    for (var cindex = 0; cindex < choices.length; cindex++) {
        var choice = choices[cindex];

        str = str + "<label class='weui-cell weui-check__label' for='question";
        str = str + question.questionId + "choice" + choice.choiceId + "'>"
        if (choice.choiceImgPath != "") {
            str = str + "<img src='" + choice.choiceImgPath + "' width=80px height=80px>"
        }
        str = str + "<div class='weui-cell__bd'> <p>" + choice.choiceContent + "</p> </div>";
        str = str + "<div class='weui-cell__ft'>";
        str = str + "<input type='checkbox' name='q";
        str = str + question.questionId + "'";
        str = str + " id='question" + question.questionId + "choice" + choice.choiceId + "'";
        str = str + " value='" + choice.choiceId + "' /> </div> </label>"
    }
    return str;
}

function getSingleChoiceLabel(question) {
    var str = "";
    var choices = question.choices;
    for (var cindex = 0; cindex < choices.length; cindex++) {
        var choice = choices[cindex];

        str = str + "<label class='weui-cell weui-check__label' for='question";
        str = str + question.questionId + "choice" + choice.choiceId + "'>"

        if (choice.choiceImgPath != "") {
            str = str + "<img src='" + choice.choiceImgPath + "' width=80px height=80px>"
        }

        str = str + "<div class='weui-cell__bd'> <p>" + choice.choiceContent + "</p> </div>";
        str = str + "<div class='weui-cell__ft'>";
        str = str + "<input type='radio' name='q";
        str = str + question.questionId + "'";
        str = str + " id='question" + question.questionId + "choice" + choice.choiceId + "'";

        if (question.textChoice == 1) {
            str = str + " onclick=disableTextArea(); "
        }
        str = str + " value='" + choice.choiceId + "' /> </div> </label>"
    }
    if (question.textChoice == 1) {
        str = str + "<label class='weui-cell weui-check__label' for='question";
        str = str + question.questionId + "choiceother'>"
        str = str + "<div class='weui-cell__bd'> <p> 其他 </p> </div>";
        str = str + "<div class='weui-cell__ft'>";
        str = str + "<input type='radio' name='q";
        str = str + question.questionId + "'";
        str = str + " id='question" + question.questionId + "choiceother'";
        str = str + " onclick=enableTextArea() /> </div></label>"
    }
    return str;
}

function next(str) {
    location.href = "#top";
    if (questions[quesitonIndex].questionType == 5) {
        if (str == '') {
        } else {
            postdata = postdata + "&tq" + questions[quesitonIndex].questionId + "=" + str;
        }
    } else {
        var data = $('form').serialize();
        var RegExp = /^tq\d+\=$/g
        if (data) {
            if (data != "") {
                if (RegExp.test(data)) {
                    showDialog2("请先答题", "确定");
                    return;
                } else {
                    if (postdata == "") {
                        postdata = data;
                    } else {
                        postdata = postdata + "&" + data;
                    }
                }
            } else {
                showDialog2("请先答题", "确定");
                return;
            }
        } else {
            showDialog2("请先答题", "确定");
            return;
        }
    }
    if (quesitonIndex == questions.length -1) {
        submit();
    } else {
        quesitonIndex = quesitonIndex + 1;
        displayPage(quesitonIndex);
    }
}


function last() {
    location.href = "#top";
    quesitonIndex = quesitonIndex - 1;
    var questinId = questions[quesitonIndex].questionId;


    remove = "tq" + questinId;
    index = postdata.indexOf(remove);
    if (index > 0) {
        postdata = postdata.substr(0, index-1);
    } else if (index == 0) {
        postdata = postdata.substr(0, index);
    }

    var remove = "q" + questinId;
    var index = postdata.indexOf(remove);
    if (index > 0) {
        postdata = postdata.substr(0, index-1);
    } else if (index == 0) {
        postdata = postdata.substr(0, index);
    }

    displayPage(quesitonIndex);
}

function showLoadingToast(str) {
    var $loadingToast = $('#loadingToast');
    if ($loadingToast.css('display') != 'none') {
        return;
    }
    $("#loadToastStr").html(str);
    $loadingToast.fadeIn(100);
}


function showToast(str) {
    var $toast = $('#toast');
    if ($toast.css('display') != 'none') return;
    $("#toastStr").html(str);
    $toast.fadeIn(100);
    setTimeout(function () {
        $toast.fadeOut(100);
    }, 2000);

}

function showDialog2(str1, str2) {
    var $dialog = $('#iosDialog2');
    $("#dialog2Str1").html(str1);
    $("#dialog2Str2").html(str2);
    $dialog.fadeIn(200);

    $dialog.on('click', '.weui-dialog__btn', function () {
        $(this).parents('.js_dialog').fadeOut(200);
    });
}

function enableTextArea() {
    $("#textQuestionArea").attr("disabled",false);
}
function disableTextArea(id) {
    $("#textQuestionArea").val("");
    $("#textQuestionArea").attr("disabled",true);
}

function hideLoadingToast() {
    $('#loadingToast').fadeOut(100);
}

function submit() {
    showLoadingToast("提交答案...");
    var po = "deliveryID=" + $.trim($("#deliveryID").val()) + "&";
    po = po + postdata;
    //alert("po = " + po);

    var xhr = $.ajax({
        method: 'POST',
        url: 'surveyAction_retrieveAnswer.action',
        data: po,
        dataType: "json",
        timeout:20000,
        scriptCharset: 'utf-8',
        success:function(data){ //请求成功的回调函数
            hideLoadingToast();
            if (data.success == 1) {
                var url =  "https://" + window.location.host;
                url = url + "/hospital-wechat/message.jsp?msg=答卷提交成功!<br/><br/>" + data.msg;
                window.location.href = url;
            } else if (data.success == -1) {
                var url =  "https://" + window.location.host;
                url = url + "/hospital-wechat/message.jsp?msg=答卷出错...";
                window.location.href = url;
            } else {
                showDialog2("提交失败，请稍后再试", "确认");
            }
        },
        complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
            if(status=='timeout'){//超时,status还有success,error等值的情况
                xhr.abort();    // 超时后中断请求
                hideLoadingToast();
                showDialog2("答卷提交失败，请稍后再试", "确认");
            }
        }
    });

}

function get_signature()
{
    var _ossConfig = $.parseJSON($.trim($("#ossConfig").val()));
    host = _ossConfig.host;
    policyBase64 = _ossConfig.policy;
    accessid = _ossConfig.accessid;
    signature = _ossConfig.signature;
    expire = _ossConfig.expire;
    callbackbody = _ossConfig.callback;
    key = _ossConfig.dir;
    return true;
};

function get_suffix(filename) {
    var pos = filename.lastIndexOf('.')
    var suffix = ''
    if (pos != -1) {
        suffix = filename.substring(pos)
    }
    return suffix;
}

function calculate_object_name(filename)
{
    var suffix = get_suffix(filename);
    var deliveryID = document.getElementById("deliveryID").value;
    var name = $.trim($("#openid").val()) + '/' +
        deliveryID + "_" + quesitonIndex + "_" +filenames.length + suffix;
    return name;
}

/*function get_uploaded_object_name(filename)
{
    return g_object_name
}*/

function set_upload_param(up, file)
{
    if (file.name != '') {
        file.name = calculate_object_name(file.name)
        new_multipart_params = {
            'key' : key + file.name,
            'policy': policyBase64,
            'OSSAccessKeyId': accessid,
            'success_action_status' : '200', //让服务端返回200,不然，默认会返回204
            'callback' : callbackbody,
            'signature': signature,
        };

        up.setOption({
            'url': host,
            'multipart_params': new_multipart_params
        });
    }
}


var uploader = new plupload.Uploader({
    runtimes : 'html5,flash,silverlight,html4',
    browse_button : 'selectfiles',
    container: document.getElementById('container'),
    flash_swf_url : 'lib/plupload/js/Moxie.swf',
    silverlight_xap_url : 'lib/plupload/js/Moxie.xap',
    url : host,

    resize: {
        width: 1920,//指定压缩后图片的宽度，如果没有设置该属性则默认为原始图片的宽度
        quality: 90,//压缩后图片的质量，只对jpg格式的图片有效，默认为90。quality可以跟width和height一起使用，但也可以单独使用，单独使用时，压缩后图片的宽高不会变化，但由于质量降低了，所以体积也会变小
        preserve_headers: true//压缩后是否保留图片的元数据，true为保留，false为不保留,默认为true。删除图片的元数据能使图片的体积减小一点点
    },

    filters: {
        mime_types : [
            { title : "Image files", extensions : "image/*,jpg,jpeg,png,gif,pdf" },
        ],
        max_file_size : '30mb', //最大只能上传10mb的文件
        prevent_duplicates : true
    },

    init: {
        PostInit: function() {
            document.getElementById('ossfile').innerHTML = '';

        },

        FilesAdded: function(up, files) {
            var len = len = files.length;
            for(var i = 0; i<len; i++){
                var html = '<li class="weui-uploader__file" id="file-' + files[i].id +'" style=""></li>';
                $(html).appendTo('#file-list');
                !function(i){
                    previewImage(files[i],function(imgsrc){
                        document.getElementById('file-'+files[i].id).style.cssText = "background-image:url("+imgsrc+")"
                    })
                }(i);
            }
        },

        FilesRemoved: function(up, files) {
            var len = len = files.length;
            for(var i = 0; i<len; i++) {
                document.getElementById('file-'+files[i].id).remove();
            }
        },

        BeforeUpload: function(up, file) {
            set_upload_param(up, file);
            showLoadingToast("图片上传中...");
        },

        UploadProgress: function(up, file) {
            document.getElementById('file-'+file.id).setAttribute("class",
                "weui-uploader__file weui-uploader__file_status");
            document.getElementById('file-'+file.id).innerHTML =
                '<div class="weui-uploader__file-content">' + file.percent + '%</div>';
        },

        FileUploaded: function(up, file, info) {
            if (info.status == 200)
            {
                ++uploaded_count;
                filenames.push(file.name);
            }
            else
            {
                //alert(info.response)
            }
        },

        UploadComplete: function(up, files) {
            if (files.length == 0) {
                if (questions[quesitonIndex].questionType == 5) {
                    skipType = 3;
                    showDialog1("本题需要上传照片吗？", "不需要", "需要");
                } else {
                    next("");
                }
                return;
            }

            if (files.length == uploaded_count) {
                hideLoadingToast();
                next(filenames.join(";"));
            } else {
                if (questions[quesitonIndex].questionType != 5) {
                    next("");
                }
            }
        },

        Error: function(up, err) {
            if (err.code == -600) {
                showDialog2("所选图片过大，请勿超过10M:" + err.file.name, "确定");
            } else if (err.code == -601) {
                showDialog2("所选文件不是图片:" + err.file.name, "确定");
            } else if (err.code == -602) {
                showDialog2("请勿重复上传图片:" + err.file.name, "确定");
            } else {

                showDialog2("图片\'"+err.file.name+"\'上传失败，请重新添加", "确定");
                document.getElementById('file-'+err.file.id).setAttribute("class",
                    "weui-uploader__file weui-uploader__file_status");
                document.getElementById('file-'+err.file.id).innerHTML =
                    '<div class="weui-uploader__file-content">' +
                    '<i class="weui-icon-warn"></i>' +
                    '</div>'
            }
            hideLoadingToast();
        }
    }
});


function previewImage(file,callback){
    if(!file) {
        return;
    }
    if (!/image\//.test(file.type)){
        callback("./img/survey/pdf.png");
    }else if(file.type=='image/gif'){ //gif使用FileReader进行预览,因为mOxie.Image只支持jpg和png
        var gif = new moxie.file.FileReader();
        gif.onload = function(){
            callback(gif.result);
            gif.destroy();
            gif = null;
        };
        gif.readAsDataURL(file.getSource());
    }else{
        var image = new moxie.image.Image();
        image.onload = function() {
            //image.downsize( 300, 300 );//先压缩一下要预览的图片,宽300，高300
            var imgsrc = image.type=='image/jpeg' ? image.getAsDataURL('image/jpeg',80) : image.getAsDataURL(); //得到图片src,实质为一个base64编码的数据
            callback && callback(imgsrc); //callback传入的参数为预览图片的url
            image.destroy();
            image = null;
        };
        image.load( file.getSource() );
    }
}

$('#nextButton').on("click", function() {
    uploader.start();
    return false;
});

$('#skipSurvey').on("click", function() {
    skipType = 1;
    showDialog1("确定以后不再接收本问卷的所有提醒吗？", "不再接收" ,"继续接收")
});

$('#skipDeliveryInfo').on("click", function() {
    skipType = 2;
    showDialog1("确定跳过本次问卷吗？", "跳过" ,"不跳过")
});

$("#file-list").on("click", "li", function(){
    $("#galleryImg").attr("style", this.getAttribute("style"));
    $("#gallery").fadeIn(100);
    $("#curFileName").val(this.getAttribute("id"));
});

$("#gallery").on("click", function(){
    $("#gallery").fadeOut(100);
    $("#curFileName").val("");
});

$("#galleryImgDel").on("click", function(){
    $("#gallery").fadeOut(100);
    var remove_file_name = $("#curFileName").val().slice(5);
    uploader.removeFile(remove_file_name);
});

$("#dialog1Str3").on('click', function () {
    $(this).parents('.js_dialog').fadeOut(200);
});

$("#dialog1Str2").on('click', function () {
    $(this).parents('.js_dialog').fadeOut(200);
    if (skipType == 1) {
        skipSurvey($.trim($("#deliveryID").val()))
    } else if (skipType == 2) {
        skipDeliveryInfo($.trim($("#deliveryID").val()))
    } else if (skipType == 3) {
        next("");
    }
    skipType = 0;
});

function skipDeliveryInfo(deliveryID) {
    var postdata = "openid=" + $.trim($("#openid").val()) +
        "&deliveryID=" + deliveryID;
    showLoadingToast("跳过问卷...")
    ajax(
        {
            method: 'POST',
            url: 'surveyAction_skipDeliveryInfo.action',
            params: postdata,
            callback: function (data) {
                hideLoadingToast();
                $('#loading').hide();
                if (data == 1) {
                    var url =  "https://" + window.location.host;
                    url = url + "/hospital-wechat/message.jsp?msg=设置成功，您将不再收到本次问卷的随访提醒。<br/><br/>";
                    window.location.href = url;
                } else if (data == -1) {
                    showDialog2("问卷错误", "确认");
                } else if (data == -2) {
                    showDialog2("用户名错误", "确认");
                } else {
                    showDialog2("设置失败，请稍后再试", "确认");
                }
            }
        }
    );
}

function skipSurvey(deliveryID) {
    var postdata = "openid=" + $.trim($("#openid").val()) +
        "&deliveryID=" + deliveryID;
    showLoadingToast("跳过问卷...")
    ajax(
        {
            method: 'POST',
            url: 'surveyAction_skipSurvey.action',
            params: postdata,
            callback: function (data) {
                hideLoadingToast();
                $('#loading').hide();
                if (data == 1) {
                    var url =  "https://" + window.location.host;
                    url = url + "/hospital-wechat/message.jsp?msg=设置成功!您将不会再收到本系列问卷的随访提醒。<br/><br/>";
                    window.location.href = url;
                } else if (data == -1) {
                    showDialog2("问卷错误", "确认");
                } else if (data == -2) {
                    showDialog2("用户名错误", "确认");
                } else {
                    showDialog2("设置失败，请稍后再试", "确认");
                }
            }
        }
    );
}

function showDialog1(str1, str2,str3) {
    var $dialog = $('#iosDialog1');
    $("#dialog1Str1").html(str1);
    $("#dialog1Str2").html(str2);
    $("#dialog1Str3").html(str3);
    $dialog.fadeIn(200);
}

uploader.init();
