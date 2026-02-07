package in.capp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class Home 
{
	
    @RequestMapping("/test/hello")
	public String show()
	{
		return "test";
	}

    @RequestMapping("/test/ajax_test")
	public String testPage()
	{
		return "test";
	}
    
    @RequestMapping("/test/get_time")
    @ResponseBody
   	public String getServerTime()
   	{
    	return new Date().toString();
   	}

}
