package controller.Teaching;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.Teaching;
import service.TeachingService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
@WebServlet("/teaching.ctl")
public class TeachingController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String teaching_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teaching对象
        Teaching teachingToAdd = JSON.parseObject(teaching_json, Teaching.class);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Teaching对象
        try {
            TeachingService.getInstance().add(teachingToAdd);
            message.put("message", "增加成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
        } catch (Exception e) {
            message.put("message", "网络异常");
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
            //如果id = null, 表示响应所有开课对象，否则响应id指定的开课对象
            if (id_str == null) {
                responseTeachings(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseTeaching(id, response);
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的开课
        try {
            TeachingService.getInstance().delete(id);
            message.put("message", "删除成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        String teaching_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teaching对象
        Teaching teachingToAdd = JSON.parseObject(teaching_json, Teaching.class);

        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Teaching对象对应的记录
        try {
            TeachingService.getInstance().update(teachingToAdd);
            message.put("message", "修改成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
        } catch (Exception e) {
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    //响应一个开课对象
    private void responseTeaching(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找开课
        Teaching teaching = TeachingService.getInstance().find(id);
        String teaching_json = JSON.toJSONString(teaching);
        //响应teaching_json到前端
        response.getWriter().println(teaching_json);
    }

    //响应所有开课对象
    private void responseTeachings(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有开课
        Collection<Teaching> teachings = TeachingService.getInstance().findAll();
        String teachings_json = JSON.toJSONString(teachings, SerializerFeature.DisableCircularReferenceDetect);
        //响应teachings_json到前端
        response.getWriter().println(teachings_json);
    }
}
