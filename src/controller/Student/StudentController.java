package controller.Student;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.Student;
import service.StudentService;
import util.JSONUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/student.ctl")
public class StudentController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String student_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Student对象
        Student studentToAdd = JSON.parseObject(student_json, Student.class);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Student对象
        boolean ifAdd = StudentService.getInstance().add(studentToAdd);
        if (ifAdd==true){
            message.put("message", "增加成功");}
        else {
            message.put("message","添加失败");
        }
        //响应message到前端
        response.getWriter().println(message);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有教师对象，否则响应id指定的教师对象
            if (id_str == null) {
                responseStudents(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseStudent(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        String student_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Student对象
        Student studentToAdd = JSON.parseObject(student_json, Student.class);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Student对象对应的记录
        try {
            StudentService.getInstance().update(studentToAdd);
            message.put("message", "修改成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        Student student = null;
        try {
            student = StudentService.getInstance().find(id);
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message","找不到相应id老师");
        }
        boolean ifAdd = StudentService.getInstance().delete(student);
        if (ifAdd==true){
            message.put("message", "增加成功");}
        else {
            message.put("message","添加失败");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    private void responseStudent(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Student student = StudentService.getInstance().find(id);
        String student_json = JSON.toJSONString(student);
        //响应student_json到前端
        response.getWriter().println(student_json);
    }

    private void responseStudents(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Collection<Student> students = StudentService.getInstance().findAll();
        String students_json = JSON.toJSONString(students, SerializerFeature.DisableCircularReferenceDetect);
        //响应students_json到前端
        response.getWriter().println(students_json);
    }
}
