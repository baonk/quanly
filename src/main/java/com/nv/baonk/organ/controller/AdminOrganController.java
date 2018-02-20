package com.nv.baonk.organ.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nv.baonk.common.CommonUtil;
import com.nv.baonk.login.service.UserService;
import com.nv.baonk.login.vo.Role;
import com.nv.baonk.login.vo.User;
import com.nv.baonk.organ.service.DepartmentService;
import com.nv.baonk.organ.vo.Department;
import com.nv.baonk.organ.vo.SimpleDepartment;
import com.nv.baonk.vo.ResponseObject;
import com.nv.baonk.vo.ValidateResponseObject;

@Controller
public class AdminOrganController {
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private DepartmentService deptService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder BCryptPass;
	
	private final Logger logger = LoggerFactory.getLogger(AdminOrganController.class);

	@RequestMapping(value="/admin/organ/organRight", method = RequestMethod.GET)
	public String mainMenu(@CookieValue("loginCookie")String loginCookie, Model model, HttpServletRequest request) throws JsonProcessingException {
		ObjectMapper om    = new ObjectMapper();
		int checkAdmin     = 0;
		User user          = commonUtil.getUserInfo(loginCookie);
		Set<Role> roleList = user.getRoles();
		int tenantId       = user.getTenantid();
		String userDeptId  = user.getDepartmentid();
		String userCompId  = user.getCompanyid();
		Department dept    = deptService.findByDepartmentidAndTenantid(userDeptId, tenantId);
		String[] deptPath  = dept.getDepartmentpath().split("::");
		
		//Check user role
		for (Role role: roleList) {
			if (role.getRoleid() == 1) {
				checkAdmin = 1;
				break;
			}
		}
		
		if (checkAdmin == 0) {
			return "access-denied";
		}
		
		//Get all company		
		List<SimpleDepartment> simpleCompanyList = deptService.getAllSimpleSubDepts("self", tenantId);
		
		for (SimpleDepartment company: simpleCompanyList) {
			if (company.getCompanyid().equals(userCompId)) {
				getAllSubDepts2(company, tenantId, deptPath, 1);
			}
			else {
				getAllSubDepts(company, tenantId, 1);
			}
		}
		
		model.addAttribute("listDepartment", om.writeValueAsString(simpleCompanyList));
		model.addAttribute("userdeptID", userDeptId);
		model.addAttribute("usercompID", userCompId);
		
		return "/admin/organ/organRight";
	}

	@RequestMapping(value="/admin/organ/getSimpleSubDept", method = RequestMethod.POST)
	@ResponseBody
	public String getSimpleSubDept(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request) throws JsonProcessingException {
		logger.debug("======================getSimpleSubDept start======================");
		
		ObjectMapper om       = new ObjectMapper();
		User user             = commonUtil.getUserInfo(loginCookie);
		int tenantId          = user.getTenantid();
		String deptID         = request.getParameter("deptID");
		SimpleDepartment dept = deptService.getSimpleDeptList(deptID, tenantId);
		
		getAllSubDepts(dept, tenantId, 1);
		
		logger.debug("======================getSimpleSubDept end======================");
		return om.writeValueAsString(dept);
	}	

	@RequestMapping(value="/admin/organ/getDetailInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getDetailInfo(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request) throws JsonProcessingException {
		logger.debug("======================getDetailInfo start======================");
		ObjectMapper om     = new ObjectMapper();
		User user           = commonUtil.getUserInfo(loginCookie);
		int tenantId        = user.getTenantid();
		String deptID       = request.getParameter("deptID");
		String mode         = request.getParameter("optionVal");
		ResponseObject fail = new ResponseObject("Error");
		
		if (mode.equals("muser")) {
			List<User> listUser = userService.findUsersInAdminMode(deptID, tenantId);
			
			if (listUser.isEmpty()) {
				logger.debug("======================getDetailInfo end======================");
				return om.writeValueAsString(fail);
			}
			
			logger.debug("======================getDetailInfo end======================");
			return om.writeValueAsString(listUser);
		}
		else if (mode.equals("mdept")) {
			List<SimpleDepartment> listSimpleDept = deptService.getAllSimpleSubDepts(deptID, tenantId);
			if (listSimpleDept.isEmpty()) {
				logger.debug("======================getDetailInfo end======================");
				return om.writeValueAsString(fail);
			}
			
			logger.debug("======================getDetailInfo end======================");
			return om.writeValueAsString(listSimpleDept);
		}
		else {
			Department dept = deptService.findByDepartmentidAndTenantid(deptID, tenantId);
			
			if (dept.getParentdept().equals("self")) {
				SimpleDepartment company = deptService.getSimpleDeptList(deptID, tenantId);
				logger.debug("======================getDetailInfo end======================");
				return om.writeValueAsString(company);
			}
			else {
				logger.debug("======================getDetailInfo end======================");
				return om.writeValueAsString(fail);
			}
		}
	}

