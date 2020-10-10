package com.zlimbo.bcweb.domain;

import jnr.ffi.annotations.In;

import java.text.SimpleDateFormat;
import java.util.*;

public class Invoice {

    private String hashValue;
    private String invoiceNo;
    private String buyerName;
    private String buyerTaxesNo;
    private String sellerName;
    private String sellerTaxesNo;
    private String invoiceDate;
    private String invoiceType;
    private String taxesPoint;
    private String taxes;
    private String price;
    private String pricePlusTaxes;
    private String invoiceNumber;
    private String statementSheet;
    private String statementWeight;
    private String timestamp;
    private String contractAddress;

    public Invoice() { }

    public Invoice(String hashValue,
                   String invoiceNo,
                   String buyerName,
                   String buyerTaxesNo,
                   String sellerName,
                   String sellerTaxesNo,
                   String invoiceDate,
                   String invoiceType,
                   String taxesPoint,
                   String taxes,
                   String price,
                   String pricePlusTaxes,
                   String invoiceNumber,
                   String statementSheet,
                   String statementWeight,
                   String timestamp,
                   String contractAddress) {
        this.hashValue = hashValue;
        this.invoiceNo = invoiceNo;
        this.buyerName = buyerName;
        this.buyerTaxesNo = buyerTaxesNo;
        this.sellerName = sellerName;
        this.sellerTaxesNo = sellerTaxesNo;
        this.invoiceDate = invoiceDate;
        this.invoiceType = invoiceType;
        this.taxesPoint = taxesPoint;
        this.taxes = taxes;
        this.price = price;
        this.pricePlusTaxes = pricePlusTaxes;
        this.invoiceNumber = invoiceNumber;
        this.statementSheet = statementSheet;
        this.statementWeight = statementWeight;
        this.timestamp = timestamp;
        this.contractAddress = contractAddress;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTaxesNo() {
        return buyerTaxesNo;
    }

    public void setBuyerTaxesNo(String buyerTaxesNo) {
        this.buyerTaxesNo = buyerTaxesNo;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerTaxesNo() {
        return sellerTaxesNo;
    }

    public void setSellerTaxesNo(String sellerTaxesNo) {
        this.sellerTaxesNo = sellerTaxesNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getTaxesPoint() {
        return taxesPoint;
    }

    public void setTaxesPoint(String taxesPoint) {
        this.taxesPoint = taxesPoint;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricePlusTaxes() {
        return pricePlusTaxes;
    }

    public void setPricePlusTaxes(String pricePlusTaxes) {
        this.pricePlusTaxes = pricePlusTaxes;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getStatementSheet() {
        return statementSheet;
    }

    public void setStatementSheet(String statementSheet) {
        this.statementSheet = statementSheet;
    }

    public String getStatementWeight() {
        return statementWeight;
    }

    public void setStatementWeight(String statementWeight) {
        this.statementWeight = statementWeight;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }


    public static Invoice getRandomInvoice() {
        Random random = new Random();
        Invoice invoice = new Invoice();

        String hashValue = getHexString(32);
        String invoiceNo = getOctString(10);
        String buyerName = COMPANYS[random.nextInt(COMPANYS.length)];
        String buyerTaxesNo = COMPANYS_TAXESNO.get(buyerName);
        if (buyerTaxesNo == null) {
            buyerTaxesNo = getTAX_REGISTRATION_CODE();
            COMPANYS_TAXESNO.put(buyerName, buyerTaxesNo);
        }
        String sellerName = COMPANYS[random.nextInt(COMPANYS.length)];
        String sellerTaxesNo = COMPANYS_TAXESNO.get(sellerName);
        if (sellerTaxesNo == null) {
            sellerTaxesNo = getTAX_REGISTRATION_CODE();
            COMPANYS_TAXESNO.put(buyerName, sellerTaxesNo);
        }
        String invoiceDate = new SimpleDateFormat("yyyy-MM-dd" ).format(new Date());
        String invoiceType = INVOICE_KIND[random.nextInt(INVOICE_KIND.length)];
        String taxesPoint = (10 + random.nextInt(10)) + "%";
        int taxesRaw = 100 + random.nextInt(1000);
        int priceRaw = 10000 + random.nextInt(100000);
        String taxes = "" + taxesRaw;
        String price = "" + priceRaw;
        String pricePlusTaxes = "" + (taxesRaw + priceRaw);
        String invoiceNumber = "" + (1 + random.nextInt(3));
        String statementSheet = "" + (1 + random.nextInt(3));
        String statementWeight = (1 + random.nextInt(10)) + "kg";
        String timestamp = "" + System.currentTimeMillis();
        String contractAddress = getHexString(16);
        return new Invoice(
                hashValue,
                invoiceNo,
                buyerName,
                buyerTaxesNo,
                sellerName,
                sellerTaxesNo,
                invoiceDate,
                invoiceType,
                taxesPoint,
                taxes,
                price,
                pricePlusTaxes,
                invoiceNumber,
                statementSheet,
                statementWeight,
                timestamp,
                contractAddress);
    }


    final static String[] INVOICE_KIND = {"增值税专用发票", "普通发票", "专业发票"};
    
    //生成企业组织机构代码
    public static String getORGANIZATION_CODE(){
        int [] in = { 3, 7, 9, 10, 5, 8, 4, 2 };
        String data = "";
        String yz = "";
        int a = 0;
        //随机生成英文字母和数字
        for (int i = 0; i < in.length; i++){
            String word = getCharAndNumr(1,0).toUpperCase();
            if (word.matches("[A-Z]")) {
                a += in[i] * getAsc(word);
            }else{
                a += in[i] * Integer.parseInt(word);
            }
            data += word;
        }
        //确定序列
        int c9 = 11 - a % 11;
        //判断c9大小，安装 X 0 或者C9
        if (c9 == 10) {
            yz = "X";
        } else if (c9 == 11) {
            yz = "0";
        } else {
            yz = c9 + "";
        }
        data += "-"+yz;
        return data.toUpperCase();
    }

    //生成税务登记号码
    public static String getTAX_REGISTRATION_CODE(){
        String data = "";
        String first = "73"+getCharAndNumr(4,2);
        String end = getORGANIZATION_CODE();
        data= first+end;
        data =data.toUpperCase().replaceAll("-","");
        if (!test5(data.toUpperCase())) getTAX_REGISTRATION_CODE();
        return data;
    }

    public static int getAsc(String st) {
        byte[] gc = st.getBytes();
        int ascNum = (int) gc[0] - 55;
        return ascNum;
    }

    public static boolean test5(String data){
        String regex = "[1-8][1-6]\\d{4}[a-zA-Z0-9]{9}$";
        if (!data.matches(regex)) {
            return false;
        }else
            return true;
    }

    public static String getCharAndNumr(int length,int status) {
        Random random = new Random();
        StringBuffer valSb = new StringBuffer();
        String charStr = "0123456789abcdefghijklmnopqrstuvwxy";
        if (status == 1) charStr = "0123456789";
        if (status == 2) charStr = "0123456789";
        if (status == 3) charStr = "0123456789ABCDEFGHJKLMNPQRTUWXY";
        int charLength = charStr.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            if (status==1&&index==0){ index =3;}
            valSb.append(charStr.charAt(index));
        }
        return valSb.toString();
    }

    public static List<Character> Hex =
            Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');

    // 返回指定的十六进制字符串
    public static String getHexString(int length) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        Random random = new Random();
        while (length-- != 0) {
            stringBuilder.append(Hex.get(random.nextInt(16)));
        }
        return stringBuilder.toString();
    }

    public static String getOctString(int length) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        Random random = new Random();
        while (length-- != 0) {
            stringBuilder.append(Hex.get(random.nextInt(10)));
        }
        return stringBuilder.toString();
    }

    static Map<String, String> COMPANYS_TAXESNO = new HashMap<>();
    final static String[] COMPANYS = {
            "证券简称",
            "荣丰控股",
            "三湘印象",
            "科华生物",
            "思源电气",
            "威尔泰",
            "中国海诚",
            "汉钟精机",
            "悦心健康",
            "延华智能",
            "海得控制",
            "二三四五",
            "宏达新材",
            "上海莱士",
            "美邦服饰",
            "神开股份",
            "普利特",
            "新朋股份",
            "柘中股份",
            "中远海科",
            "摩恩电气",
            "松芝股份",
            "嘉麟杰",
            "协鑫集成",
            "新时达",
            "徐家汇",
            "顺灏股份",
            "百润股份",
            "姚记科技",
            "金安国纪",
            "康达新材",
            "良信电器",
            "纳尔股份",
            "力盛赛车",
            "天海防务",
            "网宿科技",
            "上海凯宝",
            "东方财富",
            "旗天科技",
            "安诺其",
            "华平股份",
            "锐奇股份",
            "泰胜风能",
            "科泰电源",
            "万达信息",
            "汉得信息",
            "东富龙",
            "华峰超纤",
            "科大智能",
            "金力泰",
            "上海钢联",
            "永利股份",
            "上海新阳",
            "天玑科技",
            "卫宁健康",
            "巴安水务",
            "开能健康",
            "安科瑞",
            "凯利泰",
            "中颖电子",
            "华虹计通",
            "新文化",
            "鼎捷软件",
            "安硕信息",
            "飞凯材料",
            "普丽盛",
            "华铭智能",
            "信息发展",
            "沃施股份",
            "润欣科技",
            "海顺新材",
            "维宏股份",
            "雪榕生物",
            "古鳌科技",
            "会畅通讯",
            "移为通信",
            "汇纳科技",
            "富瀚微",
            "华测导航",
            "透景生命",
            "上海瀚讯",
            "矩子科技",
            "浦发银行",
            "上海机场",
            "上港集团",
            "宝钢股份",
            "上海电力",
            "中远海能",
            "国投资本",
            "中船科技",
            "上海梅林",
            "东风科技",
            "中视传媒",
            "大名城",
            "开创国际",
            "上汽集团",
            "东方航空",
            "ST长投",
            "中国船舶",
            "航天机电",
            "上海建工",
            "上海贝岭",
            "ST创兴",
            "复星医药",
            "紫江企业",
            "开开实业",
            "东方创业",
            "浦东建设",
            "上海家化",
            "振华重工",
            "现代制药",
            "鹏欣资源",
            "中化国际",
            "华丽家族",
            "上海能源",
            "置信电气",
            "交大昂立",
            "宏达矿业",
            "光明乳业",
            "方正科技",
            "云赛智联",
            "市北高新",
            "汇通能源",
            "绿地控股",
            "ST沪科",
            "ST毅达",
            "大众交通",
            "老凤祥",
            "神奇制药",
            "丰华股份",
            "金枫酒业",
            "氯碱化工",
            "海立股份",
            "天宸股份",
            "华鑫股份",
            "光大嘉宝",
            "华谊集团",
            "复旦复华",
            "申达股份",
            "新世界",
            "华建集团",
            "龙头股份",
            "ST富控",
            "大众公用",
            "三爱富",
            "东方明珠",
            "新黄浦",
            "浦东金桥",
            "号百控股",
            "万业企业",
            "申能股份",
            "爱建集团",
            "同达创业",
            "外高桥",
            "城投控股",
            "锦江投资",
            "飞乐音响",
            "ST游久",
            "申华控股",
            "ST中安",
            "豫园股份",
            "昂立教育",
            "强生控股",
            "陆家嘴",
            "中华企业",
            "交运股份",
            "上海凤凰",
            "上海石化",
            "上海三毛",
            "亚通股份",
            "绿庭投资",
            "ST岩石",
            "光明地产",
            "ST爱旭",
            "华域汽车",
            "上实发展",
            "锦江酒店",
            "ST运盛",
            "安信信托",
            "中路股份",
            "耀皮玻璃",
            "隧道股份",
            "上海物贸",
            "世茂股份",
            "益民集团",
            "新华传媒",
            "兰生股份",
            "百联股份",
            "第一医药",
            "申通地铁",
            "上海机电",
            "界龙实业",
            "海通证券",
            "上海九百",
            "上柴股份",
            "上工申贝",
            "宝信软件",
            "同济科技",
            "上海临港",
            "华东电脑",
            "海欣股份",
            "妙可蓝多",
            "张江高科",
            "东方证券",
            "春秋航空",
            "上海环境",
            "国泰君安",
            "上海银行",
            "环旭电子",
            "交通银行",
            "大智慧",
            "上海电影",
            "中国太保",
            "上海医药",
            "中国核建",
            "广电电气",
            "中银证券",
            "上海电气",
            "光大证券",
            "美凯龙",
            "中远海发",
            "招商轮船",
            "宝钢包装",
            "龙宇燃油",
            "联明股份",
            "北特科技",
            "创力集团",
            "爱普股份",
            "新通联",
            "全筑股份",
            "凯众股份",
            "泛微网络",
            "德邦股份",
            "博通集成",
            "剑桥科技",
            "润达医疗",
            "华培动力",
            "华贸物流",
            "上海沪工",
            "拉夏贝尔",
            "上海亚虹",
            "网达软件",
            "汇得科技",
            "日播时尚",
            "保隆科技",
            "上海洗霸",
            "爱婴室",
            "菲林格尔",
            "格尔软件",
            "移远通信",
            "宏和科技",
            "上海雅仕",
            "上海天洋",
            "水星家纺",
            "亚士创能",
            "风语筑",
            "恒为科技",
            "翔港科技",
            "韦尔股份",
            "欧普照明",
            "荣泰健康",
            "艾艾精工",
            "地素时尚",
            "中曼石油",
            "徕木股份",
            "畅联股份",
            "彤程新材",
            "璞泰来",
            "永冠新材",
            "晶华新材",
            "至纯科技",
            "密尔克卫",
            "海利生物",
            "鸣志电器",
            "龙韵股份",
            "岱美股份",
            "来伊份",
            "科博达",
            "雅运股份",
            "华荣股份",
            "飞科电器",
            "数据港",
            "吉祥航空",
            "元祖股份",
            "城地股份",
            "天永智能",
            "晨光文具",
            "金桥信息",
            "威派格",
            "克来机电",
            "康德莱",
            "至正股份",
            "澜起科技",
            "中微公司",
            "心脉医疗",
            "乐鑫科技",
            "安集科技",
            "申联生物",
            "晶晨股份",
            "普元信息",
            "聚辰股份",
            "优刻得",
            "柏楚电子",
            "美迪西",
            "昊海生科",
            "晶丰明源"
    };
}
