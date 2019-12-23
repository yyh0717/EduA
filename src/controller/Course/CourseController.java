package controller.Course;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Course;
import service.CourseService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
@WebServlet("/course.ctl")
public class CourseController extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String id_str = request.getParameter("id");
        String courseTypeId_str = request.getParameter("courseTypeId");
        String name_str = request.getParameter("name");
        JSONObject message = new JSONObject();
        try {
            if (id_str!=null||courseTypeId_str!=null||name_str!=null){
                if (id_str!=null){
                    int id = Integer.parseInt(id_str);
                    Course course = CourseService.getInstance().find(id);
                    String course_json = JSON.toJSONString(course);
                    response.getWriter().println(course_json);
                }
                else if(courseTypeId_str!=null){
                    int courseTypeId = Integer.parseInt(courseTypeId_str);
                }
                else if(name_str!=null){
                    Collection<Course> courses = CourseService.getInstance().findByFuzzy(name_str);
                    //将集合类对象转化为JSON数据
                    String courses_json = JSON.toJSONString(courses);
                    response.getWriter().println(courses_json);
                }
            }else {
                Collection<Course> courses = CourseService.getInstance().findAll();
                String courses_json = JSON.toJSONString(courses);
                response.getWriter().println(courses_json);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        //从请求中获取json数据
        String project_json = JSONUtil.getJSON(request);
        //将json数据转换成对象
        Course course = JSON.parseObject(project_json, Course.class);
        JSONObject message = new JSONObject();
        //添加
        try {
            CourseService.getInstance().add(course);
            message.put("message","添加成功");
            response.getWriter().println(message);
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message","数据库异常");
            response.getWriter().println(message);
        }
        catch (Exception e) {
            e.printStackTrace();
            message.put("message","网络异常");
            response.getWriter().println(message);
        }
    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        //从请求中获取id的值
        String id_str = request.getParameter("id");
        //转换成int
        int id = Integer.parseInt(id_str);
        JSONObject message = new JSONObject();
        try {
            //删除方法根据id
            CourseService.getInstance().delete(id);
            message.put("message","删除成功");
            response.getWriter().println(message);
        } catch (SQLException e) {
            //抓取错误
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            response.getWriter().println(message);
        } catch (Exception e){
            //抓取错误
            message.put("message", "网络异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        //从请求中获取json数据
        String course_json = JSONUtil.getJSON(request);
        //将json数据转换成对象
        Course courseToUpdate = JSON.parseObject(course_json, Course.class);
        JSONObject message = new JSONObject();
        try {
            //调用修改方法
            CourseService.getInstance().update(courseToUpdate);
            message.put("message", "修改成功");
            response.getWriter().println(message);
        } catch (SQLException e) {
            //抓取错误信息
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            response.getWriter().println(message);
        } catch (Exception e) {
            //抓取错误信息
            message.put("message", "网络异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }
    }
    }

