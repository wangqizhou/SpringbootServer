/**
 * Created by evis on 2017/1/3.
 */

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
window.onload = function () {
    var data = getQueryString("id");
    var $table = $('#download-table');
    $table.bootstrapTable({
        url: "downloads",
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
            params.categoryId = data;
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
                title: '名称',
                field: 'name',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '下载量',
                field: 'num',
                align: 'center',
                valign: 'middle'
            }
        ]
    });
};
