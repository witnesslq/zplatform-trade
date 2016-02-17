/* 
 * Test.java  
 * 
 * version v1.0
 *
 * 2015年10月9日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.zlebank.zplatform.commons.utils.Base64Utils;
import com.zlebank.zplatform.commons.utils.RSAUtils;
import com.zlebank.zplatform.trade.insteadPay.message.BaseMessage;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPayFile;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPayQuery_Request;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPay_Request;
import com.zlebank.zplatform.trade.insteadPay.message.MerWhiteList_Request;
import com.zlebank.zplatform.trade.insteadPay.message.RealnameAuthFile;
import com.zlebank.zplatform.trade.insteadPay.message.RealnameAuth_Request;


/**
 * 
 * 代付报文生成
 *
 * @author Luxiaoshuai
 * @version
 * @date 2015年11月26日 上午10:06:15
 * @since
 */
public class CreateInsteadPayMassage {
    
    private Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
    
//    /**私钥**/
//    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANf1wsgo1oBUdhPlihXA3zMwHd6S94rnlw/BH2PqmEYGMJyHFx03UF6RRVUfPiqipemtixrmiO2WcdCpa0OjsKOvJ1YZbK7SJdjxQxsaQ35vvoG3ztdKiuBrnzPnUVUbSRrv2eYzBbA0MkBt3DCoSUapy8xGw9RmqP7gLAa711/fAgMBAAECgYEAumN3SG/OqhW7j3KWGi5LtY86yGXJ1o4JF+TTZdYMtKsU+5OwhFvANs5kq5H6gqu8TJGmtIHx/jTRaIlAdbFqelR65elHJa/PeHu3s/8VHhfFHesPGjQ8w2C5q/QoGH+iTO0HIhagwHkSFbrIKoKx8a4vM6i0G1N8HOngggLJKakCQQD8ygXa8hghc8yhw0uEZOelTtYCX1uCpI7+er7CjiODm6NILXzvc0ld2HjX2ATjzMaUUdSlnYC7L//EJnlYXthbAkEA2rP7scWlklgLTzjrBcbsEsTExJs2Orokae3Eiu9fc027gr3c1abDbfUqJvkgWpBis3RWd1hECgNDBDobnU6NzQJAK3pz11ycWeSY5Zm5e2P/k6cjl/TewHGdRpfGB6B66z/xrZBlVKn3jHSXFEjLToMCBLCuam3Unl17GDml1VU60QJAEqruPvxZ56XwQTDgjruuBuQYz5dYB6c/2HLdJebMxPDLoptGME5tvAMDOBRnuJM+eYbiook8NYaVv9Sd6s17uQJBAJ6aLix3qhY0oinxWEaIV7kRWRUT6fDWGouN4lyl5+NH9Hr11ZfqJrwSf+xsCtA5QCqqRXbseRmsJbvXT9kfrAE=";
//    /**公钥**/
//    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDX9cLIKNaAVHYT5YoVwN8zMB3ekveK55cPwR9j6phGBjCchxcdN1BekUVVHz4qoqXprYsa5ojtlnHQqWtDo7CjrydWGWyu0iXY8UMbGkN+b76Bt87XSorga58z51FVG0ka79nmMwWwNDJAbdwwqElGqcvMRsPUZqj+4CwGu9df3wIDAQAB";;
//    /**平台公钥**/
//    public static final String LOCAL_PUB = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEAZ+Uakvf6Fr2jCanHl/0q1cE9YWWj8SMe4JYKNjAZ5SS20sVZ7MRly3c7KwnkHZ9M2z3882eV/6bHulKM4W+by77W/voFsH+b6RTJ49QQrLU2joHlrPU4CHH6BZTmAxLr1RNCIbHv7WRE0Gch1MEnf9maYDl2yXd6xiyjmCbSQIDAQAB";

