package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mystaff")
public class Staff implements Serializable {
    private static final long serialVersionUID = -3710185376322109571L;
    @TableId(type = IdType.AUTO)
    private Integer staff_id;
    private String staff_name;
    private String film_staff_id;
    private String status;
}
