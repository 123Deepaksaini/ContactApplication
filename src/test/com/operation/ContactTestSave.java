package test.com.operation;

import in.capp.dao.ContactDaoImpl;
import in.capp.domain.Contact;

public class ContactTestSave {

	public static void main(String args[])
	{
	Contact c=new Contact();
	 c.setUserId(6); 
	 c.setName("deepak");
	c.setPhone("9639079236");
	 c.setEmail("ds1791604@gmail.com");
	 c.setAddress("Rampur");
	 c.setRemark("school friend");
    ContactDaoImpl cot=new ContactDaoImpl();
    cot.save(c);
	
	}
	
}