	@RequestMapping(value="/admin/organ/getSearchInfo", method = RequestMethod.POST)
	@ResponseBody
	public String searchDetailInfo(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request) throws JsonProcessingException {
		logger.debug("======================searchDetailInfo start======================");
		ObjectMapper om     = new ObjectMapper();
		User user           = commonUtil.getUserInfo(loginCookie);
		int tenantId        = user.getTenantid();
		String deptID       = request.getParameter("deptID");
		String mode         = request.getParameter("optionVal");
		String sStr         = request.getParameter("searchStr");
		String field        = request.getParameter("selectValue");
		ResponseObject fail = new ResponseObject("Error");
		
		if (mode.equals("muser")) {
			List<User> listUser = userService.findUsersWithSearchOption(deptID, sStr, field, tenantId);
			
			if (listUser.isEmpty()) {
				logger.debug("======================searchDetailInfo end======================");
				return om.writeValueAsString(fail);
			}
			
			logger.debug("======================searchDetailInfo end======================");
			return om.writeValueAsString(listUser);
		}
		else if (mode.equals("mdept")) {
			List<Department> listSimpleDept = deptService.findDeptsWithSearchOption(deptID, sStr, field, tenantId);
			if (listSimpleDept.isEmpty()) {
				logger.debug("======================searchDetailInfo end======================");
				return om.writeValueAsString(fail);
			}
			
			logger.debug("======================searchDetailInfo end======================");
			return om.writeValueAsString(listSimpleDept);
		}
		else {
			List<Department> listCompany = deptService.findCompanyWithSearchOption(sStr, field, tenantId);
			
			if (listCompany != null && !listCompany.isEmpty()) {
				logger.debug("======================searchDetailInfo end======================");
				return om.writeValueAsString(listCompany);
			}
			else {
				logger.debug("======================searchDetailInfo end======================");
				return om.writeValueAsString(fail);
			}
		}
	}

	@RequestMapping(value="/admin/organ/getDeptSearchInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getDeptSearchInfo(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request) throws JsonProcessingException {
		logger.debug("======================getDeptSearchInfo start======================");
		ObjectMapper om     = new ObjectMapper();
		User user           = commonUtil.getUserInfo(loginCookie);
		int tenantId        = user.getTenantid();
		String sStr         = request.getParameter("searchStr");
		ResponseObject fail = new ResponseObject("Error");
		
		List<Department> listSimpleDept = deptService.getDeptsByDeptNameSearch(sStr, tenantId);
		if (listSimpleDept.isEmpty()) {
			logger.debug("======================getDeptSearchInfo end======================");
			return om.writeValueAsString(fail);
		}
		
		fail.setResult(sStr);
		logger.debug("======================getDeptSearchInfo end======================");
		return om.writeValueAsString(fail);
	}

	@RequestMapping(value="/admin/organ/searchingDept")
	public String getSearchingDeptsInfo(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, Model model) throws JsonProcessingException {
		User user                  = commonUtil.getUserInfo(loginCookie);
		int tenantId               = user.getTenantid();
		String sStr                = request.getParameter("sStr");
		List<Department> listDepts = deptService.getDeptsByDeptNameSearch(sStr, tenantId);
		model.addAttribute("deptList", listDepts);
	
		return "/admin/organ/selectDept";
	}

	public void getAllSubDepts(SimpleDepartment dept, int tenantId, int mode) {
		List<SimpleDepartment> listSubSimpleDepts = deptService.getAllSimpleSubDepts(dept.getDepartmentid(), tenantId);
		
		if (listSubSimpleDepts.size() > 0) {
			dept.setSubDept(listSubSimpleDepts);
			dept.setHasSubDept(1);
			
			for (SimpleDepartment subdept: listSubSimpleDepts) {
				if (mode == 0) {
					getAllSubDepts(subdept, tenantId, mode);
				}
				else {
					List<SimpleDepartment> subSimpleDepts = deptService.getAllSimpleSubDepts(subdept.getDepartmentid(), tenantId);
					if (subSimpleDepts.size() > 0) {
						subdept.setHasSubDept(1);
					}
					else {
						subdept.setHasSubDept(0);
					}
				}
			}
		}
		else {
			dept.setHasSubDept(0);
		}
		
	}

	public void getAllSubDepts2(SimpleDepartment dept, int tenantId, String[] deptPath, int order) {
		List<SimpleDepartment> listSubSimpleDepts = deptService.getAllSimpleSubDepts(dept.getDepartmentid(), tenantId);
		
		if (listSubSimpleDepts.size() > 0) {
			dept.setSubDept(listSubSimpleDepts);
			dept.setHasSubDept(1);
			
			for (SimpleDepartment subdept: listSubSimpleDepts) {
				List<SimpleDepartment> subSimpleDepts = deptService.getAllSimpleSubDepts(subdept.getDepartmentid(), tenantId);
				if (subSimpleDepts.size() > 0) {
					subdept.setHasSubDept(1);
					
					if (order < deptPath.length && subdept.getDepartmentid().equals(deptPath[order])) {
						getAllSubDepts2(subdept, tenantId, deptPath, order + 1);
					}
				}
				else {
					subdept.setHasSubDept(0);
				}
			}
		}
		else {
			dept.setHasSubDept(0);
		}
	}

	@RequestMapping(value="/admin/organization", method = RequestMethod.GET)
	public String adminOrganization(Model model, HttpServletRequest request) {
		return "admin/organ/organMainBoard";
	}

	@RequestMapping(value="/admin/organLeft", method = RequestMethod.GET)
	public String adminOrganLeft(Model model, HttpServletRequest request) {
		logger.debug("admin organization left board is running!");
		return "admin/organ/organLeft";
	}

