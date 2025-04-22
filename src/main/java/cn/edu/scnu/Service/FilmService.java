package cn.edu.scnu.Service;

import cn.edu.scnu.entity.Film;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.scnu.mapper.FilmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

@Service
public class FilmService extends ServiceImpl<FilmMapper, Film> {
    @Autowired
    private FilmMapper filmMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    public List<String> findfclass(){
        QueryWrapper<Film> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("distinct fclass");
        List<Film> filmList = filmMapper.selectList(queryWrapper);
        List<String> fclasslist=new ArrayList<>();
        for(Film film : filmList){
            fclasslist.add(film.getFclass());
        }
        return fclasslist;
    }

    public List<String> findfarea(){
        QueryWrapper<Film> queryWrapper =new QueryWrapper<>();
        queryWrapper.select("distinct farea");
        List<Film> filmList = filmMapper.selectList(queryWrapper);
        List<String> fclasslist=new ArrayList<>();
        for(Film film : filmList){
            fclasslist.add(film.getFarea());
        }
        return fclasslist;
    }

    public Map<String, Object> queryPage(String fname,
                                         String fclass,
                                         String farea,
                                         String fperformer,
                                         Integer pageNo,
                                         Integer pageSize) {
        QueryWrapper<Film> queryWrapper = new QueryWrapper<>();
        if(!"".equals(fname)&&fname!=""){
            queryWrapper.like("fname",fname);
        }
        if(!"".equals(fclass)&&fclass!=""){
            queryWrapper.like("fclass",fclass);
        }
        if(!"".equals(farea)&&farea!=""){
            queryWrapper.like("farea",farea);
        }
        if(!"".equals(fperformer)&&fperformer!=""){
            queryWrapper.and(wrapper -> wrapper
                    .like("performer", fperformer)
                    .or()
                    .like("director", fperformer)
            );
        }
        queryWrapper.orderByDesc("playbackVolume");
        int count = filmMapper.selectCount(queryWrapper).intValue();

        Page<Film> page=new Page<Film>(pageNo,pageSize);
        Page<Film> filmPage= filmMapper.selectPage(page,queryWrapper);

        Map<String,Object> map=new HashMap<>();
        map.put("count",count);
        map.put("records",page.getRecords());
        return map;
    }


    public Object findFilmById(String filmid) {
        Object object=redisTemplate.opsForValue().get("film_"+filmid);
        if(object!=null){
            return (Film)object;
        }else{
            Film film = filmMapper.selectById(filmid);
            redisTemplate.opsForValue().set("film_"+filmid, film);
            return film;
        }
    }


    public void updateFilm(Film film) {
        filmMapper.updateById(film);
        redisTemplate.opsForValue().set("film_"+ film.getFilmid(), film);
    }

    public List<String[]> getFilmPrintList() {
        int i=1;
        List<String[]> films=new ArrayList<>();
        QueryWrapper<Film> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("playbackVolume");
        List<Film> filmList = filmMapper.selectList(queryWrapper);
        for(Film film:filmList){
            String[] row = {Integer.toString(i), film.getFname(), film.getFclass(), film.getFarea(), film.getDirector(), film.getPerformer(),
            Integer.toString(film.getPlaybackvolume()), Float.toString(film.getScore())};
            films.add(row);
            i++;
        }
        return films;
    }


    public Map<String, Object> queryRankPage(Integer rankType,Integer pageNo, Integer pageSize) {
        QueryWrapper<Film> queryWrapper = new QueryWrapper<>();
        if(rankType==1){
            queryWrapper.orderByDesc("playbackVolume");
        }
       else if(rankType==2){
            queryWrapper.orderByDesc("rankingThisWeek");
        }
        else if(rankType==3){
            queryWrapper.orderByDesc("rankingThisMonth");
        }
        else if(rankType==4){
            queryWrapper.orderByDesc("score");

        }
        int count = filmMapper.selectCount(queryWrapper).intValue();

        Page<Film> page=new Page<Film>(pageNo,pageSize);
        Page<Film> filmPage= filmMapper.selectPage(page,queryWrapper);

        Map<String,Object> map=new HashMap<>();
        map.put("count",count);
        map.put("records",page.getRecords());
        return map;
    }


    public Map<String, Integer> countByFclass() {
        List<Film> filmList = filmMapper.selectList(null);
        Map<String, Integer> countMap = new HashMap<>();
        for (Film film : filmList) {
            String fclass = film.getFclass();
            countMap.put(fclass, countMap.getOrDefault(fclass, 0) + 1);
        }
        return countMap;
    }

    public Map<String, Integer> countByFarea() {
        List<Film> filmList = filmMapper.selectList(null);
        Map<String, Integer> countMap = new HashMap<>();
        for (Film film : filmList) {
            String farea = film.getFarea();
            countMap.put(farea, countMap.getOrDefault(farea, 0) + 1);
        }
        return countMap;
    }

    public Map<Integer, Integer> countByFyear() {
        List<Film> filmList = filmMapper.selectList(null);
        Map<Integer, Integer> countMap = new TreeMap<>();
        for (Film film : filmList) {
            Integer year = film.getYear();
            countMap.put(year, countMap.getOrDefault(year, 0) + 1);
        }
        return countMap;
    }

    public Map<Float, Integer> countByScore() {
        List<Film> filmList = filmMapper.selectList(null);
        Map<Float, Integer> countMap = new TreeMap<>();
        for (Film film : filmList) {
            Float score = film.getScore();
            countMap.put(score, countMap.getOrDefault(score, 0) + 1);
        }
        return countMap;
    }
}
