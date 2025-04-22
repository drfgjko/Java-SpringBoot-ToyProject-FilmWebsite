package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("comment")
public class Comment implements Serializable {
    private static final long serialVersionUID = -3710185376322109571L;
    @TableId(type= IdType.AUTO)
    private Integer commentId;
    private String content;
    private Integer filmId;
    private Integer memberCommentId;
    private Timestamp pjtime;
}
