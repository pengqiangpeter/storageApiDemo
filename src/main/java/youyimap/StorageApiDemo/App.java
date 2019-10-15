package youyimap.StorageApiDemo;

import java.util.Calendar;  
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.text.SimpleDateFormat;

public class App {
	public static String storageId = "1";
	public static String access_token = "8SijhdNduIoulcNfSwAnaEBfzuBZdYeaUoCoK9aDTmLdhK0NZWHTiro8JD8tTziu";
	

	public static void main(String[] args) {
//		若进销存软件管理的是连锁店，使用以下代码
		syncChainData();
//		若进销存软件管理的是单个店铺，使用以下代码
		syncSingleShopData();
//		删除进销存软件商名下的所有连锁商、店铺、商品，慎用。
//		deleteAllData();
	}

	public static JSONObject chainData = new JSONObject();
	
	public static void syncChainData() {
		chainData.put("id", "1-swsn1");
		JSONArray shops = new JSONArray();
		chainData.put("shops", shops);
		
		JSONObject shop,commodity, quantities;
		JSONArray commodities,colors, sizes;
		
		shop = new JSONObject();
		shops.add(shop);
		shop.put("id","1-swsn1-exampleShop1Id");
		commodities = new JSONArray();
		shop.put("commodities", commodities);
				
		commodity= new JSONObject();
		commodities.add(commodity);
		commodity.put("productCode", "exampleProductCode1");
		commodity.put("price", 199);
		colors = JSONArray.parseArray("[\"红色\",\"蓝色\"]");
		commodity.put("colors", colors);
		sizes = JSONArray.parseArray("[\"S\",\"M\",\"L\"]");
		commodity.put("sizes", sizes);
		quantities = JSONObject.parseObject("{\"红色\":{\"S\":1,\"M\":1},\"蓝色\":{\"L\":13}}");
		commodity.put("quantities", quantities);

		commodity= new JSONObject();
		commodities.add(commodity);
		commodity.put("productCode", "exampleProductCode2");
		commodity.put("price", 299);
		colors = JSONArray.parseArray("[\"唯一色\"]");
		commodity.put("colors", colors);
		sizes = JSONArray.parseArray("[\"160\",\"165\",\"170\"]");
		commodity.put("sizes", sizes);
		quantities = JSONObject.parseObject("{\"唯一色\":{\"160\":1211,\"170\":12}}");
		commodity.put("quantities", quantities);

		shop = new JSONObject();
		shops.add(shop);
		shop.put("id","1-swsn1-exampleShop2Id");
		commodities = new JSONArray();
		shop.put("commodities", commodities);
				
		commodity= new JSONObject();
		commodities.add(commodity);
		commodity.put("productCode", "exampleProductCode3");
		commodity.put("price", 399);
		colors = JSONArray.parseArray("[\"乳白色\",\"粉红色\"]");
		commodity.put("colors", colors);
		sizes = JSONArray.parseArray("[\"通码\"]");
		commodity.put("sizes", sizes);
		quantities = JSONObject.parseObject("{\"乳白色\":{\"通码\":12},\"粉红色\":{\"通码\":124}}");
		commodity.put("quantities", quantities);		

		Calendar calendar = Calendar.getInstance();  
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, (int)(Math.random()*5)+1);

		UploadChainDataThread.run();

