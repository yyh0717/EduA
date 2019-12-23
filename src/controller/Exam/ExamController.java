package controller.Exam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.Exam;
import service.ExamService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/exam.ctl")
public class ExamController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String id_str = request.getParameter("id");
        String id_student = request.getParameter("studentId");
        String id_teaching = request.getParameter("teachingId");
        JSONObject message = new JSONObject();
        try {
            if (id_str!=null||id_student!=null||id_teaching!=null){
                if(id_str!=null){
                    int id = Integer.parseInt(id_str);
                    Exam exam = ExamService.getInstance().find(id);
                    String exam_str = JSON.toJSONString(exam);
                    response.getWriter().println(exam_str);
                }else if (id_student!=null){
                    int student_id = Integer.parseInt(id_student);
                    Collection<Exam> examsByStudent = ExamService.getInstance().findByStudent(student_id);
                    String examsByStudent_str = JSON.toJSONString(examsByStudent, SerializerFeature.DisableCircularReferenceDetect);
                    response.getWriter().println(examsByStudent_str);
                }else if (id_teaching!=null){
                    int teaching_id =Integer.parseInt(id_teaching);
                    Collection<Exam> exams = ExamService.getInstance().findByTeaching(teaching_id);
                    String exams_str = JSON.toJSONString(exams,SerializerFeature.DisableCircularReferenceDetect);
                    response.getWriter().println(exams_str);
                }
            }else {
                Collection<Exam> examsOfAll = ExamService.getInstance().findAll();
                String examsOfAll_str = JSON.toJSONString(examsOfAll,SerializerFeature.DisableCircularReferenceDetect);
                response.getWriter().println(examsOfAll_str);
            }
        }catch (SQLException e){
            e.printStackTrace();
            message.put("message","数据库异常");
            response.getWriter().println(message);
        }catch (Exception e){
            e.printStackTrace();
            message.put("message","网络异常");
            response.getWriter().println(message);
        }

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exam_str = JSONUtil.getJSON(request);
        Exam exam = JSONObject.parseObject(exam_str,Exam.class);
        JSONObject message = new JSONObject();
        try {
            ExamService.getInstance().update(exam);
            message.put("message","更新成功");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message","数据库异常");
            response.getWriter().println(message);
        }
        catch (Exception e){
            e.printStackTrace();
            message.put("message","网络异常");
            response.getWriter().println(message);
        }
    }
}
