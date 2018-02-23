package com.nv.baonk.common;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.nv.baonk.chat.service.ChatService;
import com.nv.baonk.chat.vo.ChatMessageSimpleVO;
import com.nv.baonk.chat.vo.ChatMessageVO;
import com.nv.baonk.login.service.UserService;
import com.nv.baonk.login.vo.User;
import com.nv.baonk.organ.vo.Department;
import com.nv.baonk.security.SecurityConfigBaonk;


@Component
public class CommonUtil {
	
	public static final String PT_BASIC = "basic";
	public static final String PT_STANDARD = "standard";
	public static final int BUFF_SIZE = 4096;
	
	@Autowired
    private SecurityConfigBaonk securityConfBaonk;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChatService chatSerivce;

	/* File separator setting*/
	public String separator            = "/";
	public final String CRLF           = "\r\n";
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	public User getUserInfo(String loginCookie) {
		try {
			String decData        = securityConfBaonk.decryptAES(loginCookie);
			String[] decDataArray = decData.split("\\+");
			String serverName     = decDataArray[0];
			String userID         = decDataArray[1];
			String userPassword   = decDataArray[2];
			int tenantId          = Integer.parseInt(decDataArray[3]);
			
			logger.debug("Server Name: " + serverName + " || User ID: " + userID + " || User Password: " + userPassword + " || Tenant ID: " + tenantId);
			
			User user = userService.findUserByUseridAndTenantid(userID, tenantId);
			return user;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getEncodedFileNameForDownload(String userAgentValue, String filename) {
		try {
			// in case of IE & Edge
			// the filename needs to be UTF-8 and URL-encoded.
			// URI class is more appropriate than URLEncoder class for this purpose.
			if (userAgentValue.contains("Trident") || userAgentValue.contains("Edge")) {
				
				filename = filename.replaceAll(":", "%3A");
				URI uri  = new URI(null, null, filename, null);
				filename = uri.toASCIIString();
				filename = filename.replaceAll("%253A", "%3A");
			}
			// in case of Chrome, Safari
			// the filename consists of UTF-8 encoded bytes.
			else {
				filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return filename;
	}
	
	public void downFile(HttpServletRequest request, HttpServletResponse response, String downFileName, String orgFileName) throws Exception {		
		orgFileName = getEncodedFileNameForDownload(request.getHeader("User-Agent"), orgFileName);		
		File file   = new File(downFileName);
		
		if (!file.exists()) {
			throw new FileNotFoundException(downFileName);
		}
		
		if (!file.isFile()) {
			throw new FileNotFoundException(downFileName);
		}
		
		int fSize = (int)file.length();
		
		if (fSize > 0) {
			BufferedInputStream in = null;
	
			try {
				in = new BufferedInputStream(new FileInputStream(file));
				String mimetype = "application/octet-stream";//"application/x-msdownload"
				
				response.setBufferSize(BUFF_SIZE);
				response.setContentType(mimetype);
				response.setHeader("Content-Disposition", "attachment; filename=\"" + orgFileName + "\"");
				response.setContentLength(fSize);
				
				FileCopyUtils.copy(in, response.getOutputStream());
			} 
			finally {
				if (in != null) {
					try {
						in.close();
					}
					catch (Exception ignore) {
						logger.debug("IGNORED: {}", ignore.getMessage());
					}
				}
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
	
	public String createExcelReportFile(List<User> listOfUsers, List<Department> listOfDepts, String realPath, String fileName) throws IOException {
		String fullPath          = realPath + "/file/uploadExcelFile/" + fileName;
		File file                = new File(realPath + "/file/uploadExcelFile");
		
		if (file == null || !file.exists()) {
			file.mkdir();
		}
		else {
			FileUtils.cleanDirectory(file); 
		}
		
		FileOutputStream fileOut = null;
		
		HSSFWorkbook  workbook   = new HSSFWorkbook();
		HSSFSheet sheet1         = workbook.createSheet("Employee Information");
		HSSFSheet sheet2         = workbook.createSheet("Department Information");
		
		//Process users' information
		Row rowhead1             = sheet1.createRow(0);
		
		rowhead1.createCell(0).setCellValue("User ID");
		rowhead1.createCell(1).setCellValue("User Name");
		rowhead1.createCell(2).setCellValue("Birthday");
		rowhead1.createCell(3).setCellValue("Email");
		rowhead1.createCell(4).setCellValue("Position");
		rowhead1.createCell(5).setCellValue("Other position");
		rowhead1.createCell(6).setCellValue("Telephone number");
		rowhead1.createCell(7).setCellValue("Homephone number");
		rowhead1.createCell(8).setCellValue("Nick Name");
		rowhead1.createCell(9).setCellValue("Sex");
		rowhead1.createCell(10).setCellValue("Address");
		rowhead1.createCell(11).setCellValue("Country");
		rowhead1.createCell(12).setCellValue("PostCode");
		rowhead1.createCell(13).setCellValue("Hobby");
		rowhead1.createCell(14).setCellValue("Department ID");
		rowhead1.createCell(15).setCellValue("Department Name");
		rowhead1.createCell(16).setCellValue("Company ID");
		rowhead1.createCell(17).setCellValue("Company Name");
		
		int i = 1;
		
		for (User user : listOfUsers) {
			Row newRow1 = sheet1.createRow(i++);
			
			newRow1.createCell(0).setCellValue(user.getUserid());
			newRow1.createCell(1).setCellValue(user.getUsername());
			newRow1.createCell(2).setCellValue(user.getBirthday());
			newRow1.createCell(3).setCellValue(user.getEmail());
			newRow1.createCell(4).setCellValue(user.getPosition());
			newRow1.createCell(5).setCellValue(user.getOtherpos());
			newRow1.createCell(6).setCellValue(user.getPhone());
			newRow1.createCell(7).setCellValue(user.getHomephone());
			newRow1.createCell(8).setCellValue(user.getNickname());
			newRow1.createCell(9).setCellValue(user.getSex());
			newRow1.createCell(10).setCellValue(user.getHomeaddress());
			newRow1.createCell(11).setCellValue(user.getCountry());
			newRow1.createCell(12).setCellValue(user.getPostcode());
			newRow1.createCell(13).setCellValue(user.getHobby());
			newRow1.createCell(14).setCellValue(user.getDepartmentid());
			newRow1.createCell(15).setCellValue(user.getDepartmentname());
			newRow1.createCell(16).setCellValue(user.getCompanyid());
			newRow1.createCell(17).setCellValue(user.getCompanyname());
		}
		
		//Process departments information
		Row rowhead2 = sheet2.createRow(0);
		
		rowhead2.createCell(0).setCellValue("Department ID");
		rowhead2.createCell(1).setCellValue("Department Name");
		rowhead2.createCell(2).setCellValue("Department Email");
		rowhead2.createCell(3).setCellValue("Company ID");
		rowhead2.createCell(4).setCellValue("Company Name");
		
		i = 1;
		
		for (Department dept : listOfDepts) {
			Row newRow2 = sheet2.createRow(i++);
			
			newRow2.createCell(0).setCellValue(dept.getDepartmentid());
			newRow2.createCell(1).setCellValue(dept.getDepartmentname());
			newRow2.createCell(2).setCellValue(dept.getEmail());
			newRow2.createCell(3).setCellValue(dept.getCompanyId());
			newRow2.createCell(4).setCellValue(dept.getCompanyName());
		}
		
		try {
			fileOut = new FileOutputStream(fullPath);
			workbook.write(fileOut);
			fileOut.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			fileOut.close();
		}
		
		return fullPath;
	}
	
	/*public LoginSimpleVO userInfoSimple(String loginCookie) {
		try{
			String decData        = egovFileScrty.decryptAES(loginCookie);			
			String[] decDataArray = decData.split("///");
			
			String serverName = decDataArray[0];
			String userID     = decDataArray[1];
			String locale     = decDataArray[5];
			String lang       = decDataArray[6];
			String timeZone   = decDataArray[7];
			
			String tenantIdStr = "0";
			
			if (decDataArray.length >= 9) {
				tenantIdStr = decDataArray[8];
			}
			
			LoginSimpleVO user = new LoginSimpleVO();
			user.setId(userID);
			user.setTenantId(Integer.parseInt(tenantIdStr));
			user.setLang(lang);
			user.setLocale(new Locale(locale));
			user.setOffset(timeZone);
			user.setServerName(serverName);
			
			return user;
		}catch(Exception e){
			return null;
		}
	}
	
	public LoginVO aprUserInfo(String loginCookie) {
		try{
			logger.debug("aprUserInfo started");
			LoginVO user                 = userInfo(loginCookie);
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request   = sra.getRequest();
			Cookie[] cookie              = request.getCookies();
			
			for (int k = 0; k < cookie.length; k++) {
				switch (cookie[k].getName()) {
				case "APRUI0":
					user.setDeptID(cookie[k].getValue());
					break;
				case "APRUI1":
					user.setDeptName1(URLDecoder.decode(cookie[k].getValue(), "utf-8"));
					break;
				case "APRUI2":
					user.setDeptName2(URLDecoder.decode(cookie[k].getValue(), "utf-8"));
					break;
				case "APRUI3":
					user.setCompanyID(URLDecoder.decode(cookie[k].getValue(), "utf-8"));
					break;
				case "APRUI4":
					user.setCompanyName2(URLDecoder.decode(cookie[k].getValue(), "utf-8"));
					break;
				case "APRUI5":
					user.setTitle(URLDecoder.decode(cookie[k].getValue(), "utf-8"));
					break;
				case "APRUI6":
					user.setTitle2(URLDecoder.decode(cookie[k].getValue(), "utf-8"));
					break;
				case "APRUI7":
					user.setCompanyID(cookie[k].getValue());
					break;
				}
			}
			
			if (user.getPrimary().equals("1")) {
				user.setTitle(user.getTitle());
				user.setDeptName(user.getDeptName1());
				user.setDisplayName(user.getDisplayName());
				user.setCompanyID(user.getCompanyID());
			} else {
				user.setTitle(user.getTitle2());
				user.setDeptName(user.getDeptName2());
				user.setDisplayName(user.getDisplayName2());
				user.setCompanyID(user.getCompanyName2());
			}
			
			logger.debug("aprUserInfo ended");
			
			return user;
		}catch(Exception e){
			return null;
		}
	}
	
	public LoginVO checkAdmin(String loginCookie){
		try{
			LoginVO user = userInfo(loginCookie);
			
			if (user.getRollInfo().indexOf("c=1") == -1 && user.getRollInfo().indexOf("k=1") == -1){
				return null;
			}else{
				return user;
			}
		}catch(Exception e){
			return null;
		}
	}
	
	public LoginVO aprCheckAdmin(String loginCookie){
		try{
			LoginVO user = aprUserInfo(loginCookie);
	
			if (user.getRollInfo().indexOf("c=1") == -1 && user.getRollInfo().indexOf("k=1") == -1){
				return null;
			}else{
				return user;
			}
		}catch(Exception e){
			return null;
		}
	}
	public List<String> getUserIdAndPassword(String loginCookie) {
		try{
			String decData            = egovFileScrty.decryptAES(loginCookie);
			List<String> returnObject = new ArrayList<String>();
			String userId             = decData.split("///")[1];
			String pass               = decData.split("///")[4];
			returnObject.add(userId);
			returnObject.add(pass);
			return returnObject;
		}catch(Exception e){
			return null;
		}
	}
	
	public static void addXUACompatibleHeaderToResponse(HttpServletRequest request, HttpServletResponse response) {
		String browser         = ClientUtil.getClientInfo(request, "browser");
		String compatibleValue = null;
		
		if (browser.equals("Edge") || browser.equals("IE11")) {
			compatibleValue = "IE=edge";
		} else if (browser.equals("IE10")) {
			compatibleValue = "IE=10";
		} else if (browser.equals("IE9")) {
			compatibleValue = "IE=9";
		} else if (browser.equals("IE8")) {
			compatibleValue = "IE=8";
		}
		
		if (compatibleValue != null) {
			response.setHeader("X-UA-Compatible", compatibleValue);
		}
	}
	
	public boolean isLoginCookieExists(HttpServletRequest request, HttpServletResponse response) {
		boolean isCookie = false;
		Cookie[] cookies = request.getCookies();
		
		/HttpSession session = request.getSession(false);
        
        //if (session != null) {
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if("loginCookie".equals(cookie.getName())){
	                    //접속한 클라이언트 IP
	                    String ip = ClientUtil.getClientIP(request);
	                    String cValue = "";
	                    try {
	                        //쿠기에 저장되어 있는 IP
	                        cValue = egovFileScrty.decryptAES(cookie.getValue());
	
	                        if(cValue.split("///")[3].equals(ip)){                  
	                            isCookie = true;
	                        }
	                    } catch (Exception e) {
	                        //e.printStackTrace();
	                    }
	                }
	            }
	        }
        } else {
        	if (cookies != null) {
        		for (Cookie cookie : cookies) {
        			if(!cookie.getName().equals("saveid") && !cookie.getName().matches("POPUP_.*")){
        				cookie.setMaxAge(0);
        				cookie.setPath("/");
        				response.addCookie(cookie);
        			}
        	    }
        	}
        }        
        return isCookie;
	}
	
	public Document convertStringToDocument(String xmlStr) {
		String replaceData = xmlStr.trim().replaceFirst("^([\\W]+)<","<");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;
        Document doc = null;
        
        try {  
            builder = factory.newDocumentBuilder();  
            doc = builder.parse(new InputSource(new StringReader(replaceData)));
        } catch (Exception e) {}
        
        return doc;
	}
	
	public Document convertRequestToDocument(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();		
        String readData = "";
        BufferedReader br;
        Document doc = null;
        
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			
			while ((readData = br.readLine()) != null ) {
	            sb.append(readData);
	        }
			doc = convertStringToDocument(sb.toString());
			
		} catch(Exception e){}
		
		return doc;		
	}
	
	public String convertDocumentToString(Document doc){
		try{
			TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    StringWriter writer = new StringWriter();
		    transformer.transform(new DOMSource(doc), new StreamResult(writer));
		    String output = writer.getBuffer().toString();	    
			
			return output;
		}catch(Exception e){
			return null;
		}
	}

	public String getQueryResult(Object vo) throws Exception {
		StringBuilder stb = new StringBuilder();		
		
		if (vo != null) {
			stb.append("<ROW>");
			
			for (Field field : vo.getClass().getDeclaredFields()) {
		        field.setAccessible(true);
				String data = String.valueOf(field.get(vo));
	
				if (data == null || data.equals(null) || data.equals("null")) {
					data = "";
				}		
				
		        stb.append("<" + field.getName().toUpperCase() + ">");
		        stb.append(cleanValue(data));
		        stb.append("</" + field.getName().toUpperCase() + ">");		        
		    }
			
			stb.append("</ROW>");
		} else {
			stb.append("");
		}

		return stb.toString();
	}
	 
	public String getQueryResult(List<Object> vo, String xmlTag) throws Exception{
		StringBuilder stb = new StringBuilder();		
		
		if (vo == null) {
			stb.append("");
			return stb.toString();
		}
		
	    stb.append("<DATA>");
	    
	    for (int i = 0; i < vo.size(); i++) {
			stb.append("<ROW>");
			
			for(Field field : vo.get(i).getClass().getDeclaredFields()){
		        field.setAccessible(true);
				String data = String.valueOf(field.get(vo.get(i)));
	
				if(data == null || data.equals(null) || data.equals("null")){
					data = "";
				}				
		        stb.append("<" + field.getName().toUpperCase() + ">");
		        stb.append(cleanValue(data));
		        stb.append("</" + field.getName().toUpperCase() + ">");		        
		    }
			stb.append("</ROW>");
		}
		stb.append("</DATA>");
		
		return stb.toString();
	}
	

	public String getMultiData(String lang, int tenantID) throws Exception{
		if (!lang.equals(commonService.getTenantConfig("PrimaryLang", tenantID))) {
			return "2";
		} else {
			return "";
		}
	}

	public String getPrimaryData(String lang, int tenantID) throws Exception {
		if (lang.equals(commonService.getTenantConfig("PrimaryLang", tenantID))) {
			return "1";
		} else {
			return "2";
		}
	}

	
	public String getLangData(String lang){
		if (lang.equals("1")) {
			return "";
		} else {
			return lang;
		}
	}	
	
	public String cleanValue(String pOrgString) {
		String value = ""; 
				
		if (pOrgString != null) {
			value = pOrgString.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
	        value = value.replaceAll("'", "&#39;");
	        value = value.replaceAll("\"", "&quot;");
	        value = value.replaceAll("eval\\((.*)\\)", "");
	        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");        
	        //value = value.replaceAll("script", "");
		}

		return value;
	}
	
	// 2016.09.06 by kgs: Property value의 값을 변환
	public String cleanPropertyValue(String pOrgString) {
		String value = ""; 
				
		if(pOrgString != null){
			value = pOrgString.replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
		}

		return value;
	}
	
	public String trimDoubleQuotes(String src) {
		if (src.startsWith("\"") && src.endsWith("\"")) {
			src = src.substring(1, src.length() - 1);
		}		
		
		return src;
	}
	
	public boolean checkIE(HttpServletRequest request) {
		if (request.getHeader("User-Agent").indexOf("rv:11") > 0 || request.getHeader("User-Agent").indexOf("Trident/7.0") > 0) {
			return true;
		} else if ( request.getHeader("User-Agent").indexOf("Chrome") > 0) {
			return false;
		} else {
			return false;
		}
	}
	
	public String isoUTFDate(String dateTimeStr) throws Exception {
        String resultStr = "";

        if (dateTimeStr != null && !dateTimeStr.trim().equals("")){
            if (dateTimeStr.indexOf(" ") != -1){
                resultStr = dateTimeStr.split(" ")[0] + "T" + dateTimeStr.split(" ")[1] + ".000Z";
            } else{
                resultStr = dateTimeStr + "T00:00:00.000Z";
            }
        } else{
            resultStr = "";
        }
        
        return resultStr;
    }
	
	public String getRealPath(HttpServletRequest request) {
		String realPath = request.getServletContext().getRealPath("");
		
		if (realPath.substring(realPath.length() - 1).equals(separator)) {
			realPath = realPath.substring(0, realPath.length() - 1);
		} else if (realPath.substring(realPath.length() - 1).equals("\\")) {
			realPath = realPath.substring(0, realPath.length() - 1);
		}
		
		return realPath;
	}
	
	public String getUploadPath(String property, int tenantId) {
		return separator + "fileroot" + separator + tenantId + config.getProperty(property);
	}

	public String getDateStringInUTC(String dateStr, String offset, boolean timeZoneToUTC) {
//		logger.debug("dateStr=" + dateStr + ", offset=" + offset + ", timeZoneToUTC=" + timeZoneToUTC);
		
		if (dateStr == null) {
			logger.error("dateStr is null.");
			return null;
		}
		
		if (offset == null || offset.indexOf("|") == -1) {
			logger.error("offset is null or offset format is wrong.");
			return dateStr;
		}
		
		String pattern = "";
		if (dateStr.length() == 8) {
			pattern = "yyyyMMdd";
		} else if (dateStr.length() == 10) {
			if (dateStr.indexOf("/") > -1) {
				pattern = "yyyy/MM/dd";
			} else {
				pattern = "yyyy-MM-dd";
			}
		} else if (dateStr.length() == 16) {
			if (dateStr.indexOf("/") > -1) {
				pattern = "yyyy/MM/dd HH:mm";
			} else {
				pattern = "yyyy-MM-dd HH:mm";
			}
		} else if (dateStr.length() == 21) {
			if (dateStr.indexOf("/") > -1) {
				pattern = "yyyy/MM/dd aa h:mm:ss";
			} else {
				pattern = "yyyy-MM-dd aa h:mm:ss";
			}
		} else {
			if (dateStr.indexOf("/") > -1) {
				pattern = "yyyy/MM/dd HH:mm:ss";
			} else {
				pattern = "yyyy-MM-dd HH:mm:ss";
			}
		}
//		logger.debug("pattern=" + pattern);
		
		String[] offsetArr = offset.split("\\|");
		
		SimpleDateFormat userFormat = new SimpleDateFormat(pattern);
		userFormat.setTimeZone(TimeZone.getTimeZone("GMT" + offsetArr[1]));
		
		SimpleDateFormat utcFormat = new SimpleDateFormat(pattern);
		utcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		String resultDateStr = "";
		try {
			if (timeZoneToUTC) {
				resultDateStr = utcFormat.format(userFormat.parse(dateStr));
			} else {
				resultDateStr = userFormat.format(utcFormat.parse(dateStr));
			}
		} catch (ParseException e) {
			logger.error("Check the dateStr format.");
			return dateStr;
		}
		
//		logger.debug("resultDateStr=" + resultDateStr);
		return resultDateStr;
	}

	public String getTodayUTCTime(String format) throws Exception {
		logger.debug("getTodayUTCTime started");
		
		ZoneId utc = ZoneId.of("UTC");
		ZonedDateTime getTime = ZonedDateTime.of(LocalDateTime.now(utc), utc);
		
		DateTimeFormatter formatter = null;
		
		if (format == null || format.equals("")) {
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		} else {
			try {
				formatter = DateTimeFormatter.ofPattern(format);
			} catch (Exception e) {
				logger.error("formatter error :: " + e.getMessage());
				formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			}
		}
		
		String today = getTime.format(formatter);
		
		logger.debug("getTodayUTCTime ended");
		
		return today;
	}

	public String getMinuteUTC(String offSet) throws Exception {
		logger.debug("getMinuteUTC started");
		
		String format = offSet.split("\\|")[1];
		String cal = format.substring(0,1);
		int min = Integer.parseInt(format.substring(1,3)) * 60 + Integer.parseInt(format.substring(4,6));
		String time = cal + min;	
		
		return time;
	}
	
	public String makeDate (String year, String month, String day, boolean startFlag) {
		String result = "";
		
		if (month.length() == 1) {
			month = "0" + month;
		}
		
		if (!year.equals("") && !month.equals("") && !day.equals("")) {
			result = year + "-" + month + "-" + day;
			
			if (startFlag) {
				result += " 00:00:00";
			} else {
				result += " 23:59:59";
			}
		}
		return result;
	}
	
	public String getTwoLetterLangFromLangNum(String langNum) {
		String returnValue = "";
		
		if (langNum == null) {
			logger.error("langNum is null.");
			return null;
		}
		
		if (langNum.equals("1")) {
			returnValue = "ko";
		} else if (langNum.equals("2")) {
			returnValue = "en";
		} else if (langNum.equals("3")) {
			returnValue = "ja";
		} else if (langNum.equals("4")) {
			returnValue = "zh";
		} else {
			logger.error("Invalid langNum.");
		}
		
		return returnValue;
	}
	
	public String getLangNumFromTwoLetterLang(String twoLetterLang) {
		String returnValue = "";
		
		if (twoLetterLang == null) {
			logger.error("twoLetterLang is null.");
			return null;
		}
		
		if (twoLetterLang.equalsIgnoreCase("ko")) {
			returnValue = "1";
		} else if (twoLetterLang.equalsIgnoreCase("en")) {
			returnValue = "2";
		} else if (twoLetterLang.equalsIgnoreCase("ja")) {
			returnValue = "3";
		} else if (twoLetterLang.equalsIgnoreCase("zh")) {
			returnValue = "4";
		} else {
			logger.error("Invalid twoLetterLang.");
		}
		
		return returnValue;
	}
	
	public String makeListField(String orgStr) {
		if (orgStr == null || orgStr.equals("NULL") || orgStr.equals("null")) {
			return "";
		} else {
			return orgStr;
		}
	}
	
	public String byteCalculation(String bytes) {
        String retFormat = "0";
        Double size = Double.parseDouble(bytes);

        String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };       

        if (!bytes.equals("0")) {
              int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
              DecimalFormat df = new DecimalFormat("#,###.##");
              double ret = ((size / Math.pow(1024, Math.floor(idx))));
              retFormat = df.format(ret) + " " + s[idx];
         } else {
              retFormat += " " + s[0];
         }

         return retFormat;
	}

	public String getPackageType(int tenantId) throws Exception {
		String packageType = "standard";
		
		String licenseKey = ezCommonService.getTenantConfig("LicenseKey", tenantId);
		
		logger.debug("licenseKey=" + licenseKey);
		
		if (!licenseKey.equals("")) {
			try {
				// 라이센스키를 복호화한다.
				licenseKey = egovFileScrty.decryptAES(licenseKey);
				
				logger.debug("Decrypted licenseKey=" + licenseKey);
				
				String items[] = licenseKey.split(":");

				if (items.length >= 3) {
					packageType = items[2];					
				}
			} catch (Exception e) {
				logger.debug("License Key Decryption failed.");
			}			
		}
		
		logger.debug("packageType=" + packageType);
		
		return packageType;
	}	

    public void resetLoginFailAttempts(String userID, int tenantID) throws Exception{
    	String userLoginFailedAttempt = commonService.getUserConfigInfo(tenantID, userID, "LoginFailCount"); 
    	
		if (userLoginFailedAttempt.equals("")) {
			//User hasn't logged in fail yet
			return;
		} else {
			//Reset the number to 0
			commonService.updateUserConfigInfo(tenantID, userID, "LoginFailCount", "0");
		}
    }*/
	
	public void writeUploadedFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
		InputStream stream       = null;
		OutputStream bos         = null;
		String stordFilePathReal = (stordFilePath == null ? "" : stordFilePath);
		int BUFF_SIZE            = 4096;
		
		try {
			stream      = file.getInputStream();
			File cFile  = new File(stordFilePathReal);
	
			if (!cFile.isDirectory()) {
				boolean _flag = cFile.mkdirs();
				if (!_flag) {
					throw new IOException("Directory creation Failed ");
				}
			}
	
			bos           = new FileOutputStream(stordFilePathReal + File.separator + newName);
			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];
	
			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} 
		catch (FileNotFoundException fnfe) {
			logger.debug("fnfe: {}", fnfe);
		} 
		catch (IOException ioe) {
			logger.debug("ioe: {}", ioe);
		} 
		catch (Exception e) {
			logger.debug("e: {}", e);
		} 
		finally {
			if (bos != null) {
				try {
					bos.close();
				} 
				catch (Exception ignore) {
					logger.debug("IGNORED: {}", ignore.getMessage());
				}
			}
			if (stream != null) {
				try {
					stream.close();
				}
				catch (Exception ignore) {
					logger.debug("IGNORED: {}", ignore.getMessage());
				}
			}
		}
	}

	public ChatMessageVO getChatMessage(ChatMessageSimpleVO message) throws Exception {
		int tenantId               = message.getTenantId();
		User sender                = userService.findUserByUseridAndTenantid(message.getSender(), tenantId);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date                  = new Date();
		String time                = formatter.format(date);
		ChatMessageVO messageVO    = new ChatMessageVO();
		
		messageVO.setClusterId(message.getClusterId());
		messageVO.setContent(message.getContent());
		messageVO.setReceiverId(message.getReceiver());
		messageVO.setFileName(message.getFileName());
		messageVO.setFilePath(message.getFilePath());
		messageVO.setSenderId(message.getSender());
		messageVO.setTenantId(tenantId);
		messageVO.setUserImage(sender.getImage());
		messageVO.setSenderName(sender.getUsername());
		messageVO.setMessageId(getMaxMessageId(tenantId));
		messageVO.setCreatedTime(time);
		
		if (message.getContentType().equals(ChatMessageSimpleVO.ContentType.TEXT)) {
			messageVO.setContType(1);
		}
		else if (message.getContentType().equals(ChatMessageSimpleVO.ContentType.STICKER)) {
			messageVO.setContType(2);
		}
		else if (message.getContentType().equals(ChatMessageSimpleVO.ContentType.IMAGE)) {
			messageVO.setContType(3);
		}
		else if (message.getContentType().equals(ChatMessageSimpleVO.ContentType.FILE)) {
			messageVO.setContType(4);
		}
		
		if (message.getReceiverType().equals(ChatMessageSimpleVO.ReceiverType.SINGLE)) {
			messageVO.setReceiverType(1);
		}
		else if (message.getReceiverType().equals(ChatMessageSimpleVO.ReceiverType.GROUP)) {
			messageVO.setReceiverType(2);
		}
		
		if (message.getType().equals(ChatMessageSimpleVO.MessageType.CHAT)) {
			messageVO.setMessageType(1);
		}
		else if (message.getType().equals(ChatMessageSimpleVO.MessageType.JOIN)) {
			messageVO.setMessageType(2);
		}
		else if (message.getType().equals(ChatMessageSimpleVO.MessageType.LEAVE)) {
			messageVO.setMessageType(3);
		}
		
		return messageVO;
	}

	private String getMaxMessageId(int tenantId) throws Exception {
		int currentMaxFileId = -1;
		String result        = chatSerivce.getMaxMessageId(tenantId);
		currentMaxFileId     = result.equals("")        ? 1 : Integer.parseInt(result);
		currentMaxFileId     = (currentMaxFileId == -1) ? 1 : (currentMaxFileId + 1);
		return Integer.toString(currentMaxFileId);
	}
}