	@RequestMapping(value="/admin/userRegistration", method = RequestMethod.GET)
	public String registration(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, Model model){
		logger.debug("----------------------Add user is running-----------------------!");
		User loginUser  = commonUtil.getUserInfo(loginCookie);
		int tenantId    = loginUser.getTenantid();
		String deptId   = request.getParameter("deptId")   != null ? request.getParameter("deptId")   : "";
		String deptName = request.getParameter("deptName") != null ? request.getParameter("deptName") : "";
		String userId   = request.getParameter("userId")   != null ? request.getParameter("userId")   : "";
		
		if (!deptId.equals("")) {
			User user = new User();
			model.addAttribute("user", user);
			model.addAttribute("deptID", deptId);
			model.addAttribute("deptName", deptName);
			model.addAttribute("mode", "add");
		}
		else {
			User vUser = userService.findUserByUseridAndTenantid(userId, tenantId);
			model.addAttribute("user", vUser);
			model.addAttribute("mode", "view");
		}
		
		logger.debug("-----------------------Add user end-----------------------------!");
		return "admin/organ/addUser";
	}

	@RequestMapping(value="/admin/moveUser", method = RequestMethod.GET)
	public String moveUser(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, Model model) throws JsonProcessingException{
		logger.debug("----------------------Move user is running-----------------------!");
		ObjectMapper om       = new ObjectMapper();
		User loginUser        = commonUtil.getUserInfo(loginCookie);
		int tenantId          = loginUser.getTenantid();
		String userId         = request.getParameter("userId") != null ? request.getParameter("userId") : "";
		User clickedUser      = userService.findUserByUseridAndTenantid(userId, tenantId);
		SimpleDepartment dept = deptService.getSimpleDeptList(clickedUser.getCompanyid(), tenantId);
		
		getAllSubDepts(dept, tenantId, 1);
		model.addAttribute("listDepartment", om.writeValueAsString(dept));
		model.addAttribute("usercompID", clickedUser.getCompanyid());
		model.addAttribute("userID", userId);
		
		logger.debug("-----------------------Move user end-----------------------------!");
		return "admin/organ/moveUser";
	}

	@RequestMapping(value="/admin/saveMovedUser", method = RequestMethod.POST)
	public void saveMovedUser(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		logger.debug("----------------------Save moved user is running-----------------------!");
		
		User loginUser     = commonUtil.getUserInfo(loginCookie);
		int tenantId       = loginUser.getTenantid();
		String userId      = request.getParameter("userId")     != null ? request.getParameter("userId")      : "";
		String newDeptId   = request.getParameter("newDeptId")  != null ? request.getParameter("newDeptId")   : "";
		User movedUser     = userService.findUserByUseridAndTenantid(userId, tenantId);
		Department newDept = deptService.findByDepartmentidAndTenantid(newDeptId, tenantId);
		
		movedUser.setDepartmentid(newDept.getDepartmentid());
		movedUser.setDepartmentname(newDept.getDepartmentname());
		userService.updateUser(movedUser);
		
		logger.debug("-----------------------Save moved User end-----------------------------!");
	}

	@RequestMapping(value="/admin/changeUserPasswd", method = RequestMethod.GET)
	public String changeUserPasswd(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, Model model) throws JsonProcessingException {
		logger.debug("----------------------Change user passwd is running-----------------------!");
		String userId = request.getParameter("userId") != null ? request.getParameter("userId") : "";
		model.addAttribute("userID", userId);
		
		logger.debug("-----------------------Change user passwd end-----------------------------!");
		return "admin/organ/changeUserPasswd";
	}	

	@RequestMapping(value="/admin/saveUserNewPassword", method = RequestMethod.POST)
	@ResponseBody
	public String saveNewPassword(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, Model model) throws JsonProcessingException {
		logger.debug("----------------------Save new user's passwd is running-----------------------!");
		
		ResponseObject rObj     = new ResponseObject();
		ObjectMapper om         = new ObjectMapper();
		User currentUser        = commonUtil.getUserInfo(loginCookie);
		String userId           = request.getParameter("userId") != null ? request.getParameter("userId") : "";
		String newPasswd        = request.getParameter("newPasswd");
		
		if (newPasswd.length() < 5) {
			rObj.setResult("Password must have at least 5 characters!");
			return om.writeValueAsString(rObj);
		}
		
		User changedPassWdUser  = userService.findUserByUseridAndTenantid(userId, currentUser.getTenantid());
		String encodedPassword  = BCryptPass.encode(newPasswd);
		changedPassWdUser.setPassword(encodedPassword);
		
		try {
			userService.updateUser(changedPassWdUser);
			rObj.setResult("OK");
		}
		catch (Exception e) {
			e.printStackTrace();
			rObj.setResult("FAIL");
		}
		
		logger.debug("-----------------------Save new user's passwd end-----------------------------!");
		
		return om.writeValueAsString(rObj);
	}	

