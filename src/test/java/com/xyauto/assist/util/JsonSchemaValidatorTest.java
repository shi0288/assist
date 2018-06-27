//package com.xyauto.assist.util;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.mcp.validate.validator.IdCard;
//import com.xyauto.assist.entity.Execution;
//import org.junit.Test;
//import tk.mybatis.mapper.util.SimpleTypeUtil;
//
//import java.lang.annotation.Annotation;
//
///**
// * Created by shiqm on 2018-01-18.
// */
////@RunWith(SpringJUnit4ClassRunner.class)
////@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class JsonSchemaValidatorTest {
//
//
////    @Autowired
//    private IdCard idCard;
//
//
//    @Test
//    public void validatorSchema() throws Exception {
////        Set< EntityColumn > columnList = EntityHelper.getColumns(Execution.class);
////        System.out.println(columnList);
//
////        System.out.println( Annotation.class.isAssignableFrom(Short.class));
//
//
//        System.out.println(SimpleTypeUtil.isSimpleType(Short.class));
//
////        System.out.println(idCard);
////
////        JSONArray jsonObject= JSON.parseArray("[{ \"title\": \"有奖活动\", \"subject\": \"有奖活动\", \"cover\": \"\", \"img\": \"http://img1.qcdqcdn.com/group1/M00/10/12/o4YBAFpXOEyAWsoeAAEOHHopYOk122.png\", \"newimg\": \"http://img1.qcdqcdn.com/group1/M00/0A/C0/o4YBAFoTw22AJYuIAACpUviLQl4453.png\", \"background\": \"http://img1.qcdqcdn.com/group1/M00/0C/E0/o4YBAFo7hjmAF1xyAAn4pvqsRko951.png\", \"url\": \"http://www.sina.com.cn\" }, { \"title\": \"买车那些事\", \"subject\": \"买车那些事\", \"cover\": \"\", \"img\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOHKAUfgkAAC_lYAASLw585.png\", \"newimg\": \"http://img1.qcdqcdn.com/group1/M00/06/9D/oYYBAFoTw3CAIxCkAACZJYtMPkc086.png\", \"background\": \"http://img1.qcdqcdn.com/group1/M00/08/BD/oYYBAFo7hkOABk1IAAbj7I0ko_s139.png\", \"url\": \"http://www.baidu.com\" }, { \"title\": \"抢报价单\", \"subject\": \"抢报价单\", \"cover\": \"\", \"img\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOHiAVf-vAADyBNYBAZ4339.png\", \"newimg\": \"http://img1.qcdqcdn.com/group1/M00/0F/CA/o4YBAFpUO_yANMjpAACSUpi_2sU352.png\", \"background\": \"http://img1.qcdqcdn.com/group1/M00/0F/CA/o4YBAFpUPASAVdZ_AAhKrfNWcvc422.png\", \"url\": \"http://www.baidu.com\" }, { \"title\": \"帮我选车\", \"subject\": \"帮我选车\", \"cover\": \"\", \"img\": \"http://img1.qcdqcdn.com/group1/M00/10/12/o4YBAFpXOH-AKmWEAAChFku2WGM159.png\", \"newimg\": \"http://img1.qcdqcdn.com/group1/M00/08/BF/oYYBAFo7jlqAZYd0AABzlRvCyyc645.png\", \"background\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOXaALFWbAAQsUpbxiFI685.png\", \"url\": \"http://www.baidu.com\" }, { \"title\": \"新车快报\", \"subject\": \"新车快报\", \"cover\": \"\", \"img\": \"http://img1.qcdqcdn.com/group1/M00/10/12/o4YBAFpXOJ6AHQJdAADhqgEF7cw301.png\", \"newimg\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXObWAcZ7MAACIPh8MNZo855.png\", \"background\": \"http://img1.qcdqcdn.com/group1/M00/10/12/o4YBAFpXOI6ADARIAAcrwhPv2oM720.png\", \"url\": \"123214\" }, { \"title\": \"用车一点通\", \"subject\": \"用车一点通\", \"cover\": \"\", \"img\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOeqAZVR5AAEG9ahYQVE584.png\", \"newimg\": \"http://img1.qcdqcdn.com/group1/M00/10/12/o4YBAFpXObuAMjMaAAB91TYOVR4676.png\", \"background\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOVOAfGLwAAaQxE-UE7Y760.png\", \"url\": \"http://www.baidu.com\" }, { \"title\": \"帮我砍价\", \"subject\": \"帮我砍价\", \"cover\": \"\", \"img\": \"http://img1.qcdqcdn.com/group1/M00/10/12/o4YBAFpXOfWASKkvAADmFSRHFfw155.png\", \"newimg\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOduAeZQrAACKaL4BlYw363.png\", \"background\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOZyAExdFAAfOouhX8n0834.png\", \"url\": \"http://www.baidu.com\" }, { \"title\": \"高能问答\", \"subject\": \"高能问答\", \"cover\": \"\", \"img\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOfqABYrGAAEhPLe8h_8263.png\", \"newimg\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOlGAey5kAACJXbaACr0722.png\", \"background\": \"http://img1.qcdqcdn.com/group1/M00/0B/F0/oYYBAFpXOX2AVE-hAAq9WvgTd7g973.png\", \"url\": \"http://www.baidu.com\" }, { \"title\": \"汽车新闻早知道\", \"subject\": \"汽车新闻早知道\", \"cover\": \"\", \"img\": \"http://img1.qcdqcdn.com/group1/M00/10/12/o4YBAFpXOf6AWdapAADu005GmbA140.png\", \"newimg\": \"http://img1.qcdqcdn.com/group1/M00/10/12/o4YBAFpXOdCAQjqBAACHKTOUx38063.png\", \"background\": \"http://img1.qcdqcdn.com/group1/M00/10/12/o4YBAFpXOZSAQHgmAAh4ZeATI5Y852.png\", \"url\": \"http://www.baidu.com\" }]");
////
////        System.out.println(jsonObject.toString());
////        short temp=1;
////        Short one=new Short(temp);
////        Short tow=new Short(temp);
////
////        System.out.println(one.shortValue()==tow.shortValue());
//
//
//
//
//    }
//
//}