     /**私钥**/
    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI1xcwY4R0j/2uwZH1sKuRaZmPBy4TUdBei2enKEj7Ipikp9La0sc9xtlmqgBXGglbo1JbEpw6izu1AsRcAFUYojWAJ8H3TZb7a/23EfffA+Rr4uqzmsEYts88rsO977oP17xYvGTACj/eeFxAzWjVjG40spsMjmAGuHKPdujKgHAgMBAAECgYAMKnbvUQXopIErD5of1ZNH+1wpSmhOjjmE47Uyhm5pr6MCdzPnkkxA6Hgooaz2Mg7O3mVokKQQrazt33D8bDiwHEqr4vTn0fNoUEdzCJYgQSzG8dpbDh/NQBezA4+DumJDgJh7NcxLSpWknLJ/sh+/xWy1/cmvbZOXUOS4AIRnQQJBAPQeR7ycwR7QRrGznH6OvucWeRp3wyHOWNT/1o9JTk9LhlWTEWN4sUPPwS2FnnxOYQ6+txxUWUovGnLSAGMjnGECQQCUU9Wmc4s53PZEwRbi6foxrMpLrv5xbWp1Np4nsrMaDhRVkKFt+XQ2T7O0KEBGMrXn/NJtvqg6ctwpRiqf2d1nAkEAwpPEIC1drP7b6lY8fY+v0CfUfWlKKo77tUL6tRj8By4us9TWfk+8E03sLyma6n/a0tLmxvjgdsXWsgB6+Ipw4QJAHhn1XdZNBu0kvUdCUcKSF8qtMzbMRFCkLi1rYzsMxdjdn4++cwcQ4oTCP2l7wcNAz1erjMIIefQdYfLva4lXOQJBAJWWmhsiUzkOX/tsshSq+TIA5SYhJU6sc9w0Mv5OH+oDLsTz61wNh6qaWNOOYFQXEBOsIGj5SHO4vXKzUXCh+ig=";
    /**公钥**/
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNcXMGOEdI/9rsGR9bCrkWmZjwcuE1HQXotnpyhI+yKYpKfS2tLHPcbZZqoAVxoJW6NSWxKcOos7tQLEXABVGKI1gCfB902W+2v9txH33wPka+Lqs5rBGLbPPK7Dve+6D9e8WLxkwAo/3nhcQM1o1YxuNLKbDI5gBrhyj3boyoBwIDAQAB";
    /**平台公钥**/
    public static final String LOCAL_PUB = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChnpVKt4IQAiJr6L8eGNEmYRvhQQCFC2rfIBTP5QYu6tNSKrTj42ghyxCLCczb5hZxDKh/Yu56XMMy/8LSszdOY9TfdYhONYi10nIJuysjkWtkPpG/h8ffcXR0dool18BJrmA0zpv5b7WhCC4+Nvqoc8Dhku3sgrGwWiEwp4qB9QIDAQAB";

    
    /**商户号**/
    public static final String MER_ID = "200000000000590";

