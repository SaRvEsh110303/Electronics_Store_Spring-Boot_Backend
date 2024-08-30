package com.sarz.electronic.store;

import com.sarz.electronic.store.entities.Roles;
import com.sarz.electronic.store.repositories.RolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RolesRepo rolesRepo;
	@Value("${normal.role.id}")
	private String roleNormalID;
	@Value("${admin.role.id}")
	private String roleAdminID;
	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("Sarz@2003"));

		try{
			Roles roleNormal = Roles.builder().roleId(roleNormalID).roleName("ROLE_NORMAL").build();
			Roles roleAdmin = Roles.builder().roleId(roleAdminID).roleName("ROLE_ADMIN").build();
			 rolesRepo.save(roleNormal);
			 rolesRepo.save(roleAdmin);

		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
