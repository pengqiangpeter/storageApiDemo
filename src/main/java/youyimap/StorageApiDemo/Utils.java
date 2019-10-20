package youyimap.StorageApiDemo;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class Utils {
	
	public static JSONObject getChain(String chainId) {
		System.out.println("--------从优衣地图读取id为"+chainId+"的连锁商--------");
		String str = MyHttpClient.get("http://api.youyimap.com/api/storages/"+App.storageId+"/getChain?id="+App.chainData.getString("id")+"&access_token="+App.access_token);
		System.out.println(str);
		return JSONObject.parseObject(str);
	}
	
	public static void createChain(JSONObject chain) throws Exception {
		System.out.println("--------指示优衣地图创建id为"+chain.getString("id")+"连锁商--------");
		JSONObject newChain = (JSONObject)chain.clone();
		newChain.remove("shops");
		String rtn = MyHttpClient.postJson("http://api.youyimap.com/api/storages/"+App.storageId+"/createChain?access_token="+App.access_token, newChain.toJSONString());
		if(rtn.indexOf("SUCCESS")<0) {
//			System.out.println(rtn);
			throw new Exception();
		}
	}
	
	public static void createShops(String chainId, JSONArray shops) throws Exception {
		if(shops==null||shops.size()==0) return;
		System.out.println("--------指示优衣地图在连锁商"+chainId+"下创建店铺,数量为"+shops.size()+"--------");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("chainId", chainId);
		JSONArray newShops = (JSONArray)shops.clone();
		for(int i=0;i<newShops.size();i++) {
			newShops.set(i, (JSONObject)shops.getJSONObject(i).clone());
			newShops.getJSONObject(i).remove("commodities");
		}
		jsonObj.put("shops", newShops);
		String rtn = MyHttpClient.postJson("http://api.youyimap.com/api/storages/"+App.storageId+"/createShops?access_token="+App.access_token, jsonObj.toString());
//		System.out.println(rtn);
		if(rtn.indexOf("SUCCESS")<0) {
//			System.out.println(rtn);
			throw new Exception();
		}
	}

	public static void createCommodities(String shopId, JSONArray commodities) throws Exception {
		if(commodities==null||commodities.size()==0) return;
		System.out.println("--------指示优衣地图创建店铺"+shopId+"下的商品，数量为"+commodities.size()+"--------");
		JSONObject param = new JSONObject();
		param.put("shopId", shopId);
		param.put("commodities", commodities);
		String rtn = MyHttpClient.postJson("http://api.youyimap.com/api/storages/"+App.storageId+"/createCommodities?access_token="+App.access_token, param.toJSONString());
		if(rtn.indexOf("SUCCESS")<0) {
//			System.out.println(rtn);
			throw new Exception();
		}
	}
	public static JSONArray getShopIdsOfChain(String chainId) throws Exception {
		System.out.println("--------从优衣地图读取连锁商"+chainId+"下的所有店铺的ID--------");
		String str = MyHttpClient.get("http://api.youyimap.com/api/storages/"+App.storageId+"/getshops?chainId="+chainId+"&onlyId=true&access_token="+App.access_token);
		if(str.indexOf("[")<0) {
//			System.out.println(rtn);
			throw new Exception();
		}
		System.out.println(str);
		
		return JSONArray.parseArray(str);
	}
	

	public static JSONArray getShopsOfChain(String chainId) throws Exception {
		System.out.println("--------从优衣地图读取连锁商"+chainId+"下的所有店铺的ID--------");
		String str = MyHttpClient.get("http://api.youyimap.com/api/storages/"+App.storageId+"/getshops?chainId="+chainId+"&access_token="+App.access_token);
		if(str.indexOf("[")<0) {
//			System.out.println(rtn);
			throw new Exception();
		}
		System.out.println(str);
		
		return JSONArray.parseArray(str);
	}

	public static JSONObject getShop(String shopId) {
		System.out.println("--------从优衣地图读取ID为："+shopId+"的店铺--------");
		String str = MyHttpClient.get("http://api.youyimap.com/api/storages/"+App.storageId+"/getShop?shopId="+shopId+"&access_token="+App.access_token);
		System.out.println(str);
		
		return JSONObject.parseObject(str);
	}

	public static void sortShops(JSONArray shops, JSONArray shopsOnYouyimap, JSONArray shopsToCreate, JSONArray shopsToDelete, JSONArray shopsExist){
		System.out.println("--------提取出优衣地图需要创建的店铺、需要删除的店铺、已经存在的店铺--------");
		if((shopsOnYouyimap==null||shopsOnYouyimap.size()==0)&&shops!=null) {
			for(int i=0;i<shops.size();i++) {
				shopsToCreate.add(shops.getJSONObject(i));
//				System.out.println("----Create-----"+shops.getJSONObject(i).getString("id"));
			}
		}
		else if((shops==null||shops.size()==0)&&shopsOnYouyimap!=null) {
			for(int i=0;i<shopsOnYouyimap.size();i++) {
				shopsToDelete.add(shopsOnYouyimap.getJSONObject(i));
//				System.out.println("----delete-----"+shopIds[i]);
			}
		}
		else {
			Set<String> exitIdSet = new HashSet<String>();
			for(int i=0;i<shops.size();i++) {
				for(int j=0;j<shopsOnYouyimap.size();j++) {
					if(shops.getJSONObject(i).getString("id").equals(shopsOnYouyimap.getJSONObject(j).getString("id"))) {
						shopsExist.add(shops.getJSONObject(i));
//						System.out.println("----Exist-----"+shops.getJSONObject(i).getString("id"));
						exitIdSet.add(shops.getJSONObject(i).getString("id"));
						break;
					}
					if(j==shopsOnYouyimap.size()-1) {
						shopsToCreate.add(shops.getJSONObject(i));
//						System.out.println("----Create---"+shops.getJSONObject(i).getString("id"));
					}
				}
			}
			for(int j=0;j<shopsOnYouyimap.size();j++) {
				if(!exitIdSet.contains(shopsOnYouyimap.getJSONObject(j).getString("id"))) {
//					System.out.println("----Delete---"+shopIds[j]);
					shopsToDelete.add(shopsOnYouyimap.getJSONObject(j));
				}
			}
		}
	}
	

	public static void deleteCommoditiesOfShop(String shopId) throws Exception {
		System.out.println("--------指示优衣地图删除店铺"+shopId+"下的所有商品--------");
		String rtn = MyHttpClient.get("http://api.youyimap.com/api/storages/"+App.storageId+"/deleteCommoditiesOfShop?shopId="+shopId+"&access_token="+App.access_token);
		if(rtn.indexOf("SUCCESS")<0) {
//			System.out.println(rtn);
			throw new Exception();
		}
	}
	
	public static void deleteShop(String shopId) throws Exception {
		System.out.println("--------指示优衣地图删除店铺"+shopId+"-------");
		String rtn = MyHttpClient.get("http://api.youyimap.com/api/storages/"+App.storageId+"/deleteShop?shopId="+shopId+"&access_token="+App.access_token);
		if(rtn.indexOf("SUCCESS")<0) {
//			System.out.println(rtn);
			throw new Exception();
		}
	}
	

	public static JSONArray getCommoditiesOfShop(String shopId) throws Exception {
		System.out.println("--------从优衣地图获取店铺"+shopId+"下的所有商品-------");
		String str = MyHttpClient.get("http://api.youyimap.com/api/storages/"+App.storageId+"/getCommodities?shopId="+shopId+"&access_token="+App.access_token);
		if(str.indexOf("[")<0) {
//			System.out.println(rtn);
			throw new Exception();
		}
		return JSONArray.parseArray(str);
	}
	
	public static void sortCommodities(JSONArray commodities, JSONArray commoditiesOnYouyimap, JSONArray commoditiesToCreate, JSONArray commoditiesToDelete, JSONArray commoditiesToModify) {
		System.out.println("--------提取出优衣地图需要创建的商品、需要删除的商品、需要修改数据的商品--------");
		if(commodities!=null&&(commoditiesOnYouyimap==null||commoditiesOnYouyimap.size()==0)) {
			for(int i=0;i<commodities.size();i++) {
				commoditiesToCreate.add(commodities.getJSONObject(i));
//				System.out.println("----Create-----"+commodities[i].productCode);
			}
		}
		else if(commoditiesOnYouyimap!=null&&(commodities==null||commodities.size()==0)) {
			for(int i=0;i<commoditiesOnYouyimap.size();i++) {
				commoditiesToDelete.add(commoditiesOnYouyimap.getJSONObject(i));
//				System.out.println("----delete-----"+commodityProductCodes[i]);
			}
		}
		else {
			Set<String> existSet = new HashSet<String>();
			for(int i=0;i<commodities.size();i++) {
				for(int j=0;j<commoditiesOnYouyimap.size();j++) {
					JSONObject commodityLocal = commodities.getJSONObject(i);
					JSONObject commodityOnYouyimap = commoditiesOnYouyimap.getJSONObject(j);
					commodityOnYouyimap.remove("shopId");
					if(commodityLocal.getString("productCode")
							.equals(commodityOnYouyimap.getString("productCode"))) {
//						System.out.println("----Exist-----"+commodities[i].productCode);
						existSet.add(commodityLocal.getString("productCode"));
						if(!commodityLocal.toString()
								.equals(commodityOnYouyimap.toString())) {
							System.out.println("发现本地商品数据与优衣地图上的数据有差异。\n本地商品数据："+commodityLocal.toString()+"\n优衣地图上的商品数据："+commodityOnYouyimap.toString());
							commoditiesToModify.add(commodityLocal);
						}
						break;
					}
					if(j==commoditiesOnYouyimap.size()-1) {
						commoditiesToCreate.add(commodityLocal);
//						System.out.println("----Create---"+commodities[i].productCode);
					}
				}
			}
			for(int j=0;j<commoditiesOnYouyimap.size();j++) {
				if(!existSet.contains(commoditiesOnYouyimap.getJSONObject(j).getString("productCode"))) {
//					System.out.println("----Delete---"+commodityProductCodes[j]);
					commoditiesToDelete.add(commoditiesOnYouyimap.getJSONObject(j));
				}
			}
		}
	}
	
	public static void deleteCommodities(String shopId, JSONArray commodities) throws Exception{
		System.out.println("--------删除店铺"+shopId+"下的"+commodities.size()+"件商品-----");
		JSONObject param = new JSONObject();
		param.put("shopId", shopId);
		JSONArray productCodes = new JSONArray();
		for(int i=0;i<commodities.size();i++) {
			productCodes.add(commodities.getJSONObject(i).getString("productCode"));
		}
		param.put("productCodes", productCodes);
		String rtn = MyHttpClient.postJson("http://api.youyimap.com/api/storages/"+App.storageId+"/deleteCommodities?access_token="+App.access_token, param.toString());
		if(rtn.indexOf("SUCCESS")<0) {
			System.out.println(rtn);
			throw new Exception();
		}
	}
	
	public static void modifyCommodity(String shopId, JSONObject commodityToModify) throws Exception{
		System.out.println("--------修改商品"+commodityToModify.getString("productCode")+"--------");
		JSONObject newCommodity = (JSONObject)commodityToModify.clone();
		newCommodity.put("shopId", shopId);
		String rtn = MyHttpClient.postJson("http://api.youyimap.com/api/storages/"+App.storageId+"/modifyCommodity?access_token="+App.access_token, newCommodity.toString());
		if(rtn.indexOf("SUCCESS")<0) {
			System.out.println(rtn);
			throw new Exception();
		}
	}
}


//class JSONObject {
//	public String id;
//	public String name;
//	public String tel;
//	public String liaison;
//	public JSONArray shops;
//}
//
//class JSONObject {
//	public String id;
//	public String name;
//	public String address;
//	public String tel;
//	public String liaison;
//	public JSONArray commodities;
//}
//
//class JSONObject {
//	public String productCode;
//	public String name;
//	public String brandName;
//	public String colors;
//	public String sizes;
//	public String quantities;
//	public int signPrice;
//	public int price;
//}