    /**
     * 总指挥
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        CreateInsteadPayMassage run = new CreateInsteadPayMassage();
        // 生成代付报文
        run.createInsteadPay();
        // 生成代付查询结果
//        run.createInsteadPayQuery();
        // 生成实名认证 
//        run.createRealNameAuth();
        // 生成实名认证查询
//        run.createRealNameAuthQuery();
        // 生成白名单添加
//        run.createMerchWhiteList();
        // 加密
//        run.compressionByDeflated();
        
//        run.test();
        
//        run.privateDecyptData();
    }
    
    private void privateDecyptData() throws Exception {
//        String encryptedData = "QAdIWj2gJ/HRMPup4RfZreSIImsqYvCY35U4pg7MA3OBWo9hMYR7NmDk6ERXaNMVgIQxSqmCW4rn1OeYuBqaRVTANOoxaCRpD0VkaCrUJNE9jUTnIrPXidfWeLp3Z6tXYSRcQYTYVj95yVyxYV/TVLDJH4LSr7mIstYP4TAjuZUGzLgfKz0//byrLYOlioThD04CRfNAntTq5JzGyluybxvJyodK9gPC+me9IeWH6wkHQ0aw4F8UBUyYnH59uOPaUasS0bABbaGJYd5VnF/VLI1A2o5prq6xduvqiBkIb/Y3S4nf/pKBTcxD85+Fb3jm/c3vkA9TDehEfA7j1NaPclOuBsRGjkdPfzA1r5k59rTiq9tjWo83hFsv37rNykjFa6wXuOSTCh+x4dOGlwcpPzqrqKRd8Qdb0vawW1SOw5keb1IWDuZ48lbyArRwTzishXLTtTn8jiREmU242j2oYmX+NSp96SVVnTrNltRlBDd4cmrLUL0BWrjIlLxGSgpBdMLVAcYdlGm3sHq7go40RboGipa90eV2+j5DH+PI7nmOOHTEV8LsoNtKXclREWtcqUcIlz0EtY2fzhpnKMQfU8FZu9DdAaG/sKZdgamZcnQyhyQDNxb4a9KueAUyN9p1VkEy+CdJJqPe8u21l+hIMskJbfyjPNekFwW2Dxe0Pg8xOS0fsdFx3czVEp1So9kp7xrIYh8te7dpzauzY3FGf6Nta4T8fOlSmnRGpPXG0B+giMULNzAWVBfpKHkmhgLwmlO2GbKWE6Vy4OvOwnAFh+hcwbJkAaE3eZ9Ay11KOcsRqP9n8eLNMPNWRJr3EUNWIlbu9dH4cGRjIqHuja3bFw==";
        String encryptedData = "vNNDpuaSkwOYmZc4a5rmURON8KD2/qbAcy7bJ5e6bWhO0nSkXIFRKARkXn5IJ1wSxjcZgucwu5Khgkfq3H1M5xI3lajC64twb8K5Lh9qt4vxvuQ+Y24bQMYYU0jDbPUQkZKck2sPp0JVhREd0fQMKd+m+tx37XPoP5PuxaLd7/A=";
       String privateKey="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMQBn5RqS9/oWvaMJqceX/SrVwT1hZaPxIx7glgo2MBnlJLbSxVnsxGXLdzsrCeQdn0zbPfzzZ5X/pse6Uozhb5vLvtb++gWwf5vpFMnj1BCstTaOgeWs9TgIcfoFlOYDEuvVE0Ihse/tZETQZyHUwSd/2ZpgOXbJd3rGLKOYJtJAgMBAAECgYAa08YNwEbGTL02hZQ8x3GGwXTVnu074E2clMMQYfbYztSNDSrHsTvXKoQCevpNJ/6vwDwn69y20YBtpttzZn3lh2uXN332khtyXw26hYK89ZNM1A+3TjTBO3o2V+SFP6JWoCdIBgEffd+0m9Q9QjGZpC7vfElk0ynoGeCek12ByQJBAOJnzMNTCq/DM023RY8u42VAtAmTqPy025ZgyYQH1+y5lYPtS+6eBqCCj1iO+6vxREnggnKw3mmQI/DHPQCZ6T8CQQDdoJQNWlJ4O5GyVVlNy5tC7NY7FkNDSoFqTgqaUG8bftONH+Dtz7YFIxFh28BaL+0HGruwPNjohFab8h/DqBF3AkBTeHVzpdAQ0OS/SExq55yKnfpCdaAd2wgCPxjMhp/C9QD8rKYMCVLkJn525fA/iinlGtK9OhwlyzCU+r+w0w29AkAnLxPeBIEeNtH8qLBgW8Y4n+0Eh1wfEpYs+F1RiTULtHGsut0mzpmM8PVKyvKIP371yZkzgJ6NBZDWg4lwK0/jAkAWIZktGvHqQbWu1XlCVZr3ool6JVJoLIlxTbMmHGMQd/9HGfjIBjd62K26pfm3vJ2gdA0cvAo0HnUmEaw6IF80";
//       privateKey = 
       byte[] strs = RSAUtils.decryptByPrivateKey(Base64Utils.decode(encryptedData), privateKey);
       String words = new String(strs);
       System.out.println("解密后的明文是："+words);
       
    }
    
    /**
     * 生成白名单添加
     * @throws Exception 
     */
    private void createMerchWhiteList() throws Exception {
        MerWhiteList_Request request = new MerWhiteList_Request();
        commSetter(request, "74");
        request.setAccNo("6226091212413805");
        request.setAccName("鲁晓帅");

        // 加签生成
        JSONObject jsonData = JSONObject.fromObject(request);
        String beforeSign = jsonData.toString();
        String signedData = RSAUtils.sign(beforeSign.getBytes("UTF-8"), PRIVATE_KEY);
        jsonData.put("signature", signedData);
        System.out.println("完整json："+jsonData.toString());
        // 复制到ClipBoard
        sysc.setContents(new StringSelection(jsonData.toString()), null);
    }

    /**
     * 
     */
    private void test() {
        String a = null;
        String b="";
        String c = a+b;
        StringBuilder errorMsgDetail = new StringBuilder();
        c=errorMsgDetail.toString();
        System.out.println( c );
        
    }

