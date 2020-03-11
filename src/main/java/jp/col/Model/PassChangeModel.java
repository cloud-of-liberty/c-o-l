package jp.col.Model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PassChangeModel {
 
    private String email;
	@NotBlank(message = "現在のパスワードを入力してください。")
    private String currentPassword;
	@NotBlank(message = "新しいパスワードを入力してください。")
	@Size(min = 6, max = 20 , message = "新しいパスワードは6文字以上20文字以下半角英数字で入力してください。")
	@Pattern(regexp ="^[A-Za-z0-9]+$" , message = "新しいパスワードは半角英数字で入力してください。")
    private String newPassword;
	@NotBlank(message = "パスワード（確認入力）を入力してください。")
	private String passwordConfirm;
 
	public void setEmail(String email) {
        this.email = email;
    }
 
    public String getEmail() {
        return email;
    }
  
    public String getCurrentPassword() {
        return currentPassword;
    }
 
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}