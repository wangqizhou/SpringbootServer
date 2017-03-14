/**
 * Created by evis on 2017/3/7.
 */
//审核视频
var vcurPage = 0; // 当前页
var vlastPage; // 最后页
var vdirect = 0; // 方向
var vbegin;
var vend;
var valen;
var vapage;
var vapageSize = 15;
var vanum;
$(document).ready(
    function display4() {
        var html = getVideoResource(1, 15);
        vanum = getVideoAuditNumber();
        valen = vanum;// 求这个表的总行数，剔除第一行介绍
        vapage = valen % vapageSize == 0 ? valen / vapageSize : Math.floor(valen / vapageSize) + 1;// 根据记录条数，计算页数
        vcurPage = 1;// 设置当前为第一页
        displayPage4(1);// 显示第一页
        document.getElementById("vabtn0").innerHTML = "当前 " + vcurPage + "/" + vapage + " 页    每页 "; // 显示当前多少页
        document.getElementById("vasjzl").innerHTML = "数据总量 " + valen + ""; // 显示数据量
        document.getElementById("vapageSize").value = vapageSize;
        $("#vabtn1").click(function firstPage() { // 首页
            vcurPage = 1;
            vdirect = 0;
            displayPage4();
        });
        $("#vabtn2").click(function frontPage() { // 上一页
            vdirect = -1;
            displayPage4();
        });
        $("#vabtn3").click(function nextPage() { // 下一页
            vdirect = 1;
            displayPage4();
        });
        $("#vabtn4").click(function lastPage() { // 尾页
            vcurPage = vapage;
            vdirect = 0;
            displayPage4();
        });
        $("#vabtn5").click(function changePage() { // 转页
            vcurPage = document.getElementById("vachangePage").value * 1;
            if (!/^[1-9]\d*$/.test(vcurPage)) {
                showCommonModal("请输入正整数");
                return;
            }
            vdirect = 0;
            displayPage4();
        });
    }
);

function displayPage4() {
    if (vcurPage <= 1 && vdirect == -1) {
        vdirect = 0;
        showCommonModal("已经是第一页了");
        return;
    } else if (vcurPage >= vapage && vdirect == 1) {
        vdirect = 0;
        showCommonModal("已经是最后一页了");
        return;
    }
    vlastPage = vcurPage;
    // 修复当len=1时，curPage计算得0的bug
    if (valen >= vapageSize) {
        vcurPage = (vcurPage + vdirect);
        getVideoResource(vcurPage, vapageSize);
        valen = getVideoAuditNumber();
    } else {
        getVideoResource(vcurPage, vapageSize);
        valen = getVideoAuditNumber();
        vcurPage = 1;
    }
    vapage = valen % vapageSize == 0 ? valen / vapageSize : Math.floor(valen / vapageSize) + 1;// 根据记录条数，计算页数
    document.getElementById("vabtn0").innerHTML = "当前 " + vcurPage + "/" + vapage + " 页    每页 "; // 显示当前多少页
    document.getElementById("vasjzl").innerHTML = "数据总量 " + valen + ""; // 显示数据量
}

/**
 * 获取未审核视频
 */
