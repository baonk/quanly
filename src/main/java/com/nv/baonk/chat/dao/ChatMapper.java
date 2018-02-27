package com.nv.baonk.chat.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.nv.baonk.chat.vo.ChatMessageVO;
import com.nv.baonk.chat.vo.ChatUserVO;

@Mapper
public interface ChatMapper {
	public List<ChatUserVO> getChatUserList(@Param("userId") String userId, @Param("startPoint") int startPoint, @Param("blockSize") int blockSize, @Param("tenantId") int tenantId);
	public int getChatUserListCnt(@Param("userId") String userId, @Param("tenantId") int tenantId);
	public List<ChatUserVO> getAllChatUsers(@Param("userId") String userId, @Param("startPoint") int startPoint, @Param("blockSize") int blockSize, @Param("tenantId") int tenantId);
	public int getAllChatUsersCnt(@Param("userId") String userId, @Param("tenantId") int tenantId);
	public List<ChatMessageVO> getPersonalMessages(@Param("senderId") String senderId, @Param("receiverId") String receiverId, @Param("endPoint") int endPoint, @Param("tenantId") int tenantId);
	public String getMaxMessageId(@Param("tenantId") int tenantId);
	public void saveMessage(ChatMessageVO messageVO);
	public List<ChatUserVO> getAllGroupChat(@Param("userId") String userId, @Param("deptId") String departmentId, @Param("tenantId") int tenantId);
}
