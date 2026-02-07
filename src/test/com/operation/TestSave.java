package test.com.operation;

import in.capp.dao.UserDaoImpl;
import in.capp.domain.User;

public class TestSave {

	public static void main(String args[])
	{
	
		User u=new User();
		u.setName("deepak");
		
		u.setPhone("9639079236");
		u.setEmail("ds1791604@gmail.com");
		u.setAddress("rampur");
		u.setRole(1);
		u.setLoginStatus(2);
		u.setLoginName("deepak1234");
		u.setPassword("deepak1234");
	
      UserDaoImpl ud=new UserDaoImpl();
		
		ud.save(u);
	}
}
