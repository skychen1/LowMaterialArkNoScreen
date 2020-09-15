package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/19 17:17
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public  class Movie implements Serializable {

   public  String         one;
   public  String         two;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public  boolean         isDelete;

   public String getOne() {
	return one;
   }

   public void setOne(String one) {
	this.one = one;
   }

   public String getTwo() {
	return two;
   }

   public void setTwo(String two) {
	this.two = two;
   }

   public String getThree() {
	return three;
   }

   public void setThree(String three) {
	this.three = three;
   }

   public String getFour() {
	return four;
   }

   public void setFour(String four) {
	this.four = four;
   }

   public String getFive() {
	return five;
   }

   public void setFive(String five) {
	this.five = five;
   }

   public String getSix() {
	return six;
   }

   public void setSix(String six) {
	this.six = six;
   }

   public String getSeven() {
	return seven;
   }

   public void setSeven(String seven) {
	this.seven = seven;
   }

   public String getEight() {
	return eight;
   }

   public void setEight(String eight) {
	this.eight = eight;
   }

   public int getNumber1() {
	return number1;
   }

   public void setNumber1(int number1) {
	this.number1 = number1;
   }

   public int getNumber2() {
	return number2;
   }

   public void setNumber2(int number2) {
	this.number2 = number2;
   }

   public int getNumber3() {
	return number3;
   }

   public void setNumber3(int number3) {
	this.number3 = number3;
   }

   public  String         three;
   public  String         four;
   public  String         five;
   public  String         six;
   public  String         seven;
   public  String         eight;
   private List<ListBean> list;
   public  String         mString;
   public List<ListBean> getList() { return list;}

   public void setList(List<ListBean> list) { this.list = list;}
   public String getString() { return mString;}

   public void setString(String title) { this.mString = title;}

   public int number1;
   public int number2;
   public int number3;

   public Movie(
	   String one, String two, String three, String four, String five, String six, String seven,
	   String eight) {
	this.one = one;
	this.two = two;
	this.three = three;
	this.four = four;
	this.five = five;
	this.six = six;
	this.seven = seven;
	this.eight = eight;

   }
   public Movie(
	   int one, int two, int three) {
	this.number1 = one;
	this.number2 = two;
	this.number3 = three;

   }
   public Movie(String one) {
	this.mString = one;


   }
   public static class ListBean {
	public String mString;

	public String getString() { return mString;}

	public void setString(String title) { this.mString = title;}

   }
}