	@RequestMapping(value="/admin/exportExcelFile", method = RequestMethod.GET)
	public void exportExcelFile(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("----------------------Export excel file is running-----------------------!");
		User currentUser              = commonUtil.getUserInfo(loginCookie);		
		String companyId              = request.getParameter("companyId") != null ? request.getParameter("companyId") : "";
		List<User> listOfUsers        = userService.findAllCompanyEmployees(companyId, currentUser.getTenantid());
		List<Department> listOfDepts  = deptService.getAllDepartmentsOfCompany(companyId, currentUser.getTenantid());
		
		Collections.sort(listOfUsers, Comparator.comparing(User::getDepartmentid));
		Collections.sort(listOfDepts, Comparator.comparing(Department::getDepartmentid));
		
		String realPath = request.getServletContext().getRealPath("");
		String fileName = "exportInformation.xls";
		String fullPath = commonUtil.createExcelReportFile(listOfUsers, listOfDepts, realPath, fileName);
		
		commonUtil.downFile(request, response, fullPath, fileName);
		
		logger.debug("-----------------------Export excel file end-----------------------------!");
		
	}

	@RequestMapping(value="/admin/deleteUser", method = RequestMethod.POST)
	public void deleteUser(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		logger.debug("----------------------Delete user is running-----------------------!");
		
		User loginUser  = commonUtil.getUserInfo(loginCookie);
		int tenantId    = loginUser.getTenantid();
		String userId   = request.getParameter("userId") != null ? request.getParameter("userId") : "";
		User deleteUser = userService.findUserByUseridAndTenantid(userId, tenantId);
		
		try {
			userService.deleteUser(deleteUser);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.debug("-----------------------Delete user end-----------------------------!");
	}

	@RequestMapping(value = "/admin/addUser", method = RequestMethod.POST)
	@ResponseBody
	public ValidateResponseObject createNewUser(HttpServletRequest request, @CookieValue("loginCookie")String loginCookie, @Valid User user, BindingResult bindingResult, Model model) throws JsonProcessingException {
		logger.debug("-------------------createNewUser is running---------------------!");
		
		User currentUser                = commonUtil.getUserInfo(loginCookie);
		int tenantId                    = currentUser.getTenantid();
		ValidateResponseObject response = new ValidateResponseObject();
		User userExists                 = userService.findUserByUseridAndTenantid(user.getUserid(), tenantId);
		
		if (userExists != null) {
			logger.debug("User Id existed!");
			bindingResult.rejectValue("userid", "error.user", "This userId existed!");
		}
		
		if (bindingResult.hasErrors()) {
			logger.debug("Binding result has Error!");
			
			//Get error message
			Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(
					Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
			);
			
			response.setResult(0);
			response.setErrorMessages(errors);
		}
		else {
			String deptID       = user.getDepartmentid();
			Department dept     = deptService.findByDepartmentidAndTenantid(deptID, tenantId);
			Department company  = deptService.findByDepartmentidAndTenantid(dept.getCompanyId(), tenantId);
			
			user.setTenantid(tenantId);
			user.setCompanyid(company.getCompanyId());
			user.setCompanyname(company.getCompanyName());
			
			logger.debug("++++++++++++++++++Check User Infor++++++++++++++++++!");
			logger.debug("----------------- User ID             : " + user.getUserid());
			logger.debug("----------------- User Name           : " + user.getUsername());
			logger.debug("----------------- User Department Name: " + user.getDepartmentname());
			logger.debug("----------------- User Department ID  : " + user.getDepartmentid());
			logger.debug("----------------- User Company ID     : " + user.getCompanyid());
			logger.debug("----------------- User Company Name   : " + user.getCompanyname());
			logger.debug("----------------- User Tenant ID      : " + user.getCompanyname());
			logger.debug("++++++++++++++++++User Infor End++++++++++++++++++++!");
			
			userService.saveUser(user);
			response.setResult(1);
		}
		
		return response;
	}

	@RequestMapping(value = "/admin/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public ValidateResponseObject updateUser(HttpServletRequest request, @CookieValue("loginCookie")String loginCookie, @Valid User user, BindingResult bindingResult, Model model) throws JsonProcessingException {
		logger.debug("-------------------createNewUser is running---------------------!");
		
		User currentUser                = commonUtil.getUserInfo(loginCookie);
		int tenantId                    = currentUser.getTenantid();
		ValidateResponseObject response = new ValidateResponseObject();
		User userExists                 = userService.findUserByUseridAndTenantid(user.getUserid(), tenantId);
		
		if (userExists == null) {
			logger.debug("Error: user not found!");
			bindingResult.rejectValue("userid", "error.user", "User information not found in database");
		}
		
		if (bindingResult.hasErrors()) {
			logger.debug("Binding result has Error!");
			
			//Get error message
			Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(
					Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
			);
			
			response.setResult(0);
			response.setErrorMessages(errors);
		}
		else {
			user.setTenantid(tenantId);
			user.setCompanyid(userExists.getCompanyid());
			user.setCompanyname(userExists.getCompanyname());
			user.setRoles(userExists.getRoles());
			user.setActive(1);
			
			logger.debug("++++++++++++++++++Check User Infor++++++++++++++++++!");
			logger.debug("----------------- User ID             : " + user.getUserid());
			logger.debug("----------------- User Name           : " + user.getUsername());
			logger.debug("----------------- User Department Name: " + user.getDepartmentname());
			logger.debug("----------------- User Department ID  : " + user.getDepartmentid());
			logger.debug("----------------- User Company ID     : " + user.getCompanyid());
			logger.debug("----------------- User Company Name   : " + user.getCompanyname());
			logger.debug("----------------- User Tenant ID      : " + user.getTenantid());
			logger.debug("++++++++++++++++++User Infor End++++++++++++++++++++!");
			
			userService.updateUser(user);
			response.setResult(1);
		}	
		
		return response;
	}

	@RequestMapping(value="/admin/addUserImage", method = RequestMethod.GET)
	public String addImage(HttpServletRequest request, Model model) {
		logger.debug("----------------------Add user image is running-----------------------!");
		logger.debug("-----------------------Add user image end-----------------------------!");
		return "admin/organ/employeePicture";
	}

	@RequestMapping(value = "/admin/organ/signImageUpload", method = RequestMethod.POST)
	@ResponseBody
	public String signImageUpload(HttpServletRequest req, Model model, MultipartHttpServletRequest request) throws Exception {
		logger.debug("----------------------signImageUpload is running-----------------------!");
		
		String result                   = "";
		List<MultipartFile> multiFile   = request.getFiles("fileToUpload");
		String realPath                 = request.getServletContext().getRealPath("");
		String pFileName                = "";
		String sGUID                    = UUID.randomUUID().toString();
		String pUploadSN                = sGUID;
		
		if (multiFile.size() == 0) {
			result = "{\"data\":\"Fail\"}";
			return result;
		}
		
		if (!multiFile.get(0).getOriginalFilename().isEmpty() && !multiFile.get(0).getOriginalFilename().equals("")) {
			pFileName = multiFile.get(0).getOriginalFilename();
		}
		
		String pDirPath = "/file/";
		pDirPath        = realPath + pDirPath;
		logger.debug("pDirPath: " + pDirPath);
		
		if (!pDirPath.substring(pDirPath.length() - 1).equals("/")) {
			pDirPath = pDirPath + "/";
		}
		
		File file = new File(pDirPath + "uploadFile");
		
		if (!file.exists()) {
			file.mkdir();
		}
		
		String extend      = pFileName.substring(pFileName.lastIndexOf(".") + 1);
		String newFileName = pUploadSN + "." + extend;
		String finalPath   = "/file/uploadFile/" + newFileName;
		
		commonUtil.writeUploadedFile(multiFile.get(0), newFileName, pDirPath + "uploadFile");
		
		result = "{\"data\":\"" + finalPath + "\",\"fname\":\"" + pFileName + "\"}";
		logger.debug("-----------------------signImageUpload end-----------------------------!");
		
		return result;
	}

	@RequestMapping(value="/admin/addDepartment", method = RequestMethod.GET)
	public String addDepartment(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, Model model) {
		logger.debug("----------------------Add department is running-----------------------!");
		User loginUser   = commonUtil.getUserInfo(loginCookie);
		int tenantId     = loginUser.getTenantid();
		String pDeptId   = request.getParameter("pDeptId")   != null ? request.getParameter("pDeptId")   : "";
		String pDeptName = request.getParameter("pDeptName") != null ? request.getParameter("pDeptName") : "";
		String deptId    = request.getParameter("deptId")    != null ? request.getParameter("deptId")    : "";
		
		if (!pDeptId.equals("")) {
			Department pDept = deptService.findByDepartmentidAndTenantid(pDeptId, tenantId);
			String compName  = pDept.getCompanyName();
			String compID    = pDept.getCompanyId();
			Department dept  = new Department();
			
			model.addAttribute("dept", dept);
			model.addAttribute("pDeptID", pDeptId);
			model.addAttribute("pDeptName", pDeptName);
			model.addAttribute("compID", compID);
			model.addAttribute("compName", compName);
			model.addAttribute("mode", "add");
		}
		else {
			Department vDept = deptService.findByDepartmentidAndTenantid(deptId, tenantId);
			Department pDept = deptService.findByDepartmentidAndTenantid(vDept.getParentdept(), tenantId);
			model.addAttribute("dept", vDept);
			model.addAttribute("pDeptName", pDept.getDepartmentname());
			model.addAttribute("mode", "view");
		}
		
		logger.debug("-----------------------Add department end-----------------------------!");
		return "admin/organ/addDept";
	}

	@RequestMapping(value = "/admin/saveNewDept", method = RequestMethod.POST)
	@ResponseBody
	public ValidateResponseObject saveNewDept(HttpServletRequest request, @CookieValue("loginCookie")String loginCookie, @Valid Department dept, BindingResult bindingResult, Model model) throws JsonProcessingException {
		logger.debug("-------------------save new dept is running---------------------!");
		
		User currentUser                = commonUtil.getUserInfo(loginCookie);
		int tenantId                    = currentUser.getTenantid();
		ValidateResponseObject response = new ValidateResponseObject();
		Department existDept            = deptService.findByDepartmentidAndTenantid(dept.getDepartmentid(), tenantId);
		
		if (existDept != null) {
			logger.debug("Dept Id existed!");
			bindingResult.rejectValue("departmentid", "error.dept", "This deptId existed!");
		}
		
		if (bindingResult.hasErrors()) {
			logger.debug("Binding result has Error!");
			
			//Get error message
			Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(
				Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
			);
			
			response.setResult(0);
			response.setErrorMessages(errors);
		}
		else {
			String pDeptID      = dept.getParentdept();
			Department pDept    = deptService.findByDepartmentidAndTenantid(pDeptID, tenantId);
			
			dept.setTenantid(tenantId);
			dept.setDepartmentpath(pDept.getDepartmentpath() + "::" + dept.getDepartmentid());
			
			logger.debug("++++++++++++++++++Check Dept Infor++++++++++++++++++!");
			logger.debug("----------------- Dept ID             : " + dept.getDepartmentid());
			logger.debug("----------------- Dept Name           : " + dept.getDepartmentname());
			logger.debug("----------------- Parent Dept ID      : " + dept.getParentdept());
			logger.debug("----------------- Dept Email          : " + dept.getEmail());
			logger.debug("----------------- Dept Path           : " + dept.getDepartmentpath());
			logger.debug("----------------- Company ID          : " + dept.getCompanyId());
			logger.debug("----------------- Company Name        : " + dept.getCompanyName());
			logger.debug("----------------- Tenant ID           : " + dept.getTenantid());
			logger.debug("++++++++++++++++++Dept Infor End++++++++++++++++++++!");
			
			deptService.saveDept(dept);
			response.setResult(1);
		}
		
		return response;
	}

	@RequestMapping(value = "/admin/updateDept", method = RequestMethod.POST)
	@ResponseBody
	public ValidateResponseObject updateDept(HttpServletRequest request, @CookieValue("loginCookie")String loginCookie, @Valid Department dept, BindingResult bindingResult, Model model) throws JsonProcessingException {
		logger.debug("-------------------update dept is running---------------------!");
		
		User currentUser                = commonUtil.getUserInfo(loginCookie);
		int tenantId                    = currentUser.getTenantid();
		ValidateResponseObject response = new ValidateResponseObject();
		Department existDept            = deptService.findByDepartmentidAndTenantid(dept.getDepartmentid(), tenantId);
		
		if (existDept == null) {
			logger.debug("Error: Department not found!");
			bindingResult.rejectValue("departmentid", "error.dept", "Department information not found");
		}
		
		if (bindingResult.hasErrors()) {
			logger.debug("Binding result has Error!");
			
			//Get error message
			Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(
				Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
			);
			
			response.setResult(0);
			response.setErrorMessages(errors);
		}
		else {
			dept.setDepartmentpath(existDept.getDepartmentpath());
			
			logger.debug("++++++++++++++++++Check Dept Infor++++++++++++++++++!");
			logger.debug("----------------- Dept ID             : " + dept.getDepartmentid());
			logger.debug("----------------- Dept Name           : " + dept.getDepartmentname());
			logger.debug("----------------- Parent Dept ID      : " + dept.getParentdept());
			logger.debug("----------------- Dept Email          : " + dept.getEmail());
			logger.debug("----------------- Dept Path           : " + dept.getDepartmentpath());
			logger.debug("----------------- Company ID          : " + dept.getCompanyId());
			logger.debug("----------------- Company Name        : " + dept.getCompanyName());
			logger.debug("----------------- Tenant ID           : " + dept.getTenantid());
			logger.debug("++++++++++++++++++Dept Infor End++++++++++++++++++++!");
			
			deptService.updateDept(dept);
			response.setResult(1);
		}
		
		return response;
	}

	@RequestMapping(value="/admin/deleteDept", method = RequestMethod.POST)
	@ResponseBody
	public ValidateResponseObject deleteDept(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		logger.debug("----------------------Delete dept is running-----------------------!");
		
		User loginUser              = commonUtil.getUserInfo(loginCookie);
		ValidateResponseObject resp = new ValidateResponseObject();
		Map<String, String> errors  = new HashMap<>();
		int tenantId                = loginUser.getTenantid();
		String deptId               = request.getParameter("deptId") != null ? request.getParameter("deptId") : "";
		
		boolean checkBeforeDel = hasUser(deptId, tenantId);
		
		if (!checkBeforeDel) {
			try {
				Department dept = deptService.findByDepartmentidAndTenantid(deptId, tenantId);
				List<Department> listSubDepts = deptService.getAllSubDepts(deptId, tenantId);
				
				for (Department depart : listSubDepts) {
					deptService.deleteDept(depart);
				}
				
				deptService.deleteDept(dept);
				resp.setResult(1);
			}
			catch (Exception e) {
				resp.setResult(0);
				errors.put("unknown", e.getMessage());
				resp.setErrorMessages(errors);
			}
		}
		else {
			errors.put("hasUser", "This department has users so you cannot delete it!");
			resp.setErrorMessages(errors);
			resp.setResult(0);
		}
		
		logger.debug("-----------------------Delete dept end-----------------------------!");
		
		return resp;
	}

	@RequestMapping(value="/admin/moveDept", method = RequestMethod.GET)
	public String moveDept(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, Model model) throws JsonProcessingException {
		logger.debug("----------------------Move dept is running-----------------------!");
		ObjectMapper om       = new ObjectMapper();
		User loginUser        = commonUtil.getUserInfo(loginCookie);
		int tenantId          = loginUser.getTenantid();
		String deptId         = request.getParameter("deptId") != null ? request.getParameter("deptId") : "";
		Department mdept      = deptService.findByDepartmentidAndTenantid(deptId, tenantId);
		SimpleDepartment dept = deptService.getSimpleDeptList(mdept.getCompanyId(), tenantId);
		
		getAllSubDepts(dept, tenantId, 1);
		model.addAttribute("listDepartment", om.writeValueAsString(dept));
		model.addAttribute("usercompID", mdept.getCompanyId());
		model.addAttribute("deptID", deptId);
		
		logger.debug("-----------------------Move dept end-----------------------------!");
		return "admin/organ/moveDept";
	}

	@RequestMapping(value="/admin/saveMovedDept", method = RequestMethod.POST)
	@ResponseBody
	public ValidateResponseObject saveMovedDept(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		logger.debug("----------------------Save moved dept is running-----------------------!");
		
		User loginUser                = commonUtil.getUserInfo(loginCookie);
		int tenantId                  = loginUser.getTenantid();
		ValidateResponseObject resp   = new ValidateResponseObject();
		Map<String, String> errors    = new HashMap<>();
		String currentDeptId          = request.getParameter("crDeptId")  != null ? request.getParameter("crDeptId")  : "";
		String newDeptId              = request.getParameter("newDeptId") != null ? request.getParameter("newDeptId") : "";
		Department movedDept          = deptService.findByDepartmentidAndTenantid(currentDeptId, tenantId);
		Department newDept            = deptService.findByDepartmentidAndTenantid(newDeptId, tenantId);
		List<Department> listSubDepts = new ArrayList<Department>();
		
		getAllSubDepts(listSubDepts, currentDeptId, tenantId);
		
		if (currentDeptId.equals(newDeptId)) {
			errors.put("reason", "Cannot move this department to its position!");
			resp.setErrorMessages(errors);
			resp.setResult(0);
			return resp;
		}
		
		if (listSubDepts.contains(newDept)) {
			errors.put("reason", "Cannot move this department to its sub department!");
			resp.setErrorMessages(errors);
			resp.setResult(0);
			return resp;
		}
		
		try {
			String deptPath = newDept.getDepartmentpath();
			movedDept.setDepartmentpath(deptPath + "::" + movedDept.getDepartmentid());
			movedDept.setParentdept(newDeptId);
			deptService.updateDept(movedDept);
			movedAllSubDept(currentDeptId, movedDept.getDepartmentpath(), tenantId);
			resp.setResult(1);
		}
		catch (Exception e) {
			errors.put("reason", e.getMessage());
			resp.setResult(0);
		}
		
		logger.debug("-----------------------Save dept User end-----------------------------!");
		
		return resp;
	}

	@RequestMapping(value="/admin/organ/getInfoAfterMoving", method = RequestMethod.POST)
	@ResponseBody
	public String getNewSubDept(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request) throws JsonProcessingException {
		logger.debug("======================getSimpleSubDept start======================");
		
		ObjectMapper om                    = new ObjectMapper();
		User user                          = commonUtil.getUserInfo(loginCookie);
		int tenantId                       = user.getTenantid();
		String deptID                      = request.getParameter("deptID");
		Department dept                    = deptService.findByDepartmentidAndTenantid(deptID, tenantId);
		String[] deptPath                  = dept.getDepartmentpath().split("::");
		SimpleDepartment highestParentDept = deptService.getSimpleDeptList(deptPath[1], tenantId);
		getAllSubDepts2(highestParentDept, tenantId, deptPath, 2);
		
		logger.debug("======================getSimpleSubDept end======================");
		return om.writeValueAsString(highestParentDept);
	}

	@RequestMapping(value="/admin/addCompany", method = RequestMethod.GET)
	public String addCompany(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, Model model) {
		logger.debug("----------------------Add company is running-----------------------!");
		User loginUser   = commonUtil.getUserInfo(loginCookie);
		int tenantId     = loginUser.getTenantid();
		String companyId = request.getParameter("companyId") != null ? request.getParameter("companyId") : "";
		
		if (companyId.equals("")) {
			//Get all company
			List<SimpleDepartment> simpleCompanyList = deptService.getAllSimpleSubDepts("self", tenantId);
			Department dept                          = new Department();
			
			model.addAttribute("numberOfComp", simpleCompanyList.size() + 1);
			model.addAttribute("dept", dept);
			model.addAttribute("mode", "add");
		}
		else {
			Department vDept = deptService.findByDepartmentidAndTenantid(companyId, tenantId);
			model.addAttribute("dept", vDept);
			model.addAttribute("mode", "view");
		}
		
		logger.debug("-----------------------Add company end-----------------------------!");
		return "admin/organ/addCompany";
	}

	@RequestMapping(value = "/admin/saveNewCompany", method = RequestMethod.POST)
	@ResponseBody
	public ValidateResponseObject saveNewComp(HttpServletRequest request, @CookieValue("loginCookie")String loginCookie, @Valid Department dept, BindingResult bindingResult, Model model) throws JsonProcessingException {
		logger.debug("-------------------save new company is running---------------------!");
		
		User currentUser                = commonUtil.getUserInfo(loginCookie);
		int tenantId                    = currentUser.getTenantid();
		ValidateResponseObject response = new ValidateResponseObject();
		Department existComp            = deptService.findByDepartmentidAndTenantid(dept.getDepartmentid(), tenantId);
		
		if (existComp != null) {
			logger.debug("Dept Id existed!");
			bindingResult.rejectValue("departmentid", "error.dept", "This deptId existed!");
		}
		
		if (bindingResult.hasErrors()) {
			logger.debug("Binding result has Error!");
			
			//Get error message
			Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(
				Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
			);
			
			response.setResult(0);
			response.setErrorMessages(errors);
		}
		else {
			dept.setTenantid(tenantId);
			dept.setDepartmentpath(dept.getDepartmentid() + "::");
			dept.setCompanyId(dept.getDepartmentid());
			dept.setCompanyName(dept.getDepartmentname());
			dept.setParentdept("self");
			
			logger.debug("++++++++++++++++++Check Company Infor++++++++++++++++++!");
			logger.debug("----------------- Company ID              : " + dept.getDepartmentid());
			logger.debug("----------------- Company Name            : " + dept.getDepartmentname());
			logger.debug("----------------- Company Email           : " + dept.getEmail());
			logger.debug("----------------- Tenant ID               : " + dept.getTenantid());
			logger.debug("++++++++++++++++++Company Infor End++++++++++++++++++++!");
			
			deptService.saveDept(dept);
			response.setResult(1);
		}	
		
		return response;
	}

	@RequestMapping(value = "/admin/updateCompany", method = RequestMethod.POST)
	@ResponseBody
	public ValidateResponseObject updateCompany(HttpServletRequest request, @CookieValue("loginCookie")String loginCookie, @Valid Department dept, BindingResult bindingResult, Model model) throws JsonProcessingException {
		logger.debug("-------------------update company is running---------------------!");
		
		User currentUser                = commonUtil.getUserInfo(loginCookie);
		int tenantId                    = currentUser.getTenantid();
		ValidateResponseObject response = new ValidateResponseObject();
		Department existComp            = deptService.findByDepartmentidAndTenantid(dept.getDepartmentid(), tenantId);
		
		if (existComp == null) {
			logger.debug("Error: Company not found!");
			bindingResult.rejectValue("departmentid", "error.dept", "Company information not found");
		}
		
		if (bindingResult.hasErrors()) {
			logger.debug("Binding result has Error!");
			
			//Get error message
			Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(
				Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
			);
			
			response.setResult(0);
			response.setErrorMessages(errors);
		}
		else {
			existComp.setEmail(dept.getEmail());
			
			logger.debug("++++++++++++++++++Check Company Infor++++++++++++++++++!");
			logger.debug("----------------- Company ID              : " + existComp.getDepartmentid());
			logger.debug("----------------- Company Name            : " + existComp.getDepartmentname());
			logger.debug("----------------- Company Email           : " + existComp.getEmail());
			logger.debug("----------------- Tenant ID               : " + existComp.getTenantid());
			logger.debug("++++++++++++++++++Company Infor End++++++++++++++++++++!");
			
			deptService.updateDept(existComp);
			response.setResult(1);
		}
		
		return response;
	}

	@RequestMapping(value="/admin/deleteCompany", method = RequestMethod.POST)
	@ResponseBody
	public ValidateResponseObject deleteCompany(@CookieValue("loginCookie")String loginCookie, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		logger.debug("----------------------Delete company is running-----------------------!");
		
		User loginUser                = commonUtil.getUserInfo(loginCookie);
		ValidateResponseObject resp   = new ValidateResponseObject();
		Map<String, String> errors    = new HashMap<>();
		int tenantId                  = loginUser.getTenantid();
		String companyId              = request.getParameter("companyId") != null ? request.getParameter("companyId") : "";
		List<Department> subDeptsList = deptService.getAllSubDepts(companyId, tenantId);
		
		if (subDeptsList.size() == 0) {
			try {
				Department company = deptService.findByDepartmentidAndTenantid(companyId, tenantId);
				deptService.deleteDept(company);
				resp.setResult(1);
			}
			catch (Exception e) {
				resp.setResult(0);
				errors.put("reason", e.getMessage());
				resp.setErrorMessages(errors);
			}
		}
		else {
			errors.put("reason", "You cannot delete a company which has departments!");
			resp.setErrorMessages(errors);
			resp.setResult(0);
		}
		
		logger.debug("-----------------------Delete company end-----------------------------!");
		
		return resp;
	}
	
	private boolean hasUser(String deptId, int tenantId) {
		boolean check = false;
		List<User> listOfUsers = userService.getAllUsersOfDepartment(deptId, tenantId);
		
		if (listOfUsers.size() == 0) {
			List<Department> listSubDepts = deptService.getAllSubDepts(deptId, tenantId);
			
			for (Department dept : listSubDepts) {
				if (hasUser(dept.getDepartmentid(), tenantId)) {
					check = true;
					break;
				}
			}
		}
		else {
			check = true;
		}
		
		return check;
	}
	
	private void getAllSubDepts(List<Department> listSubDepts, String deptId, int tenantId) {
		List<Department> subDeptsList = deptService.getAllSubDepts(deptId, tenantId);
		if (subDeptsList.size() > 0) {
			listSubDepts.addAll(subDeptsList);
			for (Department dept : subDeptsList) {
				getAllSubDepts(listSubDepts, dept.getDepartmentid(), tenantId);
			}
		}
	}
	
	private void movedAllSubDept(String deptId, String deptpath, int tenantId) {
		List<Department> subDeptsList = deptService.getAllSubDepts(deptId, tenantId);
		
		if (subDeptsList.size() > 0) {
			for (Department dept2 : subDeptsList) {
				dept2.setDepartmentpath(deptpath + "::" + dept2.getDepartmentid());
				deptService.updateDept(dept2);
				movedAllSubDept(dept2.getDepartmentid(), dept2.getDepartmentpath(), tenantId);
			}
		}
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}