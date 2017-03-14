// 弹出提示框
function showCommonModal(content) {
    $('#commonModalBody p').html(content);
    $('#commonModal').modal('show');
    $('#commonModalSuccess').on("click", function () {
        $('#commonModal').modal('hide');
    });
}

//全局变量
var dbSelect = '';
var imageCategoryList = new Array();
var videoCategoryList = new Array();
var productCategoryList = new Array();

//日期选择
$(function () {
    $("#imageCreateTime").datepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: 'linked',//今日按钮
        format: "yyyy-mm-dd 00:00:00"//日期格式
    });
    $("#imageUpdateTime").datepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: 'linked',//今日按钮
        format: "yyyy-mm-dd 00:00:00"//日期格式
    });
    $("#videoDate").datepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: 'linked',//今日按钮
        format: "yyyy-mm-dd"//日期格式
    });
    $("#videoCreateTime").datepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: 'linked',//今日按钮
        format: "yyyy-mm-dd 00:00:00"//日期格式
    });
    $("#videoUpdateTime").datepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: 'linked',//今日按钮
        format: "yyyy-mm-dd 00:00:00"//日期格式
    });
    $("#videoReleaseTime").datepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: 'linked',//今日按钮
        format: "yyyy-mm-dd 00:00:00"//日期格式
    });
});

//获取数据库列表
function getDBList() {
    var $table = $('#dbTable');
    $table.bootstrapTable({
        url: "../db/backup",
        dataType: "json",
        method: "get",//请求方式
        singleSelect: true,
        clickToSelect: true,
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        pagination: false, //启动分页
        toolbar:"#exportDB",
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        onClickRow: function (row, $element) {
            if (dbSelect != row ){
                dbSelect = row;
            } else {
                dbSelect = '';
            }
        },
        onCheck: function (row, $element) {
        },
        columns: [
            {
                checkbox: true
            },
            {
                title: '数据库名称',
                field: 'filePath',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '创建时间',
                field: 'createTime',
                align: 'center'
            },
            {
                title: '删除',
                field: 'filePath',
                align: 'center',
                formatter:function(value,row,index){
                    var e = '<a href="#" onclick="delDB(\''+ row.filePath + '\')">删除</a> ';
                    return e;
                }
            },
            {
                title: '导出',
                field: 'filePath',
                align: 'center',
                formatter:function(value,row,index){
                    var e = '<a id="exportDB" href="#" onclick="exportDB(\''+ row.filePath + '\')">导出</a> ';
                    return e;
                }
            }
        ]
    });
}

//导出数据库
function exportDB(path) {
    $.ajax({
        type: "post",
        url: "db/exportDB",
        data: path,
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            $('#exportDB').attr('href', result);
        },
        error: function (result) {
            showCommonModal(result);
        }
    });
}

//还原数据库
function restoreDB() {
    var path = dbSelect['filePath'];
    $.ajax({
        type: "post",
        url: "../db/restore/" + path,
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            showCommonModal(result);
        },
        error: function (result) {
            showCommonModal(result);
        }
    });
    $('#dbModal').modal('hide');
}

//删除数据库
function delDB(path) {
    $('#commonModalBody p').html("是否删除选中的数据库？");
    $('#commonModal').modal('show');
    $('#commonModalSuccess').on("click", function () {
        $.ajax({
            type: "delete",
            url: "../db/delete/" + path,
            dataType: 'json',
            contentType: "application/json",
            success: function(result) {
                $('#commonModal').modal('hide');
                $('#dbTable').bootstrapTable('refresh');
            },
            error: function (result) {
                showCommonModal(result);
            }
        });
    });
}

//备份数据库
function backup() {
    $.ajax({
        type: "post",
        url: "../db/backup",
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            showCommonModal(result);
            $('#dbTable').bootstrapTable('refresh');
        },
        error: function (result) {
            showCommonModal(result);
        }
    });
}

//还原数据库
function restore() {
    var path = dbSelect['filePath'];
    if (path != undefined){
        $('#dbModalBody p').html("是否将数据库还原为："+ path + " 版本?");
        $('#dbModal').modal('show');
    } else {
        showCommonModal("请选择要还原的数据库！");
    }
}

//获取用户列表
function getAllUser() {

    var $table = $('#user-table');
    $table.bootstrapTable({
        url: "../users",
        dataType: "json",
        method: "get",//请求方式
        pagination: true, //分页
        singleSelect: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 20, 30],
        queryParamsType: "limit",//查询参数组织方式
        queryParams: function getParams(params) {
            //params obj
            params.other = "otherInfo";
            return params;
        },
        // searchOnEnterKey: true,//回车搜索
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        // showExport: true,//是否显示导出
        // exportDataType: "basic",//basic', 'all', 'selected'.
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        sidePagination: "server", //服务端处理分页
        columns: [
            {
                title: '操作',
                field: 'id',
                align: 'center',
                formatter:function(value,row,index){
                    var e = '<a href="#" onclick="editUser(\''+ index + '\')">编辑</a> ';
                    return e;
                }
            },
            {
                title: '用户ID',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '用户名',
                field: 'username',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '用户手机',
                field: 'phone',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '用户邮箱',
                field: 'email',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '昵称',
                field: 'nickname',
                align: 'center'
            },
            {
                title: '注册时间',
                field: 'registerTime',
                align: 'center'
            },
            {
                title: '位置',
                field: 'location',
                align: 'center'
            },
            {
                title: '性别',
                field: 'sex',
                align: 'center'
            },
            {
                title: '头像地址',
                field: 'figureUrl',
                align: 'center'
            },
            {
                title: '注册源',
                field: 'source',
                align: 'center'
            },
            {
                title: '设备类型',
                field: 'phoneDevice',
                align: 'center'
            },
            {
                title: '设备系统',
                field: 'phoneSystem',
                align: 'center'
            },
            {
                title: 'VR设备类型',
                field: 'vrDevice',
                align: 'center'
            }
        ]
    });
}

//跳转到编辑界面
function editUser(index) {
    //$element是当前tr的jquery对象
    var tr = $('#user-table').find('tr:eq('+ ++index +')');
    var arr= [];
    tr.find("td").each(function () {
        arr.push($.trim($(this).text()));
    });
    //获取表格中的一行数据
    var id = arr[1];
    // alert("id-->" +id);
    var name = arr[2];
    var phone = arr[3];
    var email = arr[4];
    var nick = arr[5];
    var registerTime = arr[6];
    var location = arr[7];
    var sex = arr[8];
    var figureUrl = arr[9];
    var source = arr[10];
    var phoneDevice = arr[11];
    var phoneSystem = arr[12];
    var vrDevice = arr[13];

    // alert("id-->" +id +"  name-->" +name+"  nick-->"+nick+"  location-->"+location+"  sex-->"+sex);
    //向模态框中传值
    $('#modify-userId').val(id);
    $('#modify-userName').val(name);
    $('#modify-phone').val(phone);
    $('#modify-email').val(email);
    $('#modify-nickName').val(nick);
    $('#modify-registerTime').val(registerTime);
    $('#modify-location').val(location);
    $('#modify-sex').val(sex);
    $('#modify-figureUrl').val(figureUrl);
    $('#modify-source').val(source);
    $('#modify-phoneDevice').val(phoneDevice);
    $('#modify-phoneSystem').val(phoneSystem);
    $('#modify-vrDevice').val(vrDevice);
    $('#userModal').modal('show');
}

//获取日志列表
function getAllLogger() {

    var $table = $('#logger-table');
    $table.bootstrapTable({
        url: "../loggers",
        dataType: "json",
        method: "get",//请求方式
        pagination: true, //分页
        singleSelect: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 20, 30],
        queryParamsType: "limit",//查询参数组织方式
        queryParams: function getParams(params) {
            //params obj
            params.other = "otherInfo";
            return params;
        },
        // searchOnEnterKey: true,//回车搜索
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        // showExport: true,//是否显示导出
        // exportDataType: "basic",//basic', 'all', 'selected'.
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        sidePagination: "server", //服务端处理分页
        columns: [
            {
                title: '日志ID',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '日志时间',
                field: 'time',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '日志Action',
                field: 'action',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '日志Owner',
                field: 'owner',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '日志信息',
                field: 'message',
                align: 'center'
            }
        ]
    });
}

