/**
 * Created by evis on 2017/3/7.
 */
/**
 * auditImage
 */
var curPage = 0; // 当前页
var lastPage; // 最后页
var direct = 0; // 方向
var begin;
var end;
var alen;
var apage;
var apageSize = 15;
var anum;
$(document).ready(
    function display3() {
        var html = getResource(1,15);
        anum = getAuditNumber();
        alen = anum;// 求这个表的总行数，剔除第一行介绍
        apage = alen % apageSize == 0 ? alen / apageSize : Math.floor(alen/ apageSize) + 1;// 根据记录条数，计算页数
        curPage = 1;// 设置当前为第一页
        displayPage3(1);// 显示第一页
        document.getElementById("abtn0").innerHTML = "当前 " + curPage + "/" + apage + " 页    每页 "; // 显示当前多少页
        document.getElementById("asjzl").innerHTML = "数据总量 " + alen + ""; // 显示数据量
        document.getElementById("apageSize").value = apageSize;
        $("#abtn1").click(function firstPage() { // 首页
            curPage = 1;
            direct = 0;
            displayPage3();
        });
        $("#abtn2").click(function frontPage() { // 上一页
            direct = -1;
            displayPage3();
        });
        $("#abtn3").click(function nextPage() { // 下一页
            direct = 1;
            displayPage3();
        });
        $("#abtn4").click(function lastPage() { // 尾页
            curPage = apage;
            direct = 0;
            displayPage3();
        });
        $("#abtn5").click(function changePage() { // 转页
            curPage = document.getElementById("achangePage").value * 1;
            if (!/^[1-9]\d*$/.test(curPage)) {
                showCommonModal("请输入正整数");
                return;
            }
            direct = 0;
            displayPage3();
        });
    }
);

function displayPage3() {
    if (curPage <= 1 && direct == -1) {
        direct = 0;
        showCommonModal("已经是第一页了");
        return;
    } else if (curPage >= apage && direct == 1) {
        direct = 0;
        showCommonModal("已经是最后一页了");
        return;
    }
    lastPage = curPage;
    // 修复当len=1时，curPage计算得0的bug
    if (alen >= apageSize) {
        curPage = (curPage + direct);
        getResource(curPage, apageSize);
        alen = getAuditNumber();
    } else {
        getResource(curPage, apageSize);
        alen = getAuditNumber();
        curPage = 1;
    }
    apage = alen % apageSize == 0 ? alen / apageSize : Math.floor(alen/ apageSize) + 1;// 根据记录条数，计算页数
    document.getElementById("abtn0").innerHTML = "当前 " + curPage + "/" + apage + " 页    每页 "; // 显示当前多少页
    document.getElementById("asjzl").innerHTML = "数据总量 " + alen + ""; // 显示数据量
}

/**
 * 获取未审核图片
 */
function getResource(pageNo, pageSize) {
    var judgeAll = 0;
    var list = new Array();
    var img = "";
    var id;
    var imageInfo = new Array();
    var index = 0;
    var imageArray = new Array();
    $.ajax({
        url : "../audit/image?page=" + pageNo + "&&" + "page_size=" + pageSize,
        dataType : 'json',
        contentType : "application/json",
        type : "get",
        async : true,
        success : function(data) {
            img += "<div class='imgtop'><button class='select'></button><button class='audittitle'>"
                + "提交审核通过图片"
                + "</button><button class='deletePic'>"
                + "删除审核未通过图片"
                + "</button><button class='send' data-counter='0'>"
                + "&#10004;" + "</button></div>";
            if (data == 0) {
                img += '';
                $("#resourceAudit").html(img);
            }
            img += "<div style='text-align:center;clear:both'></div>";
            img += "<ul class='imgul'>";
            $.each(data, function(i, imgObj) {
                imageInfo.push(imgObj);
                imageArray.push(imgObj.id);
                id = "image" + index;
                index += 1;
                img += '<li class="pic"  id ="' + id
                    + '" ><img class="images" src="'
                    + imgObj.thumbnail + '"></li>';
            });
            img += "</ul>";
            $("#resourceAudit").html(img);
            $.each(imageArray, function(i, singleImg) {
                var id = "image" + i;
                var oLis = document.getElementById(id);
                var judge = 0;
                if (oLis == null) {
                    alert(error);
                } else {
                    oLis.onclick = function() {
                        if (judge == 0) {
                            getImageCategory();
                            showImageInfo(imageInfo[i]);
                            window.open(imageInfo[i].url);
                            list.push(singleImg);
                            judge = 1;
                        } else if (judge == 1) {
                            list.pop(singleImg);
                            judge = 0;
                        }
                    };
                }
            })

            $('.pic').click(function() {
                $(this).toggleClass('selected');
                if ($('.pic.selected').length == 0)
                    $('.select').removeClass('selected');
                else
                    $('.select').addClass('selected');
                counter();
            });

            // all item selection
            $('.select').click(function() {
                if ($('.pic.selected').length == 0) {
                    $('.pic').addClass('selected');
                    $('.select').addClass('selected');
                } else {
                    $('.pic').removeClass('selected');
                    $('.select').removeClass('selected');
                }
                if (judgeAll == 0) {
                    for (var i = 0; i < imageArray.length; i++) {
                        list.push(imageArray[i]);
                    }
                    judgeAll = 1;
                } else if (judgeAll == 1) {
                    for (var i = 0; i < imageArray.length; i++) {
                        list.pop(imageArray[i]);
                    }
                    judgeAll = 0;
                }
                counter();
            });

            // number of selected items
            function counter() {
                if ($('.pic.selected').length > 0)
                    $('.send').addClass('selected');
                else
                    $('.send').removeClass('selected');
                $('.send').attr('data-counter',
                    $('.pic.selected').length);
            }

            $('.audittitle').click(function() {
                $.ajax({
                    url : "../audit/image/id",
                    dataType : 'json',
                    contentType : "application/json",
                    type : "put",
                    data : JSON.stringify(list),
                    async : true,
                    success : function(data) {
                        showCommonModal(data);
                        displayPage3();
                        getResource(pageNo, pageSize);
                    },
                    error : function(data) {
                        showCommonModal(data);
                    }
                });
            });

            $('.deletePic').click(function() {
                $.ajax({
                    url : "../audit/image/id",
                    dataType : 'json',
                    contentType : "application/json",
                    type : "delete",
                    data : JSON.stringify(list),
                    async : true,
                    success : function(data) {
                        showCommonModal(data);
                        displayPage3();
                        getResource(pageNo, pageSize);
                    },
                    error : function() {
                        showCommonModal(data);
                    }
                });
            });
        },
        error : function() {
            showCommonModal("error");
        }
    });
    return img;
}

//显示图片信息
function showImageInfo(image) {
    var id = image.id;
    var name = image.name;
    var categoryId = image.categoryId;
    var categoryName = image.categoryName;
    var createTime = image.createTime;
    var updateTime = image.updateTime;
    var format = image.format;
    var height = image.height;
    var width = image.width;
    var size = image.size;
    var url = image.url;
    var thumbnail = image.thumbnail;
    var ownerId = image.ownerId;
    var audit = image.audit;
    var downloadCount = image.downloadCount;

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

/**
 * 获取未审核图片数量
 *
 * @returns number
 */
function getAuditNumber() {
    var number;
    $.ajax({
        url : "../images/number?audit=false",
        dataType : 'json',
        contentType : "application/json",
        type : "get",
        async : false,
        success : function(data) {
            number = data;
        },
        error : function() {
        }
    });
    return number;
}