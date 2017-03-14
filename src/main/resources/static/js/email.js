/**
 * Created by evis on 2017/2/7.
 */
/*
 测试邮件服务器连接
 当前url为mail
 */
$('#testConnection').bind("click", function () {
    var $label = $('#testConnectionLabel');
    var $btn = $('#testConnection');

    $btn.text("正在连接...");
    $.ajax({
        url : "test",
        type : "get",
        dataType : 'json',
        contentType : "application/json",
        async : true,
        success : function(ret) {
            if (ret == "success") {
                $label.text("成功");
                $btn.text("连接成功");
            } else {
                $label.text("失败");
                $btn.text("连接失败");
            }
        },
        error : function(ret) {
            $label.text("失败");
            $btn.text("连接失败");
        }
    });
});

var r = new Array();
var all = new Array();

$(function($) {

    $.ajax({
        url: "getMembers",
        dataType: 'json',
        contentType: "application/json",
        type: "get",
        async: false,
        success: function (data) {
            r = data.slice();
            for ( var i=0; i < data.length; i++ ){
                var obj=document.getElementById('js_multiselect_to_1');
                obj.options.add(new Option(data[i].username,data[i].username));
            }
        },
        error: function (data) {}
    });

    $.ajax({
        url: "../admins",
        dataType: 'json',
        contentType: "application/json",
        type: "get",
        async: false,
        success: function (data) {
            all = data.slice();
            for (var l=0; l<data.length ; l++){
                for (var j=0; j<r.length ; j++){
                    if (r[j].username == data[l].username){
                        data.splice(l,1);
                    }
                }
            }
            for ( var i=0; i < data.length; i++ ){
                var obj=document.getElementById('js-multiselect');
                obj.options.add(new Option(data[i].username,data[i].username));
            }
        },
        error: function (data) {}
    });

    $('#js-multiselect').multiselect({
        right: '#js_multiselect_to_1',
        // rightAll: '#js_right_All_1',
        rightSelected: '#js_right_Selected_1',
        leftSelected: '#js_left_Selected_1',
        // leftAll: '#js_left_All_1',
        beforeMoveToRight: function($left, $right, $options) {
            var value = JSON.stringify($options.val());
            var v = value.replace("\"","").replace("\"","");
            for (var i=0; i<all.length ; i++){
                if (all[i].username == v){
                    r.push(all[i]);
                }
            }
            $.ajax({
                url: "addMembers",
                dataType: 'json',
                contentType: "application/json",
                type: "put",
                data : JSON.stringify(r),
                async: false,
                success: function (data) {},
                error: function (data) {}
            });
            return true;
        },
        beforeMoveToLeft: function($left, $right, $options) {
            var value = JSON.stringify($options.val());
            var v = value.replace("\"","").replace("\"","");
            for (var i=0; i<all.length ; i++){
                for (var j=0; j<r.length ; j++){
                    if (all[i].username == v && r[j].username == all[i].username){
                        r.splice(j,1);
                    }
                }
            }
            $.ajax({
                url: "addMembers",
                dataType: 'json',
                contentType: "application/json",
                type: "put",
                data : JSON.stringify(r),
                async: false,
                success: function (data) {},
                error: function (data) {}
            });
            return true;
        }
    });

});
