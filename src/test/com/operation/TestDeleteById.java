package test.com.operation;

import in.capp.dao.UserDaoImpl;
import in.capp.domain.User;

public class TestDeleteById {

	
	public static void main(String args[])
	{
			
		UserDaoImpl ud=new UserDaoImpl();
		
		ud.delete(4);;
		
		System.out.println("deleted......");
	}
}