    /**
     * 生成实名认证
     * @throws Exception 
     */
    private void createRealNameAuth() throws Exception {
        RealnameAuth_Request request = new RealnameAuth_Request();
        commSetter(request, "72");
        request.setChannelType("00");
        request.setAccessType("0");
        request.setOrderId("2000000000030");
        request.setTxnTime("20151126121212");
        // 加密信息
        RealnameAuthFile file = new RealnameAuthFile();
        file.setCardNo("6226091212413805");
        file.setCardType("1");
        file.setCertifId("13112219880904243X");
        file.setCertifTp("01");
        file.setCustomerNm("鲁晓帅");
        file.setCvn2("");
        file.setExpired("");
        file.setPhoneNo("13426342943");
        JSONObject encyptData = JSONObject.fromObject(file);
        byte[] strs = RSAUtils.encryptByPublicKey(encyptData.toString().getBytes(), LOCAL_PUB);

        // 加签生成
        JSONObject jsonData = JSONObject.fromObject(request);
        jsonData.put("encryptData", Base64Utils.encode(strs));
        String beforeSign = jsonData.toString();
        String signedData = RSAUtils.sign(beforeSign.getBytes(), PRIVATE_KEY);
        jsonData.put("signature", signedData);
        System.out.println("完整json："+jsonData.toString());
        // 复制到ClipBoard
        sysc.setContents(new StringSelection(jsonData.toString()), null);
    }

    /**
     * 生成实名认证查询
     * @throws Exception 
     */
    private void createRealNameAuthQuery() throws Exception {
        RealnameAuth_Request request = new RealnameAuth_Request();
        commSetter(request, "73");
        request.setChannelType("00");
        request.setAccessType("0");
        request.setOrderId("1000000000023");
        request.setTxnTime("20151126121212");

        // 加签生成
        JSONObject jsonData = JSONObject.fromObject(request);
        String beforeSign = jsonData.toString();
        String signedData = RSAUtils.sign(beforeSign.getBytes(), PRIVATE_KEY);
        jsonData.put("signature", signedData);
        System.out.println("完整json："+jsonData.toString());
        // 复制到ClipBoard
        sysc.setContents(new StringSelection(jsonData.toString()), null);
    }

    private void compressionByDeflated() throws Exception {
        try {
            // Encode a String into bytes
            String inputString = "我是一个大好人999999999999999999999999999999999999999999999999999999999999999999999";
            System.out.println("input size:"+inputString.getBytes().length);
            byte[] input = inputString.getBytes("UTF-8");

            // Compress the bytes
            byte[] output = new byte[10000];
            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            int compressedDataLength = compresser.deflate(output);
            byte[] convert = new byte[compressedDataLength];
            
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(output));
            inputStream.read(convert);

            String data = Base64Utils.encode(convert);
            System.out.println("convert size:"+ convert.length);
            System.out.println("out put size:"+ data);

            // Decompress the bytes
            Inflater decompresser = new Inflater();
            decompresser.setInput(output, 0, compressedDataLength);
            byte[] result = new byte[1000];
            int resultLength = decompresser.inflate(result);
            decompresser.end();

            // Decode the bytes into a String
            String outputString = new String(result, 0, resultLength, "UTF-8");
            System.out.println(outputString);
            } catch(java.io.UnsupportedEncodingException ex) {
                ex.printStackTrace();
            } catch (java.util.zip.DataFormatException ex) {
                ex.printStackTrace();
            }
    }

    /**
     * 生成代付查询结果
     * @throws Exception 
     */
    private void createInsteadPayQuery() throws Exception {
        InsteadPayQuery_Request request = new InsteadPayQuery_Request();
        commSetter(request, "22");
        request.setChannelType("00");
        request.setAccessType("0");
        request.setBatchNo("1025");
        request.setTxnTime("20151126121212");
        
        JSONObject jsonData = JSONObject.fromObject(request);
        String beforeSign = jsonData.toString();
        String signedData = RSAUtils.sign(beforeSign.getBytes(), PRIVATE_KEY);
        System.out.println("加签前："+beforeSign);
        jsonData.put("signature", signedData);
        System.out.println("加签结果："+signedData);
        System.out.println("完整json："+jsonData.toString());
        // 复制到ClipBoard
        sysc.setContents(new StringSelection(jsonData.toString()), null);
    }

    /**
     *  生成代付报文
     * @throws Exception 
     */
    private void createInsteadPay() throws Exception {
        InsteadPay_Request request = new InsteadPay_Request();
        commSetter(request, "21");
        request.setChannelType("00");
        request.setAccessType("0");
        request.setBatchNo("1025");
        request.setTxnTime("20151126121212");
        request.setTotalQty("2");
        request.setTotalAmt("200");
        // 设定明细文件
        InsteadPayFile file1 = createPayFile("20");
        InsteadPayFile file2 = createPayFile("180");
        List<InsteadPayFile> files = new ArrayList<InsteadPayFile>();
        files.add(file1);
        files.add(file2);
        //////////////////////////////////////////////////////
        // 转为JSON数组
        JSONArray jsonContent = JSONArray.fromObject(files);
//        System.out.println(jsonContent.toString());
        // 压缩前字节
        byte[] input =jsonContent.toString().getBytes("UTF-8");
        // 压缩后字节初始化
        byte[] output = new byte[10000];
        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        // 压缩并返回压缩后的字节数组长度
        int compressedDataLength = compresser.deflate(output);
        // 压缩后实际字节数组初始化
        byte[] convert = new byte[compressedDataLength];
        // 字节数组截取
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(output));
        inputStream.read(convert);
        // 字节数组->BASE64编码
        String fileContent = Base64Utils.encode(convert);
