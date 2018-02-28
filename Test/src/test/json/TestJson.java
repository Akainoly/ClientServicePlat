package test.json;

import java.util.Map;

import com.alibaba.fastjson.JSON;

public class TestJson {
	
	public static void main(String[] args) {
		String json="{\"SYS_HEAD\":{\"ConsumerId\":\"030107\",\"TransServiceCode\":\"test002\",\"RequestDate\":\"20170427\",\"RequestTime\":\"111121\",\"ConsumerSeqNo\":\"GSUTE201704270000000000003045772\",\"ConsumerIP\":\"132.2.58.214\",\"ServerIP\":\"192.9.200.131\",\"TranMode\":\"1\",\"MacValue\":\"70:1C:E7:28:C9:40\",\"Reserve\":\"\"},\"REQ_BODY\":{	\"idType\":\"B01\",\"idNo\":\"320583198611011016\",				\"cellphoneNo\":\"\"}}";
		Map<?,?> request=JSON.parseObject(json, Map.class);
		System.out.println(request.get("SYS_HEAD").getClass().getName());
	}

}