//修改用户信息
function updateUser() {
    var id = $('#modify-userId').val();
    var name = $('#modify-userName').val();
    var phone = $('#modify-phone').val();
    var email = $('#modify-email').val();
    var nick = $('#modify-nickName').val();
    var registerTime = $('#modify-registerTime').val();
    var location = $('#modify-location').val();
    var sex = $('#modify-sex').val();
    var figureUrl = $('#modify-figureUrl').val();
    var source = $('#modify-source').val();
    var phoneDevice = $('#modify-phoneDevice').val();
    var phoneSystem = $('#modify-phoneSystem').val();
    var vrDevice = $('#modify-vrDevice').val();
    var data = {"id":id, "username":name,"phone":phone, "email":email, "nickname":nick, "registerTime":registerTime, "location":location, "sex":sex,
    "figureUrl":figureUrl, "source":source, "phoneDevice":phoneDevice, "phoneSystem":phoneSystem, "vrDevice":vrDevice};
    $.ajax({
        type: "put",
        url: "../users",
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            //location.reload();
            $('#userModal').modal('hide');
            showCommonModal(result);
            $('#user-table').bootstrapTable('refresh', {silent: true});
        },
        error: function (result) {
            $('#userModal').modal('hide');
            showCommonModal(result);
        }
    });
}

//获取所有管理员
function getAllAdmin () {
    var $table = $('#admin-table');
    $table.bootstrapTable({
        url: "../admins",
        dataType: "json",
        method: "get",//请求方式
        pagination: false, //分页
        singleSelect: true,
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        toolbar: "#registerAdmin",
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "right",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        columns: [
            {
                title: '操作',
                field: 'id',
                align: 'center',
                formatter:function(value,row,index){
                    var e = '<a href="#" onclick="editAdmin(\''+ index + '\')">编辑</a> ';
                    return e;
                }
            },
            {
                title: '管理员ID',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '管理员名',
                field: 'username',
                align: 'center',
                valign: 'middle'
            },
            // {
            //     title: '密码',
            //     field: 'password',
            //     align: 'center',
            //     valign: 'middle'
            // },
            {
                title: '邮箱',
                field: 'email',
                align: 'center',
                valign: 'middle'
            },
            {
                title: 'enabled',
                field: 'enabled',
                align: 'center'
            },
            {
                title: '管理员权限',
                field: 'authority',
                align: 'center'
            }
        ]
    });
}

//编辑管理员信息
function editAdmin(index) {
    //$element是当前tr的jquery对象
    var tr = $('#admin-table').find('tr:eq('+ ++index +')');
    var arr= [];
    tr.find("td").each(function () {
        arr.push($.trim($(this).text()));
    });
    //获取表格中的一行数据
    var id = arr[1];
    // alert("id-->" +id);
    var name = arr[2];
    var password = "******";
    var email = arr[3];
    var enabled = arr[4];
    var authority = new Array();
    authority = arr[5].split(',');
    // alert("id-->" +id +"  name-->" +name+"  nick-->"+nick+"  location-->"+location+"  sex-->"+sex);
    //向模态框中传值
    $('#modify-adminId').val(id);
    $('#modify-adminName').val(name);
    $('#modify-adminPassword').val(password);
    $('#modify-adminEmail').val(email);
    $('#modify-enabled').val(enabled);
    $('#modify-authority').selectpicker('val', authority);
    $('#adminModal').modal('show');
}

//更新管理员
function updateAdmin () {
    var id = $('#modify-adminId').val();
    var name = $('#modify-adminName').val();
    var password = null;
    $('#modify-adminPassword').change(function () {
        password = encrypt($('#modify-adminPassword').val());
    });
    var email = $('#modify-adminEmail').val();
    var enabled = $('#modify-enabled').val();
    var authority = $('#modify-authority').val();
    var data = {"id":id, "username":name, "password":password, "email":email, "enabled":enabled, "authority":authority.toString()};
    $.ajax({
        type: "put",
        url: "../admins",
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            //location.reload();
            $('#adminModal').modal('hide');
            showCommonModal(result);
            $('#admin-table').bootstrapTable('refresh', {silent: true});
        },
        error: function (result) {
            $('#adminModal').modal('hide');
            showCommonModal(result);
        }
    });
}

//获取上传页面的图片分类
function getImageCategory () {
    $.ajax({
        type: "get",
        url: "../categories/type/image",
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            var html = '';
            var imageCategoryId = '';
            var imageCategoryName = '';
            $.each (result, function (i, categories) {
                imageCategoryList.push(categories);
                html += '<li><a href="#">'+categories.name+'</a></li>';
                imageCategoryId += '<li><a href="#">'+categories.id+'</a></li>';
                imageCategoryName += '<li><a href="#">'+categories.name+'</a></li>';
            });
            $('#imageCategoryList').html(html);
            $('#imageCategoryId').html(imageCategoryId);
            $('#imageCategoryName').html(imageCategoryName);
        },
        error: function (result) {

        }
    });
}

//获取上传页面的视频分类
function getVideoCategory () {
    $.ajax({
        type: "get",
        url: "../categories/type/video",
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            var html = '';
            var videoCategoryId = '';
            var videoCategoryName = '';
            $.each (result, function (i, categories) {
                videoCategoryList.push(categories);
                html += '<li><a href="#">'+categories.name+'</a></li>';
                videoCategoryId += '<li><a href="#">'+categories.id+'</a></li>';
                videoCategoryName += '<li><a href="#">'+categories.name+'</a></li>';
            });
            $('#videoCategoryList').html(html);
            $('#videoCategoryId').html(videoCategoryId);
            $('#videoCategoryName').html(videoCategoryName);
        },
        error: function (result) {

        }
    });
}

//获取上传页面的产品分类
function getProductCategory() {
    $.ajax({
        type: "get",
        url: "../categories/type/product",
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            var html = '';
            var productCategoryId = '';
            var productCategoryName = '';
            $.each (result, function (i, categories) {
                productCategoryList.push(categories);
                html += '<li><a href="#">'+categories.name+'</a></li>';
                productCategoryId += '<li><a href="#">'+categories.id+'</a></li>';
                productCategoryName += '<li><a href="#">'+categories.name+'</a></li>';
            });
            $('#productCategoryList').html(html);
            $('#productCategoryId').html(productCategoryId);
            $('#productCategoryName').html(productCategoryName);
        },
        error: function (result) {

        }
    });
}

//显示选择的分类
$(function () {
    $('#imageCategoryList').on('click', function(e) {
        var $target = $(e.target);
        $('#showImageCategory').val($target.text())
    });

    $('#videoCategoryList').on('click', function(e) {
        var $target = $(e.target);
        $('#showVideoCategory').val($target.text())
    });

    $('#productCategoryList').on('click', function(e) {
        var $target = $(e.target);
        $('#showProductCategory').val($target.text())
    });

    $('#imageCategoryId').on('click', function(e) {
        var $target = $(e.target);
        $('#modify-imageCategoryId').val($target.text());
        for (var i = 0 ; i <imageCategoryList.length ; i++){
            if (imageCategoryList[i].id == $target.text()) {
                $('#modify-imageCategoryName').val(imageCategoryList[i].name);
            }
        }
    });

    $('#imageCategoryName').on('click', function(e) {
        var $target = $(e.target);
        $('#modify-imageCategoryName').val($target.text());
        for (var i = 0 ; i <imageCategoryList.length ; i++){
            if (imageCategoryList[i].name == $target.text()) {
                $('#modify-imageCategoryId').val(imageCategoryList[i].id);
            }
        }
    });

    $('#videoCategoryId').on('click', function(e) {
        var $target = $(e.target);
        $('#modify-videoCategoryId').val($target.text());
        for (var i = 0 ; i <videoCategoryList.length ; i++){
            if (videoCategoryList[i].id == $target.text()) {
                $('#modify-videoCategoryName').val(videoCategoryList[i].name);
            }
        }
    });

    $('#videoCategoryName').on('click', function(e) {
        var $target = $(e.target);
        $('#modify-videoCategoryName').val($target.text());
        for (var i = 0 ; i <videoCategoryList.length ; i++){
            if (videoCategoryList[i].name == $target.text()) {
                $('#modify-videoCategoryId').val(videoCategoryList[i].id);
            }
        }
    });

    $('#productCategoryId').on('click', function(e) {
        var $target = $(e.target);
        $('#modify-productCategoryId').val($target.text());
        for (var i = 0 ; i <productCategoryList.length ; i++){
            if (productCategoryList[i].id == $target.text()) {
                $('#modify-productCategoryName').val(productCategoryList[i].name);
            }
        }
    });

    $('#productCategoryName').on('click', function(e) {
        var $target = $(e.target);
        $('#modify-productCategoryName').val($target.text());
        for (var i = 0 ; i <productCategoryList.length ; i++){
            if (productCategoryList[i].name == $target.text()) {
                $('#modify-productCategoryId').val(productCategoryList[i].id);
            }
        }
    });
});

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

