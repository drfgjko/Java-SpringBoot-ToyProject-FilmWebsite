package cn.edu.scnu.controller;

import cn.edu.scnu.Service.CommentService;
import cn.edu.scnu.Service.FilmService;
import cn.edu.scnu.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.sql.Timestamp;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private FilmService filmService;

    @RequestMapping("/filmdetail/evaluate")
    public String filmevaluate(String filmid, Model model, HttpSession session){
        Film film=(Film)filmService.findFilmById(filmid);
        TbMember member=(TbMember) session.getAttribute("memberLogin");
        model.addAttribute("member",member);
        model.addAttribute("film", film);
        return "filmevaluate";
    }

    @RequestMapping("/evaluate/doEvaluate")
    public String doEvaluate(HttpServletRequest request,HttpSession session){

        Integer filmid=Integer.parseInt(request.getParameter("filmid"));
        String pjcontent=request.getParameter("pjcontent");
        TbMember member=(TbMember) session.getAttribute("memberLogin");
//        System.out.println("+++++++++++"+member);
        Comment comment=new Comment();
        comment.setCommentId(commentService.countComment()+1);
        comment.setFilmId(filmid);
        comment.setContent(pjcontent);
        comment.setMemberCommentId(member.getMemberId());
        comment.setPjtime(new Timestamp(System.currentTimeMillis()));
        commentService.updateComment(comment);

        return "forward:/index/filmdetail";
    }

}
