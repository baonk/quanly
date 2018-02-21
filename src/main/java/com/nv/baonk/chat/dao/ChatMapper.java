package com.nv.baonk.chat.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.nv.baonk.chat.vo.ChatUserVO;

@Mapper
public interface ChatMapper {
	public List<ChatUserVO> getChatUserList(@Param("userId") String userId, @Param("startPoint") int startPoint, @Param("blockSize") int blockSize, @Param("tenantId") int tenantId);
	public int getChatUserListCnt(@Param("userId") String userId, @Param("tenantId") int tenantId);
}