function onprogress(e) {
    var loaded = e.loaded;     //已经上传大小情况
    var tot = e.total;      //附件总大小
    var per = Math.floor(100*loaded/tot);  //已经上传的百分比
    $("#prog").css("width",per + "%").text(per + "%");
    $('#progressModal').modal('show');
    $('#progressModalSuccess').on("click", function () {
        location.reload();
        $('#progressModal').modal('hide');
    });
}

//上传
$(document).on('ready', function(){

    //上传图片
    $("#imageFiles").fileinput({
        language: 'zh',
        uploadUrl: '../upload/images',
        uploadExtraData: function() {
            var data = {
                "categoryName" : $('#showImageCategory').val()
            }
            return data;
        },
        showPreview: true, //显示预览按钮
        showRemove: true, //显示移除按钮
        dropZoneEnabled: true,//是否显示拖拽区域
        uploadAsync: true, //默认异步上传
        showUpload: true, //是否显示上传按钮
        allowedFileExtensions : ['jpg', 'png','gif','mp4','avi'],
        overwriteInitial: false,
        validateInitialCount: true,
        maxFileCount: 9, //同时最多上传9个文件
        browseClass: "btn btn-primary", //按钮样式
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
        allowedFileTypes: ['image', 'video', 'flash'],
        slugCallback: function(filename) {
            return filename.replace('(', '_').replace(']', '_');
        }
    });

    //异步上传返回结果处理
    $('#imageFiles').on('fileerror', function(event, data, msg) {
        location.reload();
    });

    //异步上传返回结果处理
    $("#imageFiles").on("fileuploaded", function (event, data) {
        location.reload();
    });

    //上传前
    $('#imageFiles').on('filepreupload', function(event, data, previewId, index) {

    });

    $('#uploadVideoFiles').submit(function (e) {
        e.preventDefault();
        var files = new Array();
        var landscapeCoverUrl = document.getElementById('landscapeCoverUrl').files[0];
        files.push(landscapeCoverUrl);
        var portraitCoverUrl = document.getElementById('portraitCoverUrl').files[0];
        files.push(portraitCoverUrl);
        var preview1Url = document.getElementById('preview1Url').files[0];
        files.push(preview1Url);
        var preview2Url = document.getElementById('preview2Url').files[0];
        files.push(preview2Url);
        var preview3Url = document.getElementById('preview3Url').files[0];
        files.push(preview3Url);
        var video = document.getElementById('video').files[0];
        files.push(video);

        var formData = new FormData();
        for (var i = 0; i<files.length; i++){
            formData.append("videoFiles", files[i]);
        }

        formData.append("categoryName", $('#showVideoCategory').val());
        formData.append("properties", JSON.stringify($("#uploadVideoInfo").serializeObject()));

        //上传视频
        $.ajax({
            type: "post",
            url: "../upload/video",
            data: formData,
            dataType: 'json',
            processData : false,
            contentType: false,
            xhr: function(){
                var xhr = $.ajaxSettings.xhr();
                if(onprogress && xhr.upload) {
                    xhr.upload.addEventListener("progress" , onprogress, false);
                    return xhr;
                }
            },
            success: function(result) {
                // showCommonModal(result);
            },
            error: function (result) {
                showCommonModal(result);
            }
        });
    });

    //上传广告
    $("#productFiles").fileinput({
        language: 'zh',
        uploadUrl: '../upload/product', // you must set a valid URL here else you will get an error
        uploadExtraData:function (previewId, index) {
            var order = {
                categoryName : $('#showProductCategory').val(),
                properties: JSON.stringify($("#uploadProductInfo").serializeObject())
            };
            return order;
        },
        showPreview: true, //显示预览按钮
        showRemove: true, //显示移除按钮
        dropZoneEnabled: false,//是否显示拖拽区域
        uploadAsync: false, //默认异步上传
        showUpload: true, //是否显示上传按钮
        allowedFileExtensions : ['jpg', 'png','gif','mp4','avi'],
        overwriteInitial: false,
        validateInitialCount: true,
        maxFileCount: 1, //同时最多上传1个文件
        browseClass: "btn btn-primary", //按钮样式
        previewFileIcon: "<i class='glyphicon glyphicon-file'></i>",
        allowedFileTypes: ['image', 'video', 'flash'],
        slugCallback: function(filename) {
            return filename.replace('(', '_').replace(']', '_');
        }
    });

    //异步上传返回结果处理
    $('#productFiles').on('fileerror', function(event, data, msg) {
        location.reload();
    });

    //异步上传返回结果处理
    $("#productFiles").on("fileuploaded", function (event, data) {
        location.reload();
    });

    //上传前
    $('#productFiles').on('filepreupload', function(event, data, previewId, index) {

    });
});

//统计全部类型的数量
function statistics() {
    var $table = $('#statistic-table');
    $table.bootstrapTable({
        url: "../statistics",
        dataType: "json",
        method: "get",//请求方式
        pagination: false, //分页
        singleSelect: true,
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        columns: [
            {
                title: 'ContentType',
                field: 'type',
                align: 'center',
                valign: 'middle'
            },
            {
                title: 'CategoryName',
                field: 'category',
                align: 'center',
                valign: 'middle'
            },
            {
                title: 'Number',
                field: 'num',
                align: 'center',
                valign: 'middle'
            }
        ]
    });
}

//获取全部设备
function getAllDevice() {
    var $table = $('#device-table');
    $table.bootstrapTable({
        url: "../devices",
        dataType: "json",
        method: "get",//请求方式
        pagination: true, //分页
        singleSelect: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 20, 30],
        queryParamsType: "limit",//查询参数组织方式
        queryParams: function getParams(params) {
            //params obj
            params.other = "otherInfo";
            return params;
        },
        // searchOnEnterKey: true,//回车搜索
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        // showExport: true,//是否显示导出
        // exportDataType: "basic",//basic', 'all', 'selected'.
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        sidePagination: "server", //服务端处理分页
        columns: [
            {
                title: '操作',
                field: 'id',
                align: 'center',
                formatter:function(value,row,index){
                    var e = '<a href="#" onclick="editDevice(\''+ index + '\')">编辑</a> ';
                    return e;
                }
            },
            {
                title: '设备ID',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '设备类型',
                field: 'model',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '设备系统',
                field: 'system',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '登录地点',
                field: 'location',
                align: 'center',
                valign: 'middle'
            },
            {
                title: 'client',
                field: 'client',
                align: 'center'
            },
            {
                title: 'clientVersion',
                field: 'clientVersion',
                align: 'center'
            },
            {
                title: 'accessTime',
                field: 'accessTime',
                align: 'center'
            },
            {
                title: 'imei',
                field: 'imei',
                align: 'center'
            }
        ]
    });
}

//跳转到编辑界面
function editDevice(index) {
    //$element是当前tr的jquery对象
    var tr = $('#device-table').find('tr:eq('+ ++index +')');
    var arr= [];
    tr.find("td").each(function () {
        arr.push($.trim($(this).text()));
    });
    //获取表格中的一行数据
    var id = arr[1];
    // alert("id-->" +id);
    var model = arr[2];
    var system = arr[3];
    var location = arr[4];
    var client = arr[5];
    var clientVersion = arr[6];
    var accessTime = arr[7];
    var imei = arr[8];

    // alert("id-->" +id +"  name-->" +name+"  nick-->"+nick+"  location-->"+location+"  sex-->"+sex);
    //向模态框中传值
    $('#modify-deviceId').val(id);
    $('#modify-deviceModel').val(model);
    $('#modify-deviceSystem').val(system);
    $('#modify-deviceLocation').val(location);
    $('#modify-deviceClient').val(client);
    $('#modify-clientVersion').val(clientVersion);
    $('#modify-accessTime').val(accessTime);
    $('#modify-imei').val(imei);
    $('#deviceModal').modal('show');
}

//修改设备信息
function updateDevice() {
    var id = $('#modify-deviceId').val();
    var model = $('#modify-deviceModel').val();
    var system = $('#modify-deviceSystem').val();
    var location = $('#modify-deviceLocation').val();
    var client = $('#modify-deviceClient').val();
    var clientVersion = $('#modify-clientVersion').val();
    var asscessTime = $('#modify-accessTime').val();
    var imei = $('#modify-imei').val();
    var data = {"id":id, "model":model,"system":system, "location":location, "client":client, "clientVersion":clientVersion, "asscessTime":asscessTime, "imei":imei};
    $.ajax({
        type: "put",
        url: "../devices",
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            //location.reload();
            $('#deviceModal').modal('hide');
            showCommonModal(result);
            $('#device-table').bootstrapTable('refresh', {silent: true});
        },
        error: function (result) {
            $('#deviceModal').modal('hide');
            showCommonModal(result);
        }
    });
}

