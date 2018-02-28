package com.nv.baonk.chat.service;

import java.util.List;

import com.nv.baonk.chat.vo.ChatMessageVO;
import com.nv.baonk.chat.vo.ChatUserVO;
import com.nv.baonk.chat.vo.ConversationVO;

public interface ChatService {
	List<ChatUserVO> getChatUserList(String userId, int startPoint, int blockSize, int tenantId) throws Exception;
	int getChatUserListCnt(String userId, int tenantId) throws Exception;
	List<ChatUserVO> getAllChatUsers(String userId, int startPoint, int blockSize, int tenantId) throws Exception;
	int getAllChatUsersCnt(String userId, int tenantId) throws Exception;
	List<ChatMessageVO> getPersonalMessages(String userId, String friendId, int endPoint, int tenantId) throws Exception;
	String getMaxMessageId(int tenantId) throws Exception;
	void saveMessage(ChatMessageVO messageVO) throws Exception;
	List<ChatUserVO> getAllGroupChat(String userId, String departmentid, int tenantId) throws Exception;
	void insertLastMessage(ConversationVO conversation) throws Exception;
}
