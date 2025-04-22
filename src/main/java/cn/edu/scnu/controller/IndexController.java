package cn.edu.scnu.controller;

import cn.edu.scnu.Service.CommentService;
import cn.edu.scnu.Service.FilmService;
import cn.edu.scnu.Service.TbMemberService;
import cn.edu.scnu.entity.Comment;
import cn.edu.scnu.entity.Film;
import cn.edu.scnu.entity.TbMember;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController{
    @Autowired
    private FilmService filmService;
    @Autowired
    private TbMemberService tbMemberService;


    @Autowired
    private CommentService commentService;

    @RequestMapping("/index")
    public String index(@RequestParam(name = "pageNo",defaultValue = "1")Integer pageNo,
                        @RequestParam(name="fname",defaultValue = "")String fname,
                        @RequestParam(name="fclass",defaultValue = "")String fclass,
                        @RequestParam(name="farea",defaultValue = "")String farea,
                        @RequestParam(name="fperformer",defaultValue = "")String fperformer,
                        Model model,HttpSession session){
        Integer pageSize=8;

        Map<String,Object> map= filmService.queryPage(fname,fclass,farea,fperformer,pageNo,pageSize);
        int totalRecords = (Integer)map.get("count");
//        System.out.println(totalRecords);

        List<Film> filmlist=(List<Film>) map.get("records");
        Integer pageCount = (totalRecords % pageSize == 0)?(totalRecords/pageSize):((totalRecords/pageSize)+1);

        //       判断登录状态，传递member
        if (session.getAttribute("memberLogin") != null){
            TbMember member=(TbMember) session.getAttribute("memberLogin");
            model.addAttribute("member",member);
        }



        model.addAttribute("currentPage",pageNo);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("fname",fname);
        model.addAttribute("fclass",fclass);
        model.addAttribute("farea",farea);
        model.addAttribute("fperformer",fperformer);
        model.addAttribute("filmlist",filmlist);
        model.addAttribute("fclasses", filmService.findfclass());
        model.addAttribute("fareas",filmService.findfarea());
        return "index";
    }

    @RequestMapping("/index/filmdetail")
    public String filmdetail(String filmid,Model model,HttpSession session){
        //       判断登录状态，传递member
        if (session.getAttribute("memberLogin") == null)
            return "redirect:/index/toLogin";
        TbMember member=(TbMember) session.getAttribute("memberLogin");

        Map<Comment,String> commentMap = commentService.getCommentsByFilmId(filmid);

        model.addAttribute("member",member);
        model.addAttribute("film", filmService.findFilmById(filmid));
        model.addAttribute("comments",commentMap);

        return "filmdetail";
    }
    @RequestMapping("/index/video")
    public String video(String filmid,Model model,HttpSession session){
        Film film=(Film)filmService.findFilmById(filmid);

//       判断登录状态，传递member
        if (session.getAttribute("memberLogin") == null)
            return "redirect:/index/toLogin";
        TbMember member=(TbMember) session.getAttribute("memberLogin");
        model.addAttribute("member",member);


        // 增加 playbackVolume 值
        film.setPlaybackvolume(film.getPlaybackvolume() + 1);
        // 更新电影信息
        filmService.updateFilm(film);

        // 将电影信息添加到模型中
        model.addAttribute("film", film);
        return "video";
    }

    @RequestMapping("/index/toLogin")
    public String toLogin(){
        return "login_register";
    }

    @RequestMapping("/index/doLogin")
    @ResponseBody
    public String doLogin(String email, String password, HttpSession session){
        TbMember member= tbMemberService.login(email,password);

        if (member!=null){
            session.setAttribute("memberLogin",member);
//            System.out.println("++++++++++++"+session.getAttribute("memberLogin"));
            return "登录成功！";
        }else {
            return "登录失败！";
        }
    }

    @RequestMapping("/index/doRegister")
    @ResponseBody
    public String doRegister(String email,String passw1,String username){
        TbMember member1= tbMemberService.getByUsername(username);
        if (member1!=null){
            return "该用户名已被注册！";
        }
        TbMember member=new TbMember();
        member.setMemberId((int) tbMemberService.count()+1);
        member.setEmail(email);
        member.setPassword(passw1);
        member.setMembername(username);
        member.setType(0);
        tbMemberService.save(member);
//        System.out.println("注册成功！");
        return "注册成功";
    }

    @RequestMapping("/index/logOut")
    public String logOut(HttpSession session){
        session.removeAttribute("memberLogin");
        return "redirect:/index";
    }

    @RequestMapping("/admin/toAdminLogin")
    public String ToVip(Model model,HttpSession session){
        TbMember member=(TbMember) session.getAttribute("memberLogin");
        model.addAttribute("memberId",member.getMemberId());
        return "upgradetovip";
    }

    @RequestMapping("/barchart")
    public String ToBarchart(){
        return "barchart";
    }


}