	    TimerTask timerTask  = new TimerTask() {  
	        public void run() {  
	        	UploadChainDataThread.run();
	        }  
	    };
	    int period = 3;
	    new Timer().scheduleAtFixedRate(timerTask, calendar.getTime(), 1000 *60 * 60 * 24 * period);
	    System.out.println("同步操作将每"+period+"天执行一次，一次执行时间为"
	    		+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime())
	    );


	    //模拟添加店铺事件
	    new Timer().schedule(new TimerTask(){
          public void run(){
        	JSONObject shop = new JSONObject();
      		shop.put("id","1-swsn1-exampleShop3Id");
      		JSONArray shops = new JSONArray();
      		shops.add(shop);
            try {
				Utils.createShops(chainData.getString("id"), shops);
			} catch (Exception e) {
				e.printStackTrace();
			}
          }
        },1000*6);
	    	    
	    //模拟删除店铺事件
	    new Timer().schedule(new TimerTask(){
          public void run(){
            try {
				Utils.deleteShop("1-swsn1-exampleShop3Id");
			} catch (Exception e) {
				e.printStackTrace();
			}
          }
        },1000*8);
	    
	    //模拟添加商品事件
	    new Timer().schedule(new TimerTask(){
          public void run(){  				
        	JSONObject  commodity= new JSONObject();
    		commodity.put("productCode", "exampleProductCode4");
    		commodity.put("price", 199);
    		JSONArray colors = JSONArray.parseArray("[\"天蓝色\",\"墨绿色\"]");
    		commodity.put("colors", colors);
    		JSONArray sizes = JSONArray.parseArray("[\"通码\"]");
    		commodity.put("sizes", sizes);
    		JSONObject  quantities = JSONObject.parseObject("{\"蓝色\":{\"通码\":12},\"墨绿色\":{\"通码\":124}}");
    		commodity.put("quantities", quantities);		
        	JSONArray commodities = new JSONArray();  
    		commodities.add(commodity);
            try {
				Utils.createCommodities("1-swsn1-exampleShop2Id", commodities);
			} catch (Exception e) {
				e.printStackTrace();
			}
          }
        },1000*10);

	    //模拟删除商品事件
	    new Timer().schedule(new TimerTask(){
          public void run(){
          	JSONObject  commodity= new JSONObject();
      		commodity.put("productCode", "exampleProductCode4");
          	JSONArray commodities = new JSONArray();  
      		commodities.add(commodity);
            try {
				Utils.deleteCommodities("1-swsn1-exampleShop2Id", commodities);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
        },1000*15);
	    
	    //模拟修改商品事件
	    new Timer().schedule(new TimerTask(){
          public void run(){
        	JSONObject commodity= new JSONObject();
    		commodity.put("productCode", "exampleProductCode3");
    		commodity.put("price", 200);
    		JSONArray colors = JSONArray.parseArray("[\"乳白色\",\"粉红色\"]");
    		commodity.put("colors", colors);
    		JSONArray sizes = JSONArray.parseArray("[\"通码\"]");
    		commodity.put("sizes", sizes);
    		JSONObject quantities = JSONObject.parseObject("{\"乳白色\":{\"通码\":10},\"粉红色\":{\"通码\":100}}");
    		commodity.put("quantities", quantities);		
            try {
				Utils.modifyCommodity("1-swsn1-exampleShop2Id", commodity);
			} catch (Exception e) {
				e.printStackTrace();
			}
          }
        },1000*14);
	}
	

	public static JSONObject shopData = new JSONObject();

	public static void syncSingleShopData() {	
		JSONObject commodity, quantities;
		JSONArray commodities,colors, sizes;
		
		shopData.put("id","1-swsn2");
		commodities = new JSONArray();
		shopData.put("commodities", commodities);
				
		commodity= new JSONObject();
		commodities.add(commodity);
		commodity.put("productCode", "exampleProductCode1");
		commodity.put("price", 199);
		colors = JSONArray.parseArray("[\"红色\",\"蓝色\"]");
		commodity.put("colors", colors);
		sizes = JSONArray.parseArray("[\"S\",\"M\",\"L\"]");
		commodity.put("sizes", sizes);
		quantities = JSONObject.parseObject("{\"红色\":{\"S\":1,\"M\":1111},\"蓝色\":{\"L\":13}}");
		commodity.put("quantities", quantities);

		commodity= new JSONObject();
		commodities.add(commodity);
		commodity.put("productCode", "exampleProductCode2");
		commodity.put("price", 199);
		colors = JSONArray.parseArray("[\"唯一色\"]");
		commodity.put("colors", colors);
		sizes = JSONArray.parseArray("[\"160\",\"165\",\"170\"]");
		commodity.put("sizes", sizes);
		quantities = JSONObject.parseObject("{\"唯一色\":{\"160\":1211,\"170\":12}}");
		commodity.put("quantities", quantities);

		Calendar calendar = Calendar.getInstance();  
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, (int)(Math.random()*5)+1);

		UploadSingleShopThread.run();

	    TimerTask timerTask  = new TimerTask() {  
	        public void run() {  
	        	UploadSingleShopThread.run();
	        }  
	    };
	    int period = 3;
	    new Timer().scheduleAtFixedRate(timerTask, calendar.getTime(), 1000 *60 * 60 * 24 * period);
	    System.out.println("同步操作将每"+period+"天执行一次，一次执行时间为"
	    		+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime())
	    );
	    
	    //模拟添加商品事件
	    new Timer().schedule(new TimerTask(){
          public void run(){  				
        	JSONObject  commodity= new JSONObject();
    		commodity.put("productCode", "exampleProductCode3");
    		commodity.put("price", 199);
    		JSONArray colors = JSONArray.parseArray("[\"天蓝色\",\"墨绿色\"]");
    		commodity.put("colors", colors);
    		JSONArray sizes = JSONArray.parseArray("[\"通码\"]");
    		commodity.put("sizes", sizes);
    		JSONObject  quantities = JSONObject.parseObject("{\"蓝色\":{\"通码\":12},\"墨绿色\":{\"通码\":124}}");
    		commodity.put("quantities", quantities);		
        	JSONArray commodities = new JSONArray();  
    		commodities.add(commodity);
            try {
				Utils.createCommodities("1-swsn2", commodities);
			} catch (Exception e) {
				e.printStackTrace();
			}
          }
        },1000*6);

	    //模拟删除商品事件
	    new Timer().schedule(new TimerTask(){
          public void run(){
          	JSONObject  commodity= new JSONObject();
      		commodity.put("productCode", "exampleProductCode3");
          	JSONArray commodities = new JSONArray();  
      		commodities.add(commodity);
            try {
				Utils.deleteCommodities("1-swsn2", commodities);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
        },1000*8);
	    
	    //模拟修改商品事件
	    new Timer().schedule(new TimerTask(){
          public void run(){
        	JSONObject commodity= new JSONObject();
    		commodity.put("productCode", "exampleProductCode2");
    		commodity.put("price", 199);
    		JSONArray colors = JSONArray.parseArray("[\"唯一色\"]");
    		commodity.put("colors", colors);
    		JSONArray sizes = JSONArray.parseArray("[\"160\",\"165\",\"170\"]");
    		commodity.put("sizes", sizes);
    		JSONObject quantities = JSONObject.parseObject("{\"唯一色\":{\"160\":1111,\"170\":12}}");
    		commodity.put("quantities", quantities);
            try {
				Utils.modifyCommodity("1-swsn2", commodity);
			} catch (Exception e) {
				e.printStackTrace();
			}
          }
        },1000*10);
	}
	
	public static void deleteAllData(){
		String rtn = MyHttpClient.get("http://api.yiyimap.com/api/storages/"+App.storageId+"/deleteAllData?access_token="+App.access_token);
		if(rtn.indexOf("SUCCESS")<0) {
			System.out.println(rtn);
		} else {
			System.out.println("**********删除成功**********");
		}
	}
}