//跳转到downloadCount
function editCategory(index) {
    //$element是当前tr的jquery对象
    var tr = $('#category-table').find('tr:eq('+ ++index +')');
    var arr= [];
    tr.find("td").each(function () {
        arr.push($.trim($(this).text()));
    });
    var id = arr[1];
    window.open("../downloadCount?id="+id, "show download count");
}

//获取所有分类
function getCategories() {
    var $table = $('#category-table');
    $table.bootstrapTable({
        url: "../categories",
        dataType: "json",
        method: "get",//请求方式
        pagination: false, //分页
        singleSelect: true,
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        columns: [
            {
                title: '查看',
                field: 'id',
                align: 'center',
                formatter:function(value,row,index){
                    var e = '<a href="#" onclick="editCategory(\''+ index + '\')">查看</a> ';
                    return e;
                }
            },
            {
                title: 'Id',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: 'Name',
                field: 'name',
                align: 'center',
                valign: 'middle'
            },
            {
                title: 'Type',
                field: 'type',
                align: 'center',
                valign: 'middle'
            }
        ]
    });
}

//获取收藏记录
function getFavorites() {
    var $table = $('#favorite-table');
    $table.bootstrapTable({
        url: "../favorites",
        dataType: "json",
        method: "get",//请求方式
        pagination: true, //分页
        singleSelect: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 20, 30],
        queryParamsType: "limit",//查询参数组织方式
        queryParams: function getParams(params) {
            //params obj
            params.other = "otherInfo";
            return params;
        },
        // searchOnEnterKey: true,//回车搜索
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        // showExport: true,//是否显示导出
        // exportDataType: "basic",//basic', 'all', 'selected'.
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        sidePagination: "server", //服务端处理分页
        columns: [
            // {
            //     title: '操作',
            //     field: 'id',
            //     align: 'center',
            //     formatter:function(value,row,index){
            //         var e = '<a href="#" onclick="editFavorite(\''+ index + '\')">编辑</a> ';
            //         return e;
            //     }
            // },
            {
                title: 'ID',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '视频ID',
                field: 'videoId',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '视频名称',
                field: 'videoName',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '用户ID',
                field: 'userId',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '收藏时间',
                field: 'time',
                align: 'center'
            }
        ]
    });
}

//获取播放记录
function getPlayRecords() {
    var $table = $('#playRecord-table');
    $table.bootstrapTable({
        url: "../playRecords",
        dataType: "json",
        method: "get",//请求方式
        pagination: true, //分页
        singleSelect: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 20, 30],
        queryParamsType: "limit",//查询参数组织方式
        queryParams: function getParams(params) {
            //params obj
            params.other = "otherInfo";
            return params;
        },
        // searchOnEnterKey: true,//回车搜索
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        // showExport: true,//是否显示导出
        // exportDataType: "basic",//basic', 'all', 'selected'.
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        sidePagination: "server", //服务端处理分页
        columns: [
            // {
            //     title: '操作',
            //     field: 'id',
            //     align: 'center',
            //     formatter:function(value,row,index){
            //         var e = '<a href="#" onclick="editFavorite(\''+ index + '\')">编辑</a> ';
            //         return e;
            //     }
            // },
            {
                title: 'ID',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '视频ID',
                field: 'videoId',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '视频名称',
                field: 'videoName',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '用户ID',
                field: 'userId',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '起始时间',
                field: 'startTime',
                align: 'center'
            },
            {
                title: '结束时间',
                field: 'endTime',
                align: 'center'
            },
            {
                title: '时长',
                field: 'duration',
                align: 'center'
            },
            {
                title: 'client',
                field: 'client',
                align: 'center'
            },
            {
                title: 'clientVersion',
                field: 'clientVersion',
                align: 'center'
            }
        ]
    });
}

//获取新增用户
$(function () {
    $('#newUserRange').on('click', function (e) {
        var $target = $(e.target);
        $('#showNewUserRange').val($target.text());
    });

    $("#newUserDate").datepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: 'linked',//今日按钮
        format: "yyyy-mm-dd"//日期格式
    });

    $('#newUserGranularity').on('click', function (e) {
        var $target = $(e.target);
        $('#showNewUserGranularity').val($target.text());
    });

    $('#newUserType').on('click', function (e) {
        var $target = $(e.target);
        $('#showNewUserType').val($target.text());
    });

    //提交查询新增用户请求
    $('#submitNewUser').click(function () {
        var range = $('#showNewUserRange').val();
        var date = $('#showNewUserDate').val();
        var granularity = $('#showNewUserGranularity').val();
        var type = $('#showNewUserType').val();
        var $table = $('#newUser-table');
        if (range != '' && date != '' && granularity != '' && type != '') {
            if (type.indexOf("时间") > 0) {
                if (granularity == "周") {
                    $table.bootstrapTable('destroy');
                    $table.bootstrapTable({
                        url: "../users/new",
                        dataType: "json",
                        method: "get",//请求方式
                        pagination: true, //分页
                        singleSelect: true,
                        pageNumber: 1,
                        pageSize: 10,
                        pageList: [10, 20, 30],
                        queryParamsType: "limit",//查询参数组织方式
                        queryParams: function getParams(params) {
                            //params obj
                            params.other = "otherInfo";
                            params.range = range;
                            params.date = date;
                            params.granularity = granularity;
                            params.type = type;
                            return params;
                        },
                        // searchOnEnterKey: true,//回车搜索
                        showRefresh: true,//刷新按钮
                        showToggle: true,//表格转换为card
                        showColumns: true,//列选择按钮
                        undefinedText: null,
                        // showExport: true,//是否显示导出
                        // exportDataType: "basic",//basic', 'all', 'selected'.
                        buttonsAlign: "left",//按钮对齐方式
                        toolbarAlign: "left",//工具栏对齐方式
                        locale: "zh-CN",//中文支持,
                        search: false, //显示搜索框
                        sidePagination: "server", //服务端处理分页
                        columns: [
                            {
                                title: 'grade',
                                field: 'grade',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: '周开始时间',
                                field: 'startTime',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: '周结束时间',
                                field: 'endTime',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'count',
                                field: 'count',
                                align: 'center',
                                valign: 'middle'
                            }
                        ]
                    });

                } else {
                    $table.bootstrapTable('destroy');
                    $table.bootstrapTable({
                        url: "../users/new",
                        dataType: "json",
                        method: "get",//请求方式
                        pagination: true, //分页
                        singleSelect: true,
                        pageNumber: 1,
                        pageSize: 10,
                        pageList: [10, 20, 30],
                        queryParamsType: "limit",//查询参数组织方式
                        queryParams: function getParams(params) {
                            //params obj
                            params.other = "otherInfo";
                            params.range = range;
                            params.date = date;
                            params.granularity = granularity;
                            params.type = type;
                            return params;
                        },
                        // searchOnEnterKey: true,//回车搜索
                        showRefresh: true,//刷新按钮
                        showToggle: true,//表格转换为card
                        showColumns: true,//列选择按钮
                        undefinedText: null,
                        // showExport: true,//是否显示导出
                        // exportDataType: "basic",//basic', 'all', 'selected'.
                        buttonsAlign: "left",//按钮对齐方式
                        toolbarAlign: "left",//工具栏对齐方式
                        locale: "zh-CN",//中文支持,
                        search: false, //显示搜索框
                        sidePagination: "server", //服务端处理分页
                        columns: [
                            {
                                title: 'grade',
                                field: 'grade',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'count',
                                field: 'count',
                                align: 'center',
                                valign: 'middle'
                            }
                        ]
                    });

                }
            } else {
                if (granularity == "周") {
                    $table.bootstrapTable('destroy');
                    $table.bootstrapTable({
                        url: "../users/new",
                        dataType: "json",
                        method: "get",//请求方式
                        pagination: true, //分页
                        singleSelect: true,
                        pageNumber: 1,
                        pageSize: 10,
                        pageList: [10, 20, 30],
                        queryParamsType: "limit",//查询参数组织方式
                        queryParams: function getParams(params) {
                            //params obj
                            params.other = "otherInfo";
                            params.range = range;
                            params.date = date;
                            params.granularity = granularity;
                            params.type = type;
                            return params;
                        },
                        // searchOnEnterKey: true,//回车搜索
                        showRefresh: true,//刷新按钮
                        showToggle: true,//表格转换为card
                        showColumns: true,//列选择按钮
                        undefinedText: null,
                        // showExport: true,//是否显示导出
                        // exportDataType: "basic",//basic', 'all', 'selected'.
                        buttonsAlign: "left",//按钮对齐方式
                        toolbarAlign: "left",//工具栏对齐方式
                        locale: "zh-CN",//中文支持,
                        search: false, //显示搜索框
                        sidePagination: "server", //服务端处理分页
                        columns: [
                            {
                                title: 'grade',
                                field: 'grade',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: '周开始时间',
                                field: 'startTime',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: '周结束时间',
                                field: 'endTime',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'location',
                                field: 'location',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'count',
                                field: 'count',
                                align: 'center',
                                valign: 'middle'
                            }
                        ]
                    });

                } else {
                    $table.bootstrapTable('destroy');
                    $table.bootstrapTable({
                        url: "../users/new",
                        dataType: "json",
                        method: "get",//请求方式
                        pagination: true, //分页
                        singleSelect: true,
                        pageNumber: 1,
                        pageSize: 10,
                        pageList: [10, 20, 30],
                        queryParamsType: "limit",//查询参数组织方式
                        queryParams: function getParams(params) {
                            //params obj
                            params.other = "otherInfo";
                            params.range = range;
                            params.date = date;
                            params.granularity = granularity;
                            params.type = type;
                            return params;
                        },
                        // searchOnEnterKey: true,//回车搜索
                        showRefresh: true,//刷新按钮
                        showToggle: true,//表格转换为card
                        showColumns: true,//列选择按钮
                        undefinedText: null,
                        // showExport: true,//是否显示导出
                        // exportDataType: "basic",//basic', 'all', 'selected'.
                        buttonsAlign: "left",//按钮对齐方式
                        toolbarAlign: "left",//工具栏对齐方式
                        locale: "zh-CN",//中文支持,
                        search: false, //显示搜索框
                        sidePagination: "server", //服务端处理分页
                        columns: [
                            {
                                title: 'grade',
                                field: 'grade',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'location',
                                field: 'location',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'count',
                                field: 'count',
                                align: 'center',
                                valign: 'middle'
                            }
                        ]
                    });

                }

            }
        } else {
            showCommonModal("请选择完整的条件！");
        }
    });

    //导出查询的新增用户
    $('#exportNU').click(function () {
        var range = $('#showNewUserRange').val();
        var date = $('#showNewUserDate').val();
        var granularity = $('#showNewUserGranularity').val();
        var type = $('#showNewUserType').val();
        var data = {
            "range": range,
            "date": date,
            "granularity": granularity,
            "type": type
        };
        if (range != '' && date != '' && granularity != '' && type != '') {
            $.ajax({
                url: "../users/new",
                dataType: 'json',
                contentType: "application/json",
                type: "post",
                data: JSON.stringify(data),
                async: false,
                success: function (data) {
                    $('#exportNU').attr('href', data);
                },
                error: function (data) {
                    showCommonModal(data);
                }
            });
        } else {
            showCommonModal('请选择完整的条件！');
        }
    });
});

