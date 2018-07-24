package com.miyuan.rpc.cases;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.miyuan.rpc.response.PeerCountResp;
import com.miyuan.rpc.utils.HttpUtils;
import com.miyuan.rpc.utils.PARAMETER;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class PeerCount {
	
	
  @Test(dataProvider = "peerCountData")
  public void peerCount(String jsonrpc, String method,String id) {
	  List <NameValuePair> param = new ArrayList<NameValuePair>();
	  JSONObject bodyjson = new JSONObject();
	  bodyjson.put("jsonrpc", jsonrpc);
	  bodyjson.put("method", method);
	  bodyjson.put("params", param);
	  bodyjson.put("id", id);
	  String respString = HttpUtils.sendJson(PARAMETER.url, bodyjson.toString());
	  PeerCountResp resp = JSONObject.parseObject(respString, PeerCountResp.class);
	  String resultId = resp.getId();
	  String resultJsonrpc = resp.getJsonrpc();
	  Assert.assertEquals(resultId, id);
	  Assert.assertEquals(resultJsonrpc, jsonrpc);
  }
  
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
  }


  @DataProvider
  public Object[][] peerCountData() {
    return new Object[][] {
      { "2.0", "peerCount","74" },
      //{ 2, "b" },
    };
  }
  
  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
  }

}
