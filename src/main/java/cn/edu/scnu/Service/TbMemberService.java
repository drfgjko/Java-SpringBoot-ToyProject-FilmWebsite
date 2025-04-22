package cn.edu.scnu.Service;

import cn.edu.scnu.entity.TbMember;
import cn.edu.scnu.mapper.TbMemberMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TbMemberService extends ServiceImpl<TbMemberMapper, TbMember> {
    @Autowired
    private TbMemberMapper TbMemberMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HttpServletRequest request;

    public TbMember login(String email, String password) {
        QueryWrapper<TbMember> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("email",email);

        List<TbMember> members=TbMemberMapper.selectList(queryWrapper);

        for(TbMember member:members){
            System.out.println(member);
            if(member!=null && member.getPassword().equals(password))
                return member;
        }
        return null;
    }

    public TbMember getByUsername(String username){
        QueryWrapper<TbMember> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("membername",username);

        TbMember member=TbMemberMapper.selectOne(queryWrapper);

        if(member!=null) return member;
        return null;
    }

    public void updateVipStatus(Integer memberId, int vipStatus) {
        TbMember member = getById(memberId);
        member.setType(vipStatus);
        TbMemberMapper.updateById(member);

    }
}
