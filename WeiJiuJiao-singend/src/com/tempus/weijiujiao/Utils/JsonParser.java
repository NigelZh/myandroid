package com.tempus.weijiujiao.Utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.bean.Device;
import com.tempus.weijiujiao.bean.Light;
import com.tempus.weijiujiao.bean.ProductInfo;
import com.tempus.weijiujiao.bean.ProductInfo.BasicInfo;
import com.tempus.weijiujiao.bean.ProductInfo.BrandInfo;
import com.tempus.weijiujiao.bean.ProductInfo.TasteInfo;
import com.tempus.weijiujiao.bean.ProductInfo.VarietyInfo;
import com.tempus.weijiujiao.bean.Regional;
import com.tempus.weijiujiao.bean.ReportEntity;
import com.tempus.weijiujiao.bean.TracePoint;
import com.tempus.weijiujiao.bean.User;
import com.tempus.weijiujiao.bean.Version;

public class JsonParser {
	/**
	 * 仅解析操作结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	private static ParserResult parserStatus(String jsonStr)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		ParserResult result = new ParserResult();
		result.setCode(jb.getInt("StatusCode"));
		return result;
	}

	/**
	 * 解析当前用户对设备时候有支配权限。
	 * 
	 * @param jsonStr
	 * @return 0为有支配权限，1没有，2服务器异常
	 * @throws JSONException
	 */
	public static ParserResult parserDeviceAuthority(String jsonStr)
			throws JSONException {
		return parserStatus(jsonStr);
	}

