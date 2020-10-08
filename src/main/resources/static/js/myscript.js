//获取当前时间
function getNowFormatDate() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    var Hour = date.getHours();       // 获取当前小时数(0-23)
    var Minute = date.getMinutes();     // 获取当前分钟数(0-59)
    var Second = date.getSeconds();     // 获取当前秒数(0-59)
    var show_day = new Array('星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六');
    var day = date.getDay();
    if (Hour < 10) {
        Hour = "0" + Hour;
    }
    if (Minute < 10) {
        Minute = "0" + Minute;
    }
    if (Second < 10) {
        Second = "0" + Second;
    }
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = '<div><p>' + year + '年' + month + '月' + strDate + '号</p><p>' + show_day[day] + '</p></div>';
    var HMS = Hour + ':' + Minute + ':' + Second;
    var temp_time = year + '-' + month + '-' + strDate + ' ' + HMS;
    $('.nowTime li:nth-child(1)').html(HMS);
    $('.nowTime li:nth-child(2)').html(currentdate);
    //$('.topRec_List li div:nth-child(3)').html(temp_time);
    setTimeout(getNowFormatDate, 1000);//每隔1秒重新调用一次该函数
}


var update = {
    type: "get",
    url: "/update",    //向后端请求数据的url
    success: function (liItem) {
        if (liItem) {
            $(".maquee").find("ul").animate({
                marginTop: "-37px"
            }, 500, function () {
                $(this).append(liItem);
                $(this).find("li:first").remove();
                $(this).css({marginTop: "0px"});
            })
        }
    }
};


// 时间
getNowFormatDate();
// 数据实时更新 + 滚动
setInterval(function(){$.ajax(update)},3000);
// 随机生成发票数据
$("#randomInvoice").click(function (){
    $.ajax({
        type: "GET",
        url: "/randomInvoice",
        success: function (data) {
            var jsonobj = eval("(" + data + ")");
            $("#hashValue").val(jsonobj.hashValue);
            $("#invoiceNo").val(jsonobj.invoiceNo);
            $("#buyerName").val(jsonobj.buyerName);
            $("#buyerTaxesNo").val(jsonobj.buyerTaxesNo);
            $("#sellerName").val(jsonobj.sellerName);
            $("#sellerTaxesNo").val(jsonobj.sellerTaxesNo);
            $("#invoiceDate").val(jsonobj.invoiceDate);
            $("#invoiceType").val(jsonobj.invoiceType);
            $("#taxesPoint").val(jsonobj.taxesPoint);
            $("#taxes").val(jsonobj.taxes);
            $("#price").val(jsonobj.price);
            $("#pricePlusTaxes").val(jsonobj.pricePlusTaxes);
            $("#invoiceNumber").val(jsonobj.invoiceNumber);
            $("#statementSheet").val(jsonobj.statementSheet);
            $("#statementWeight").val(jsonobj.statementWeight);
            $("#timestamp").val(jsonobj.timestamp);
        }
    })
})


// 查询数据
$(document).on("click", "#btn-invoiceQuery", function () {
    //var jsonString = JSON.stringify(json);
    //var data = $('#invoiceForm').serialize()
    // alert(jsonString)
    var data = {"hashValue": $("#hashValueForQuery").val()}
    alert(JSON.stringify(data));
    $.ajax({
        type: 'GET',
        url: '/invoiceQuery',
        dataType: 'text',
        data: data,
        error: function (request) {
            alert("提交失败！");
        },
        success: function (data) {
                var jsonobj = eval("(" + data + ")");
                alert(jsonobj);
        }
    })
})



// setInterval('autoScroll(".maquee")', 2000);
//
// // 动态滚动
// function autoScroll(obj) {
//     var sz = $(obj).find("ul").find("li").size();
//     if (sz > 6) {
//         $(obj).find("ul").animate({
//                 marginTop: "-37px"
//             }, 500, function () {
//                 //var sz = $(this).find("li").size();
//                 //alert( "Size: " + sz);
//                 $(this).find("li:first").remove();
//                 $(this).css({marginTop: "0px"});
//             })
//     }
// }

// //定义一个avalonjs的控制器
// var viewmodel = avalon.define({
//     //id必须和页面上定义的ms-controller名字相同，否则无法控制页面
//     $id: "invoices",
//     datalist: {},
//
//     request: function () {
//         var update = {
//             type: "post",
//             url: "/update",    //向后端请求数据的url
//             data: {},
//             success: function (data) {
//                 viewmodel.invoice = data;
//                 setTimeout(function (){$.ajax(viewmodel);}, 3000);
//             }
//         }
//         $.ajax(update);
//     }
// });


// var getting = {
//     url:'请求的服务器地址',
//     dataType:'json',
//     success:function(res) {
//         $('#com').html(str);
//         setTimeout(function(){$.ajax(getting);},1000);//1秒后定时发送请求
//     }
// };
// $.ajax(getting)