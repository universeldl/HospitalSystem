$(document).ready(function () {
    $("#data_list").tableDnD({
        //滚动的速度
        scrollAmount: 10,
        onDragClass: 'highlight',
        //当拖动排序完成后
        onDrop: function (table, row) {
            //获取id为table的元素
            var table = document.getElementById("data_list");
            //获取table元素所包含的tr元素集合
            var tr = table.getElementsByTagName("tr");
            //遍历所有的tr
            for (var i = 0; i < tr.length; i++) {
                //获取拖动排序结束后新表格中，row id的结果
                var rowid = tr[i].getAttribute("id");
                console.log("排序完成后表格的第 " + (i + 1) + " 行id为 : " + rowid);
            }

            console.log($('#data_list').tableDnDSerialize());

            let postdata = "surveyId=" + getQueryVariable("surveyId") + "&" + $('#data_list').tableDnDSerialize();
            ajax(
                {
                    method: 'POST',
                    url: 'doctor/surveyManageAction_sortQuestion.action',
                    params: postdata,
                    callback: function (data) {
                        if (data == 1) {
                            showInfo("更新成功");
                        } else {
                            showInfo("更新失败");
                        }
                    }
                }
            );

        },
        onDragStart: function (table, row) {
            console.log(row.id);
        },
    });
});

function getQueryVariable(variable)
{
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}