//获取活跃用户
$(function () {
    $('#activeUserRange').on('click', function (e) {
        var $target = $(e.target);
        $('#showActiveUserRange').val($target.text());
    });

    $("#activeUserDate").datepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: 'linked',//今日按钮
        format: "yyyy-mm-dd"//日期格式
    });

    $('#activeUserGranularity').on('click', function (e) {
        var $target = $(e.target);
        $('#showActiveUserGranularity').val($target.text());
    });

    $('#activeUserType').on('click', function (e) {
        var $target = $(e.target);
        $('#showActiveUserType').val($target.text());
    });

    //提交查询活跃用户请求
    $('#submitActiveUser').click(function () {
        var range = $('#showActiveUserRange').val();
        var date = $('#showActiveUserDate').val();
        var granularity = $('#showActiveUserGranularity').val();
        var type = $('#showActiveUserType').val();
        var $table = $('#activeUser-table');
        if (range != '' && date != '' && granularity != '' && type != '') {
            if (type.indexOf("时间") > 0) {
                if (granularity == "周") {
                    $table.bootstrapTable('destroy');
                    $table.bootstrapTable({
                        url: "../users/active",
                        dataType: "json",
                        method: "get",//请求方式
                        pagination: true, //分页
                        singleSelect: true,
                        pageNumber: 1,
                        pageSize: 10,
                        pageList: [10, 20, 30],
                        queryParamsType: "limit",//查询参数组织方式
                        queryParams: function getParams(params) {
                            //params obj
                            params.other = "otherInfo";
                            params.range = range;
                            params.date = date;
                            params.granularity = granularity;
                            params.type = type;
                            return params;
                        },
                        // searchOnEnterKey: true,//回车搜索
                        showRefresh: true,//刷新按钮
                        showToggle: true,//表格转换为card
                        showColumns: true,//列选择按钮
                        undefinedText: null,
                        // showExport: true,//是否显示导出
                        // exportDataType: "basic",//basic', 'all', 'selected'.
                        buttonsAlign: "left",//按钮对齐方式
                        toolbarAlign: "left",//工具栏对齐方式
                        locale: "zh-CN",//中文支持,
                        search: false, //显示搜索框
                        sidePagination: "server", //服务端处理分页
                        columns: [
                            {
                                title: 'grade',
                                field: 'grade',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: '周开始时间',
                                field: 'startTime',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: '周结束时间',
                                field: 'endTime',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'count',
                                field: 'count',
                                align: 'center',
                                valign: 'middle'
                            }
                        ]
                    });

                } else {
                    $table.bootstrapTable('destroy');
                    $table.bootstrapTable({
                        url: "../users/active",
                        dataType: "json",
                        method: "get",//请求方式
                        pagination: true, //分页
                        singleSelect: true,
                        pageNumber: 1,
                        pageSize: 10,
                        pageList: [10, 20, 30],
                        queryParamsType: "limit",//查询参数组织方式
                        queryParams: function getParams(params) {
                            //params obj
                            params.other = "otherInfo";
                            params.range = range;
                            params.date = date;
                            params.granularity = granularity;
                            params.type = type;
                            return params;
                        },
                        // searchOnEnterKey: true,//回车搜索
                        showRefresh: true,//刷新按钮
                        showToggle: true,//表格转换为card
                        showColumns: true,//列选择按钮
                        undefinedText: null,
                        // showExport: true,//是否显示导出
                        // exportDataType: "basic",//basic', 'all', 'selected'.
                        buttonsAlign: "left",//按钮对齐方式
                        toolbarAlign: "left",//工具栏对齐方式
                        locale: "zh-CN",//中文支持,
                        search: false, //显示搜索框
                        sidePagination: "server", //服务端处理分页
                        columns: [
                            {
                                title: 'grade',
                                field: 'grade',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'count',
                                field: 'count',
                                align: 'center',
                                valign: 'middle'
                            }
                        ]
                    });

                }
            } else {
                if (granularity == "周") {
                    $table.bootstrapTable('destroy');
                    $table.bootstrapTable({
                        url: "../users/active",
                        dataType: "json",
                        method: "get",//请求方式
                        pagination: true, //分页
                        singleSelect: true,
                        pageNumber: 1,
                        pageSize: 10,
                        pageList: [10, 20, 30],
                        queryParamsType: "limit",//查询参数组织方式
                        queryParams: function getParams(params) {
                            //params obj
                            params.other = "otherInfo";
                            params.range = range;
                            params.date = date;
                            params.granularity = granularity;
                            params.type = type;
                            return params;
                        },
                        // searchOnEnterKey: true,//回车搜索
                        showRefresh: true,//刷新按钮
                        showToggle: true,//表格转换为card
                        showColumns: true,//列选择按钮
                        undefinedText: null,
                        // showExport: true,//是否显示导出
                        // exportDataType: "basic",//basic', 'all', 'selected'.
                        buttonsAlign: "left",//按钮对齐方式
                        toolbarAlign: "left",//工具栏对齐方式
                        locale: "zh-CN",//中文支持,
                        search: false, //显示搜索框
                        sidePagination: "server", //服务端处理分页
                        columns: [
                            {
                                title: 'grade',
                                field: 'grade',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: '周开始时间',
                                field: 'startTime',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: '周结束时间',
                                field: 'endTime',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'location',
                                field: 'location',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'count',
                                field: 'count',
                                align: 'center',
                                valign: 'middle'
                            }
                        ]
                    });

                } else {
                    $table.bootstrapTable('destroy');
                    $table.bootstrapTable({
                        url: "../users/active",
                        dataType: "json",
                        method: "get",//请求方式
                        pagination: true, //分页
                        singleSelect: true,
                        pageNumber: 1,
                        pageSize: 10,
                        pageList: [10, 20, 30],
                        queryParamsType: "limit",//查询参数组织方式
                        queryParams: function getParams(params) {
                            //params obj
                            params.other = "otherInfo";
                            params.range = range;
                            params.date = date;
                            params.granularity = granularity;
                            params.type = type;
                            return params;
                        },
                        // searchOnEnterKey: true,//回车搜索
                        showRefresh: true,//刷新按钮
                        showToggle: true,//表格转换为card
                        showColumns: true,//列选择按钮
                        undefinedText: null,
                        // showExport: true,//是否显示导出
                        // exportDataType: "basic",//basic', 'all', 'selected'.
                        buttonsAlign: "left",//按钮对齐方式
                        toolbarAlign: "left",//工具栏对齐方式
                        locale: "zh-CN",//中文支持,
                        search: false, //显示搜索框
                        sidePagination: "server", //服务端处理分页
                        columns: [
                            {
                                title: 'grade',
                                field: 'grade',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'location',
                                field: 'location',
                                align: 'center',
                                valign: 'middle'
                            },
                            {
                                title: 'count',
                                field: 'count',
                                align: 'center',
                                valign: 'middle'
                            }
                        ]
                    });

                }

            }
        } else {
            showCommonModal("请选择完整的条件！");
        }
    });

    //导出查询的活跃用户
    $('#exportAU').click(function () {
        var range = $('#showActiveUserRange').val();
        var date = $('#showActiveUserDate').val();
        var granularity = $('#showActiveUserGranularity').val();
        var type = $('#showActiveUserType').val();
        var data = {
            "range": range,
            "date": date,
            "granularity": granularity,
            "type": type
        };
        if (range != '' && date != '' && granularity != '' && type != '') {
            $.ajax({
                url: "../users/active",
                dataType: 'json',
                contentType: "application/json",
                type: "post",
                data: JSON.stringify(data),
                async: false,
                success: function (data) {
                    $('#exportAU').attr('href', data);
                },
                error: function (data) {
                    showCommonModal(data);
                }
            });
        } else {
            showCommonModal('请选择完整的条件！');
        }
    });
});

