/**
 * Created by evis on 2017/3/7.
 */
//审核广告
var pcurPage = 0; // 当前页
var plastPage; // 最后页
var pdirect = 0; // 方向
var pbegin;
var pend;
var palen;
var papage;
var papageSize = 15;
var panum;
$(document).ready(
    function display5() {
        var html = getProductResource(1, 15);
        panum = getProductAuditNumber();
        palen = panum;// 求这个表的总行数，剔除第一行介绍
        papage = palen % papageSize == 0 ? palen / papageSize : Math.floor(palen / papageSize) + 1;// 根据记录条数，计算页数
        pcurPage = 1;// 设置当前为第一页
        displayPage5(1);// 显示第一页
        document.getElementById("pabtn0").innerHTML = "当前 " + pcurPage + "/" + papage + " 页    每页 "; // 显示当前多少页
        document.getElementById("pasjzl").innerHTML = "数据总量 " + palen + ""; // 显示数据量
        document.getElementById("papageSize").value = papageSize;
        $("#pabtn1").click(function firstPage() { // 首页
            pcurPage = 1;
            pdirect = 0;
            displayPage5();
        });
        $("#pabtn2").click(function frontPage() { // 上一页
            pdirect = -1;
            displayPage5();
        });
        $("#pabtn3").click(function nextPage() { // 下一页
            pdirect = 1;
            displayPage5();
        });
        $("#pabtn4").click(function lastPage() { // 尾页
            pcurPage = papage;
            pdirect = 0;
            displayPage5();
        });
        $("#pabtn5").click(function changePage() { // 转页
            pcurPage = document.getElementById("pachangePage").value * 1;
            if (!/^[1-9]\d*$/.test(pcurPage)) {
                showCommonModal("请输入正整数");
                return;
            }
            pdirect = 0;
            displayPage5();
        });
    }
);

function displayPage5() {
    if (pcurPage <= 1 && pdirect == -1) {
        pdirect = 0;
        showCommonModal("已经是第一页了");
        return;
    } else if (pcurPage >= papage && pdirect == 1) {
        pdirect = 0;
        showCommonModal("已经是最后一页了");
        return;
    }
    plastPage = pcurPage;
    // 修复当len=1时，curPage计算得0的bug
    if (palen >= papageSize) {
        pcurPage = (pcurPage + pdirect);
        getProductResource(pcurPage, papageSize);
        palen = getProductAuditNumber();
    } else {
        getProductResource(pcurPage, papageSize);
        palen = getProductAuditNumber();
        pcurPage = 1;
    }
    papage = palen % papageSize == 0 ? palen / papageSize : Math.floor(palen / papageSize) + 1;// 根据记录条数，计算页数
    document.getElementById("pabtn0").innerHTML = "当前 " + pcurPage + "/" + papage + " 页    每页 "; // 显示当前多少页
    document.getElementById("pasjzl").innerHTML = "数据总量 " + palen + ""; // 显示数据量
}

/**
 * 获取未审核广告
 */
function getProductResource(pageNo, pageSize) {
    var judgeAll = 0;
    var list = new Array();
    var v = "";
    var id;
    var productInfo = new Array();
    var index = 0;
    var productArray = new Array();
    $.ajax({
        url : "../audit/product?page=" + pageNo + "&&" + "page_size=" + pageSize,
        dataType : 'json',
        contentType : "application/json",
        type : "get",
        async : true,
        success : function(data) {
            v += "<div class='imgtop'><button class='pselect'></button><button class='paudittitle'>"
                + "提交审核通过视频"
                + "</button><button class='pdeletePic'>"
                + "删除审核未通过视频"
                + "</button><button class='psend' data-counter='0'>"
                + "&#10004;" + "</button></div>";
            if (data == 0) {
                v += '';
                $("#videoAudit").html(v);
            }
            v += "<div style='text-align:center;clear:both'></div>";
            v += "<ul class='imgul'>";
            $.each(data, function(i, vObj) {
                productInfo.push(vObj);
                productArray.push(vObj);
                id = "product" + index;
                index += 1;
                v += '<li class="ppic"  id ="' + id
                    + '" ><img class="images" src="'
                    + vObj.imgUrl + '"></li>';
            });
            v += "</ul>";
            $("#productAudit").html(v);
            $.each(productArray, function(i, singleV) {
                var id = "product" + i;
                var voLis = document.getElementById(id);
                var judge = 0;
                if (voLis == null) {
                    alert(error);
                } else {
                    voLis.onclick = function() {
                        if (judge == 0) {
                            getProductCategory();
                            showProductInfo(productInfo[i]);
                            window.open(productInfo[i].imgUrl);
                            list.push(singleV.id);
                            judge = 1;
                        } else if (judge == 1) {
                            list.pop(singleV.id);
                            judge = 0;
                        }
                    };
                }
            })

            $('.ppic').click(function() {
                $(this).toggleClass('selected');
                if ($('.ppic.selected').length == 0)
                    $('.pselect').removeClass('selected');
                else
                    $('.pselect').addClass('selected');
                counter();
            });

            // all item selection
            $('.pselect').click(function() {
                if ($('.ppic.selected').length == 0) {
                    $('.ppic').addClass('selected');
                    $('.pselect').addClass('selected');
                } else {
                    $('.ppic').removeClass('selected');
                    $('.pselect').removeClass('selected');
                }
                if (judgeAll == 0) {
                    for (var i = 0; i < productArray.length; i++) {
                        list.push(productArray[i].id);
                    }
                    judgeAll = 1;
                } else if (judgeAll == 1) {
                    for (var i = 0; i < productArray.length; i++) {
                        list.pop(productArray[i].id);
                    }
                    judgeAll = 0;
                }
                counter();
            });

            // number of selected items
            function counter() {
                if ($('.ppic.selected').length > 0)
                    $('.psend').addClass('selected');
                else
                    $('.psend').removeClass('selected');
                $('.psend').attr('data-counter',
                    $('.ppic.selected').length);
            }

            $('.paudittitle').click(function() {
                $.ajax({
                    url : "../audit/product/id",
                    dataType : 'json',
                    contentType : "application/json",
                    type : "PUT",
                    data : JSON.stringify(list),
                    async : true,
                    success : function(data) {
                        showCommonModal(data);
                        displayPage5();
                        getProductResource(pageNo, pageSize);
                    },
                    error : function(data) {
                        showCommonModal(data);
                    }
                });
            });

            $('.pdeletePic').click(function() {
                $.ajax({
                    url : "../audit/product/id",
                    dataType : 'json',
                    contentType : "application/json",
                    type : "delete",
                    data : JSON.stringify(list),
                    async : true,
                    success : function(data) {
                        showCommonModal(data);
                        displayPage5();
                        getProductResource(pageNo, pageSize);
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

//显示广告信息
function showProductInfo(product) {
    var id = product.id;
    var name = product.name;
    var categoryId = product.categoryId;
    var categoryName = product.categoryName;
    var imgUrl = product.imgUrl;
    var websiteUrl = product.websiteUrl;
    var introduction = product.introduction;
    var ownerId = product.ownerId;
    var audit = product.audit;

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

/**
 * 获取未审核广告数量
 *
 * @returns number
 */
function getProductAuditNumber() {
    var number;
    $.ajax({
        url : "../products/number?audit=false",
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