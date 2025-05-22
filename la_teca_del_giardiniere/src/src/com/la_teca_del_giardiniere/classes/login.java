package src.com.la_teca_del_giardiniere.classes;

public class login {
	private String nome; 
	private String password;
	
	
	public login(){
		
	}
	
	public login(String nome, String password) {
		super();
		this.nome = nome;
		this.password = password;
	}




	public String getNome() {
		return nome;
	}




	public void setNome(String nome) {
		this.nome = nome;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	@Override
	public String toString() {
		return "login [nome=" + nome + ", password=" + password + "]";
	}
	
}


