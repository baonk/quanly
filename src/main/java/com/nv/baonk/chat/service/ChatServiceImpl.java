package com.nv.baonk.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nv.baonk.chat.dao.ChatMapper;
import com.nv.baonk.chat.vo.ChatUserVO;

@Service("ChatService")
public class ChatServiceImpl implements ChatService{
	@Autowired
	private ChatMapper chatMapper;
	
	@Override
	public List<ChatUserVO> getChatUserList(String userId, int startPoint, int blockSize, int tenantId) throws Exception {
		return chatMapper.getChatUserList(userId, startPoint, blockSize, tenantId);
	}

	@Override
	public int getChatUserListCnt(String userId, int tenantId) throws Exception {
		return chatMapper.getChatUserListCnt(userId, tenantId);
	}

	@Override
	public List<ChatUserVO> getAllChatUsers(String userId, int startPoint, int blockSize, int tenantId) throws Exception {
		return chatMapper.getAllChatUsers(userId, startPoint, blockSize, tenantId);
	}

	@Override
	public int getAllChatUsersCnt(String userId, int tenantId) throws Exception {
		return chatMapper.getAllChatUsersCnt(userId, tenantId);
	}

}
