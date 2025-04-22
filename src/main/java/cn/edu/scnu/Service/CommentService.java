package cn.edu.scnu.Service;

import cn.edu.scnu.entity.Comment;
import cn.edu.scnu.entity.TbMember;
import cn.edu.scnu.mapper.CommentMapper;
import cn.edu.scnu.mapper.TbMemberMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TbMemberMapper tbMemberMapper;

    public Map<Comment,String> getCommentsByFilmId(String filmId) {
        // 使用QueryWrapper来构建查询条件
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("film_id", filmId);
        queryWrapper.orderByDesc("pjtime");
        // 执行查询并返回结果列表
        List<Comment> commentList=commentMapper.selectList(queryWrapper);
//        System.out.println("++++++"+commentList);
        Map<Comment,String> commentMap= new LinkedHashMap<>();
        String username;
        for (Comment comment:commentList){
            username=tbMemberMapper.selectById(comment.getMemberCommentId()).getMembername();
            commentMap.put(comment,username);
        }
        return commentMap;
    }

    public Integer countComment() {
        List<Comment> commentList = commentMapper.selectList(null);
        return commentList.size();
    }
    public void updateComment(Comment comment) {
        commentMapper.insert(comment);
    }


}