class UploadChainDataThread {
	public static void run() {
		System.out.println("*********开始与优衣地图同步数据**********");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println("当前时间为："+df.format(new Date()));// new Date()为获取当前系统时间
		
		JSONObject chainData = App.chainData;
		
		JSONObject chain = Utils.getChain(chainData.getString("id"));
		if(chain.getString("id")==null) {
			System.out.println("id为"+chainData.getString("id")+"的连锁商不存在");
			try {
				//创建连锁商
				Utils.createChain(chainData);
				JSONArray shops = chainData.getJSONArray("shops");
				if(shops!=null&&shops.size()>0) {
					//创建店铺
					Utils.createShops(chainData.getString("id"), chainData.getJSONArray("shops"));
					//创建店铺下的商品
					for(int i=0;i<shops.size();i++) {
						JSONObject shop = shops.getJSONObject(i);
						JSONArray commodities = shop.getJSONArray("commodities");
						if(commodities!=null&&commodities.size()>0) {
							Utils.createCommodities(shop.getString("id"), commodities);
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				//读取连锁商下所有店铺
				JSONArray shops = Utils.getShopsOfChain(chainData.getString("id"));
				JSONArray shopsToCreate = new JSONArray();
				JSONArray shopsToDelete = new JSONArray();
				JSONArray shopsExist = new JSONArray();
				Utils.sortShops(chainData.getJSONArray("shops"), shops, shopsToCreate, shopsToDelete, shopsExist);
				System.out.println("有"+shopsToCreate.size()+"个店铺需要创建，有"+shopsToDelete.size()+"个店铺需要删除，有"+shopsExist.size()+"个店铺已经存在");
				if(shopsToCreate.size()>0) {
					//创建未创建的店铺
					Utils.createShops(chainData.getString("id"), shopsToCreate);
					for(int i=0;i<shopsToCreate.size();i++) {
						JSONObject shop = shopsToCreate.getJSONObject(i);
						if(shop.getJSONArray("commodities")!=null&&shop.getJSONArray("commodities").size()>0) {
							//创建新建的店铺下的商品
							Utils.createCommodities(shop.getString("id"), shop.getJSONArray("commodities"));
						}
					}
				}
				if(shopsToDelete.size()>0) {
					for(int i=0;i<shopsToDelete.size();i++) {
						String shopId = shopsToDelete.getJSONObject(i).getString("id");
						Utils.deleteCommoditiesOfShop(shopId);
						Utils.deleteShop(shopId);
					}
				}
				if(shopsExist.size()>0) {
					for(int i=0;i<shopsExist.size();i++) {
						JSONObject shop = shopsExist.getJSONObject(i);
						//读取店铺下所有商品
						JSONArray commoditiesOnYouyimap = Utils.getCommoditiesOfShop(shop.getString("id"));

						JSONArray commoditiesToCreate = new JSONArray();
						JSONArray commoditiesToDelete = new JSONArray();
						JSONArray commoditiesToModify = new JSONArray();
						Utils.sortCommodities(shop.getJSONArray("commodities"), commoditiesOnYouyimap, commoditiesToCreate, commoditiesToDelete, commoditiesToModify);

						System.out.println("有"+commoditiesToCreate.size()+"个商品需要创建，有"+commoditiesToDelete.size()+"个商品需要删除，有"+commoditiesToModify.size()+"个商品需要修改");
						if(commoditiesToCreate.size()>0) {
							Utils.createCommodities(shop.getString("id"), commoditiesToCreate);
						}
						if(commoditiesToDelete.size()>0) {
							Utils.deleteCommodities(shop.getString("id"), commoditiesToDelete);
						}
						if(commoditiesToModify.size()>0) {
							for(int j=0;j<commoditiesToModify.size();j++) {
								JSONObject commodityToModify = commoditiesToModify.getJSONObject(j);
								Utils.modifyCommodity(shop.getString("id"), commodityToModify);
							}
						}
					}
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("*********同步完成**********");
	}
	
}



class UploadSingleShopThread {
	public static void run() {
		System.out.println("*********开始与优衣地图同步数据**********");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println("当前时间为："+df.format(new Date()));// new Date()为获取当前系统时间
		
		JSONObject shopData = App.shopData;
		
		JSONObject shop = Utils.getShop(shopData.getString("id"));
		if(shop.getString("id")==null) {
			System.out.println("id为"+shopData.getString("id")+"的店铺不存在");
			try {
				//创建店铺
				JSONArray shops= new JSONArray();
				shops.add(shopData);
				Utils.createShops(null, shops);
				//创建店铺下的商品
				JSONArray commodities = shopData.getJSONArray("commodities");
				if(commodities!=null&&commodities.size()>0) {
					Utils.createCommodities(shopData.getString("id"), commodities);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			//读取店铺下所有商品
			JSONArray commoditiesOnYouyimap;
			try {
				commoditiesOnYouyimap = Utils.getCommoditiesOfShop(shopData.getString("id"));
				JSONArray commoditiesToCreate = new JSONArray();
				JSONArray commoditiesToDelete = new JSONArray();
				JSONArray commoditiesToModify = new JSONArray();
				Utils.sortCommodities(shopData.getJSONArray("commodities"), commoditiesOnYouyimap, commoditiesToCreate, commoditiesToDelete, commoditiesToModify);

				System.out.println("有"+commoditiesToCreate.size()+"个商品需要创建，有"+commoditiesToDelete.size()+"个商品需要删除，有"+commoditiesToModify.size()+"个商品需要修改");
				if(commoditiesToCreate.size()>0) {
					Utils.createCommodities(shopData.getString("id"), commoditiesToCreate);
				}
				if(commoditiesToDelete.size()>0) {
					Utils.deleteCommodities(shopData.getString("id"), commoditiesToDelete);
				}
				if(commoditiesToModify.size()>0) {
					for(int j=0;j<commoditiesToModify.size();j++) {
						JSONObject commodityToModify = commoditiesToModify.getJSONObject(j);
						Utils.modifyCommodity(shopData.getString("id"), commodityToModify);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("*********同步完成**********");
	}
	
}

