package model;


public class QuestionItemInfo{
	int grade;
	int time;
	int score;
	String question;
	String bitmap_url;
	int user_icon;
	String user_name;
	int user_cnt;
	public void setGrade(int grade){
		this.grade = grade;
	}
	public int getGrade(){
		return grade;
	}
	public void setTime(int time){
		this.time=time;
	}
	public int getTime(){
		return time;
	}
	public void setQuestion(String question){
		this.question = question;
	}
	public String getQuestion(){
		return question;
	}
	
	public void setUsername(String user_name){
		this.user_name = user_name;
	}
	public String getUsername(){
		return user_name;
	}
	public void setBitmapUrl(String bitmap_url){
		this.bitmap_url = bitmap_url;
	}
	public String getBitmapUrl(){
		return bitmap_url;
	}
	public void setUserIcon(int user_icon){
		this.user_icon=user_icon;
	}
	public int getUserIcon(){
		return user_icon;
	}
	public void setUserCnt(int user_cnt){
		this.user_cnt = user_cnt;
	}
}