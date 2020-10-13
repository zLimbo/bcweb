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


var invoiceUpdate = {
    type: "get",
    url: "/invoiceUpdate",    //向后端请求数据的url
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

var bcinfoUpdate = {
    type: "get",
    url: "/bcinfoUpdate",
    dataType: "json",
    success: function (jsonObj) {
        $("#chainId").html(jsonObj.chainId);
        $("#chainName").html(jsonObj.chainName);
        $("#genesisTS").html(jsonObj.genesisTS);
        $("#peerCount").html(jsonObj.peerCount);
        $("#blockNumber").html(jsonObj.blockNumber);

        $("#blockId").html(jsonObj.blockId);
        $("#blockJsonrpc").html(jsonObj.blockJsonrpc);
        $("#blockVersion").html(jsonObj.blockVersion);
        $("#headerTimestamp").html(jsonObj.headerTimestamp);
        $("#blockTxNumber").html(jsonObj.blockTxNumber);
        //alert(data["peerCount"]);
        //var jsonObj = eval("(" + data + ")");
    }
}


// 时间
getNowFormatDate();
// 数据实时更新 + 滚动
setInterval(function(){$.ajax(invoiceUpdate)},3000);
// 实施更新链信息
setInterval(function(){$.ajax(bcinfoUpdate)},3000);

// 随机生成发票数据
$("#randomInvoice").click(function (){
    $.ajax({
        type: "GET",
        url: "/randomInvoice",
        dataType: "json",
        success: function (jsonObj) {
            //var jsonObj = eval("(" + data + ")");
            $("#hashValue").val(jsonObj.hashValue);
            $("#invoiceNo").val(jsonObj.invoiceNo);
            $("#buyerName").val(jsonObj.buyerName);
            $("#buyerTaxesNo").val(jsonObj.buyerTaxesNo);
            $("#sellerName").val(jsonObj.sellerName);
            $("#sellerTaxesNo").val(jsonObj.sellerTaxesNo);
            $("#invoiceDate").val(jsonObj.invoiceDate);
            $("#invoiceType").val(jsonObj.invoiceType);
            $("#taxesPoint").val(jsonObj.taxesPoint);
            $("#taxes").val(jsonObj.taxes);
            $("#price").val(jsonObj.price);
            $("#pricePlusTaxes").val(jsonObj.pricePlusTaxes);
            $("#invoiceNumber").val(jsonObj.invoiceNumber);
            $("#statementSheet").val(jsonObj.statementSheet);
            $("#statementWeight").val(jsonObj.statementWeight);
            $("#timestamp").val(jsonObj.timestamp);
            $("#contractAddress").val(jsonObj.contractAddress);
        }
    })
})


// 查询数据
$("#btn-invoiceQuery").click(function (){
    $("#query-hashValue").val("");
    $("#query-invoiceNo").val("");
    $("#query-buyerName").val("");
    $("#query-buyerTaxesNo").val("");
    $("#query-sellerName").val("");
    $("#query-sellerTaxesNo").val("");
    $("#query-invoiceDate").val("");
    $("#query-invoiceType").val("");
    $("#query-taxesPoint").val("");
    $("#query-taxes").val("");
    $("#query-price").val("");
    $("#query-pricePlusTaxes").val("");
    $("#query-invoiceNumber").val("");
    $("#query-statementSheet").val("");
    $("#query-statementWeight").val("");
    $("#query-timestamp").val("");
    $("#query-contractAddress").val("");
// $(document).on("click", "#btn-invoiceQuery", function () {
    //var jsonString = JSON.stringify(json);
    //var data = $('#invoiceForm').serialize()
    // alert(jsonString)
    var data = {"hashValue": $("#hashValueForQuery").val()}
    //alert(JSON.stringify(data));
    $.ajax({
        type: 'GET',
        url: '/invoiceQuery',
        dataType: 'json',
        data: data,
        error: function (request) {
            //alert("提交错误");
        },
        success: function (jsonObj) {
            //var jsonObj = eval("(" + data + ")");
            $("#query-hashValue").val(jsonObj.hashValue);
            $("#query-invoiceNo").val(jsonObj.invoiceNo);
            $("#query-buyerName").val(jsonObj.buyerName);
            $("#query-buyerTaxesNo").val(jsonObj.buyerTaxesNo);
            $("#query-sellerName").val(jsonObj.sellerName);
            $("#query-sellerTaxesNo").val(jsonObj.sellerTaxesNo);
            $("#query-invoiceDate").val(jsonObj.invoiceDate);
            $("#query-invoiceType").val(jsonObj.invoiceType);
            $("#query-taxesPoint").val(jsonObj.taxesPoint);
            $("#query-taxes").val(jsonObj.taxes);
            $("#query-price").val(jsonObj.price);
            $("#query-pricePlusTaxes").val(jsonObj.pricePlusTaxes);
            $("#query-invoiceNumber").val(jsonObj.invoiceNumber);
            $("#query-statementSheet").val(jsonObj.statementSheet);
            $("#query-statementWeight").val(jsonObj.statementWeight);
            $("#query-timestamp").val(jsonObj.timestamp);
            $("#query-contractAddress").val(jsonObj.contractAddress);
        }
    })
})


$("#btn-insertModal").click(function () {
    $("#hashValue").val("");
    $("#invoiceNo").val("");
    $("#buyerName").val("");
    $("#buyerTaxesNo").val("");
    $("#sellerName").val("");
    $("#sellerTaxesNo").val("");
    $("#invoiceDate").val("");
    $("#invoiceType").val("");
    $("#taxesPoint").val("");
    $("#taxes").val("");
    $("#price").val("");
    $("#pricePlusTaxes").val("");
    $("#invoiceNumber").val("");
    $("#statementSheet").val("");
    $("#statementWeight").val("");
    $("#timestamp").val("");
    $("#contractAddress").val("");
})


// 插入数据
$("#btn-invoiceInsert").click(function () {
    var json=$("#form-invoiceInsert").serializeJSON();
    //alert(json);
    var jsonString = JSON.stringify(json);
    //alert(jsonString);

    $.ajax({
        contentType: "application/json; charset=utf-8",
        type: 'POST',
        url: '/invoiceInsert',
        data: jsonString,
        error: function (request) {
            alert("插入失败！");
        },
        success: function (liItem) {
            if (liItem === "哈希值重复！") {
                alert(liItem);
            } else if (liItem === "系统错误！") {

            } else if (liItem) {
                $(".maquee").find("ul").append(liItem);
            }
            $("#myModal-invoiceInsert").modal('hide');
        }
    })
})


var base = +new Date(2014, 9, 3);
var oneDay = 24 * 3600 * 1000;
var date = [];
var data = [Math.random() * 150];
var now = new Date(base);

function addData(shift) {
    now = [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/');
    date.push(now);
    data.push((Math.random() - 0.4) * 10 + data[data.length - 1]);

    if (shift) {
        date.shift();
        data.shift();
    }

    now = new Date(+new Date(now) + oneDay);
}

for (var i = 1; i < 100; i++) {
    addData();
}

option = {
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: date
    },
    yAxis: {
        boundaryGap: [0, '50%'],
        type: 'value'
    },
    series: [
        {
            name: '成交',
            type: 'line',
            smooth: true,
            symbol: 'none',
            stack: 'a',
            areaStyle: {
                normal: {}
            },
            data: data
        }
    ]
};

setInterval(function () {
    addData(true);
    myChart1.setOption({
        xAxis: {
            data: date
        },
        series: [{
            name: '成交',
            data: data
        }]
    });
}, 500);

// 基于准备好的dom，初始化echarts实例
var myChart1 = echarts.init(document.getElementById('main'));
myChart1.setOption(option)


// 基于准备好的dom，初始化echarts实例
var myChart2 = echarts.init(document.getElementById('main2'), 'wonderland');

 //myChart2.showLoading();  // 开启 loading 效果
setInterval(function () {
    $.ajax({
        type: "GET",
        url: "/graphUpdate",
        dataType: "json",
        success: function (jsonObj) {
            // myChart2.hideLoading();  // 隐藏 loading 效果
            //alert(jsonObj);
            // 指定图表的配置项和数据
            var option = {
                series : [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '45%',
                        center: ['40%', '40%'],
                        data:jsonObj.pieData,
                        roseType: 'angle',
                        itemStyle: {
                            normal: {
                                shadowBlur: 200,
                                shadowColor: 'rgba(0, 0, 0, 0.5)',
                            }
                        },
                        label: {
                            normal: {
                                show: true,
                                position: 'outter',
                                textStyle: {
                                    fontWeight: 200,
                                    fontSize: 12
                                },
                                formatter: '{b} : {c} ({d}%)'
                            }
                        }
                    }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart2.setOption(option);
        }
    })
}, 3000);