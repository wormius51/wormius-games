package wormius.games.wormiusgames.helpers;

public class PageMaker implements IPageMaker {

	@Override
	public String resetPasswordPage(String resetToken) {
		return String.format(page("resetPassword"), resetToken) ;
	}
	
	
	
	private String page(String name) {
		String s = "";
		switch (name) {
		case "resetPassword":
			s = 
			"<html>"
			+ "<h1>Reset Password</h1>"
			+ "<h2>enter new password</h2>"
			+ "<form action = '/user/resetPassword?resetToken=%s' method = 'post'>"
			+ "<input name = 'password' type = 'password'>"
			+ "<input type = 'submit'>"
			+ "</form>"
			+ "</html>";
			break;

		default:
			break;
		}
		
		return s;
	}

}
