package domain;

import java.io.Serializable;

public class Teaching  implements Comparable<Teaching>, Serializable {
	private int id;
	private int minimumNumber;

	private int startWeek;
	private int endWeek;
	public Course course;
	public Teacher teacher;
	public Term term;
	public ClassRoom classRoom;
	public Periods periods;
	public Teaching(){
		super();
	}

	public Teaching(int minimumNumber, int startWeek, int endWeek, Course course,
					Teacher teacher, Term term, ClassRoom classRoom, Periods periods) {
		this.minimumNumber = minimumNumber;
		this.startWeek = startWeek;
		this.endWeek = endWeek;
		this.course = course;
		this.teacher = teacher;
		this.term = term;
		this.classRoom = classRoom;
		this.periods = periods;
	}

	public Teaching(int id, int minimumNumber, int startWeek, int endWeek, Course course,
					Teacher teacher, Term term, ClassRoom classRoom, Periods periods) {
		this(minimumNumber,startWeek,endWeek,course,teacher,term,classRoom,periods);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinimumNumber() {
		return minimumNumber;
	}

	public void setMinimumNumber(int minimumNumber) {
		this.minimumNumber = minimumNumber;
	}

	public int getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}

	public int getEndWeek() {
		return endWeek;
	}

	public void setEndWeek(int endWeek) {
		this.endWeek = endWeek;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public ClassRoom getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(ClassRoom classRoom) {
		this.classRoom = classRoom;
	}

	public Periods getPeriods() {
		return periods;
	}

	public void setPeriods(Periods periods) {
		this.periods = periods;
	}
	public int compareTo(Teaching o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}
}

