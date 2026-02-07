package in.capp.service;

import java.util.List;

import in.capp.domain.Contact;

public interface ContactService {

	public void save(Contact c);
	public void update(Contact c);
	public void delete(Integer contactId);
	public void delete(Integer contactIds[]);
	public Contact findById(Integer contactId);
	
	public List<Contact> findUserContact(Integer userId);

	public List<Contact> findUserContact(Integer userId,String text);


}
