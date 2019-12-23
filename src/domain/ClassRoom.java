package domain;


import java.io.Serializable;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */

public class ClassRoom implements Comparable<ClassRoom>, Serializable {
	/**
	 * <!-- begin-user-doc -->
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	
	private int id;

	/**
	 * <!-- begin-user-doc -->
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	
	private String no;

	/**
	 * <!-- begin-user-doc -->
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	
	private int maximumNumber;

	/**
	 * <!-- begin-user-doc -->
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	
	public TeachingBuilding teachingBuilding;

	/**
	 * <!-- begin-user-doc -->
	 * <!--  end-user-doc  -->
	 * @generated
	 */
	public ClassRoom(){
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!--  end-user-doc  -->
	 * @generated
	 * @ordered
	 */
	
	public int getFloor() {
		// TODO implement me
		return 0;
	}
	public ClassRoom(Integer id, String no,Integer maximumNumber,TeachingBuilding teachingBuilding) {
		this(no,maximumNumber,teachingBuilding);
		this.id = id;
	}
	public ClassRoom(String no,Integer maximumNumber,TeachingBuilding teachingBuilding) {
		this.no = no;
		this.maximumNumber = maximumNumber;
		this.teachingBuilding = teachingBuilding;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getMaximumNumber() {
		return maximumNumber;
	}

	public void setMaximumNumber(int maximumNumber) {
		this.maximumNumber = maximumNumber;
	}

	public TeachingBuilding getTeachingBuilding() {
		return teachingBuilding;
	}

	public void setTeachingBuilding(TeachingBuilding teachingBuilding) {
		this.teachingBuilding = teachingBuilding;
	}
	public int compareTo(ClassRoom o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}
}

