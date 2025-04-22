package cn.edu.scnu.mapper;

import cn.edu.scnu.entity.TbMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface TbMemberMapper extends BaseMapper<TbMember> {

}