//获取内容热度
$(function () {
    $('#range').on('click', function(e) {
        var $target = $(e.target);
        $('#heatRange').val($target.text());
    });

    $("#heatDates").datepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: 'linked',//今日按钮
        format: "yyyy-mm-dd"//日期格式
    });

    $('#granularity').on('click', function(e) {
        var $target = $(e.target);
        $('#heatGranularity').val($target.text());
    });

    $('#addNull').on('click', function(e) {
        var $target = $(e.target);
        $('#heatAddNull').val($target.text());
    });

    //提交查询内容热度请求
    $('#submitHeatContent').click(function () {
        var range = $('#heatRange').val();
        var date = $('#heatDate').val();
        var granularity = $('#heatGranularity').val();
        var flag = $('#heatAddNull').val();
        var $table = $('#heatContent-table');
        if (range != '' && date != '' && granularity != '' && flag != '') {
            $table.bootstrapTable('destroy');
            $table.bootstrapTable({
                url: "../videos/hot",
                dataType: "json",
                method: "get",//请求方式
                pagination: true, //分页
                singleSelect: true,
                pageNumber: 1,
                pageSize: 10,
                pageList: [10, 20, 30],
                queryParamsType: "limit",//查询参数组织方式
                queryParams: function getParams(params) {
                    //params obj
                    params.other = "otherInfo";
                    params.range = range;
                    params.date = date;
                    params.granularity = granularity;
                    params.flag = flag;
                    return params;
                },
                // searchOnEnterKey: true,//回车搜索
                showRefresh: true,//刷新按钮
                showToggle: true,//表格转换为card
                showColumns: true,//列选择按钮
                undefinedText: null,
                // showExport: true,//是否显示导出
                // exportDataType: "basic",//basic', 'all', 'selected'.
                buttonsAlign: "left",//按钮对齐方式
                toolbarAlign: "left",//工具栏对齐方式
                locale: "zh-CN",//中文支持,
                search: false, //显示搜索框
                sidePagination: "server", //服务端处理分页
                columns: [
                    // {
                    //     title: '操作',
                    //     field: 'id',
                    //     align: 'center',
                    //     formatter:function(value,row,index){
                    //         var e = '<a href="#" onclick="editDevice(\''+ index + '\')">编辑</a> ';
                    //         return e;
                    //     }
                    // },
                    {
                        title: 'grade',
                        field: 'grade',
                        align: 'center',
                        valign: 'middle'
                    },
                    {
                        title: 'startTime',
                        field: 'startTime',
                        align: 'center',
                        valign: 'middle'
                    },
                    {
                        title: 'endTime',
                        field: 'endTime',
                        align: 'center',
                        valign: 'middle'
                    },
                    {
                        title: 'videoId',
                        field: 'videoId',
                        align: 'center',
                        valign: 'middle'
                    },
                    {
                        title: 'videoName',
                        field: 'videoName',
                        align: 'center'
                    },
                    {
                        title: 'categoryId',
                        field: 'categoryId',
                        align: 'center'
                    },
                    {
                        title: 'totalCount',
                        field: 'totalCount',
                        align: 'center'
                    },
                    {
                        title: 'comCount',
                        field: 'comCount',
                        align: 'center'
                    }
                ]
            });
        } else {
            showCommonModal('请选择完整的条件！');
        }


    });

    //导出查询的内容热度
    $('#exportHeat').click(function () {
        var range = $('#heatRange').val();
        var date = $('#heatDate').val();
        var granularity = $('#heatGranularity').val();
        var flag = $('#heatAddNull').val();
        var data = {
            "range": range,
            "date": date,
            "granularity": granularity,
            "flag": flag
        };
        if (range != '' && date != '' && granularity != '' && flag != '') {
            $.ajax({
                url: "../videos/hot",
                dataType: 'json',
                contentType: "application/json",
                type: "post",
                data: JSON.stringify(data),
                async: false,
                success: function (data) {
                    $('#exportHeat').attr('href', data);
                },
                error: function (data) {
                    showCommonModal(data);
                }
            });
        } else {
            showCommonModal('请选择完整的条件！');
        }
    });

});

//获取所有图片
function getAllImages() {
    var $table = $('#image-table');
    $table.bootstrapTable({
        url: "../images",
        dataType: "json",
        method: "get",//请求方式
        pagination: true, //分页
        singleSelect: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 20, 30],
        queryParamsType: "limit",//查询参数组织方式
        queryParams: function getParams(params) {
            //params obj
            params.other = "otherInfo";
            return params;
        },
        // searchOnEnterKey: true,//回车搜索
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        // showExport: true,//是否显示导出
        // exportDataType: "basic",//basic', 'all', 'selected'.
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        sidePagination: "server", //服务端处理分页
        columns: [
            {
                title: '操作',
                field: 'id',
                align: 'center',
                formatter:function(value,row,index){
                    var e = '<a href="#" onclick="editImage(\''+ index + '\');getImageCategory();">编辑</a> ';
                        e += '<a href="#" onclick="deleteImageDialog(\''+ row.id + '\');" style="color: red">删除</a> ';
                    return e;
                }
            },
            {
                title: 'ID',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '图片名称',
                field: 'name',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '分类ID',
                field: 'categoryId',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '分类名称',
                field: 'categoryName',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '创建时间',
                field: 'createTime',
                align: 'center'
            },
            {
                title: '更新时间',
                field: 'updateTime',
                align: 'center'
            },
            {
                title: '图片格式',
                field: 'format',
                align: 'center'
            },
            {
                title: '高度',
                field: 'height',
                align: 'center'
            },
            {
                title: '宽度',
                field: 'width',
                align: 'center'
            },
            {
                title: '大小',
                field: 'size',
                align: 'center'
            },
            {
                title: 'url',
                field: 'url',
                align: 'center'
            },
            {
                title: 'thumbnail',
                field: 'thumbnail',
                align: 'center'
            },
            {
                title: 'ownerId',
                field: 'ownerId',
                align: 'center'
            },
            {
                title: '审核',
                field: 'audit',
                align: 'center'
            },
            {
                title: '下载量',
                field: 'downloadCount',
                align: 'center'
            }
        ]
    });
}

