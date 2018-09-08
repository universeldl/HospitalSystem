package com.hospital.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import net.sf.json.JSONObject;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class AliOssConfig {
    static final String endpoint = "oss-cn-shanghai.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    static final String accessKeyId = "";
    static final String accessKeySecret = "";
    static final String bucketName = "scmc-wechat";

    static public String getPostPolicyString() {
        String dir = "";
        String host = "http://" + bucketName + "." + endpoint;
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            long expireTime = 300;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            //respMap.put("expire", formatISO8601Date(expiration));
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            JSONObject ja1 = JSONObject.fromObject(respMap);
            System.out.println(ja1.toString());

            return ja1.toString();
/*
            response(ServletActionContext.getRequest(), response, ja1.toString());
*/

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
/*
            Assert.fail(e.getMessage());
*/
        }
    }

	static public OSSClient getOssClient() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}

	static public String getBucketName() {
        return bucketName;
	}

}
