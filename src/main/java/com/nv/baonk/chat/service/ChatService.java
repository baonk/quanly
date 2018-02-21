package com.nv.baonk.chat.service;

import java.util.List;
import com.nv.baonk.chat.vo.ChatUserVO;

public interface ChatService {
	List<ChatUserVO> getChatUserList(String userId, int startPoint, int blockSize, int tenantId) throws Exception;
	int getChatUserListCnt(String userId, int tenantId) throws Exception;
}
