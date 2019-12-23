package controller.Teacher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import domain.Teacher;
import service.TeacherService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        String id_str = request.getParameter("id");
        JSONObject message = new JSONObject();
        try {
            if (id_str != null) {
                int id = Integer.parseInt(id_str);
                Teacher teacher = TeacherService.getInstance().find(id);
                String teacher_str = JSON.toJSONString(teacher);
                response.getWriter().println(teacher_str);
            } else {
                Collection<Teacher> teachers = TeacherService.getInstance().findAll();
                String teachers_str = JSON.toJSONString(teachers);
                response.getWriter().println(teachers_str);
            }
        }catch (SQLException e){
            message.put("message","数据库异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String teacher_json = JSONUtil.getJSON(request);
        Teacher teacher = JSON.parseObject(teacher_json,Teacher.class);
        JSONObject message = new JSONObject();
        try {
            TeacherService.getInstance().add(teacher);
            message.put("message","添加成功");
            response.getWriter().println(message);
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message","数据库异常");
            response.getWriter().println(message);
        }catch (Exception e) {
            e.printStackTrace();
            message.put("message","网络异常");
            response.getWriter().println(message);
        }

    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        JSONObject message = new JSONObject();
        try {
        Teacher teacher = TeacherService.getInstance().find(id);
        TeacherService.getInstance().delete(teacher);
            message.put("message","删除成功");
            response.getWriter().println(message);
        }catch (SQLException e) {
            e.printStackTrace();
            message.put("message","数据库异常");
            response.getWriter().println(message);
        }catch (Exception e) {
            e.printStackTrace();
            message.put("message","网络异常");
            response.getWriter().println(message);
        }
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String teacher_json = JSONUtil.getJSON(request);
        Teacher teacher = JSON.parseObject(teacher_json,Teacher.class);
        JSONObject message = new JSONObject();
        try {
            TeacherService.getInstance().update(teacher);
            message.put("message","添加成功");
            response.getWriter().println(message);
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message","数据库异常");
            response.getWriter().println(message);
        }catch (Exception e) {
            e.printStackTrace();
            message.put("message","网络异常");
            response.getWriter().println(message);
        }

    }
}