	/**
	 * 解析获取验证码结果
	 * 
	 * @param jsonStr
	 *            json结果
	 * @return ParserResult
	 * @throws JSONException
	 */
	public static ParserResult parserRandomCode(String jsonStr)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		ParserResult result = new ParserResult();
		result.setCode(jb.getInt("StatusCode"));
		result.setObj(jb.getString("VerifyCode"));
		return result;
	}

	/**
	 * 解析注册结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserRegister(String jsonStr)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		ParserResult result = new ParserResult();
		result.setCode(jb.getInt("StatusCode"));
		result.setObj(jb.getString("UserId"));
		return result;
	}

	/**
	 * 解析重置密码结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserResetPassword(String jsonStr)
			throws JSONException {
		return parserStatus(jsonStr);
	}

	/**
	 * 解析登录结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserLogin(String jsonStr) throws JSONException {
		ParserResult result = new ParserResult();
		User user = User.getUser();
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		result.setCode(jb.getInt("StatusCode"));
		if (result.getCode() == 0) {
			user.setLogin(true);
			JSONObject jInfo = jb.getJSONObject("UserInfo");
			user.setUserName(jInfo.getString("UserName"));
			user.setUserAddress(jInfo.getString("UserAddress"));
			user.setUserEmail(jInfo.getString("UserEmail"));
			user.setUserType(jInfo.getInt("UserType"));
			user.setUserID(jInfo.getString("UserId"));
			user.setUserNumber(jInfo.getString("UserNumber"));
			user.setUserRegisterTime(jInfo.getString("UserRegisterTime"));
			user.setUserImagerURL(jInfo.getString("UserImage"));
			user.setGravidenCount(jInfo.getInt("JGNum"));
			user.setWinecallerCount(jInfo.getInt("JJNum"));
		} else {
			user.setLogin(false);
		}
		result.setObj(user);
		return result;
	}

	/**
	 * 解析设备列表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserDeviceList(String jsonStr)
			throws JSONException {
		ParserResult result = new ParserResult();
		List<Device> deviceList = null;
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		result.setCode(jb.getInt("StatusCode"));
		if (result.getCode() == 0) {
			result.setTotalPage(jb.getInt("totalPage"));
			deviceList = new ArrayList<Device>();
			JSONArray dArray = jb.getJSONArray("DeviceList");
			for (int i = 0; i < dArray.length(); i++) {
				Device d = new Device();
				JSONObject jbd = dArray.getJSONObject(i);
				d.setId(jbd.getString("id"));
				d.setSize(jbd.getInt("Size"));
				d.setStock(jbd.getInt("StockCount"));
				d.setName(jbd.getString("Name"));
				d.setOwn(jbd.getInt("Owner") == 0 ? true : false);
				d.setPositon(jbd.getString("Position"));
				d.setAddress(jbd.getString("Address"));
				d.setTemperature(jbd.getString("Temperature"));
				d.setRemark(jbd.getString("Remark"));
				d.setImgURL(jbd.getString("Img"));
				d.setEmail(jbd.getString("Email"));
				d.setNumber(jbd.getString("PhoneNum"));
				deviceList.add(d);
			}
			result.setObj(deviceList);
		}
		return result;
	}

	/**
	 * 商品信息解析
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserProductInfo(String jsonStr)
			throws JSONException {
		Debug.Out(jsonStr);
		ParserResult result = new ParserResult();
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jbInfo = (JSONObject) jsonParser.nextValue();
		ProductInfo pinfo = new ProductInfo();
		result.setCode(jbInfo.getInt("StatusCode"));
		if (result.getCode() == 0) {
			if (jbInfo.getJSONObject("ProductBasicInfo") != null) {
				JSONObject jbBasic = jbInfo.getJSONObject("ProductBasicInfo");
				BasicInfo binfo = pinfo.new BasicInfo();
				binfo.setId(jbBasic.getString("ID"));
				binfo.setName(jbBasic.getString("Name"));
				binfo.setName_en(jbBasic.getString("Name_en"));
				binfo.setYear(jbBasic.getString("Year"));
				binfo.setArea(jbBasic.getString("Area"));
				binfo.setAlcoholStrength(jbBasic.getString("AlcoholStrength"));
				binfo.setSubCategory(jbBasic.getString("SubCategory"));
				binfo.setVolume(jbBasic.getString("Volume"));
				binfo.setLocation(jbBasic.getString("Location"));
				binfo.setPrice(Float.parseFloat(jbBasic.getString("Price")));
				binfo.setStar(jbBasic.getInt("Star"));
				binfo.setSeller(jbBasic.getString("Seller"));
				binfo.setImageURL(jbBasic.getString("image"));
				binfo.setBrandName(jbBasic.getString("BrandName"));
				if (jbBasic.getJSONArray("TracePoint") != null) {
					JSONArray traceArray = jbBasic.getJSONArray("TracePoint");
					List<TracePoint> traceList = new ArrayList<TracePoint>();
					for (int i = 0; i < traceArray.length(); i++) {
						JSONObject jbtrace = traceArray.getJSONObject(i);
						TracePoint tp = new TracePoint();
						tp.setTime(jbtrace.getString("time"));
						tp.setImgUrl(jbtrace.getString("Img"));
						traceList.add(tp);
					}
					binfo.setTraceList(traceList);
				}
				pinfo.setBasicinfo(binfo);
			}
			if (jbInfo.getJSONObject("ProductBrandInfo") != null) {
				JSONObject jbBrand = jbInfo.getJSONObject("ProductBrandInfo");
				BrandInfo binfo = pinfo.new BrandInfo();
				binfo.setId(jbBrand.getString("Id"));
				binfo.setName(jbBrand.getString("name"));
				binfo.setNameEn(jbBrand.getString("Name_en"));
				binfo.setArea(jbBrand.getString("Area"));
				binfo.setRank(jbBrand.getString("Rank"));
				binfo.setAverage(jbBrand.getString("Average"));
				binfo.setVarietites(jbBrand.getString("Varietites"));
				binfo.setBestYear(jbBrand.getString("bestYear"));
				binfo.setWebstate(jbBrand.getString("Webstate"));
				binfo.setDetialDesc(jbBrand.getString("Introduction"));
				binfo.setRegion(jbBrand.getString("Region"));
				binfo.setImageURL(jbBrand.getString("Img"));
				pinfo.setBrandinfo(binfo);
			}
			if (jbInfo.getJSONArray("ProductVarietyInfo") != null) {
				JSONArray varietyArray = jbInfo
						.getJSONArray("ProductVarietyInfo");
				List<VarietyInfo> varietyList = new ArrayList<ProductInfo.VarietyInfo>();
				for (int i = 0; i < varietyArray.length(); i++) {
					JSONObject jbVariety = varietyArray.getJSONObject(i);
					VarietyInfo vinfo = pinfo.new VarietyInfo();
					vinfo.setName(jbVariety.getString("name"));
					vinfo.setName_en(jbVariety.getString("Name_en"));
					vinfo.setDesc(jbVariety.getString("Desc"));
					varietyList.add(vinfo);
				}
				pinfo.setVarietyInfoList(varietyList);
			}
			if (jbInfo.getJSONObject("ProductTasteInfo") != null) {
				JSONObject jbTaste = jbInfo.getJSONObject("ProductTasteInfo");
				TasteInfo tInfo = pinfo.new TasteInfo();
				tInfo.setSoberTime(jbTaste.getString("SoberTime"));
				tInfo.setSampletemperature(jbTaste
						.getString("Sampletemperature"));
				tInfo.setFragrance(jbTaste.getString("Fragrance"));
				tInfo.setWithDishes(jbTaste.getString("WithDishes"));
				tInfo.setRobertScore(jbTaste.getString("RobertScore"));
				tInfo.setObserverScore(jbTaste.getString("ObserverScore"));
				tInfo.setExpert(jbTaste.getString("Expert"));
				pinfo.setTeasteinfo(tInfo);
			}
		}
		result.setObj(pinfo);
		return result;
	}

	/**
	 * 解析检索结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserSearch(String jsonStr)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		ParserResult result = new ParserResult();
		result.setCode(jb.getInt("StatusCode"));
		List<ProductInfo> pInfoList;
		if (result.getCode() == 0) {
			result.setTotalPage(jb.getInt("totalPage"));
			if (jb.getJSONArray("ProductBasicInfoList") != null) {
				JSONArray basicArray = jb.getJSONArray("ProductBasicInfoList");
				pInfoList = new ArrayList<ProductInfo>();
				for (int i = 0; i < basicArray.length(); i++) {
					JSONObject jbBasic = basicArray.getJSONObject(i);
					ProductInfo pInfo = new ProductInfo();
					BasicInfo bInfo = pInfo.new BasicInfo();
					bInfo.setId(jbBasic.getString("ID"));
					bInfo.setName(jbBasic.getString("Name"));
					bInfo.setName_en(jbBasic.getString("Name_en"));
					bInfo.setYear(jbBasic.getString("Year"));
					bInfo.setArea(jbBasic.getString("Area"));
					bInfo.setAlcoholStrength(jbBasic
							.getString("AlcoholStrength"));
					bInfo.setSubCategory(jbBasic.getString("SubCategory"));
					bInfo.setVolume(jbBasic.getString("Volume"));
					bInfo.setLocation(jbBasic.getString("Location"));
					bInfo.setImageURL(jbBasic.getString("image"));
					if (jbBasic.getJSONArray("TracePoint") != null) {
						JSONArray traceArray = jbBasic
								.getJSONArray("TracePoint");
						List<TracePoint> traceList = new ArrayList<TracePoint>();
						for (int j = 0; j < traceArray.length(); j++) {
							JSONObject jbtrace = traceArray.getJSONObject(j);
							TracePoint tp = new TracePoint();
							tp.setTime(jbtrace.getString("time"));
							tp.setImgUrl(jbtrace.getString("Img"));
							traceList.add(tp);
						}
						bInfo.setTraceList(traceList);
					}
					pInfo.setBasicinfo(bInfo);
					pInfoList.add(pInfo);
				}
				result.setObj(pInfoList);
			}
		}
		return result;
	}

	/**
	 * 解析添加设备结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserAddDevice(String jsonStr)
			throws JSONException {
		return parserStatus(jsonStr);
	}

	/**
	 * 解析更新信息结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserInfoUpdate(String jsonStr)
			throws JSONException {
		return parserStatus(jsonStr);
	}

	/**
	 * 解析更新结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserAppUpgrade(String jsonStr)
			throws JSONException {
		// return parserStatus(jsonStr);
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		ParserResult result = new ParserResult();
		result.setCode(jb.getInt("StatusCode"));
		if (result.getCode() == 0) {
			Version v = new Version();
			v.setVersionCode(jb.getInt("NewVersionCode"));
			v.setVersionName(jb.getString("NewVersionName"));
			v.setAppUrl(jb.getString("URL"));
			result.setObj(v);
		}
		return result;
	}

	/**
	 * 解析灯源列表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserLightList(String jsonStr)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		ParserResult result = new ParserResult();
		result.setCode(jb.getInt("StatusCode"));
		if (result.getCode() == 0) {
			if (jb.getJSONArray("LightList") != null) {
				List<Light> lightList = new ArrayList<Light>();
				JSONArray lightArray = jb.getJSONArray("LightList");
				for (int i = 0; i < lightArray.length(); i++) {
					JSONObject jbLight = lightArray.getJSONObject(i);
					Light l = new Light();
					l.setId(jbLight.getString("id"));
					int lightStaus = jbLight.getInt("Status");
					if (lightStaus == 0) {
						l.setON(true);
						l.setUseable(true);
					} else if (lightStaus == 1) {
						l.setON(false);
						l.setUseable(true);
					} else if (lightStaus == 2) {
						l.setON(false);
						l.setUseable(false);
					}
					lightList.add(l);
				}
				result.setObj(lightList);
			}
		}
		return result;
	}

	/**
	 * 解析酒窖区域列表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserRegionalList(String jsonStr)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		ParserResult result = new ParserResult();
		result.setCode(jb.getInt("StatusCode"));
		if (result.getCode() == 0) {
			if (jb.getJSONArray("RegionalList") != null) {
				JSONArray regionalArray = jb.getJSONArray("RegionalList");
				List<Regional> regionalList = new ArrayList<Regional>();
				for (int i = 0; i < regionalArray.length(); i++) {
					JSONObject jbR = regionalArray.getJSONObject(i);
					Regional r = new Regional();
					r.setId(jbR.getString("ID"));
					r.setName(jbR.getString("Name"));
					r.setStock(jbR.getInt("Stock"));
					r.setVolume(jbR.getInt("Volume"));
					regionalList.add(r);
				}
				result.setObj(regionalList);
			}
		}
		return result;
	}

	/**
	 * 获得cmd
	 * 
	 * @param msgJson
	 * @return
	 * @throws JSONException
	 */
	public static String parserSocketMessageCMD(String msgJson)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		return jb.getString("CmdWord");
	}

	/**
	 * 解析连接状态
	 * 
	 * @param msgJson
	 * @return 1连接成功，0连接失败
	 * @throws JSONException
	 */
	public static int parserSocketConnectState(String msgJson)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		String Connectstate = jb.getJSONObject("Content").getString("message");
		if (Connectstate.equals("连接成功")) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 解析温度
	 * 
	 * @param msgJson
	 * @return 温度
	 * @throws JSONException
	 */
	public static int parserSocketTemper(String msgJson) throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		return jb.getJSONObject("Content").getInt("Temper");
	}

	/**
	 * 解析室内温度
	 * 
	 * @param msgJson
	 * @return 室内温度
	 * @throws JSONException
	 */
	public static int parserSocketInTemper(String msgJson) throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		return jb.getJSONObject("Content").getInt("InTemper");
	}

	/**
	 * 解析湿度
	 * 
	 * @param msgJson
	 * @return 湿度
	 * @throws JSONException
	 */
	public static int parserSocketHumidity(String msgJson) throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		return jb.getJSONObject("Content").getInt("Humidity");
	}

	/**
	 * 解析室内湿度
	 * 
	 * @param msgJson
	 * @return 室内湿度
	 * @throws JSONException
	 */
	public static int parserSocketInHumidity(String msgJson)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		return jb.getJSONObject("Content").getInt("InHumidity");
	}

	/**
	 * 解析存储
	 * 
	 * @param msgJson
	 * @return intArray[0] 实际存储 intArray[1] 总容量
	 * @throws JSONException
	 */
	public static int[] parserSocketStock(String msgJson) throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		int stockcount = jb.getJSONObject("Content").getInt("StockCount");
		int totalSize = jb.getJSONObject("Content").getInt("TotalStock");
		int[] stock = { stockcount, totalSize };
		return stock;
	}

	/**
	 * 解析灯源状态
	 * 
	 * @param msgJson
	 * @return StringArray[0] 灯源编号 String[1]灯源状态 1开0关
	 * @throws JSONException
	 */
	public static String[] parserSocketLamp(String msgJson)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		String id = jb.getJSONObject("Content").getString("LampID");
		int state = jb.getJSONObject("Content").getInt("Lamp");
		String[] lamp = { id, state + "" };
		return lamp;
	}

	/**
	 * 解析锁状态
	 * 
	 * @param msgJson
	 * @return 1 开，0关
	 * @throws JSONException
	 */
	public static int parserSocketLock(String msgJson) throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		return jb.getJSONObject("Content").getInt("Lock");
	}

	/**
	 * 解析设置密码结果
	 * 
	 * @param msgJson
	 * @return 1设置成功，0设置失败
	 * @throws JSONException
	 */
	public static int parserSocketSetPassWord(String msgJson)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		String state = jb.getJSONObject("Content").getString("message");
		if (!StringUtils.isNull(state) && state.equals("设置成功")) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 解析设备连接状态
	 * 
	 * @param msgJson
	 * @return 1在线，0不在线
	 * @throws JSONException
	 */
	public static int parserSocketDeviceState(String msgJson)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(msgJson);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		return jb.getJSONObject("Content").getInt("message");

	}

	/**
	 * 解析报表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserReportEntityList(String jsonStr)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		ParserResult result = new ParserResult();
		result.setCode(jb.getInt("StatusCode"));
		if (result.getCode() == 0) {
			if (jb.getJSONArray("ReportList") != null) {
				JSONArray entityArray = jb.getJSONArray("ReportList");
				List<ReportEntity> entityList = new ArrayList<ReportEntity>();
				for (int i = 0; i < entityArray.length(); i++) {
					JSONObject jbR = entityArray.getJSONObject(i);
					ReportEntity entity = new ReportEntity();
					entity.setName(jbR.getString("Name"));
					entity.setSaleCount(jbR.getInt("Count"));
					entity.setVolume(jbR.getString("Volume"));
					entityList.add(entity);
				}
				result.setObj(entityList);
			}
		}
		return result;
	}

	/**
	 * 解析意见反馈
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserFeedBakc(String jsonStr)
			throws JSONException {
		return parserStatus(jsonStr);
	}

	/**
	 * 解析验证结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserVerifyCode(String jsonStr)
			throws JSONException {
		return parserStatus(jsonStr);
	}

	/**
	 * 解析图片上传结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static ParserResult parserUploadImage(String jsonStr)
			throws JSONException {
		JSONTokener jsonParser = new JSONTokener(jsonStr);
		JSONObject jb = (JSONObject) jsonParser.nextValue();
		ParserResult result = new ParserResult();
		result.setCode(jb.getInt("StatusCode"));
		if (result.getCode() == 0) {
			result.setObj(jb.getString("webpath"));
		}
		return result;
	}
}
