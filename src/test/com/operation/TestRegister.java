package test.com.operation;

import in.capp.domain.User;
import in.capp.service.UserService;
import in.capp.service.UserServiceImpl;

public class TestRegister {

	public static void main(String args[])
	{
	
	    User u=new User();
		u.setName("deepak");
		
		u.setPhone("9639079236");
		u.setEmail("ds1791604@gmail.com");
		u.setAddress("rampur");
		u.setLoginName("deepak123");
		u.setPassword("deepak123");
		u.setRole(UserService.ROLE_ADMIN);
		u.setLoginStatus(UserService.LOGIN_STATUS_ACTIVE);
		
	
       UserServiceImpl service=new UserServiceImpl();
		service.register(u);
		 
       System.out.println("register");
	}
}