//显示要修改的图片信息
function editImage(index) {
    //$element是当前tr的jquery对象
    var tr = $('#image-table').find('tr:eq('+ ++index +')');
    var arr= [];
    tr.find("td").each(function () {
        arr.push($.trim($(this).text()));
    });

    var id = arr[1];
    var name = arr[2];
    var categoryId = arr[3];
    var categoryName = arr[4];
    var createTime = arr[5];
    var updateTime = arr[6];
    var format = arr[7];
    var height = arr[8];
    var width = arr[9];
    var size = arr[10];
    var url = arr[11];
    var thumbnail = arr[12];
    var ownerId = arr[13];
    var audit = arr[14];
    var downloadCount = arr[15];

    //向模态框中传值
    $('#modify-imageId').val(id);
    $('#modify-imageName').val(name);
    $('#modify-imageCategoryId').val(categoryId);
    $('#modify-imageCategoryName').val(categoryName);
    $('#modify-imageCreateTime').val(createTime);
    $('#modify-imageUpdateTime').val(updateTime);
    $('#modify-imageFormat').val(format);
    $('#modify-imageHeight').val(height);
    $('#modify-imageWidth').val(width);
    $('#modify-imageSize').val(size);
    $('#modify-imageUrl').val(url);
    $('#modify-imageThumbnail').val(thumbnail);
    $('#modify-imageOwnerId').val(ownerId);
    $('#modify-imageAudit').val(audit);
    $('#modify-imageDownloadCount').val(downloadCount);
    $('#imageModal').modal('show');
}

//修改图片
function updateImage() {
    var id = $('#modify-imageId').val();
    var name = $('#modify-imageName').val();
    var categoryId = $('#modify-imageCategoryId').val();
    var categoryName = $('#modify-imageCategoryName').val();
    var createTime = $('#modify-imageCreateTime').val();
    var updateTime = $('#modify-imageUpdateTime').val();
    var format = $('#modify-imageFormat').val();
    var height = $('#modify-imageHeight').val();
    var width = $('#modify-imageWidth').val();
    var size = $('#modify-imageSize').val();
    var url = $('#modify-imageUrl').val();
    var thumbnail = $('#modify-imageThumbnail').val();
    var ownerId = $('#modify-imageOwnerId').val();
    var audit = $('#modify-imageAudit').val();
    var downloadCount = $('#modify-imageDownloadCount').val();

    var data = {"id":id, "name":name,"categoryId":categoryId, "categoryName":categoryName
        , "createTime":createTime, "updateTime":updateTime, "format":format, "height":height
        , "width":width, "size":size, "url":url, "thumbnail":thumbnail
        , "ownerId":ownerId, "audit":audit, "downloadCount":downloadCount};
    $.ajax({
        type: "put",
        url: "../images",
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            $('#imageModal').modal('hide');
            showCommonModal(result);
            $('#image-table').bootstrapTable('refresh', {silent: true});
        },
        error: function (result) {
            $('#imageModal').modal('hide');
            showCommonModal(result);
        }
    });
}

//删除图片弹窗
function deleteImageDialog(id) {
    $('#commonModalBody p').html("是否删除选中的图片？");
    $('#commonModal').modal('show');
    $('#commonModalSuccess').on("click", function () {
        deleteImage(id);
    });
}

//删除图片
function deleteImage(id) {
    $.ajax({
        type: "delete",
        url: "../images",
        data: id,
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            $('#commonModal').modal('hide');
            $('#commonModalSuccess').off().on("click");
            $('#image-table').bootstrapTable('refresh', {silent: true});
        },
        error: function (result) {
            showCommonModal(result);
        }
    });
}

//获取所有视频
function getAllVideos() {
    var $table = $('#video-table');
    $table.bootstrapTable({
        url: "../videos",
        dataType: "json",
        method: "get",//请求方式
        pagination: true, //分页
        singleSelect: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 20, 30],
        queryParamsType: "limit",//查询参数组织方式
        queryParams: function getParams(params) {
            //params obj
            params.other = "otherInfo";
            return params;
        },
        // searchOnEnterKey: true,//回车搜索
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        // showExport: true,//是否显示导出
        // exportDataType: "basic",//basic', 'all', 'selected'.
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        sidePagination: "server", //服务端处理分页
        columns: [
            {
                title: '操作',
                field: 'id',
                align: 'center',
                formatter:function(value,row,index){
                    var e = '<a href="#" onclick="editVideo(\''+ index + '\');getVideoCategory();">编辑</a> ';
                    e += '<a href="#" onclick="deleteVideoDialog(\''+ row.id + '\');" style="color: red">删除</a> ';
                    return e;
                }
            },
            {
                title: 'ID',
                field: 'id',
                align: 'center'
            },
            {
                title: '图片名称',
                field: 'name',
                align: 'center'
            },
            {
                title: '分类ID',
                field: 'categoryId',
                align: 'center'
            },
            {
                title: '分类名称',
                field: 'categoryName',
                align: 'center'
            },
            {
                title: '创建时间',
                field: 'createTime',
                align: 'center'
            },
            {
                title: '更新时间',
                field: 'updateTime',
                align: 'center'
            },
            {
                title: '上映时间',
                field: 'releaseTime',
                align: 'center'
            },
            {
                title: '图片格式',
                field: 'format',
                align: 'center'
            },
            {
                title: '高度',
                field: 'height',
                align: 'center'
            },
            {
                title: '宽度',
                field: 'width',
                align: 'center'
            },
            {
                title: '大小',
                field: 'size',
                align: 'center'
            },
            {
                title: '时长',
                field: 'duration',
                align: 'center'
            },
            {
                title: '演员',
                field: 'actors',
                formatter: function (value, row, index) {
                    return "<textarea rows= '1' disabled='disabled'>" + value + "</textarea>";
                }
            },
            {
                title: '上映地点',
                field: 'location',
                align: 'center'
            },
            {
                title: 'url',
                field: 'url',
                align: 'center'
            },
            {
                title: 'landscapeCoverUrl',
                field: 'landscapeCoverUrl',
                align: 'center'
            },
            {
                title: 'portraitCoverUrl',
                field: 'portraitCoverUrl',
                align: 'center'
            },
            {
                title: 'preview1Url',
                field: 'preview1Url',
                align: 'center'
            },
            {
                title: 'preview2Url',
                field: 'preview2Url',
                align: 'center'
            },
            {
                title: 'preview3Url',
                field: 'preview3Url',
                align: 'center'
            },
            {
                title: 'brief',
                field: 'brief',
                align: 'center'
            },
            {
                title: 'introduction',
                field: 'introduction',
                formatter: function (value, row, index) {
                    return "<textarea rows= '1' disabled='disabled'>" + value + "</textarea>";
                }
            },
            {
                title: 'ownerId',
                field: 'ownerId',
                align: 'center'
            },
            {
                title: '审核',
                field: 'audit',
                align: 'center'
            },
            {
                title: '下载量',
                field: 'downloadCount',
                align: 'center'
            }
        ]
    });
}

//显示修改的视频信息
function editVideo(index) {
    //$element是当前tr的jquery对象
    var tr = $('#video-table').find('tr:eq('+ ++index +')');
    var arr= [];
    tr.find("td").each(function () {
        arr.push($.trim($(this).text()));
    });

    var id = arr[1];
    var name = arr[2];
    var categoryId = arr[3];
    var categoryName = arr[4];
    var createTime = arr[5];
    var updateTime = arr[6];
    var releaseTime = arr[7];
    var format = arr[8];
    var height = arr[9];
    var width = arr[10];
    var size = arr[11];
    var duration = arr[12];
    var actors = arr[13];
    var location = arr[14];
    var url = arr[15];
    var landscapeCoverUrl = arr[16];
    var portraitCoverUrl = arr[17];
    var preview1Url = arr[18];
    var preview2Url = arr[19];
    var preview3Url = arr[20];
    var brief = arr[21];
    var introduction = arr[22];
    var ownerId = arr[23];
    var audit = arr[24];
    var downloadCount = arr[25];

    //向模态框中传值
    $('#modify-videoId').val(id);
    $('#modify-videoName').val(name);
    $('#modify-videoCategoryId').val(categoryId);
    $('#modify-videoCategoryName').val(categoryName);
    $('#modify-videoCreateTime').val(createTime);
    $('#modify-videoUpdateTime').val(updateTime);
    $('#modify-videoReleaseTime').val(releaseTime);
    $('#modify-videoFormat').val(format);
    $('#modify-videoHeight').val(height);
    $('#modify-videoWidth').val(width);
    $('#modify-videoSize').val(size);
    $('#modify-videoDuration').val(duration);
    $('#modify-videoActors').val(actors);
    $('#modify-videoLocation').val(location);
    $('#modify-videoUrl').val(url);
    $('#modify-landscapeCoverUrl').val(landscapeCoverUrl);
    $('#modify-portraitCoverUrl').val(portraitCoverUrl);
    $('#modify-preview1Url').val(preview1Url);
    $('#modify-preview2Url').val(preview2Url);
    $('#modify-preview3Url').val(preview3Url);
    $('#modify-brief').val(brief);
    $('#modify-introduction').val(introduction);
    $('#modify-videoOwnerId').val(ownerId);
    $('#modify-videoAudit').val(audit);
    $('#modify-videoDownloadCount').val(downloadCount);
    $('#videoModal').modal('show');
}

