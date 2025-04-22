package cn.edu.scnu.controller;

import cn.edu.scnu.Service.FilmService;
import cn.edu.scnu.Service.TbMemberService;
import cn.edu.scnu.entity.Film;
import cn.edu.scnu.mapper.FilmMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Controller
//测试用
public class RankController {
    @Autowired
    private FilmService filmService;
    @Autowired
    private FilmMapper filmMapper;
    @Autowired
    private TbMemberService tbMemberService;


    //排行榜入口
    @RequestMapping("/rank")
    public String rank(@RequestParam(name = "rankType",defaultValue = "1")Integer rankType,
            @RequestParam(name = "pageNo",defaultValue = "1")Integer pageNo,
                       Model model){

//        echarts图表功能
//        电影类型分布柱状图数据
        Map<String,Integer> fclassMap = filmService.countByFclass();
        List<String> movieTypes = new ArrayList<>();
        List<Integer> typeCounts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : fclassMap.entrySet()){
            movieTypes.add(entry.getKey());
            typeCounts.add(entry.getValue());
        }
        model.addAttribute("movieTypes",movieTypes);
        model.addAttribute("typeCounts",typeCounts);

//        电影地区分布饼图数据
        Map<String,Integer> fareaMap = filmService.countByFarea();
        List<String> movieAreas = new ArrayList<>();
        List<Integer> areaCounts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : fareaMap.entrySet()){
            movieAreas.add(entry.getKey());
            areaCounts.add(entry.getValue());
        }
        model.addAttribute("movieRegions",movieAreas);
        model.addAttribute("regionCounts",areaCounts);

//影片上映年份分布折线图数据
        Map<Integer,Integer> fyearMap = filmService.countByFyear();
        List<Integer> movieyears = new ArrayList<>();
        List<Integer> yearCounts = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : fyearMap.entrySet()){
            movieyears.add(entry.getKey());
            yearCounts.add(entry.getValue());
        }
        model.addAttribute("movieYears",movieyears);
        model.addAttribute("yearCounts",yearCounts);

//        影片评分分布散点图
        Map<Float,Integer> fscoreMap = filmService.countByScore();
        List<Float> moviescores = new ArrayList<>();
        List<Integer> scoreCounts = new ArrayList<>();
        for (Map.Entry<Float, Integer> entry : fscoreMap.entrySet()){
            moviescores.add(entry.getKey());
            scoreCounts.add(entry.getValue());
        }
        model.addAttribute("moviescores",moviescores);
        model.addAttribute("scoreCounts",yearCounts);


//        排行榜功能
        Integer pageSize=5;

        Map<String,Object> map= filmService.queryRankPage(rankType,pageNo,pageSize);
        int totalRecords = (Integer)map.get("count");
        List<Film> filmlist=(List<Film>) map.get("records");
        Integer pageCount = (totalRecords % pageSize == 0)?(totalRecords/pageSize):((totalRecords/pageSize)+1);
        model.addAttribute("rankType",rankType);
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("filmlist",filmlist);

        return "rank";
    }

    @RequestMapping("/echart")
    public String echart(@RequestParam(name = "rankType",defaultValue = "1")Integer rankType,
                       @RequestParam(name = "pageNo",defaultValue = "1")Integer pageNo,
                       Model model){

//        echarts图表功能
//        电影类型分布柱状图数据
        Map<String,Integer> fclassMap = filmService.countByFclass();
        List<String> movieTypes = new ArrayList<>();
        List<Integer> typeCounts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : fclassMap.entrySet()){
            movieTypes.add(entry.getKey());
            typeCounts.add(entry.getValue());
        }
        model.addAttribute("movieTypes",movieTypes);
        model.addAttribute("typeCounts",typeCounts);

//        电影地区分布饼图数据
        Map<String,Integer> fareaMap = filmService.countByFarea();
        List<String> movieAreas = new ArrayList<>();
        List<Integer> areaCounts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : fareaMap.entrySet()){
            movieAreas.add(entry.getKey());
            areaCounts.add(entry.getValue());
        }
        model.addAttribute("movieRegions",movieAreas);
        model.addAttribute("regionCounts",areaCounts);

//影片上映年份分布折线图数据
        Map<Integer,Integer> fyearMap = filmService.countByFyear();
        List<Integer> movieyears = new ArrayList<>();
        List<Integer> yearCounts = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : fyearMap.entrySet()){
            movieyears.add(entry.getKey());
            yearCounts.add(entry.getValue());
        }
        model.addAttribute("movieYears",movieyears);
        model.addAttribute("yearCounts",yearCounts);

//        影片评分分布散点图
        Map<Float,Integer> fscoreMap = filmService.countByScore();
        List<Float> moviescores = new ArrayList<>();
        List<Integer> scoreCounts = new ArrayList<>();
        for (Map.Entry<Float, Integer> entry : fscoreMap.entrySet()){
            moviescores.add(entry.getKey());
            scoreCounts.add(entry.getValue());
        }
        model.addAttribute("moviescores",moviescores);
        model.addAttribute("scoreCounts",yearCounts);


//        排行榜功能
        Integer pageSize=5;

        Map<String,Object> map= filmService.queryRankPage(rankType,pageNo,pageSize);
        int totalRecords = (Integer)map.get("count");
        List<Film> filmlist=(List<Film>) map.get("records");
        Integer pageCount = (totalRecords % pageSize == 0)?(totalRecords/pageSize):((totalRecords/pageSize)+1);
        model.addAttribute("rankType",rankType);
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("filmlist",filmlist);

        return "echart";
    }


    @GetMapping("/byte")
    public ResponseEntity<List<Map<String, Object>>> getDepartmentData() {
        try {
            // 1. 获取所有电影列表
            List<Film> allFilms = filmMapper.selectList(null);

            // 2. 统计每个地区的电影数量
            Map<String, Integer> filmAreaCount = new HashMap<>();
            for (Film film : allFilms) {
                String filmArea = film.getFarea();
                if (filmAreaCount.containsKey(filmArea)) {
                    filmAreaCount.put(filmArea, filmAreaCount.get(filmArea) + 1);
                } else {
                    filmAreaCount.put(filmArea, 1);
                }
            }

            // 3. 将统计结果转换成前端所需的数据格式
            List<Map<String, Object>> resultData = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : filmAreaCount.entrySet()) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("name", entry.getKey());
                dataMap.put("value", entry.getValue());
                resultData.add(dataMap);
            }

            // 4. 返回结果
            return ResponseEntity.ok(resultData);
        } catch (Exception e) {
            // 处理异常
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(
                            new HashMap<String, Object>() {{ put("error", e.getMessage()); }}
                    ));
        }
    }

//    POI技术打印榜单
    @RequestMapping(value = "/rank/print", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> print() {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建一个空白表
        Sheet sheet = workbook.createSheet("films");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        String[] columns = {"排名", "影片名称", "类型", "地区", "导演", "参演","总观看次数","评分"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // 数据
       List<String[]> movieData = filmService.getFilmPrintList();

        // 填充数据行
        int rowNum = 1;
        for (String[] movie : movieData) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < movie.length; i++) {
                row.createCell(i).setCellValue(movie[i]);
            }
        }

        // 将工作簿内容写入 ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 准备响应实体，包含 Excel 文件内容
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "films.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(outputStream.toByteArray(), headers, org.springframework.http.HttpStatus.OK);
        return responseEntity;
    }

}
