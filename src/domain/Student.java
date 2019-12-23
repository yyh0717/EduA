package domain;


import java.io.Serializable;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */

public class Student extends Actor implements Comparable<Teacher>, Serializable
{
	/**
	 * <!-- begin-user-doc -->
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	
	private String studentNo;

	/**
	 * <!-- begin-user-doc -->
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	
	public Department department;

	/**
	 * <!-- begin-user-doc -->
	 * <!--  end-user-doc  -->
	 * @generated
	 */
	public Student(){
		super();
	}

	public Student(int id,String studentNo,String name,String IDCard,String phoneNumber, Department department) {
		this.id = id;
		this.studentNo = studentNo;
		this.name = name;
		this.IDCard = IDCard;
		this.phoneNumber = phoneNumber;
		this.department = department;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	public int compareTo(Teacher o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}
}

