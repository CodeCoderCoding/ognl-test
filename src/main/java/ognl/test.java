package ognl;

import com.alibaba.fastjson.JSONObject;

public class test {

    public static void main(String[] args) {
        String jsonStr = "{\"body\":{\"accountInfo\":{\"loanBankCode\":\"0308\",\"repayBankProvince\":\"\",\"loanBankProvince\":\"\",\"loanPrivatePublicAccount\":\"01\",\"loanTrusteeType\":\"B134003\"},\"contactInfo\":[{\"relativeName\":\"王一\",\"relativeCompanyAddress\":\"来广营\",\"relativeCode\":\"F1004\",\"relativeMobile\":\"18612994747\"},{\"relativeName\":\"王二\",\"relativeCode\":\"F1004\",\"relativeMobile\":\"18612994848\"}],\"professionInfo\":{\"incomeLevelCode\":\"B20202\",\"professionCode\":\"F12326\",\"incomeSourceCode\":\"B20305\",\"duty\":\"B2910\",\"yearIncome\":48000,\"industryCode\":\"B1036\"}}}";
        JSONObject source = JSONObject.parseObject(jsonStr);
        try {
            //将json数据放到context中，并获取特定的值
            OgnlContext context = new OgnlContext();
            OgnlUtil ognlUtil = OgnlUtil.build().context(context).add("body", source);
            Object value = ognlUtil.getValue("#body.body.accountInfo.loanBankCode");
            System.out.println("#body.body.accountInfo.loanBankCode:"+value);
            // 获取JsonArray中值
            Object value1 = ognlUtil.getValue("#body.body.contactInfo[0].relativeName");
            System.out.println("#body.body.contactInfo[0].relativeName:"+value1);
            //获取的value值为空字符串，则正常打印空字符串
            Object value3 = ognlUtil.getValue("#body.body.accountInfo.repayBankProvince");
            System.out.println(value3);
            //获取的json key不存在，则打印null
            Object value4 = ognlUtil.getValue("#body.body.accountInfo.repayBankProvince1");
            System.out.println(value4);
            //获取json对象
            String expression = "body.contactInfo";
            Object value2 = Ognl.getValue(expression, new OgnlContext(), source);
            System.out.println("body.contactInfo:"+value2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