//        System.out.println("DEFLATE："+ fileContent);
        //////////////////////////////////////////////////////

//        request.setFileContent(fileContent);

        JSONObject jsonData = JSONObject.fromObject(request);
        jsonData.put("fileContent", fileContent);
        String beforeSign = jsonData.toString();
        String signedData = RSAUtils.sign(beforeSign.getBytes(), PRIVATE_KEY);
//        System.out.println("加签前："+beforeSign);
        jsonData.put("signature", signedData);
//        System.out.println("加签结果："+signedData);
        System.out.println("完整json："+jsonData.toString());
        // 复制到ClipBoard
        sysc.setContents(new StringSelection(jsonData.toString()), null);
    }

    /**
     * 代付明细
     * @param amt 
     * @return
     */
    private InsteadPayFile createPayFile( String amt) {
        InsteadPayFile file1 = new InsteadPayFile();
        file1.setMerId(MER_ID);
        Random random = new Random();
        file1.setOrderId("IP"+System.currentTimeMillis()+random.nextInt(100));
        file1.setCurrencyCode("156");
        file1.setAmt(amt);
        file1.setBizType("000001");
        file1.setAccType("01");
        file1.setAccNo("6226091212413805");
        file1.setAccName("鲁晓帅");
        file1.setBankCode("123456789012");
        file1.setIssInsProvince("");
        file1.setIssInsCity("");
        file1.setIssInsName("");
        file1.setCertifTp("01");
        file1.setCertifId("131122198809042433");
        file1.setPhoneNo("13426342943");
        file1.setBillType("01");
        file1.setNotes("这是一个测试");
        return file1;
    }

    /**
     * 私钥解密
     * @throws Exception 
     */
    private void privateOpen() throws Exception {
        
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOYQmI3QralUVuXxGJmLyvTAtsot2/TWLCIMUa8eG5AvbXBwA8Y+t+oMChL8uZH2/JHg120wTm1jJaEqlh6HK5K0lvGdQYdxYkd1g42cwEzgMr+TENrCONleLHe1AGj3sFOnAexM0m3lUw3KfcIzsjXX2wJB4Yw5zNzLGtehD2flAgMBAAECgYB9fBvZbjLqowfqz4Adb/Q5X0vUJwNMHe1gfNuo6oEaOeQ1acOFQ/xIelycUqBdDxxf7QVTlv+QBn4l0/ahnBSVHYfnQAJuuu8UsOL1Hyw/u2brc+lUOCVRDSMEHHHttHQ4Ke+j5+Yrwl5oB4H7wTfWEOYqin/9nBnSmg1nUb3KAQJBAPa9m1YAKX3kIu1AaDUhhvW2TalWxUHXBbpQ5EkXqTMt+ZBuaXSq36ugP8t1cf5TTaj7ehOqvi4+OeqSImq8o0ECQQDussmYf8+rD68q5C7FcTRLLYvVCdlynuvX2cp0lrugHUD2y1R/XSuIu66oUbm0hXwUfO87VJUJr1TNQT07cW+lAkEA81790owCYPwfPyiiIJXbSozweTDvDAwMyEN1iGrgJ20XldByD/Ni/yPnCMXlFFgSZ7T0KmXlhDM4aRiO/fzmgQJAWgYqDA7gNqbirK2EikiorVXPMBjX3ufbEPx947zZpIiD8NA83RA5lAcc1zSDcncJlfyMiXbcAtwSgpsBSgL9VQJBAOesp37Cybow7cN2KIlkTB3MtbqUXlajOhMTZzfj6PYOe+bL/oNugUbILNlwm7x1J/TGQHGPn2W7qlaTpcF45IE=";
        String encryptedData = "TiVZeHBjqo02VzcqjRzCji5eog5KY11R8J9aRIF/WXZvSRZhe5/PRDao3/JbTJbYht3QicSjsnXltT4puap40DfDxHI3JCTIYwKBD+3Pv2xuxP7PkFL1EY+w72sDYII6vMOVRjPl2vY4BnZPDY6qK4l+MiwGQc4/Z+65TyedZiQIrP+DzUP0e3YAntjHU+GsgAyWRyV5Cf5Bw5VH3aRtnnxUtsyqI3XlZ8H/eQMbS6nsTemnBUNJuGXtf36rwL5obNKHVN2w5qiDc89ZxNWu42co83SLMAsJRZQQJgocoltJUrAeOnLkZkY+RAdkWnfmkO9FqsgO0gOQTt2NVoOkC2r/mV6p/LmNPGdTkVJHMbO+6RgqVcpU59kfZ8YuJauneLu6imII4ZsH2K41KnHQDcTQMK0e+2B92nvuFJEA7+7MVAmh9+5B4gIMMuDJq+/UymY/ncEnTDeDAipqUsy/PTenC9sbDjSvD6nAGw4CGuA2FO/5feDHxJKvnNglDLc=";
        
        byte[] strs = RSAUtils.decryptByPrivateKey(Base64Utils.decode(encryptedData), privateKey);
        String words = new String(strs);
        System.out.println("解密后的明文是："+words);
    }



    /**
     * 私钥加签
     * @throws Exception 
     */
    private void r20151021() throws Exception {
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKLLsnVlaexD2Xt/7N2EJ6yUNj6Jrv7SZhyrfBoUzX+0LJ62u1Zrok71lBzyY9OscqO+te1anVuKB+1blv4eYuKdIM0vd6AkIYz+AQo+ij4P1uOKIll8wf1Rlshkx5PYAMfM0d6zaqRiLG4pFGn5hLtC/srK40pieX2QLR6Ecgu1AgMBAAECgYBeSKL+otGJ6HHYuOoyEQv5Q413Ar67DpfdGuQxJsMtpugAs8wj7Qp/uHMjoK54NXPfqeArq0NfdL3KljUI0AhsTuU3J83ZVMXKiekXgDzhkZyW9mOwXKMUF0rKlM/yPewQ+tN54Nunew30qMWO5iGg+6gi8uSFNIsWO5Yl/zPisQJBAPDmn59F/JJEb7efD9Hd2IGoaD0pC3fxn33LYXNABs+6kG4e9AjzPPPe84z/mAy7BDaV3Enk9mI0eUhvJPKoaqsCQQCs/9YeZDBvM79Peh1TazsuosFsymyjJ7fklTCA2YuXAKVXmbbBvRZlVgh4x0I9Lj5Il3PB/Q85tIAl4aFCZ2MfAkEAoDAK9oM4Vx7Q1t4bS+dj/5u3bOvtJ13xO1nRPCzYGqupPMCyb30nC9c2RozzU4vMWmu+ZxisSVMxTtwxSnZ5LwJBAKvWf4+2gumiI/BE4qY+iA2durVeKCSzyhRyIDiMXfxCtvlZhM/SC1Hi8A1QFzqAGXuvfFr/C0WNRhDamnxEB48CQGwPxQ91JJkzPXPxlbiIINYNR8R+wJWP3o9MN+aBhMGifjDQUHSDB19ScVAyK0IG0j3YKzUL3v5yAgnfnFb56rM=";
        String strs = RSAUtils.sign("luxiaoshuai".getBytes(), privateKey);
        System.out.println("加签后是："+strs);
    }

    
    /**
     * 共通属性设定
     * @param request
     * @param type
     */
    private void commSetter(BaseMessage request, String type) {
        request.setVersion("v1.0");
        request.setEncoding("01");
        request.setSignature("");
        request.setSignMethod("01");
        request.setTxnType(type);
        request.setTxnSubType("00");
        request.setBizType("000202");
        request.setMerId(MER_ID);
    }
}
