package domain;


import java.io.Serializable;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */

public class Teacher extends Actor implements Comparable<Teacher>, Serializable
{
	private String teacherNo;
	public Department department;
	public Degree degree;
	public Title title;
	public Teacher(){
		super();
	}

	public String getTeacherNo() {
		return teacherNo;
	}

	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public Teacher(int id,String teacherNo,String name,String IDCard,String phoneNumber,
				   Department department, Degree degree, Title title) {
		this.id=id;
		this.teacherNo = teacherNo;
		this.name = name;
		this.IDCard = IDCard;
		this.phoneNumber = phoneNumber;
		this.department = department;
		this.degree = degree;
		this.title = title;
	}
	public int compareTo(Teacher o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}
}

