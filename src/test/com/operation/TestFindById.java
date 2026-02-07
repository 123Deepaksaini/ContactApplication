package test.com.operation;

import in.capp.dao.UserDaoImpl;
import in.capp.domain.User;

public class TestFindById {

	
	public static void main(String args[])
	{
	
		UserDaoImpl ud=new UserDaoImpl();
		
		User u=ud.findById(6);
		System.out.println("details......");
		System.out.println(u.getName());
		System.out.println(u.getEmail());
		System.out.println(u.getPhone());
		System.out.println(u.getLoginName());
		System.out.println(u.getLoginStatus());
		System.out.println(u.getRole());		
	}
}