function getVideoResource(pageNo, pageSize) {
    var judgeAll = 0;
    var list = new Array();
    var v = "";
    var id;
    var videoInfo = new Array();
    var index = 0;
    var videoArray = new Array();
    $.ajax({
        url : "../audit/video?page=" + pageNo + "&&" + "page_size=" + pageSize,
        dataType : 'json',
        contentType : "application/json",
        type : "get",
        async : true,
        success : function(data) {
            v += "<div class='imgtop'><button class='vselect'></button><button class='vaudittitle'>"
                + "提交审核通过视频"
                + "</button><button class='vdeletePic'>"
                + "删除审核未通过视频"
                + "</button><button class='vsend' data-counter='0'>"
                + "&#10004;" + "</button></div>";
            if (data == 0) {
                v += '';
                $("#videoAudit").html(v);
            }
            v += "<div style='text-align:center;clear:both'></div>";
            v += "<ul class='imgul'>";
            $.each(data, function(i, vObj) {
                videoInfo.push(vObj);
                videoArray.push(vObj);
                id = "video" + index;
                index += 1;
                v += '<li class="vpic"  id ="' + id
                    + '" ><img class="images" src="'
                    + vObj.preview1Url + '"></li>';
            });
            v += "</ul>";
            $("#videoAudit").html(v);
            $.each(videoArray, function(i, singleV) {
                var id = "video" + i;
                var voLis = document.getElementById(id);
                var judge = 0;
                if (voLis == null) {
                    alert(error);
                } else {
                    voLis.onclick = function() {
                        if (judge == 0) {
                            getVideoCategory();
                            showVideoInfo(videoInfo[i]);
                            window.open(videoInfo[i].url);
                            list.push(singleV.id);
                            judge = 1;
                        } else if (judge == 1) {
                            list.pop(singleV.id);
                            judge = 0;
                        }
                    };
                }
            })

            $('.vpic').click(function() {
                $(this).toggleClass('selected');
                if ($('.vpic.selected').length == 0)
                    $('.vselect').removeClass('selected');
                else
                    $('.vselect').addClass('selected');
                counter();
            });

            // all item selection
            $('.vselect').click(function() {
                if ($('.vpic.selected').length == 0) {
                    $('.vpic').addClass('selected');
                    $('.vselect').addClass('selected');
                } else {
                    $('.vpic').removeClass('selected');
                    $('.vselect').removeClass('selected');
                }
                if (judgeAll == 0) {
                    for (var i = 0; i < videoArray.length; i++) {
                        list.push(videoArray[i].id);
                    }
                    judgeAll = 1;
                } else if (judgeAll == 1) {
                    for (var i = 0; i < videoArray.length; i++) {
                        list.pop(videoArray[i].id);
                    }
                    judgeAll = 0;
                }
                counter();
            });

            // number of selected items
            function counter() {
                if ($('.vpic.selected').length > 0)
                    $('.vsend').addClass('selected');
                else
                    $('.vsend').removeClass('selected');
                $('.vsend').attr('data-counter',
                    $('.vpic.selected').length);
            }

            $('.vaudittitle').click(function() {
                $.ajax({
                    url : "../audit/video/id",
                    dataType : 'json',
                    contentType : "application/json",
                    type : "PUT",
                    data : JSON.stringify(list),
                    async : true,
                    success : function(data) {
                        showCommonModal(data);
                        displayPage4();
                        getVideoResource(pageNo, pageSize);
                    },
                    error : function(data) {
                        showCommonModal(data);
                    }
                });
            });

            $('.vdeletePic').click(function() {
                $.ajax({
                    url : "../audit/video/id",
                    dataType : 'json',
                    contentType : "application/json",
                    type : "delete",
                    data : JSON.stringify(list),
                    async : true,
                    success : function(data) {
                        showCommonModal(data);
                        displayPage4();
                        getVideoResource(pageNo, pageSize);
                    },
                    error : function(data) {
                        showCommonModal(data);
                    }
                });
            });
        },
        error : function() {
            showCommonModal("error");
        }
    });
    return v;
}

//显示视频信息
function showVideoInfo(video) {
    var id = video.id;
    var name = video.name;
    var categoryId = video.categoryId;
    var categoryName = video.categoryName;
    var createTime = video.createTime;
    var updateTime = video.updateTime;
    var releaseTime = video.releaseTime;
    var format = video.format;
    var height = video.height;
    var width = video.width;
    var size = video.size;
    var duration = video.duration;
    var actors = video.actors;
    var location = video.location;
    var url = video.url;
    var landscapeCoverUrl = video.landscapeCoverUrl;
    var portraitCoverUrl = video.portraitCoverUrl;
    var preview1Url = video.preview1Url;
    var preview2Url = video.preview2Url;
    var preview3Url = video.preview3Url;
    var brief = video.brief;
    var introduction = video.introduction;
    var ownerId = video.ownerId;
    var audit = video.audit;
    var downloadCount = video.downloadCount;

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

/**
 * 获取未审核视频数量
 *
 * @returns number
 */
function getVideoAuditNumber() {
    var number;
    $.ajax({
        url : "../videos/number?audit=false",
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