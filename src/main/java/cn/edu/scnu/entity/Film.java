package cn.edu.scnu.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;
@Data
@TableName("filmdb")
public class Film implements Serializable {
    private static final long serialVersionUID = -3710185376322109571L;
    @TableId(type = IdType.AUTO)
    private Integer filmid;
    private String filmiink;
    private String picture;
    private String fname;
    private String introduction;
    private String fclass;
    private String farea;
    private Float score;
    private Integer rankingthisweek;
    private Integer rankingthismonth;
    private Integer toprated;
    private Integer year;
    private Integer time;
    private Integer playbackvolume;
    private String director;
    private String performer;
    private Integer playpermissions;
}