//修改视频
function updateVideo() {
    var id = $('#modify-videoId').val();
    var name = $('#modify-videoName').val();
    var categoryId = $('#modify-videoCategoryId').val();
    var categoryName = $('#modify-videoCategoryName').val();
    var createTime = $('#modify-videoCreateTime').val();
    var updateTime = $('#modify-videoUpdateTime').val();
    var releaseTime = $('#modify-videoReleaseTime').val();
    var format = $('#modify-videoFormat').val();
    var height = $('#modify-videoHeight').val();
    var width = $('#modify-videoWidth').val();
    var size = $('#modify-videoSize').val();
    var duration = $('#modify-videoDuration').val();
    var actors = $('#modify-videoActors').val();
    var location = $('#modify-videoLocation').val();
    var url = $('#modify-videoUrl').val();
    var landscapeCoverUrl = $('#modify-landscapeCoverUrl').val();
    var portraitCoverUrl = $('#modify-portraitCoverUrl').val();
    var preview1Url = $('#modify-preview1Url').val();
    var preview2Url = $('#modify-preview2Url').val();
    var preview3Url = $('#modify-preview3Url').val();
    var brief = $('#modify-brief').val();
    var introduction = $('#modify-introduction').val();
    var ownerId = $('#modify-videoOwnerId').val();
    var audit = $('#modify-videoAudit').val();
    var downloadCount = $('#modify-videoDownloadCount').val();

    var data = {"id":id, "name":name,"categoryId":categoryId, "categoryName":categoryName
        , "createTime":createTime, "updateTime":updateTime, "releaseTime":releaseTime, "format":format, "height":height
        , "width":width, "size":size, "duration":duration, "actors":actors, "location":location, "url":url
        , "landscapeCoverUrl":landscapeCoverUrl, "portraitCoverUrl":portraitCoverUrl, "preview1Url":preview1Url, "preview2Url":preview2Url
        , "preview3Url":preview3Url, "brief":brief, "introduction":introduction
        , "ownerId":ownerId, "audit":audit, "downloadCount":downloadCount};
    $.ajax({
        type: "put",
        url: "../videos",
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            $('#videoModal').modal('hide');
            showCommonModal(result);
            $('#video-table').bootstrapTable('refresh', {silent: true});
        },
        error: function (result) {
            $('#videoModal').modal('hide');
            showCommonModal(result);
        }
    });
}

//删除视频弹窗
function deleteVideoDialog(id) {
    $('#commonModalBody p').html("是否删除选中的视频？");
    $('#commonModal').modal('show');
    $('#commonModalSuccess').on("click", function () {
        deleteVideo(id);
    });
}

//删除视频
function deleteVideo(id) {
    $.ajax({
        type: "delete",
        url: "../videos",
        data: id,
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            $('#commonModal').modal('hide');
            $('#commonModalSuccess').off().on("click");
            $('#video-table').bootstrapTable('refresh', {silent: true});
        },
        error: function (result) {
            showCommonModal(result);
        }
    });
}

//获取所有广告
function getAllProducts() {
    var $table = $('#product-table');
    $table.bootstrapTable({
        url: "../products",
        dataType: "json",
        method: "get",//请求方式
        pagination: true, //分页
        singleSelect: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 20, 30],
        queryParamsType: "limit",//查询参数组织方式
        queryParams: function getParams(params) {
            //params obj
            params.other = "otherInfo";
            return params;
        },
        // searchOnEnterKey: true,//回车搜索
        showRefresh: true,//刷新按钮
        showToggle: true,//表格转换为card
        showColumns: true,//列选择按钮
        undefinedText: null,
        // showExport: true,//是否显示导出
        // exportDataType: "basic",//basic', 'all', 'selected'.
        buttonsAlign: "left",//按钮对齐方式
        toolbarAlign: "left",//工具栏对齐方式
        locale: "zh-CN",//中文支持,
        search: false, //显示搜索框
        sidePagination: "server", //服务端处理分页
        columns: [
            {
                title: '操作',
                field: 'id',
                align: 'center',
                formatter:function(value,row,index){
                    var e = '<a href="#" onclick="editProduct(\''+ index + '\');getProductCategory();">编辑</a> ';
                    e += '<a href="#" onclick="deleteProductDialog(\''+ row.id + '\');" style="color: red">删除</a> ';
                    return e;
                }
            },
            {
                title: 'ID',
                field: 'id',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '广告名称',
                field: 'name',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '分类ID',
                field: 'categoryId',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '分类名称',
                field: 'categoryName',
                align: 'center',
                valign: 'middle'
            },
            {
                title: 'imgUrl',
                field: 'imgUrl',
                align: 'center'
            },
            {
                title: 'websiteUrl',
                field: 'websiteUrl',
                align: 'center'
            },
            {
                title: 'introduction',
                field: 'introduction',
                align: 'center'
            },
            {
                title: 'ownerId',
                field: 'ownerId',
                align: 'center'
            },
            {
                title: 'audit',
                field: 'audit',
                align: 'center'
            }
        ]
    });
}

//显示要修改的广告信息
function editProduct(index) {
    //$element是当前tr的jquery对象
    var tr = $('#product-table').find('tr:eq('+ ++index +')');
    var arr= [];
    tr.find("td").each(function () {
        arr.push($.trim($(this).text()));
    });

    var id = arr[1];
    var name = arr[2];
    var categoryId = arr[3];
    var categoryName = arr[4];
    var imgUrl = arr[5];
    var websiteUrl = arr[6];
    var introduction = arr[7];
    var ownerId = arr[8];
    var audit = arr[9];


    //向模态框中传值
    $('#modify-productId').val(id);
    $('#modify-productName').val(name);
    $('#modify-productCategoryId').val(categoryId);
    $('#modify-productCategoryName').val(categoryName);
    $('#modify-productImgUrl').val(imgUrl);
    $('#modify-productWebsiteUrl').val(websiteUrl);
    $('#modify-productIntroduction').val(introduction);
    $('#modify-productOwnerId').val(ownerId);
    $('#modify-productAudit').val(audit);
    $('#productModal').modal('show');
}

//修改广告
function updateProduct() {
    var id = $('#modify-productId').val();
    var name = $('#modify-productName').val();
    var categoryId = $('#modify-productCategoryId').val();
    var categoryName = $('#modify-productCategoryName').val();
    var imgUrl = $('#modify-productImgUrl').val();
    var websiteUrl = $('#modify-productWebsiteUrl').val();
    var introduction = $('#modify-productIntroduction').val();
    var ownerId = $('#modify-productOwnerId').val();
    var audit = $('#modify-productAudit').val();

    var data = {"id":id, "name":name,"categoryId":categoryId, "categoryName":categoryName
        , "imgUrl":imgUrl, "websiteUrl":websiteUrl, "introduction":introduction, "ownerId":ownerId, "audit":audit};
    $.ajax({
        type: "put",
        url: "../products",
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            $('#productModal').modal('hide');
            showCommonModal(result);
            $('#product-table').bootstrapTable('refresh', {silent: true});
        },
        error: function (result) {
            $('#productModal').modal('hide');
            showCommonModal(result);
        }
    });
}

//删除广告弹窗
function deleteProductDialog(id) {
    $('#commonModalBody p').html("是否删除选中的广告？");
    $('#commonModal').modal('show');
    $('#commonModalSuccess').on("click", function () {
        deleteProduct(id);
    });
}

//删除广告
function deleteProduct(id) {
    $.ajax({
        type: "delete",
        url: "../products",
        data: id,
        dataType: 'json',
        contentType: "application/json",
        success: function(result) {
            $('#commonModal').modal('hide');
            $('#commonModalSuccess').off().on("click");
            $('#product-table').bootstrapTable('refresh', {silent: true});
        },
        error: function (result) {
            showCommonModal(result);
        }
    });
}

// //添加cookies
// function setCookie(name,value) {
//     var Days = 30;
//     var exp = new Date();
//     exp.setTime(exp.getTime() + Days*24*60*60*1000);
//     document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
// }
//
// //读取cookies
// function getCookie(name) {
//     var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
//     if(arr=document.cookie.match(reg))
//         return unescape(arr[2]);
//     else
//         return null;
// }
//
// //删除cookies
// function delCookie(name) {
//     var exp = new Date();
//     exp.setTime(exp.getTime() - 1);
//     var cval=getCookie(name);
//     if(cval!=null)
//         document.cookie= name + "="+cval+";expires="+exp.toGMTString();
// }